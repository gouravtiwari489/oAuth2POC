package com.reportgenerator.reports.repository;

import com.reportgenerator.reports.domain.ReportsMetaData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsMetaDataRepository extends JpaRepository<ReportsMetaData, Integer> {

  List<ReportsMetaData> findByReportArea(String reportArea);

  ReportsMetaData findByColumnDisplayNameAndReportArea(String columnDisplayName, String reportArea);
}
