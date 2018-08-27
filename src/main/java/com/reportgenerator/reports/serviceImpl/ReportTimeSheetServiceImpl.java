package com.reportgenerator.reports.serviceImpl;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import com.reportgenerator.reports.configuration.DynamicReportConfiguration;
import com.reportgenerator.reports.domain.Columns;
import com.reportgenerator.reports.domain.Field;
import com.reportgenerator.reports.domain.Filter;
import com.reportgenerator.reports.domain.GroupByCustom;
import com.reportgenerator.reports.domain.ReportView;
import com.reportgenerator.reports.domain.TableConstraint;
import com.reportgenerator.reports.exception.BusinessException;
import com.reportgenerator.reports.repository.ColumnsRepository;
import com.reportgenerator.reports.repository.ConstraintRepository;
import com.reportgenerator.reports.repository.FieldRepository;
import com.reportgenerator.reports.repository.FilterRepository;
import com.reportgenerator.reports.repository.GroupByCustomRepository;
import com.reportgenerator.reports.repository.ReportRepository;
import com.reportgenerator.reports.repository.ReportsMetaDataRepository;
import com.reportgenerator.reports.service.IReportTimeSheetService;
import com.reportgenerator.reports.security.filter.AuthTokenStore;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.jasper.builder.export.JasperXlsxExporterBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportTimeSheetServiceImpl implements IReportTimeSheetService {

  @Autowired ReportsMetaDataRepository reportsMetaDataRepository;

  @Autowired ReportRepository reportRepository;

  @Autowired DynamicReportConfiguration dRC;

  @Autowired ConstraintRepository constraintRepository;

  @Autowired DynamicReportConfiguration dyc;

  @Autowired ColumnsRepository columnsRepository;

  @Autowired GroupByCustomRepository groupByCustomRepo;

  @Autowired FilterRepository filterRepository;

  @Autowired FieldRepository fieldRepository;
  
  @Autowired AuthTokenStore tokenStore;

  Set<String> selfJoinList = new HashSet<String>();

  Map<String, String> selfJoinMap = new HashMap<>();

  @SuppressWarnings("unchecked")
  @Override
  public List<String> getValuesFromColumnDisplayName(Columns columns) {
    Session session = dRC.getSessionFactory().openSession();
    List<String> valueList = new ArrayList<String>();
    try {
      session.beginTransaction();
      // select DISTINCT(columns.) from osi_customer
      if (columns.getColumnName() != null && columns.getTableColumn() != null) {

        String queryString =
            "select DISTINCT(" + columns.getColumnName() + ") from " + columns.getTableColumn();
        valueList = session.createSQLQuery(queryString).list();
      }

    } catch (Exception e) {
      if (session.getTransaction() != null) session.getTransaction().rollback();
    } finally {
      session.close();
    }

    return valueList;
  }

  @Override
  public List<ReportView> getReportsByNameAndArea(String reportName, String reportArea) {
    return reportRepository.findByReportNameAndReportArea(reportName, reportArea);
  }

  @Override
  public byte[] generateReportBytes(ReportView reportView,String tokenId) throws Exception {
    generateReport(reportView,tokenStore.getUserOrganization(tokenId));
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

  public void generateReport(ReportView reportView,String UserOrganization) throws Exception {
    JasperReportBuilder report = DynamicReports.report();
    JasperReportBuilder reportForCsv = DynamicReports.report();
    Map<String, List<Columns>> map = reportView.getTableColumnMap();
    String query = "";
    String queryToExecute = "select *";
    
    Iterator<String> iterator = reportView.getTableColumnMap().keySet().iterator();
	while(iterator.hasNext()){
		String entry = iterator.next();
		List<Columns> stList = map.get(entry);
		Iterator<Columns> its = stList.iterator();
		while(its.hasNext()){
			Columns sn = its.next();
			for(GroupByCustom gBC: reportView.getGroupByList()){
				if(sn.getColumnName().equals(gBC.getColumnName())){
					if(gBC.getOperation()!=null){
						its.remove();
					}
				}
			}
		}
		
		
	}
    // create select clause and report columns
	createSelectCriteria(reportForCsv, reportView, query);
    query = createSelectCriteria(report, reportView, query);
    queryToExecute = queryToExecute.replace("*", query);
    queryToExecute = removeLastCharacter(queryToExecute);
    // create from clause
    Set<String> fromSet = new HashSet<>();
    Set<Columns> setC = new HashSet<Columns>();
    for (String key : map.keySet()) {
    if(map.get(key).isEmpty()){
    	fromSet.add(createFromClause(key));
    }	
      for (Columns column : map.get(key)) {
        if (!setC.add(column)) {
          String fromClauseForSelf = "";
          fromClauseForSelf = fromClauseForSelf + key + " " + key + "1";
          //selfJoinList.add(column.getColumnName());
          selfJoinMap.put(key, column.getColumnName());
          fromSet.add(fromClauseForSelf);
        } else {
          fromSet.add(createFromClause(key));
        }
      }
      /*
       * List<TableConstraint> tableConstraint =
       * constraintRepository.findByReferencedTableName(key); if
       * (!tableConstraint.isEmpty())
       * fromList.add(createFromClause(tableConstraint));
       */
    }
    String from = "";
    /*
     * for (String s : fromList) { from = from + s + ","; }
     */
    List<String> fromList = new ArrayList<>();

    fromList.addAll(fromSet);
    for (int i = 0; i < fromList.size(); i++) {

      if (i == fromSet.size() - 1) from = from + fromList.get(i);
      else from = from + fromList.get(i) + ",";
    }

    if (map.size() == 1) {
      for (String key : map.keySet()) {
        queryToExecute = queryToExecute + " from " + key + " WHERE ";
      }
    }
    if (map.size() > 1) {

      // check if map contains 2 columns with same name and different
      // display name
    	
    	if(reportView.getReportArea().equalsIgnoreCase("TIMESHEET")){
    		if (map.containsKey("osi_timesheet_entry") || map.containsKey("OSI_TIMESHEET_ENTRY")) {

    	        queryToExecute = queryToExecute + " from " + from + " WHERE ";
    	      } else {
    	        queryToExecute = queryToExecute + " from " + from + "," + "OSI_TIMESHEET_ENTRY" + " WHERE ";
    	      }
    	}
    	
    	if(reportView.getReportArea().equalsIgnoreCase("EMPLOYEE")){
    		if (map.containsKey("osi_employees") || map.containsKey("OSI_EMPLOYEES")) {

    	        queryToExecute = queryToExecute + " from " + from + " WHERE ";
    	      } else {
    	        queryToExecute = queryToExecute + " from " + from + "," + "OSI_EMPLOYEES" + " WHERE ";
    	      }
    	}
    	
    	if(reportView.getReportArea().equalsIgnoreCase("Expenses")){
    		if (map.containsKey("osi_expenses") || map.containsKey("OSI_EXPENSES")) {

    	        queryToExecute = queryToExecute + " from " + from + " WHERE ";
    	      } else {
    	        queryToExecute = queryToExecute + " from " + from + "," + "OSI_EXPENSES" + " WHERE ";
    	      }
    	}
    	if(reportView.getReportArea().equalsIgnoreCase("Invoice")){
    		if (map.containsKey("osi_invoices") || map.containsKey("OSI_INVOICES")) {

    	        queryToExecute = queryToExecute + " from " + from + " WHERE ";
    	      } else {
    	        queryToExecute = queryToExecute + " from " + from + "," + "OSI_INVOICES" + " WHERE ";
    	      }
    	}
    	if(reportView.getReportArea().equalsIgnoreCase("Project")){
    		if (map.containsKey("osi_projects") || map.containsKey("OSI_PROJECTS")) {

    	        queryToExecute = queryToExecute + " from " + from + " WHERE ";
    	      } else {
    	        queryToExecute = queryToExecute + " from " + from + "," + "OSI_PROJECTS" + " WHERE ";
    	      }
    	}
      
    }

    // create where clause
    List<String> joinClauseList = new ArrayList<>();
    if(reportView.getReportArea()!=null) {
    for (String key : map.keySet()) {
    //  List<TableConstraint> tableConstraint = constraintRepository.findByReferencedTableName(key);
      List<TableConstraint> tableConstraint = constraintRepository.findByReferencedTableNameAndReportArea(key, reportView.getReportArea());
      String joinClause = "";
      if (tableConstraint != null && !tableConstraint.isEmpty()) {
        joinClause = createWhereClause(tableConstraint);
        joinClauseList.add(joinClause);
      }
    }
   }

    List<String> listFilterQuery = new ArrayList<String>();
    List<String> listFilterDateQuery = new ArrayList<String>();
    if (!reportView.getFilters().isEmpty() && reportView.getFilters().size() > 0) {
      for (Filter filter : reportView.getFilters()) {
        if (filter.getFields() != null) {
          for (Field field : filter.getFields()) {
            if (field.getColumnValues() != null && !field.getColumnValues().isEmpty()) {
              buildFilterClause(listFilterQuery, field);
            } else if (field.getColumnValues() == null && field.getColumnValue() != null) {
              List<String> columnValuesList = Arrays.asList(field.getColumnValue().split("-"));
              if (!columnValuesList.isEmpty()) {
                field.setColumnValues(columnValuesList);
                buildFilterClause(listFilterQuery, field);
              }
            }
            if (field.getFromDate() != null && field.getToDate() != null) {
              SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
              String filterDateQuery =
                  field.getColumnName()
                      + " between '"
                      + formatter.format(field.getFromDate())
                      + "' and '"
                      + formatter.format(field.getToDate())
                      + "'";
              listFilterDateQuery.add(filterDateQuery);
            }
          }
        }
      }
    }

    String filter = "";
    if (!listFilterQuery.isEmpty()) {
      for (int i = 0; i < listFilterQuery.size(); i++) {
        if (i == 0) filter = filter + listFilterQuery.get(i);
        else filter = filter + " OR " + listFilterQuery.get(i);
      }
    }

    if (!listFilterDateQuery.isEmpty()) {
      for (int i = 0; i < listFilterDateQuery.size(); i++) {
        if (i == 0) filter = filter + " and " + listFilterDateQuery.get(i);
        else filter = filter + " and " + listFilterDateQuery.get(i);
      }
    }
    /*
     * if (filter != null && filter != "") filter = filter.substring(0,
     * filter.length() - 3);
     */

    if (filter != null && filter != "" && map.size() == 1) queryToExecute = queryToExecute + filter;
    if (filter != null && filter != "" && map.size() > 1)
      queryToExecute = queryToExecute + filter + " AND ";

    String join = "";
    if (map.size() > 1) {
      for (String s : joinClauseList) {
        join = join + s + " AND ";
      }
    }

    if ((join != null && join != "")) join = join.substring(0, join.length() - 4);

    // if(map.size() > 1){
    queryToExecute = queryToExecute + join;
    // }
    if (reportView.getGroupBy() != null || !reportView.getGroupByList().isEmpty()) {
      String groupBy = "";
      for (int i = 0; i < reportView.getGroupByList().size(); i++) {
        if (reportView.getGroupByList().get(i).getOperation()== null) {
          if (i == 0){ 
        	  groupBy = groupBy.concat(reportView.getGroupByList().get(i).getColumnName());
        	  }
          else {
        	  if(groupBy == ""){
        		  groupBy =  reportView.getGroupByList().get(i).getColumnName();
        	  }else{
        		  
        		  groupBy = groupBy + " , " + reportView.getGroupByList().get(i).getColumnName();
        	  }
          }
        }
      }
      queryToExecute = queryToExecute + " group by " + groupBy;
    }
    if (reportView.getOrderBy() != null || !reportView.getOrderByList().isEmpty()) {
      String orderBy = "";
      for (int i = 0; i < reportView.getOrderByList().size(); i++) {
        if (i == 0) orderBy = orderBy.concat(reportView.getOrderByList().get(i));
        else orderBy = orderBy + " , " + reportView.getOrderByList().get(i);
      }
      queryToExecute = queryToExecute + " order by " + orderBy;
    }

    System.err.println("********" + queryToExecute);
    /* StyleBuilder boldStyle = DynamicReports.stl.style().bold();
    @SuppressWarnings("deprecation")
    StyleBuilder boldCenteredStyle =
        DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
    StyleBuilder columnTitleStyle =
        DynamicReports.stl
            .style(boldCenteredStyle)
            .setBorder(DynamicReports.stl.pen1Point())
            .setBackgroundColor(Color.LIGHT_GRAY);
    report
        .setColumnTitleStyle(columnTitleStyle)
        .highlightDetailEvenRows()
        .title(Components.text(reportView.getReportName()).setStyle(boldCenteredStyle))
        .addTitle(Components.text("User Name :" + reportView.getOwner()))
        .addTitle(Components.text("Owner Name :" + reportView.getOwner()))
        .addTitle(Components.text("Date & Time of Execution :  " + new java.util.Date()))
        .addTitle(Components.text(""))
        .addTitle(Components.text(""))*/
    
    StyleBuilder boldStyle = DynamicReports.stl.style().bold();
    @SuppressWarnings("deprecation")
    StyleBuilder boldCenteredStyle =
        DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(18);
    
    StyleBuilder boldCenteredStyle1 =
            DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
    StyleBuilder boldCenteredStyle2 =
            DynamicReports.stl.style().setHorizontalAlignment(HorizontalAlignment.CENTER);
    
    StyleBuilder boldLeftStyle =
            DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.LEFT);
    StyleBuilder boldRightStyle =
            DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT);
    StyleBuilder columnTitleStyle =
        DynamicReports.stl
            .style(boldCenteredStyle1)
            .setBorder(DynamicReports.stl.pen1Point())
            .setBackgroundColor(Color.LIGHT_GRAY);
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    String formattedDate = date.format(formatters);
    report
        .setColumnTitleStyle(columnTitleStyle)
        .highlightDetailEvenRows()
        .addTitle(Components.text(reportView.getReportName()).setStyle(boldCenteredStyle))
        .addTitle(Components.text(""))
        .addPageHeader(Components.horizontalList(Components.text("User : "+ reportView.getOwner()).setStyle(boldLeftStyle),
        		Components.text("Owner :"+ reportView.getOwner()).setStyle(boldRightStyle)))
        .addPageHeader(Components.text("").setFixedHeight(4))
        .addPageFooter(Components.text("").setFixedHeight(4))
        .addPageFooter(Components.horizontalList(Components.text(UserOrganization).setStyle(boldLeftStyle),
        		Components.text("Date: " +formattedDate).setStyle(boldRightStyle)))
        .addPageFooter(Components.pageXofY())
        .setDataSource(queryToExecute, dyc.connection());
    
    reportForCsv
    .setColumnTitleStyle(columnTitleStyle)
    .highlightDetailEvenRows()
    .setDataSource(queryToExecute, dyc.connection())
    .ignorePagination();
    try {
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
    } catch (DRException e) {
      e.printStackTrace();
      if (e != null) {
        throw new Exception("Error while generating report");
      }
      // e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void buildFilterClause(List<String> listFilterQuery, Field field) {
	  String filterQuery = "";
    for (String s : field.getColumnValues()) {
      
      if (field.getOperation().equalsIgnoreCase("EQUALS")) {
        field.setOperation("=");
        if (!selfJoinMap.isEmpty()) {
          for (String key : selfJoinMap.keySet()) {
            if (selfJoinMap.get(key).equals(field.getColumnName())) {
              filterQuery =
                  key + "." + field.getColumnName() + field.getOperation() + "'" + s + "'";
            } else {
              filterQuery =
                  field.getTableName()
                      + "."
                      + field.getColumnName()
                      + field.getOperation()
                      + "'"
                      + s
                      + "'";
            }
          }
        } else {
          filterQuery = field.getColumnName() + field.getOperation() + "'" + s + "'";
        }

        if (filterQuery != "") {
            listFilterQuery.add(filterQuery);
          }
        field.setOperation("EQUALS");
      }

      if (field.getOperation().equalsIgnoreCase("NOT EQUALS")) {
        field.setOperation("!=");
        if (!selfJoinMap.isEmpty()) {
          for (String key : selfJoinMap.keySet()) {
            if (selfJoinMap.get(key).equals(field.getColumnName())) {
              filterQuery =
                  key + "." + field.getColumnName() + field.getOperation() + "'" + s + "'";
            } else {
              filterQuery =
                  field.getTableName()
                      + "."
                      + field.getColumnName()
                      + field.getOperation()
                      + "'"
                      + s
                      + "'";
            }
          }
        } else {
          filterQuery =
              field.getTableName()
                  + "."
                  + field.getColumnName()
                  + field.getOperation()
                  + "'"
                  + s
                  + "'";
        }

        if (filterQuery != "") {
            listFilterQuery.add(filterQuery);
          }
        field.setOperation("NOT EQUALS");
      }

      if (field.getOperation().equalsIgnoreCase("LIKE")) {
        field.setOperation("LIKE");
        if (!selfJoinMap.isEmpty()) {
          for (String key : selfJoinMap.keySet()) {
            if (selfJoinMap.get(key).equals(field.getColumnName())) {
              filterQuery =
                  key + "." + field.getColumnName() + field.getOperation() + "'" + s + "'";
            } else {
              filterQuery = field.getColumnName() + field.getOperation() + "'" + s + "%" + "'";
            }
          }
        } else {
          filterQuery = field.getColumnName() + field.getOperation() + "'" + s + "%" + "'";
        }
        if (filterQuery != "") {
            listFilterQuery.add(filterQuery);
          }
      }

      if (field.getOperation().equalsIgnoreCase("IN")) {
        field.setOperation("IN");

        String in = "";
        for (int i = 0; i < field.getColumnValues().size(); i++) {
          if (i == field.getColumnValues().size() - 1)
            in = in + "'" + field.getColumnValues().get(i) + "'";
          else in = in + "'" + field.getColumnValues().get(i) + "'" + ",";
        }

        if (!selfJoinMap.isEmpty()) {
          for (String key : selfJoinMap.keySet()) {
            if (selfJoinMap.get(key).equals(field.getColumnName())) {
              filterQuery =
                  key + "." + field.getColumnName() + " " + field.getOperation() + " (" + in + ")";
            } else {
              filterQuery = field.getColumnName() + " " + field.getOperation() + " (" + in + ")";
            }
          }
        } else {
          filterQuery = field.getColumnName() + " " + field.getOperation() + " (" + in + ")";
        }
      }

     

      if (field.getOperation().equalsIgnoreCase("IN")) {
    	  if (filterQuery != "") {
    	        listFilterQuery.add(filterQuery);
    	      }
        break;
      }

      if (field.getOperation().equalsIgnoreCase("NOT IN")) {
        field.setOperation("NOT IN");

        String in = "";
        for (int i = 0; i < field.getColumnValues().size(); i++) {
          if (i == field.getColumnValues().size() - 1)
            in = in + "'" + field.getColumnValues().get(i) + "'";
          else in = in + "'" + field.getColumnValues().get(i) + "'" + ",";
        }

        if (!selfJoinMap.isEmpty()) {
          for (String key : selfJoinMap.keySet()) {
            if (selfJoinMap.get(key).equals(field.getColumnName())) {
              filterQuery =
                  key + "." + field.getColumnName() + " " + field.getOperation() + " (" + in + ")";
            } else {
              filterQuery = field.getColumnName() + " " + field.getOperation() + " (" + in + ")";
            }
          }
        } else {
          filterQuery = field.getColumnName() + " " + field.getOperation() + " (" + in + ")";
        }
      }

     
      if (field.getOperation().equalsIgnoreCase("NOT IN")) {
    	  if (filterQuery != "") {
    	        listFilterQuery.add(filterQuery);
    	      }
        break;
      }
      
      
    }
   /* if (filterQuery != "") {
        listFilterQuery.add(filterQuery);
      }*/
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private String createSelectCriteria(
      JasperReportBuilder report, ReportView reportView, String query) throws DRException {
    Set<Columns> setColumnValues = new HashSet<>();
    Map<String, List<Columns>> map = reportView.getTableColumnMap();
    Integer count = 0;
    for (String key : map.keySet()) {
      count = 0;
      for (Columns column : map.get(key)) {
        if (!setColumnValues.add(column)) {
          count = count + 1;
          query =
              query
                  + key
                  + "1"
                  + "."
                  + column.getColumnName()
                  + " AS "
                  + column.getColumnDisplayName()
                  + ",";
        } else {
          query = query + key + "." + column.getColumnName() + ",";
        }
        {
          if (column.getTypeName().equalsIgnoreCase("INT")) {
            column.setTypeName("integer");
          } else if (column.getTypeName().equalsIgnoreCase("VARCHAR")) {
            column.setTypeName("string");
          } else if (column.getTypeName().equalsIgnoreCase("DECIMAL")) {
            column.setTypeName("bigdecimal");
          } else if (column.getTypeName().equalsIgnoreCase("DATETIME")) {
            column.setTypeName("date");
          } else if (column.getTypeName().equalsIgnoreCase("CHAR")) {
            column.setTypeName("character");
          }

          if (column.getColumnDisplayName() != null) {
            if (count > 0) {
              report.addColumn(
                  col.column(
                      column.getColumnDisplayName(),
                      column.getColumnDisplayName(),
                      (DRIDataType) type.detectType(column.getTypeName())));
            } else {
              report.addColumn(
                  col.column(
                      column.getColumnDisplayName(),
                      column.getColumnName(),
                      (DRIDataType) type.detectType(column.getTypeName())));
            }
          } else {
            report.addColumn(
                col.column(
                    column.getColumnName(),
                    column.getColumnName(),
                    (DRIDataType) type.detectType(column.getTypeName())));
          }
        }
      }
    }

    if (!reportView.getGroupByList().isEmpty()) {
      for (GroupByCustom gBC : reportView.getGroupByList()) {
        if (gBC.getOperation() != null && gBC.getOperation().equalsIgnoreCase("count")) {
          String displayName = "Count" + "(" + gBC.getColumnName() + ")";
          String displayNameInDB = "count" + "(" + gBC.getColumnName() + ")";
          report.addColumn(
              col.column(
                  displayName, displayNameInDB, (DRIDataType) type.detectType("bigdecimal")));
          query = query + displayNameInDB + ",";
        }

        if (gBC.getOperation() != null && gBC.getOperation().equalsIgnoreCase("sum")) {
          String displayName = "Sum" + "(" + gBC.getColumnName() + ")";
          String displayNameInDB = "sum" + "(" + gBC.getColumnName() + ")";
          report.addColumn(
              col.column(
                  displayName, displayNameInDB, (DRIDataType) type.detectType("bigdecimal")));
          query = query + displayNameInDB + ",";
        }

        if (gBC.getOperation() != null && gBC.getOperation().equalsIgnoreCase("max")) {
          String displayName = "Max" + "(" + gBC.getColumnName() + ")";
          String displayNameInDB = "max" + "(" + gBC.getColumnName() + ")";
          report.addColumn(
              col.column(
                  displayName, displayNameInDB, (DRIDataType) type.detectType("bigdecimal")));
          query = query + displayNameInDB + ",";
        }

        if (gBC.getOperation() != null && gBC.getOperation().equalsIgnoreCase("min")) {
          String displayName = "Min" + "(" + gBC.getColumnName() + ")";
          String displayNameInDB = "min" + "(" + gBC.getColumnName() + ")";
          report.addColumn(
              col.column(
                  displayName, displayNameInDB, (DRIDataType) type.detectType("bigdecimal")));
          query = query + displayNameInDB + ",";
        }

        if (gBC.getOperation() != null && gBC.getOperation().equalsIgnoreCase("average")) {
          String displayName = "Average" + "(" + gBC.getColumnName() + ")";
          String displayNameInDB = "average" + "(" + gBC.getColumnName() + ")";
          report.addColumn(
              col.column(
                  displayName, displayNameInDB, (DRIDataType) type.detectType("bigdecimal")));
          query = query + displayNameInDB + ",";
        }
      }
    }

    return query;
  }

  public String removeLastCharacter(String str) {
    if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
      str = str.substring(0, str.length() - 1);
    }
    return str;
  }

  private String createFromClause(String key) {
    String fromClause = "";
    // if (StringUtils.isNotBlank(tableConstraint.get(0).getColumnName())) {
    fromClause = fromClause + key;
    // }
    return fromClause;
  }

  private String createWhereClause(List<TableConstraint> tableConstraintMapper) {
    String joinClause = "";
    if (StringUtils.isNotBlank(tableConstraintMapper.get(0).getColumnName())) {
      if (tableConstraintMapper
          .get(0)
          .getReferencedColumnName()
          .equals(tableConstraintMapper.get(0).getColumnName())) {

        joinClause =
            joinClause
                + tableConstraintMapper.get(0).getReferencedTableName()
                + "."
                + tableConstraintMapper.get(0).getReferencedColumnName()
                + "="
                + tableConstraintMapper.get(0).getParentTableName()
                + "."
                + tableConstraintMapper.get(0).getReferencedColumnName();
      } else {
        joinClause =
            joinClause
                + tableConstraintMapper.get(0).getReferencedTableName()
                + "."
                + tableConstraintMapper.get(0).getReferencedColumnName()
                + "="
                + tableConstraintMapper.get(0).getParentTableName()
                + "."
                + tableConstraintMapper.get(0).getColumnName();
      }
    }

    return joinClause;
  }

  @Override
  public void saveNewReportViewWithMap(ReportView reportView) throws BusinessException {
    for (Map.Entry<String, List<Columns>> entry : reportView.getTableColumnMap().entrySet()) {
      ReportView reportViewObj = new ReportView();
      reportViewObj.setReportName(reportView.getReportName());
      reportViewObj.setOwner(reportView.getOwner());
      reportViewObj.setSharedTo(reportView.getSharedTo());
      reportViewObj.setActions(reportView.getActions());
      reportViewObj.setCreatedDate(reportView.getCreatedDate());
      reportViewObj.setLastExcecution(reportView.getLastExcecution());
      reportViewObj.setReportArea(reportView.getReportArea());
      if (reportView.getGroupByList().size() > 0)
        reportViewObj.setGroupBy(reportView.getGroupByList().size() + " elements");
      if (reportView.getOrderByList().size() > 0)
        reportViewObj.setOrderBy(createNewOrderByList(reportView.getOrderByList()));
      reportViewObj.setPurpose(reportView.getPurpose());
      reportViewObj.setTableName(entry.getKey());
      reportViewObj = createNewReportView(reportViewObj);
      for (Columns columns : entry.getValue()) {
        columns.setReportName(reportViewObj.getReportName());
        columns.setTableColumn(reportViewObj.getTableName());
        createColumnsObj(columns);
      }
    }
  }

  private String createNewOrderByList(List<String> orderByList) {
    String orderByStr = null;
    for (int i = 0; i < orderByList.size(); i++) {
      if (i == 0) orderByStr = orderByList.get(0);
      else orderByStr = orderByStr + "," + orderByList.get(i);
    }
    return orderByStr;
  }

  private String createNewFieldByList(List<String> fieldByList) {
    String fieldByStr = null;
    for (int i = 0; i < fieldByList.size(); i++) {
      if (i == 0) fieldByStr = fieldByList.get(0);
      else fieldByStr = fieldByStr + "-" + fieldByList.get(i);
    }
    return fieldByStr;
  }

  @Override
  public ReportView createNewReportView(ReportView reportView) {
    return reportRepository.save(reportView);
  }

  @Override
  public Columns createColumnsObj(Columns columns) {
    return columnsRepository.save(columns);
  }

  @Override
  public void saveNewReportViewWithMapGrpBy(List<GroupByCustom> groupByList, String reportName) {
    for (GroupByCustom gp : groupByList) {
      GroupByCustom gpObj = new GroupByCustom();
      gpObj.setColumnName(gp.getColumnName());
      gpObj.setOperation(gp.getOperation());
      gpObj.setReportName(reportName);
      groupByCustomRepo.save(gpObj);
    }
  }

  @Override
  public void saveNewReportViewWithMapFilter(List<Filter> filterList, String reportName) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
    for (Filter filter : filterList) {
      filter.setReportName(reportName);
      if (!filter.getFields().isEmpty() && filter.getFields().size() > 0) {
        for (Field field : filter.getFields()) {
          if (!field.getColumnValues().isEmpty() || field.getColumnValues().size() > 0)
            field.setColumnValue(createNewFieldByList(field.getColumnValues()));
          field.setFilterName(filter.getFilterName());
          // field.setFromDate(formatter.format(field.getFromDate()));
          fieldRepository.save(field);
        }
      }
      filterRepository.save(filter);
    }
  }

  @Override
  public ReportView generateReportViewWithMap(List<ReportView> reportViewList) {
    List<Columns> columnsList = new ArrayList<Columns>();
    ReportView reportViewWithMap = new ReportView();
    Map<String, List<Columns>> tableColumnMap = new HashMap<String, List<Columns>>();
    columnsList = getColumnsForReportName(reportViewList.get(0).getReportName());
    if (!columnsList.isEmpty()) {
      for (int i = 0; i < reportViewList.size(); i++) {
        ArrayList<Columns> mapValueList = new ArrayList<Columns>();
        reportViewWithMap.setReportName(reportViewList.get(i).getReportName());
        reportViewWithMap.setOwner(reportViewList.get(i).getOwner());
        reportViewWithMap.setSharedTo(reportViewList.get(i).getSharedTo());
        reportViewWithMap.setActions(reportViewList.get(i).getActions());
        reportViewWithMap.setCreatedDate(reportViewList.get(i).getCreatedDate());
        reportViewWithMap.setLastExcecution(reportViewList.get(i).getLastExcecution());
        reportViewWithMap.setGroupBy(reportViewList.get(i).getGroupBy());
        reportViewWithMap.setOrderBy(reportViewList.get(i).getOrderBy());
        reportViewWithMap.setPurpose(reportViewList.get(i).getPurpose());
        reportViewWithMap.setReportArea(reportViewList.get(i).getReportArea());
        for (Columns column : columnsList) {
          if (!tableColumnMap.containsKey(column.getTableColumn()))
            if (column.getTableColumn().equalsIgnoreCase(reportViewList.get(i).getTableName()))
              mapValueList.add(column);
        }
        tableColumnMap.put(reportViewList.get(i).getTableName(), mapValueList);
      }
      reportViewWithMap.setTableColumnMap(tableColumnMap);
    }
    return reportViewWithMap;
  }

  @Override
  public List<Columns> getColumnsForReportName(String reportName) {
    return columnsRepository.findByReportName(reportName);
  }

  @Override
  public ReportView generateReportWithMapGrpBy(ReportView finalReportView) {
    List<GroupByCustom> groupByList =
        groupByCustomRepo.findByReportName(finalReportView.getReportName());
    if (groupByList.size() > 0) finalReportView.setGroupByList(groupByList);
    return finalReportView;
  }

  @Override
  public ReportView generateReportWithMapOrderBy(ReportView finalReportView) {
    String str = finalReportView.getOrderBy();
    List<String> orderByList = Arrays.asList(str.split(","));
    finalReportView.setOrderByList(orderByList);
    return finalReportView;
  }

  @Override
  public List<Filter> getFilterByReportName(String reportName) {
    List<Filter> filterList = new ArrayList<Filter>();
    List<Field> fieldList = new ArrayList<Field>();
    filterList = filterRepository.findByReportName(reportName);
    if (filterList.size() > 0) {
      for (Filter filter : filterList) {
        fieldList = fieldRepository.findByFilterName(filter.getFilterName());
        if (!fieldList.isEmpty() || fieldList.size() > 0) {
          filter.setFields(fieldList);
        }
      }
    }
    return filterList;
  }

  @Override
  public Filter getFilterByFilterName(String filterName) {
    return filterRepository.findByFilterName(filterName);
  }

  @Override
  public Filter createFilterReport(Filter filter, ReportView reportView) {
    if (reportView.getReportName() != null && reportView.getReportArea() != null)
      createNewReportView(reportView);
    return filterRepository.save(filter);
  }

  @Override
  public List<Field> getFieldsByFilterName(String filterName) {
    return fieldRepository.findByFilterName(filterName);
  }
}
