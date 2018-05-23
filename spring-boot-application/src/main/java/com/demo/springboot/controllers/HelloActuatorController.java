package com.demo.springboot.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloActuatorController {
    @RequestMapping(value = "/helloactuator", method= RequestMethod.GET)
    public String helloActuator() {
        return "Hello Spring Boot Actuator";
    }
}