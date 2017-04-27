
package com.mybatis;
import java.net.URL;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

//package com.mybatis;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
@EnableTransactionManagement
@MapperScan(basePackages="com.dao")
public class MyBatisConfig {
//DataSource配置
	@Autowired
	private Environment env;
	
//	@ConfigurationProperties(prefix = "spring.datasource")
    @Bean(name="dataSource",destroyMethod = "close")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
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
		HikariConfig config = new HikariConfig(url.getPath()+"config/hikari.properties");
		HikariDataSource dataSource = new HikariDataSource(config);

		return dataSource;*/
    }
 
    //提供SqlSeesion
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
 
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
 
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
 
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mappers/*.xml"));
 
        return sqlSessionFactoryBean.getObject();
    }
 

   /* @Bean
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		// TODO Auto-generated method stub
    	return new DataSourceTransactionManager(dataSource());
	}*/
    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
       return new SqlSessionTemplate(sqlSessionFactory);
    }
}

//
//@Configuration
////加上这个注解，使得支持事务
//@EnableTransactionManagement
//@MapperScan(basePackages={"com.dao"})
//public class MyBatisConfig implements TransactionManagementConfigurer {
//
//	@Autowired
//	private Environment env;
//	@Bean(name = "dataSource")
//  public DataSource dataSource() {
//	DruidDataSource dataSource = new DruidDataSource();
//	    
//    Properties prt = new Properties();
//    prt.setProperty("url", env.getProperty("spring.datasource.url"));
//    prt.setProperty("username", env.getProperty("spring.datasource.username"));
//    prt.setProperty("password", env.getProperty("spring.datasource.password"));
//    prt.setProperty("driverClassName", env.getProperty("spring.datasource.driverClassName"));
//    
//    dataSource.setConnectProperties(prt);
//    dataSource.setPoolPreparedStatements(true);
//    return dataSource;
//  }
//	  
//  
//  @Override
//  public PlatformTransactionManager annotationDrivenTransactionManager() {
//    return new DataSourceTransactionManager(dataSource());
//  }
//  
//  @Bean(name = "sqlSessionFactory")
//  public SqlSessionFactory sqlSessionFactoryBean() {
//    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//    bean.setDataSource(dataSource());
//    bean.setTypeAliasesPackage("com.model");
//    
//    //添加XML目录
//    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//    try {
//        bean.setMapperLocations(resolver.getResources("classpath:mappers/*.xml"));
//        return bean.getObject();
//    } catch (Exception e) {
//        e.printStackTrace();
//        throw new RuntimeException(e);
//    }
//  }
//  @Bean
//  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//    return new SqlSessionTemplate(sqlSessionFactory);
//  }
//}
// 