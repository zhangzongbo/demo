package com.example.demo.service.api;

import com.example.demo.dto.LoginDTO;
import com.example.demo.entity.JSONResult;

/**
 * @author zhangzongbo
 * @date 19-1-14 下午6:21
 */
public interface LoginService {

    /**
     * password login
     * @param loginDTO login dto
     * @return result
     */
    JSONResult passwordLogin(LoginDTO loginDTO);
}
