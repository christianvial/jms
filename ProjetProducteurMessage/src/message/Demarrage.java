package message;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class Demarrage {

	/**
	 * @param args
	 * @throws JMSException 
	 */
	public static void main(String[] args) throws JMSException {
		// TODO Auto-generated method stub
		int nbm;
		System.out.println("Je démarre  ");
				
		try {
			nbm=ProducteurMessageJms.envoiMessage();
			System.out.println("Nombre de messages envoyés : " + nbm);
		}
		catch (JMSException e) {
			// TODO Auto-generated catch block
			System.out.println("erreur : " + e.getMessage());
		}
		catch (NamingException e) {
			// TODO Auto-generated catch block
			System.out.println("erreur : " + e.getMessage());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("erreur : " + e.getMessage());
		}
		
	}
}
