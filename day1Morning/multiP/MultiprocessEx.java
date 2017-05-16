package multiP;

/** 
    Execution: java multiP.MultiprocessingEx

    A short program to illustrate multiprocessing in Java via the
    overloaded exec method in the java.lang.Runtime class.

    This program runs a local text editor (see documentation below) on a file: the
    editor runs as a process separate from the JVM, which exists after 'execing'
    the text-editor process. The program introduces part of Java's multiprocessing
    API, and underscores the independence of processes.

    See also: java.lang.ProcessBuilder
*/
public class MultiprocessEx {
    // Set to an editor on local system, one that can be invoked from
    // the command-line. In this example, the editor is named 'gedit', and is 
    // available on various Unix-like systems. (On Windows, 'notepad' might be used instead; on a Mac, 'vim'.)
    private static final String sampleEditor = "gedit";  // the editor
    private static final String file2Edit = "text.txt";  // the file to be edited (included in the ZIP)

    public static void main(String[ ] args) {
	try {
	    // Set up the executable command as a string.
	    String cmd = sampleEditor + " " + file2Edit;
	    
	    System.err.println("About to execute: " + cmd);

	    // Execute the command, which results in a separate process.
	    // The Process reference then can be used to analyze and manage this process.
	    // In the meantime, the JVM will execute two print statements below, and then exit;
	    // but the editor process continues until killed.
	    Process proc = Runtime.getRuntime().exec(cmd);

	    // Some info to see what's happening.
	    System.err.println("Is the execed process alive? Answer: " + proc.isAlive());

	    int status = proc.waitFor();  // wait for the execed process to exit
	    System.err.println("execed process terminated with status code " + status);
	    System.err.println("JVM about to exit...");
	} 
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
}

/** Output from sample run:

    About to execute: gedit text.txt
    Is the execed process alive? Answer: true
    execed process terminated with status code 0
    JVM about to exit...
*/
