import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TaskStorage {
    private static final String FILE_DIR = "./tasks/";
    private static final String BACKUP_DIR = "./backups/";
    private static final String FILE_NAME = getFileName();
    private static final String BACKUP_FILE_NAME = getBackupFileName();

    public static String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDateTime.now().format(formatter);
    }

    public static String generateFileName(String directory, int week, String date, String suffix) {
        if (suffix == null || suffix.isEmpty()) {
            return String.format("%sweek_%d.txt", directory, week);
            // return String.format("%sweek_%d_%s.txt", directory, week, date);
        } else {
            return String.format("%sweek_%d_%s_%s.txt", directory, week, date, suffix);
        }
    }

    public static String getFileName() {
        return generateFileName(FILE_DIR, TaskTracker.getCurrentWeek(), 
                    getFormattedTimestamp(), "");
    }

    public static String getBackupFileName() {
        return generateFileName(BACKUP_DIR, TaskTracker.getCurrentWeek(), 
                    getFormattedTimestamp(), "backup");
    }

    public static List<Task> loadData() {
        List<Task> tasks = new ArrayList<>();
        try {
            Files.createDirectories(Paths.get(FILE_DIR));
        } catch (IOException e) {
            TaskTracker.printText("\nFailed to create file folder!\n", TaskTracker.RED);
            return tasks;
        }
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                TaskTracker.printText("\nFailed to create a new file!\n", TaskTracker.RED);
                return tasks;
            }
            return tasks;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(Task.fromStringToTask(line));
            }
        } catch (IOException e) {
            TaskTracker.printText("\nFailed to load tasks!\n", TaskTracker.RED);
        }
        return tasks;
    }

    public static void saveData(List<Task> tasks) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for  (Task task : tasks) {
                writer.println(task.fromTaskToString(false));
            }
        } catch (IOException e) {
            TaskTracker.printText("\nFailed to save tasks!\n", TaskTracker.RED);
        }
    }

    public static void backupFile() {
        try {
            Files.createDirectories(Paths.get(BACKUP_DIR));
        } catch (IOException e) {
            TaskTracker.printText("\nFailed to create backup folder!\n", TaskTracker.RED);
            return;
        }
        try {
            Files.copy(Paths.get(FILE_NAME), Paths.get(BACKUP_FILE_NAME), 
                        StandardCopyOption.REPLACE_EXISTING);
            TaskTracker.printText("\n✅ Backup created: " + BACKUP_FILE_NAME + "\n", TaskTracker.GREEN);
        } catch (IOException e) {
            TaskTracker.printText("\nFailed to back up the task file!\n", TaskTracker.RED);
        }
    }
}