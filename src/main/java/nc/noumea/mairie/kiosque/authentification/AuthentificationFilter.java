package nc.noumea.mairie.kiosque.authentification;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nc.noumea.mairie.kiosque.dto.LightUserDto;
import nc.noumea.mairie.kiosque.profil.dto.ProfilAgentDto;
import nc.noumea.mairie.ws.IRadiWSConsumer;
import nc.noumea.mairie.ws.ISirhWSConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

@Component
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AuthentificationFilter implements Filter {
	
    public static final String ACCES_CONNEXION  = "/connexion";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";

    private Logger logger = LoggerFactory.getLogger(AuthentificationFilter.class);
    
    private IRadiWSConsumer radiWSConsumer;
    
	private ISirhWSConsumer sirhWsConsumer;
    
    public void init( FilterConfig config ) throws ServletException {
    	ServletContext servletContext = config.getServletContext();
    	WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		
		AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
		
		radiWSConsumer = (IRadiWSConsumer) autowireCapableBeanFactory.getBean("radiWSConsumer");
		sirhWsConsumer = (ISirhWSConsumer) autowireCapableBeanFactory.getBean("sirhWsConsumer");
    }

    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
            ServletException {

        /* Cast des objets request et response */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
    	HttpSession hSess = ((HttpServletRequest)request).getSession();
    	
    	if(null != hSess.getAttribute("currentUser")) {
    		chain.doFilter( request, response );
            return;
    	}
    	
        if(null == request.getRemoteUser() || "".equals(request.getRemoteUser().trim())) {
        	logger.debug("RemoteUser is NULL");
    		hSess.invalidate();
    		request.logout();
        	response.sendError(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED, "You are logged out.");
    		return;
        }
        
        LightUserDto userDto = radiWSConsumer.getAgentCompteADByLogin(request.getRemoteUser());
        if(null == userDto) {
        	logger.debug("User not exist in Radi WS with RemoteUser : " + request.getRemoteUser());
    		request.logout();
    		return;
        }
        
        if(0 == userDto.getEmployeeNumber()) {
        	logger.debug("User not exist in Radi WS with RemoteUser : " + request.getRemoteUser());
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous n'êtes pas un agent de la mairie, vous n'êtes pas autorisé à accéder à cette application.");
    		return;
        }
        
        ProfilAgentDto profilAgent = sirhWsConsumer.getEtatCivil(userDto.getEmployeeNumber());
        
        if(null == profilAgent) {
        	logger.debug("ProfilAgent not exist in SIRH WS with EmployeeNumber : " + userDto.getEmployeeNumber());
    		request.logout();
    		return;
        }
        
        hSess.setAttribute("currentUser", profilAgent);
        
//        logger.info("----------------- REQUEST ----------------");
//        logger.info("REQUEST getUserPrincipal : " + request.getUserPrincipal());
//        logger.info("REQUEST getRemoteUser : " + request.getRemoteUser());
//        logger.info("REQUEST getRemoteHost : " + request.getRemoteHost());
//        logger.info("REQUEST getRemotePort : " + request.getRemotePort());
//        logger.info("REQUEST getRemoteAddr : " + request.getRemoteAddr());
//        logger.info("REQUEST getAuthType : " + request.getAuthType());
//        logger.info("REQUEST getProtocol : " + request.getProtocol());
//        logger.info("REQUEST getReader : " + request.getReader());
//        logger.info("----------------- REQUEST HEADER ----------------");
//        logger.info("REQUEST HEADER HTTP_X_PROXY_REMOTE_USER : " + request.getHeader("HTTP_X_PROXY_REMOTE_USER"));
//        logger.info("REQUEST HEADER REMOTE_USER : " + request.getHeader("REMOTE_USER"));
//        logger.info("REQUEST HEADER HTTP_REMOTE_USER : " + request.getHeader("HTTP_REMOTE_USER"));
//        logger.info("REQUEST HEADER REMOTE-USER : " + request.getHeader("REMOTE-USER"));
//        logger.info("REQUEST HEADER HTTP-REMOTE-USER : " + request.getHeader("HTTP-REMOTE-USER"));
//        logger.info("REQUEST HEADER CAS-User : " + request.getHeader("CAS-User"));
//        logger.info("REQUEST HEADER X-Forwarded-User : " + request.getHeader("X-Forwarded-User"));
//        logger.info("REQUEST HEADER REMOTE_HOST : " + request.getHeader("REMOTE_HOST"));
//        logger.info("REQUEST HEADER REMOTE_IDENT : " + request.getHeader("REMOTE_IDENT"));
//        logger.info("REQUEST HEADER REMOTE_ADDR : " + request.getHeader("REMOTE_ADDR"));
//        logger.info("REQUEST HEADER REMOTE_ADDR : " + request.getHeader("REMOTE_ADDR"));
//        logger.info("REQUEST HEADER authorization : " + request.getHeader("authorization"));
//        logger.info("----------------- REQUEST ATTRIBUT ----------------");
//        logger.info("REQUEST ATTRIBUT HTTP_X_PROXY_REMOTE_USER : " + request.getAttribute("HTTP_X_PROXY_REMOTE_USER"));
//        logger.info("REQUEST ATTRIBUT REMOTE_USER : " + request.getAttribute("REMOTE_USER"));
//        logger.info("REQUEST ATTRIBUT HTTP_REMOTE_USER : " + request.getAttribute("HTTP_REMOTE_USER"));
//        logger.info("REQUEST ATTRIBUT REMOTE-USER : " + request.getAttribute("REMOTE-USER"));
//        logger.info("REQUEST ATTRIBUT HTTP-REMOTE-USER : " + request.getAttribute("HTTP-REMOTE-USER"));
//        logger.info("REQUEST ATTRIBUT CAS-User : " + request.getAttribute("CAS-User"));
//        logger.info("REQUEST ATTRIBUT X-Forwarded-User : " + request.getAttribute("X-Forwarded-User"));
//        logger.info("REQUEST ATTRIBUT REMOTE_HOST : " + request.getHeader("REMOTE_HOST"));
//        logger.info("REQUEST ATTRIBUT REMOTE_IDENT : " + request.getHeader("REMOTE_IDENT"));
//        logger.info("REQUEST ATTRIBUT REMOTE_ADDR : " + request.getHeader("REMOTE_ADDR"));
//        logger.info("REQUEST ATTRIBUT user-agent : " + request.getHeader("user-agent"));
//        logger.info("REQUEST ATTRIBUT USER-AGENT : " + request.getHeader("USER-AGENT"));
//        logger.info("----------------- RESPONSE HEADER ----------------");
//        logger.info("RESPONSE HEADER HTTP_X_PROXY_REMOTE_USER : " + response.getHeader("HTTP_X_PROXY_REMOTE_USER"));
//        logger.info("RESPONSE HEADER REMOTE_USER : " + response.getHeader("REMOTE_USER"));
//        logger.info("RESPONSE HEADER HTTP_REMOTE_USER : " + response.getHeader("HTTP_REMOTE_USER"));
//        logger.info("RESPONSE HEADER REMOTE-USER : " + response.getHeader("REMOTE-USER"));
//        logger.info("RESPONSE HEADER HTTP-REMOTE-USER : " + response.getHeader("HTTP-REMOTE-USER"));
//        logger.info("RESPONSE HEADER CAS-User : " + response.getHeader("CAS-User"));
//        logger.info("RESPONSE HEADER X-Forwarded-User : " + response.getHeader("X-Forwarded-User"));

        chain.doFilter( request, response );
        return;
    }

    public void destroy() {
    }
}
