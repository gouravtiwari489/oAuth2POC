package com.reportgenerator.reports.service;

public interface IReportDownloadService {

  byte[] downloadPdf() throws Exception;

  byte[] downloadExcel() throws Exception;
}
