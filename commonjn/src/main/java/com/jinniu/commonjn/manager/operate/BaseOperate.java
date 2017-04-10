package com.jinniu.commonjn.manager.operate;

/**
 * 基本操作类型
 */
public enum BaseOperate {
    // 优先级高的要写在前面
    HU("胡"),//胡包括自摸、吃胡

    YING_AN_GANG("硬暗杠"),
    RUAN_AN_GANG("软暗杠"),

    YING_DA_MING_GANG("硬大明杠"),
    RUAN_DA_MING_GANG("软大明杠"),

    YING_JIA_GANG("硬加杠"),
    RUAN_JIA_GANG("软加杠"),
    //GANG("杠"),

    YING_PENG("硬碰"),
    RUAN_PENG("软碰"),

    PLAY_A_MAHJONG("打出一张牌");

    private String name;

    BaseOperate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{\"BaseOperate\":{"
                + "\"name\":\"" + name + "\""
                + "}}";
    }
}
