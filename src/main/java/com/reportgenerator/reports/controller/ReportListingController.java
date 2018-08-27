package com.reportgenerator.reports.controller;

import com.reportgenerator.reports.domain.ReportView;
import com.reportgenerator.reports.domain.ResponseMessageVO;
import com.reportgenerator.reports.domain.TableConstraint;
import com.reportgenerator.reports.service.IReportListingService;
import com.reportgenerator.reports.service.IReportMetaDataService;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ReportListingController {

  @Autowired 
  IReportListingService reportListingService;
  
  @Autowired 
  IReportMetaDataService reportMetaDataService;

  @GetMapping("/reportArea") //When you click on timsheet in side bar.
  public ResponseEntity<?> getReport(@RequestParam String reportArea) {
    List<ReportView> reportViewList = new ArrayList<ReportView>();
    List<TableConstraint> tableConstraintsList = new ArrayList<TableConstraint>();
    try {
    	tableConstraintsList = reportMetaDataService.getEntireTableConstraintsList(reportArea);
    	if (tableConstraintsList.size() <= 0 || tableConstraintsList.isEmpty())
    		reportMetaDataService.getFirstTimeSaveConstraintsData(reportArea);
    	reportViewList = reportListingService.getUniqueReportList(reportArea);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to fetch report"), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<List<ReportView>>(reportViewList, HttpStatus.OK);
  }

  @GetMapping("/getReport")
  public ResponseEntity<?> findReport(@RequestParam String reportArea) {
    List<String> reportNameList = new ArrayList<String>();
    try {
      reportNameList = reportListingService.getReportNameList(reportArea);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to fetch report"), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<List<String>>(reportNameList, HttpStatus.OK);
  }

  @GetMapping("/getFilterNames")
  public ResponseEntity<?> getFilterNames(String reportName) {
    List<String> filterNameList = new ArrayList<String>();
    try {
      filterNameList = reportListingService.getFilterNameList(reportName);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to fetch report"), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<List<String>>(filterNameList, HttpStatus.OK);
  }

  @PostMapping("/deleteReport")
  public ResponseEntity<?> deleteReport(
      @RequestParam String reportName, @RequestParam String reportArea) {
    List<ReportView> reportViewList = new ArrayList<ReportView>();
    try {
    	reportListingService.deleteReport(reportName, reportArea);
      reportViewList = reportListingService.getUniqueReportList(reportArea);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to delete report"), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<List<ReportView>>(reportViewList, HttpStatus.OK);
  }
}
