package com.reportgenerator.reports.controller;

import com.reportgenerator.reports.domain.Columns;
import com.reportgenerator.reports.domain.Field;
import com.reportgenerator.reports.domain.Filter;
import com.reportgenerator.reports.domain.GroupByCustom;
import com.reportgenerator.reports.domain.ReportView;
import com.reportgenerator.reports.domain.ReportsMetaData;
import com.reportgenerator.reports.domain.ResponseMessageVO;
import com.reportgenerator.reports.service.IReportMetaDataService;
import com.reportgenerator.reports.service.IReportTimeSheetService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ReportTimeSheetController {

  @Autowired IReportTimeSheetService reportTimeSheetService;

  @Autowired IReportMetaDataService reportMetaDataService;

  @GetMapping("/tableName") // When click on Create new report button
  public ResponseEntity<?> getConstraintsFromTable(@RequestParam String reportArea) {
    List<ReportsMetaData> reportsMetaDataList = new ArrayList<ReportsMetaData>();
    Map<String, List<Columns>> responseMap = new HashMap<String, List<Columns>>();
    try {
      reportsMetaDataList = reportMetaDataService.getReportsMetaDataForReportArea(reportArea);
      log.info("reportsMetaDataList" + reportsMetaDataList);

      for (ReportsMetaData reportsMetaData : reportsMetaDataList) {
        if (reportsMetaData != null) {
          log.info(
              "!responseMap.containsKey(reportsMetaData.getTableName())"
                  + !responseMap.containsKey(reportsMetaData.getTableName()));
          if (!responseMap.containsKey(reportsMetaData.getTableName())) {
            List<Columns> list = new ArrayList<>();
            Columns column = new Columns();
            column.setColumnDisplayName(reportsMetaData.getColumnDisplayName());
            column.setColumnName(reportsMetaData.getColumnName());
            column.setTypeName(reportsMetaData.getColumnType());
            column.setTableColumn(reportsMetaData.getTableName());
            list.add(column);
            responseMap.put(reportsMetaData.getTableName(), list);
          } else {
            Columns column = new Columns();
            column.setColumnDisplayName(reportsMetaData.getColumnDisplayName());
            column.setColumnName(reportsMetaData.getColumnName());
            column.setTypeName(reportsMetaData.getColumnType());
            column.setTableColumn(reportsMetaData.getTableName());
            List<Columns> listR = responseMap.get(reportsMetaData.getTableName());
            listR.add(column);
            responseMap.put(reportsMetaData.getTableName(), listR);
          }
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to get Meta Data"), HttpStatus.NOT_FOUND);
    }
    log.info("responseMap---" + responseMap);
    return new ResponseEntity<Map<String, List<Columns>>>(responseMap, HttpStatus.OK);
  }

  @GetMapping("/filterCondition")
  public ResponseEntity<?> getFilterConditionForCol(
      @RequestParam String columnDisplayName, @RequestParam String ReportArea) {
    ReportsMetaData reportMetaData = new ReportsMetaData();
    List<String> filterConditionList = new ArrayList<String>();
    try {
      reportMetaData =
          reportMetaDataService.getFilterConditionForColAndReportArea(
              columnDisplayName, ReportArea);
      if (reportMetaData != null)
        filterConditionList = Arrays.asList(reportMetaData.getFilterConditions().split("-"));
      log.info("filterConditionList-----" + filterConditionList);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to get Filter"), HttpStatus.NOT_FOUND);
    }
    log.info("filterConditionList-----" + filterConditionList);
    return new ResponseEntity<List<String>>(filterConditionList, HttpStatus.OK);
  }

  @PostMapping("/getValues")
  public ResponseEntity<?> getValuesFromColumnDisplayName(@RequestBody Columns columns) {
    List<String> columnValuesList = new ArrayList<String>();
    try {
      columnValuesList = reportTimeSheetService.getValuesFromColumnDisplayName(columns);
      log.info("columnValuesList -----" + columnValuesList);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to get values for the column"), HttpStatus.NOT_FOUND);
    }
    Collections.sort(columnValuesList);
    log.info("columnValuesList-----" + columnValuesList);
    return new ResponseEntity<List<String>>(columnValuesList, HttpStatus.OK);
  }

  @PostMapping("/generateReport") // when click on Save and run button and re-run
  public ResponseEntity<?> generateReport(@RequestBody ReportView reportView,@RequestHeader HttpHeaders headers) {
    List<ReportView> reportViewList =
        reportTimeSheetService.getReportsByNameAndArea(
            reportView.getReportName(), reportView.getReportArea());

    if (reportViewList.size() <= 0 || reportViewList.isEmpty()) {
      try {
        log.info("generateReportBytes started");
        ReportView reportViewCopy = (ReportView) reportView.clone();
        byte[] bytes = reportTimeSheetService.generateReportBytes(reportView,headers.get("Auth_Token").get(0));
        log.info("generateReportBytes ended and lenth of bytes is" + bytes.length);
        if (bytes.length > 0) {
          reportTimeSheetService.saveNewReportViewWithMap(reportViewCopy);
          log.info("saveNewReportViewWithMap ended and lenth of bytes is" + reportViewCopy);
          if (!reportViewCopy.getGroupByList().isEmpty())
            reportTimeSheetService.saveNewReportViewWithMapGrpBy(
                reportViewCopy.getGroupByList(), reportViewCopy.getReportName());
          if (!reportViewCopy.getFilters().isEmpty())
            reportTimeSheetService.saveNewReportViewWithMapFilter(
                reportViewCopy.getFilters(), reportViewCopy.getReportName());
          return new ResponseEntity<byte[]>(bytes, HttpStatus.CREATED);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(
            new ResponseMessageVO("FAILED to generate report"), HttpStatus.NOT_FOUND);
      }
    } else {
      try {
        ReportView finalReportView =
            reportTimeSheetService.generateReportViewWithMap(reportViewList);
        if (finalReportView.getGroupBy() != null)
          finalReportView = reportTimeSheetService.generateReportWithMapGrpBy(finalReportView);
        else finalReportView.setGroupByList(new ArrayList<GroupByCustom>());
        if (finalReportView.getOrderBy() != null)
          finalReportView = reportTimeSheetService.generateReportWithMapOrderBy(finalReportView);
        else finalReportView.setOrderByList(new ArrayList<String>());
        List<Filter> filterList = new ArrayList<Filter>();
        List<Field> fieldList = new ArrayList<Field>();
        if (!reportView.getFilters().isEmpty()) {
          if (reportView.getFilters().get(0).getFields().isEmpty()) {
            fieldList =
                reportTimeSheetService.getFieldsByFilterName(
                    reportView.getFilters().get(0).getFilterName());
            if (!fieldList.isEmpty() || fieldList.size() > 0)
              reportView.getFilters().get(0).setFields(fieldList);
          } else {
            reportTimeSheetService.saveNewReportViewWithMapFilter(
                reportView.getFilters(), reportView.getReportName());
          }
          finalReportView.setFilters(reportView.getFilters());
        } else {
          filterList =
              reportTimeSheetService.getFilterByReportName(finalReportView.getReportName());
          log.info("filetrliost " + filterList);
          finalReportView.setFilters(filterList);
        }
        log.info("generateReportBytes");
        byte[] bytes = reportTimeSheetService.generateReportBytes(finalReportView,headers.get("Auth_Token").get(0));
        log.info("generateReportBytes ended and lenth of bytes is" + bytes.length);
        return new ResponseEntity<byte[]>(bytes, HttpStatus.OK);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(
            new ResponseMessageVO("FAILED to generate report"), HttpStatus.EXPECTATION_FAILED);
      }
    }
    return null;
  }

  @PostMapping("/createFilter")
  public ResponseEntity<ResponseMessageVO> createReport(@RequestBody ReportView reportView)
      throws SQLException {
    Filter filterObj =
        reportTimeSheetService.getFilterByFilterName(
            reportView.getFilters().get(0).getFilterName());
    if (filterObj != null) {
      return new ResponseEntity<ResponseMessageVO>(
          new ResponseMessageVO(
              reportView.getFilters().get(0).getFilterName() + " filter allready Exist."),
          HttpStatus.CONFLICT);
    } else {
      Filter reportFilter =
          reportTimeSheetService.createFilterReport(reportView.getFilters().get(0), reportView);
      if (reportFilter != null)
        return new ResponseEntity<>(
            new ResponseMessageVO("filter has been created successfully."), HttpStatus.CREATED);
      return new ResponseEntity<>(
          new ResponseMessageVO("Filter creation has FAILED"), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/getFiltersByReportName")
  public ResponseEntity<?> getAllFilters(@RequestParam String reportName) {
    List<Filter> filtersByReportNameList = new ArrayList<Filter>();
    try {
      filtersByReportNameList = reportTimeSheetService.getFilterByReportName(reportName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to generate report"), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<List<Filter>>(filtersByReportNameList, HttpStatus.OK);
  }
}
