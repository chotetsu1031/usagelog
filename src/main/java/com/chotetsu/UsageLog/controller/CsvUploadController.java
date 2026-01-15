package com.chotetsu.UsageLog.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.chotetsu.UsageLog.model.CsvRecord;
import com.chotetsu.UsageLog.service.UsageService;

@Controller
@SessionAttributes("uploadData")
public class CsvUploadController {

  private final UsageService usageService;
  private final String RAKUTEN_SOURCE = "rakuten";
  private final String AEON_SOURCE = "aeon";

  public CsvUploadController(UsageService usageService) {
    this.usageService = usageService;
  }

  @ModelAttribute("uploadData")
  public List<CsvRecord> uploadData() {
    return new ArrayList<>();
  }

  @PostMapping("/upload")
  public String uploadCsv(@RequestParam("file") MultipartFile file,
      @RequestParam("source") String source,
      @ModelAttribute("uploadData") List<CsvRecord> uploadData) {
    try {
      // CSV解析して、List<CsvRecord> に格納する
      if (source.equals(RAKUTEN_SOURCE)) { // 楽天の場合
        uploadData.clear();
        uploadData.addAll(usageService.parseRakutenCsv(file));
      } else if (source.equals(AEON_SOURCE)) { // イオンの場合
        uploadData.clear();
        uploadData.addAll(usageService.parseAeonCsv(file));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "redirect:/upload/confirm";
  }

  @GetMapping("/upload/confirm")
  public String confirm(@ModelAttribute("uploadData") List<CsvRecord> uploadData,
      Model model) {
    model.addAttribute("records", uploadData);
    return "confirm";
  }

  @PostMapping("/upload/commit")
  public String commit(@ModelAttribute("uploadData") List<CsvRecord> uploadData, SessionStatus sessionStatus) {
    // CSVデータの登録処理を呼び出す
    usageService.saveAll(uploadData);
    return "success";
  }

  @GetMapping("/api/upload/total")
  @ResponseBody
  public Map<String, Object> getTotalAmount(@ModelAttribute("uploadData") List<CsvRecord> uploadData) {
    // 合計金額を計算
    int totalAmount = uploadData.stream()
        .mapToInt(CsvRecord::getAmount)
        .sum();

    // JSON形式で返す
    Map<String, Object> response = new HashMap<>();
    response.put("totalAmount", totalAmount);
    response.put("recordCount", uploadData.size());
    return response;
  }
}
