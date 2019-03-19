package com.leonds.config;

import com.leonds.core.OperatorServiceImpl;
import com.leonds.core.orm.MyMapWrapperFactory;
import com.leonds.core.orm.OperatorService;
import com.leonds.core.orm.PersistenceManagerImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Leon
 */
@Configuration
public class MyBatisConfig {

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sessionFactory.setMapperLocations(resolver.getResources("classpath*:com/leonds/**/mapper/*.xml"));

            org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
            configuration.setMapUnderscoreToCamelCase(true);

            sessionFactory.setObjectWrapperFactory(new MyMapWrapperFactory());

            sessionFactory.setConfiguration(configuration);
            return sessionFactory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public OperatorService operatorService() {
        return new OperatorServiceImpl();
    }

    @Bean
    public PersistenceManagerImpl persistenceManager(SqlSessionFactory sqlSessionFactory, OperatorService operatorService) {
        PersistenceManagerImpl persistenceManager = new PersistenceManagerImpl();
        persistenceManager.setSqlSessionFactory(sqlSessionFactory);
        persistenceManager.setOperatorService(operatorService);
        return persistenceManager;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.leonds.core.orm");
        return mapperScannerConfigurer;
    }

}
