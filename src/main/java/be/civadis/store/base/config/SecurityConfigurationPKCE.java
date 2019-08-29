package be.civadis.store.base.config;

import be.civadis.store.base.multitenancy.GatewayTenantFilter;
import be.civadis.store.base.multitenancy.TenantContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import java.util.Optional;
import be.civadis.store.base.security.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import be.civadis.store.base.security.oauth2.AudienceValidator;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import be.civadis.store.base.security.oauth2.AuthorizationHeaderFilter;
import be.civadis.store.base.security.oauth2.AuthorizationHeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Order(101)
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@Import(SecurityProblemSupport.class)
public class SecurityConfigurationPKCE extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfigurationPKCE(CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/pkce/**").oauth2Login().disable();
    }

}
