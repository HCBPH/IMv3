package com.ycj.imv3.mapper;

import com.ycj.imv3.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

public interface TestMapper {
    final String table_name = "test";

    @Select("SELECT content FROM " + table_name)
    public ArrayList<String> getAll();

    @Select("INSERT INTO " + table_name + " values('这是中文编码测试')")
    public void insertOne();
}
