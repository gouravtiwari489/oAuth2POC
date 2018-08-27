package com.reportgenerator.reports.repository;

import com.reportgenerator.reports.domain.Filter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Integer> {

  List<Filter> findByReportName(String reportName);

  Filter findByFilterName(String filterName);

  List<Filter> deleteByReportName(String reportName);
}
