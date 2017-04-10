package com.jinniu.commonjn.dao.impl;

import com.jinniu.commonjn.dao.BaseDao;
import com.jinniu.commonjn.model.Entity;
import com.jinniu.commonjn.model.Entity.Criteria;
import com.jinniu.commonjn.model.Entity.PrimaryKey;
import com.jinniu.commonjn.model.Entity.SimpleCriteria;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<K extends Serializable, E extends Entity> implements BaseDao<K, E> {

	private Class<E> entityClass;
	
	@Autowired
	protected SqlSessionTemplate sqlSessionTemplate;
	
	
	public K save(E entity) {
		return sqlSessionTemplate.insert(statement("save"), entity) == 1 ? (K) PrimaryKey.of(entity).getValue() : null;
	}


	public int delete(K id) {
		return delete(new SimpleCriteria().eq(PrimaryKey.of(entityClass).getName(), id));
	}


	public int delete(Criteria criteria) {
		return sqlSessionTemplate.delete(statement("delete"), criteria.toMapParameter());
	}

	
	public int update(E entity) {
		return update(entity, new SimpleCriteria().eq(PrimaryKey.of(entity).getName(), PrimaryKey.of(entity).getValue()));
	}


	public int update(E entity, Criteria criteria) {
		Map<String, Object> param = criteria.toMapParameter();
		param.put("entity", entity);
		return sqlSessionTemplate.update(statement("update"), param);
	}


	public E selectOne(K id) {
		return selectOne(new SimpleCriteria().eq(PrimaryKey.of(entityClass).getName(), id));
	}


	public E selectOne(Criteria criteria) {
		List<E> list = selectList(criteria);
		if (list == null) {
			return null;
		}
		int size = list.size();
		if (size == 0) {
			return null;
		}
		if (size == 1) {
			return list.get(0);
		}
		if (size > 1) {
			throw new TooManyResultsException("Expected 1 result (or null) to be returned, but found " + size);
		}
		return null;
	}


	public List<E> selectAll() {
		return selectList(null);
	}


	public List<E> selectList(Criteria criteria) {
		return sqlSessionTemplate.selectList(statement("selectList"), criteria == null ? null : criteria.toMapParameter());
	}

	
	public long selectCount(Criteria criteria) {
		return sqlSessionTemplate.selectOne(statement("selectCount"), criteria == null ? null : criteria.toMapParameter());
	}
	
	protected String statement(String id) {
		return String.format("com.jinniu.commonjn.dao.%sDao.%s", entityClass.getSimpleName(), id);
	}
	
	{
		entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

}