package com.john.dao;

import com.john.builder.User;

import java.util.List;

/**
 * @author:wenwei
 * @date:2020/02/22
 * @description:
 */
public interface IUserDao {
   public User findOne(int id, String username);
   public List<User> findList();

   public Boolean insertUser(User user);
   public Boolean updateUser(User user);
   public Boolean deleteUserById(Integer uid);
}
