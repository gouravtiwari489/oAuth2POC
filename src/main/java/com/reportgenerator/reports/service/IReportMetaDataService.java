package com.reportgenerator.reports.service;

import com.reportgenerator.reports.domain.ReportsMetaData;
import com.reportgenerator.reports.domain.TableConstraint;
import java.util.List;

public interface IReportMetaDataService {

  List<TableConstraint> getEntireTableConstraintsList(String reportArea) throws Exception;

  List<ReportsMetaData> getReportsMetaDataForReportArea(String reportArea) throws Exception;

  ReportsMetaData getFilterConditionForColAndReportArea(String columnDisplayName, String ReportArea)
      throws Exception;

  void getFirstTimeSaveConstraintsData(String reportArea) throws Exception;
}
