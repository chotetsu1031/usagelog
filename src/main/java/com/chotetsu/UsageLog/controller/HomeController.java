package com.chotetsu.UsageLog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/upload")
  public String upload() {
    return "upload";
  }
}