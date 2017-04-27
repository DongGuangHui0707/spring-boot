
package com.mybatis;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@Configuration
@EnableTransactionManagement
@MapperScan(basePackages="com.dao2",sqlSessionTemplateRef="sqlSessionTemplate2")
public class MyBatis2Config {
	
	//DataSource配置
    @Bean(name="dataSource2")
    @ConfigurationProperties(prefix = "spring.datasource2")
    public DataSource dataSource() {
		/*HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		dataSource.setConnectionTestQuery(env.getProperty("spring.datasource.connectionTestQuery"));
		*/
		/**
		connectionTestQuery=SELECT 1
		dataSourceClassName=org.postgresql.ds.PGSimpleDataSource
		dataSource.user=test
		dataSource.password=test
		dataSource.databaseName=mydb
		dataSource.serverName=localhost
		*/
    	return DataSourceBuilder.create().build();
    	/*URL url = this.getClass().getResource("/");
		HikariConfig config = new HikariConfig(url.getPath()+"config/hikari2.properties");
		HikariDataSource dataSource = new HikariDataSource(config);

		return dataSource;*/
    }
 
    //提供SqlSeesion
    @Bean(name="sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource2") DataSource dataSource) throws Exception {
 
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
 
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
 
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mappers2/*.xml"));
 
        return sqlSessionFactoryBean.getObject();
    }
 

    /*@Bean(name="annotationDrivenTransactionManager2")
	public PlatformTransactionManager annotationDrivenTransactionManager() {
    	return new DataSourceTransactionManager(dataSource());
	}*/
    @Bean(name = "transactionManager2")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource2") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean(name="sqlSessionTemplate2")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory) {
    	return new SqlSessionTemplate(sqlSessionFactory);
    }

}
