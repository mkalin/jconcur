package tpool;

import java.net.ServerSocket;  // server-side "accepting" socket
import java.net.Socket;        // client-side "initiating" socket

// Simple 'concurrent server' that echoes back the request element named "message".
public class EchoServer {
    private static final int portNumber = 9876;

    public static void main(String[ ] args) {
	new EchoServer().demo();
    }

    private void demo() {
	try {
	    ServerSocket acceptor = new ServerSocket(portNumber); // arg is the port number
	    System.out.println("Server listening on port " + portNumber);

	    // Listen for requests indefinitely.
	    while (true) {
		Socket client = acceptor.accept();  // blocks until there's a request
		new RequestHandler(client).start(); // delegate request-handling to a new thread: ###
	    }
	}
	catch (Exception e) {
	    System.err.println(e);
	}
    }
}

// one-thread-per-request model
class RequestHandler extends Thread {
    private Socket client;

    RequestHandler(Socket client) {
	this.client = client;
    }

    @Override
    public void run() {
	try {
	    byte[ ] incoming = new byte[140];

	    client.getInputStream().read(incoming); // read up to 140 bytes
	    String echoMsg = "Echoing back: " + new String(incoming);  
	    client.getOutputStream().write(echoMsg.getBytes());

	    client.close(); // disconnects
	}
	catch(Exception e) {
	    System.err.println(e);
	}
    } // thread terminates when it exists run ###
}
