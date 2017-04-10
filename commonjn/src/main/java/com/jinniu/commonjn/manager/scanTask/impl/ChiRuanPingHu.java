package com.jinniu.commonjn.manager.scanTask.impl;

import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.putOutCard.scanTask.AbstractHuScanTask;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.PersonalCardInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 扫描是否吃软平胡
 */
public class ChiRuanPingHu extends AbstractHuScanTask {

    @Override
    public Operate getOperate() {
        return Operate.CHI_RUAN_PING_HU;
    }

    ///**
    // * 递归实现dimValue中的笛卡尔积，结果放在result中
    // *
    // * @param dimValue 原始数据
    // * @param result   结果数据
    // * @param layer    dimValue的层数
    // * @param curList  每次笛卡尔积的结果
    // */
    //private static void recursive(List<List<String>> dimValue, List<List<String>> result, int layer, List<String> curList) {
    //    if (layer < dimValue.size() - 1) {
    //        if (dimValue.get(layer).size() == 0) {
    //            recursive(dimValue, result, layer + 1, curList);
    //        } else {
    //            for (int i = 0; i < dimValue.get(layer).size(); i++) {
    //                List<String> list = new ArrayList<String>(curList);
    //                list.add(dimValue.get(layer).get(i));
    //                recursive(dimValue, result, layer + 1, list);
    //            }
    //        }
    //    } else if (layer == dimValue.size() - 1) {
    //        if (dimValue.get(layer).size() == 0) {
    //            result.add(curList);
    //        } else {
    //            for (int i = 0; i < dimValue.get(layer).size(); i++) {
    //                List<String> list = new ArrayList<String>(curList);
    //                list.add(dimValue.get(layer).get(i));
    //                result.add(list);
    //            }
    //        }
    //    }
    //}
    //
    ///**
    // * 循环实现dimValue中的笛卡尔积，结果放在result中
    // *
    // * @param dimValue 原始数据
    // * @param result   结果数据
    // */
    //private static void circulate(List<List<String>> dimValue, List<List<String>> result) {
    //    int total = 1;
    //    for (List<String> list : dimValue) {
    //        total *= list.size();
    //    }
    //    String[] myResult = new String[total];
    //
    //    int itemLoopNum = 1;
    //    int loopPerItem = 1;
    //    int now = 1;
    //    for (List<String> list : dimValue) {
    //        now *= list.size();
    //
    //        int index = 0;
    //        int currentSize = list.size();
    //
    //        itemLoopNum = total / now;
    //        loopPerItem = total / (itemLoopNum * currentSize);
    //        int myIndex = 0;
    //
    //        for (String string : list) {
    //            for (int i = 0; i < loopPerItem; i++) {
    //                if (myIndex == list.size()) {
    //                    myIndex = 0;
    //                }
    //
    //                for (int j = 0; j < itemLoopNum; j++) {
    //
    //                    myResult[index] = (myResult[index] == null ? "" : myResult[index] + ",") + list.get(myIndex);
    //                    index++;
    //                }
    //                myIndex++;
    //            }
    //
    //        }
    //    }
    //
    //    List<String> stringResult = Arrays.asList(myResult);
    //    for (String string : stringResult) {
    //        String[] stringArray = string.split(",");
    //        result.add(Arrays.asList(stringArray));
    //    }
    //}
    //
    ///**
    // * 程序入口
    // *
    // * @param args
    // */
    //public static void main(String[] args) {
    //    List<String> list1 = new ArrayList<String>();
    //    list1.add("1一万");
    //    list1.add("1二万");
    //
    //    List<String> list2 = new ArrayList<String>();
    //    list2.add("2一万");
    //    list2.add("2二万");
    //
    //    List<String> list3 = new ArrayList<String>();
    //    list3.add("3一万");
    //    list3.add("3二万");
    //
    //    List<String> list4 = new ArrayList<String>();
    //    list4.add("4一万");
    //    list4.add("4二万");
    //
    //    List<List<String>> dimValue = new ArrayList<List<String>>();
    //    dimValue.add(list1);
    //    dimValue.add(list2);
    //    dimValue.add(list3);
    //    dimValue.add(list4);
    //
    //    //List<List<String>> recursiveResult = new ArrayList<List<String>>();
    //    //// 递归实现笛卡尔积
    //    //recursive(dimValue, recursiveResult, 0, new ArrayList<String>());
    //    //
    //    //System.out.println("递归实现笛卡尔乘积: 共 " + recursiveResult.size() + " 个结果");
    //    //for (List<String> list : recursiveResult) {
    //    //    for (String string : list) {
    //    //        System.out.print(string + " ");
    //    //    }
    //    //    System.out.println();
    //    //}
    //
    //    List<List<String>> circulateResult = new ArrayList<List<String>>();
    //    long start = System.currentTimeMillis();
    //    circulate(dimValue, circulateResult);
    //    long end = System.currentTimeMillis();
    //    System.out.println("循环实现笛卡尔时间（ms）：" + (end - start));
    //    System.out.println("循环实现笛卡尔乘积: 共 " + circulateResult.size() + " 个结果");
    //    for (List<String> list : circulateResult) {
    //        for (String string : list) {
    //            System.out.print(string + " ");
    //        }
    //        System.out.println();
    //    }
    //}


    @Override
    public boolean doScan(PersonalCardInfo personalCardInfo)
            throws InstantiationException, IllegalAccessException {
        List<Mahjong> handCards = new ArrayList<>(personalCardInfo.getHandCards());
        handCards.add(specifiedMahjong);

        List<Mahjong> myBaoMahjongs = getMyBaoMahjongs(handCards);

        log.debug("座位{}有{}只宝牌", personalCardInfo.getRoomMember().getSeat(),
                myBaoMahjongs.size());

        // 如果没有宝牌，则不能软平胡
        if (myBaoMahjongs.size() == 0) {
            return false;
        }

        // 创建用于笛卡尔的集合
        List<List<Mahjong>> baoMahjongs = new ArrayList<>(myBaoMahjongs.size());
        for (Mahjong myBaoMahjong : myBaoMahjongs) {
            List<Mahjong> m = new ArrayList<>(
                    getMahjongGameData().getBaoMahjongMakeUpMahjongs());
            m.add(myBaoMahjong);
            baoMahjongs.add(m);
        }

        // 用到笛卡尔积
        List<List<Mahjong>> circulateResult = circulate(baoMahjongs);
        for (List<Mahjong> mahjongs : circulateResult) {
            Collections.sort(handCards);
            //log.debug("座位{}进行软平胡扫描宝牌变换前：{}",
            //        personalCardInfo.getRoomMember().getSeat(),
            //        handCards
            //);

            for (int i = 0; i < myBaoMahjongs.size(); i++) {
                handCards.remove(myBaoMahjongs.get(i));
                handCards.add(mahjongs.get(i));
            }
            Collections.sort(handCards);
            //log.debug("座位{}进行软平胡扫描宝牌变换后：{}",
            //        personalCardInfo.getRoomMember().getSeat(),
            //        handCards
            //);
            if (isPinghu(handCards)) {
                return true;
            }

            for (int i = 0; i < myBaoMahjongs.size(); i++) {
                handCards.remove(mahjongs.get(i));
                handCards.add(myBaoMahjongs.get(i));
            }
        }

        return false;
    }
}
