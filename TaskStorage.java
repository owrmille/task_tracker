import java.io.*;
import java.util.*;

public class TaskStorage {
    private static final String FILE_NAME = "tasks.txt";

    public static List<Task> loadData() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return tasks;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(Task.fromStringToTask(line));
            }
        } catch (IOException e) {
            System.out.println("Failed to load tasks.");
        }
        return tasks;
    }

    public static void saveData(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for  (Task task : tasks) {
                writer.println(task.fromTasktoString());
            }
        } catch (IOException e) {
            System.out.println("Failed to save tasks");
        }
    }
}