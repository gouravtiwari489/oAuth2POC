package com.reportgenerator.reports.serviceImpl;

import com.reportgenerator.reports.domain.Filter;
import com.reportgenerator.reports.domain.ReportView;
import com.reportgenerator.reports.repository.ColumnsRepository;
import com.reportgenerator.reports.repository.FieldRepository;
import com.reportgenerator.reports.repository.FilterRepository;
import com.reportgenerator.reports.repository.GroupByCustomRepository;
import com.reportgenerator.reports.repository.ReportRepository;
import com.reportgenerator.reports.service.IReportListingService;
import com.reportgenerator.reports.service.IReportTimeSheetService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportListingServiceImpl implements IReportListingService {

  @Autowired 
  ReportRepository reportRepository;
  
  @Autowired 
  ColumnsRepository columnsRepository;
  
  @Autowired 
  GroupByCustomRepository groupByCustomRepository;
  
  @Autowired 
  FilterRepository filterRepository;
  
  @Autowired
  FieldRepository fieldRepository;

  @Autowired 
  IReportTimeSheetService reportTimeSheetService;

  @Override
  public List<ReportView> getUniqueReportList(String reportArea) throws Exception {
    List<ReportView> reportViewList = new ArrayList<ReportView>();
    List<String> reportNameList = new ArrayList<String>();
    for (ReportView reportView : getReports(reportArea)) {
      if (!reportNameList.contains(reportView.getReportName())) {
        reportViewList.add(reportView);
        reportNameList.add(reportView.getReportName());
      }
    }
    return reportViewList;
  }

  private List<ReportView> getReports(String reportArea) {
    return reportRepository.findByReportArea(reportArea);
  }

  @Override
  public List<String> getReportNameList(String reportArea) {
    List<String> reportNameList = new ArrayList<String>();
    for (ReportView reportView : getReports(reportArea)) {
      if (!reportNameList.contains(reportView.getReportName())) {
        reportNameList.add(reportView.getReportName());
      }
    }

    return reportNameList;
  }

  @Override
  public List<String> getFilterNameList(String reportName) throws Exception {
    List<Filter> filterList = reportTimeSheetService.getFilterByReportName(reportName);
    List<String> filterNameList = new ArrayList<>();
    for (Filter filter : filterList) {
      filterNameList.add(filter.getFilterName());
    }
    return filterNameList;
  }

  @Override
  @Transactional
  public void deleteReport(String reportName, String reportArea) {
	  List<Filter> filterList = new ArrayList<Filter>();
	  reportRepository.deleteByReportNameAndReportArea(reportName, reportArea);
	  columnsRepository.deleteByReportName(reportName);
	  groupByCustomRepository.deleteByReportName(reportName);
	  filterList = filterRepository.deleteByReportName(reportName);
	  if(!filterList.isEmpty() || filterList.size() > 0){
		  for(Filter filter : filterList){
			  fieldRepository.deleteByFilterName(filter.getFilterName());
		  }
	  }
  }
}
