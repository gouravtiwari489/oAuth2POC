package com.reportgenerator.reports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reportgenerator.reports.serviceImpl.CustomReportServiceImpl;

@RestController
public class CustomQueryReportController {

  @Autowired CustomReportServiceImpl customReportService;

  @PostMapping("/generateUtilisationReport")
  public ResponseEntity<?> generateUtilisationReport(@RequestParam String userName,@RequestHeader HttpHeaders headers)
      throws Exception {
    byte[] bytes = customReportService.generateUtilisationReport(userName, headers.get("Auth_Token").get(0));
    return new ResponseEntity<byte[]>(bytes, HttpStatus.OK);
  }

  @PostMapping("/generateResourceAssignmentReport")
  public ResponseEntity<?> generateResourceAssignmentReport(@RequestParam String userName,@RequestHeader HttpHeaders headers)
      throws Exception {
    byte[] bytes = customReportService.generateReportsForResourceAssignmentReport(userName,headers.get("Auth_Token").get(0));
    return new ResponseEntity<byte[]>(bytes, HttpStatus.OK);
  }

  @PostMapping("/generateResourceDistributionReport")
  public ResponseEntity<?> generateResourceDistributionReport(@RequestParam String userName,@RequestHeader HttpHeaders headers)
      throws Exception {
    byte[] bytes = customReportService.generateResourceDistributionReport(userName,headers.get("Auth_Token").get(0));
    return new ResponseEntity<byte[]>(bytes, HttpStatus.OK);
  }

  @PostMapping("/generateLeavesReport")
  public ResponseEntity<?> generateLeavesReport(@RequestParam String userName,@RequestHeader HttpHeaders headers) throws Exception {
    byte[] bytes = customReportService.generateLeavesReport(userName,headers.get("Auth_Token").get(0));
    return new ResponseEntity<byte[]>(bytes, HttpStatus.OK);
  }

  @PostMapping("/generateExpensesReport")
  public ResponseEntity<?> generateExpensesReport(@RequestParam String userName,@RequestHeader HttpHeaders headers) throws Exception {
    byte[] bytes = customReportService.generateExpensesReport(userName,headers.get("Auth_Token").get(0));
    return new ResponseEntity<byte[]>(bytes, HttpStatus.OK);
  }
}
