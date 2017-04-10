package com.jinniu.commonjn.manager.scanTask;

import com.jinniu.commonjn.manager.operate.CanDoOperate;
import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.picker.PersonalCardInfoPicker;
import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.MahjongGameData;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 扫描任务接口
 */
public abstract class BaseScanTask {

    protected static final Logger log = LoggerFactory.getLogger
            (BaseScanTask.class);

    /**
     * 用户打出的牌
     */
    protected Mahjong specifiedMahjong;
    /**
     * 出牌的玩家
     */
    protected User user;

    protected MahjongGameData mahjongGameData;

    /**
     * 需要对这些玩家的手牌进行扫描
     */
    protected List<PersonalCardInfo> toBeScanPersonalCardInfos;

    /**
     * 具体的任务扫描器判定到某个用户可以执行某些操作时，向此列表添加元素
     */
    private List<CanDoOperate> canOperates;
    /**
     * 提取器，从mahjongGameData中提取出需要扫描的玩家手牌
     */
    private PersonalCardInfoPicker personalCardInfoPicker;

    /**
     * 基本操作类型
     */
    public abstract Operate getOperate();

    public List<PersonalCardInfo> getToBeScanPersonalCardInfos() {
        return toBeScanPersonalCardInfos;
    }

    public void setToBeScanPersonalCardInfos(List<PersonalCardInfo> toBeScanPersonalCardInfos) {
        this.toBeScanPersonalCardInfos = toBeScanPersonalCardInfos;
    }

    public PersonalCardInfoPicker getPersonalCardInfoPicker() {
        return personalCardInfoPicker;
    }

    public void setPersonalCardInfoPicker(PersonalCardInfoPicker personalCardInfoPicker) {
        this.personalCardInfoPicker = personalCardInfoPicker;
    }

    public abstract boolean doScan(PersonalCardInfo personalCardInfo)
            throws InstantiationException, IllegalAccessException;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CanDoOperate> getCanOperates() {
        return canOperates;
    }

    public void setCanOperates(List<CanDoOperate> canOperates) {
        this.canOperates = canOperates;
    }

    public MahjongGameData getMahjongGameData() {
        return mahjongGameData;
    }

    public void setMahjongGameData(MahjongGameData mahjongGameData) {
        this.mahjongGameData = mahjongGameData;
    }

    public Mahjong getSpecifiedMahjong() {
        return specifiedMahjong;
    }

    public void setSpecifiedMahjong(Mahjong specifiedMahjong) {
        this.specifiedMahjong = specifiedMahjong;
    }

    public void scan() throws IllegalAccessException, InstantiationException {
        toBeScanPersonalCardInfos = personalCardInfoPicker.pick(mahjongGameData, user);

        for (PersonalCardInfo toBeScanPersonalCardInfo : toBeScanPersonalCardInfos) {
            Set<Operate> myOperates = getMyOperates(
                    toBeScanPersonalCardInfo.getRoomMember().getUserId());
            boolean contain = false;
            for (Operate myOperate : myOperates) {
                if (myOperate.getBaseOperate() == getOperate().getBaseOperate()) {
                    contain = true;
                    break;
                }
            }

            if (!contain) {
                log.debug(
                        "座位{}进行{}扫描，手牌：{}",
                        toBeScanPersonalCardInfo.getRoomMember().getSeat(),
                        getOperate().getName(),
                        toBeScanPersonalCardInfo.getHandCards()
                );
                if (doScan(toBeScanPersonalCardInfo)) {
                    log.debug(
                            "座位{}可以{}",
                            toBeScanPersonalCardInfo.getRoomMember().getSeat(),
                            getOperate().getName()
                    );
                    // 添加可行操作
                    myOperates.add(getOperate());
                }
            } else {
                log.debug(
                        "座位{}已有{},无需再扫描{}！",
                        toBeScanPersonalCardInfo.getRoomMember().getSeat(),
                        getOperate().getBaseOperate().getName(),
                        getOperate().getName()
                );
            }
        }
    }

    /**
     * 获取本玩家的可行的操作列表
     */
    protected Set<Operate> getMyOperates(Integer userId) {
        // 找出userId的个人牌信息
        PersonalCardInfo personalCardInfo = null;
        for (PersonalCardInfo cardInfo : mahjongGameData.getPersonalCardInfos()) {
            if (cardInfo.getRoomMember().getUserId().equals(userId)) {
                personalCardInfo = cardInfo;
                break;
            }
        }

        // 找出userId的可行的操作列表
        CanDoOperate canDoOperate = null;
        for (CanDoOperate canOperate : canOperates) {
            if (canOperate.getRoomMember().getUserId().equals(userId)) {
                canDoOperate = canOperate;
                break;
            }
        }
        if (canDoOperate == null) {
            canDoOperate = new CanDoOperate();
            canDoOperate.setRoomMember(personalCardInfo.getRoomMember());
            canDoOperate.setOperates(new TreeSet<Operate>());
            canDoOperate.setSpecialMahjong(specifiedMahjong);
            canDoOperate.setSpecialUserId(user.getId());
            canOperates.add(canDoOperate);
        }
        return canDoOperate.getOperates();
    }

    /**
     * 找出手牌中拥有的宝牌
     */
    protected List<Mahjong> getMyBaoMahjongs(Collection<Mahjong> handCards) {
        List<Mahjong> baoMahjongs = this.mahjongGameData.getBaoMahjongs();
        List<Mahjong> myBaoMahjongs = new ArrayList<>(4);

        if (baoMahjongs != null) {
            for (Mahjong baoMahjong : baoMahjongs) {
                if (handCards.contains(baoMahjong)) {
                    myBaoMahjongs.add(baoMahjong);
                }
            }
        }

        return myBaoMahjongs;
    }

    /**
     * 按麻将字号分组
     */
    protected Map<Integer, List<Mahjong>> groupByZiHao(List<Mahjong> handCards) {
        Map<Integer, List<Mahjong>> Mahjongs = new HashMap<>(6);
        for (Mahjong handCard : handCards) {
            Integer ziHao = handCard.getZi();
            List<Mahjong> ziHaoMahjongs = Mahjongs.get(ziHao);
            if (ziHaoMahjongs == null) {
                ziHaoMahjongs = new ArrayList<>(8);
                Mahjongs.put(ziHao, ziHaoMahjongs);
            }
            ziHaoMahjongs.add(handCard);
        }
        return Mahjongs;
    }

    /**
     * 按麻将号码分组
     */
    protected Map<Integer, List<Mahjong>> groupByNumber(List<Mahjong> mahjongs) {
        Map<Integer, List<Mahjong>> sameNumberMahjongs = new HashMap<>(6);
        for (Mahjong handCard : mahjongs) {
            Integer number = handCard.getNumber();
            List<Mahjong> numberMahjongs = sameNumberMahjongs.get(number);
            if (numberMahjongs == null) {
                numberMahjongs = new ArrayList<>(14);
                sameNumberMahjongs.put(number, numberMahjongs);
            }
            numberMahjongs.add(handCard);
        }
        return sameNumberMahjongs;
    }

}
