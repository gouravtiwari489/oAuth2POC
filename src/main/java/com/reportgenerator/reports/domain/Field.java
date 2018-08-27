package com.reportgenerator.reports.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "osi_field")
public class Field implements Serializable{

  private Integer fieldId;
  private String filterName;
  private String columnName;
  private String columnDisplayName;
  private String operation;
  private String columnValue;
  private Date fromDate;
  private Date toDate;
  private String tableName;

  private List<String> columnValues;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "field_id", unique = true, nullable = false)
  public Integer getFieldId() {
    return fieldId;
  }

  public void setFieldId(Integer fieldId) {
    this.fieldId = fieldId;
  }

  @Column(name = "column_name")
  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  @Column(name = "operation")
  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  @Transient
  public List<String> getColumnValues() {
    return columnValues;
  }

  public void setColumnValues(List<String> columnValues) {
    this.columnValues = columnValues;
  }

  @Column(name = "filter_name")
  public String getFilterName() {
    return filterName;
  }

  public void setFilterName(String filterName) {
    this.filterName = filterName;
  }

  @Column(name = "column_values")
  public String getColumnValue() {
    return columnValue;
  }

  public void setColumnValue(String columnValue) {
    this.columnValue = columnValue;
  }

  @Column(name = "column_display_name")
  public String getColumnDisplayName() {
    return columnDisplayName;
  }

  public void setColumnDisplayName(String columnDisplayName) {
    this.columnDisplayName = columnDisplayName;
  }

  @Column(name = "from_date")
  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  @Column(name = "to_date")
  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

  @Column(name = "table_name")
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
}
