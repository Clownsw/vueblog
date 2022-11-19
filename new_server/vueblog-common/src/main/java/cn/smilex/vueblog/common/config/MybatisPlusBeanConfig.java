package cn.smilex.vueblog.common.config;

import cn.smilex.vueblog.common.entity.VueBlogConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * @author smilex
 * @date 2022/11/11/21:20
 * @since 1.0
 */
@SuppressWarnings("all")
@Configuration
public class MybatisPlusBeanConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("cn.smilex.vueblog.*.dao");
        return mapperScannerConfigurer;
    }

    @Bean
    @DependsOn("vueBlogConfig")
    public DataSource dataSource(@NotNull VueBlogConfig vueBlogConfig) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setJdbcUrl(vueBlogConfig.getDataSourceJdbcUrl());
        hikariDataSource.setUsername(vueBlogConfig.getDataSourceUserName());
        hikariDataSource.setPassword(vueBlogConfig.getDatasourcePassWord());
        return hikariDataSource;
    }

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage("cn.smilex.vueblog.common.entity");
        sqlSessionFactory.setMapperLocations(
                new ClassPathResource("mappers/BlogDaoMapper.xml")
        );
        return sqlSessionFactory;
    }
}