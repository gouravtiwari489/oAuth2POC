package com.reportgenerator.reports.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "osi_reports_meta_data")
public class ReportsMetaData implements Serializable {

  private Integer reportsMetaDataId;
  private Integer reportAreaId;
  private String reportArea;
  private String columnName;
  private String columnDisplayName;
  private String tableName;
  private Integer filter;
  private String filterConditions;
  private String columnType;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "reports_meta_data_id", unique = true, nullable = false)
  public Integer getReportsMetaDataId() {
    return reportsMetaDataId;
  }

  public void setReportsMetaDataId(Integer reportsMetaDataId) {
    this.reportsMetaDataId = reportsMetaDataId;
  }

  @Column(name = "report_area_id")
  public Integer getReportAreaId() {
    return reportAreaId;
  }

  public void setReportAreaId(Integer reportAreaId) {
    this.reportAreaId = reportAreaId;
  }

  @Column(name = "report_area")
  public String getReportArea() {
    return reportArea;
  }

  public void setReportArea(String reportArea) {
    this.reportArea = reportArea;
  }

  @Column(name = "col_name")
  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  @Column(name = "col_display_name")
  public String getColumnDisplayName() {
    return columnDisplayName;
  }

  public void setColumnDisplayName(String columnDisplayName) {
    this.columnDisplayName = columnDisplayName;
  }

  @Column(name = "table_name")
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  @Column(name = "filter")
  public Integer getFilter() {
    return filter;
  }

  public void setFilter(Integer filter) {
    this.filter = filter;
  }

  @Column(name = "filter_conditions")
  public String getFilterConditions() {
    return filterConditions;
  }

  public void setFilterConditions(String filterConditions) {
    this.filterConditions = filterConditions;
  }

  @Column(name = "col_type")
  public String getColumnType() {
    return columnType;
  }

  public void setColumnType(String columnType) {
    this.columnType = columnType;
  }
}
