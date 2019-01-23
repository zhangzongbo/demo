package com.example.demo.controller;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.api.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhangzongbo
 * @date 19-1-16 下午6:18
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Resource
    private UserMapper userMapper;

    @RequestMapping("/getUserInfo")
    @ResponseBody()
    public JSONResult getUserInfo(@RequestParam(value = "id") Long id){

        User user = userService.getById(id);
        log.info("user: {}",user);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        User u = userMapper.selectOne(queryWrapper);
        log.info("user: {}",u);

        int count = userMapper.selectCount(new QueryWrapper<User>().eq("code",1));
        log.info("count: {}",count);

        return JSONResult.ok(u);
    }
}
