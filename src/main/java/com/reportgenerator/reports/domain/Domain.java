package com.reportgenerator.reports.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "domain")
public class Domain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "domain_id")
  String domainId;

  @Transient List<Object> tables;
}
