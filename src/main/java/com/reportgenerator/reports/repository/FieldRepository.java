package com.reportgenerator.reports.repository;

import com.reportgenerator.reports.domain.Field;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {

  List<Field> findByFilterName(String filterName);

  void deleteByFilterName(String filterName);
}
