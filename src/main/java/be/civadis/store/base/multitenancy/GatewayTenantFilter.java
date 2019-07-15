package be.civadis.store.base.multitenancy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * Filtre permettant d'extraire le tenant courant du token d'authentication
 */
public class GatewayTenantFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Attention, dans le cas du gateway, on doit gérer deux cas:
        // traitement avec tenant dans le token
        // login avec tenant en param (pas de token)

         try {

            // extraire le token
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String authHeader = httpRequest.getHeader("Authorization");
            String token = null;
            if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ") && authHeader.length() > 7){
                token = authHeader.substring(7);
            }

            // extraire le tenant du token
            String tenant = null;
            if (token != null && !token.isEmpty()){
               tenant = TokenDecoder.getInstance().getTenant(token);
            }

            // si pas dans le token, voir si param de la request
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
