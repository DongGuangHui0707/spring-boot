package com.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dao.UserMapper;
import com.dao2.User2Mapper;
import com.model.User;

@RestController
public class UserController {

	@Autowired
	UserMapper userMapper;
	@Autowired
	User2Mapper user2Mapper;
	@RequestMapping("/")
    String home() {
		System.out.println( "Hello World!" );
        return "Hello World!";
    }
   @RequestMapping("/now")
    String hehe() {
	   int k = userMapper.count();
	   int k2 = user2Mapper.count();
	   System.out.println(k);
	   System.out.println(k2);
       return "现在时间：" + (new Date()).toString();
    }
}
