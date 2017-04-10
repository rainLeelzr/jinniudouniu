package com.jinniu.commonjn.dao;

import java.io.Serializable;
import java.util.List;

import com.jinniu.commonjn.model.Entity;
import com.jinniu.commonjn.model.Entity.Criteria;

/**
 * 基础 DAO 接口
 * 
 * @author fanlychie
 *
 * @param <K>
 *            主键类型
 * @param <E>
 *            实体类型
 */
public interface BaseDao<K extends Serializable, E extends Entity> {

	/**
	 * 保存
	 * 
	 * @param entity
	 *            实体类对象
	 *            
	 * @return 返回插入数据库的主键
	 */
	K save(E entity);

	/**
	 * 根据主键删除
	 * 
	 * @param id
	 *            主键
	 *            
	 * @return 返回操作成功的记录条数
	 */
	int delete(K id);

	/**
	 * 根据条件删除
	 * 
	 * @param criteria
	 *            条件对象
	 *            
	 * @return 返回操作成功的记录条数
	 */
	int delete(Criteria criteria);

	/**
	 * 根据主键更新
	 * 
	 * @param entity
	 *            实体类对象
	 *            
	 * @return 返回操作成功的记录条数
	 */
	int update(E entity);

	/**
	 * 根据条件更新
	 * 
	 * @param entity
	 *            实体类对象
	 * @param criteria
	 *            条件对象
	 *            
	 * @return 返回操作成功的记录条数
	 */
	int update(E entity, Criteria criteria);

	/**
	 * 根据主键查询
	 * 
	 * @param id
	 *            主键
	 *            
	 * @return
	 */
	E selectOne(K id);

	/**
	 * 根据条件查询
	 * 
	 * @param criteria
	 *            条件对象
	 * @return
	 */
	E selectOne(Criteria criteria);
	
	/**
	 * 查询所有
	 * 
	 * @return
	 */
	List<E> selectAll();

	/**
	 * 根据条件查询
	 * 
	 * @param criteria
	 *            条件对象
	 * @return
	 */
	List<E> selectList(Criteria criteria);

	/**
	 * 根据条件查询条数
	 * 
	 * @param criteria
	 *            条件对象
	 * @return
	 */
	long selectCount(Criteria criteria);

}