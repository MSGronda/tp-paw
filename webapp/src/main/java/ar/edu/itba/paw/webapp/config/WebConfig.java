package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.security.filters.LocaleCaptureFilter;
import ar.edu.itba.paw.webapp.form.converters.StringToLocalTimeConverter;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@ComponentScan({
        "ar.edu.itba.paw.webapp.controller",
        "ar.edu.itba.paw.services",
        "ar.edu.itba.paw.persistence",
})
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:application-${spring.profiles.active}.properties")
})
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final long MAX_UPLOAD_SIZE = 1024 * 1024 * 100L;
    private static final int MESSAGE_SOURCE_CACHE_SECONDS = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();

        LOGGER.info("Setting up the JDBC DataSource...");
        ds.setDriverClass(org.postgresql.Driver.class);
        ds.setUrl(environment.getRequiredProperty("db.url"));
        ds.setUsername(environment.getRequiredProperty("db.username"));
        ds.setPassword(environment.getRequiredProperty("db.password"));

        return ds;
    }

    @Bean
    public Flyway flyway() {
        final Flyway f = Flyway.configure()
                .dataSource(dataSource())
                .load();

        f.repair();
        f.migrate();

        return f;
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();

        ms.setCacheSeconds((int) TimeUnit.MINUTES.toSeconds(MESSAGE_SOURCE_CACHE_SECONDS));
        ms.setBasename("classpath:i18n/messages");
        ms.setDefaultEncoding(StandardCharsets.UTF_8.name());

        return ms;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver mutipartResolver() {
        final CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding(StandardCharsets.UTF_8.name());
        resolver.setMaxUploadSize(MAX_UPLOAD_SIZE); //50mb
        return resolver;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new
                LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ar.edu.itba.paw.models");
        factoryBean.setDataSource(dataSource());
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        final Properties properties = new Properties();

        properties.setProperty("hibernate.dialect",
                "org.hibernate.dialect.PostgreSQL92Dialect");


        boolean show_sql = environment.getRequiredProperty("hibernate.show_sql").equals("true");
        properties.setProperty("hibernate.show_sql", show_sql ? "true" : "false");
        properties.setProperty("format_sql", show_sql ? "true" : "false");

        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean
    public FormattingConversionService mvcConversionService() {
        final DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new StringToLocalTimeConverter());
        return conversionService;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocalTimeConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        
        registry.addResourceHandler("/assets/**")
            .addResourceLocations("/assets/");
        
        registry.addResourceHandler("/static/**")
            .addResourceLocations("/static/");
        
        registry.addResourceHandler("/favicon.ico")
            .addResourceLocations("/static/favicon.ico");
    }
}
