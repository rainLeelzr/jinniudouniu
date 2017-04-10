package com.jinniu.commonjn.model.mahjong;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 麻将牌的数据结构
 * id百位数1为万字，2为筒字，3为条字，4为番字
 */
public enum Mahjong implements Comparable<Mahjong> {
    ONE_WANG_1(111, 11, "一万"),
    ONE_WANG_2(112, 11, "一万"),
    ONE_WANG_3(113, 11, "一万"),
    ONE_WANG_4(114, 11, "一万"),

    TWO_WANG_1(121, 12, "二万"),
    TWO_WANG_2(122, 12, "二万"),
    TWO_WANG_3(123, 12, "二万"),
    TWO_WANG_4(124, 12, "二万"),

    THREE_WANG_1(131, 13, "三万"),
    THREE_WANG_2(132, 13, "三万"),
    THREE_WANG_3(133, 13, "三万"),
    THREE_WANG_4(134, 13, "三万"),

    FOUR_WANG_1(141, 14, "四万"),
    FOUR_WANG_2(142, 14, "四万"),
    FOUR_WANG_3(143, 14, "四万"),
    FOUR_WANG_4(144, 14, "四万"),

    FIVE_WANG_1(151, 15, "五万"),
    FIVE_WANG_2(152, 15, "五万"),
    FIVE_WANG_3(153, 15, "五万"),
    FIVE_WANG_4(154, 15, "五万"),

    SIX_WANG_1(161, 16, "六万"),
    SIX_WANG_2(162, 16, "六万"),
    SIX_WANG_3(163, 16, "六万"),
    SIX_WANG_4(164, 16, "六万"),

    SEVEN_WANG_1(171, 17, "七万"),
    SEVEN_WANG_2(172, 17, "七万"),
    SEVEN_WANG_3(173, 17, "七万"),
    SEVEN_WANG_4(174, 17, "七万"),

    EIGHT_WANG_1(181, 18, "八万"),
    EIGHT_WANG_2(182, 18, "八万"),
    EIGHT_WANG_3(183, 18, "八万"),
    EIGHT_WANG_4(184, 18, "八万"),

    NINE_WANG_1(191, 19, "九万"),
    NINE_WANG_2(192, 19, "九万"),
    NINE_WANG_3(193, 19, "九万"),
    NINE_WANG_4(194, 19, "九万"),


    ONE_TONG_1(211, 21, "一筒"),
    ONE_TONG_2(212, 21, "一筒"),
    ONE_TONG_3(213, 21, "一筒"),
    ONE_TONG_4(214, 21, "一筒"),

    TWO_TONG_1(221, 22, "二筒"),
    TWO_TONG_2(222, 22, "二筒"),
    TWO_TONG_3(223, 22, "二筒"),
    TWO_TONG_4(224, 22, "二筒"),

    THREE_TONG_1(231, 23, "三筒"),
    THREE_TONG_2(232, 23, "三筒"),
    THREE_TONG_3(233, 23, "三筒"),
    THREE_TONG_4(234, 23, "三筒"),

    FOUR_TONG_1(241, 24, "四筒"),
    FOUR_TONG_2(242, 24, "四筒"),
    FOUR_TONG_3(243, 24, "四筒"),
    FOUR_TONG_4(244, 24, "四筒"),

    FIVE_TONG_1(251, 25, "五筒"),
    FIVE_TONG_2(252, 25, "五筒"),
    FIVE_TONG_3(253, 25, "五筒"),
    FIVE_TONG_4(254, 25, "五筒"),

    SIX_TONG_1(261, 26, "六筒"),
    SIX_TONG_2(262, 26, "六筒"),
    SIX_TONG_3(263, 26, "六筒"),
    SIX_TONG_4(264, 26, "六筒"),

    SEVEN_TONG_1(271, 27, "七筒"),
    SEVEN_TONG_2(272, 27, "七筒"),
    SEVEN_TONG_3(273, 27, "七筒"),
    SEVEN_TONG_4(274, 27, "七筒"),

