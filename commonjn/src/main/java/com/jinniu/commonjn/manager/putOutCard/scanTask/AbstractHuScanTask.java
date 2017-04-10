package com.jinniu.commonjn.manager.putOutCard.scanTask;


import com.jinniu.commonjn.manager.scanTask.BaseScanTask;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.Combo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 扫描用户是否可以吃胡
 */
public abstract class AbstractHuScanTask extends BaseScanTask {

    private static int[] NO_HU_SIZE = new int[]{1, 4, 7, 10, 13};
    private static int[] NO_HU_SIZE_QIDUI = new int[]{1, 3, 5, 7, 9, 11, 13};
    private boolean hasEyes = false;

    /**
     * 循环实现dimValue中的笛卡尔积
     *
     * @param dimValue 原始数据
     */
    protected static List<List<Mahjong>> circulate(List<List<Mahjong>> dimValue) {
        int total = 1;
        for (List<Mahjong> list : dimValue) {
            total *= list.size();
        }
        List<List<Mahjong>> myResult = new ArrayList<>(total);

        int itemLoopNum;
        int loopPerItem;
        int now = 1;
        for (List<Mahjong> list : dimValue) {
            now *= list.size();

            int index = 0;
            int currentSize = list.size();

            itemLoopNum = total / now;
            loopPerItem = total / (itemLoopNum * currentSize);
            int myIndex = 0;

            for (Mahjong string : list) {
                for (int i = 0; i < loopPerItem; i++) {
                    if (myIndex == list.size()) {
                        myIndex = 0;
                    }

                    for (int j = 0; j < itemLoopNum; j++) {
                        if (myResult.size() == index) {
                            List<Mahjong> temp = new ArrayList<>(dimValue
                                    .size());
                            temp.add(list.get(myIndex));
                            myResult.add(temp);
                        } else {
                            myResult.get(index).add(list.get(myIndex));
                        }
                        index++;
                    }
                    myIndex++;
                }

            }
        }

        return myResult;
    }

