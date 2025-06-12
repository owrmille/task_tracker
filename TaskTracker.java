import java.util.*;

public class TaskTracker {
    private static List<Task> tasks = TaskStorage.loadData();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println();
            System.out.println("======TASK TRACKER======");
            System.out.println();
            System.out.println("Choose one option:");
            System.out.println("1. Add Task");
            System.out.println("2. Show tasks");
            System.out.println("3. Mark task as done");
            System.out.println("4. Delete task");
            System.out.println("5. Show progress bars");
            System.out.println("6. Exit");
            System.out.println();

            String option = scanner.nextLine();

            switch (option) {
                case "1" -> {
                    System.out.println("*** Adding a new task... ***");
                    System.out.println();
                    addTask();
                }
                case "2" -> {
                    System.out.println("*** List of Tasks ***");
                    System.out.println();
                    showTasks();
                }
                case "3" -> {
                    System.out.println("*** Marking task as done ***");
                    markTaskDone();
                }
                case "4" -> {
                    System.out.println("*** Deleting task ***");
                    deleteTask();
                }
                // TO DO:
                // case "5" -> {
                //     System.out.println("*** Showing progress bars ***");
                //     showProgressBars();
                // }
                case "6" -> { 
                    TaskStorage.saveData(tasks);
                    System.out.println("*** Exiting... ***");
                    System.out.println();
                    scanner.close();
                    return;
                }
                default -> {
                    System.out.println("Invalid option.");
                    System.out.println();
                }
                
            }
        }
    }
    
    public static void addTask() {
        System.out.println("Enter task name:");
        String name = scanner.nextLine();
        System.out.println("Enter project:");
        String project = scanner.nextLine();
        System.out.println("Enter a week number:");
        int weekNumber = Integer.parseInt(scanner.nextLine());
        System.out.println("Is this task finished? (y/n):");
        boolean isDone = scanner.nextLine().equals("y");
        Task task = new Task(name, weekNumber, project, isDone);
        tasks.add(task);
        // TaskStorage.saveData(tasks);
    }

    public static void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("...EMPTY...");
            System.out.println();
            return;
        }
        int size = tasks.size();
        System.out.printf("%-2s | %-6s | %-15s | %-15s | %s", 
                        "No.", "Done", "Task", "Project", "Week No.");
        System.out.println();
        System.out.println("-------------------------------------------------------------");
        for (int i = 0; i < size; i++) {
            System.out.printf((i + 1) + ".  | " + tasks.get(i).fromTasktoString());
            System.out.println();
        }
        System.out.println();
    }

    public static void markTaskDone() {
        showTasks();
        System.out.println("Enter No. of the task to mark done:");
        int idxToMark = Integer.parseInt(scanner.nextLine()) - 1;
        tasks.get(idxToMark).markDone();
        System.out.println("UPDATED TASK:");
        System.out.printf((idxToMark + 1) + ".  | " + tasks.get(idxToMark).fromTasktoString());
        System.out.println();
        // TaskStorage.saveData(tasks);
    }

    public static void deleteTask() {
        showTasks();
        System.out.println("Enter No. of the task to delete:");
        int idxToMark = Integer.parseInt(scanner.nextLine()) - 1;
        tasks.remove(idxToMark);
        System.out.println("Task No. " + idxToMark + " was deleted");
        System.out.println();
        // TaskStorage.saveData(tasks);
    }
}