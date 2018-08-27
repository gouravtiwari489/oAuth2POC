package com.reportgenerator.reports.service;

import com.reportgenerator.reports.domain.ReportView;
import java.util.List;

public interface IReportListingService {

  List<ReportView> getUniqueReportList(String reportArea) throws Exception;

  List<String> getReportNameList(String reportArea) throws Exception;

  List<String> getFilterNameList(String reportName) throws Exception;

  void deleteReport(String reportName, String reportArea);
}
