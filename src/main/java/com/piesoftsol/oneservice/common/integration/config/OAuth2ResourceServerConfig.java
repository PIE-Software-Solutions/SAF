package com.piesoftsol.oneservice.common.integration.config;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.ONESERVICE_DATA_SOURCE;
import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.SERVICE_NAME;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.piesoftsol.oneservice.common.integration.util.EnableOAuth2JdbcSecurityCondition;

@Conditional(value = {EnableOAuth2JdbcSecurityCondition.class})
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	@Qualifier(ONESERVICE_DATA_SOURCE)
	private DataSource oneserviceDataSource;

    //

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		    .and()
		    .authorizeRequests().anyRequest().permitAll();
	// @formatter:on		
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices()).resourceId(SERVICE_NAME);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    // JDBC token store configuration

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(oneserviceDataSource);
    }

}
