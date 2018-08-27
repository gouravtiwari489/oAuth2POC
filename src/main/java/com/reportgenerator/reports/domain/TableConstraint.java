package com.reportgenerator.reports.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "osi_table_constraint")
public class TableConstraint implements Serializable {

  private String columnName;
  private String constraintName;
  private String referencedColumnName;
  private String referencedTableName;
  private String parentTableName;
  private Integer tableId;
  private String reportArea;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "table_id")
  public Integer getTableId() {
    return tableId;
  }

  public void setTableId(Integer tableId) {
    this.tableId = tableId;
  }

  @Column(name = "parent_table_name")
  public String getParentTableName() {
    return parentTableName;
  }

  public void setParentTableName(String parentTableName) {
    this.parentTableName = parentTableName;
  }

  @Column(name = "column_name")
  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  @Column(name = "constraint_name")
  public String getConstraintName() {
    return constraintName;
  }

  public void setConstraintName(String constraintName) {
    this.constraintName = constraintName;
  }

  @Column(name = "referenced_column_name")
  public String getReferencedColumnName() {
    return referencedColumnName;
  }

  public void setReferencedColumnName(String referencedColumnName) {
    this.referencedColumnName = referencedColumnName;
  }

  @Column(name = "referenced_table_name")
  public String getReferencedTableName() {
    return referencedTableName;
  }

  public void setReferencedTableName(String referencedTableName) {
    this.referencedTableName = referencedTableName;
  }

  @Column(name = "report_area")
  public String getReportArea() {
    return reportArea;
  }

  public void setReportArea(String reportArea) {
    this.reportArea = reportArea;
  }
}
