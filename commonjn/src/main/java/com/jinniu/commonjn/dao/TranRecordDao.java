package com.jinniu.commonjn.dao;

import com.jinniu.commonjn.model.TranRecord;

public interface TranRecordDao extends BaseDao<Integer, TranRecord> {
   Long countForPrizeDraw(TranRecord tranRecord);
}