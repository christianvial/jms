package message;

import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

public class ProducteurMessageJms {
	private static final Logger log = Logger
			.getLogger(ProducteurMessageJms.class.getName());

	// Set up all the default values

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "java:jms/queue/FileMessages";

	private static final String DEFAULT_USERNAME = "jmsuser";
	private static final String DEFAULT_PASSWORD = "jmsepul98!";

	@SuppressWarnings("finally")
	public static int envoiMessage() throws JMSException, NamingException {

		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		Session session = null;
		MessageProducer producteur = null;
		Destination destination = null;
		TextMessage message = null;
		Context ctxt = null;
		int nbm = 0;
		try {
			// On charge le contexte pour une recherche dans l'annuaire JNDI

			ctxt = JBossContext.getInitialContext();
			// On construit l'environnemenent � partir
			// des recherches JNDI
			String connectionFactoryString = System.getProperty(
					"connection.factory", DEFAULT_CONNECTION_FACTORY);
			log.info("Attempting to acquire connection factory \""
					+ connectionFactoryString + "\"");
			connectionFactory = (ConnectionFactory) ctxt
					.lookup(connectionFactoryString);
			log.info("Found connection factory \"" + connectionFactoryString
					+ "\" in JNDI");
			
			// Destination
			String destinationString = System.getProperty("destination",
					DEFAULT_DESTINATION);
			log.info("Attempting to acquire destination \"" + destinationString
					+ "\"");
			destination = (Destination) ctxt.lookup(destinationString);
			log.info("Found destination \"" + destinationString + "\" in JNDI");

			// On crée la connexion JMS , session, producteur et message;
			connection = connectionFactory.createConnection(
					System.getProperty("username", DEFAULT_USERNAME),
					System.getProperty("password", DEFAULT_PASSWORD));
			System.out.println("Session ouverte ! ");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			producteur = session.createProducer(destination);
			message = session.createTextMessage();
			connection.start();
			// Send the specified number of messages
			String monTexte = "";
			System.out.println("Saisir un message (Vide pour arrêter) : ");
			monTexte = Clavier.lectureChaine();
			while (monTexte.compareTo("") > 0) {
				message.setText(monTexte);
				producteur.send(message);
				System.out.println("Saisir un message (Vide pour arrêter) : ");
				monTexte = Clavier.lectureChaine();
				nbm++;
			}
		} catch (JMSException e) {
			log.severe(e.getMessage());
			throw e;
		} catch (NamingException e) {
			log.severe(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.severe(e.getMessage());
			throw e;
		}

		finally {
			if (ctxt != null) {
				ctxt.close();
			}

			// closing the connection takes care of the session, producer, and
			// consumer
			if (connection != null) {
				connection.close();
			}
			return nbm;
		}
	}
}