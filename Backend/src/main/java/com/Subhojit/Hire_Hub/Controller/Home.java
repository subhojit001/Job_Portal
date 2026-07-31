package com.Subhojit.Hire_Hub.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @GetMapping
    public String helo(){
        return "Welcome to Hire-Hub.Your new job destination.";
    }

}
