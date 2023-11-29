package com.example.demo.service;

import com.example.demo.dto.AddUserReqDto;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.UserResDto;
import com.example.demo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangzongbo
 * @since 2019-01-17
 */
public interface IUserService extends IService<User> {

    /**
     * 添加新用户
     * @param reqDto 请求体
     * @return int
     */
    int addUser(AddUserReqDto reqDto);

    /**
     * 用户登录
     * @param loginDTO 登录请求体
     * @return bool
     */
    UserResDto login(LoginDTO loginDTO);

    /**
     * shiroLogin
     * @param name 用户名
     * @param password 密码
     * @return
     */
    SimpleAuthenticationInfo shiroLogin(String name, String password);

}
