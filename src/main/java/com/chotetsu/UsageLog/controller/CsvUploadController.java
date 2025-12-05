package com.chotetsu.UsageLog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chotetsu.UsageLog.service.UsageService;

@Controller
public class CsvUploadController {

  @Autowired
  // UserServiceをインスタンス化
  private UsageService userService;

  @PostMapping("/upload")
  public String uploadCsv(@RequestParam("file") MultipartFile file) {
    // CSVデータの登録処理を呼び出す
    userService.saveCsv(file);
    return "success";
  }
}
