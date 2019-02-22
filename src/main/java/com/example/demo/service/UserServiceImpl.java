package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.dto.AddUserReqDto;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.UserResDto;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.UserStatusEnum;
import com.example.demo.exception.CustomerException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.api.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangzongbo
 * @since 2019-01-17
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int addUser(AddUserReqDto reqDto) {
        User user = new User();
        user.setName(reqDto.getUserName());
        user.setPassword(MD5Utils.encode(reqDto.getPassword()));
        user.setCode("0000");
        try {
            return userMapper.insert(user);
        }catch (DuplicateKeyException e){
            throw new CustomerException("用户名已存在!");
        }
    }

    @Override
    public UserResDto login(LoginDTO loginDTO) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("name",loginDTO.getUserName()));
        if (user == null){
            throw new CustomerException("用户名不存在");
        }
        log.info("user: {}",user);
        UserResDto resDto = new UserResDto();
        resDto.setName(user.getName());
        resDto.setCode(user.getCode());
        resDto.setStatus(user.getStatus());
        resDto.setCreateTime(user.getCreateTime());
        if(user.getPassword().equals(MD5Utils.encode(loginDTO.getPassword()))){
            return resDto;
        }else {
            throw new CustomerException("用户名或密码错误");
        }
    }

    @Override
    public SimpleAuthenticationInfo shiroLogin(String name, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("name",name));
        if(user == null){
            throw new UnknownAccountException("用户名或密码错误!");
        }
        if(!password.equals(user.getPassword())){
            throw new IncorrectCredentialsException("用户名或密码错误!");
        }
        if (UserStatusEnum.LOCKED.getStringValue().equals(user.getStatus())){
            throw new LockedAccountException("账号已被锁定,请联系管理员!");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, user.getName());
        return info;
    }


}
