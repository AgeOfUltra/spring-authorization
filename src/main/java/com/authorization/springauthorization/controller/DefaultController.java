package com.authorization.springauthorization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/app")
public class DefaultController {

    @GetMapping("/weather")
    public ModelAndView demoPage() {
        return new ModelAndView("index"); // Note: no ".html"
    }
}
