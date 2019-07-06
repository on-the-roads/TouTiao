package com.chenjiawen.Dao;

import com.chenjiawen.Model.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginticketDao {
    String  TABLE_NAME="login_ticket";
    String  INSERT_FIELD=" user_id,ticket,expired,status ";
    String  SELECT_FIELD=" id,"+INSERT_FIELD;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELD,
            ") values (#{userId},#{ticket},#{expired},#{status})"})
    int addTicket(LoginTicket loginTicket);

    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Delete({"delete from ",TABLE_NAME,"where id=#{id}"})
    void deleteById(int id);

    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("status") int status,@Param("ticket")String ticket);
}
