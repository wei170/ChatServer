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
	private User[] users;
    private int maxMessages;
	public ChatServer(User[] users, int maxMessages) {
		// TODO Complete the constructor
        this.users = users;
        this.maxMessages = maxMessages;
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
	 *            - the full line of the client request (CRLF inclduded)
	 * @return the server response
	 */
	public String parseRequest(String request) {
		// TODO: Is the complete line of the client request.
        String[] requestArray = request.split("\t");

        /*
         * Verifying the Request Format
         * For all requests, you must validate that the text of the request adheres to the protocol
         * (e.g., the fields of the request being tab delimited).
         * This includes things such as checking the number of parameters or ensuring that an integer parameter is
         *      actually a number within the correct bound.
         *
         *  1X	Request Format Errors
         *
         *  10	Format Command Error	  “Format Command Error: The specified client command isn't formatted properly.”
         *  This error code should be returned if there are any errors with the text formatting of the client request.
         *
         *  11	Unknown Command Error	“Unknown Command Error: The specified client command doesn't exist.”
         *  Your server should respond with this error code if the command of the client request does not
         *          match any of the protocol commands specified in this handout.
         */

        try {
            switch (requestArray[0]) {
                case ("ADD-USER"):
                    if (requestArray.length != 4) {
                        return String.format("FAILURE\t%2d\t%s\r\n", 10, MessageFactory.makeErrorMessage(10));
                    }
                    if (requestArray[1] == null || requestArray[2] == null || requestArray[3] == null ||
                            Integer.parseInt(requestArray[1]) < 0 || Integer.parseInt(requestArray[1]) > 9999) {
                        return String.format("FAILURE\t%2d\t%s\r\n", 10, MessageFactory.makeErrorMessage(10));
                    }
					break;
                case ("USER-LOGIN"):
                    if (requestArray.length != 3) {
                        return String.format("FAILURE\t%2d\t%s\r\n", 10, MessageFactory.makeErrorMessage(10));
                    }
                    if (requestArray[1] == null || requestArray[2] == null) {
                        return String.format("FAILURE\t%2d\t%s\r\n", 10, MessageFactory.makeErrorMessage(10));
                    }
                    break;
                case ("POST-MESSAGE"):
                    if (requestArray.length != 3) {
                        return String.format("FAILURE\t%2d\t%s\r\n", 10, MessageFactory.makeErrorMessage(10));
                    }
                    if (requestArray[1] == null || requestArray[2] == null ||
                            Integer.parseInt(requestArray[1]) < 0 || Integer.parseInt(requestArray[1]) > 9999) {
                        return String.format("FAILURE\t%2d\t%s\r\n", 10, MessageFactory.makeErrorMessage(10));
                    }
                    break;
                case ("GET-MESSAGES"):
                    if (requestArray.length != 3) {
                        return String.format("FAILURE\t%2d\t%s\r\n", 10, MessageFactory.makeErrorMessage(10));
                    }
                    if (requestArray[1] == null || requestArray[2] == null ||
                            Integer.parseInt(requestArray[1]) < 0 || Integer.parseInt(requestArray[1]) > 9999) {
                        return String.format("FAILURE\t%2d\t%s\r\n", 10, MessageFactory.makeErrorMessage(10));
                    }
                    Integer.parseInt(requestArray[2]);
                    break;
                default:
                    return String.format("FAILURE\t%2d\t%s\r\n", 10, MessageFactory.makeErrorMessage(10));
            }
        } catch (Exception e) {
            return String.format("FAILURE\t%2d\t%s\r\n", 11, MessageFactory.makeErrorMessage(11));
            // Your server should respond with this error code if the command of the client request
            // does not match any of the protocol commands specified in this handout.
        }

        /*
         *  Verify the user's login session through their cookie (except for USER-LOGIN, since no cookie is required).
         *      a. if the cookie is null, then the user is not authenticated yet, return the appropriate error message
         *      b. if the cookie timedout, set the user cookie to null and return the appropriate error message
         *      c. otherwise continue to the next step.
         */
        if (!requestArray[0].equals("USER-LOGIN")) {
            if (users[Integer.parseInt(requestArray[1])].getCookie() == null) {
                return String.format("FAILURE\t%2d\t%s\r\n", 21, MessageFactory.makeErrorMessage(21));
            }
            if (users[Integer.parseInt(requestArray[1])].getCookie().hasTimeOut()) {
                return String.format("FAILURE\t%2d\t%s\r\n", 05, MessageFactory.makeErrorMessage(05));
            }
        }
        
        // TODO Finish it!
        return "";
	}


    /*****************************************************************
     * Protocol Method
     *
     * @param args
     * @return
     ******************************************************************/
	public String addUser(String[] args) {
		//TODO
        /*
         * To solve the problem of how to add the first real user, your server will implement a default user.
         * A default user is a user whose existence is hardcoded into the server and
         * will exist in every instance of the server.
         * Your default user should have the username root and the password cs180.
         */

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
