package com.piesoftsol.oneservice.common.integration.config;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.ALLOWED_SERVICE_PATHS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.piesoftsol.oneservice.common.integration.util.AppLogger;
import com.piesoftsol.oneservice.common.integration.util.NoSecurityCondition;

/**
 * Common class to configure the authentication security for the users accessing
 * the service
 * 
 * <!-- This Class DOES NOT require any modification.-->
 * 
 * @author Kiran
 */
@Conditional(NoSecurityCondition.class)
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@Configuration
@PropertySource("file:${oneservice.home}/${oneservice.prop}.properties")
public class CommonNoSecurityConfig extends WebSecurityConfigurerAdapter {

	private final AppLogger LOGGER = new AppLogger(CommonNoSecurityConfig.class.getName());
	/**
	 * Spring Actuator paths
	 */
	private static final String[] SPRING_ACTUATOR_PATHS = new String[] { 	"/actuator/health", 
																			"/actuator/metrics", 
																			"/actuator/monitoring",
																			"/swagger-ui.html", "/webjars/**",
																			"/swagger-resources/**", 
																			"/v2/api-docs"};
	
	@Value("${oneservice.adminuser.name:#{null}}")
	private String adminUserName;
	
	@Value("${oneservice.adminuser.password:#{null}}")
	private String adminPassWord;
	
	/**
	 * To configure authentication based on user level access
	 * 
	 * @param authBuilder parm 1
	 * @throws Exception throws exception
	 */
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {
		if(null != adminUserName && !adminUserName.isEmpty() && null != adminPassWord && !adminPassWord.isEmpty()) {
			authBuilder
	        .inMemoryAuthentication()
	            .withUser(adminUserName).password(passwordEncoder().encode(adminPassWord)).roles("ADMIN");
		}
	}

	@Override
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		final String METHOD_NAME = "configure";
		LOGGER.info(METHOD_NAME, "Checking Security : ");
		httpSecurity
			.authorizeRequests().antMatchers(SPRING_ACTUATOR_PATHS).permitAll().and()
			.authorizeRequests().antMatchers(ALLOWED_SERVICE_PATHS).permitAll().and()
			.authorizeRequests().antMatchers("/restart").authenticated().anyRequest().access("hasRole('ROLE_ADMIN')").and()
			.authorizeRequests().antMatchers("/shutdownContext").authenticated().anyRequest().access("hasRole('ROLE_ADMIN')");
		httpSecurity.csrf().disable();
		httpSecurity.httpBasic();
	}
	
	
	/**
	 * Method to get the password bcrypt encoder
	 * 
	 * @return PasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
}
