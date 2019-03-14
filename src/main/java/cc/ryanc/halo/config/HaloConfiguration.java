package cc.ryanc.halo.config;

import cc.ryanc.halo.config.properties.HaloProperties;
import cc.ryanc.halo.filter.CorsFilter;
import cc.ryanc.halo.filter.LogFilter;
import cc.ryanc.halo.security.filter.AdminAuthenticationFilter;
import cc.ryanc.halo.security.filter.ApiAuthenticationFilter;
import cc.ryanc.halo.security.handler.AdminAuthenticationFailureHandler;
import cc.ryanc.halo.security.handler.DefaultAuthenticationFailureHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Halo configuration.
 *
 * @author johnniang
 */
@Configuration
@EnableConfigurationProperties(HaloProperties.class)
public class HaloConfiguration {

    /**
     * Creates a CorsFilter.
     *
     * @return Cors filter registration bean
     */
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsFilter = new FilterRegistrationBean<>();

        corsFilter.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        corsFilter.setFilter(new CorsFilter());
        corsFilter.addUrlPatterns("/api/*");

        return corsFilter;
    }

    /**
     * Creates a LogFilter.
     *
     * @return Log filter registration bean
     */
    @Bean
    FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> logFilter = new FilterRegistrationBean<>();

        logFilter.setOrder(Ordered.HIGHEST_PRECEDENCE + 9);
        logFilter.setFilter(new LogFilter());
        logFilter.addUrlPatterns("/api/*", "/admin/*");

        return logFilter;
    }

    @Bean
    public FilterRegistrationBean<ApiAuthenticationFilter> apiAuthenticationFilter(HaloProperties haloProperties, ObjectMapper objectMapper) {
        ApiAuthenticationFilter apiFilter = new ApiAuthenticationFilter();
        // Set failure handler
        apiFilter.setFailureHandler(new DefaultAuthenticationFailureHandler(haloProperties.getProductionEnv(), objectMapper));

        FilterRegistrationBean<ApiAuthenticationFilter> authenticationFilter = new FilterRegistrationBean<>();
        authenticationFilter.setFilter(apiFilter);
        authenticationFilter.addUrlPatterns("/api/*");
        authenticationFilter.setOrder(0);
        return authenticationFilter;
    }

    @Bean
    public FilterRegistrationBean<AdminAuthenticationFilter> adminAuthenticationFilter(HaloProperties haloProperties, ObjectMapper objectMapper) {
        AdminAuthenticationFilter adminFilter = new AdminAuthenticationFilter();
        // Set failure handler
        adminFilter.setFailureHandler(new AdminAuthenticationFailureHandler(haloProperties.getProductionEnv(), objectMapper));

        FilterRegistrationBean<AdminAuthenticationFilter> authenticationFilter = new FilterRegistrationBean<>();
        authenticationFilter.setFilter(adminFilter);
        authenticationFilter.addUrlPatterns("/admin/*");
        authenticationFilter.setOrder(1);
        return authenticationFilter;
    }
}