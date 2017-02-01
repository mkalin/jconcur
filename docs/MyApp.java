import java.awt.Frame; // top-level window

public class MyApp {
    public static void main(String[ ] args) {
	System.out.println("main-thread in action...");

	// a new thread manages the Frame when the Frame is shown
	new Frame("Just a demo, folks").setVisible(true);

	System.out.println("main-thread is about to exit...");
    } // 'main-thread' dies, but app lives on
}
