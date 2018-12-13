package com.example.demo;

import feign.Headers;
import feign.RequestLine;

public interface TestService
{
    @RequestLine("POST /something")
    @Headers("Content-Type: application/json")
    String postSomething();

}
