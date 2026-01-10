package com.chotetsu.UsageLog.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.chotetsu.UsageLog.entity.Category;
import com.chotetsu.UsageLog.model.FixedExpenseForm;
import com.chotetsu.UsageLog.repository.CategoryRepository;
import com.chotetsu.UsageLog.service.UsageService;

@Controller
public class FixedExpenseController {

  private final UsageService usageService;
  private final CategoryRepository categoryRepository;

  public FixedExpenseController(UsageService usageService, CategoryRepository categoryRepository) {
    this.usageService = usageService;
    this.categoryRepository = categoryRepository;
  }

  @GetMapping("/fixed-expense")
  public String showForm(Model model) {
    model.addAttribute("form", new FixedExpenseForm());
    List<Category> categories = categoryRepository.findAllActive();
    model.addAttribute("categories", categories);
    return "fixed-expense";
  }

  @PostMapping("/fixed-expense")
  public String save(@ModelAttribute("form") FixedExpenseForm form) {
    usageService.saveFixedExpense(form);
    return "redirect:/fixed-expense?success";
  }
}