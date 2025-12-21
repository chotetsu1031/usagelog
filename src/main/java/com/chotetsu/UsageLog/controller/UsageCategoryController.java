package com.chotetsu.UsageLog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsageCategoryController {
  @GetMapping("/edit")
  public String showEditPage() {
    // edit.htmlを開く
    return "edit";
  }
}
