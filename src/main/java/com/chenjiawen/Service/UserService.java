package com.chenjiawen.Service;

import com.chenjiawen.Dao.UserDao;
import com.chenjiawen.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getUserBYid(int id)
    {
        User user=userDao.selectById(id);
        return user;
    }

}
