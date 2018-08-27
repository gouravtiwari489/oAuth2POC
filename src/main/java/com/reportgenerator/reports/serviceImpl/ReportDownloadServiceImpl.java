package com.reportgenerator.reports.serviceImpl;

import com.reportgenerator.reports.service.IReportDownloadService;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

@Service
public class ReportDownloadServiceImpl implements IReportDownloadService {

  public byte[] downloadPdf() throws Exception {
    BufferedInputStream isr = new BufferedInputStream(new FileInputStream(new File("report.pdf")));
    byte[] bytes = IOUtils.toByteArray(isr);
    isr.close();
    return bytes;
  }

  public byte[] downloadExcel() throws Exception {
    BufferedInputStream isr = new BufferedInputStream(new FileInputStream(new File("report.xlsx")));
    byte[] bytes = IOUtils.toByteArray(isr);
    isr.close();
    return bytes;
  }
}
