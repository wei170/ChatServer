
import java.util.*;
import java.util.regex.Pattern;

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
    CircularBuffer buffer;
	public ChatServer(User[] users, int maxMessages) {
		// TODO Complete the constructor
        this.users = users;
        this.maxMessages = maxMessages;
        this.buffer = new CircularBuffer(6);
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

        switch (requestArray[0]) {
            case ("ADD-USER"):
                return addUser(requestArray);
            case ("USER-LOGIN"):
                return userLogin(requestArray);
            case ("POST-MESSAGE"):
                try {
                    return postMessage(requestArray, users[Integer.parseInt(requestArray[1])].getName());
                } catch (Exception e) {
                    return String.format("FAILURE\t%2d\t%s\r\n", 11, MessageFactory.makeErrorMessage(11));
                }
            case ("GET-MESSAGES"):
                return getMessages(requestArray);
            default:
                return String.format("FAILURE\t%2d\t%s\r\n", 11, MessageFactory.makeErrorMessage(11));
        }
    }


    /*****************************************************************
     * Protocol Method
     *
     * Each method needs to make its specific validation
     *  (e.g: addUser needs to verify that the user doesn't already exist),
     *   perform its job and update the cookie if needed.
     *
     * @param args
     * @return
     ******************************************************************/
	public String addUser(String[] args) {
        /*
         * addUser needs to verify that the user doesn't already exist
         *
         * 1. Usernames and passwords can only contain alphanumerical values [A-Za-z0-9].
         * 2. Usernames must be between 1 and 20 characters in length (inclusive).
         * 3. Password must be between 4 and 40 characters in length (inclusive).
         *
         */
        for (int i = 0; i < users.length; i++) {
            if (users[i].getCookie().getID() == Long.parseLong(args[1])) {
                return String.format("FAILURE\t%2d\t%s\r\n", 22, MessageFactory.makeErrorMessage(22));
            }
        }

        if (!args[2].matches("[A-Za-z0-9]+") || !args[3].matches("[A-Za-z0-9]+") ||
                args[2].length() < 1 || args[2].length() > 20||
                args[3].length() < 4 || args[3].length() > 40) {
            return String.format("FAILURE\t%2d\t%s\r\n", 24, MessageFactory.makeErrorMessage(24));
        }

        // TODO: Add the user into the Users[] users



		return "SUCCESS\r\n";
	}

	public String userLogin(String[] args) {
        /*
         * 1. The user must already have been created earlier through addUser
         * 2. The given user shouldn't already be authenticated (the SessionCookie associated should be null)
         * 3. The password must be correct
         * If every condition is met, then the method generates a new SessionCookie for the user to indicate that
         *  she is now connected.
         *  TODO: come up with more situation
         */
        if (!isCreated(args[1])) {
            return String.format("FAILURE\t%2d\t%s\r\n", 20, MessageFactory.makeErrorMessage(20));
        }
        for (User u : users) {
            if (args[1].equals(u.getName())) {
                if (u.getCookie() != null) {
                    return String.format("FAILURE\t%2d\t%s\r\n", 25, MessageFactory.makeErrorMessage(25));
                }
            }
            if (!u.checkPassword(args[2])) {
                return String.format("FAILURE\t%2d\t%s\r\n", 21, MessageFactory.makeErrorMessage(21));
            }
        }

        //TODO: Generate a bew SessionCookie for the user to indicate that she is now connected

		return "SUCCESS\t0234\r\n";
	}

	public String postMessage(String[] args, String name) {
        /*
         * The name variable is the username of the User sending the message.
         *
         * 1. The name variable is the username of the User sending the message.
         * 2. For the request to succeed, the message should contain at least 1 character after removing leading and
         *      trailing white spaces from the message.
         * 3. Messages have no limit on their length.
         * 4. The username of the poster should be displayed first, followed by a colon, a space, and then the message.
         *      For example, if my username is cs180 and I posted the message “Hello, World”
         *      then the server should store the message as:
         *
         *          "cs180: Hello, World!"
         *
         */
        int countSpace = 0;
        if (args[2].charAt(0) == ' ') countSpace++;
        if (args[2].charAt(args[2].length() - 1) == ' ') countSpace++;
        if ((args[2].length() - countSpace) < 1)
            return String.format("FAILURE\t%2d\t%s\r\n", 24, MessageFactory.makeErrorMessage(24));

        String message = String.format("%s: %s", name, args[2]);
        this.buffer.put(message);
		return "SUCCESS\r\n";
	}

	public String getMessages(String[] args) {
		//TODO
		return "SUCCESS\tmessage1\tmessage2\tmessage3\r\n";
	}

    /**
     * This is for userLogin
     * @param userName
     * @return
     */
    public boolean isCreated(String userName) {
        for (User u : users) {
            if (userName.equals(u.getName())) {
                return true;
            }
        }
        return false;
    }
}
