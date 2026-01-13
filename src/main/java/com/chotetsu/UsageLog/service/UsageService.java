package com.chotetsu.UsageLog.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chotetsu.UsageLog.entity.Category;
import com.chotetsu.UsageLog.entity.Keyword;
import com.chotetsu.UsageLog.entity.Usage;
import com.chotetsu.UsageLog.model.CsvRecord;
import com.chotetsu.UsageLog.model.FixedExpenseForm;
import com.chotetsu.UsageLog.repository.UsageRepository;

@Service
public class UsageService {

  private final int VALIDATED_USAGE = 1;// 有効
  private final int EXPENSE = 2;// 収入
  private final String RAKUTEN_SOURCE = "1";
  private final String AEON_SOURCE = "2";
  private final int UNCATEGORIZED = 10;// 未分類カテゴリコード

  @Autowired
  private UsageRepository usageRepository;
  @Autowired
  private CategoryResolver categoryResolver;

  public void saveAll(List<CsvRecord> records) {
    List<Usage> usages = new ArrayList<>();

    for (CsvRecord r : records) {
      Usage usage = new Usage();
      usage.setUsageId(UUID.randomUUID());
      usage.setDescription(r.getDescription());
      usage.setAmount(r.getAmount());
      Keyword keyword = new Keyword();
      if (r.getSource() == RAKUTEN_SOURCE) {
        // 利用明細のキーワードによってカテゴリを取得する
        keyword = categoryResolver.resolveRakuten(r.getDescription());
        usage.setCategoryCd(keyword.getCategoryCd());
        usage.setCategoryName(keyword.getCategoryName());
      } else if (r.getSource() == AEON_SOURCE) {
        // 利用明細の内容によってカテゴリを取得する
        Category category = categoryResolver.resolveAeon(r.getCategoryTips());
        if (UNCATEGORIZED == category.getCategoryCd()) {
          keyword = categoryResolver.resolveRakuten(r.getDescription());
          usage.setCategoryCd(keyword.getCategoryCd());
          usage.setCategoryName(keyword.getCategoryName());
        } else {
          usage.setCategoryCd(category.getCategoryCd());
          usage.setCategoryName(category.getCategoryName());
        }
      }
      usage.setCreatedDate(LocalDateTime.now());
      usage.setValidateFlag(VALIDATED_USAGE);// 有効フラグ
      usage.setType(EXPENSE);// 種別

      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        usage.setPurchaseDate(LocalDate.parse(r.getPurchaseDate(), formatter));
      } catch (Exception e) {
        throw new RuntimeException("日付パース失敗: " + r.getPurchaseDate(), e);
      }
      usages.add(usage);
    }
    usageRepository.saveAll(usages);
  }

  public List<CsvRecord> parseRakutenCsv(MultipartFile file) throws IOException {
    List<CsvRecord> records = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

      String line;
      boolean isFirstLine = true;

      while ((line = reader.readLine()) != null) {
        String[] values = line.split(",");
        // 各列の前後のダブルクォートを除去
        for (int i = 0; i < values.length; i++) {
          values[i] = values[i].replace("\"", "").trim();
        }
        if (isFirstLine) {
          isFirstLine = false; // 1行目はスキップ
          continue;
        }
        if (values[0].isEmpty()) {
          continue; // 対象データ以外はスキップ
        }
        CsvRecord record = new CsvRecord();
        record.setDescription(values[1]);// 購入内容
        record.setAmount(Integer.parseInt(values[4]));// 金額
        // yyyy/MM/dd を yyyy-MM-dd に変換
        String formattedDate = values[0].replace("/", "-");
        record.setPurchaseDate(formattedDate);// 購入日
        record.setCategoryTips("");
        record.setSource(RAKUTEN_SOURCE);
        records.add(record);
      }
    } catch (IOException e) {
      throw new RuntimeException("CSV読み込みエラー", e);
    }
    return records;
  }

  public List<CsvRecord> parseAeonCsv(MultipartFile file) throws IOException {
    List<CsvRecord> records = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(file.getInputStream(), "Shift_JIS"))) {

      String line;
      int lineNum = 0;
      String year20 = "20";
      String endStr = "分割・ボーナス払い明細";

      while ((line = reader.readLine()) != null) {
        lineNum++;
        String[] values = line.split(",");

        if (lineNum < 9) { // 9行目以降がデータ
          continue;
        }

        CsvRecord record = new CsvRecord();
        if (endStr.equals(values[0])) {
          break; // 対象データでないなら終了
        }
        record.setDescription(values[2]);// 購入内容
        record.setAmount(Integer.parseInt(values[6]));// 金額（値引き後）
        String inputDate = year20 + values[0];
        // 入力形式（yyyyMMdd）を定義
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 文字列を LocalDate オブジェクトに変換
        LocalDate date = LocalDate.parse(inputDate, inputFormatter);
        // 出力形式（yyyy-MM-dd）を定義
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // フォーマットして出力
        String purchaseDate = date.format(outputFormatter);
        record.setPurchaseDate(purchaseDate);// 購入日
        if (values.length > 7) {
          record.setCategoryTips((values[7].trim().substring(0, 4)));
        } else {
          record.setCategoryTips("");
        }
        record.setSource(AEON_SOURCE);
        records.add(record);
      }
    } catch (IOException e) {
      throw new RuntimeException("CSV読み込みエラー", e);
    }
    return records;
  }

  public void saveFixedExpense(FixedExpenseForm form) {
    Usage usage = new Usage();
    usage.setUsageId(UUID.randomUUID());
    usage.setDescription(form.getDescription());
    usage.setAmount(form.getAmount());
    usage.setCategoryCd(form.getCategoryCd());
    // categoryNameを取得
    Category category = categoryResolver.getCategoryByCd(form.getCategoryCd());
    usage.setCategoryName(category.getCategoryName());
    usage.setValidateFlag(VALIDATED_USAGE);
    usage.setType(EXPENSE);
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      usage.setPurchaseDate(LocalDate.parse(form.getPurchaseDate(), formatter));
    } catch (Exception e) {
      throw new RuntimeException("日付フォーマット設定失敗: " + form.getPurchaseDate(), e);
    }
    usage.setCreatedDate(LocalDateTime.now());
    usageRepository.save(usage);
  }
}