package com.chotetsu.UsageLog.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@Controller
public class GlobalExceptionHandler {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @ExceptionHandler(NoHandlerFoundException.class)
  public String handleNotFound(NoHandlerFoundException ex, HttpServletRequest request, Model model) {
    return buildErrorPage(HttpStatus.NOT_FOUND, "指定されたページは存在しません。", ex, request, model);
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Exception ex, HttpServletRequest request, Model model) {
    return buildErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "サーバー側で予期しないエラーが発生しました。", ex, request, model);
  }

  private String buildErrorPage(HttpStatus status, String defaultMessage, Exception ex,
      HttpServletRequest request, Model model) {
    String message = ex.getMessage() != null && !ex.getMessage().isBlank()
        ? ex.getMessage()
        : defaultMessage;

    model.addAttribute("status", status.value());
    model.addAttribute("error", status.getReasonPhrase());
    model.addAttribute("message", message);
    model.addAttribute("exceptionType", ex.getClass().getName());
    model.addAttribute("path", request.getRequestURI());
    model.addAttribute("timestamp", LocalDateTime.now().format(FORMATTER));
    model.addAttribute("trace", Arrays.stream(ex.getStackTrace())
        .limit(20)
        .map(StackTraceElement::toString)
        .toList());

    return "error";
  }
}
