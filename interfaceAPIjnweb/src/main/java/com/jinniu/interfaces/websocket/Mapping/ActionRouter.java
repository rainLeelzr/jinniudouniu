package com.jinniu.interfaces.websocket.Mapping;

import com.jinniu.commonjn.manager.getACard.GetACardManager;
import com.jinniu.commonjn.manager.operate.CanDoOperate;
import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.putOutCard.AfterPutOutCardManager;
import com.jinniu.commonjn.manager.qiangGang.QiangGangManager;
import com.jinniu.commonjn.model.Room;
import com.jinniu.commonjn.model.RoomMember;
import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.model.mahjong.*;
import com.jinniu.commonjn.model.mahjong.vo.*;
import com.jinniu.commonjn.redis.GameRedis;
import com.jinniu.commonjn.redis.VersionRedis;
import com.jinniu.commonjn.redis.base.Redis;
import com.jinniu.commonjn.service.RoomService;
import com.jinniu.commonjn.service.UserService;
import com.jinniu.commonjn.service.impl.GameService;
import com.jinniu.commonjn.util.*;
import com.jinniu.interfaces.monitor.MonitorManager;
import com.jinniu.interfaces.monitor.clientTouchMahjong.task.ClientTouchMahjongTask;
import com.jinniu.interfaces.monitor.clientTouchMahjong.toucher.CommonToucher;
import com.jinniu.interfaces.monitor.clientTouchMahjong.toucher.GangToucher;
import com.jinniu.interfaces.websocket.MessageManager;
import com.jinniu.interfaces.websocket.SessionManager;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

/**
 * 客户端接入到此类的某一个方法
 */
@Component
public class ActionRouter {

