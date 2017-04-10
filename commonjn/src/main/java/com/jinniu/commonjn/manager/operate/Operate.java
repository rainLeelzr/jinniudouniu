package com.jinniu.commonjn.manager.operate;

import com.jinniu.commonjn.util.PidValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 别人出牌后其他玩家可以的操作
 * 如吃胡、大明杠、碰
 */
public enum Operate {

    ZI_MO_YING_PENG_PENG_HU(PidValue.YING_ZI_MO, BaseOperate.HU, "自摸硬碰碰胡"),
    ZI_MO_RUAN_PENG_PENG_HU(PidValue.RUAN_ZI_MO, BaseOperate.HU, "自摸软碰碰胡"),

    ZI_MO_YING_QI_DUI_HU(PidValue.YING_ZI_MO, BaseOperate.HU, "自摸硬七对胡"),
    ZI_MO_RUAN_QI_DUI_HU(PidValue.RUAN_ZI_MO, BaseOperate.HU, "自摸软七对胡"),

    ZI_MO_YING_PING_HU(PidValue.YING_ZI_MO, BaseOperate.HU, "自摸硬平胡"),
    ZI_MO_RUAN_PING_HU(PidValue.RUAN_ZI_MO, BaseOperate.HU, "自摸软平胡"),

    CHI_YING_PENG_PENG_HU(PidValue.YING_CHI_HU, BaseOperate.HU, "吃硬碰碰胡"),
    CHI_RUAN_PENG_PENG_HU(PidValue.RUAN_CHI_HU, BaseOperate.HU, "吃软碰碰胡"),

    CHI_YING_QI_DUI_HU(PidValue.YING_CHI_HU, BaseOperate.HU, "吃硬七对胡"),
    CHI_RUAN_QI_DUI_HU(PidValue.RUAN_CHI_HU, BaseOperate.HU, "吃软七对胡"),

    CHI_YING_PING_HU(PidValue.YING_CHI_HU, BaseOperate.HU, "吃硬平胡"),
    CHI_RUAN_PING_HU(PidValue.RUAN_CHI_HU, BaseOperate.HU, "吃软平胡"),

    QIANG_DA_MING_GANG_HU(PidValue.QIANG_DA_MING_GANG_HU, BaseOperate.HU, "抢大明杠胡"),
    QIANG_JIA_GANG_HU(PidValue.QIANG_DA_MING_GANG_HU, BaseOperate.HU, "抢加杠胡"),

    YING_AN_GANG(PidValue.YING_AN_GANG, BaseOperate.YING_AN_GANG, "硬暗杠"),
    RUAN_AN_GANG(PidValue.RUAN_AN_GANG, BaseOperate.RUAN_AN_GANG, "软暗杠"),

    YING_DA_MING_GANG(PidValue.YING_DA_MING_GANG, BaseOperate.YING_DA_MING_GANG, "硬大明杠"),
    RUAN_DA_MING_GANG(PidValue.RUAN_DA_MING_GANG, BaseOperate.RUAN_DA_MING_GANG, "软大明杠"),

    YING_JIA_GANG(PidValue.YING_JIA_GANG, BaseOperate.YING_JIA_GANG, "硬加杠"),
    RUAN_JIA_GANG(PidValue.RUAN_JIA_GANG, BaseOperate.RUAN_JIA_GANG, "软加杠"),

    YING_PENG(PidValue.YING_PENG, BaseOperate.YING_PENG, "硬碰"),
    RUAN_PENG(PidValue.RUAN_PENG, BaseOperate.RUAN_PENG, "软碰"),

    PLAY_A_MAHJONG(PidValue.PLAY_A_MAHJONG, BaseOperate.PLAY_A_MAHJONG, "打出一张牌"),

    GUO(PidValue.GUO, BaseOperate.PLAY_A_MAHJONG, "过");

    /**
     * 操作唯一id
     */
    private PidValue pidValue;

    /**
     * 操作的大类型
     */
    private BaseOperate baseOperate;
    /**
     * 操作的名称
     */
    private String name;

    Operate(PidValue pidValue, BaseOperate baseOperate, String name) {
        this.pidValue = pidValue;
        this.baseOperate = baseOperate;
        this.name = name;
    }

    /**
     * 将operate枚举对象列表转换为pid列表
     */
    public static List<Integer> parseToPids(Collection<Operate> operates) {
        if (operates.size() == 0) {
            return Collections.emptyList();
        }

        List<Integer> pids = new ArrayList<>(operates.size());
        for (Operate operate : operates) {
            pids.add(operate.getPidValue().getPid());
        }
        return pids;
    }

    public PidValue getPidValue() {
        return pidValue;
    }

    public void setPidValue(PidValue pidValue) {
        this.pidValue = pidValue;
    }

    public BaseOperate getBaseOperate() {
        return baseOperate;
    }

    public void setBaseOperate(BaseOperate baseOperate) {
        this.baseOperate = baseOperate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
