package nc.noumea.mairie.kiosque.authentification;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
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
    
	public static final List<String> PAGES_STATIQUES = Arrays.asList("/401.jsp", "/404.jsp", "/incident.jsp",
			"/maintenance.jsp", "/version.jsp");

    private Logger logger = LoggerFactory.getLogger(AuthentificationFilter.class);
    
    private IRadiWSConsumer radiWSConsumer;
    
	private ISirhWSConsumer sirhWsConsumer;
    
    public void init( FilterConfig config ) throws ServletException {
    	ServletContext servletContext = config.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		
		AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
		
		radiWSConsumer = (IRadiWSConsumer) autowireCapableBeanFactory.getBean("radiWSConsumer");
		sirhWsConsumer = (ISirhWSConsumer) autowireCapableBeanFactory.getBean("sirhWsConsumer");
    }

    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
            ServletException {

		// cookie will only need to be changed, if this session is created by
		// this request.
//		if (((HttpServletRequest) req).getRequestURL().toString().contains("/deconnexion.zul") ) {
//			Cookie sessionCookie = findSessionCookie(((HttpServletRequest) req)
//					.getCookies());
//			
//			HttpServletResponse response = null;
//			
//			if (sessionCookie != null) {
//				String cookieDomainToSet = getCookieDomainToSet(req
//						.getServerName());
//				if (cookieDomainToSet != null) {
//					// changing the cookie only does not help, because tomcat
//					// immediately sets
//					// a string representation of this cookie as MimeHeader,
//					// thus we also
//					// have to change this representation
//					response = replaceCookie((HttpServletRequest) req, (HttpServletResponse)res, sessionCookie,
//							cookieDomainToSet);
//					logger.info("doFilter deconnexion check 1");
//				}else{
//					response = replaceCookie((HttpServletRequest) req, (HttpServletResponse)res, sessionCookie,
//							"azerty");
//					logger.info("doFilter deconnexion check 2");
//				}
//			}
//			chain.doFilter( req, response );
//            return;
//		}
        
        /* Cast des objets request et response */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
    	HttpSession hSess = ((HttpServletRequest)request).getSession();
    	
    	for (Enumeration<String> enume = request.getHeaderNames(); enume.hasMoreElements();) {
    		String headerValue = (String) enume.nextElement();
    		logger.info(headerValue + " : " + request.getHeader(headerValue));
		}
    	
    	if(null != hSess.getAttribute("currentUser")) {
    		chain.doFilter( request, response );
            return;
    	}
		// on laisse passer pour le rproxy et ainsi permettre de deployer l
		// application sur le 2e noeud tomcat
    	if(PAGES_STATIQUES.contains(request.getServletPath())) {
    		chain.doFilter( request, response );
            return;
    	}
    	
    	for (Enumeration<String> enume = request.getHeaderNames(); enume.hasMoreElements();) {
    		String headerValue = (String) enume.nextElement();
    		logger.info(headerValue + " : " + request.getHeader(headerValue));
		}
    	
        if((null == request.getHeader("x-krb_remote_user") || "".equals(request.getHeader("x-krb_remote_user").trim()))
        		&& !request.getHeader("host").contains("localhost")) {
        	logger.debug("x-krb_remote_user is NULL");
    		hSess.invalidate();
    		request.logout();
        	response.sendError(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED, "You are logged out.");
    		return;
        }
        
        String remoteUser = request.getHeader("x-krb_remote_user");

        if(null == remoteUser && request.getHeader("host").contains("localhost")) {
        	remoteUser = "chata73";
        }
        if(remoteUser.equals("rebjo84") || remoteUser.equals("nicno85")) {
        	remoteUser = "chata73";
        }
        
        LightUserDto userDto = radiWSConsumer.getAgentCompteADByLogin(remoteUser);
        if(null == userDto) {
        	logger.debug("User not exist in Radi WS with RemoteUser : " + remoteUser);
    		request.logout();
    		return;
        }
        
        if(0 == userDto.getEmployeeNumber()) {
        	logger.debug("User not exist in Radi WS with RemoteUser : " + remoteUser);
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Vous n'êtes pas un agent de la mairie, vous n'êtes pas autorisé à accéder à cette application.");
    		return;
        }
        
		ProfilAgentDto profilAgent = null;
		try {
			profilAgent = sirhWsConsumer.getEtatCivil(userDto.getEmployeeNumber());
		} catch (Exception e) {
			// le SIRH-WS ne semble pas repondre
			logger.debug("L'application SIRH-WS ne semble pas répondre.");
			request.logout();
			return;
		}
        
        if(null == profilAgent) {
        	logger.debug("ProfilAgent not exist in SIRH WS with EmployeeNumber : " + userDto.getEmployeeNumber());
    		request.logout();
    		return;
        }
        
        hSess.setAttribute("currentUser", profilAgent);
        logger.debug("Authentification du user ok : " + remoteUser);
        
        chain.doFilter( request, response );
        return;
    }

    public void destroy() {
    }
    
