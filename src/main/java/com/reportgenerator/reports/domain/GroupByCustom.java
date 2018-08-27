package com.reportgenerator.reports.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groupbycustom")
public class GroupByCustom implements Serializable {

  private Integer groupById;
  private String operation;
  private String columnName;
  private String reportName;
  //private Integer reportId;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "group_by_id", unique = true, nullable = false)
  public Integer getGroupById() {
    return groupById;
  }

  public void setGroupById(Integer groupById) {
    this.groupById = groupById;
  }

  @Column(name = "operation")
  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  @Column(name = "column_name")
  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  @Column(name = "report_name")
  public String getReportName() {
    return reportName;
  }

  public void setReportName(String reportName) {
    this.reportName = reportName;
  }
  /*@Column(name = "report_id")
  public Integer getReportId() {
  	return reportId;
  }
  public void setReportId(Integer reportId) {
  	this.reportId = reportId;
  }*/

}
