import java.util.*;

/**
 * <b> CS 180 - Project 4 - Chat Server Skeleton </b>
 * <p>
 * 
 * This is the skeleton code for the ChatServer Class. This is a private chat
 * server for you and your friends to communicate.
 * 
 * @Guocheng
 * @Xiaowei
 * 
 *
 *
 */
public class ChatServer {
	private User[] users;
    CircularBuffer buffer;
    Random r = new Random();
    public ChatServer(User[] users, int maxMessages) {
		// TODO Complete the constructor
        this.users = users;

        User[] temp = Arrays.copyOf(this.users, this.users.length + 1);
        temp[temp.length - 1] = new User("root", "cs180", null);
        this.users = temp;

        this.buffer = new CircularBuffer(maxMessages);
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
        request = request.substring(0, request.length() - 2);

        String[] requestArray = request.split("\t");
        /*
         * Verifying the Request Format
         * For all requests, you must validate that the text of the request adheres to the protocol
         * (e.g., the fields of the request being tab delimited).
         * This includes things such as checking the number of parameters or ensuring that an integer parameter is
         *      actually a number within the correct bound.
         *

         */

        try {
            switch (requestArray[0]) {
                case ("ADD-USER"):
                    if (requestArray.length != 4) {
                        return MessageFactory.makeErrorMessage(10);
                    }
                    if (requestArray[1] == null || requestArray[2] == null || requestArray[3] == null ||
                            Integer.parseInt(requestArray[1]) < 0 || Integer.parseInt(requestArray[1]) > 9999) {
                        return MessageFactory.makeErrorMessage(10);
                    }
                    break;
                case ("USER-LOGIN"):
                    if (requestArray.length != 3) {
                        return MessageFactory.makeErrorMessage(10);
                    }
                    if (requestArray[1] == null || requestArray[2] == null) {
                        return MessageFactory.makeErrorMessage(10);
                    }
                    break;
                case ("POST-MESSAGE"):
                    if (requestArray.length != 3) {
                        return MessageFactory.makeErrorMessage(10);
                    }
                    if (requestArray[1] == null || requestArray[2] == null ||
                            Integer.parseInt(requestArray[1]) < 0 || Integer.parseInt(requestArray[1]) > 9999) {
                        return MessageFactory.makeErrorMessage(10);
                    }
                    break;
                case ("GET-MESSAGES"):
                    if (requestArray.length != 3) {
                        return MessageFactory.makeErrorMessage(10);
                    }
                    if (requestArray[1] == null || requestArray[2] == null ||
                            Integer.parseInt(requestArray[1]) < 0 || Integer.parseInt(requestArray[1]) > 9999) {
                        return MessageFactory.makeErrorMessage(10);
                    }
                    Integer.parseInt(requestArray[2]);
                    break;
                default:
                    return MessageFactory.makeErrorMessage(11);
            }
        } catch (Exception e) {
            return MessageFactory.makeErrorMessage(24);
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
            for (User u : users) {
                if (u.getName().equals(requestArray[1])) {
                    if (u.getCookie() == null) {
                        return MessageFactory.makeErrorMessage(21);
                    }
                    if (u.getCookie().hasTimedOut()) {
                        u.setCookie(null);
                        return MessageFactory.makeErrorMessage(5);
                    }
                }
            }
        }

        switch (requestArray[0]) {
            case ("ADD-USER"):
                return addUser(requestArray);
            case ("USER-LOGIN"):
                return userLogin(requestArray);
            case ("POST-MESSAGE"):
                for (User u : users) {
                    if (u.getCookie() != null && u.getCookie().getID() == Long.parseLong(requestArray[1]))
                        return postMessage(requestArray, u.getName());
                }
                return MessageFactory.makeErrorMessage(23);
            case ("GET-MESSAGES"):
                return getMessages(requestArray);
            default:
                return MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_COMMAND_ERROR);
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
         * To solve the problem of how to add the first real user, your server will implement a default user.
         *  1. A default user is a user whose existence is hardcoded into the server
         *      and will exist in every instance of the server.
         *  2. Your default user should have the username root and the password cs180.
         */


        /*
         * addUser needs to verify that the user doesn't already exist
         *
         * 1. Usernames and passwords can only contain alphanumerical values [A-Za-z0-9].
         * 2. Usernames must be between 1 and 20 characters in length (inclusive).
         * 3. Password must be between 4 and 40 characters in length (inclusive).
         *
         */
        if (!isLogin(Long.parseLong(args[1]))) {
            return  MessageFactory.makeErrorMessage(MessageFactory.LOGIN_ERROR);
        }
        for (User u : users) {
            if (args[2].equals(u.getName())) {
                return MessageFactory.makeErrorMessage(MessageFactory.USER_ERROR);
            }
        }

        if (!args[2].matches("[A-Za-z0-9]+") || !args[3].matches("[A-Za-z0-9]+") ||
                 args[2].length() < 1 || args[2].length() > 20 ||
                 args[3].length() < 4 || args[3].length() > 40) {
            return MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR);
        }

