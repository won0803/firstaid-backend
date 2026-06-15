package seeya.insight.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Configuration
@PropertySource("classpath:/application.properties")
@EnableTransactionManagement
//@MapperScan(basePackages = {"seeya.insight.mapper"})
public class DatabaseConfiguration {
  @Autowired
  private ApplicationContext applicationContext;

  /**
   * 스프링 부트의 기본 데이터소스 프로퍼티 리더를 생성합니다.
   * application.properties의 spring.datasource 하위 설정을 유연하게 파싱합니다.
   */
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  /**
   * @Company : 시야인사이트
   * @Method Name : dataSource
   * @작성일 : 2021. 1. 22.
   * @작성자 : herotice
   * @변경이력 : 2026. 06. 05 수정 (Log4jdbc URL 바인딩 버그 픽스)
   * @Method 설명 : DataSource 설정 처리
   */
  @Primary
  @Bean(name="dataSource")
  public DataSource dataSource() {
    DataSourceProperties properties = dataSourceProperties();

    // 프로퍼티에서 읽어온 값들을 히카리 설정 객체에 명시적으로 주입합니다.
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(properties.getDriverClassName());

    // application.properties에 url 혹은 jdbc-url 중 어떤 것으로 적혀있어도 안전하게 매핑되도록 처리합니다.
    String url = properties.getUrl() != null ? properties.getUrl() : properties.getXa().getProperties().get("jdbc-url");
    if (url == null) {
      // 명시적 가이드라인에 따른 최후의 폴백 경로
      url = "jdbc:log4jdbc:mysql://localhost:3306/map?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&serverTimezone=Asia/Seoul";
    }
    config.setJdbcUrl(url);

    config.setUsername(properties.getUsername());
    config.setPassword(properties.getPassword());

    return new HikariDataSource(config);
  }

  /**
   * @Company : 시야인사이트
   * @Method Name : sqlSessionFactory
   * @작성일 : 2021. 1. 22.
   * @작성자 : herotice
   * @변경이력 :
   * @Method 설명 : SqlSessionFactory 설정
   */
  @Bean(name="sqlSessionFactory")
  public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier("dataSource") DataSource dataSource) throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);
    // sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/*-query.xml"));
    sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/*/*_SQL.xml"));
    sqlSessionFactoryBean.setTypeAliasesPackage("seeya.insight.dto");
    return sqlSessionFactoryBean.getObject();
  }

  /**
   * @Company : 시야인사이트
   * @Method Name : sqlSessionTemplate
   * @작성일 : 2021. 1. 22.
   * @작성자 : herotice
   * @변경이력 :
   * @Method 설명 : sqlSessionTemplate 설정
   */
  @Bean(name="sqlSessionTemplate")
  public SqlSessionTemplate sqlSessionTemplate(@Autowired @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

  /**
   * @methodName   : transactionManager
   * @author       : herotice
   * @date         : 2020.10.11
   * @description  : DB Transaction 설정
   */
  @Bean
  PlatformTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }

  /**
   * @Company : 시야인사이트
   * @Method Name : txAdvice
   * @작성일 : 2021. 1. 22.
   * @작성자 : herot
   * @변경이력 :
   * @Method 설명 : DB Transaction 설정
   */
  @Bean
  public TransactionInterceptor txAdvice() {
    final int TX_METHOD_TIMEOUT = 3 * 60;

    TransactionInterceptor txAdvice = new TransactionInterceptor();

    List<RollbackRuleAttribute> rollbackRules = new ArrayList<RollbackRuleAttribute>();
    rollbackRules.add(new RollbackRuleAttribute(Exception.class));

    DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
    readOnlyAttribute.setReadOnly(true);
    readOnlyAttribute.setTimeout(TX_METHOD_TIMEOUT);

    RuleBasedTransactionAttribute writeAttribute = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
    writeAttribute.setTimeout(TX_METHOD_TIMEOUT);

    String readOnlyTransactionDefinition = readOnlyAttribute.toString();
    String writeTransactionDefinition = writeAttribute.toString();

    log.info("ReadOnly Attribute : {}", readOnlyTransactionDefinition);
    log.info("ReadOnly Attribute : {}", readOnlyTransactionDefinition);

    // write rollback rule (Exception 발생시 rollback 처리할 메소드를 지정한다. ServiceImpl 단에서 insert, update, delete 로 시작하는 메소드는 예외 발생시 롤백대상에 해당됨.)
    Properties txAttributes = new Properties();
    txAttributes.setProperty("insert*", writeTransactionDefinition);
    txAttributes.setProperty("update*", writeTransactionDefinition);
    txAttributes.setProperty("delete*", writeTransactionDefinition);

    // readonly (위 write rollback 대상에 해당되지 않는 메소드는 전부 readonly 대상으로 예외 발생시 rollback 처리하지 않는다.)
    txAttributes.setProperty("*",readOnlyTransactionDefinition);

    txAdvice.setTransactionAttributes(txAttributes);
    txAdvice.setTransactionManager(transactionManager());

    return txAdvice;
  }

  @Bean
  public Advisor txAdvisor() {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    // transaction 적용 대상은 *ServiceImpl 임.
    pointcut.setExpression("execution(* seeya.insight.service.*Impl.*(..))");
//        pointcut.setExpression("execution(* seeya.insight.service.impl.*.*(..))");
    // pointcut.setExpression("execution(* kr.co.seeya.service..*Service.*(..))");

    return new DefaultPointcutAdvisor(pointcut, txAdvice());
  }
}