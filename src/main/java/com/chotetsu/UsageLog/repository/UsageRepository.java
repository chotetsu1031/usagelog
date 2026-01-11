package com.chotetsu.UsageLog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chotetsu.UsageLog.entity.Usage;
import com.chotetsu.UsageLog.model.SearchListForm;

public interface UsageRepository extends CrudRepository<Usage, UUID> {

  @Query("""
          SELECT distinct FUNCTION('DATE_FORMAT', u.purchaseDate, '%Y-%m') AS purchaseMonth
          FROM Usage u
          WHERE u.validateFlag = 1
          ORDER BY purchaseMonth DESC
      """)
  List<String> findPurchaseMonthList();

  @Query("""
          SELECT u
          FROM Usage u
          WHERE u.validateFlag = 1
          AND (:#{#form.purchaseMonth} IS NULL OR FUNCTION('DATE_FORMAT', u.purchaseDate, '%Y-%m') = :#{#form.purchaseMonth})
          AND (:#{#form.categoryCd} IS NULL OR u.categoryCd = :#{#form.categoryCd})
          ORDER BY u.purchaseDate DESC
      """)

  List<Usage> findBySearchUsageLog(SearchListForm form);
}
