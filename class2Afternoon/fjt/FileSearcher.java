package fjt;
 
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.concurrent.RecursiveTask;

/** 
 * The recursive task is to search a hierarchical file system for
 * files with a given extension (e.g., ".txt" or ".java"), starting at
 * the specified path (e.g., "/home/fred"). A list of such files is returned.
 *
 * Here's a simplified depiction, with ## introducing comments:
 *
 *                  /home/fred             ## root of the tree to be searched -- a directory
 *                      |
 *           +----------+----------+
 *           |          |          |
 *         movies      docs       code     ## subdirectories
 *           |          |          |
 *          ...        ...      Foo.java   ## non-directory file Foo.java
 *
 * The search starts at the specified directory /home/fred, which contains three
 * subdirectories: movies, docs, and code. Searching each of these subdirectories
 * generates a new task, as a directory is a file that contains other files. If a
 * non-directory file is found (e.g., Foo.java), and this file has the desired
 * extension (in this case, ".java"), then the file is added to the list of 
 * found files: this is a base case, and so no new task is generated.
 *
 * Java's Fork/Join framework is a high-level API for the 'scatter/gather' idiom:
 * scatter ('fork') the tasks to be handled among different threads, then gather ('join') 
 * the results together.
 *
 */

// The List<String> in RecursiveTask is a list of file names with a specified extension: it's
// the type returned from the override of the compute() method.
//
// A RecursiveTask is one that can divided into subtasks of the same kind. In this case,
// the RecursiveTask is to search a tree--a hierarchical file system--because the subtrees are
// themselves hierarchical file systems.
public class FileSearcher extends RecursiveTask<List<String>> { 
    private final String path;        // where to start a task/subtask
    private final String extension;   // what kind of file to look for
 
    // a FileSearcher needs a starting path and a desired file extension.
    public FileSearcher(String path, String extension) {
	this.path = path;
	this.extension = extension;
    }
    
    @Override
    public List<String> compute() {
	List<String> listOfFileNames = new ArrayList<String>(); // empty list

	// files in the current path (including subpaths)
	File[ ] files = new File(path).listFiles();
	if (files == null) return listOfFileNames; // base case

	// Iterate through the files, keeping a list of the tasks generated
	// to search the directories among the files.
	List<FileSearcher> tasks = new ArrayList<FileSearcher>();

	for (File file : files) {
	    String absolutePath = file.getAbsolutePath();

	    // Recursive case: a directory is a collection of files
	    if (file.isDirectory()) { 
		FileSearcher task = new FileSearcher(absolutePath, extension);
		task.fork();     // new task to search the directory
		tasks.add(task); // keep track of the new task for a report
	    }
	    // Base case: a non-directory file -- with the desired extension?
	    else if (file.getName().endsWith(extension))
		listOfFileNames.add(absolutePath); // keep track of the found files
	}

	// Gather results from the subtasks.
	assembleResults(listOfFileNames, tasks);
	return listOfFileNames;
    }

    // Execute the 'gather' half of 'scatter/gather' by joing together the
    // files found.
    private void assembleResults(List<String> list, List<FileSearcher> tasks) {
	for (FileSearcher task : tasks) {
	    Collection<String> results = task.join(); // the 'join' in fork-join
	    list.addAll(results);
	}
    }
}

