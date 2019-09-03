package be.civadis.store.base.config;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MySimpleUrlAuthenticationSuccessHandler
 */
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  
    private final Logger log = LoggerFactory.getLogger(MySimpleUrlAuthenticationSuccessHandler.class);
 
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private ApplicationProperties applicationProperties;

    public MySimpleUrlAuthenticationSuccessHandler(ApplicationProperties applicationProperties){
        this.applicationProperties = applicationProperties;
    }
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
      HttpServletResponse response, Authentication authentication)
      throws IOException {
  
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
 
    protected void handle(HttpServletRequest request, 
      HttpServletResponse response, Authentication authentication)
      throws IOException {
  
        String targetUrl = determineTargetUrl(request, response);
        // TODO : compléter si besoin headers, cookies, etc...
        if (response.isCommitted()) {
            log.debug(
              "Response has already been committed. Unable to redirect to "
              + targetUrl);
            return;
        }
 
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
 
    protected String determineTargetUrl(HttpServletRequest request,
			HttpServletResponse response) {
        return (request.getRequestURI().toLowerCase().trim().endsWith("_ext")) ? applicationProperties.getClientBaseUri() : "/";
	}
 
    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
 
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}