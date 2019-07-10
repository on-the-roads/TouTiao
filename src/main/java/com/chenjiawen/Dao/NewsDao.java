package com.chenjiawen.Dao;


import com.chenjiawen.Model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NewsDao {
    String TABLE_NAME = "news";
    String INSERT_FIELD = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELD = " id,"+INSERT_FIELD;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELD,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where user_id=#{userId}"})
    News selectByUserId(int userId);

    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where id=#{id}"})
    News selectById(int id);

    @Delete({"delete from ", TABLE_NAME, "where id=#{id}"})
    void deleteById(int id);

    @Update({"update ", TABLE_NAME, " set title=#{title} where id=#{id}"})
    void updateTitleById(News news);

    @Update({"update ",TABLE_NAME ,"set comment_count=#{commmentCount} where id=#{id} "})
    int  updateComentCount(@Param("id") int id, @Param("commmentCount") int commmentCount);

    //使用配置文件
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);
}
