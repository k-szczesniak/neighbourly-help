package pl.lodz.p.ks.it.neighbourlyhelp.utils;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "pl.lodz.p.ks.it.neighbourlyhelp.repository",
        entityManagerFactoryRef = "primaryEntityManager",
        transactionManagerRef = "primaryTransactionManager")
@EnableTransactionManagement
public class JpaConfig {

    @Bean
    public JpaEntityPackagesToScanProvider jpaEntityPackagesToScanProvider() {
        return new JpaEntityPackagesToScanProvider();
    }

//    @Bean
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @PersistenceContext(unitName = "primary")
    @Bean(name = "primaryEntityManager")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(DataSource dataSource,
                                                                              JpaEntityPackagesToScanProvider jpaEntityPackagesToScanProvider,
                                                                              JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPackagesToScan(jpaEntityPackagesToScanProvider.provide());
        factory.setDataSource(dataSource);
        factory.setPersistenceUnitName("primary");
        return factory;
    }

//    @PersistenceContext(unitName = "secondary")
//    @Bean(name = "secondaryEntityManager")
//    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(DataSource dataSource,
//                                                                                JpaEntityPackagesToScanProvider jpaEntityPackagesToScanProvider,
//                                                                                JpaVendorAdapter jpaVendorAdapter) {
//        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(jpaVendorAdapter);
//        factory.setPackagesToScan(jpaEntityPackagesToScanProvider.provide());
//        factory.setDataSource(dataSource);
//        return factory;
//    }

    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setGenerateDdl(true);
        return vendorAdapter;
    }
}
