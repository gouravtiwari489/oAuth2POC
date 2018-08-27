package com.reportgenerator.reports.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "reportview")
public class ReportView implements Serializable, Cloneable {

	private Integer reportId;
	private String reportName;
	private String reportArea;
	private String owner;
	private String sharedTo;
	private String actions;
	private String createdDate;
	private String lastExcecution;
	private Map<String, List<Columns>> tableColumnMap;
	private String groupBy;
	private List<GroupByCustom> groupByList;
	private List<String> orderByList;
	private String orderBy;
	private String purpose;
	private String tableName;
	private List<Filter> filters;
	private String selfJoined;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "report_id", unique = true, nullable = false)
	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	@Column(name = "report_name")
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "owner")
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name = "shared_to")
	public String getSharedTo() {
		return sharedTo;
	}

	public void setSharedTo(String sharedTo) {
		this.sharedTo = sharedTo;
	}

	@Column(name = "actions")
	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	@Column(name = "created_date")
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "last_excecution")
	public String getLastExcecution() {
		return lastExcecution;
	}

	public void setLastExcecution(String lastExcecution) {
		this.lastExcecution = lastExcecution;
	}

	@Column(name = "purpose")
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	@Transient
	public Map<String, List<Columns>> getTableColumnMap() {
		return tableColumnMap;
	}

	public void setTableColumnMap(Map<String, List<Columns>> tableColumnMap) {
		this.tableColumnMap = tableColumnMap;
	}

	@Column(name = "table_column")
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "group_by")
	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	@Transient
	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	@Transient
	public List<GroupByCustom> getGroupByList() {
		return groupByList;
	}

	public void setGroupByList(List<GroupByCustom> groupByList) {
		this.groupByList = groupByList;
	}

	@Transient
	public List<String> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(List<String> orderByList) {
		this.orderByList = orderByList;
	}

	@Column(name = "order_by")
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "self_joined")
	public String getSelfJoined() {
		return selfJoined;
	}

	public void setSelfJoined(String selfJoined) {
		this.selfJoined = selfJoined;
	}

	@Column(name = "report_area")
	public String getReportArea() {
		return reportArea;
	}

	public void setReportArea(String reportArea) {
		this.reportArea = reportArea;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
