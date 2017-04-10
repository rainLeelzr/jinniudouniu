package com.jinniu.commonjn.model;

public class Prize {
    private String prize_name;//奖品名称
    private double prize_weight;//奖品权重


    public String getPrize_name() {
        return prize_name;
    }

    public void setPrize_name(String prize_name) {
        this.prize_name = prize_name;
    }

    public double getPrize_weight() {
        return prize_weight;
    }

    public void setPrize_weight(double prize_weight) {
        this.prize_weight = prize_weight;
    }

    public Prize(String prize_name, double prize_weight) {
        this.prize_name = prize_name;
        this.prize_weight = prize_weight;
    }
}
