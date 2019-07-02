package com.chenjiawen.Aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    private static final Logger LOGGER= LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.chenjiawen.Controller.*Controller.*(..))")
    public void before(){
        LOGGER.info("beforeMethod:Controller");
    }

    @After("execution(* com.chenjiawen.Controller.*Controller.*(..))")
    public void after(){
        LOGGER.info("afterMethod:Controller");
    }
}