    EIGHT_TONG_1(281, 28, "八筒"),
    EIGHT_TONG_2(282, 28, "八筒"),
    EIGHT_TONG_3(283, 28, "八筒"),
    EIGHT_TONG_4(284, 28, "八筒"),

    NINE_TONG_1(291, 29, "九筒"),
    NINE_TONG_2(292, 29, "九筒"),
    NINE_TONG_3(293, 29, "九筒"),
    NINE_TONG_4(294, 29, "九筒"),


    ONE_TIAO_1(311, 31, "一条"),
    ONE_TIAO_2(312, 31, "一条"),
    ONE_TIAO_3(313, 31, "一条"),
    ONE_TIAO_4(314, 31, "一条"),

    TWO_TIAO_1(321, 32, "二条"),
    TWO_TIAO_2(322, 32, "二条"),
    TWO_TIAO_3(323, 32, "二条"),
    TWO_TIAO_4(324, 32, "二条"),

    THREE_TIAO_1(331, 33, "三条"),
    THREE_TIAO_2(332, 33, "三条"),
    THREE_TIAO_3(333, 33, "三条"),
    THREE_TIAO_4(334, 33, "三条"),

    FOUR_TIAO_1(341, 34, "四条"),
    FOUR_TIAO_2(342, 34, "四条"),
    FOUR_TIAO_3(343, 34, "四条"),
    FOUR_TIAO_4(344, 34, "四条"),

    FIVE_TIAO_1(351, 35, "五条"),
    FIVE_TIAO_2(352, 35, "五条"),
    FIVE_TIAO_3(353, 35, "五条"),
    FIVE_TIAO_4(354, 35, "五条"),

    SIX_TIAO_1(361, 36, "六条"),
    SIX_TIAO_2(362, 36, "六条"),
    SIX_TIAO_3(363, 36, "六条"),
    SIX_TIAO_4(364, 36, "六条"),

    SEVEN_TIAO_1(371, 37, "七条"),
    SEVEN_TIAO_2(372, 37, "七条"),
    SEVEN_TIAO_3(373, 37, "七条"),
    SEVEN_TIAO_4(374, 37, "七条"),

    EIGHT_TIAO_1(381, 38, "八条"),
    EIGHT_TIAO_2(382, 38, "八条"),
    EIGHT_TIAO_3(383, 38, "八条"),
    EIGHT_TIAO_4(384, 38, "八条"),

    NINE_TIAO_1(391, 39, "九条"),
    NINE_TIAO_2(392, 39, "九条"),
    NINE_TIAO_3(393, 39, "九条"),
    NINE_TIAO_4(394, 39, "九条"),

    //DONG_FENG_1(411, 41, "东"),
    //DONG_FENG_2(412, 41, "东"),
    //DONG_FENG_3(413, 41, "东"),
    //DONG_FENG_4(414, 41, "东"),
    //
    //NAN_FENG_1(421, 42, "南"),
    //NAN_FENG_2(422, 42, "南"),
    //NAN_FENG_3(423, 42, "南"),
    //NAN_FENG_4(424, 42, "南"),
    //
    //XI_FENG_1(431, 43, "西"),
    //XI_FENG_2(432, 43, "南"),
    //XI_FENG_3(433, 43, "南"),
    //XI_FENG_4(434, 43, "南"),
    //
    //BEI_FENG_1(441, 44, "北"),
    //BEI_FENG_2(442, 44, "北"),
    //BEI_FENG_3(443, 44, "北"),
    //BEI_FENG_4(444, 44, "北"),

    HONG_ZHONG_1(451, 45, "红中"),
    HONG_ZHONG_2(452, 45, "红中"),
    HONG_ZHONG_3(453, 45, "红中"),
    HONG_ZHONG_4(454, 45, "红中"),

    FA_CAI_1(461, 46, "发财"),
    FA_CAI_2(462, 46, "发财"),
    FA_CAI_3(463, 46, "发财"),
    FA_CAI_4(464, 46, "发财"),

    BAI_BAN_1(471, 47, "白板"),
    BAI_BAN_2(472, 47, "白板"),
    BAI_BAN_3(473, 47, "白板"),
    BAI_BAN_4(474, 47, "白板"),;

