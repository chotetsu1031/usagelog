package com.chotetsu.UsageLog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chotetsu.UsageLog.entity.Category;
import com.chotetsu.UsageLog.entity.Usage;
import com.chotetsu.UsageLog.model.SearchListForm;
import com.chotetsu.UsageLog.repository.CategoryRepository;
import com.chotetsu.UsageLog.repository.UsageRepository;

@Controller
public class UsageSearchController {
  private final CategoryRepository categoryRepository;
  private final UsageRepository usageRepository;

  public UsageSearchController(CategoryRepository categoryRepository, UsageRepository usageRepository) {
    this.categoryRepository = categoryRepository;
    this.usageRepository = usageRepository;
  }

  @GetMapping("/usage-search")
  public String showForm(Model model) {
    model.addAttribute("form", new SearchListForm());
    List<Category> categories = categoryRepository.findAllActive();
    List<String> purchaseMonths = usageRepository.findPurchaseMonthList();
    model.addAttribute("purchaseMonths", purchaseMonths);
    model.addAttribute("categories", categories);
    return "usage-search";
  }

  @PostMapping("/usage-search")
  public String showList(@ModelAttribute("form") SearchListForm form, Model model) {
    List<Usage> usages = usageRepository.findBySearchUsageLog(form);
    model.addAttribute("usages", usages);
    model.addAttribute("form", form);
    return "usage-list";
  }

  @GetMapping("/api/search/total")
  @ResponseBody
  public Map<String, Object> getTotalAmount(@ModelAttribute("form") SearchListForm form) {
    // 検索結果から合計金額を計算
    List<Usage> usages = usageRepository.findBySearchUsageLog(form);
    int totalAmount = usages.stream()
        .mapToInt(Usage::getAmount)
        .sum();

    // JSON形式で返す
    Map<String, Object> response = new HashMap<>();
    response.put("totalAmount", totalAmount);
    response.put("recordCount", usages.size());
    return response;
  }
}
