package com.jinniu.commonjn.manager.qiangGang;

import com.jinniu.commonjn.manager.AbstractManager;
import com.jinniu.commonjn.manager.operate.CanDoOperate;
import com.jinniu.commonjn.manager.operate.Operate;
import com.jinniu.commonjn.manager.picker.PutOutCardPicker;
import com.jinniu.commonjn.manager.scanTask.impl.*;
import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.MahjongGameData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自己明杠，扫描其他玩家有没有抢杠
 * 依次扫描scanTasks中的具体任务，得出所有玩家可以有的操作列表
 */
@Component
public class QiangGangManager extends AbstractManager implements InitializingBean {

    @Override
    protected void setPersonalCardInfoPicker() {
        this.personalCardInfoPicker = new PutOutCardPicker();
    }

    @Override
    public List<CanDoOperate> scan(MahjongGameData mahjongGameData, Mahjong putOutMahjong, User user) throws IllegalAccessException, InstantiationException {
        List<CanDoOperate> canDoOperates = super.scan(mahjongGameData, putOutMahjong, user);
        if (canDoOperates.size() > 0) {
            // 将碰碰胡、七对胡、平胡的操作转换为抢大明杠胡
            CanDoOperate canDoOperate = new CanDoOperate();
            canDoOperate.setRoomMember(canDoOperates.get(0).getRoomMember());
            canDoOperate.setSpecialUserId(canDoOperates.get(0).getSpecialUserId());
            canDoOperate.setSpecialMahjong(canDoOperates.get(0).getSpecialMahjong());
            canDoOperate.setOperates(Collections.singleton(Operate.QIANG_DA_MING_GANG_HU));

            canDoOperates = new ArrayList<>(1);
            canDoOperates.add(canDoOperate);
        }
        return canDoOperates;
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

        setPersonalCardInfoPicker();
    }
}
