package com.ycj.imv3.mapper;

import com.ycj.imv3.entity.Group;
import com.ycj.imv3.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 53059
 * @date 2022/1/30 13:16
 */
public interface GroupMembersMapper {
    final String table_name = "IM_GROUP_MEMBERS";

    @Insert("insert into "+table_name+"(gid, uid) values (#{gid}, #{uid})")
    public int join(@Param("gid") Long gid, @Param("uid") Long uid);

    @Update("update "+table_name+" set state=0 where gid=#{gid} and #{uid}")
    public int exit(@Param("gid") Long gid, @Param("uid") Long uid);

    @Delete("delete from "+table_name+" where gid=#{gid} and uid=#{uid}")
    public int drop(@Param("gid") Long gid, @Param("uid") Long uid);

    @Delete("delete from "+table_name+" where gid=#{gid}")
    public int dropOneGroup(@Param("gid") Long gid);

//    @Select("select uid from "+table_name+" where gid=#{gid} and state=1")
//    public ArrayList<Long> findUsersByGID(@Param("gid") Long gid);
    @Select("select id as uid, username from USER where id in (select uid from "+table_name+" where gid=#{gid} and state=1)")
    public ArrayList<User> findUsersByGID(@Param("gid") Long gid);

    @Select("select gid from "+table_name+" where uid=#{uid} and state=1")
    public ArrayList<Long> findGroupsByUID(@Param("uid") Long uid);

    @Select("select count(gid) from "+table_name+" where uid=#{uid} and state=1")
    public int groupCount(@Param("uid") Long uid);

    @Select("select count(uid) from "+table_name+" where gid=#{gid} and state=1")
    public int memberCount(@Param("gid") Long gid);

    @Select("select count(*) from "+table_name+" where gid=#{gid} and uid=#{uid} and state=1")
    public int check(@Param("gid") Long gid, @Param("uid") Long uid);


    @Select("select id,name,creator_id,create_time,type,group_profile from IM_GROUP_INFO where state=1 " +
            "and id in (select gid from "+table_name+" where uid=#{uid} and state=1)")
    public ArrayList<Group> findFullGroupsByUID(@Param("uid") int uid);

//    @Select("select gid from "+table_name+" where uid=#{uid} and state=1")
//    public ArrayList<Long> findGroupsOfUser(@Param("uid") Long uid);
}
