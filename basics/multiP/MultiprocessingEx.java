package multiP;

/** 
    Execution: java multiP.MultiprocessingEx

    A short program to illustrate multiprocessing in Java via the
    overloaded exec method in the java.lang.Runtime class.

    This program runs a local text editor (see documentation below) on a file: the
    editor runs as a process separate from the JVM, which exists after 'execing'
    the text-editor process. The program introduces part of Java's multiprocessing
    API, and underscores the independence of separate processes.
*/
public class MultiprocessingEx {
    // Set to an editor on local system, one that can be invoked from
    // the command-line. In this example, the editor is named 'gedit', 
    // and is available on Unix-like systems. (On Windows, 'notepad' would do.)
    private static final String sampleEditor = "gedit";  // the editor
    private static final String file2Edit = "text.txt";  // the file to be edited

    public static void main(String[ ] args) {
	try {
	    // Set up the executable command as a string.
	    String cmd = sampleEditor + " " + file2Edit;
	    
	    System.err.println("About to execute: " + cmd);

	    // Execute the command, which results in a separate process.
	    // The Process reference then can be used to managed this separate process.
	    // In the meantime, the JVM will execute two print statements below, and then exit.
	    Process proc = Runtime.getRuntime().exec(cmd);

	    // Some info to see what's happening.
	    System.err.println("Is the execed process alive? Answer: " + proc.isAlive());
	    System.err.println("JVM about to exit...");
	} 
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
