/**
 * <b> CS 180 - Project 4 - Message Factory </b>
 * <p>
 * 
 * This is the error message factory class.
 *
 */

/**
 *  Code 	Name 	Standard Message 	Additional Explanation
 0X 	Server Errors 	– 	Error Class
 00 	Unknown Error 	“Unknown Error: An unknown error occurred. This was likely caused by an uncaught exception.”

 This is the catch-all error code.
 If something unexpectedly goes wrong, like an exception being thrown or a parameter value being null, this code should be sent.

 05 	Cookie Timeout Error 	“Cookie Timeout Error: Your login cookie has timed out.”

 This error should be sent when a user, who had previously logged in to the server, sends a request to the server when their session cookie has timed out.
 Whenever this error is sent by the server, the requesting user's session cookie should be deleted (setting it to null), as they have been logged out of the server due to inactivity.

 1X 	Request Format Errors 	– 	Error Class
 10 	Format Command Error 	“Format Command Error: The specified client command isn't formatted properly.”

 This error code should be returned if there are any errors with the text formatting of the client request.

 11 	Unknown Command Error 	“Unknown Command Error: The specified client command doesn't exist.”

 Your server should respond with this error code if the command of the client request does not match any of the protocol commands specified in this handout.

 2X 	Request Denial Errors 	– 	Error Class
 20 	Username Lookup Error 	“Username Lookup Error: The specified user does not exist.”

 This error should be sent by the server when a specified user does not exist on the server.

 21 	Authentication Error 	“Authentication Error: The given password is not correct for the specified user.”

 This error code should be returned by the server when a user attempts to log in to the server without providing the proper password.

 22 	User Error 	“User Error: The user cannot be created because the username has already been taken.”

 You server should respond with this code when a error occurs regarding adding users to the server and there are no other codes that are more applicable to the situation.

 23 	Login Error 	“Login Error: The specified user has not logged in to the server.”

 This error should be returned by the server when a user attempts to perform an action on the server without having first logged in.

 24 	Invalid Value Error 	“Invalid Value Error: One of the specified values is logically invalid.”

 You should send this error if one of the values passed as a client request parameter is not logically valid (e.g. receiving a string when expecting an integer).

 25 	User Connected Error 	“User Connected Error: The specified user is already logged in the server.”

 You should send this error if someone tries to connect to the server with a user already connected (a connected user should have a cookie different from null)


 */
public class MessageFactory {
	
	public static final int UNKNOWN_ERROR = 0;
	public static final int COOKIE_TIMEOUT_ERROR = 5;
	
	public static final int FORMAT_COMMAND_ERROR = 10;
	public static final int UNKNOWN_COMMAND_ERROR = 11;
	
	public static final int USERNAME_LOOKUP_ERROR = 20;
	public static final int AUTHENTICATION_ERROR = 21;
	public static final int USER_ERROR = 22;
	public static final int LOGIN_ERROR = 23;
	public static final int INVALID_VALUE_ERROR = 24;
	public static final int USER_CONNECTED_ERROR = 25;
	
	/**
	 * Creates a "FAILURE" server response based on the error code
	 * and appends the user message to the end of the response.
	 * 
	 * @param errorCode - the generic error that occurred
	 * @param customMessage - the message describing the error
	 * @return a fully formatted server failure response or null if 
	 */
	public static String makeErrorMessage(int errorCode, String customMessage) {
		StringBuilder ret = new StringBuilder("FAILURE\t");
		ret.append(errorCode);
		ret.append("\t");
		
		switch(errorCode) {
			case UNKNOWN_ERROR:
				ret.append("Unknown Error: ");
				break;
				
			case COOKIE_TIMEOUT_ERROR:
				ret.append("Cookie Timeout Error: ");
				break;
				
			case FORMAT_COMMAND_ERROR:
				ret.append("Format Command Error: ");
				break;
				
			case UNKNOWN_COMMAND_ERROR:
				ret.append("Unknown Command Error: ");
				break;
				
			case USERNAME_LOOKUP_ERROR:
				ret.append("Username Lookup Error: ");
				break;
				
			case AUTHENTICATION_ERROR:
				ret.append("Authentication Error: ");
				break;
				
			case USER_ERROR:
				ret.append("User Error: ");
				break;
				
			case LOGIN_ERROR:
				ret.append("Login Error: ");
				break;
				
			case INVALID_VALUE_ERROR:
				ret.append("Invalid Value Error: ");
				break;
				
			case USER_CONNECTED_ERROR:
				ret.append("User Connected Error: ");
				break;
				
			default:
				return makeErrorMessage(0, String.format(
				"The error code \"%02d\" is not recognized.", errorCode));
		}
		
		ret.append(customMessage);
		ret.append("\r\n");
		
		return ret.toString();
	}
	
	public static String makeErrorMessage(int errorCode) {
		String message = "";
		
		switch(errorCode) {
			case UNKNOWN_ERROR:
				message = "An unknown error occurred. This was likely caused by an uncaught exception.";
				break;
				
			case COOKIE_TIMEOUT_ERROR:
				message = "Your login cookie has timed out.";
			break;
			
			case FORMAT_COMMAND_ERROR:
				message = "The specified client command isn't formatted properly.";
				break;
			
			case UNKNOWN_COMMAND_ERROR:
				message = "The specified client command doesn't exist.";
				break;

			case USERNAME_LOOKUP_ERROR:
				message = "The specified user does not exist.";
				break;
				
			case AUTHENTICATION_ERROR:
				message = "The given password is not correct for the specified user.";
				break;
				
			case USER_ERROR:
				message = "The user cannot be created because the username has already been taken.";
				break;
				
			case LOGIN_ERROR:
				message = "The specified user has not logged in to the server.";
				break;
				
			case INVALID_VALUE_ERROR:
				message = "One of the specified values is logically invalid.";
				break;
				
			case USER_CONNECTED_ERROR:
				message = "User Connected Error: The specified user is already logged in the server.";
				break;
		}
		
		return makeErrorMessage(errorCode, message.toString());
	}

}
