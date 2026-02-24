package com.chotetsu.UsageLog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chotetsu.UsageLog.entity.Usage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
                AND (:purchaseMonth IS NULL OR FUNCTION('DATE_FORMAT', u.purchaseDate, '%Y-%m') = :purchaseMonth)
                AND (:categoryCd IS NULL OR u.categoryCd = :categoryCd)
                ORDER BY u.purchaseDate DESC
            """)
    List<Usage> findBySearchUsageLog(@Param("purchaseMonth") String purchaseMonth,
            @Param("categoryCd") Long categoryCd);

    @Modifying
    @Transactional
    @Query("""
                UPDATE Usage u
                SET u.categoryCd = :categoryCd, u.categoryName = :categoryName
                WHERE u.usageId IN :ids
            """)
    void updateCategoryByIds(@Param("ids") List<UUID> ids, @Param("categoryCd") Long categoryCd,
            @Param("categoryName") String categoryName);
}
