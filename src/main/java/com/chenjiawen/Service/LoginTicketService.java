package com.chenjiawen.Service;

import com.chenjiawen.Dao.LoginticketDao;
import com.chenjiawen.Model.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginTicketService {

    @Autowired
    LoginticketDao loginticketDao;

    public int addLoginTicket(LoginTicket loginTicket) {
        return loginticketDao.addTicket(loginTicket);
    }
}