    Logger log = LoggerFactory.getLogger(ActionRouter.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameService gameService;

    @Autowired
    private Redis redis;

    @Autowired
    private MonitorManager monitorManager;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private GetACardManager getACardManager;

    @Autowired
    private AfterPutOutCardManager afterPutOutCardManager;

    @Autowired
    private QiangGangManager qiangGangManager;

    @Autowired
    private GameRedis gameRedis;

    @Autowired
    private VersionRedis versionRedis;

    private User getLastPutOutCardUser(MahjongGameData mahjongGameData) {
        return getUserByUserId(
                mahjongGameData
                        .getOutCards()
                        .get(mahjongGameData.getOutCards().size() - 1)
                        .getRoomMember()
                        .getUserId()
        );
    }

    private List<User> getRoomUsers(List<PersonalCardInfo> personalCardInfos) {
        List<User> users = new ArrayList<>(personalCardInfos.size());
        for (PersonalCardInfo personalCardInfo : personalCardInfos) {
            Integer userId = personalCardInfo.getRoomMember().getUserId();
            users.add(getUserByUserId(userId));
        }
        return users;
    }

    /**
     * 本轮玩家已经出牌，获取下一个出牌的玩家
     *
     * @param currentUser 本轮已经出牌的玩家
     */
    private User getNextTouchMahjongUser(MahjongGameData mahjongGameData, User currentUser) {
        // 本轮已经出牌的座位号
        Integer userSeat = null;
        for (PersonalCardInfo personalCardInfo : mahjongGameData.getPersonalCardInfos()) {
            if (personalCardInfo.getRoomMember().getUserId().equals(currentUser.getId())) {
                userSeat = personalCardInfo.getRoomMember().getSeat();
                break;
            }
        }

        // 获取下一个座位号
        Integer next = userSeat + 1;
        // 如果座位号next大于玩家人数，则座位号改为1，从头开始
        if (next > mahjongGameData.getPersonalCardInfos().size()) {
            next = 1;
        }

        Integer nextUserId = null;
        for (PersonalCardInfo personalCardInfo : mahjongGameData.getPersonalCardInfos()) {
            if (personalCardInfo.getRoomMember().getSeat().equals(next)) {
                nextUserId = personalCardInfo.getRoomMember().getUserId();
            }
        }

        User nextUser = getUserByUserId(nextUserId);
        return nextUser;
    }

    private User getUserByUserId(Integer userId) {
        WebSocketSession session = sessionManager.getByUserId(userId);
        User user;
        if (session == null) {
            user = userService.selectOne(userId);
        } else {
            user = sessionManager.getUser(session.getId());
        }
        return user;
    }

    @Pid(PidValue.LOGIN)
    public JsonResultY login(WebSocketSession session, JSONObject data)
            throws Exception {

        // 获取微信的唯一标识id
        // 服务端验证微信用户
        String ip = IpAddressUtil.getIp(session);
        //String openId = (String) data.get("openId");
        Map<String, Object> result = userService.login(data, ip);
        //登录成功时，将此user对应的session缓存起来
        if (result != null) {
            sessionManager.userLogin((User) result.get("user"), session);

            Integer loginType = (Integer) result.get("login_type");
            if (loginType == 2) {
                sessionManager.userJoinRoom((Room) result.get(("room")), session);
                result.remove("room");
            }
        }


        return new JsonResultY.Builder()
                .setPid(PidValue.LOGIN.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.LOGOUT)
    public JsonResultY logout(WebSocketSession session, JSONObject data)
            throws Exception {

        User user = userService.logout(data);
        sessionManager.userLogout(user, session);

        return new JsonResultY.Builder()
                .setPid(PidValue.LOGOUT.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(null)
                .build();
    }

    @Pid(PidValue.GET_USER)
    @LoginResource
    public JsonResultY getUserInfo(WebSocketSession session, JSONObject data)
            throws Exception {

        User user = sessionManager.getUser(session.getId());
        Map<String, Object> result = userService.getUser(data, user);
        //String userId = (String) data.get("userId");
        //User user = userService.selectOne(Integer.parseInt(userId));
        //if (user == null) {
        //    throw  CommonError.USER_NOT_EXIST.newException();
        //}
        //User user = sessionManager.getUser(session.getId());
        //String roomId = "11";
        //String version = "1111";
        //monitorManager.watch(new PengCardMonitorTask
        //        .Builder()
        //        .setRoomId(roomId)
        //        .setUserId(userId)
        //        .setVersion(version)
        //        .build());
        return new JsonResultY.Builder()
                .setPid(PidValue.GET_USER.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.CREATE_ROOM)
    @LoginResource
    public JsonResultY createRoom(WebSocketSession session, JSONObject data)
            throws Exception {

        User user = sessionManager.getUser(session.getId());
        //Map<String, Object> result = roomService.createRoom(data,user);
        Map<String, Object> result = roomService.createRoom(data);
        if (result != null) {
            sessionManager.userJoinRoom((Room) result.get(("room")), session);
        }

        return new JsonResultY.Builder()
                .setPid(PidValue.CREATE_ROOM.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.JOIN_ROOM)
    @LoginResource
    public JsonResultY joinRoom(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());

        Map<String, Object> result = roomService.joinRoom(data, user);
        if (result != null) {
            sessionManager.userJoinRoom((Room) result.get(("room")), session);
        }
        Map<String, Object> myResult = new HashMap<>();
        Set<RoomMember> roomMembers = (Set<RoomMember>) result.get(("roomMembers"));
        if (roomMembers != null) {

            for (RoomMember roomMember : roomMembers) {
                if (roomMember.getUserId().equals((Integer) result.get("userId"))) {
                    myResult.put("roomMember", roomMember);
                }

            }
            List<User> users = (ArrayList<User>) result.get(("users"));
            for (User u : users) {
                if (u.getId().equals((Integer) result.get("userId"))) {
                    myResult.put("user", u);
                }

            }
            JsonResultY jsonResultY = new JsonResultY.Builder()
                    .setPid(PidValue.JOIN_ROOM_MESSAGE.getPid())
                    .setError(CommonError.SYS_SUSSES)
                    .setData(myResult)
                    .build();

            messageManager.sendMessageToOtherRoomUsers(
                    ((Room) result.get(("room"))).getId().toString(),
                    (Integer) result.get("userId"),
                    jsonResultY);

            result.remove("userId");
        }
        return new JsonResultY.Builder()
                .setPid(PidValue.JOIN_ROOM.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.OUT_ROOM)
    @LoginResource
    public JsonResultY outRoom(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());
        Map<String, Object> result = roomService.outRoom(data, user);
        if (result != null) {
            Integer roomId = (Integer) result.get("roomId");
            result.remove("roomId");
            JsonResultY jsonResultY = new JsonResultY.Builder()
                    .setPid(PidValue.OUT_ROOM.getPid())
                    .setError(CommonError.SYS_SUSSES)
                    .setData(result)
                    .build();
            messageManager.sendMessageToRoomUsers(roomId.toString(), jsonResultY);
            sessionManager.userExitRoom(roomId.toString(), session);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Pid(PidValue.READY)
    @LoginResource
    public JsonResultY ready(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());
        Map<String, Object> result = roomService.ready(user);
        Integer type = (Integer) result.get("type");

        boolean isFirstPutOutCard = false;
        List<Object[]> firstPutOutCardBroadcasts = new ArrayList<>(4);

        if (type == 2) {
            isFirstPutOutCard = true;

            List<FirstPutOutCard> firstPutOutCards = (List<FirstPutOutCard>) result.get(GameService.FIRST_PUT_OUT_CARD_KEY);

            MahjongGameData mahjongGameData = (MahjongGameData) result.get(MahjongGameData.class.getSimpleName());
            result.remove(MahjongGameData.class.getSimpleName());

            GameStartVo gameStartVo = (GameStartVo) result.get(GameStartVo.class.getSimpleName());
            result.remove(GameStartVo.class.getSimpleName());

            // 获取庄家uId
            Integer bankerUserId = gameStartVo.getBankerUId();
            User bankerUser = getUserByUserId(bankerUserId);

            // 4个玩家，按座位号升序
            List<User> users = new ArrayList<>(firstPutOutCards.size());

            // 广播给4个用户第一次发牌
            for (FirstPutOutCard firstPutOutCard : firstPutOutCards) {
                Map<String, Object> myResult = new HashMap<>();
                myResult.put("type", 2);

                // 需要接受广播消息的用户uid
                Integer acceptBroadcastUserId = firstPutOutCard.getuId();
                User acceptBroadcastUser = getUserByUserId(acceptBroadcastUserId);
                firstPutOutCard.setuId(acceptBroadcastUser.getUId());

                users.add(acceptBroadcastUser);

                myResult.put(GameService.FIRST_PUT_OUT_CARD_KEY, firstPutOutCard);

                JsonResultY temp = new JsonResultY.Builder()
                        .setPid(PidValue.FIRST_PUT_OUT_ALL_CARD.getPid())
                        .setError(CommonError.SYS_SUSSES)
                        .setData(myResult)
                        .build();

                Object[] broadcast = new Object[]{
                        acceptBroadcastUserId, temp
                };
                firstPutOutCardBroadcasts.add(broadcast);
            }

            // 庄家摸一张牌
            monitorManager.watch(new ClientTouchMahjongTask
                    .Builder()
                    .setToucher(new CommonToucher())
                    .setGetACardManager(getACardManager)
                    .setMessageManager(messageManager)
                    .setMahjongGameData(mahjongGameData)
                    .setUser(bankerUser)
                    .setUsers(users)
                    .setGameRedis(gameRedis)
                    .setVersionRedis(versionRedis)
                    .build());

        }
        result.remove(GameService.FIRST_PUT_OUT_CARD_KEY);
        JsonResultY jsonResultY = new JsonResultY.Builder()
                .setPid(PidValue.READY.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
        messageManager.sendMessageToRoomUsers(
                (result.get("roomId")).toString(),
                jsonResultY);

        // 如果是第一次发牌，则广播給客户端他们各自的牌
        if (isFirstPutOutCard) {
            for (Object[] firstPutOutCardBroadcast : firstPutOutCardBroadcasts) {
                messageManager.sendMessageByUserId(
                        (Integer) firstPutOutCardBroadcast[0],
                        (JsonResultY) firstPutOutCardBroadcast[1]);
            }
        }

        return null;
    }

    @Pid(PidValue.DISMISS_ROOM)
    @LoginResource
    public JsonResultY dismissRoom(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());
        Map<String, Object> result = roomService.dismissRoom(data, user);
        Integer roomId = (Integer) result.get("roomId");
        result.remove("roomId");
        JsonResultY jsonResultY = new JsonResultY.Builder()
                .setPid(PidValue.DISMISS_ROOM.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
        messageManager.sendMessageToRoomUsers(
                roomId.toString(),
                jsonResultY);

        return null;
    }

    @Pid(PidValue.AGREE_DISMISS)
    @LoginResource
    public JsonResultY agreeDismiss(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());
        Map<String, Object> result = roomService.agreeDismiss(data, user);
        Integer roomId = (Integer) result.get("roomId");
        result.remove("roomId");
        JsonResultY jsonResultY = new JsonResultY.Builder()
                .setPid(PidValue.AGREE_DISMISS.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
        messageManager.sendMessageToRoomUsers(
                roomId.toString(),
                jsonResultY);
        return null;
    }

    @Pid(PidValue.PRIZE_DRAW)
    @LoginResource
    public JsonResultY prizeDraw(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());

        Map<String, Object> result = userService.prizeDraw(data, user);
        if (!data.getBoolean("judge")) {
            sessionManager.userUpdate((User) result.get("user"), session);
        }

        return new JsonResultY.Builder()
                .setPid(PidValue.PRIZE_DRAW.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.FREE_COINS)
    @LoginResource
    public JsonResultY freeCoins(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());

        Map<String, Object> result = userService.freeCoins(user);

        sessionManager.userUpdate((User) result.get("user"), session);

        return new JsonResultY.Builder()
                .setPid(PidValue.FREE_COINS.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.BIND_PHONE)
    @LoginResource
    public JsonResultY bindPhone(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());

        Map<String, Object> result = userService.bindPhone(data, user);

        if ((User) result.get("user") != null) {
            sessionManager.userUpdate((User) result.get("user"), session);
        }

        return new JsonResultY.Builder()
                .setPid(PidValue.BIND_PHONE.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.TEN_WINS)
    @LoginResource
    public JsonResultY tenWins(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());

        Map<String, Object> result = userService.tenWins(data, user);

        if ((User) result.get("user") != null) {
            sessionManager.userUpdate((User) result.get("user"), session);
        }

        return new JsonResultY.Builder()
                .setPid(PidValue.TEN_WINS.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.GET_STANDINGS)
    @LoginResource
    public JsonResultY getStanding(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());
        Room room = sessionManager.getRoom(session.getId());
        Map<String, Object> result = userService.getStanding(room, user);

        return new JsonResultY.Builder()
                .setPid(PidValue.GET_STANDINGS.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.NUMBER_OF_PLAYERS)
    @LoginResource
    public JsonResultY numberOfPlayers(WebSocketSession session, JSONObject data)
            throws Exception {

        Map<String, Object> result = roomService.numberOfPlayers(data);

        return new JsonResultY.Builder()
                .setPid(PidValue.NUMBER_OF_PLAYERS.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.BUY)
    @LoginResource
    public JsonResultY buy(WebSocketSession session, JSONObject data)
            throws Exception {
        User user = sessionManager.getUser(session.getId());
        Map<String, Object> result = userService.buy(data, user);

        return new JsonResultY.Builder()
                .setPid(PidValue.BUY.getPid())
                .setError(CommonError.SYS_SUSSES)
                .setData(result)
                .build();
    }

    @Pid(PidValue.TEST)
    public JsonResultY test(WebSocketSession session, JSONObject data)
            throws Exception {
        // 测试数据
        Room room = new Room();
        room.setId(222);
        room.setPlayers(4);

        RoomMember[] roomMembers = new RoomMember[room.getPlayers()];
        for (int i = 0; i < room.getPlayers(); i++) {
            roomMembers[i] = new RoomMember();
            roomMembers[i].setId(i + 1);
            roomMembers[i].setUserId(i + 1);
            roomMembers[i].setJoinTime(new Date());
            roomMembers[i].setRoomId(room.getId());
            roomMembers[i].setSeat(i + 1);
            roomMembers[i].setState(RoomMember.state.UNREADY.getCode());
        }

        // 初始化游戏数据
        //Map<String, Object> mahjongGameDatas = gameService.firstPutOutCard
        //     (room, roomMembers);
        //JsonUtil.toJson(mahjongGameDatas);

        // version


        return new JsonResultY.Builder()
                .setPid(PidValue.TEST)
                .setError(CommonError.SYS_SUSSES)
                .setData(null)
                .build();
    }

    @Pid(PidValue.PLAY_A_MAHJONG)
    @LoginResource
    @SuppressWarnings("unchecked")
    public JsonResultY playACard(WebSocketSession session, JSONObject data)
            throws Exception {

        int mahjongId = JsonUtil.getInt(data, "mahjongId");
        //long version = JsonUtil.getLong(data, "version");

        Mahjong playedMahjong = Mahjong.parse(mahjongId);

        User user = sessionManager.getUser(session.getId());
        Room room = sessionManager.getRoom(session.getId());

        Map<String, Object> result = gameService.playAMahjong(room, user, playedMahjong);

        // 响应用户已经打出牌
        messageManager.send(session, new JsonResultY.Builder()
                .setPid(PidValue.PLAY_A_MAHJONG)
                .setError(CommonError.SYS_SUSSES)
                .setData(null)
                .build());

        // 玩家打牌广播
        List<PlayedMahjong> playedMahjongs = (List<PlayedMahjong>) result.get(PlayedMahjong.class.getSimpleName());
        for (PlayedMahjong mahjong : playedMahjongs) {
            int acceptUserId = mahjong.getuId();
            mahjong.setuId(getUserByUserId(acceptUserId).getUId());

            messageManager.sendMessageByUserId(acceptUserId, new JsonResultY.Builder()
                    .setPid(PidValue.OTHER_USER_PLAY_A_MAHJONG)
                    .setError(CommonError.SYS_SUSSES)
                    .setData(mahjong)
                    .build());
        }
        //gameService.putOutCard(putOutCard, room, user, version);
        gameRedis.deleteCanOperates(room.getId());

        MahjongGameData mahjongGameData = (MahjongGameData) result.get(MahjongGameData.class.getSimpleName());


        // 扫描其他用户是否有吃胡、大明杠、碰的操作
        List<CanDoOperate> canOperates =
                afterPutOutCardManager.scan(mahjongGameData, playedMahjong, user);

        if (canOperates.size() == 0) {
            handleCommonNextUserTouchAMahjong(mahjongGameData, user);
        } else {
            CanDoOperate firstCanDoOperate = canOperates.remove(0);

            handleNextCanDoOperate(mahjongGameData, firstCanDoOperate);

            // 如果还有玩家可以操作，则添加到排队列表
            if (canOperates.size() != 0) {
                gameRedis.saveCanOperates(canOperates);
            }

            //CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(firstCanDoOperate.getRoomMember().getRoomId());
        }


        return null;
    }

    @Pid(PidValue.YING_AN_GANG)
    @LoginResource
    public JsonResultY yingAnGang(WebSocketSession session, JSONObject data)
            throws Exception {

        User user = sessionManager.getUser(session.getId());
        Room room = sessionManager.getRoom(session.getId());

        List<Integer> toBeGangMahjongIds = JsonUtil.getIntegerList(data, "mahjongIds");

        List<Mahjong> mahjongs = Mahjong.parseFromIds(toBeGangMahjongIds);
        if (mahjongs.size() != 4) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        MahjongGameData mahjongGameData = gameService.yingAnGang(user, room, mahjongs);

        // 响应玩家通过暗杠验证
        messageManager.send(
                session,
                new JsonResultY.Builder()
                        .setPid(PidValue.YING_AN_GANG)
                        .setError(CommonError.SYS_SUSSES)
                        .build());

        // 广播玩家执行暗杠
        for (PersonalCardInfo personalCardInfo : mahjongGameData.getPersonalCardInfos()) {

            GangBroadcast anGangBroadcast =
                    new GangBroadcast(
                            getUserByUserId(
                                    personalCardInfo.getRoomMember().getUserId()
                            ).getUId(),
                            toBeGangMahjongIds,
                            user.getUId(),
                            PidValue.YING_AN_GANG.getPid(),
                            Mahjong.parseToIds(personalCardInfo.getHandCards()),
                            Mahjong.parseCombosToMahjongIds(personalCardInfo.getPengs()),
                            GangVo.parseFromGangCombos(personalCardInfo.getGangs())
                    );
            messageManager.sendMessageByUserId(
                    personalCardInfo.getRoomMember().getUserId(),
                    new JsonResultY.Builder()
                            .setPid(PidValue.GANG_BROADCAST)
                            .setError(CommonError.SYS_SUSSES)
                            .setData(anGangBroadcast)
                            .build());
        }

        // 4个玩家，按座位号升序
        List<User> users = getRoomUsers(mahjongGameData.getPersonalCardInfos());

        // 玩家在leftCards的开头摸一张牌，并广播
        monitorManager.watch(new ClientTouchMahjongTask
                .Builder()
                .setToucher(new GangToucher())
                .setGetACardManager(getACardManager)
                .setMessageManager(messageManager)
                .setMahjongGameData(mahjongGameData)
                .setUser(user)
                .setUsers(users)
                .setGameRedis(gameRedis)
                .setVersionRedis(versionRedis)
                .build());

        return null;
    }

    @Pid(PidValue.YING_JIA_GANG)
    @LoginResource
    public JsonResultY yingJiaGang(WebSocketSession session, JSONObject data)
            throws Exception {

        User user = sessionManager.getUser(session.getId());
        Room room = sessionManager.getRoom(session.getId());

        Integer toBeJiaGangMahjongId = JsonUtil.getInt(data, "mahjongId");

        Mahjong mahjong = Mahjong.parse(toBeJiaGangMahjongId);

        Object[] result = gameService.yingJiaGang(user, room, mahjong);
        MahjongGameData mahjongGameData = (MahjongGameData) result[0];
        Combo jiaGangCombo = (Combo) result[1];

        // 响应玩家通过硬加杠验证
        messageManager.send(
                session,
                new JsonResultY.Builder()
                        .setPid(PidValue.YING_JIA_GANG)
                        .setError(CommonError.SYS_SUSSES)
                        .build());

        // 广播玩家执行硬加杠
        for (PersonalCardInfo personalCardInfo : mahjongGameData.getPersonalCardInfos()) {
            GangBroadcast yingJiaGangBroadcast =
                    new GangBroadcast(
                            getUserByUserId(
                                    personalCardInfo.getRoomMember().getUserId()
                            ).getUId(),
                            Mahjong.parseToIds(jiaGangCombo.mahjongs),
                            user.getUId(),
                            PidValue.YING_AN_GANG.getPid(),
                            Mahjong.parseToIds(personalCardInfo.getHandCards()),
                            Mahjong.parseCombosToMahjongIds(personalCardInfo.getPengs()),
                            GangVo.parseFromGangCombos(personalCardInfo.getGangs())
                    );
            messageManager.sendMessageByUserId(
                    personalCardInfo.getRoomMember().getUserId(),
                    new JsonResultY.Builder()
                            .setPid(PidValue.GANG_BROADCAST)
                            .setError(CommonError.SYS_SUSSES)
                            .setData(yingJiaGangBroadcast)
                            .build());
        }

        // 4个玩家，按座位号升序
        List<User> users = getRoomUsers(mahjongGameData.getPersonalCardInfos());

        // 玩家在leftCards的开头摸一张牌，并广播
        monitorManager.watch(new ClientTouchMahjongTask
                .Builder()
                .setToucher(new GangToucher())
                .setGetACardManager(getACardManager)
                .setMessageManager(messageManager)
                .setMahjongGameData(mahjongGameData)
                .setUser(user)
                .setUsers(users)
                .setGameRedis(gameRedis)
                .setVersionRedis(versionRedis)
                .build());

        return null;
    }

    @Pid(PidValue.YING_DA_MING_GANG)
    @LoginResource
    public JsonResultY daMingGang(WebSocketSession session, JSONObject data) throws InstantiationException, IllegalAccessException {
        User user = sessionManager.getUser(session.getId());
        Room room = sessionManager.getRoom(session.getId());

        // 0,1,2下标是原有的麻将，3是别人打的麻将
        List<Integer> toBeYingDaMingGangMahjongId = JsonUtil.getIntegerList(data, "mahjongIds");
        if (toBeYingDaMingGangMahjongId.size() != 4) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        List<Mahjong> mahjongs = Mahjong.parseFromIds(toBeYingDaMingGangMahjongId);

        // 判断4只麻将是否一样
        if (!Mahjong.isSame(mahjongs)) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        // 别人打出的麻将
        Mahjong playedMahjong = mahjongs.get(mahjongs.size() - 1);

        Object[] result = gameService.yingDaMingGang(user, room, mahjongs);
        MahjongGameData mahjongGameData = (MahjongGameData) result[0];
        Combo yingDaMingGangCombo = (Combo) result[1];

        // 响应玩家通过大明杠执行逻辑
        messageManager.send(
                session,
                new JsonResultY.Builder()
                        .setPid(PidValue.YING_DA_MING_GANG)
                        .setError(CommonError.SYS_SUSSES)
                        .build());

        // 广播玩家执行大明杠
        for (PersonalCardInfo personalCardInfo : mahjongGameData.getPersonalCardInfos()) {
            GangBroadcast ruanJiaGangBroadcast =
                    new GangBroadcast(
                            getUserByUserId(
                                    personalCardInfo.getRoomMember().getUserId()
                            ).getUId(),
                            Mahjong.parseToIds(yingDaMingGangCombo.mahjongs),
                            user.getUId(),
                            PidValue.YING_DA_MING_GANG.getPid(),
                            Mahjong.parseToIds(personalCardInfo.getHandCards()),
                            Mahjong.parseCombosToMahjongIds(personalCardInfo.getPengs()),
                            GangVo.parseFromGangCombos(personalCardInfo.getGangs())
                    );
            messageManager.sendMessageByUserId(
                    personalCardInfo.getRoomMember().getUserId(),
                    new JsonResultY.Builder()
                            .setPid(PidValue.GANG_BROADCAST)
                            .setError(CommonError.SYS_SUSSES)
                            .setData(ruanJiaGangBroadcast)
                            .build());
        }

        // 删除clientOperateQueue
        gameRedis.deleteCanOperates(room.getId());

        // 判断其他玩家有没有抢杠
        List<CanDoOperate> canOperates =
                qiangGangManager.scan(mahjongGameData, playedMahjong, user);

        if (canOperates.size() == 0) {
            // 给杠的用户在leftCards的开头摸一张牌，并广播
            handleGangTouchAMahjong(mahjongGameData, user);
        } else {
            CanDoOperate firstCanDoOperate = canOperates.remove(0);
            PersonalCardInfo personalCardInfo = PersonalCardInfo.getPersonalCardInfo(
                    mahjongGameData.getPersonalCardInfos(),
                    firstCanDoOperate.getRoomMember().getUserId()
            );

            // 保存可操作列表到redis，记录正在等待哪个玩家的什么操作
            gameRedis.saveWaitingClientOperate(firstCanDoOperate);

            // 发送可操作列表给玩家
            ClientOperate clientOperate = new ClientOperate();
            clientOperate.setuId(getUserByUserId(firstCanDoOperate.getRoomMember().getUserId()).getUId());
            clientOperate.setOperatePids(Operate.parseToPids(firstCanDoOperate.getOperates()));
            clientOperate.setHandCardIds(Mahjong.parseToIds(personalCardInfo.getHandCards()));
            clientOperate.setPengMahjongIs(Mahjong.parseCombosToMahjongIds(personalCardInfo.getPengs()));
            clientOperate.setGangs(GangVo.parseFromGangCombos(personalCardInfo.getGangs()));
            clientOperate.setPlayedMahjongId(playedMahjong.getId());
            clientOperate.setPlayerUId(user.getUId());

            messageManager.sendMessageByUserId(firstCanDoOperate.getRoomMember().getUserId(), new JsonResultY.Builder()
                    .setPid(PidValue.CLIENT_OPERATE)
                    .setError(CommonError.SYS_SUSSES)
                    .setData(clientOperate)
                    .build());

            // 如果还有玩家可以操作，则添加到排队列表
            if (canOperates.size() != 0) {
                gameRedis.saveCanOperates(canOperates);
            }
        }
        return null;
    }

    @Pid(PidValue.YING_PENG)
    @LoginResource
    public JsonResultY peng(WebSocketSession session, JSONObject data) {
        User user = sessionManager.getUser(session.getId());
        Room room = sessionManager.getRoom(session.getId());

        // 0,1下标是原有的麻将，2是别人打的麻将
        List<Integer> toBePengMahjongIds = JsonUtil.getIntegerList(data, "mahjongIds");
        if (toBePengMahjongIds.size() != 3) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        List<Mahjong> mahjongs = Mahjong.parseFromIds(toBePengMahjongIds);

        // 判断3只麻将是否一样
        if (!Mahjong.isSame(mahjongs)) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        Object[] result = gameService.peng(user, room, mahjongs);
        MahjongGameData mahjongGameData = (MahjongGameData) result[0];
        Combo yingDaMingGangCombo = (Combo) result[1];

        // 响应玩家通过碰执行逻辑
        messageManager.send(
                session,
                new JsonResultY.Builder()
                        .setPid(PidValue.YING_PENG)
                        .setError(CommonError.SYS_SUSSES)
                        .build());

        // 广播玩家执行碰
        for (PersonalCardInfo personalCardInfo : mahjongGameData.getPersonalCardInfos()) {
            messageManager.sendMessageByUserId(
                    personalCardInfo.getRoomMember().getUserId(),
                    new JsonResultY.Builder()
                            .setPid(PidValue.PENG_BROADCAST)
                            .setError(CommonError.SYS_SUSSES)
                            .setData(new PengBroadcast(
                                    getUserByUserId(personalCardInfo.getRoomMember().getUserId()).getUId(),
                                    Mahjong.parseToIds(yingDaMingGangCombo.mahjongs),
                                    user.getUId(),
                                    PidValue.YING_PENG.getPid(),
                                    Mahjong.parseToIds(personalCardInfo.getHandCards()),
                                    Mahjong.parseCombosToMahjongIds(personalCardInfo.getPengs()),
                                    GangVo.parseFromGangCombos(personalCardInfo.getGangs())
                            ))
                            .build()
            );
        }

        // 删除clientOperateQueue
        gameRedis.deleteCanOperates(room.getId());

        return null;
    }

    @Pid(PidValue.QIANG_DA_MING_GANG_HU)
    @LoginResource
    public JsonResultY qiangDaMingGangHu(WebSocketSession session, JSONObject data) throws IllegalAccessException, InstantiationException {
        User user = sessionManager.getUser(session.getId());
        Room room = sessionManager.getRoom(session.getId());

        // 别人打出来，我抢杠的麻将
        Mahjong qiangGangMahjong = Mahjong.parse(JsonUtil.getInt(data, "mahjongId"));

        Object[] result = gameService.qiangDaMingGangHu(user, room, qiangGangMahjong);
        MahjongGameData mahjongGameData = (MahjongGameData) result[0];


        // 删除clientOperateQueue
        gameRedis.deleteCanOperates(room.getId());

        return null;
    }

    @Pid(PidValue.GUO)
    @LoginResource
    public JsonResultY guo(WebSocketSession session, JSONObject data) throws IllegalAccessException, InstantiationException {
        User user = sessionManager.getUser(session.getId());
        Room room = sessionManager.getRoom(session.getId());

        // 下一个可以操作的人
        Object[] result = gameService.guo(user, room);
        CanDoOperate nextCanDoOperate = (CanDoOperate) result[0];
        CanDoOperate waitingClientOperate = (CanDoOperate) result[1];
        MahjongGameData mahjongGameData = (MahjongGameData) result[2];

        // 通知玩家通过“过”验证
        messageManager.send(
                session,
                new JsonResultY.Builder()
                        .setPid(PidValue.GUO)
                        .setError(CommonError.SYS_SUSSES)
                        .build());

        if (nextCanDoOperate != null) {
            handleNextCanDoOperate(mahjongGameData, nextCanDoOperate);
        } else {
            // 判断发起操作链的是玩家打出一张牌，还是明杠（抢杠）
            // 如果是明杠发起的其他用户操作链，则明杠用户继续执行杠逻辑，即摸牌
            // 如果是玩家打出一张牌发起的其他用户操作链，则打出一张牌玩家的下一个玩家摸牌
            if (waitingClientOperate.getOperates().contains(Operate.QIANG_DA_MING_GANG_HU)
                    || waitingClientOperate.getOperates().contains(Operate.QIANG_JIA_GANG_HU)) {
                // 别人大明杠或加杠，自己抢大明杠或加杠的情况
                // todome 找到杠玩家的uid
                User gangUser = null;
                handleGangTouchAMahjong(mahjongGameData, gangUser);
            } else if (waitingClientOperate.getOperates().contains(Operate.CHI_YING_PENG_PENG_HU)
                    || waitingClientOperate.getOperates().contains(Operate.YING_DA_MING_GANG)
                    || waitingClientOperate.getOperates().contains(Operate.YING_PENG)) {
                // 别人打牌，自己可以吃胡、大明杠、碰的情况
                handleCommonNextUserTouchAMahjong(mahjongGameData);
            } else {
                // 自己摸牌，自摸胡、暗杠、加杠的情况，自己需要主动打一张牌
            }
        }


        return null;
    }

    /**
     * 下一个玩家摸牌逻辑
     */
    private void handleGangTouchAMahjong(MahjongGameData mahjongGameData, User gangUser) {
        // 4个玩家，按座位号升序
        List<User> users = getRoomUsers(mahjongGameData.getPersonalCardInfos());

        // 给杠的用户在leftCards的开头摸一张牌，并广播
        monitorManager.watch(new ClientTouchMahjongTask
                .Builder()
                .setToucher(new GangToucher())
                .setGetACardManager(getACardManager)
                .setMessageManager(messageManager)
                .setMahjongGameData(mahjongGameData)
                .setUser(gangUser)
                .setUsers(users)
                .setGameRedis(gameRedis)
                .setVersionRedis(versionRedis)
                .build());
    }

    /**
     * 下一个玩家摸牌逻辑
     */
    private void handleCommonNextUserTouchAMahjong(MahjongGameData mahjongGameData) {
        User currentUser = getLastPutOutCardUser(mahjongGameData);

        handleCommonNextUserTouchAMahjong(mahjongGameData, currentUser);
    }

    /**
     * 下一个玩家摸牌逻辑
     */
    private void handleCommonNextUserTouchAMahjong(MahjongGameData mahjongGameData, User currentUser) {
        // 4个玩家，按座位号升序
        List<User> users = getRoomUsers(mahjongGameData.getPersonalCardInfos());

        // 一个玩家出牌后，轮到下一个玩家摸牌
        User nextUser = getNextTouchMahjongUser(mahjongGameData, currentUser);

        // 下一个玩家摸一张牌
        monitorManager.watch(new ClientTouchMahjongTask
                .Builder()
                .setToucher(new CommonToucher())
                .setGetACardManager(getACardManager)
                .setMessageManager(messageManager)
                .setMahjongGameData(mahjongGameData)
                .setUser(nextUser)
                .setUsers(users)
                .setGameRedis(gameRedis)
                .setVersionRedis(versionRedis)
                .build());
    }

    /**
     * 处理操作链中，通知下一个人的操作
     */
    private void handleNextCanDoOperate(MahjongGameData mahjongGameData, CanDoOperate nextCanDoOperate) {
        // 可以操作的人
        User beOperateUser = getUserByUserId(nextCanDoOperate.getRoomMember().getUserId());

        for (PersonalCardInfo cardInfo : mahjongGameData.getPersonalCardInfos()) {
            ClientOperate clientOperate = new ClientOperate();
            clientOperate.setuId(beOperateUser.getUId());
            clientOperate.setOperatePids(Operate.parseToPids(nextCanDoOperate.getOperates()));
            clientOperate.setHandCardIds(Mahjong.parseToIds(cardInfo.getHandCards()));
            clientOperate.setPengMahjongIs(Mahjong.parseCombosToMahjongIds(cardInfo.getPengs()));
            clientOperate.setGangs(GangVo.parseFromGangCombos(cardInfo.getGangs()));
            clientOperate.setPlayedMahjongId(nextCanDoOperate.getSpecialMahjong().getId());
            if (nextCanDoOperate.getOperates().contains(Operate.QIANG_DA_MING_GANG_HU) ||
                    nextCanDoOperate.getOperates().contains(Operate.QIANG_JIA_GANG_HU)) {
                // todome 抢杠时，找到明杠玩家的uid
            } else {
                // 非抢杠时，找到上次出牌的玩家的uid
                clientOperate.setPlayerUId(
                        getUserByUserId(
                                mahjongGameData
                                        .getOutCards()
                                        .get(mahjongGameData.getOutCards().size() - 1)
                                        .getRoomMember().getUserId())
                                .getUId());
            }

            messageManager.sendMessageByUserId(cardInfo.getRoomMember().getUserId(), new JsonResultY.Builder()
                    .setPid(PidValue.CLIENT_OPERATE)
                    .setError(CommonError.SYS_SUSSES)
                    .setData(clientOperate)
                    .build());
        }

        nextCanDoOperate.getOperates().add(Operate.GUO);
        gameRedis.saveWaitingClientOperate(nextCanDoOperate);
    }

}
