package com.piesoftsol.oneservice.common.integration.config;

import java.util.Map;

import org.springframework.context.annotation.Conditional;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import com.piesoftsol.oneservice.common.integration.util.EnableOAuth2JdbcSecurityCondition;

@Conditional(value = {EnableOAuth2JdbcSecurityCondition.class})
@Component
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
        OAuth2Authentication authentication = super.extractAuthentication(claims);
        authentication.setDetails(claims);
        return authentication;
    }

}
