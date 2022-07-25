package com.ycj.imv3.mapper;

import com.ycj.imv3.entity.Group;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

/**
 * @author 53059
 * @date 2022/1/29 17:38
 */
public interface GroupMapper {

    final String table_name = "IM_GROUP_INFO";

    @Insert("insert into "+table_name+"(id, name, creator_id) values(#{gid}, #{gname}, #{creator_id})")
    public int createGroup(@Param("gid") Long gid, @Param("gname") String gname, @Param("creator_id") Long creator_id);

    @Select("select id,name,creator_id,create_time from "+table_name+" where id=#{gid} and state=1")
    public Group findGroup(@Param("gid") Long gid);

    @Select("select id,name,creator_id,create_time from "+table_name+" where state=1")
    public ArrayList<Group> findAllGroups();

    @Select("select count(id) from "+ table_name +" where state=1")
    public int counts();

    @Update("update "+ table_name +" set state=0 where id=#{gid};")
    public int dropGroup(Long gid);

    @Delete("delete from "+table_name+" where id=#{gid}")
    public int realDropGroup(@Param("gid") Long gid);

}
