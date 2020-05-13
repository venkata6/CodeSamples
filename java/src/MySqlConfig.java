
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.listen.AfterQueryListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import javax.persistence.EntityManagerFactory;import javax.sql.DataSource;

@Configuration@EnableTransactionManagement@EnableJpaRepositories(
    entityManagerFactoryRef = "mysqlEntityManagerFactory",    transactionManagerRef = "mysqlTransactionManager",    
    basePackages = "your package")
@ComponentScan({"your package.*"})
@EntityScan("your package")

public class MysqlRepoCfg {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger("your class");   
    @Bean(name = "mysqlDataSource")
    public DataSource dataSource(MySQLHikariProperties mySQLHikariProperties) {


        HikariConfig config = new HikariConfig();        config.setDriverClassName(mySQLHikariProperties.getMySQLDriverClassName());        
        config.setJdbcUrl(mySQLHikariProperties.getMySQLJdbcUrl());       
        config.setUsername(mySQLHikariProperties.getUsername());        
        config.setPassword(mySQLHikariProperties.getPassword());        
        config.setMaximumPoolSize(mySQLHikariProperties.getMySQLMaximumPoolSize());        
        config.setLeakDetectionThreshold(mySQLHikariProperties.getMySQLLeakDetectionThreshold());        
        config.setConnectionTimeout(mySQLHikariProperties.getMySQLConnectionTimeout());        
        config.setMinimumIdle(mySQLHikariProperties.getMySQLMinimumIdle());        
        config.setPoolName(mySQLHikariProperties.getMySQLPoolName());

//        config.addDataSourceProperty("databaseName", "MySQL");
        HikariDataSource ds = new HikariDataSource(config);

        return ds;    }

    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    mysqlEntityManagerFactory(
        EntityManagerFactoryBuilder builder,        @Qualifier("mysqlDataSource") DataSource mysqlDataSource
    ) {


       return            builder
                .dataSource(mysqlDataSource)
                .packages("com.roku.server.takedown.model")
                .build();    }


    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(
        @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory
            mysqlEntityManagerFactory
    ) {
        return new JpaTransactionManager(mysqlEntityManagerFactory);    }

    @Bean(name = "mysqlTransactionTemplate")
    public TransactionTemplate MySQLJdbcTemplate(@Qualifier("mysqlTransactionManager") PlatformTransactionManager tManager) {
        return new TransactionTemplate(tManager);    }

    @Bean(name="mySQLFluentJdbc")
    public FluentJdbc fluentJdbc(@Qualifier("mysqlDataSource") DataSource dataSource, EnvironmentConfig environmentConfig) {

        AfterQueryListener listener = execution -> {
            if(execution.success()) {
                LOGGER.debug(
                        String.format(
                                "MySQL: Query took %s ms to execute: %s",                                
                                execution.executionTimeMs(),                                
                                execution.sql()
                        )
                );            } else {
                LOGGER.error(
                        String.format(
                                "MySQL: Query failed % , error %s",                                
                                execution.sql(), execution.sqlException()
                        )
                );            }
        };
        FluentJdbc fluentJdbc = new FluentJdbcBuilder()
                .connectionProvider(dataSource)
                .afterQueryListener(listener)
                .build();        return fluentJdbc;    }


}
