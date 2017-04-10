package com.jinniu.interfaces.monitor.clientTouchMahjong.task;

import com.jinniu.commonjn.manager.getACard.GetACardManager;
import com.jinniu.commonjn.manager.operate.CanDoOperate;
import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.model.RoomMember;
import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.model.mahjong.vo.ClientTouchMahjong;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.MahjongGameData;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;
import com.jinniu.commonjn.model.mahjong.vo.GangVo;
import com.jinniu.commonjn.redis.GameRedis;
import com.jinniu.commonjn.redis.VersionRedis;
import com.jinniu.interfaces.monitor.MonitorTask;
import com.jinniu.interfaces.monitor.clientTouchMahjong.task.callback.success.TouchMahjongSender;
import com.jinniu.interfaces.monitor.clientTouchMahjong.toucher.Toucher;
import com.jinniu.interfaces.websocket.MessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 客户端摸牌操作
 * 从剩下的牌中抽出最后一张牌，并判断玩家可以的操作，返回给给客户端
 */
public class ClientTouchMahjongTask implements MonitorTask {
    private static final Logger log = LoggerFactory.getLogger(MonitorTask.class);
    /**
     * 监控任务成功时，执行的方法
     */
    protected Runnable successCallback;
    /**
     * 监控任务失败时，执行的方法
     */
    protected Runnable failCallback;
    /**
     * 监控任务结束后，不管任务成功与否，都需要执行的回调方法
     */
    protected Runnable finishCallback;
    private Toucher toucher;
    private String taskName;
    private GetACardManager getACardManager;
    private MahjongGameData mahjongGameData;
    private User user;// 摸牌的玩家
    private List<User> users;// 4个玩家，按座位号升序
    private CanDoOperate canOperate;// 扫描出来摸到牌的玩家可以的操作
    private List<ClientTouchMahjong> clientTouchMahjongs;
    private MessageManager messageManager;
    private GameRedis gameRedis;
    private VersionRedis versionRedis;

    @Override
    public String getTaskName() {
        return taskName;
    }