    // 字号,id百位数1为万字，2为筒字，3为条字，4为番字
    public static final Integer WANG_ZI = 1;
    public static final Integer TONG_ZI = 2;
    public static final Integer TIAO_ZI = 3;
    public static final Integer FAN_ZI = 4;
    /**
     * 所有麻将的集合
     */
    private static List<Mahjong> allMahjongs;

    static {
        allMahjongs = new ArrayList<>(Mahjong.values().length);
        for (Mahjong mahjong : Mahjong.values()) {
            allMahjongs.add(mahjong);
        }
    }

    /**
     * 每张麻将牌的唯一id
     */
    private Integer id;

    /**
     * 麻将牌的号码，每个号码有4张牌
     */
    private Integer number;

    /**
     * 麻将牌的名称
     */
    private String name;

    Mahjong(Integer id, Integer number, String name) {
        this.id = id;
        this.number = number;
        this.name = name;
    }

    /**
     * 根据id，解析为麻将牌对象
     */
    public static Mahjong parse(int id) {
        for (Mahjong mahjong : Mahjong.values()) {
            if (mahjong.id == id) {
                return mahjong;
            }
        }
        throw new RuntimeException("麻将id参数错误，不能解析到对应的麻将牌");
    }

    /**
     * 获取全部麻将牌
     */
    public static List<Mahjong> getAllMahjongs() {
        List<Mahjong> newMahjongs = new ArrayList<>(allMahjongs.size());
        newMahjongs.addAll(allMahjongs);
        return newMahjongs;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 999; i++) {
            System.out.println(i + ":" + i % 10);
        }
    }

    /**
     * 把麻将枚举对象列表转换为麻将id列表
     */
    public static List<Integer> parseToIds(Collection<Mahjong> mahjongs) {
        if (mahjongs.size() == 0) {
            return Collections.emptyList();
        }

        List<Integer> mahjongIds = new ArrayList<>(mahjongs.size());
        for (Mahjong mahjong : mahjongs) {
            mahjongIds.add(mahjong.getId());
        }
        return mahjongIds;
    }

    /**
     * 把麻将id列表转为枚举对象列表
     */
    public static List<Mahjong> parseFromIds(Collection<Integer> mahjongIds) {
        if (mahjongIds.size() == 0) {
            return Collections.emptyList();
        }

        List<Mahjong> mahjongs = new ArrayList<>(mahjongIds.size());
        for (Integer mahjongId : mahjongIds) {
            mahjongs.add(parse(mahjongId));
        }
        return mahjongs;
    }

    /**
     * 把碰列表转为碰列表
     */
    public static List<List<Integer>> parseCombosToMahjongIds(Collection<Combo> Combos) {
        if (Combos.size() == 0) {
            return Collections.emptyList();
        }

        List<List<Integer>> comboIntegers = new ArrayList<>(Combos.size());
        for (Combo combo : Combos) {
            List<Integer> temp = parseToIds(combo.mahjongs);
            comboIntegers.add(temp);
        }
        return comboIntegers;
    }

    /**
     * 判断是否一样的麻将，例如都是1万
     */
    public static boolean isSame(List<Mahjong> toBeGangMahjongs) {
        if (toBeGangMahjongs.size() == 0) {
            return false;
        }
        Integer number = null;
        for (Mahjong toBeGangMahjong : toBeGangMahjongs) {
            if (number == null) {
                number = toBeGangMahjong.getNumber();
            } else {
                if (!number.equals(toBeGangMahjong.getNumber())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        //return "{\"id\":\"" + id + "\""
        //        + ", \"number\":\"" + number + "\""
        //        + ", \"name\":\"" + name + "\""
        //        + "}";
        return toSimpleString();
    }

    public String toSimpleString() {
        return "\"" + name + "\"";
    }

    /**
     * 获取字号
     */
    public Integer getZi() {
        return (this.id / 100) % 10;
    }

    /**
     * 获取麻将的数字，一万、一筒、一条为1，一万、一筒、一条为2
     */
    public Integer getDigit() {
        return this.number % 10;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
