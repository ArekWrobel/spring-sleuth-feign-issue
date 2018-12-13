package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.stereotype.Controller
public class Controller
{
    private TestService testService;

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    String test()
    {
        System.out.println(testService.postSomething());
        return "OK";
    }

    @Autowired
    public void setTestService(TestService testService)
    {
        this.testService = testService;
    }
}
