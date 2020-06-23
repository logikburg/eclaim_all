package hk.org.ha.eclaim.core.config;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
@PropertySources(value = { @PropertySource("classpath:/config/security.properties"),
		@PropertySource("classpath:/config/report.properties"),
		@PropertySource("classpath:/config/dataconv.properties") })
@EnableTransactionManagement
@ComponentScan({ "hk.org.ha.eclaim.*" })
public class AppConfig {

	@Value("${ws.cidlogin.defaulturl}")
	private String cidLoginDefaultUrl;

	// JNDI - START
	/*
	 * @Primary
	 * 
	 * @Bean public DataSource dataSource() throws javax.naming.NamingException {
	 * JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
	 * jndiObjectFactoryBean.setJndiName("jdbc/mprs-mprsapp-ds");
	 * jndiObjectFactoryBean.setResourceRef(true);
	 * jndiObjectFactoryBean.setLookupOnStartup(true);
	 * jndiObjectFactoryBean.setExpectedType(DataSource.class);
	 * jndiObjectFactoryBean.afterPropertiesSet(); return (DataSource)
	 * jndiObjectFactoryBean.getObject(); }
	 * 
	 * @Primary
	 * 
	 * @Bean public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	 * throws NamingException { LocalContainerEntityManagerFactoryBean factoryBean =
	 * new LocalContainerEntityManagerFactoryBean();
	 * factoryBean.setPackagesToScan(new String[] { "hk.org.ha.eclaim.bs.*.po",
	 * "hk.org.ha.eclaim.core.po" });
	 * factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
	 * factoryBean.setDataSource(dataSource());
	 * factoryBean.setPersistenceUnitName("persistenceUnitName"); return
	 * factoryBean; }
	 */

//	public DataSource cidsDataSource() throws javax.naming.NamingException {
//		JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
//		jndiObjectFactoryBean.setJndiName("jdbc/mprs-cid-ds");
//		jndiObjectFactoryBean.setResourceRef(true);
//        jndiObjectFactoryBean.setLookupOnStartup(true);
//        jndiObjectFactoryBean.setExpectedType(DataSource.class);
//        jndiObjectFactoryBean.afterPropertiesSet();
//        return (DataSource) jndiObjectFactoryBean.getObject();
//    }
//        
//	@Bean
//	@Qualifier(value="cidsEntityManagerFactory")
//	public LocalContainerEntityManagerFactoryBean cidsEntityManagerFactory() throws NamingException {
//		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
//		factoryBean.setDataSource(cidsDataSource());
//		factoryBean.setPersistenceUnitName("cids");
//		return factoryBean;
//	}

	// JNDI - END

	// For development only - Replace JNDI Datasource - START
	@Primary
	@Bean(destroyMethod = "")
	public DataSource dataSource() throws javax.naming.NamingException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521/xe");
		dataSource.setUsername("eclaim");
		dataSource.setPassword("abcd1234");
		return dataSource;
	}

	@Primary
	@Bean
	@Qualifier(value = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPackagesToScan(new String[] { "hk.org.ha.eclaim.bs.*.po", "hk.org.ha.eclaim.core.*" });
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.setDataSource(dataSource());
		factoryBean.setJpaProperties(jpaProperties());
		return factoryBean;
	}

	public DataSource cidsDataSource() throws javax.naming.NamingException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		dataSource.setUrl("jdbc:sqlserver://192.168.25.148:1443");
		dataSource.setUsername("cid_mprs_user");
		dataSource.setPassword("cid_mprs_password");
		return dataSource;
	}

//	@Bean
//	@Qualifier(value="cidsEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean cidsEntityManagerFactory() throws NamingException {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.setDataSource(cidsDataSource());
		factoryBean.setPersistenceUnitName("cids");
		return factoryBean;
	}

	private Properties jpaProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "false");
		properties.put("hibernate.proc.param_null_passing", "true");
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.order_inserts", "true");
		return properties;
	}
	// For development only - Replace JNDI Datasource - END

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(false);
		hibernateJpaVendorAdapter.setGenerateDdl(false);
		hibernateJpaVendorAdapter.setDatabase(Database.ORACLE);
		return hibernateJpaVendorAdapter;
	}

	@Primary
	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(emf);
		return txManager;
	}

	@Bean
	@Autowired
	public PlatformTransactionManager cidsTransactionManager(EntityManagerFactory emf) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(emf);
		return txManager;
	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("hk.org.ha.cid_login_ws.cidloginfunctions");
		return marshaller;
	}

	@Bean
	public WebServiceTemplate cidLoginWebServiceTemplate(Jaxb2Marshaller marshaller) {
		WebServiceTemplate client = new WebServiceTemplate();
		client.setDefaultUri(cidLoginDefaultUrl);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

	/*
	 * @Bean public static PropertySourcesPlaceholderConfigurer
	 * propertySourcesPlaceholderConfigurer() { return new
	 * PropertySourcesPlaceholderConfigurer(); }
	 */

}
