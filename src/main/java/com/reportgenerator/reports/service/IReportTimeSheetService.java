package com.reportgenerator.reports.service;

import com.reportgenerator.reports.domain.Columns;
import com.reportgenerator.reports.domain.Field;
import com.reportgenerator.reports.domain.Filter;
import com.reportgenerator.reports.domain.GroupByCustom;
import com.reportgenerator.reports.domain.ReportView;
import java.util.List;

public interface IReportTimeSheetService {

  List<String> getValuesFromColumnDisplayName(Columns columns) throws Exception;

  List<ReportView> getReportsByNameAndArea(String reportName, String reportArea);

  public byte[] generateReportBytes(ReportView reportView, String tokenId) throws Exception;

  void saveNewReportViewWithMap(ReportView reportView) throws Exception;

  ReportView createNewReportView(ReportView reportView) throws Exception;

  Columns createColumnsObj(Columns columns) throws Exception;

  void saveNewReportViewWithMapGrpBy(List<GroupByCustom> groupByList, String reportName)
      throws Exception;

  void saveNewReportViewWithMapFilter(List<Filter> filterList, String reportName) throws Exception;

  ReportView generateReportViewWithMap(List<ReportView> reportViewList) throws Exception;

  List<Columns> getColumnsForReportName(String reportName) throws Exception;

  ReportView generateReportWithMapGrpBy(ReportView finalReportView) throws Exception;

  ReportView generateReportWithMapOrderBy(ReportView finalReportView) throws Exception;

  List<Filter> getFilterByReportName(String reportName) throws Exception;

  Filter getFilterByFilterName(String filterName);

  Filter createFilterReport(Filter filter, ReportView reportView);

  List<Field> getFieldsByFilterName(String filterName) throws Exception;
}
