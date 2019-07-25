package be.civadis.store.base.multitenancy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import be.civadis.store.base.security.oauth2.AuthorizationHeaderUtil;

import java.io.IOException;

/**
 * Filtre permettant d'extraire le tenant courant du token d'authentication
 */
public class GatewayTenantFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Attention, dans le cas du gateway, on doit gérer plusieurs cas :
        // traitement avec tenant dans le token
        // traitement avec session active dans spring secu
        // login avec tenant en param (pas de token)

         try {

            String token = null;
            String tenant = null;
            
            // extraire le token (si présent) puis le tenant du token
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ") && authHeader.length() > 7){
                token = authHeader.substring(7);
            }
            if (token != null && !token.isEmpty()){
               tenant = TokenDecoder.getInstance().getTenant(token);
            }

            if (tenant == null || tenant.isEmpty()){
                //recherche du tenant dans la secu spring
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                AbstractAuthenticationToken authToken = (AbstractAuthenticationToken) authentication;
                if (authToken instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authToken;
                    tenant = oauthToken.getAuthorizedClientRegistrationId();
                    
                } else if (authToken instanceof JwtAuthenticationToken) {
                    token = ((JwtAuthenticationToken) authToken).getToken().getTokenValue();
                    tenant = TokenDecoder.getInstance().getTenant(token);
                }
            }

            // si pas dans le token, voir si présent dans la request
            if (tenant == null || tenant.isEmpty()){
                String[] realms = request.getParameterValues("realm");
                if (realms != null && realms.length > 0){
                    tenant = realms[0];
                } else {
                    // cas de oauth2/authorization/{realm}
                    String uri = httpRequest.getRequestURI();
                    String searchPart = "oauth2/authorization/";
                    if (uri.contains(searchPart)){
                        String endPart = uri.substring(uri.indexOf(searchPart) + searchPart.length());
                        if (endPart != null && !endPart.isEmpty()){
                            tenant = endPart;
                        }
                    }
                }
            }

            // set du tenant dans la context
            if (tenant != null && !tenant.isEmpty()){
                TenantContext.setCurrentTenant(tenant);
            } else {
                TenantContext.clear();
            }
             
            chain.doFilter(request, response);
 
         } finally {
             TenantContext.clear();
         }

    }

}
