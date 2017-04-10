package com.jinniu.commonjn.model.mahjong;

import com.jinniu.commonjn.util.PidValue;

import java.util.Collections;
import java.util.List;

public class Combo {

    public Type type;

    public YingRuan yingRuan;

    public List<Mahjong> mahjongs;

    /**
     * 此combo是通过哪个pid请求生成的，例如硬大明杠
     */
    public PidValue pidValue;

    /**
     * 创建硬大明杠的combo
     */
    public static Combo newYingDaMingGang(List<Mahjong> mahjongs) {
        Combo combo = new Combo();
        combo.setType(Combo.Type.AAAA);
        combo.setYingRuan(YingRuan.YING);
        combo.setPidValue(PidValue.YING_DA_MING_GANG);
        combo.setMahjongs(mahjongs);
        Collections.sort(mahjongs);
        return combo;
    }

    /**
     * 创建碰的combo
     */
    public static Combo newPeng(List<Mahjong> mahjongs) {
        Combo combo = new Combo();
        combo.setType(Combo.Type.AAA);
        combo.setYingRuan(YingRuan.YING);
        combo.setPidValue(PidValue.YING_PENG);
        combo.setMahjongs(mahjongs);
        Collections.sort(mahjongs);
        return combo;
    }

    /**
     * 创建软大明杠的combo
     */
    public static Combo newRuanDaMingGang(List<Mahjong> mahjongs) {
        Combo combo = new Combo();
        combo.setType(Combo.Type.AAAA);
        combo.setYingRuan(YingRuan.RUAN);
        combo.setPidValue(PidValue.RUAN_DA_MING_GANG);
        combo.setMahjongs(mahjongs);
        Collections.sort(mahjongs);
        return combo;
    }

    /**
     * 创建软加杠的combo
     */
    public static Combo newRuanJiaGang(List<Mahjong> mahjongs) {
        Combo combo = new Combo();
        combo.setType(Combo.Type.AAAA);
        combo.setYingRuan(YingRuan.RUAN);
        combo.setPidValue(PidValue.RUAN_JIA_GANG);
        combo.setMahjongs(mahjongs);
        Collections.sort(mahjongs);
        return combo;
    }

    /**
     * 创建硬加杠的combo
     */
    public static Combo newYingJiaGang(List<Mahjong> mahjongs) {
        Combo combo = new Combo();
        combo.setType(Combo.Type.AAAA);
        combo.setYingRuan(YingRuan.YING);
        combo.setPidValue(PidValue.YING_JIA_GANG);
        combo.setMahjongs(mahjongs);
        Collections.sort(mahjongs);
        return combo;
    }

    /**
     * 创建软暗杠的combo
     */
    public static Combo newRuanAnGang(List<Mahjong> mahjongs) {
        Combo combo = new Combo();
        combo.setType(Combo.Type.AAAA);
        combo.setYingRuan(YingRuan.RUAN);
        combo.setPidValue(PidValue.RUAN_AN_GANG);
        combo.setMahjongs(mahjongs);
        Collections.sort(mahjongs);
        return combo;
    }

    /**
     * 创建硬暗杠的combo
     */
    public static Combo newYingAnGang(List<Mahjong> mahjongs) {
        Combo combo = new Combo();
        combo.setType(Combo.Type.AAAA);
        combo.setYingRuan(YingRuan.YING);
        combo.setPidValue(PidValue.YING_AN_GANG);
        combo.setMahjongs(mahjongs);
        Collections.sort(mahjongs);
        return combo;
    }

    public PidValue getPidValue() {
        return pidValue;
    }

    public void setPidValue(PidValue pidValue) {
        this.pidValue = pidValue;
    }

    public YingRuan getYingRuan() {
        return yingRuan;
    }

    public void setYingRuan(YingRuan yingRuan) {
        this.yingRuan = yingRuan;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Mahjong> getMahjongs() {
        return mahjongs;
    }

    public void setMahjongs(List<Mahjong> mahjongs) {
        this.mahjongs = mahjongs;
    }

    public enum Type {
        // 杠
        AAAA(),

        // 碰
        AAA(),

        // 顺子
        ABC(),

        // 眼
        AA();

        Type() {
        }
    }
}
