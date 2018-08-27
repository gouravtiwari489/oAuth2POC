package com.reportgenerator.reports.repository;

import com.reportgenerator.reports.domain.ReportView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportView, Integer> {

  List<ReportView> findByReportNameAndReportArea(String reportName, String reportArea);

  void deleteByReportNameAndReportArea(String reportName, String reportArea);

  List<ReportView> findByReportArea(String reportArea);
}
