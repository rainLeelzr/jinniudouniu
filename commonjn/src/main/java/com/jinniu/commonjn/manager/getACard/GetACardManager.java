package com.jinniu.commonjn.manager.getACard;

import com.jinniu.commonjn.manager.AbstractManager;
import com.jinniu.commonjn.manager.picker.GetACardPicker;
import com.jinniu.commonjn.manager.scanTask.impl.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 自己摸到一张牌，可以的操作扫描管理器，
 * 如自摸、暗杠，加杠
 * 依次扫描scanTasks中的具体任务，得出所有玩家可以有的操作列表
 */
@Component
public class GetACardManager extends AbstractManager implements InitializingBean {

    @Override
    protected void setPersonalCardInfoPicker() {
        this.personalCardInfoPicker = new GetACardPicker();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scanTasks = new ArrayList<>();

        // 硬胡
        scanTasks.add(ZiMoYingPengPengHu.class);
        scanTasks.add(ZiMoYingQiDuiHu.class);
        scanTasks.add(ZiMoYingPingHu.class);

        // 软胡
        scanTasks.add(ZiMoRuanPengPengHu.class);
        scanTasks.add(ZiMoRuanQiDuiHu.class);
        scanTasks.add(ZiMoRuanPingHu.class);

        // 硬暗杠
        scanTasks.add(YingAnGang.class);

        // 硬加杠
        scanTasks.add(YingJiaGang.class);

        // 软暗杠
        //scanTasks.add(RuanAnGang.class);

        // 软加杠
        //scanTasks.add(RuanJiaGang.class);

        setPersonalCardInfoPicker();
    }
}