    @Override
    public void run() {
        try {
            toucher.setLeftCards(mahjongGameData.getLeftCards());
            Mahjong touchMahjong = toucher.touch();

            // 扫描摸到牌的人可以的操作
            try {
                List<CanDoOperate> canOperates = getACardManager.scan(
                        mahjongGameData,
                        touchMahjong,
                        user
                );

                Long nextVersion = versionRedis.nextVersion(mahjongGameData.getRoomId());
                mahjongGameData.setVersion(nextVersion);

                // 摸牌的人的roomMember
                RoomMember touchMahjongRoomMember = null;

                // 封装4个ClientTouchMahjong对象，分别发给对应的客户端
                clientTouchMahjongs = new ArrayList<>(mahjongGameData.getPersonalCardInfos().size());
                for (PersonalCardInfo personalCardInfo : mahjongGameData.getPersonalCardInfos()) {
                    User personalCardInfoUser = null;
                    for (User tempUser : users) {
                        // 获取此手牌的user
                        if (personalCardInfoUser == null &&
                                tempUser.getId().equals(personalCardInfo.getRoomMember().getUserId())) {
                            personalCardInfoUser = tempUser;
                        }

                        // 获取摸牌的人的roomMember
                        if (touchMahjongRoomMember == null &&
                                user.getId().equals(personalCardInfo.getRoomMember().getUserId())) {
                            touchMahjongRoomMember = personalCardInfo.getRoomMember();
                        }

                        if (personalCardInfoUser != null && touchMahjongRoomMember != null) {
                            break;
                        }
                    }
                    // 此personalCardInfo是否摸麻将的人
                    boolean isToucher = personalCardInfoUser.getId().equals(user.getId());

                    ClientTouchMahjong clientTouchMahjong = new ClientTouchMahjong();
                    clientTouchMahjong.setVersion(mahjongGameData.getVersion());
                    clientTouchMahjong.setLeftCardCount(mahjongGameData.getLeftCards().size());
                    clientTouchMahjong.setuId(personalCardInfoUser.getUId());
                    clientTouchMahjong.setHandCardIds(Mahjong.parseToIds(personalCardInfo.getHandCards()));
                    clientTouchMahjong.setTouchMahjongId(isToucher ? touchMahjong.getId() : null);
                    clientTouchMahjong.setTouchMahjongUId(user.getUId());

                    clientTouchMahjong.setPengMahjongIds(Mahjong.parseCombosToMahjongIds(personalCardInfo.getPengs()));
                    clientTouchMahjong.setGangs(GangVo.parseFromGangCombos(personalCardInfo.getGangs()));
                    if (isToucher && canOperates.size() != 0) {
                        clientTouchMahjong.setOperatePids(Operate.parseToPids(canOperates.get(0).getOperates()));
                    } else {
                        clientTouchMahjong.setOperatePids(Collections.<Integer>emptyList());
                    }

                    if (isToucher) {
                        personalCardInfo.setTouchMahjong(touchMahjong);
                    }

                    clientTouchMahjongs.add(clientTouchMahjong);
                }


                // redis的剩余牌去掉
                gameRedis.saveMahjongGameData(mahjongGameData);

                // canOperate没有自摸、杠、碰的操作
                if (canOperates.size() == 0) {
                    canOperate = new CanDoOperate();
                    canOperate.setRoomMember(touchMahjongRoomMember);
                    canOperate.setOperates(new HashSet<Operate>(2));
                } else {
                    canOperate = canOperates.get(0);
                }
                // 添加可以打牌操作
                canOperate.getOperates().add(Operate.PLAY_A_MAHJONG);
                // 保存可操作列表到redis，记录正在等待哪个玩家的什么操作
                gameRedis.saveWaitingClientOperate(canOperate);

                CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(canOperate.getRoomMember().getRoomId());

                setSuccessCallback(new TouchMahjongSender
                        .Builder()
                        .setClientTouchMahjongs(clientTouchMahjongs)
                        .setUsers(users)
                        .setMessageManager(messageManager)
                        .build());

                success();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                fail();
            }
            finish();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    @Override
    public void setSuccessCallback(Runnable success) {
        this.successCallback = success;
    }

    @Override
    public void setFailCallback(Runnable fail) {

    }

    @Override
    public void setFinishCallback(Runnable finish) {

    }

    private void success() {
        if (successCallback != null) {
            successCallback.run();
        }
    }

    private void fail() {
        if (failCallback != null) {
            failCallback.run();
        }
    }

    private void finish() {
        if (finishCallback != null) {
            finishCallback.run();
        }
    }

    public static class Builder {

        /**
         * 任务名称
         */
        private String taskName = "客户端摸牌";

        private ClientTouchMahjongTask task;

        public Builder() {
            task = new ClientTouchMahjongTask();
            task.taskName = taskName;
        }

        public Builder setTaskName(String taskName) {
            task.taskName = taskName;
            return this;
        }

        public Builder setToucher(Toucher toucher) {
            task.toucher = toucher;
            return this;
        }

        public Builder setGetACardManager(GetACardManager getACardManager) {
            task.getACardManager = getACardManager;
            return this;
        }

        public Builder setMahjongGameData(MahjongGameData mahjongGameData) {
            task.mahjongGameData = mahjongGameData;
            return this;
        }

        public Builder setMessageManager(MessageManager messageManager) {
            task.messageManager = messageManager;
            return this;
        }

        public Builder setSuccessCallback(Runnable callback) {
            task.successCallback = callback;
            return this;
        }

        public Builder setUser(User user) {
            task.user = user;
            return this;
        }

        public Builder setUsers(List<User> users) {
            task.users = users;
            return this;
        }

        public Builder setGameRedis(GameRedis gameRedis) {
            task.gameRedis = gameRedis;
            return this;
        }

        public Builder setVersionRedis(VersionRedis versionRedis) {
            task.versionRedis = versionRedis;
            return this;
        }

        public ClientTouchMahjongTask build() {
            return task;
        }
    }
}
