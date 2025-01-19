package wad.seoul_nolgoat.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wad.seoul_nolgoat.filter.MDCFilter;
import wad.seoul_nolgoat.filter.RequestLoggingFilter;

@Configuration
public class LogFilterConfig {

    @Bean
    public FilterRegistrationBean<MDCFilter> mdcFilter() {
        FilterRegistrationBean<MDCFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new MDCFilter());
        filterRegistrationBean.setOrder(1);

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new RequestLoggingFilter());
        filterRegistrationBean.setOrder(2);

        return filterRegistrationBean;
    }
}
