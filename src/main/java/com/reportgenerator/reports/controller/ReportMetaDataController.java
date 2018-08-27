package com.reportgenerator.reports.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reportgenerator.reports.domain.TableConstraint;
import com.reportgenerator.reports.service.IReportMetaDataService;

@RestController
public class ReportMetaDataController {

  @Autowired IReportMetaDataService reportMetaDataService;
/*
  @GetMapping("/firstTimeSaveTableConstraintData")
  public void getFirstTimeSaveTableConstraintData(@RequestParam String tableName) {
    List<TableConstraint> tableConstraintsList = new ArrayList<TableConstraint>();
   // tableConstraintsList = reportMetaDataService.getEntireTableConstraintsList();
    if (tableConstraintsList.size() <= 0 || tableConstraintsList.isEmpty())
      try {
        reportMetaDataService.getFirstTimeSaveConstraintsData(tableName);
      } catch (Exception e) {
       // log.error(e.getMessage(), e);
        e.printStackTrace();
      }
  }*/
}
