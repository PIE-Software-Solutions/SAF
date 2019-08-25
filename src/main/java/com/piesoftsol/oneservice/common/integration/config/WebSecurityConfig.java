package com.piesoftsol.oneservice.common.integration.config;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.ONESERVICE_DATA_SOURCE;
import static com.piesoftsol.oneservice.common.integration.util.SQLQueryConstants.GET_OAUTH_USERS;
import static com.piesoftsol.oneservice.common.integration.util.SQLQueryConstants.GET_OAUTH_USER_ROLES;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.piesoftsol.oneservice.common.integration.util.EnableOAuth2JdbcServerCondition;

@Conditional(value = {EnableOAuth2JdbcServerCondition.class})
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
	@Qualifier(ONESERVICE_DATA_SOURCE)
	private DataSource oneserviceDataSource;

    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
	auth.jdbcAuthentication().dataSource(oneserviceDataSource).passwordEncoder(passwordEncoder)
	.usersByUsernameQuery(GET_OAUTH_USERS).authoritiesByUsernameQuery(GET_OAUTH_USER_ROLES);
	/*.inMemoryAuthentication()
	  .withUser("john").password(passwordEncoder.encode("123")).roles("USER").and()
	  .withUser("tom").password(passwordEncoder.encode("111")).roles("ADMIN").and()
	  .withUser("user1").password(passwordEncoder.encode("pass")).roles("USER").and()
	  .withUser("admin").password(passwordEncoder.encode("nimda")).roles("ADMIN");*/
    }// @formatter:on

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
		http.authorizeRequests().antMatchers("/login").permitAll()
		.antMatchers("/oauth/token/revokeById/**").permitAll()
		.antMatchers("/tokens/**").permitAll()
		.anyRequest().authenticated()
		.and().formLogin().permitAll()
		.and().csrf().disable();
		// @formatter:on
    }

}
