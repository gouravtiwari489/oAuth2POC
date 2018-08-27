package com.reportgenerator.reports.domain;

public enum FilterCriteria {
  HOURSSPENT(
      "select op.project_name , oc.PERIOD_NAME, count(PERIOD_NAME) as PERIOD_NAME_COUNT, oj.job_code_name, sum(ot.hours) 'Total' "
          + "from osi_projects op, osi_calendar oc, osi_timesheet_entry ot, osi_job_codes oj "
          + "where oc.ORG_ID = ot.ORG_ID AND ot.ORG_ID = oj.ORG_ID AND op.PROJECT_ID = ot.PROJECT_ID group by PERIOD_NAME"),
  PROJECTS(
      "SELECT  PROJECT_ID as projectId, PROJECT_NAME as projectName, PROJECT_STATUS as projectStatus FROM OSI_PROJECTS "),
  STRUCTURAL(
      "Eases the design by identifying a simple way to realize relationships between entities."),
  BEHAVIORAL(
      "Identifies common communication patterns between objects and realize these patterns.");

  private final String description;

  private FilterCriteria(final String description) {
    this.description = description;
  }

  public String describe() {
    return description;
  }
}
