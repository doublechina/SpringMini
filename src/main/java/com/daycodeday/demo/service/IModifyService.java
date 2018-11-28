package com.daycodeday.demo.service;

/**
 * 增删改业务
 *
 */
public interface IModifyService {

	/**
	 * 增加
	 */
	public String add(String name, String addr);
	
	/**
	 * 修改
	 */
	public String edit(Integer id, String name);
	
	/**
	 * 删除
	 */
	public String remove(Integer id);
	
}