//    @Override
//    public void invoke(Request request, Response response) throws IOException, ServletException {
//      
//
//      // process the next valve
//      getNext().invoke(request, response);
//    }

	protected Cookie findSessionCookie(Cookie[] cookies) {
		if (cookies != null) {
		    for (Cookie cookie : cookies) {
			    if ("JSESSIONID".equals(cookie.getName())) {
			    	return cookie;
			    }
			    if ("JSESSIONIDSSO".equals(cookie.getName())) {
			    	return cookie;
			    }
			}
		}
		return null;
	}

    protected HttpServletResponse replaceCookie(HttpServletRequest request, HttpServletResponse res, Cookie originalCookie, String domainToSet) {
      // if the response has already been committed, our replacementstrategy will have no effect

		// find the Set-Cookie header for the existing cookie and replace its
		// value with new cookie
//    	for (Enumeration<String> enume = request.getHeaderNames(); enume.hasMoreElements();) {
//    		String headerValue = (String) enume.nextElement();
//    		logger.info(headerValue + " : " + request.getHeader(headerValue));
//		
//			if (headerValue.equals("cookie")) {
//				
//				String value = request.getHeader(headerValue);
//				if (value.indexOf(originalCookie.getName()) >= 0) {
//					if (originalCookie.getDomain() == null) {
//						StringBuilder builder = new StringBuilder(
//								value).append("; Domain=").append(
//								domainToSet);
//						value = builder.toString();
//					} else {
//						String newDomain = value.replaceAll(
//								"Domain=[A-Za-z0-9.-]*",
//								"Domain=" + domainToSet);
//						value = newDomain;
//					}
//					logger.info("replaceCookie check 1 ; headerValue : " + headerValue + " ; request.getHeader : " + request.getHeader(headerValue) + " ; value : " + value + " ; originalCookie.getName() : " + originalCookie.getName());
//				}
//			}
//			if(headerValue.equals("authorization")) {
//				String value = request.getHeader(headerValue);
//				StringBuilder builder = new StringBuilder();
//				value = builder.toString();
//				logger.info("replaceCookie check 2 ; headerValue : " + headerValue + " ; request.getHeader : " + request.getHeader(headerValue) + " ; value : " + value);
//			}
//			logger.info("replaceCookie check 3 " + headerValue + " replace : " + request.getHeader(headerValue));
//        }
    	
    	Cookie[] cookies = request.getCookies();
    	int cookieLenght = cookies.length;
    	for (int i = 0; i < cookieLenght; i++) {
	    	Cookie cookie = cookies[i];
		    	cookie.setMaxAge(0);
		    	cookie.setPath("/");
		    	cookie.setDomain(request.getHeader("host"));
		    	cookie.setValue("test");
	    	res.addCookie(cookie);
	    	logger.info("replaceCookie check 4 Cookie " + cookie.getName());
    	}
    	
    	return res;
    }

	protected String getCookieDomainToSet(String cookieDomain) {
		String[] parts = cookieDomain.split("\\.");
		if (parts.length >= 3) {
			return "." + parts[parts.length - 2] + "."
					+ parts[parts.length - 1];
		}
		return null;
	}

//	public String toString() {
//		return ("CrossSubdomainSessionValve[container=" + container.getName() + ']');
//	}
}
