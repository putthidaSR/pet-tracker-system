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

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tcss559.model.EmailDTO;
import tcss559.model.TextMessageDTO;
import tcss559.utilities.EmailService;
import tcss559.utilities.TextMessageService;

@Path("/notification")
public class NotificationProvider {
	
	/**
	 * @api {POST} /notification/sms Send text message service
	 * @apiName sendTextMessage
	 * @apiGroup NotificationProvider
	 *
	 * @apiParam {String} recipient Email recipient.
	 * @apiParam {String} subject Email subject.
	 * @apiParam {String} content Email content.
	 *
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 * @apiError(Error 503) ServiceError The intern service cause a error.
	 */
	@Path("/sms")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendTextMessage(TextMessageDTO sms) {
		try {
			
			// invoke Twilio service to send text message
			TextMessageService.sendSMS(sms.getDestinationPhoneNumber(), sms.getTextMessage());
	        return Response.status(Response.Status.OK).entity("").build();

		} catch (Exception e) {
			// Return expected error message
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Fail to send text message").build();
		}
	}
	
	/**
	 * @api {POST} /notification/email Send email service
	 * @apiName sendEmail
	 * @apiGroup NotificationProvider
	 *
	 * @apiParam {String} recipient Email recipient.
	 * @apiParam {String} subject Email subject.
	 * @apiParam {String} content Email content.
	 *
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 * @apiError(Error 503) ServiceError The intern service cause a error.
	 */
	@Path("/email")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendEmail(EmailDTO email) {
		try {
			EmailService.gmailSender(email.getRecipient(), email.getSubject(), email.getContent());
		} catch (MessagingException e) {
			e.printStackTrace();
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("FAILURE").build();
		}
        return Response.status(Response.Status.OK).entity("").build();
	}
	
}
