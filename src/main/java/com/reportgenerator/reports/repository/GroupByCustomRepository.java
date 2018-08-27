package com.reportgenerator.reports.repository;

import com.reportgenerator.reports.domain.GroupByCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupByCustomRepository extends JpaRepository<GroupByCustom, Integer> {

  List<GroupByCustom> findByReportName(String reportName);

  void deleteByReportName(String reportName);
}
