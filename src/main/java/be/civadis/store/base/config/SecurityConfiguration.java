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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final ApplicationProperties applicationProperties;
    
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(ApplicationProperties applicationProperties, CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        this.applicationProperties = applicationProperties;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
            .addFilterBefore(corsFilter, CsrfFilter.class)
            .exceptionHandling()
            .accessDeniedHandler(problemSupport)
        .and()
            .headers()
            .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; img-src 'self' data:")
        .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
        .and()
            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
        .and()
            .frameOptions()
            .deny()
        .and()
            .authorizeRequests()
            .antMatchers("/api/auth-info").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .oauth2Login().successHandler(myAuthenticationSuccessHandler(applicationProperties))
        .and()
            .addFilterAfter(new GatewayTenantFilter(), CorsFilter.class)
            .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(mapGroupOrRolesClaimToGrantedAuthorities());
        // @formatter:on
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(ApplicationProperties applicationProperties){
        //SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        //handler.setTargetUrlParameter("spring-security-redirect");
        return new MySimpleUrlAuthenticationSuccessHandler(applicationProperties);
    }

    /**
     * Map authorities from "groups" or "roles" claim in ID Token.
     *
     * @return a {@link GrantedAuthoritiesMapper} that maps groups from
     * the IdP to Spring Security Authorities.
     */
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
                mappedAuthorities.addAll(mapRolesToGrantedAuthorities(
                    getRolesFromClaims(oidcUserAuthority.getUserInfo().getClaims())));
            });

            return mappedAuthorities;
        };
    }

    private Converter<Jwt, AbstractAuthenticationToken> mapGroupOrRolesClaimToGrantedAuthorities() {
        return new JwtAuthenticationConverter() {
            @Override
            protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
                return mapRolesToGrantedAuthorities(
                    getRolesFromClaims(jwt.getClaims())
                );
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Collection<String> getRolesFromClaims(Map<String, Object> claims) {
        return (Collection<String>) claims.getOrDefault("groups",
            claims.getOrDefault("roles", new ArrayList<>()));
    }

    private List<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<String> roles) {
        return roles.stream()
            .filter(role -> role.startsWith("ROLE_"))
            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
JwtDecoder jwtDecoder(ClientRegistrationRepository registrations) {
    ClientRegistration registration = registrations.findByRegistrationId(Optional.ofNullable(TenantContext.getCurrentTenant()).orElse(null));
    String issuerUri = (String) registration.getProviderDetails().getConfigurationMetadata().get("issuer");
        NimbusJwtDecoderJwkSupport jwtDecoder = (NimbusJwtDecoderJwkSupport)
            JwtDecoders.fromOidcIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator();
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    @Bean
    public AuthorizationHeaderFilter authHeaderFilter(AuthorizationHeaderUtil headerUtil) {
        return new AuthorizationHeaderFilter(headerUtil);
    }
}
