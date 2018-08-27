package com.reportgenerator.reports.repository;

import com.reportgenerator.reports.domain.TableConstraint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConstraintRepository extends JpaRepository<TableConstraint, Integer> {

  public List<TableConstraint> findByReferencedTableName(String referencedTableName);
  public List<TableConstraint> findByReferencedTableNameAndReportArea(String referencedTableName, String reportArea);
  public List<TableConstraint> findByReportArea(String reportArea);
}
