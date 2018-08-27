package com.reportgenerator.reports.serviceImpl;

import com.reportgenerator.reports.configuration.DynamicReportConfiguration;
import com.reportgenerator.reports.domain.ReportsMetaData;
import com.reportgenerator.reports.domain.TableConstraint;
import com.reportgenerator.reports.repository.ConstraintRepository;
import com.reportgenerator.reports.repository.ReportsMetaDataRepository;
import com.reportgenerator.reports.service.IReportMetaDataService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportMetaDataServiceImpl implements IReportMetaDataService {

  @Autowired ConstraintRepository constraintRepository;

  @Autowired ReportsMetaDataRepository reportsMetaDataRepository;

  @Autowired DynamicReportConfiguration dRC;

  @Override
  public ReportsMetaData getFilterConditionForColAndReportArea(
      String columnDisplayName, String ReportArea) {
    return reportsMetaDataRepository.findByColumnDisplayNameAndReportArea(
        columnDisplayName, ReportArea);
  }

  @Override
  public List<ReportsMetaData> getReportsMetaDataForReportArea(String reportArea) {
    return reportsMetaDataRepository.findByReportArea(reportArea);
  }

  @Override
  public List<TableConstraint> getEntireTableConstraintsList(String reportArea) {
    List<TableConstraint> tableConstraints = new ArrayList<TableConstraint>();
    tableConstraints = constraintRepository.findByReportArea(reportArea);
    return tableConstraints;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getFirstTimeSaveConstraintsData(String reportArea) {
    Session session = dRC.getSessionFactory().openSession();
    List<TableConstraint> responseList = new ArrayList<TableConstraint>();
    List<String> allUniqueTableNameList = new ArrayList<String>();
    String tableName = getTableNameByReportArea(reportArea);
    try {
      session.beginTransaction();
      String queryString =
          "select distinct(COLUMN_NAME) as columnName , CONSTRAINT_NAME as constraintName, "
              + "REFERENCED_COLUMN_NAME as referencedColumnName, REFERENCED_TABLE_NAME as referencedTableName"
              + " from information_schema.KEY_COLUMN_USAGE"
              + " where TABLE_NAME ='"
              + tableName
              + "'"+"AND table_schema = 'osi_one_db'";
      responseList =
          session
              .createSQLQuery(queryString)
              .addScalar("columnName")
              .addScalar("constraintName")
              .addScalar("referencedColumnName")
              .addScalar("referencedTableName")
              .setResultTransformer(Transformers.aliasToBean(TableConstraint.class))
              .list();
      for (TableConstraint tableConstraint : responseList) {
        tableConstraint.setParentTableName(tableName);
        tableConstraint.setReportArea(reportArea);
        constraintRepository.save(tableConstraint);
      }
      session.getTransaction().commit();
      allUniqueTableNameList.add(tableName);
      //Map<String, List<Columns>> responseMap = createTableColumnMapping(responseList, tableName);
      //createTableColumnMapping(responseList, tableName);
      for (TableConstraint tableConstraint : responseList) {
        //Map<String, List<Columns>> secondLevelResMap = Collections.EMPTY_MAP;
        if (tableConstraint.getReferencedTableName() != null) {
          if (!allUniqueTableNameList.contains(tableConstraint.getReferencedTableName())) {
            getSecondLevelRefTable(tableConstraint.getReferencedTableName(), session, allUniqueTableNameList, reportArea);
            allUniqueTableNameList.add(tableConstraint.getReferencedTableName());
            //responseMap.putAll(secondLevelResMap);
          }
        }
      }
    } catch (Exception e) {
      if (session.getTransaction() != null) session.getTransaction().rollback();
    } finally {
      session.close();
    }
  }

  private String getTableNameByReportArea(String reportArea) {
	  String tableName = null;
	  if(reportArea.equalsIgnoreCase("timesheet"))
	    	tableName = "osi_timesheet_entry";
	    else if(reportArea.equalsIgnoreCase("expenses"))
	    	tableName = "osi_expenses";
	    else if(reportArea.equalsIgnoreCase("employee"))
	    	tableName = "osi_employees";
	    else if(reportArea.equalsIgnoreCase("project"))
	    	tableName = "osi_projects";
	    else if(reportArea.equalsIgnoreCase("invoice"))
	    	tableName = "osi_invoices";
	    else if(reportArea.equalsIgnoreCase("leaves"))
	    	tableName = "osi_leaves";
	    else if(reportArea.equalsIgnoreCase("PAndL"))
	    	tableName = "";
	  return tableName;
}

@SuppressWarnings("unchecked")
  private void getSecondLevelRefTable(
      String referencedTableName, Session session, List<String> allUniqueTableNameList, String reportArea) {
    List<TableConstraint> responseList = Collections.EMPTY_LIST;
    //Map<String, List<Columns>> secondLevelResMap = Collections.EMPTY_MAP;
    try {
      session.beginTransaction();
      String queryString =
          "select distinct(COLUMN_NAME) as columnName , CONSTRAINT_NAME as constraintName, "
              + "REFERENCED_COLUMN_NAME as referencedColumnName, REFERENCED_TABLE_NAME as referencedTableName"
              + " from information_schema.KEY_COLUMN_USAGE"
              + " where TABLE_NAME = '"
              + referencedTableName
              + "'";
      responseList =
          session
              .createSQLQuery(queryString)
              .addScalar("columnName")
              .addScalar("constraintName")
              .addScalar("referencedColumnName")
              .addScalar("referencedTableName")
              .setResultTransformer(Transformers.aliasToBean(TableConstraint.class))
              .list();
      for (TableConstraint tableConstraint : responseList) {
        tableConstraint.setParentTableName(referencedTableName);
        tableConstraint.setReportArea(reportArea);
        constraintRepository.save(tableConstraint);
      }
      //secondLevelResMap = createTableColumnMapping(responseList, referencedTableName);
      for (TableConstraint tableConstraint : responseList) {
        //Map<String, List<Columns>> nLevelResMap = Collections.EMPTY_MAP;
        if (tableConstraint.getReferencedTableName() != null) {
          if (!allUniqueTableNameList.contains(tableConstraint.getReferencedTableName())) {
            allUniqueTableNameList.add(tableConstraint.getReferencedTableName());
            //nLevelResMap =
            getThirdLevelRefTable(
                tableConstraint.getReferencedTableName(), session, allUniqueTableNameList, reportArea);
            //responseMap.putAll(nLevelResMap);
          }
        }
      }
    } catch (Exception e) {
      if (session.getTransaction() != null) session.getTransaction().rollback();
    }
    //return secondLevelResMap;
  }

  @SuppressWarnings("unchecked")
  private void getThirdLevelRefTable(
      String referencedTableName, Session session, List<String> allUniqueTableNameList, String reportArea) {
    List<TableConstraint> responseList = Collections.EMPTY_LIST;
    //Map<String, List<Columns>> secondLevelResMap = Collections.EMPTY_MAP;
    try {
      session.beginTransaction();
      String queryString =
          "select distinct(COLUMN_NAME) as columnName , CONSTRAINT_NAME as constraintName, "
              + "REFERENCED_COLUMN_NAME as referencedColumnName, REFERENCED_TABLE_NAME as referencedTableName"
              + " from information_schema.KEY_COLUMN_USAGE"
              + " where TABLE_NAME = '"
              + referencedTableName
              + "'";
      responseList =
          session
              .createSQLQuery(queryString)
              .addScalar("columnName")
              .addScalar("constraintName")
              .addScalar("referencedColumnName")
              .addScalar("referencedTableName")
              .setResultTransformer(Transformers.aliasToBean(TableConstraint.class))
              .list();
      for (TableConstraint tableConstraint : responseList) {
        if (tableConstraint.getReferencedTableName() != null) {
          tableConstraint.setParentTableName(referencedTableName);
          tableConstraint.setReportArea(reportArea);
          constraintRepository.save(tableConstraint);
        }
      }
      //secondLevelResMap = createTableColumnMapping(responseList, referencedTableName);
    } catch (Exception e) {
      if (session.getTransaction() != null) session.getTransaction().rollback();
    }
    //return secondLevelResMap;
  }
}
