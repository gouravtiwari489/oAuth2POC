package com.reportgenerator.reports.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "osi_filter")
public class Filter implements Serializable {

  private Integer filterId;
  private String filterName;
  private String reportName;
  private List<Field> fields;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "filter_id")
  public Integer getFilterId() {
    return filterId;
  }

  public void setFilterId(Integer filterId) {
    this.filterId = filterId;
  }

  @Column(name = "filter_name")
  public String getFilterName() {
    return filterName;
  }

  public void setFilterName(String filterName) {
    this.filterName = filterName;
  }

  @Column(name = "report_name")
  public String getReportName() {
    return reportName;
  }

  public void setReportName(String reportName) {
    this.reportName = reportName;
  }

  @Transient
  public List<Field> getFields() {
    return fields;
  }

  public void setFields(List<Field> fields) {
    this.fields = fields;
  }
}
