package com.jinniu.commonjn.manager.putOutCard;

import com.jinniu.commonjn.manager.AbstractManager;
import com.jinniu.commonjn.manager.picker.PutOutCardPicker;
import com.jinniu.commonjn.manager.scanTask.impl.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 别人出牌后其他玩家可以的操作扫描管理器，
 * 如吃胡、大明杠、碰
 * 依次扫描scanTasks中的具体任务，得出所有玩家可以有的操作列表
 */
@Component
public class AfterPutOutCardManager extends AbstractManager implements InitializingBean {

    @Override
    protected void setPersonalCardInfoPicker() {
        this.personalCardInfoPicker = new PutOutCardPicker();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scanTasks = new ArrayList<>();
        // 硬胡
        scanTasks.add(ChiYingPengPengHu.class);
        scanTasks.add(ChiYingQiDuiHu.class);
        scanTasks.add(ChiYingPingHu.class);

        // 软胡
        scanTasks.add(ChiRuanPengPengHu.class);
        scanTasks.add(ChiRuanQiDuiHu.class);
        scanTasks.add(ChiRuanPingHu.class);

        // 硬大明杠
        scanTasks.add(YingDaMingGang.class);

        // 硬碰
        scanTasks.add(YingPeng.class);

        // 软大明杠
        //scanTasks.add(RuanDaMingGang.class);

        // 软碰
        //scanTasks.add(RuanPeng.class);

        setPersonalCardInfoPicker();
    }
}
