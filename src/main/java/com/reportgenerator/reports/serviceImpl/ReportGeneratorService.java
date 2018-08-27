package com.reportgenerator.reports.serviceImpl;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportgenerator.reports.configuration.DynamicReportConfiguration;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;

@Service
public class ReportGeneratorService {
  @Autowired DynamicReportConfiguration dynamicReportConfiguration;

  public byte[] generateReport(
      String query, String reportName, Map<String, String> columnMap, String userName, String orgCode)
      throws Exception {
	   

    JasperReportBuilder report = DynamicReports.report();
    JasperReportBuilder reportForCsv = DynamicReports.report();

    setCriteriaToReport(report,reportForCsv, columnMap);

    StyleBuilder boldStyle = DynamicReports.stl.style().bold();
    @SuppressWarnings("deprecation")
    StyleBuilder boldCenteredStyle =
        DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(18);
    
    StyleBuilder boldCenteredStyle1 =
            DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
    StyleBuilder boldCenteredStyle2 =
            DynamicReports.stl.style().setHorizontalAlignment(HorizontalAlignment.CENTER);
    StyleBuilder columnTitleStyle =
        DynamicReports.stl
            .style(boldCenteredStyle1)
            .setBorder(DynamicReports.stl.pen1Point())
            .setBackgroundColor(Color.LIGHT_GRAY);
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    String formattedDate = date.format(formatters);
    StyleBuilder boldLeftStyle =
            DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.LEFT);
    StyleBuilder boldRightStyle =
            DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
    
    report
    .setColumnTitleStyle(columnTitleStyle)
    .highlightDetailEvenRows()
    .addTitle(Components.text(reportName).setStyle(boldCenteredStyle))
    .addTitle(Components.text(""))
   // .addPageHeader(Components.text("User : "+ reportView.getOwner()+"                                                                                                                                       "+"Owner :"+ reportView.getOwner()+ "\n").setStyle(boldCenteredStyle1))
    .addPageHeader(Components.horizontalList(Components.text("User : "+ userName).setStyle(boldLeftStyle),
    		Components.text("Owner :"+ "Canned").setStyle(boldRightStyle)))
    .addPageHeader(Components.text("").setFixedHeight(4))
    .addPageFooter(Components.text("").setFixedHeight(4))
    //.addPageFooter(Components.text(UserOrganization+"                                                                                                                                                        Date: " +formattedDate).setStyle(boldCenteredStyle2))
    .addPageFooter(Components.horizontalList(Components.text(orgCode).setStyle(boldLeftStyle),
    		Components.text("Date: " +formattedDate).setStyle(boldRightStyle)))
    .addPageFooter(Components.pageXofY())
    .setDataSource(query,  dynamicReportConfiguration.connection());
    
    reportForCsv
    .setColumnTitleStyle(columnTitleStyle)
    .highlightDetailEvenRows()
    .setDataSource(query, dynamicReportConfiguration.connection())
    .ignorePagination();

    reportForCsv.toCsv(new FileOutputStream("report.csv"));
    report.toPdf(new FileOutputStream("report.pdf"));
    JasperXlsxExporterBuilder xlsxExporter =
        Exporters.xlsxExporter("report.xlsx")
            .setDetectCellType(true)
            .setIgnorePageMargins(true)
            .setWhitePageBackground(false)
            .setRemoveEmptySpaceBetweenColumns(true)
            .setFontSizeFixEnabled(true);
    reportForCsv.toXlsx(xlsxExporter);
    return generateReportBytes();
  }

  private String getOrganiztionName() {
	  
	return null;
}

private void setCriteriaToReport(JasperReportBuilder report,JasperReportBuilder reportForCsv, Map<String, String> columnMap)
      throws DRException {
    for (Map.Entry<String, String> entry : columnMap.entrySet()) {
      report.addColumn(
          col.column(
              entry.getKey(), entry.getKey(), (DRIDataType) type.detectType(entry.getValue())));
      reportForCsv.addColumn(
              col.column(
                  entry.getKey(), entry.getKey(), (DRIDataType) type.detectType(entry.getValue())));
    }
  }

  private byte[] generateReportBytes() throws Exception {
    BufferedInputStream isr = new BufferedInputStream(new FileInputStream(new File("report.csv")));
    byte[] bytes = IOUtils.toByteArray(isr);
    String s = new String(bytes);
    char[] z = s.toCharArray();
    for (int i = 0; i < z.length; i++) {
      if (z[i] == '"') {
        z[i] = ' ';
        for (int j = i; j < z.length; j++) {
          if (z[j] == ',') z[j] = '\0';
          if (z[j] == '"') {
            z[j] = ' ';
            j = z.length;
          }
        }
      }
    }
    bytes = new String(z).getBytes();
    isr.close();
    return bytes;
  }
}
