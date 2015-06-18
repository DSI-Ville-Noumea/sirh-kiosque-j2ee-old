package nc.noumea.mairie.kiosque.authentification;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class AuthentificationFilterTest {

	@Test
	public void convertRemoteUser_withAt() {
		
		AuthentificationFilter filter = new AuthentificationFilter();
		String result = filter.convertRemoteUser("rebjo84@SITE-MAIRIE.NOUMEA.NC");
		
		assertEquals("rebjo84", result);
	}

	@Test
	public void convertRemoteUser_withoutAt() {
		
		AuthentificationFilter filter = new AuthentificationFilter();
		String result = filter.convertRemoteUser("rebjo84");
		
		assertEquals("rebjo84", result);
	}

	@Test
	public void convertRemoteUser_casTordu() {
		
		AuthentificationFilter filter = new AuthentificationFilter();
		String result = filter.convertRemoteUser("rebjo84@site@SITE-MAIRIE.NOUMEA.NC");
		
		assertEquals("rebjo84", result);
	}
}
