package com.chenjiawen.Dao;

import com.chenjiawen.Model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {
    String  TABLE_NAME="user";
    String  INSERT_FIELD=" name,password,salt,head_url ";
    String  SELECT_FIELD=" id,name,password,salt,head_url ";

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELD,
            ") values (#{name},#{password},#{salt},#{head_url})"})
    int addUser(User user);

    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where id=#{id}"})
    User selectById(int id);

    @Delete({"delete from ",TABLE_NAME,"where id=#{id}"})
    void deleteById(int id);

    @Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
    void updatePassById(User user);

}
