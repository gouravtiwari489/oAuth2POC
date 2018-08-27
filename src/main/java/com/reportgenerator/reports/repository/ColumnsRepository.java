package com.reportgenerator.reports.repository;

import com.reportgenerator.reports.domain.Columns;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnsRepository extends JpaRepository<Columns, Integer> {

  List<Columns> findByReportName(String reportName);

  void deleteByReportName(String reportName);
}
