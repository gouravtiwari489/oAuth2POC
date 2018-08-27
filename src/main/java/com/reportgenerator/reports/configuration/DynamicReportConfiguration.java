package com.reportgenerator.reports.configuration;

import java.sql.Connection;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DynamicReportConfiguration {

  @Autowired EntityManager em;

  public Connection connection() {
    SessionImpl impl = (SessionImpl) getSessionFactory().openSession();
    return impl.connection();
  }

  public SessionFactory getSessionFactory() {
    Session session = em.unwrap(Session.class);
    return session.getSessionFactory();
  }
}
