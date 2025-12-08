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

import com.chotetsu.UsageLog.entity.Usage;
import com.chotetsu.UsageLog.model.CsvRecord;
import com.chotetsu.UsageLog.repository.UsageRepository;

@Service
public class UsageService {

  private final int VALIDATED_USAGE = 1;// 有効

  @Autowired
  private UsageRepository usageRepository;

  public void saveAll(List<CsvRecord> records) {
    List<Usage> usages = new ArrayList<>();

    for (CsvRecord r : records) {
      Usage usage = new Usage();

      usage.setUsage_id(UUID.randomUUID());
      usage.setDescription(r.getDescription());
      usage.setAmount(r.getAmount());
      usage.setCreated_date(LocalDateTime.now());
      usage.setValidate_flag(VALIDATED_USAGE);// 有効フラグ

      // purchase_date は String なのでパースする
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        usage.setPurchase_date(sdf.parse(r.getPurchase_date()));
      } catch (ParseException e) {
        throw new RuntimeException("日付パース失敗: " + r.getPurchase_date(), e);
      }
      usages.add(usage);
    }
    usageRepository.saveAll(usages);
  }

  public void saveCsv(MultipartFile file) {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      String line;
      boolean isFirstLine = true;

      while ((line = reader.readLine()) != null) {
        String[] cols = line.split(",");
        // 各列の前後のダブルクォートを除去
        for (int i = 0; i < cols.length; i++) {
          cols[i] = cols[i].replace("\"", "").trim();
        }
        if (isFirstLine) {
          isFirstLine = false; // 1行目はスキップ
          continue;
        }

        Usage usage = new Usage();
        usage.setUsage_id(UUID.randomUUID());// 利用明細ID
        usage.setDescription(cols[1]);// 購入内容
        LocalDateTime ldt = LocalDateTime.now();
        usage.setCreated_date(ldt);// 登録日
        usage.setAmount(Integer.parseInt(cols[4]));// 金額
        usage.setPurchase_date(sdf.parse(cols[0]));// 購入日

        usageRepository.save(usage);
      }
    } catch (IOException e) {
      throw new RuntimeException("CSV読み込みエラー", e);
    } catch (ParseException e) {
      // エラー処理（ログ出力やデフォルト値設定など）
      e.printStackTrace();
    }
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
        record.setPurchase_date(values[0]);// 購入日
        records.add(record);
      }
    } catch (IOException e) {
      throw new RuntimeException("CSV読み込みエラー", e);
    }
    return records;
  }
}