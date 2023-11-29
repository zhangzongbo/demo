package com.example.demo.service.impl;

import com.example.demo.config.StaticConfig;
import com.example.demo.dto.LoginDTO;
import com.example.demo.entity.JSONResult;
import com.example.demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangzongbo
 * @date 19-1-14 下午6:23
 */


@Service
public class LoginServiceImpl implements LoginService {


    private final StaticConfig staticConfig;

    @Autowired
    public LoginServiceImpl(StaticConfig staticConfig){
        this.staticConfig = staticConfig;
    }

    @Override
    public JSONResult passwordLogin(LoginDTO loginDTO) {

        String userName = loginDTO.getUserName();
        String password = loginDTO.getPassword();
        if (userName.equals(staticConfig.getUserName()) && password.equals(staticConfig.getPassword())){
            return JSONResult.ok("登陆成功");
        }else {
            return JSONResult.error("登录失败,用户名或密码错误");
        }

    }
}
