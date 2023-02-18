package br.net.dac.manager.Infrastructure.Persistence.Configurations;

import java.util.HashMap;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "br.net.dac.manager.Infrastructure.Persistence.Repositories", entityManagerFactoryRef = "managerEntityManager", transactionManagerRef = "managerTransactionManager")
public class ManagerDatabase {

  @Autowired
  private Environment env;

  @Bean
  @ConfigurationProperties("spring.datasource.manager")
  public DataSourceProperties managerDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  public DataSource managerDataSource() {
    return managerDataSourceProperties()
        .initializeDataSourceBuilder()
        .build();
  }

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean managerEntityManager(
      @Qualifier("managerDataSource") DataSource dataSource,
      EntityManagerFactoryBuilder builder) {
    final HashMap<String, Object> properties = new HashMap<String, Object>();
    properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
    properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));

    return builder
        .dataSource(dataSource)
        .packages("br.net.dac.manager.Domain.Entities")
        .properties(properties)
        .build();
  }

  @Bean
  public PlatformTransactionManager managerTransactionManager(
      @Qualifier("managerEntityManager") LocalContainerEntityManagerFactoryBean managerEntityManager) {
    return new JpaTransactionManager(Objects.requireNonNull(managerEntityManager.getObject()));
  }
}