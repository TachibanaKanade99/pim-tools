package vn.elca.training;

import org.h2.server.web.WebServlet;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import vn.elca.training.model.dao.EmployeeDao;
import vn.elca.training.model.dao.GroupTableDao;
import vn.elca.training.model.dao.ProjectDao;
import vn.elca.training.service.GroupTableService;
import vn.elca.training.service.ProjectService;
import vn.elca.training.util.ApplicationMapper;
import vn.elca.training.validator.EmployeeValidator;
import vn.elca.training.validator.ProjectValidator;
import vn.elca.training.controllers.AbstractApplicationController;

/**
 * @author gtn
 *
 */
@SpringBootApplication(scanBasePackages = "vn.elca.training")
@ComponentScan(basePackageClasses = {
        AbstractApplicationController.class,
        ApplicationMapper.class,
        // service layer
        ProjectService.class,
        GroupTableService.class,
        // validator layer
        ProjectValidator.class,
        EmployeeValidator.class,
        // dao layer
        ProjectDao.class,
        GroupTableDao.class,
        EmployeeDao.class
})
@PropertySource({"classpath:/application.properties", "classpath:/messages.properties"})
public class ApplicationWebConfig extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApplicationWebConfig.class);
    }

    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/h2console/*");
        return registrationBean;
    }
}
