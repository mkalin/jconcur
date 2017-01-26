package fjt;
 
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.concurrent.RecursiveTask;
 
public class FileSearcher extends RecursiveTask<List<String>> {
    private final String path;
    private final String extension;
 
    public FileSearcher(String path, String extension) {
	this.path = path;
	this.extension = extension;
    }
    
    @Override
    public List<String> compute() {
	List<String> listOfFileNames = new ArrayList<String>();
	File[ ] files = new File(path).listFiles();
	if (files == null) return listOfFileNames; // base case

	List<FileSearcher> tasks = new ArrayList<FileSearcher>();
	for (File file : files) {
	    String absolutePath = file.getAbsolutePath();
	    if (file.isDirectory()) {
		FileSearcher task = new FileSearcher(absolutePath, extension);
		task.fork(); // recursive case
		tasks.add(task);
	    }
	    else if (file.getName().endsWith(extension))
		listOfFileNames.add(absolutePath);
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
class Main {
    public static void main(String[ ] args) {
	List<String> list = 
	    new FileSearcher(System.getProperty("user.home"), ".txt").compute();
	System.out.println("Files with extension .txt:");
	for (String string : list) System.out.println(string);
    }
}