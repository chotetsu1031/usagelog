package com.chotetsu.UsageLog.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chotetsu.UsageLog.entity.Category;
import com.chotetsu.UsageLog.entity.Usage;
import com.chotetsu.UsageLog.model.CsvRecord;
import com.chotetsu.UsageLog.repository.UsageRepository;

@Service
public class UsageService {

  private final int VALIDATED_USAGE = 1;// 有効

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
      // 利用明細の内容からカテゴリを取得する
      Category category = categoryResolver.resolve(r.getDescription());
      usage.setCategoryCd(category.getCategoryCd());
      usage.setCategoryName(category.getCategoryName());
      usage.setCreatedDate(LocalDateTime.now());
      usage.setValidate_flag(VALIDATED_USAGE);// 有効フラグ

      // purchaseDate は String なのでパースする
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        usage.setPurchaseDate(sdf.parse(r.getPurchaseDate()));
      } catch (ParseException e) {
        throw new RuntimeException("日付パース失敗: " + r.getPurchaseDate(), e);
      }
      usages.add(usage);
    }
    usageRepository.saveAll(usages);
  }

  public List<CsvRecord> parseCsv(MultipartFile file) throws IOException {
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

        CsvRecord record = new CsvRecord();
        record.setDescription(values[1]);// 購入内容
        record.setAmount(Integer.parseInt(values[4]));// 金額
        record.setPurchaseDate(values[0]);// 購入日
        records.add(record);
      }
    } catch (IOException e) {
      throw new RuntimeException("CSV読み込みエラー", e);
    }
    return records;
  }
}