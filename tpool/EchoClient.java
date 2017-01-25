package tpool;

import java.net.Socket;

public class EchoClient {
    private static final int portNumber = 9876;
    private static final String host = "localhost";

    public static void main(String[ ] args) {
	new EchoClient().demo();
    }

    private void demo() {
	String msg = "Hello, world!";       // the usual
	int n = 8;
	for (int i = 0; i < n; i++) {
	    try {
		byte[ ] buffer = new byte[256]; // a little margin for error
		Socket client = new Socket(host, portNumber);
		client.getOutputStream().write(msg.getBytes());
		client.getInputStream().read(buffer);
		System.out.println(new String(buffer));
		client.close();
	    }
	    catch (Exception e) {
		System.err.println(e);
	    }
	}
    }
}
