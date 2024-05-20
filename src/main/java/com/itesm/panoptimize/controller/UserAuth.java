package com.itesm.panoptimize.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserAuth {

    @GetMapping("/login")
    public String authentication() {
        return "User authenticated successfully";
    }
}
