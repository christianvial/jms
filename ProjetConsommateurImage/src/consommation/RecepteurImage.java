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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.jms.BytesMessage;



public class RecepteurImage {

	private static final Logger log = Logger.getLogger(RecepteurImage.class
			.getName());

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "java:jms/queue/demoQueue";
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
		Runtime process = Runtime.getRuntime();

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
			
			System.out.println("connexion");
            BytesMessage bm = (BytesMessage)consommateur.receive(1000);
            File file = new File("D:\\images\\image.png");
            FileOutputStream fos = new FileOutputStream(file);
            System.out.println("fichier créé");
            BufferedOutputStream outBuf = new BufferedOutputStream(fos);
            int i;
            while((i=bm.readInt())!=-1){
               outBuf.write(i);
            }
            outBuf.close();
            fos.close();           
            connection.stop();
            connection.close();
            System.out.println("L'image a bien été reçue, il est possible de la voir");
            // pour Linux on utilise eog pour visualiser une image
            //process.exec("eog /home/uburoi/test.PNG" );
            // Pour XP on peut utiliser PictureViewer de Quicktime
            process.exec("C:\\Windows\\System32\\mspaint D:\\images\\image.png") ;
            } catch (JMSException e) {
            System.out.println("Exception occurred: " + e.toString());

			connection.stop();
			connection.close();

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