    /**
     * 判断给定的牌是否平胡
     */
    protected boolean isPinghu(List<Mahjong> handCards) {
        // 按麻将的字号分组
        Map<Integer, List<Mahjong>> ziHaoMahjongs = groupByZiHao(handCards);

        if (preCheck(ziHaoMahjongs, false)) {
            for (List<Mahjong> mahjongSet : ziHaoMahjongs.values()) {
                //log.debug("按麻将的字号分组:{}", mahjongSet);
                if (!checkPingHu(new ArrayList<>(mahjongSet))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断给定的牌是否七对
     */
    protected boolean isQiDui(List<Mahjong> handCards) {
        // 按麻将的字号分组
        Map<Integer, List<Mahjong>> ziHaoMahjongs = groupByZiHao(handCards);

        if (preCheck(ziHaoMahjongs, true)) {
            for (List<Mahjong> mahjongSet : ziHaoMahjongs.values()) {
                //log.debug("按麻将的字号分组:{}", mahjongSet);
                if (!checkQiDui(new ArrayList<>(mahjongSet))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断给定的牌是否碰碰胡
     */
    protected boolean isPengPengHu(List<Mahjong> handCards) {
        // 按麻将的字号分组
        Map<Integer, List<Mahjong>> ziHaoMahjongs = groupByZiHao(handCards);

        if (preCheck(ziHaoMahjongs, false)) {
            for (List<Mahjong> mahjongSet : ziHaoMahjongs.values()) {
                //log.debug("按麻将的字号分组:{}", mahjongSet);
                if (!checkPengPengHu(new ArrayList<>(mahjongSet))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 执行具体的胡牌算法前，先简单判断给定的牌是否符合必要的胡牌条件
     * 此检查适用碰碰胡、平胡
     */
    private boolean preCheck(Map<Integer, List<Mahjong>> ziHaoMahjongs, boolean isQiDui) {
        for (List<Mahjong> mahjongs : ziHaoMahjongs.values()) {
            // 检查每个字号组合的大小符不合胡牌大小

            int[] noHuSize = isQiDui ? NO_HU_SIZE_QIDUI : NO_HU_SIZE;

            for (int size : noHuSize) {
                if (size == mahjongs.size()) {
                    return false;
                }
            }

            // mahjongs.size() == 2只能为眼
            if (mahjongs.size() == 2) {
                Combo AA = AA(new ArrayList<>(mahjongs));
                if (AA == null) {
                    return false;
                }
                hasEyes = true;
            }
        }

        return true;
    }

    /**
     * 根据传入的list，组成AAA、ABC、AA组合
     * 组合成功，返回true，不成功返回false
     */
    private boolean checkPingHu(List<Mahjong> mahjongs) {
        if (mahjongs.size() == 0) {
            return true;
        }

        Combo combo = AAA(mahjongs);
        if (combo != null) {
            if (checkPingHu(mahjongs)) {
                return true;
            } else {
                putBackMahjongToList(combo, mahjongs);
                combo = ABC(mahjongs);
                if (combo != null) {
                    if (checkPingHu(mahjongs)) {
                        return true;
                    } else {
                        putBackMahjongToList(combo, mahjongs);
                        combo = AA(mahjongs);
                        if (combo != null) {
                            return checkPingHu(mahjongs);
                        } else {
                            return false;
                        }
                    }
                } else {
                    combo = AA(mahjongs);
                    if (combo != null) {
                        return checkPingHu(mahjongs);
                    } else {
                        return false;
                    }
                }
            }
        } else {
            combo = ABC(mahjongs);
            if (combo != null) {
                if (checkPingHu(mahjongs)) {
                    return true;
                } else {
                    putBackMahjongToList(combo, mahjongs);
                    combo = AA(mahjongs);
                    if (combo != null) {
                        return checkPingHu(mahjongs);
                    } else {
                        return false;
                    }
                }
            } else {
                combo = AA(mahjongs);
                if (combo != null) {
                    return checkPingHu(mahjongs);
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * 根据传入的list，组成AAA、ABC、AA组合
     * 组合成功，返回true，不成功返回false
     */
    //private boolean checkPingHu(List<Mahjong> mahjongs) {
    //    if (mahjongs.size() == 0) {
    //        return true;
    //    }
    //
    //    Combo combo = AAA(mahjongs);
    //    if (combo == null) {
    //        combo = ABC(mahjongs);
    //        if (combo == null) {
    //            combo = AA(mahjongs);
    //            if (combo == null) {
    //                return false;
    //            } else {
    //                if (hasEyes) {
    //                    return false;
    //                } else {
    //                    hasEyes = true;
    //                }
    //                return checkPingHu(mahjongs);
    //            }
    //        } else {
    //            if (mahjongs.size() == 0) {
    //                return true;
    //            } else {
    //                return checkPingHu(mahjongs);
    //            }
    //        }
    //    } else {
    //        if (mahjongs.size() == 0) {
    //            return true;
    //        } else {
    //            return checkPingHu(mahjongs);
    //        }
    //    }
    //}

    /**
     * 根据传入的list，组成AA组合
     * 组合成功，返回true，不成功返回false
     */
    private boolean checkQiDui(List<Mahjong> mahjongs) {
        if (mahjongs.size() == 0) {
            return true;
        }

        Combo combo = AA(mahjongs);
        if (combo == null) {
            return false;
        } else {
            if (mahjongs.size() == 0) {
                return true;
            } else {
                return checkQiDui(mahjongs);
            }
        }
    }

    /**
     * 根据传入的list，组成AAA、AA组合
     * 组合成功，返回true，不成功返回false
     */
    private boolean checkPengPengHu(List<Mahjong> mahjongs) {
        if (mahjongs.size() == 0) {
            return true;
        }

        Combo combo = AAA(mahjongs);
        if (combo == null) {
            combo = AA(mahjongs);
            if (combo == null) {
                return false;
            } else {
                return checkPengPengHu(mahjongs);
            }
        } else {
            if (mahjongs.size() == 0) {
                return true;
            } else {
                return checkPengPengHu(mahjongs);
            }
        }
    }

    /**
     * 从列表第一只为指定开始牌，找出一个AAA的组合
     */
    private Combo AAA(List<Mahjong> mahjongs) {
        if (mahjongs.size() < 3) {
            return null;
        }

        Combo combo = new Combo();
        combo.type = Combo.Type.AAA;
        List<Mahjong> AAAMahjong = new ArrayList<>(3);
        combo.mahjongs = AAAMahjong;
        AAAMahjong.add(mahjongs.get(0));
        mahjongs.remove(0);

        for (int i = 0; i < mahjongs.size(); i++) {
            if (AAAMahjong.get(0).getDigit().equals(mahjongs.get(i).getDigit())) {
                AAAMahjong.add(mahjongs.get(i));
                mahjongs.remove(i);
                break;
            }
        }

        if (AAAMahjong.size() != 2) {
            putBackMahjongToList(combo, mahjongs);
            return null;
        }

        for (int i = 0; i < mahjongs.size(); i++) {
            if (AAAMahjong.get(0).getDigit().equals(mahjongs.get(i).getDigit())) {
                AAAMahjong.add(mahjongs.get(i));
                mahjongs.remove(i);
                break;
            }
        }

        if (AAAMahjong.size() != 3) {
            putBackMahjongToList(combo, mahjongs);
            return null;
        }

        return combo;
    }

    /**
     * 从列表第一只为指定开始牌，找出一个ABC的组合
     */
    private Combo ABC(List<Mahjong> mahjongs) {
        if (mahjongs.size() < 3) {
            return null;
        }

        Combo combo = new Combo();
        combo.type = Combo.Type.ABC;
        List<Mahjong> ABCMahjong = new ArrayList<>(3);
        combo.mahjongs = ABCMahjong;
        ABCMahjong.add(mahjongs.get(0));
        mahjongs.remove(0);

        for (int i = 0; i < mahjongs.size(); i++) {
            if (ABCMahjong.get(0).getDigit() + 1 == mahjongs.get(i).getDigit()) {
                ABCMahjong.add(mahjongs.get(i));
                mahjongs.remove(i);
                break;
            }
        }

        if (ABCMahjong.size() != 2) {
            putBackMahjongToList(combo, mahjongs);
            return null;
        }

        for (int i = 0; i < mahjongs.size(); i++) {
            if (ABCMahjong.get(0).getDigit() + 2 == mahjongs.get(i).getDigit()) {
                ABCMahjong.add(mahjongs.get(i));
                mahjongs.remove(i);
                break;
            }
        }

        if (ABCMahjong.size() != 3) {
            putBackMahjongToList(combo, mahjongs);
            return null;
        }

        return combo;
    }

    /**
     * 从列表第一只为指定开始牌，找出一个AA的组合
     */
    private Combo AA(List<Mahjong> mahjongs) {
        Combo combo = new Combo();
        combo.type = Combo.Type.AA;
        List<Mahjong> AAMahjong = new ArrayList<>(2);
        combo.mahjongs = AAMahjong;
        AAMahjong.add(mahjongs.get(0));
        mahjongs.remove(0);

        for (int i = 0; i < mahjongs.size(); i++) {
            if (AAMahjong.get(0).getDigit().equals(mahjongs.get(i).getDigit())) {
                AAMahjong.add(mahjongs.get(i));
                mahjongs.remove(i);
                break;
            }
        }

        if (AAMahjong.size() != 2) {
            putBackMahjongToList(combo, mahjongs);
            return null;
        }

        return combo;
    }

    private void putBackMahjongToList(Combo combo, List<Mahjong> mahjongs) {
        for (Mahjong m : combo.mahjongs) {
            mahjongs.add(m);
        }
        Collections.sort(mahjongs);
    }
}
