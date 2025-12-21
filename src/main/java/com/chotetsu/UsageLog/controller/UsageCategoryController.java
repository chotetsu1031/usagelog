package com.chotetsu.UsageLog.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class UsageCategoryController {
  @GetMapping("/edit")
  public String editCategory() {
    // edit.htmlを開く
    return "edit";
  }
}
