package com.reportgenerator.reports.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "columns")
public class Columns implements Serializable {

  private Integer columnId;
  private String columnName;
  private String typeName;
  private Integer columnSize;
  private boolean checked;
  private String reportName;
  private String tableColumn;
  private String columnDisplayName;

  public Columns(
      Integer columnId,
      String columnName,
      String columnType,
      int columnSize,
      boolean checked,
      String reportName,
      String tableColumn) {
    this.columnId = columnId;
    this.columnName = columnName;
    this.typeName = columnType;
    this.columnSize = columnSize;
    this.checked = checked;
    this.reportName = reportName;
    this.tableColumn = tableColumn;
  }

  public Columns() {}

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "column_id", unique = true, nullable = false)
  public Integer getColumnId() {
    return columnId;
  }

  public void setColumnId(Integer columnId) {
    this.columnId = columnId;
  }

  @Column(name = "column_name")
  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  @Column(name = "column_type")
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  @Column(name = "column_size")
  public Integer getColumnSize() {
    return columnSize;
  }

  public void setColumnSize(Integer columnSize) {
    this.columnSize = columnSize;
  }

  @Column(name = "checked")
  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  @Column(name = "report_name")
  public String getReportName() {
    return reportName;
  }

  public void setReportName(String reportName) {
    this.reportName = reportName;
  }

  @Column(name = "table_column")
  public String getTableColumn() {
    return tableColumn;
  }

  public void setTableColumn(String tableColumn) {
    this.tableColumn = tableColumn;
  }

  @Column(name = "column_display_name")
  public String getColumnDisplayName() {
    return columnDisplayName;
  }

  public void setColumnDisplayName(String columnDisplayName) {
    this.columnDisplayName = columnDisplayName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Columns other = (Columns) obj;
    if (columnName == null) {
      if (other.columnName != null) return false;
    } else if (!columnName.equals(other.columnName)) return false;
    return true;
  }
}