        for (User u : users) {
            if (u.getCookie() != null && u.getCookie().getID() == Long.parseLong(args[1])) {
                if (u.getCookie().hasTimedOut()) {
                    u.setCookie(null);
                    return MessageFactory.makeErrorMessage(MessageFactory.COOKIE_TIMEOUT_ERROR);
                }
                u.getCookie().updateTimeOfActivity();
            }
        }

        User[] temp = Arrays.copyOf(users, users.length + 1);
        User newUser = new User(args[2], args[3], null);
        temp[temp.length - 1] = newUser;
        users = temp;

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
            return MessageFactory.makeErrorMessage(MessageFactory.USERNAME_LOOKUP_ERROR);
        }
        for (User u : users) {
            if (args[1].equals(u.getName())) {
                if (u.getCookie() != null) {
                    return MessageFactory.makeErrorMessage(MessageFactory.USER_CONNECTED_ERROR);
                }
                if (!u.checkPassword(args[2])) {
                    return MessageFactory.makeErrorMessage(MessageFactory.AUTHENTICATION_ERROR);
                }
                u.setCookie(new SessionCookie((long) (Math.random() * 10000)));

                return String.format("SUCCESS\t%04d\r\n", u.getCookie().getID());
            }

        }

        /*
         * 1. When this request succeeds, the server sends the client their login cookie ID.
         *      You should note that the session ID (0234) is separated from the request status by a tab '\t'
         *      because it is a separate field of information.
         * 2. You should note how the cookie ID is formatted in this example response.
         *      Even though the value of the identifier is 234, four digits were returned in the string.
         *      Regardless of the actual number of characters needed to represent the number,
         *      4 digits should be returned by the server.
         *          Ex: If the ID number is 12, the server returns “SUCCESS\t0012\r\n”.
         *          Similarly, if the ID is 4, the server responds with “SUCCESS\t0004\r\n”.
         */
        return MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_ERROR);
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
        if (!isLogin(Long.parseLong(args[1]))) {
            return  MessageFactory.makeErrorMessage(MessageFactory.LOGIN_ERROR);
        }
        for (User u : users) {
            if (u.getCookie() != null && u.getName().equals(name)) {
                if (u.getCookie().hasTimedOut()) {
                    u.setCookie(null);
                    return MessageFactory.makeErrorMessage(MessageFactory.COOKIE_TIMEOUT_ERROR);
                }
                else u.getCookie().updateTimeOfActivity();
            }
        }
        if ((args[2].trim().length()) < 1)
            return MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR);

        String message = String.format("%s: %s", name, args[2]);
        this.buffer.put(message);
		return "SUCCESS\r\n";
	}

	public String getMessages(String[] args) {
        /*
         * 1. For the request to succeed, the number of messages requested must be >= 1,
         *  otherwise an INVALID_VALUE_ERROR (error #24) should be returned.
         * 2. The number of messages required can be higher than the number of available messages,
         *  the function returns as many as possible. It can also return 0 messages if none are available (SUCCESS\r\n).
         * 3. Messages should be listed in chronological order with the oldest messages at the beginning.
         *
         * For example, if my username was cs180 and I posted the messages “Hello, World” and then “What's up?”,
         *  the ASCII server response would look like this:
         *      "SUCCESS\t0000) cs180: Hello, World!\t0001) cs180: What's up?\r\n"
         *
         */
        if (!isLogin(Long.parseLong(args[1]))) {
            return  MessageFactory.makeErrorMessage(MessageFactory.LOGIN_ERROR);
        }

        try {
            for (User u : users) {
                if (u.getCookie() != null && u.getCookie().getID() == Long.parseLong(args[1])) {
                    if (u.getCookie().hasTimedOut()) {
                        u.setCookie(null);
                        return MessageFactory.makeErrorMessage(MessageFactory.COOKIE_TIMEOUT_ERROR);
                    }
                }
            }

            if (Integer.parseInt(args[2]) < 1) {
                return MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR);
            }

            String result = "SUCCESS";
            for (String s : buffer.getNewest(Integer.parseInt(args[2]))) {
                result += "\t" + s;
            }
            result += "\r\n";
            return result;
        } catch (NumberFormatException e) {
            return MessageFactory.makeErrorMessage(24);
        }
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

    public boolean isLogin(long id) {
        for (User u : users) {
            if (u.getCookie() != null && id == u.getCookie().getID()) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        User[] users = new User[1];
        users[0] = new User("greg", "greg", new SessionCookie(42));
        ChatServer chatServer = new ChatServer(users, 100);

        String msg = "Hello, world!!";

        String ta = "SUCCESS\r\n";
        String student = chatServer.postMessage(new String[] { "POST-MESSAGE", "42", msg },  "greg");
        System.out.println(student);
        student = chatServer.getMessages(new String[]{"GET-MESSAGES", "42", "1"});
        System.out.println(student);

        String[] tab = student.trim().split("\t");
        System.out.println(tab[1]);

    }
}
