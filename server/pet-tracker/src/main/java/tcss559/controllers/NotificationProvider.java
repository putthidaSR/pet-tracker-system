/**********************************************************************
 * TCSS559 - Quiz 3
 * Autumn 2021
 * Date: 11/19/2021
 * 
 * Resource class that will host the URI path "/notification"
 * 
 * @author Putthida Samrith
 **********************************************************************/

package tcss559.controllers;

import javax.ws.rs.Path;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Path("/notification")
public class NotificationProvider {
	
	// Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "ACa885cc956c0f0c4708626b82e1cbc440";
	public static final String AUTH_TOKEN = "6c2b6a1c903e0501e150460528c75e32";
	public static final String PHONE_NUMBER_FROM = "+15039173764"; // default sender phone number (free from Twilio)

	/**
	 * Helper method to send text message to the specified phone number
	 * @param destinationPhoneNumber - The phone number of the receiver
	 * @param textMessage-  The text of the message you want to send. Can be up to 1,600 characters in length.
	 * @throws Exception when message cannot be sent for any reason
	 */
	public static void sendSMS(String destinationPhoneNumber, String textMessage) throws Exception {

		try {

			// Initiate Twilio service
			Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

			// Create a MessageCreator to execute sending message action
			Message message = Message.creator(
					new com.twilio.type.PhoneNumber(destinationPhoneNumber),
					new com.twilio.type.PhoneNumber(PHONE_NUMBER_FROM), textMessage).create();
			
			// If no error is thrown, at this point, message is successfully sent
			System.out.println(message.getSid());
			System.out.println("Message is successfully sent");

		} catch (Exception e) {
			// Return expected error message
			throw new Exception(
					"Failed to send SMS to the specified number. Error Message: " + e.getLocalizedMessage());
		}
	}
	
}
