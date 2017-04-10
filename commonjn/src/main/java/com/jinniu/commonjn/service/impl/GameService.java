package com.jinniu.commonjn.service.impl;

import com.jinniu.commonjn.dao.RoomMemberDao;
import com.jinniu.commonjn.manager.getACard.GetACardManager;
import com.jinniu.commonjn.manager.operate.CanDoOperate;
import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.putOutCard.AfterPutOutCardManager;
import com.jinniu.commonjn.model.Room;
import com.jinniu.commonjn.model.RoomMember;
import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.model.mahjong.*;
import com.jinniu.commonjn.model.mahjong.vo.*;
import com.jinniu.commonjn.redis.GameRedis;
import com.jinniu.commonjn.redis.RoomRedis;
import com.jinniu.commonjn.redis.VersionRedis;
import com.jinniu.commonjn.util.CommonError;
import com.jinniu.commonjn.util.JsonUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

    public static final String FIRST_PUT_OUT_CARD_KEY = "firstPutOutCard";
    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    @Autowired
    private GameRedis gameRedis;

    @Autowired
    private VersionRedis versionRedis;

    @Autowired
    private AfterPutOutCardManager afterPutOutCardManager;

    @Autowired
    private GetACardManager getACardManager;

    @Autowired
    private RoomMemberDao roomMemberDao;

    @Autowired
    private RoomRedis roomRedis;

    /**
     * 判断玩家有没有执行操作的权利
     *
     * @param roomId      玩家所在的房间id
     * @param userId      玩家id
     * @param toDoOperate 玩家需要执行的操作
     */
    private void canOperate(Integer roomId, Integer userId, Operate toDoOperate) {
        // 取出等待客户端操作对象waitingClientOperate
        CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(roomId);
        if (!waitingClientOperate.getRoomMember().getUserId().equals(userId)) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }
        if (!waitingClientOperate.getOperates().contains(toDoOperate)) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }
    }

    /***
     * 初始化数据包括：骰子的点数、每个人的手牌、剩余的牌等信息。
     * 一局游戏开始时，生成麻将的初始数据。
     */
    public Map<String, Object> firstPutOutCard(
            Room room, List<RoomMember> roomMembers)
            throws InstantiationException, IllegalAccessException {

        // 对roomMembers按座位号升序
        Collections.sort(roomMembers, new Comparator<RoomMember>() {
            @Override
            public int compare(RoomMember o1, RoomMember o2) {
                return o1.getSeat() - o2.getSeat();
            }
        });

        Map<String, Object> result = new HashMap<>(6);

        int players = room.getPlayers();
        int bankerSite = 1;

        //先设置为userId，在api层转换为uId
        Integer bankerUId = roomMembers.get(bankerSite - 1).getUserId();

        // 初始化一局麻将的数据
        MahjongGameData mahjongGameData = MahjongGameData.initData(players, bankerSite);
        log.debug("初始化一局麻将的数据:{}", JsonUtil.toJson(mahjongGameData));


        // 获取新版本号
        Long version = versionRedis.nextVersion(room.getId());
        mahjongGameData.setVersion(version);

        // roomMember改为游戏中
        for (RoomMember roomMember : roomMembers) {
            roomMember.setState(RoomMember.state.PLAYING.getCode());
            roomMemberDao.update(roomMember);
            roomRedis.editRoom(roomMember);
            roomRedis.joinRoom(roomMember);
        }

        // 游戏开始数据
        GameStartVo gameStartVo = new GameStartVo();

        // 添加roomMeMber，拆分成4份手牌数据，传给客户端
        List<FirstPutOutCard> firstPutOutCards = new ArrayList<>(players);
        for (int i = 0; i < players; i++) {
            PersonalCardInfo personalCardInfo = mahjongGameData.getPersonalCardInfos().get(i);
            // 添加玩家信息RoomMember
            personalCardInfo.setRoomMember(roomMembers.get(i));

            // 设置游戏开始数据
            if (i == 0) {
                gameStartVo.setBankerUId(bankerUId);//先设置为userId，在api层转换为uId
                gameStartVo.setDices(mahjongGameData.getDices());
                gameStartVo.setBaoMotherId(mahjongGameData.getBaoMother().getId());

                List<Integer> baoMahjongIds = new ArrayList<>(mahjongGameData.getBaoMahjongs().size());
                for (Mahjong mahjong : mahjongGameData.getBaoMahjongs()) {
                    baoMahjongIds.add(mahjong.getId());
                }
                gameStartVo.setBaoMahjongs(baoMahjongIds);
            }

            FirstPutOutCard fpc = new FirstPutOutCard();
            fpc.setuId(personalCardInfo.getRoomMember().getUserId());//先设置为userId，在api层转换为uId
            fpc.setHandCardIds(Mahjong.parseToIds(personalCardInfo.getHandCards()));
            fpc.setLeftCardCount(mahjongGameData.getLeftCards().size());

            firstPutOutCards.add(fpc);
        }

        result.put(FIRST_PUT_OUT_CARD_KEY, firstPutOutCards);
        result.put(GameStartVo.class.getSimpleName(), gameStartVo);

        // 麻将数据存redis
        gameRedis.saveMahjongGameData(mahjongGameData);
        result.put(MahjongGameData.class.getSimpleName(), mahjongGameData);
        return result;
    }

    /**
     * 打出一张麻将
     */
    public Map<String, Object> playAMahjong(Room room, User user, Mahjong playedMahjong) {
        // 取出等待客户端操作对象waitingClientOperate
        CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(room.getId());
        if (!waitingClientOperate.getRoomMember().getUserId().equals(user.getId())) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }

        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());

        // 出牌验证
        if (!putOutCardValidate(playedMahjong, mahjongGameData, user)) {
            throw CommonError.USER_NOT_HAVE_SPECIFIED_CARD.newException();
        }

        // 删除redis的等待客户端操作对象waitingClientOperate
        gameRedis.deleteWaitingClientOperate(room.getId());

        // 添加打出的麻将到游戏数据
        PersonalCardInfo personalCardInfo = PersonalCardInfo.getPersonalCardInfo(
                mahjongGameData.getPersonalCardInfos(),
                user.getId()
        );
        mahjongGameData.getOutCards().add(new OutCard(playedMahjong, personalCardInfo.getRoomMember()));
        gameRedis.saveMahjongGameData(mahjongGameData);

        // 广播打出的牌
        List<PlayedMahjong> playedMahjongs = playedMahjongBroadcast(mahjongGameData, user, playedMahjong);

        Map<String, Object> result = new HashedMap(2);
        result.put(PlayedMahjong.class.getSimpleName(), playedMahjongs);
        result.put(MahjongGameData.class.getSimpleName(), mahjongGameData);
        return result;
    }


    private List<PlayedMahjong> playedMahjongBroadcast(MahjongGameData mahjongGameData, User user, Mahjong playedMahjong) {
        List<PlayedMahjong> playedMahjongs = new ArrayList<>(mahjongGameData.getPersonalCardInfos().size());
        for (PersonalCardInfo personalCardInfo : mahjongGameData.getPersonalCardInfos()) {
            PlayedMahjong temp = new PlayedMahjong();
            temp.setuId(personalCardInfo.getRoomMember().getUserId());//先设置为userid，在api转uid
            temp.setLeftCardCount(mahjongGameData.getLeftCards().size());
            temp.setPlayedMahjongId(playedMahjong.getId());
            temp.setPlayedUId(user.getUId());
            temp.setHandCardIds(Mahjong.parseToIds(personalCardInfo.getHandCards()));
            temp.setPengMahjongIs(Mahjong.parseCombosToMahjongIds(personalCardInfo.getPengs()));
            temp.setGangs(GangVo.parseFromGangCombos(personalCardInfo.getGangs()));
            temp.setVersion(mahjongGameData.getVersion());
            playedMahjongs.add(temp);
        }
        return playedMahjongs;
    }

    /**
     * 玩家操作时，验证其版本号
     */
    //private void validateVersion(Room room, long version) {
    //    Long nowVersion = versionRedis.nowVersion(room.getId());
    //    if (!nowVersion.equals(version)) {
    //        throw CommonError.SYS_VERSION_TIMEOUT.newException();
    //    }
    //}


    /**
     * 客户端打出一张牌的处理逻辑。
     * 遍历其他3个玩家，判断是否有碰，明杠，吃胡。
     * 如果有，则按优先级降序，存进redis的
     * 选择优先级最高的操作，返回给对应的客户端，等待其选择执行操作或着选择过
     * 如果选择过，则读redis待操作集合，循环上一步操作。
     * 如果全部人都选择过（待操作集合为空），则进入下家摸牌方法。
     * 如果待操作集合中有人选择了执行操作，则清空待操作集合，执行相应操作。
     *
     * @param putOutMahjong 打出的牌对象
     * @param room          用户所在的房间
     * @param version       消息版本号
     */
    public void putOutCard(Mahjong putOutMahjong, Room room, User user,
                           Long version)
            throws InstantiationException, IllegalAccessException {
        // 验证版本号
        //validateVersion(room, version);

        // 取出麻将数据
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(
                room.getId());

        // 出牌验证
        if (!putOutCardValidate(putOutMahjong, mahjongGameData, user)) {
            throw CommonError.USER_NOT_HAVE_SPECIFIED_CARD.newException();
        }

        for (PersonalCardInfo personalCardInfo : mahjongGameData.getPersonalCardInfos()) {
            log.debug("验证后座位{}的手牌：{}{}",
                    personalCardInfo.getRoomMember().getSeat(),
                    personalCardInfo.getHandCards().size(),
                    personalCardInfo.getHandCards());
        }

        // 扫描其他用户是否有吃胡、大明杠、碰的操作
        List<CanDoOperate> canOperates =
                afterPutOutCardManager.scan(mahjongGameData, putOutMahjong, user);
        log.debug("扫描出来可以的操作：{}", canOperates);


    }

    /**
     * 验证客户端出的牌是否合法，合法则在玩家的PersonalCardInfo手牌集合中移除该麻将牌
     */
    private boolean putOutCardValidate(Mahjong putOutCard, MahjongGameData
            mahjongGameData, User user) {
        // 获取玩家手牌信息
        PersonalCardInfo personalCardInfo =
                PersonalCardInfo.getPersonalCardInfo(mahjongGameData.getPersonalCardInfos(), user.getId());

        // 判断客户端打出的牌是不是刚摸上手的
        if (putOutCard == personalCardInfo.getTouchMahjong()) {
            personalCardInfo.setTouchMahjong(null);
            return true;
        }

        // 判断客户端打出的牌是不是其拥有的手牌
        boolean isHandCard = false;
        Iterator<Mahjong> iterator = personalCardInfo.getHandCards().iterator();
        while (iterator.hasNext()) {
            Mahjong mahjong = iterator.next();
            if (mahjong == putOutCard) {
                iterator.remove();
                isHandCard = true;
                break;
            }
        }
        // 在碰的情况下，personalCardInfo.getTouchMahjong()是null
        if (isHandCard && personalCardInfo.getTouchMahjong() != null) {
            // 把摸到的麻将放到手牌列表
            personalCardInfo.getHandCards().add(personalCardInfo.getTouchMahjong());
        }
        return isHandCard;

    }

    /**
     * 处理硬暗杠的请求逻辑
     */
    public MahjongGameData yingAnGang(User user, Room room, List<Mahjong> toBeGangMahjongs) {
        // 判断需要暗杠的牌是否一样
        if (!Mahjong.isSame(toBeGangMahjongs)) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        // 取出等待客户端操作对象waitingClientOperate
        CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(room.getId());
        if (!waitingClientOperate.getRoomMember().getUserId().equals(user.getId())) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }

        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());

        // 玩家个人卡信息
        PersonalCardInfo personalCardInfo = PersonalCardInfo.getPersonalCardInfo(mahjongGameData.getPersonalCardInfos(), user);

        // 判断玩家是否含有暗杠的牌
        boolean isYingAnGang = PersonalCardInfo.hasMahjongsWithTouchMahjong(personalCardInfo, toBeGangMahjongs);
        if (!isYingAnGang) {
            throw CommonError.USER_NOT_HAVE_SPECIFIED_CARD.newException();
        }

        // 玩家的个人卡信息中添加杠列表
        Combo gang = Combo.newYingAnGang(toBeGangMahjongs);
        personalCardInfo.getGangs().add(gang);

        // 玩家的个人卡信息的手牌中移除已杠的麻将
        personalCardInfo.getHandCards().add(personalCardInfo.getTouchMahjong());
        personalCardInfo.setTouchMahjong(null);
        personalCardInfo.getHandCards().removeAll(toBeGangMahjongs);

        gameRedis.saveMahjongGameData(mahjongGameData);
        return mahjongGameData;
    }

    /**
     * 处理软暗杠的请求逻辑
     */
    public MahjongGameData ruanAnGang(User user, Room room, List<Mahjong> toBeGangMahjongs) {
        // 取出等待客户端操作对象waitingClientOperate
        CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(room.getId());
        if (!waitingClientOperate.getRoomMember().getUserId().equals(user.getId())) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }

        // 判断需要暗杠的牌是否一样
        if (Mahjong.isSame(toBeGangMahjongs)) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());

        // 玩家个人卡信息
        PersonalCardInfo personalCardInfo = PersonalCardInfo.getPersonalCardInfo(mahjongGameData.getPersonalCardInfos(), user);

        // 判断玩家是否含有软暗杠的牌
        boolean isRuanAnGang = isRuanAnGang(personalCardInfo, toBeGangMahjongs, mahjongGameData.getBaoMahjongs());
        if (!isRuanAnGang) {
            throw CommonError.USER_NOT_HAVE_SPECIFIED_CARD.newException();
        }

        // 玩家的个人卡信息中添加杠列表
        Combo gang = Combo.newRuanAnGang(toBeGangMahjongs);
        personalCardInfo.getGangs().add(gang);

        // 玩家的个人卡信息的手牌中移除已杠的麻将
        personalCardInfo.getHandCards().add(personalCardInfo.getTouchMahjong());
        personalCardInfo.setTouchMahjong(null);
        personalCardInfo.getHandCards().removeAll(toBeGangMahjongs);

        gameRedis.saveMahjongGameData(mahjongGameData);
        return mahjongGameData;
    }

    /**
     * 验证玩家提交的软暗杠请求是否正确
     */
    private boolean isRuanAnGang(PersonalCardInfo personalCardInfo, List<Mahjong> toBeGangMahjongs, List<Mahjong> baoMahjongs) {
        // 判断玩家的牌中是否含有提交过来杠的牌
        boolean hasToBeGangMahjong = PersonalCardInfo.hasMahjongsWithTouchMahjong(personalCardInfo, toBeGangMahjongs);
        if (!hasToBeGangMahjong) {
            throw CommonError.USER_NOT_HAVE_SPECIFIED_CARD.newException();
        }

        // 判断toBeGangMahjongs有没有宝牌
        Integer baoMahjongNumber = baoMahjongs.get(0).getNumber();
        List<Integer> baoMahjongIndex = new ArrayList<>(toBeGangMahjongs.size() - 1);
        for (int i = 0; i < toBeGangMahjongs.size(); i++) {
            Mahjong toBeGangMahjong = toBeGangMahjongs.get(i);
            if (toBeGangMahjong.getNumber().equals(baoMahjongNumber)) {
                baoMahjongIndex.add(i);
            }
        }
        if (baoMahjongIndex.size() == 0) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        // 判断需要杠的牌中，除了宝牌以外的牌是否一样
        Integer gangNumber = null;
        for (int i = 0; i < toBeGangMahjongs.size(); i++) {
            if (baoMahjongIndex.contains(i)) {
                continue;
            }
            if (gangNumber == null) {
                gangNumber = toBeGangMahjongs.get(i).getNumber();
            } else {
                if (!gangNumber.equals(toBeGangMahjongs.get(i).getNumber())) {
                    throw CommonError.SYS_PARAM_ERROR.newException();
                }
            }
        }

        return true;

    }

    /**
     * 验证玩家提交的硬加杠请求是否正确
     */
    public Object[] yingJiaGang(User user, Room room, Mahjong mahjong) {
        // 取出等待客户端操作对象waitingClientOperate
        CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(room.getId());
        if (!waitingClientOperate.getRoomMember().getUserId().equals(user.getId())) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }

        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());

        // 玩家个人卡信息
        PersonalCardInfo personalCardInfo = PersonalCardInfo.getPersonalCardInfo(mahjongGameData.getPersonalCardInfos(), user);

        // 判断玩家是否含有碰
        List<Combo> pengs = personalCardInfo.getPengs();
        if (pengs.size() == 0) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        // 找到需要加杠的碰
        Combo toBeJiaBangCombo = null;
        for (Combo peng : pengs) {
            if (peng.getYingRuan() == YingRuan.YING) {
                List<Mahjong> mahjongs = peng.getMahjongs();
                if (mahjong.getNumber().equals(mahjongs.get(0).getNumber())) {
                    toBeJiaBangCombo = peng;
                    break;
                }
            }
        }
        if (toBeJiaBangCombo == null) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        // 添加杠combo
        List<Mahjong> yingjiaGangMahjongs = new ArrayList<>(toBeJiaBangCombo.getMahjongs());
        yingjiaGangMahjongs.add(mahjong);
        Combo jiaGangCombo = Combo.newYingJiaGang(yingjiaGangMahjongs);
        personalCardInfo.getGangs().add(jiaGangCombo);

        // 删除碰combo
        pengs.remove(toBeJiaBangCombo);

        // 玩家的个人卡信息的手牌中移除已杠的麻将
        personalCardInfo.getHandCards().add(personalCardInfo.getTouchMahjong());
        personalCardInfo.setTouchMahjong(null);
        personalCardInfo.getHandCards().remove(mahjong);

        gameRedis.saveMahjongGameData(mahjongGameData);
        return new Object[]{mahjongGameData, jiaGangCombo};
    }

    /**
     * 验证玩家提交的软加杠请求是否正确
     */
    public Object[] ruanJiaGang(User user, Room room, List<Mahjong> mahjongs) {
        // 取出等待客户端操作对象waitingClientOperate
        CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(room.getId());
        if (!waitingClientOperate.getRoomMember().getUserId().equals(user.getId())) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }

        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());

        // 玩家个人卡信息
        PersonalCardInfo personalCardInfo = PersonalCardInfo.getPersonalCardInfo(mahjongGameData.getPersonalCardInfos(), user);

        // 判断玩家是否含有碰
        List<Combo> pengs = personalCardInfo.getPengs();
        if (pengs.size() == 0) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        // 分析mahjongs是不是软加杠组合，并找到需要被杠的麻将
        if (Mahjong.isSame(mahjongs)) {// 如果麻将都一样，则不是软加杠
            throw CommonError.SYS_PARAM_ERROR.newException();
        }
        Integer baoMahjongNumber = mahjongGameData.getBaoMahjongs().get(0).getNumber();
        Mahjong beJiaGangMahjong = null;
        for (Mahjong mahjong : mahjongs) {
            if (!baoMahjongNumber.equals(mahjong.getNumber())) {
                if (beJiaGangMahjong == null) {
                    beJiaGangMahjong = mahjong;
                } else {
                    if (!beJiaGangMahjong.getNumber().equals(mahjong.getNumber())) {
                        // 出现了与宝牌和beJiaGangMahjong都不相同的麻将，即mahjongs出现了3种麻将，则加杠麻将参数错误
                        throw CommonError.SYS_PARAM_ERROR.newException();
                    }
                }

            }
        }

        // 找到需要加杠的碰组合。
        // 如果是硬碰，则需要有宝牌才能加杠
        // 如果是软碰，则有宝牌和碰牌都可以加杠
        Combo toBeJiaBangCombo = null;
        for (Combo peng : pengs) {
            for (Mahjong tempMahjong : peng.getMahjongs()) {
                // 如果这个combo组合是宝牌归位碰，或者不等于beJiaGangMahjong,则不是需要加杠的组合
                if (tempMahjong.getNumber().equals(baoMahjongNumber)) {
                    if (peng.getYingRuan() == YingRuan.YING) {
                        break;
                    }
                } else if (!tempMahjong.getNumber().equals(beJiaGangMahjong.getNumber())) {
                    break;
                }
            }
            toBeJiaBangCombo = peng;
            break;
        }
        if (toBeJiaBangCombo == null) {
            throw CommonError.USER_NOT_HAVE_SPECIFIED_CARD.newException();
        }

        // 添加杠combo
        Combo jiaGangCombo = Combo.newRuanJiaGang(mahjongs);
        personalCardInfo.getGangs().add(jiaGangCombo);

        // 删除碰combo
        pengs.remove(toBeJiaBangCombo);

        // 玩家的个人卡信息的手牌中移除已杠的麻将
        personalCardInfo.getHandCards().add(personalCardInfo.getTouchMahjong());
        personalCardInfo.setTouchMahjong(null);
        personalCardInfo.getHandCards().remove(mahjongs.get(mahjongs.size() - 1));

        gameRedis.saveMahjongGameData(mahjongGameData);
        return new Object[]{mahjongGameData, jiaGangCombo};
    }

    /**
     * 执行硬大明杠的逻辑
     */
    public Object[] yingDaMingGang(User user, Room room, List<Mahjong> mahjongs) {
        // 取出等待客户端操作对象waitingClientOperate
        CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(room.getId());
        if (!waitingClientOperate.getRoomMember().getUserId().equals(user.getId())) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }

        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());

        // 玩家个人卡信息
        PersonalCardInfo personalCardInfo = PersonalCardInfo.getPersonalCardInfo(mahjongGameData.getPersonalCardInfos(), user);

        // 判断玩家手牌是否有3只跟别的玩家打出一样的麻将
        Mahjong playedMahjong = mahjongs.remove(mahjongs.size() - 1);
        if (!personalCardInfo.getHandCards().containsAll(mahjongs)) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        mahjongs.add(playedMahjong);

        // 添加杠combo
        Combo yingDaMingGangCombo = Combo.newYingDaMingGang(mahjongs);
        personalCardInfo.getGangs().add(yingDaMingGangCombo);

        // 玩家的个人卡信息的手牌中移除已杠的麻将
        personalCardInfo.getHandCards().removeAll(mahjongs);

        gameRedis.saveMahjongGameData(mahjongGameData);
        return new Object[]{mahjongGameData, yingDaMingGangCombo};
    }

    /**
     * 执行碰的逻辑
     */
    public Object[] peng(User user, Room room, List<Mahjong> mahjongs) {
        canOperate(room.getId(), user.getId(), Operate.YING_PENG);

        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());

        // 玩家个人卡信息
        PersonalCardInfo personalCardInfo = PersonalCardInfo.getPersonalCardInfo(mahjongGameData.getPersonalCardInfos(), user);

        // 判断玩家手牌是否有2只跟别的玩家打出一样的麻将
        Mahjong playedMahjong = mahjongs.remove(mahjongs.size() - 1);
        if (!personalCardInfo.getHandCards().containsAll(mahjongs)) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        mahjongs.add(playedMahjong);

        // 添加碰combo
        Combo yingDaMingGangCombo = Combo.newPeng(mahjongs);
        personalCardInfo.getPengs().add(yingDaMingGangCombo);

        // 玩家的个人卡信息的手牌中移除已碰的麻将
        personalCardInfo.getHandCards().removeAll(mahjongs);

        gameRedis.saveMahjongGameData(mahjongGameData);
        return new Object[]{mahjongGameData, yingDaMingGangCombo};
    }

    /**
     * 在软碰或软杠的组合中，找到真正碰的牌，即找到非宝牌的牌
     * 例如碰：一万 一万 五筒
     * 五筒是宝牌，则会返回真正碰的牌：一万
     *
     * @param mahjongs         碰或杠的麻将列表，最多只能出现两种麻将，
     * @param baoMahjongNumber 宝牌的number
     * @return 真正碰的牌
     */
    private Mahjong findReallyMahjong(List<Mahjong> mahjongs, Integer baoMahjongNumber) {
        Mahjong reallyMahjong = null;
        Mahjong baoMahjong = null;
        for (Mahjong mahjong : mahjongs) {
            if (!baoMahjongNumber.equals(mahjong.getNumber())) {
                if (reallyMahjong == null) {
                    reallyMahjong = mahjong;
                } else {
                    if (!reallyMahjong.getNumber().equals(mahjong.getNumber())) {
                        throw CommonError.SYS_PARAM_ERROR.newException();
                    }
                }
            } else {
                baoMahjong = mahjong;
            }
        }
        if (baoMahjong == null) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }
        if (reallyMahjong == null) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }
        return reallyMahjong;
    }

    /**
     * 执行软大明杠的逻辑
     */
    public Object[] ruanDaMingGang(User user, Room room, List<Mahjong> mahjongs) {
        canOperate(room.getId(), user.getId(), Operate.RUAN_DA_MING_GANG);


        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());

        // 验证需要软大明杠的麻将是否符合规则
        findReallyMahjong(mahjongs, mahjongGameData.getBaoMahjongs().get(0).getNumber());

        // 玩家个人卡信息
        PersonalCardInfo personalCardInfo =
                PersonalCardInfo.getPersonalCardInfo(mahjongGameData.getPersonalCardInfos(), user);

        // 判断玩家手牌是否有3只跟别的玩家打出一样的麻将
        Mahjong playedMahjong = mahjongs.remove(mahjongs.size() - 1);
        if (!personalCardInfo.getHandCards().containsAll(mahjongs)) {
            throw CommonError.SYS_PARAM_ERROR.newException();
        }

        mahjongs.add(playedMahjong);

        // 添加杠combo
        Combo ruanDaMingGangCombo = Combo.newRuanDaMingGang(mahjongs);
        personalCardInfo.getGangs().add(ruanDaMingGangCombo);

        // 玩家的个人卡信息的手牌中移除已杠的麻将
        personalCardInfo.getHandCards().removeAll(mahjongs);

        gameRedis.saveMahjongGameData(mahjongGameData);
        return new Object[]{mahjongGameData, ruanDaMingGangCombo};
    }

    /**
     * 执行抢大明杠的逻辑
     */
    public Object[] qiangDaMingGangHu(User user, Room room, Mahjong qiangGangMahjong) throws InstantiationException, IllegalAccessException {
        canOperate(room.getId(), user.getId(), Operate.QIANG_DA_MING_GANG_HU);

        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());

        // 获取胡牌类型
        List<CanDoOperate> canOperates = getACardManager.scan(
                mahjongGameData,
                qiangGangMahjong,
                user
        );


        return new Object[]{mahjongGameData};
    }

    /**
     * 从redis获取clientOperateQueue，下一个可以操作的人
     */
    public Object[] guo(User user, Room room) {
        // 取出等待客户端操作对象waitingClientOperate
        CanDoOperate waitingClientOperate = gameRedis.getWaitingClientOperate(room.getId());
        if (!waitingClientOperate.getRoomMember().getUserId().equals(user.getId())) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }
        if (!waitingClientOperate.getOperates().contains(Operate.GUO)) {
            throw CommonError.NOT_YOUR_TURN.newException();
        }

        CanDoOperate nextCanDoOperate = gameRedis.getNextCanDoOperate(room.getId());

        if (nextCanDoOperate != null) {
            gameRedis.saveWaitingClientOperate(nextCanDoOperate);

        }

        // 取出麻将数据对象
        MahjongGameData mahjongGameData = gameRedis.getMahjongGameData(room.getId());


        return new Object[]{nextCanDoOperate, waitingClientOperate, mahjongGameData};

    }
}
