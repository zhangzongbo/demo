package com.example.demo.controller;

import com.example.demo.dto.DoPostDTO;
import com.example.demo.entity.JsonResult;
import com.example.demo.exception.CustomerException;
import com.example.demo.service.AsyncTaskDemo;
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


    //构造函数方式注入

    private final AsyncTaskDemo task;
    @Autowired
    public DemoController(AsyncTaskDemo task){
        this.task = task;
    }


    @RequestMapping("/task")
    @ResponseBody
    public JsonResult health(@RequestParam(value = "url") String url ,
                             @RequestParam(value = "id") int id){
        log.info("url: {}, id: {}",url ,id);
        task.doTask(url);
        return new JsonResult().ok();
    }

    @RequestMapping("/error")
    @ResponseBody
    public JsonResult error(){
        throw new CustomerException("发生错误");
    }

    @RequestMapping(value = "/doPost", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult doPost(@Valid @RequestBody DoPostDTO req){

        Map<String,String> stringMap = new HashMap<>(5);
        stringMap.put("name",req.getName());
        stringMap.put("id",String.valueOf(req.getId()));
        return new JsonResult(stringMap);

    }

}
