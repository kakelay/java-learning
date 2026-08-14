package com.in28minutes.springboot.myfirstwebapp.repository;

import com.in28minutes.springboot.myfirstwebapp.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTableName(String tableName);

    List<AuditLog> findByRecordId(Long recordId);

    List<AuditLog> findByOperation(String operation);

    List<AuditLog> findByUsername(String username);

    Page<AuditLog> findByTableNameOrderByOperationTimestampDesc(String tableName, Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.operationTimestamp BETWEEN :startDate AND :endDate")
    List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM AuditLog a WHERE a.tableName = :tableName AND a.recordId = :recordId ORDER BY a.operationTimestamp DESC")
    List<AuditLog> findAuditTrailForRecord(@Param("tableName") String tableName,
                                          @Param("recordId") Long recordId);

    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.operation = :operation AND a.operationTimestamp >= :since")
    long countOperationsSince(@Param("operation") String operation,
                             @Param("since") LocalDateTime since);

    @Query("SELECT DISTINCT a.tableName FROM AuditLog a ORDER BY a.tableName")
    List<String> findAllTableNames();

    @Query("SELECT a FROM AuditLog a WHERE a.user.id = :userId ORDER BY a.operationTimestamp DESC")
    Page<AuditLog> findByUserId(@Param("userId") Long userId, Pageable pageable);
}