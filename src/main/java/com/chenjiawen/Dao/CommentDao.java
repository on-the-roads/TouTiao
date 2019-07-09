package com.chenjiawen.Dao;

import com.chenjiawen.Model.Comment;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CommentDao {

    String TABLE_NAME = "comment";
    String INSERT_FIELD = " user_id,create_date,entity_id,entity_type,status,content ";
    String SELECT_FIELD = " id," + INSERT_FIELD;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELD,
            ") values (#{userId},#{createDate},#{entityId},#{entityType},#{status},#{content})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELD, " from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

}
