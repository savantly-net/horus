package net.savantly.horus.webapp.application.configuration;

import java.util.Collections;

import javax.servlet.Filter;

import org.apache.isis.applib.annotation.OrderPrecedence;
import org.apache.isis.core.config.IsisConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
	public static final int CORS_FILTER_ORDER = OrderPrecedence.EARLY - 100;
	
	private final IsisConfiguration isisConfiguration;
	
	public CorsConfig(IsisConfiguration isisConfiguration) {
		this.isisConfiguration = isisConfiguration;
	}
	
	@Bean
	@Order(OrderPrecedence.EARLY)
    public FilterRegistrationBean<Filter> corsFilterRegistration() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(corsFilter());
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        filterRegistrationBean.setOrder(CORS_FILTER_ORDER);
        return filterRegistrationBean;
    }
	
	public CorsFilter corsFilter() {
		return new CorsFilter(corsConfigurationSource());
	}
	
	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(isisConfiguration.getExtensions().getCors().getAllowedHeaders());
		configuration.setAllowedMethods(isisConfiguration.getExtensions().getCors().getAllowedMethods());
		configuration.setAllowedOrigins(isisConfiguration.getExtensions().getCors().getAllowedOrigins());
		configuration.setExposedHeaders(isisConfiguration.getExtensions().getCors().getExposedHeaders());
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
