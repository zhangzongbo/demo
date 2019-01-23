package com.example.demo.controller;

import com.example.demo.dto.DoPostDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.entity.JSONResult;
import com.example.demo.exception.CustomerException;
import com.example.demo.service.AsyncTaskDemo;
import com.example.demo.service.LoginServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangzongbo
 * @date 18-12-21 下午2:12
 */

@Controller
@RequestMapping("/demo")

@Slf4j
public class DemoController {


    @Autowired
    private AsyncTaskDemo task;

    @Autowired
    private LoginServiceImpl loginService;


    @RequestMapping("/task")
    @ResponseBody
    public JSONResult health(@RequestParam(value = "url") String url ,
                             @RequestParam(value = "id") int id){
        task.doTask(url);
        return JSONResult.ok();
    }

    @RequestMapping("/error")
    @ResponseBody
    public JSONResult error(){
        throw new CustomerException("发生错误");
    }

    @RequestMapping(value = "/doPost", method = RequestMethod.POST)
    @ResponseBody
    public JSONResult doPost(@Valid @RequestBody DoPostDTO req){

        Map<String,String> stringMap = new HashMap<>(5);
        stringMap.put("name",req.getName());
        stringMap.put("id",String.valueOf(req.getId()));
        return  JSONResult.ok(stringMap);

    }

    @RequestMapping("/login")
    @ResponseBody
    public JSONResult login(@Valid @RequestBody LoginDTO loginDTO){

        return loginService.passwordLogin(loginDTO);
    }

}
