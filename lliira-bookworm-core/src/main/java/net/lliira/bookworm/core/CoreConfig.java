package net.lliira.bookworm.core;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("net.lliira.bookworm.core.persist.mapper")
public class CoreConfig {

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
	    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
	    sessionFactory.setDataSource(dataSource);
	    sessionFactory.setTypeAliasesPackage("net.lliira.bookworm.core.persist.entity");
	    return sessionFactory.getObject();
	}
}
