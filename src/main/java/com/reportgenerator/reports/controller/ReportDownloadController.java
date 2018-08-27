package com.reportgenerator.reports.controller;

import com.reportgenerator.reports.domain.ResponseMessageVO;
import com.reportgenerator.reports.service.IReportDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ReportDownloadController {

  @Autowired IReportDownloadService reportDownloadService;

  @GetMapping("/downloadPdf")
  public ResponseEntity<?> downloadReport(@RequestParam String fileType) {
    try {
      byte[] bytes = reportDownloadService.downloadPdf();
      return new ResponseEntity<byte[]>(bytes, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to download pdf report"), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/downloadExcel")
  public ResponseEntity<?> downloadExcelReport(@RequestParam String fileType) {
    try {
      byte[] bytes = reportDownloadService.downloadExcel();
      return new ResponseEntity<byte[]>(bytes, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity<>(
          new ResponseMessageVO("FAILED to download excel report"), HttpStatus.NOT_FOUND);
    }
  }
}
