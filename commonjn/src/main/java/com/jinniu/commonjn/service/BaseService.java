package com.jinniu.commonjn.service;

import java.io.Serializable;
import java.util.List;

import com.jinniu.commonjn.model.Entity;
import com.jinniu.commonjn.model.Entity.Criteria;
import com.jinniu.commonjn.model.Entity.Pagination;

/**
 * 基础 Service 接口
 * 
 * @author fanlychie
 *
 * @param <K>
 *            主键类型
 * @param <E>
 *            实体类型
 */
public interface BaseService<K extends Serializable, E extends Entity> {

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
	
	/**
	 * 根据条件分页查询, 
	 * 
	 * limit 以 pagination 参数为准, pagination 参数中若有 page 参数则以 page 参数做
	 * 
	 * limit 参数, 若没有 page 参数则以 offset 参数做 limit 查询, 若有检索参数值 search, 
	 * 
	 * 除非设置 pagination 的 field 参数的值才以 field-search 的值作为条件查询, 
	 * 
	 * 否则以 criteria 条件参数为准进行查询分页
	 * 
	 * @param criteria
	 *            条件对象
	 * @param pagination
	 *            分页对象参数
	 * 
	 * @return 返回分页对象
	 */
	Pagination selectPage(Criteria criteria, Pagination pagination);
	
}