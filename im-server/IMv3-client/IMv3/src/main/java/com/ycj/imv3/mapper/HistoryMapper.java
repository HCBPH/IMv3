package com.ycj.imv3.mapper;

import com.ycj.imv3.entity.GroupMessageOuterClass;
import com.ycj.imv3.entity.MessageOuterClass;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

/**
 * @author 53059
 * @date 2022/2/11 19:54
 */
public interface HistoryMapper {
    final String singleHistory = "SINGLE_CHAT_HISTORY";
    final String groupHistory = "GROUP_CHAT_HISTORY";
    final String unreadSingleHistory = "UNREAD_SINGLE_HISTORY";
    final String unreadGroupHistory = "UNREAD_GROUP_HISTORY";


/*
* 查询历史记录
* */
//    可以尝试将表的索引建立在时间戳之上，我们根据时间戳定位数据，也许会提高性能
    @Select("select id_from as from_, id_to as to_, content as content_, times as timestamp_ " +
            "from "+singleHistory+" where times between #{t_start} and #{t_end} " +
            "and state=1 and id_from=#{from} and id_to=#{to}")
    public ArrayList<MessageOuterClass.Message> findSingleHistory(@Param("from") long from, @Param("to") long to, @Param("t_start") String t_start, @Param("t_end") String t_end);

    @Select("select id_from as from_, id_to as to_, content as content_, times as timestamp_ " +
            "from "+singleHistory+" where times between #{t_start} and #{t_end} " +
            "and state=1 and id_from=#{from}")
    public ArrayList<MessageOuterClass.Message> findSingleHistoryByFrom(@Param("from") String from, @Param("t_start") String t_start, @Param("t_end") String t_end);

    @Select("select id_from as from_, id_to as to_, content as content_, times as timestamp_ " +
            "from "+singleHistory+" where times between #{t_start} and #{t_end} " +
            "and state=1 and id_to=#{to}")
    public ArrayList<MessageOuterClass.Message> findSingleHistoryByTo(@Param("to") String to, @Param("t_start") String t_start, @Param("t_end") String t_end);

    @Select("select id_from as from_, id_to as to_, content as content_, times as timestamp_ " +
            "from "+groupHistory+" where times between #{t_start} and #{t_end} " +
            "and state=1 and id_from=#{from} and id_to=#{to}")
    public ArrayList<GroupMessageOuterClass.GroupMessage> findGroupHistory(@Param("from") long from, @Param("to") long to, @Param("t_start") String t_start, @Param("t_end") String t_end);

    @Select("select id_from as from_, id_to as to_, content as content_, times as timestamp_ " +
            "from "+groupHistory+" where times between #{t_start} and #{t_end} " +
            "and state=1 and id_from=#{from}")
    public ArrayList<GroupMessageOuterClass.GroupMessage> findGroupHistoryByFrom(@Param("from") long from, @Param("t_start") String t_start, @Param("t_end") String t_end);

    @Select("select id_from as from_, id_to as to_, content as content_, times as timestamp_ " +
            "from "+groupHistory+" where times between #{t_start} and #{t_end} " +
            "and state=1 and id_to=#{to}")
    public ArrayList<GroupMessageOuterClass.GroupMessage> findGroupHistoryByTo(@Param("to") long to, @Param("t_start") String t_start, @Param("t_end") String t_end);



/*
* 查询未读取未读取的历史记录
* */
    @Select("select id_from as from_, id_to as to_, content as content_, times as timestamp_ " +
            "from "+unreadSingleHistory+" where state=1 and id_to=#{to}")
    public ArrayList<MessageOuterClass.Message> findUnreadSingle(@Param("to") long to);


    @Select("select id_from as from_, gid as to_, content as content_, times as timestamp_ " +
            "from "+unreadGroupHistory+" where state=1 and id_to=#{to}")
    public ArrayList<GroupMessageOuterClass.GroupMessage> findUnreadGroup(@Param("to") long to);


/*
* 插入聊天记录
* */
    @Insert("insert into " + singleHistory + "(id_from, id_to, content, times) values(#{from}, #{to}, #{content}, #{times})")
    public int insertSingleHistory(@Param("from") long from, @Param("to") long to, @Param("content") String content, @Param("times") long times);

    @Insert("insert into " + groupHistory + "(id_from, id_to, content, times) values(#{from}, #{to}, #{content}, #{times})")
    public int insertGroupHistory(@Param("from") long from, @Param("to") long to, @Param("content") String content, @Param("times") long times);


/*
 * 保存离线消息
 * */
    @Insert("insert into " + unreadSingleHistory + "(id_from, id_to, content, times) values(#{from}, #{to}, #{content}, #{times})")
    public int insertSingleUnread(@Param("from") long from, @Param("to") long to, @Param("content") String content, @Param("times") long times);

    @Insert("insert into " + unreadGroupHistory + "(id_from, id_to, gid, content, times) values(#{from}, #{to}, #{gid}, #{content}, #{times})")
    public int insertGroupUnread(@Param("from") long from, @Param("to") long to, @Param("gid") long gid, @Param("content") String content, @Param("times") long times);


/*
 * 删除聊天记录
 * */
    @Update("update " + singleHistory + " set state=0 where id_from=#{from}")
    public int dropSingleHistory(@Param("from") long from);


    /*
 * 未读消息已读
 * */
    @Delete("delete from " + unreadSingleHistory + " where id_to=#{to}")
    public int deleteSingleUnread(@Param("to") long to);


    @Delete("delete from " + unreadGroupHistory + " where id_to=#{to}")
    public int deleteGroupUnread(@Param("to") long to);


}
