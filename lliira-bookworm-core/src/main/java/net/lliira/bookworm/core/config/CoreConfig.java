package net.lliira.bookworm.core.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.lliira.bookworm.core.service.AuthorService;
import net.lliira.bookworm.core.service.BookService;
import net.lliira.bookworm.core.service.CategoryService;

@Configuration
public class CoreConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage("net.lliira.bookworm.core.persist.model");
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.REUSE);
    }

    @Bean
    public AuthorService authorService() {
        return new AuthorService();
    }
    
    @Bean
    public BookService bookService() {
        return new BookService();
    }
    
    @Bean
    public CategoryService categoryService() {
        return new CategoryService();
    }
}
