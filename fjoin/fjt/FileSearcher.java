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
 *                  /home/fred     ## root of the tree to be searched -- a directory
 *                      |
 *           +----------+----------+
 *           |          |          |
 *         movies      docs       code     ## subdirectory files
 *           |          |          |
 *          ...        ...      Foo.java   ## non-directory file
 *
 * The search starts at the specified directory /home/fred, which contains three
 * subdirectories: movies, docs, and code. Searching each of these subdirectories
 * generates a new task, as a directory is a file that contains other files. If a
 * non-directory file is found (e.g., Foo.java), and this file has the desired
 * extension (in this case, ".java"), then the file is added to the list of 
 * found files: this is a base case, and so no new task is generated.
 *
 */
public class FileSearcher extends RecursiveTask<List<String>> {
    private final String path;
    private final String extension;
 
    public FileSearcher(String path, String extension) {
	this.path = path;
	this.extension = extension;
    }
    
    @Override
    public List<String> compute() {
	List<String> listOfFileNames = new ArrayList<String>(); // empty list

	File[ ] files = new File(path).listFiles();
	if (files == null) return listOfFileNames; // base case

	List<FileSearcher> tasks = new ArrayList<FileSearcher>();
	for (File file : files) {
	    String absolutePath = file.getAbsolutePath();

	    if (file.isDirectory()) { // a directory is the recursive case
		FileSearcher task = new FileSearcher(absolutePath, extension);
		task.fork();     // recursive case
		tasks.add(task); // keep track of the new task
	    }
	    else if (file.getName().endsWith(extension))
		listOfFileNames.add(absolutePath); // keep track of the found files
	}

	assembleResults(listOfFileNames, tasks);
	return listOfFileNames;
    }

    private void assembleResults(List<String> list, List<FileSearcher> tasks) {
	for (FileSearcher task : tasks) {
	    Collection<String> results = task.join();
	    list.addAll(results);
	}
    }
}

