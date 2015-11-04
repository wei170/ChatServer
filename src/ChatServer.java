import java.util.*;

/**
 * <b> CS 180 - Project 4 - Chat Server Skeleton </b>
 * <p>
 * 
 * This is the skeleton code for the ChatServer Class. This is a private chat
 * server for you and your friends to communicate.
 * 
 * @author (Your Name) <(YourEmail@purdue.edu)>
 * 
 * @lab (Your Lab Section)
 * 
 * @version (Today's Date)
 *
 */
public class ChatServer {
	
	public ChatServer(User[] users, int maxMessages) {
		// TODO Complete the constructor
	}

	/**
	 * This method begins server execution.
	 */
	public void run() {
		boolean verbose = false;
		System.out.printf("The VERBOSE option is off.\n\n");
		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.printf("Input Server Request: ");
			String command = in.nextLine();

			// this allows students to manually place "\r\n" at end of command
			// in prompt
			command = replaceEscapeChars(command);

			if (command.startsWith("kill"))
				break;

			if (command.startsWith("verbose")) {
				verbose = !verbose;
				System.out.printf("VERBOSE has been turned %s.\n\n", verbose ? "on" : "off");
				continue;
			}

			String response = null;
			try {
				response = parseRequest(command);
			} catch (Exception ex) {
				response = MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_ERROR,
						String.format("An exception of %s occurred.", ex.getMessage()));
			}

			// change the formatting of the server response so it prints well on
			// the terminal (for testing purposes only)
			if (response.startsWith("SUCCESS\t"))
				response = response.replace("\t", "\n");

			// print the server response
			if (verbose)
				System.out.printf("response:\n");
			System.out.printf("\"%s\"\n\n", response);
		}

		in.close();
	}

	/**
	 * Replaces "poorly formatted" escape characters with their proper values.
	 * For some terminals, when escaped characters are entered, the terminal
	 * includes the "\" as a character instead of entering the escape character.
	 * This function replaces the incorrectly inputed characters with their
	 * proper escaped characters.
	 * 
	 * @param str
	 *            - the string to be edited
	 * @return the properly escaped string
	 */
	private static String replaceEscapeChars(String str) {
		str = str.replace("\\r", "\r");
		str = str.replace("\\n", "\n");
		str = str.replace("\\t", "\t");

		return str;
	}

	/**
	 * Determines which client command the request is using and calls the
	 * function associated with that command.
	 *
     * 1.Parse the client request (that means split the request into the command and parameters).
     * 2.Check format validity (valid command and correct number of parameters for this command).
     *      if invalid return the appropriate error message
     * 3.Verify the user's login session through their cookie (except for USER-LOGIN, since no cookie is required).
     *      if the cookie is null, then the user is not authenticated yet, return the appropriate error message
     *      if the cookie timedout, set the user cookie to null and return the appropriate error message
     *      otherwise continue to the next step.
     * 4.Invoke the appropriate protocol method and return its response.
     *
     *            Client Request Commands
     * Command	       Parameter 1	    Parameter 2	    Parameter 3
     * ---------------------------------------------------------------
     * ADD-USER	       cookie ID	    username	    password
     * USER-LOGIN	   username	        password
     * POST-MESSAGE	   cookie ID	    message
     * GET-MESSAGES	   cookie ID	    numMessages

     *
	 * @param request
	 *            - the full line of the client request (CRLF included)
	 * @return the server response
	 */
	public String parseRequest(String request) {
		// TODO: Is the complete line of the client request.
        String[] requestArray = request.split("\t");

		return request;
	}


    /******************************************************************
     * Protocol Method
     *
     * @param args
     * @return
     ******************************************************************/
	public String addUser(String[] args) {
		//TODO

		return "";
	}

	public String userLogin(String[] args) {
		//TODO
		return "";
	}

	public String postMessage(String[] args, String name) {
		//TODO
		return "";
	}

	public String getMessages(String[] args) {
		//TODO
		return "";
	}
}
