package com.leonds.blog.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Leon
 */
@Controller
public class HomeController {

    @GetMapping("/tags")
    public String tags() {
        return "tags";
    }
}
