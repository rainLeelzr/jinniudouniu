package com.jinniu.commonjn.manager.scanTask.impl;

import com.jinniu.commonjn.manager.operate.Operate;

/**
 * 扫描是否可以软暗杠
 */
public class RuanAnGang extends RuanDaMingGang {

    @Override
    public Operate getOperate() {
        return Operate.RUAN_AN_GANG;
    }

}
