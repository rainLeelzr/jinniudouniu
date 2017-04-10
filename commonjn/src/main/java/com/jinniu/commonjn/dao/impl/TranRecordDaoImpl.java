package com.jinniu.commonjn.dao.impl;

import com.jinniu.commonjn.dao.TranRecordDao;
import com.jinniu.commonjn.model.TranRecord;
import org.springframework.stereotype.Repository;

@Repository
public class TranRecordDaoImpl extends BaseDaoImpl<Integer, TranRecord> implements TranRecordDao {

    @Override
    public Long countForPrizeDraw(TranRecord tranRecord) {
        return sqlSessionTemplate.selectOne(
                statement("countForPrizeDraw"),
                tranRecord
        );
    }
}