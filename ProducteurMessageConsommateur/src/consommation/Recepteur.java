package consommation;

import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

public class Recepteur {

	private static final Logger log = Logger.getLogger(Recepteur.class
			.getName());

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "java:/jms/queue/FileMessages";

	private static final String DEFAULT_USERNAME = "jmsuser";
	private static final String DEFAULT_PASSWORD = "jmsepul98!";

	public static void main(String[] args) throws Exception {

		// TODO Auto-generated method stub
		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		Session session = null;
		MessageConsumer consommateur = null;
		Destination destination = null;
		TextMessage textmessage = null;
		Context ctxt = null;
		Message message = null;

		try {
			// On charge le contexte pour une recherche dans l'annuaire JNDI

			ctxt = JBossContext.getInitialContext();
			// On construit l'environnemenent à partir
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
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			consommateur = session.createConsumer(destination);

			connection.start();
			System.out.println("Début de la réception des messages !");
			// bloque la réception pendant 1000 ms
			message = consommateur.receive(1000);
			while (message != null) {
				if (message instanceof TextMessage) {
					textmessage = (TextMessage) message;
					System.out.println("Message reçu : "
							+ textmessage.getText());
				}
				message = (TextMessage) consommateur.receive(1000);
			}
			connection.stop();
			connection.close();

		} catch (JMSException e) {
			log.severe(e.getMessage());
			System.out.println("erreur : " + e.getMessage());
		} catch (NamingException e) {
			log.severe(e.getMessage());
			System.out.println("erreur : " + e.getMessage());
		} catch (Exception e) {
			log.severe(e.getMessage());
			System.out.println("erreur : " + e.getMessage());
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
		}

	}

}
