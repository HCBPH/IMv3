package com.ycj.imv3.mapper;

import com.ycj.imv3.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

/**
 * @author 53059
 * @date 2022/1/19 22:37
 */
public interface UserMapper {
    
    final String table_name = "USER";

    @Select("SELECT id AS uid,username, password FROM " + table_name + " where state=1")
    public ArrayList<User> getAll();

    @Select("SELECT id AS uid,username, password FROM " + table_name + " where name=#{name} and state=1")
    public User getUserByName(String name);

    @Select("SELECT id AS uid,username, password FROM " + table_name + " where id=#{uid} and state=1")
    public User getUserByUid(long uid);

}
