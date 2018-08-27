package com.reportgenerator.reports.repository;

import com.reportgenerator.reports.domain.Role;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

  ArrayList<Role> findByRoleId(int i);
}
