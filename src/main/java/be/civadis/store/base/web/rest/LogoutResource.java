package be.civadis.store.base.web.rest;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import be.civadis.store.base.multitenancy.TokenDecoder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
* REST controller for managing global OIDC logout.
*/
@RestController
public class LogoutResource {
    private ApplicationContext applicationContext;

    public LogoutResource(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * {@code POST  /api/logout} : logout the current user.
     *
     * @param request the {@link HttpServletRequest}.
     * @param idToken the ID token.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and a body with a global logout URL and ID token.
     */
    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    @AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken) {

       
        ClientRegistrationRepository registrations = this.applicationContext.getBean(ClientRegistrationRepository.class);
        String tenant = TokenDecoder.getInstance().getTenant(idToken.getTokenValue());
       
        ClientRegistration registration = registrations.findByRegistrationId((tenant != null) ? tenant : "oidc");
        
        String logoutUrl = registration.getProviderDetails()
            .getConfigurationMetadata().get("end_session_endpoint").toString();

        Map<String, String> logoutDetails = new HashMap<>();
        logoutDetails.put("logoutUrl", logoutUrl);
        logoutDetails.put("idToken", idToken.getTokenValue());
        request.getSession().invalidate();
        return ResponseEntity.ok().body(logoutDetails);
    }
}
