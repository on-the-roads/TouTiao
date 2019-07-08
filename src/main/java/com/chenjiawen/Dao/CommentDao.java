package com.chenjiawen.Dao;

import com.chenjiawen.Model.Comment;
import com.chenjiawen.Model.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public class CommentDao {
    String  TABLE_NAME="comment";
    String  INSERT_FIELD=" user_id,create_date,entity_id,entity_type,status,content ";
    String  SELECT_FIELD=" id,"+INSERT_FIELD;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELD,
            ") values (#{userId},#{createDate},#{entityId},#{entityType},#{status},#{content})"})
    int addComment(Comment comment);

    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Delete({"delete from ",TABLE_NAME,"where id=#{id}"})
    void deleteById(int id);

    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("status") int status,@Param("ticket")String ticket);
}
