package com.piesoftsol.oneservice.common.integration.config;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Conditional;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.piesoftsol.oneservice.common.integration.util.EnableOAuth2JdbcServerCondition;

@Conditional(value = {EnableOAuth2JdbcServerCondition.class})
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("organization", authentication.getName() + randomAlphabetic(4));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
