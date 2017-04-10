package com.jinniu.commonjn.model.mahjong;

public enum YingRuan {
    YING("硬", 2),
    RUAN("软", 1);

    private String name;

    // 倍数
    private int multiple;

    YingRuan(String name, int multiple) {
        this.name = name;
        this.multiple = multiple;
    }
}
