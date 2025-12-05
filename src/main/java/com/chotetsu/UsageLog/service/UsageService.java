package com.chotetsu.UsageLog.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chotetsu.UsageLog.entity.Usage;
import com.chotetsu.UsageLog.repository.UsageRepository;

@Service
public class UsageService {

  @Autowired
  private UsageRepository usageRepository;

  public void saveCsv(MultipartFile file) {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      String line;
      boolean isFirstLine = true;
      // String[] cols = line.split(",");

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
}