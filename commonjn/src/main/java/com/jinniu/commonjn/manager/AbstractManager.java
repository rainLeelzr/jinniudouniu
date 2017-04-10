package com.jinniu.commonjn.manager;

import com.jinniu.commonjn.manager.operate.CanDoOperate;
import com.jinniu.commonjn.manager.picker.PersonalCardInfoPicker;
import com.jinniu.commonjn.manager.scanTask.BaseScanTask;
import com.jinniu.commonjn.model.User;
import com.jinniu.commonjn.model.mahjong.Mahjong;
import com.jinniu.commonjn.model.mahjong.MahjongGameData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 操作扫描管理器，
 * * 依次扫描scanTasks中的具体任务，得出所有玩家可以有的操作列表
 */
public abstract class AbstractManager {

    protected static final Logger log = LoggerFactory.getLogger(AbstractManager.class);

    /**
     * 已注册的扫描任务
     */
    protected List<Class<? extends BaseScanTask>> scanTasks;

    protected PersonalCardInfoPicker personalCardInfoPicker;

    protected abstract void setPersonalCardInfoPicker();

    public List<CanDoOperate> scan(MahjongGameData mahjongGameData, Mahjong putOutMahjong, User user) throws
            IllegalAccessException,
            InstantiationException {
        List<CanDoOperate> canDoOperates = new ArrayList<>();
        for (Class<? extends BaseScanTask> scanTask : scanTasks) {
            BaseScanTask task = scanTask.newInstance();
            task.setMahjongGameData(mahjongGameData);
            task.setSpecifiedMahjong(putOutMahjong);
            task.setUser(user);
            task.setCanOperates(canDoOperates);
            task.setPersonalCardInfoPicker(personalCardInfoPicker);
            task.scan();
        }
        Iterator<CanDoOperate> it = canDoOperates.iterator();
        while (it.hasNext()) {
            CanDoOperate next = it.next();
            if (next.getOperates().size() == 0) {
                it.remove();
            }
        }
        Collections.sort(canDoOperates);
        log.debug("最终扫描结果：{}", canDoOperates);
        return canDoOperates;
    }


}
