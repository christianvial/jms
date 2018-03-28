package consommation;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JBossContext {

	//private static final String DEFAULT_USERNAME = "jmsuser";
	//private static final String DEFAULT_PASSWORD = "jmsepul98!";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "http-remoting://localhost:8080";

	public static Context getInitialContext() throws NamingException {
		Properties prop = new Properties();
		
		prop.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		prop.put(Context.PROVIDER_URL,
				System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
		
		/* prop.put(Context.SECURITY_PRINCIPAL,
				System.getProperty("username", DEFAULT_USERNAME));
		 prop.put(Context.SECURITY_CREDENTIALS,
				System.getProperty("password", DEFAULT_PASSWORD));*/
		
		return new InitialContext(prop);
 
	}

}
