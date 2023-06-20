package com.stb.bankaccountservice;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Test", description = "Testing")
@RequestMapping("/api/v1/accounts")
public class TestController {

    @GetMapping
    public String test() {
        return "Hello World!";
    }
}
