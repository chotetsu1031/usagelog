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
import com.chotetsu.UsageLog.model.CategoryUpdateRequest;
import com.chotetsu.UsageLog.service.UsageService;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UsageSearchController {
  private final CategoryRepository categoryRepository;
  private final UsageRepository usageRepository;
  private final UsageService usageService;

  public UsageSearchController(CategoryRepository categoryRepository, UsageRepository usageRepository,
      UsageService usageService) {
    this.categoryRepository = categoryRepository;
    this.usageRepository = usageRepository;
    this.usageService = usageService;
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
    List<Usage> usages = usageRepository.findBySearchUsageLog(form.getPurchaseMonth(), form.getCategoryCd());
    model.addAttribute("usages", usages);
    model.addAttribute("form", form);
    List<Category> categories = categoryRepository.findAllActive();
    model.addAttribute("categories", categories);
    return "usage-list";
  }

  @GetMapping("/api/search/total")
  @ResponseBody
  public Map<String, Object> getTotalAmount(@ModelAttribute("form") SearchListForm form) {
    // 検索結果から合計金額を計算
    List<Usage> usages = usageRepository.findBySearchUsageLog(form.getPurchaseMonth(), form.getCategoryCd());
    int totalAmount = usages.stream()
        .mapToInt(Usage::getAmount)
        .sum();

    // JSON形式で返す
    Map<String, Object> response = new HashMap<>();
    response.put("totalAmount", totalAmount);
    response.put("recordCount", usages.size());
    return response;
  }

  @PostMapping("/api/usage/update-category")
  @ResponseBody
  public Map<String, Object> updateCategory(@RequestBody CategoryUpdateRequest req) {
    try {
      usageService.updateCategoryForUsages(req.getUsageIds(), req.getCategoryCd());
      Map<String, Object> res = new HashMap<>();
      res.put("status", "success");
      return res;
    } catch (Exception e) {
      Map<String, Object> res = new HashMap<>();
      res.put("status", "error");
      res.put("message", e.getMessage());
      return res;
    }
  }
}
