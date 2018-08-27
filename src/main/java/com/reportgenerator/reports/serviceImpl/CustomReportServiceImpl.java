package com.reportgenerator.reports.serviceImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportgenerator.reports.configuration.DynamicReportConfiguration;
import com.reportgenerator.reports.security.filter.AuthTokenStore;

@Service
public class CustomReportServiceImpl {

  @Autowired DynamicReportConfiguration dyc;

  @Autowired ReportGeneratorService reportService;
  
  @Autowired AuthTokenStore tokenStore;
  
  

  String resource_utilization_query =
      "select ((sum(ote.hours)/(oo.TOTAL_HRS_PER_YEAR/12))*100) AS 'Utiliztion%'  ,oo.org_name AS 'Organization',oe.full_name AS 'Employee'"
          + " from osi_timesheet_entry ote,osi_organizations oo,osi_employees oe,osi_calendar oc"
          + " where oo.org_id = ote.org_id"
          + " and oe.employee_id = ote.employee_id"
          + " and oc.calendar_id = ote.calendar_id"
          + " and (oc.WEEK_START_DATE > STR_TO_DATE (? ,'%Y-%m-%d') OR oc.WEEK_START_DATE = STR_TO_DATE (? ,'%Y-%m-%d'))"
          + " AND (oc.WEEK_END_DATE = STR_TO_DATE(? ,'%Y-%m-%d')  OR oc.WEEK_END_DATE < STR_TO_DATE(? ,'%Y-%m-%d'))"
          + " group by org_name,full_name "
          + " order by org_name";

  String resource_assignment_query =
      "SELECT oc.customer_name AS 'Customer',op.project_name  AS 'Project',oe.full_name AS 'Employee',osa.activity_name AS 'Task/Activity' FROM  osi_projects op, osi_customers oc,"
          + " osi_employees oe, osi_project_resources pr,osi_sow_activities osa"
          + " WHERE pr.employee_id = oe.employee_id"
          + " AND op.customer_id = oc.customer_id AND op.project_status = 'A' AND pr.res_end_date <= CURDATE() AND osa.project_id = op.project_id"
          + " AND pr.sow_activity_id =  osa.sow_activity_id"
          + " GROUP BY full_name ORDER BY customer_name";

  String resource_distribution_query =
      "select oe.full_name AS 'Employee',oc.customer_name AS 'Customer',od.segment7 AS 'Department'"
          + " from  osi_employees oe, osi_customers oc, osi_projects op,"
          + " osi_assignments oa, osi_departments od, osi_project_resources opr"
          + " where oe.employee_id = opr.employee_id "
          + "and opr.project_id = opr.project_id"
          + " and op.customer_id = oc.customer_id"
          + " and oe.employee_id = oa.employee_id"
          + " and od.dept_id = oa.dept_id"
          + " and od.org_id = oe.org_id"
          + " and op.project_status = 'A'"
          + " and opr.res_end_date <= CURDATE()";

  String emloyee_leaves_query =
      "SELECT oe.full_name AS 'Employee',SUM(no_of_hours) AS 'Total Hours',transaction_date AS 'Transaction Date', "
          + "CASE WHEN transaction_type = b'1' THEN 'Credited' "
          + " WHEN transaction_type = b'0' THEN 'Debited' END AS 'Weight Group' "
          + "FROM osi_leaves_account olc,osi_employees oe "
          + "WHERE oe.employee_id = olc.employee_id"
          + "GROUP BY oe.full_name ";

  String employee_expenses =
      "SELECT oer.expense_report_id AS 'ExpenseId',oem.full_name AS 'Employee' ,oet.type_name AS 'Type of expense',"
          + " IF(oex.client_billable = b'1', 'Billable', 'Non Billable') AS 'expense billable',opr.project_name 'Project'"
          + " FROM  osi_expense_report oer, osi_employees oem, osi_expense_types oet, osi_expenses oex, osi_projects opr"
          + " WHERE OER.employee_id = OEM.employee_id"
          + " AND oex.expense_report_id = oer.expense_report_id"
          + " AND oet.type_id = oex.expense_type";

  public byte[] generateUtilisationReport(String userName,String authToken) throws Exception {
    String query = getPopulatedQuery();
    String reportName = "Utilisation Report";
    Map<String, String> columnMap = new HashMap<>();
    columnMap.put("Utiliztion%", "double");
    columnMap.put("Organization", "string");
    columnMap.put("Employee", "string");
    return reportService.generateReport(query, reportName, columnMap, userName,getUserOrganization(authToken));
  }

private String getUserOrganization(String authToken) {
	return tokenStore.getUserOrganization(authToken);
}

  private String getPopulatedQuery() throws SQLException {
    PreparedStatement pstmt = dyc.connection().prepareStatement(resource_utilization_query);
    Calendar now = Calendar.getInstance(); // Gets the current date and time
    int year = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH);
    int monthLastDate = now.getActualMaximum(Calendar.DATE);
    int day = 1;
    String date = year + "-" + month + "-" + day;
    String lastDate = year + "-" + month + "-" + monthLastDate;
    pstmt.setString(1, date);
    pstmt.setString(2, date);
    pstmt.setString(3, lastDate);
    pstmt.setString(4, lastDate);
    String text = pstmt.toString();
    return text.substring(text.indexOf(": ") + 2).replace("]", "");
  }

  public byte[] generateReportsForResourceAssignmentReport(String userName,String authToken) throws Exception {
    String reportName = " Resources assignment for Project";
    Map<String, String> columnMap = new HashMap<>();
    columnMap.put("Customer", "string");
    columnMap.put("Project", "string");
    columnMap.put("Employee", "string");
    columnMap.put("Task/Activity", "string");
    return reportService.generateReport(resource_assignment_query, reportName, columnMap, userName,getUserOrganization(authToken));
  }

  public byte[] generateResourceDistributionReport(String userName,String authToken) throws Exception {
    String reportName = " Resources Distribution for Project";
    Map<String, String> columnMap = new HashMap<>();
    columnMap.put("Employee", "string");
    columnMap.put("Customer", "string");
    columnMap.put("Department", "string");
    return reportService.generateReport(
        resource_distribution_query, reportName, columnMap, userName,getUserOrganization(authToken));
  }

  public byte[] generateLeavesReport(String userName,String authToken) throws Exception {
    String reportName = " Employee leaves Report";
    Map<String, String> columnMap = new HashMap<>();
    columnMap.put("Employee", "string");
    columnMap.put("Total Hours", "integer");
    columnMap.put("Transaction Date", "date");
    columnMap.put("Weight Group", "string");
    return reportService.generateReport(emloyee_leaves_query, reportName, columnMap, userName,getUserOrganization(authToken));
  }

  public byte[] generateExpensesReport(String userName,String authToken) throws Exception {
    String reportName = " Employee expenses Report";
    Map<String, String> columnMap = new HashMap<>();
    columnMap.put("ExpenseId", "integer");
    columnMap.put("Employee", "string");
    columnMap.put("Type of expense", "string");
    columnMap.put("expense billable", "string");
    columnMap.put("Project", "string");
    return reportService.generateReport(employee_expenses, reportName, columnMap, userName,getUserOrganization(authToken));
  }
}
