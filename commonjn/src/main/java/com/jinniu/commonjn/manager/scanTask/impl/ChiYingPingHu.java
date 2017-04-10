package com.jinniu.commonjn.manager.scanTask.impl;

import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.putOutCard.scanTask.AbstractHuScanTask;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫描是否吃硬平胡
 */
public class ChiYingPingHu extends AbstractHuScanTask {

    @Override
    public Operate getOperate() {
        return Operate.CHI_YING_PING_HU;
    }

    private static int[] noHuSize = new int[]{1, 4, 7, 10, 13};

    @Override
    public boolean doScan(PersonalCardInfo personalCardInfo) throws InstantiationException, IllegalAccessException {
        List<Mahjong> handCards = new ArrayList<>(personalCardInfo.getHandCards());
        handCards.add(specifiedMahjong);
        return isPinghu(handCards);
    }

    //public boolean isPingHu(PersonalCardInfo personalCardInfo) {
    //    Set<Mahjong> handCards = new HashSet<>(personalCardInfo.getHandCards());
    //    handCards.add(specifiedMahjong);
    //
    //    // 按麻将的字号分组
    //    Map<Integer, Set<Mahjong>> ziHaoMahjongs = groupByZiHao(handCards);
    //
    //    /**
    //     * 字号组内牌型举例
    //     * "二万", "二万", "二万"
    //     * "二筒", "五筒", "八筒", "八筒", "九筒",
    //     * "一条", "二条", "三条",
    //     * "红中", "发财"
    //     */
    //
    //    // 判断每个字号组是否满足硬平胡
    //    int[] elementSize = new int[]{1, 4, 7, 10, 13};
    //    for (Set<Mahjong> mahjongs : ziHaoMahjongs.values()) {
    //        //1: 如果组内元素个数等于1, 4, 7, 10, 13,则肯定不是硬平胡
    //        for (int size : elementSize) {
    //            if (size == mahjongs.size()) {
    //                return false;
    //            }
    //        }
    //
    //        //2: 找出肯定是眼的组合。如果找到了眼，以后又找到眼，则肯定不是平胡
    //        if (mahjongs.size() == 2) {// size等于2，只可能是眼
    //            if (!size2(mahjongs)) {
    //                return false;
    //            }
    //        }
    //
    //        // mahjongs.size() == 3，只能是顺子或3只一样
    //        /*
    //         * 判断是不是番字，是番字则只能3只一样
    //         * 不是番字，可以是顺子或3只一样
    //         */
    //        if (mahjongs.size() == 3) {
    //            if (!size3(mahjongs)) {
    //                return false;
    //            }
    //        }
    //
    //        // 如果mahjongs.size() == 5，则必须有一对是眼，而且之前没有出现过眼
    //        if (mahjongs.size() == 5) {
    //            if (!size5(mahjongs)) {
    //                return false;
    //            }
    //        }
    //
    //        // 如果mahjongs.size() == 6，则有两个组合：
    //        // 两组都是3只相同
    //        // 或者一组相同一组顺子
    //        if (mahjongs.size() == 6) {
    //            if (!size6(mahjongs)) {
    //                return false;
    //            }
    //        }
    //
    //        // 如果mahjongs.size() == 8
    //        if (mahjongs.size() == 8) {
    //            if (!size8(mahjongs)) {
    //                return false;
    //            }
    //        }
    //
    //
    //    }
    //
    //    return true;
    //}
    //
    ///**
    // * 必须有一对眼。
    // * 可行组合：
    // * 两个对对碰加一对眼
    // * 一个对对碰一个顺子一对眼
    // * 二个顺子一对眼
    // */
    //private boolean size8(Set<Mahjong> mahjongs) {
    //    // 之前已经有眼，则这个mahjongs.size() == 8不管是不是眼，都不是平胡
    //    if (hasEye) {
    //        return false;
    //    }
    //
    //    //（麻将已经升序）
    //    List<Mahjong> m = new ArrayList<>(mahjongs);
    //    log.debug("判断是否硬平胡，字号组中size为{}时，麻将：{}", mahjongs.size(), m);
    //
    //    Integer digit1 = m.get(0).getDigit();
    //    Integer digit2 = m.get(1).getDigit();
    //    Integer digit3 = m.get(2).getDigit();
    //    Integer digit4 = m.get(3).getDigit();
    //    Integer digit5 = m.get(4).getDigit();
    //    Integer digit6 = m.get(5).getDigit();
    //    Integer digit7 = m.get(6).getDigit();
    //    Integer digit8 = m.get(7).getDigit();
    //
    //
    //    /*
    //     * 先判断两个对对碰加一对眼的情况
    //     * 1 1 1 2 2 2 3 3
    //     * 1 1 1 2 2 3 3 3
    //     * 1 1 2 2 2 3 3 3
    //     * 眼的位置可能在前，中，后
    //     */
    //    /*
    //     * 四只相同的情况
    //     *
    //     * 1 1 1 1 2 2 2 2 两组都是4只，不可能胡
    //     *
    //     * 1 1 1 1 2 3 3 3 第5、6、7、8四只一样，第1、2、3组成对对碰，4、5、6组成顺子，第7、8组成眼
    //     * 1 1 1 1 2 2 3 3 第5、6、7、8四只一样，第1、5、7组成顺子，2、6、8组成顺子，第3、4组成眼
    //     *                 第5、6、7、8四只一样，第1、2组成眼，3、5、7组成顺子，4、6、8组成顺子
    //     * 1 1 1 1 2 2 2 3 第5、6、7、8四只一样，第1、5、8组成顺子，2、3、4组成对对碰，第6、7组成眼
    //     *
    //     * 1 2 2 2 2 3 4 4 第2、3、4、5四只一样，第1、2、6组成顺子，3、4、5组成对对碰，第7、8组成眼
    //     * 1 2 2 2 2 3 3 4 第2、3、4、5四只一样，第1、2、6组成顺子，3、4组成眼，第5、7、8组成顺子
    //     *
    //     * 1 2 3 3 3 3 4 4 第3、4、5、6四只一样，第1、2、3组成顺子，4、5、6组成对对碰，第7、8组成眼
    //     * 1 2 3 3 3 3 4 5 第3、4、5、6四只一样，第1、2、3组成顺子，4、5组成眼， 第6、7、8组成顺子
    //     * 2 2 3 3 3 3 4 5 第3、4、5、6四只一样，第1、2组成眼，3、4、5组成对对碰，第6、7、8组成顺子
    //     * 2 2 3 3 3 3 4 4 第3、4、5、6四只一样，第1、3、7组成顺子，第2、4、8组成顺子，第5、6组成眼
    //     *
    //     * 1 1 4 5 5 5 5 6 第4、5、6、7四只一样，第1、2组成眼，3、7、8组成顺子，第4、5、6组成对对碰
    //     * 3 4 4 5 5 5 5 6 第4、5、6、7四只一样，第1、2、4组成顺子，3、5、8组成顺子，第6、7组成眼
    //     *
    //     * 4 4 4 5 6 6 6 6 第5、6、7、8四只一样，第1、2组成眼，3、4、5组成顺子，第6、7、8组成对对碰
    //     * 4 4 5 5 6 6 6 6 第5、6、7、8四只一样，第1、3、5组成顺子，2、4、6组成顺子，第7、8组成眼
    //     * 4 5 5 5 6 6 6 6 第5、6、7、8四只一样，第1、2、5组成顺子，第3、4组成眼，6、7、8组成对对碰
    //     */
    //    if (digit1.equals(digit2) && digit1.equals(digit3)) {
    //        // 前面3个组成对对碰
    //        if (digit4.equals(digit5) && digit4.equals(digit6)) {
    //            // 第2个组合是对对碰,则第3个组合是眼
    //            if (!digit7.equals(digit8)) {
    //                return false;
    //            }
    //        } else if (digit6.equals(digit7) && digit6.equals(digit8)) {
    //            // 第3个组合是对对碰,则第2个组合是眼
    //            if (!digit4.equals(digit5)) {
    //                return false;
    //            }
    //        }
    //    } else if (digit3.equals(digit4) && digit3.equals(digit5)) {
    //        // 第3、4、5组成对对碰
    //        if (digit6.equals(digit7) && digit6.equals(digit8)) {
    //            if (!digit1.equals(digit2)) {
    //                return false;
    //            }
    //        }
    //    }
    //
    //    /*
    //     * 一个对对碰一个顺子一对眼
    //     * 1 1 1 2 3 4 5 5
    //     * 1 1 1 1 2 3 3 3
    //     *
    //     */
    //
    //    return true;
    //}
    //
    ///**
    // * 可行的组合
    // * 1 1 1 2 2 2
    // * 1 1 1 2 3 4
    // * 1 1 1 5 6 7
    // * 1 1 1 1 2 3
    // * <p>
    // * 1 2 3 4 4 4
    // * 1 2 3 3 4 5
    // * 1 2 3 3 3 3
    // * <p>
    // * 1 1 2 2 3 3
    // * <p>
    // * 不可行的组合
    // * 1 1 1 2 3 3
    // * 1 1 1 8 8 9
    // * 总结：如果前面3只一样，则后面是3只一样或是顺子
    // * 如果前面2只一样，则第1、3、5只和2、4、6只是顺子
    // * 如果前面3只顺子，则后面是3只一样或是顺子
    // */
    //private boolean size6(Set<Mahjong> mahjongs) {
    //    //（麻将已经升序）
    //    List<Mahjong> m = new ArrayList<>(mahjongs);
    //    log.debug("判断是否硬平胡，字号组中size为{}时，麻将：{}", mahjongs.size(), m);
    //
    //    if (m.get(0).getNumber().equals(m.get(1).getNumber())
    //            && m.get(0).getNumber().equals(m.get(2).getNumber())
    //            ) {
    //        // 如果前面3只一样，则后面是3只一样或是顺子
    //        if (m.get(3).getNumber().equals(m.get(4).getNumber())) {
    //            // 是否3只一样
    //            if (!m.get(3).getNumber().equals(m.get(4).getNumber())) {
    //                return false;
    //            }
    //        } else {
    //            // 是否顺子
    //            Integer digit4 = m.get(3).getDigit();
    //            Integer digit5 = m.get(4).getDigit();
    //            if (digit4 + 1 != digit5) {
    //                return false;
    //            }
    //            Integer digit6 = m.get(5).getDigit();
    //            if (digit5 + 1 != digit6) {
    //                return false;
    //            }
    //        }
    //    } else if (m.get(0).getNumber().equals(m.get(1).getNumber())) {
    //        /*
    //         * 如果前面2只一样，则第1、3、5只和2、4、6只是顺子
    //         */
    //        // 1、3、5
    //        Integer digit1 = m.get(0).getDigit();
    //        Integer digit3 = m.get(2).getDigit();
    //        if (digit1 + 1 != digit3) {
    //            return false;
    //        }
    //        Integer digit5 = m.get(4).getDigit();
    //        if (digit3 + 1 != digit5) {
    //            return false;
    //        }
    //
    //        // 2、4、6
    //        Integer digit2 = m.get(1).getDigit();
    //        Integer digit4 = m.get(3).getDigit();
    //        if (digit2 + 1 != digit4) {
    //            return false;
    //        }
    //        Integer digit6 = m.get(5).getDigit();
    //        if (digit4 + 1 != digit6) {
    //            return false;
    //        }
    //    } else {
    //        /*
    //         * 剩下一种情况：前面3只顺子，后面是3只一样或是顺子
    //         */
    //        // 判断前面3只顺子
    //        Integer digit1 = m.get(0).getDigit();
    //        Integer digit2 = m.get(1).getDigit();
    //        if (digit1 + 1 != digit2) {
    //            return false;
    //        }
    //        Integer digit3 = m.get(2).getDigit();
    //        if (digit2 + 1 != digit3) {
    //            return false;
    //        }
    //
    //        // 判断后面是3只一样或是顺子
    //        Integer digit4 = m.get(3).getDigit();
    //        Integer digit5 = m.get(4).getDigit();
    //        Integer digit6 = m.get(5).getDigit();
    //        if (digit4.equals(digit5)) {
    //            if (!digit4.equals(digit6)) {
    //                return false;
    //            }
    //        } else {
    //            if (digit4 + 1 != digit5) {
    //                return false;
    //            }
    //            if (digit5 + 1 != digit6) {
    //                return false;
    //            }
    //        }
    //    }
    //
    //    return true;
    //}
    //
    //private boolean size2(Set<Mahjong> mahjongs) {
    //    // 之前已经有眼，则这个mahjongs.size() == 2不管是不是眼，都不是平胡
    //    if (hasEye) {
    //        return false;
    //    }
    //
    //    Integer firstMahjongNumber = null;
    //    for (Mahjong mahjong : mahjongs) {
    //        if (firstMahjongNumber == null) {
    //            firstMahjongNumber = mahjong.getNumber();
    //            continue;
    //        }
    //
    //        //如果不是眼，则肯定不是平胡
    //        if (!firstMahjongNumber.equals(mahjong.getNumber())) {
    //            return false;
    //        }
    //
    //        hasEye = true;
    //    }
    //
    //    return true;
    //}
    //
    //private boolean size3(Set<Mahjong> mahjongs) {
    //    Integer ziHao = null;
    //    Integer firstNumber = null;
    //    Integer firstDigit = null;
    //    // 番字的判断
    //    Iterator<Mahjong> iterator = mahjongs.iterator();
    //    int i = 0;
    //    while (iterator.hasNext()) {
    //        Mahjong mahjong = iterator.next();
    //
    //        if (i == 0) {
    //            ziHao = mahjong.getZi();
    //            firstNumber = mahjong.getNumber();
    //            firstDigit = mahjong.getDigit();
    //            if (!Mahjong.FAN_ZI.equals(ziHao)) {
    //                break;
    //            }
    //        } else {
    //            // 判断麻将是否和第一个相同
    //            if (!mahjong.getNumber().equals(firstNumber)) {
    //                return false;
    //            }
    //        }
    //
    //        i++;
    //    }
    //
    //    // 不是番字的判断
    //    iterator = mahjongs.iterator();
    //    i = 0;
    //    Integer secondNumber = null;
    //    Integer secondDigit = null;
    //    while (iterator.hasNext()) {
    //        Mahjong mahjong = iterator.next();
    //        if (i == 1) {
    //            secondNumber = mahjong.getNumber();
    //            secondDigit = mahjong.getDigit();
    //        } else if (i == 2) {
    //            // 如果第一个和第二个相等，则第三个也要相等
    //            if (firstNumber.equals(secondNumber)) {
    //                if (!firstNumber.equals(mahjong.getNumber())) {
    //                    return false;
    //                }
    //            } else {
    //                // 第一、二、三个要组成顺子（所有麻将已经升序）
    //                // 1 2 3 4 5 6 7 8 9 firstDigit要少于8
    //                if (firstDigit > 7) {
    //                    return false;
    //                } else {
    //                    if (firstDigit + 1 != secondDigit) {
    //                        return false;
    //                    }
    //                    if (firstDigit + 2 != mahjong.getDigit()) {
    //                        return false;
    //                    }
    //                }
    //            }
    //        }
    //
    //        i++;
    //    }
    //    return true;
    //}
    //
    ///**
    // * 必须有一对是眼，而且之前没有出现过眼
    // */
    //private boolean size5(Set<Mahjong> mahjongs) {
    //    if (hasEye) {
    //        return false;
    //    }
    //
    //    List<Mahjong> m = new ArrayList<>(mahjongs);
    //    log.debug("判断是否硬平胡，字号组中size为5时，麻将：{}", m);
    //
    //    // 判断有没有杠，有则不能胡.
    //    // 第1、2、3、4个麻将或2、3、4、5个麻将一样，则是有杠
    //    if ((m.get(0).getNumber().equals(m.get(1).getNumber())
    //            && m.get(0).getNumber().equals(m.get(2).getNumber())
    //            && m.get(0).getNumber().equals(m.get(3).getNumber())
    //    ) ||
    //            (m.get(1).getNumber().equals(m.get(2).getNumber())
    //                    && m.get(1).getNumber().equals(m.get(3).getNumber())
    //                    && m.get(1).getNumber().equals(m.get(4).getNumber())
    //            )
    //            ) {
    //        return false;
    //    }
    //
    //    // 判断有没有3只相等，如果3只相等，则另外两只能是眼
    //    // 4种情况例子：1 1 1 2 2
    //    // 1 2 2 2 3
    //    // 1 2 3 3 3
    //    // 1 1 2 2 2
    //    if (m.get(1).getNumber().equals(m.get(2).getNumber())
    //            && m.get(1).getNumber().equals(m.get(3).getNumber())
    //            ) {
    //        //如果中间3只相等，则不能胡
    //        return false;
    //    } else if (m.get(0).getNumber().equals(m.get(1).getNumber())
    //            && m.get(0).getNumber().equals(m.get(2).getNumber())
    //            ) {
    //        //如果前面3只相等，则后面2只必须相等
    //        if (!m.get(3).getNumber().equals(m.get(4).getNumber())) {
    //            return false;
    //        }
    //    } else if (m.get(2).getNumber().equals(m.get(3).getNumber())
    //            && m.get(2).getNumber().equals(m.get(4).getNumber())
    //            ) {
    //        //如果后面3只相等，则前面2只必须相等
    //        if (!m.get(0).getNumber().equals(m.get(1).getNumber())) {
    //            return false;
    //        }
    //    }
    //
    //    return true;
    //}
    //
    ///**
    // * 必须是AAA或ABC组合
    // */
    //private boolean canHuWithoutEye(Set<Mahjong> mahjongs) {
    //    //（麻将已经升序）
    //    List<Mahjong> m = new ArrayList<>(mahjongs);
    //
    //    List<Combo[]> combined = new ArrayList<>(4);
    //    Combo[] firstCombos = new Combo[2];
    //    firstCombos[0] = new Combo();
    //    firstCombos[0].setMahjongs(new Mahjong[3]);
    //    firstCombos[0].getMahjongs()[0] = m.get(0);
    //    m.remove(0);
    //
    //    if (m.get(0).equals(firstCombos[0].getMahjongs()[0])) {
    //        firstCombos[0].getMahjongs()[1] = m.get(0);
    //        firstCombos[0].setType(Combo.Type.AAA.getType());
    //        m.remove(0);
    //    } else if (firstCombos[0].getMahjongs()[0].getDigit() + 1 == m.get(0)
    //            .getDigit()) {
    //        firstCombos[0].getMahjongs()[1] = m.get(0);
    //        firstCombos[0].setType(Combo.Type.ABC.getType());
    //        m.remove(0);
    //    } else {
    //        return false;
    //    }
    //
    //    Integer wantDigit = null;
    //    if (firstCombos[0].getType() == Combo.Type.AAA.getType()) {
    //        wantDigit = firstCombos[0].getMahjongs()[0].getDigit();
    //    } else if (firstCombos[0].getType() == Combo.Type.ABC.getType()) {
    //        wantDigit = firstCombos[0].getMahjongs()[1].getDigit() + 1;
    //    }
    //
    //    for (int i = 0; i < m.size(); i++) {
    //        if (m.get(i).getDigit().equals(wantDigit)) {
    //            firstCombos[0].getMahjongs()[3] = m.get(i);
    //            m.remove(i);
    //        }
    //    }
    //
    //    if(firstCombos[0].getMahjongs()[3] == null){
    //        // 此组合不行，需要删除此组合。
    //        // 判断上一层组合，是否AAA ABC都试过了，如果试过，再判断上一层组合
    //    }
    //
    //    if (m.size() == 0) {
    //        if (firstCombos[0].getMahjongs()[3] != null) {
    //            return true;
    //        } else {
    //            return false;
    //        }
    //    }else{
    //
    //    }
    //
    //
    //    return true;
    //}
    //
    //private boolean canHuWithEye(Set<Mahjong> mahjongs) {
    //    return true;
    //}

}
