package com.jinniu.commonjn.model.mahjong.type;

import com.jinniu.commonjn.model.mahjong.Combo;
import com.jinniu.commonjn.util.PidValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 杠的类型
 */
public enum GangType {
    AN_GANG(1, GangClassify.AN_GANG, PidValue.YING_AN_GANG, "暗杠"),
    DA_MING_GANG(2, GangClassify.MING_GANG, PidValue.YING_DA_MING_GANG, "大明杠"),
    JIA_GANG(3, GangClassify.MING_GANG, PidValue.YING_JIA_GANG, "加杠");

    /**
     * id
     */
    private Integer id;

    /**
     * 杠的大类
     */
    private GangClassify gangClassify;

    /**
     * 杠操作的来源协议
     */
    private PidValue pidValue;

    /**
     * 杠的名称
     */
    private String name;

    GangType(Integer id, GangClassify gangClassify, PidValue pidValue, String name) {
        this.id = id;
        this.gangClassify = gangClassify;
        this.pidValue = pidValue;
        this.name = name;
    }

    public static GangType parseFromPidValue(PidValue pidValue) {
        GangType[] values = GangType.values();
        for (GangType value : values) {
            if (value.getPidValue() == pidValue) {
                return value;
            }
        }
        throw new UnsupportedOperationException(String.format("杠的类型GangType不支持此PidValue: %s", pidValue));
    }

    public static List<GangType> parseFromGangCombos(List<Combo> combos) {
        List<GangType> gangTypes = new ArrayList<>(combos.size());
        for (Combo combo : combos) {
            gangTypes.add(parseFromPidValue(combo.getPidValue()));
        }
        return gangTypes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PidValue getPidValue() {
        return pidValue;
    }

    public void setPidValue(PidValue pidValue) {
        this.pidValue = pidValue;
    }

    public GangClassify getGangClassify() {
        return gangClassify;
    }

    public void setGangClassify(GangClassify gangClassify) {
        this.gangClassify = gangClassify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 杠的大类
     */
    public static enum GangClassify {
        MING_GANG(1, "明杠"),
        AN_GANG(2, "暗杠");

        private Integer id;

        private String name;

        GangClassify(Integer code, String name) {
            this.id = code;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
