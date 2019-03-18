package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dto.AddUserReqDto;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.UserResDto;
import com.example.demo.entity.JSONResult;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomerException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.api.IUserService;
import com.example.demo.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        User u = userMapper.selectOne(queryWrapper);
        log.info("user: {}",u);

        UserResDto userResDto = new UserResDto();
        userResDto.setName(u.getName());
        userResDto.setCode(u.getCode());
        userResDto.setStatus(u.getStatus());
        userResDto.setCreateTime(u.getCreateTime());

        int count = userMapper.selectCount(new QueryWrapper<User>().eq("code",1));
        log.info("count: {}",count);

        return JSONResult.ok(userResDto);
    }

    @RequestMapping("/getUserList")
    @ResponseBody()
    public JSONResult getUserList(){

        Page<User> page = new Page<>(1,5);
        IPage<User> userIPage = userMapper.selectPage(page,new QueryWrapper<User>().eq("code",0));
        return JSONResult.ok(userIPage);
    }

    @RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    @ResponseBody()
    public JSONResult addUser(@Valid @RequestBody AddUserReqDto reqDto){
        try {
            return JSONResult.ok(userService.addUser(reqDto));
        }catch (Exception e){
            log.error("注册异常: {}", e.getMessage(), e);
            if (e instanceof CustomerException){
                return JSONResult.error(e.getMessage());
            }
            return JSONResult.error("系统繁忙");
        }
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody()
    public JSONResult login(@Valid @RequestBody LoginDTO loginDTO){
        try {
            UserResDto resDto = userService.login(loginDTO);
            return JSONResult.ok(resDto);
        }catch (CustomerException e){
            log.error("登录异常: {}", e.getMessage(), e);
            return JSONResult.error("用户名或密码错误!");
        }
    }

    @RequestMapping(value = "/shiroLogin", method = RequestMethod.POST)
    @ResponseBody()
    public JSONResult shiroLogin(@Valid @RequestBody LoginDTO loginDTO){
        String password = MD5Utils.encode(loginDTO.getPassword());
        UsernamePasswordToken token = new UsernamePasswordToken(loginDTO.getUserName(), password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return JSONResult.ok();
        }catch (Exception e){
            log.error("shiro 登录异常: {}", e.getMessage(), e);
            return JSONResult.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getUserInfoShiro", method = RequestMethod.GET)
    @ResponseBody()
    public JSONResult getUserInfoShiro(){

        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        UserResDto resDto = new UserResDto();
        resDto.setName(user.getName());
        resDto.setCode(user.getCode());
        resDto.setStatus(user.getStatus());
        resDto.setCreateTime(user.getCreateTime());
        return JSONResult.ok(resDto);
    }

}
