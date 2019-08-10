package com.pldk.modules.system.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pldk.modules.system.domain.User;

/**
 * @author Pldk
 * @date 2019/07/01
 */
public interface FirstUserRepository extends BaseRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	   /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
    public User findByUsername(String username);

    /**
     * 根据用户名查询用户数据,且排查指定ID的用户
     * @param username 用户名
     * @param id 排除的用户ID
     * @return 用户数据
     */
    public User findByUsernameAndIdNot(String username, Long id);
}