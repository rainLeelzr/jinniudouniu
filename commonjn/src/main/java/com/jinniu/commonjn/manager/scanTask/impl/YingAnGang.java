package com.jinniu.commonjn.manager.scanTask.impl;

import com.jinniu.commonjn.manager.operate.Operate;

/**
 * 扫描是否可以硬暗杠
 */
public class YingAnGang extends YingDaMingGang {

    @Override
    public Operate getOperate() {
        return Operate.YING_AN_GANG;
    }

}
