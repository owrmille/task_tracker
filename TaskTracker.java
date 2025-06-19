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
                case "5" -> {
                    System.out.println("*** Showing progress bars ***");
                    showProjectProgressBar();
                    showWeekProgressBar();
                }
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
            System.out.printf((i + 1) + ".  | " + tasks.get(i).fromTaskToString());
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
        System.out.printf((idxToMark + 1) + ".  | " + tasks.get(idxToMark).fromTaskToString());
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

    public static void showProjectProgressBar() {
        Map<String, List<Task>> projectMap = new HashMap<>();
        for (Task task: tasks) {
            projectMap.computeIfAbsent(task.getProject(), newProject -> new ArrayList<>()).add(task);
        }
        System.out.println("\nProgress by project");
        for (String project : projectMap.keySet()) {
            List<Task> tasksInProject = projectMap.get(project);
            long doneCount = tasksInProject.stream().filter(Task::getIsDone).count();
            int percentage = (int) (100 * doneCount/tasksInProject.size());
            String bar = drawProgressBar(project, percentage);
            System.out.printf("- %s: %s %d%%\n", project, bar, percentage);
        }
    }

    public static String drawProgressBar(String project, int percentage) {
        StringBuilder bar = new StringBuilder();
        int filledCount = percentage / 10 * 2;
        for (int i = 0; i < 20; i++) {
            bar.append(i < filledCount ? "\u25A0" : "\u25A1");
        }
        return bar.toString();
    }

    public static void showWeekProgressBar() {
        Map<String, List<Task>> weekMap = new HashMap<>();
        for (Task task: tasks) {
            weekMap.computeIfAbsent(String.valueOf(task.getWeekNumber()), newWeek -> new ArrayList<>()).add(task);
        }
        System.out.println("\nProgress by week");
        for (String week : weekMap.keySet()) {
            List<Task> tasksInWeek = weekMap.get(week);
            long doneCount = tasksInWeek.stream().filter(Task::getIsDone).count();
            int percentage = (int) (100 * doneCount/tasksInWeek.size());
            String bar = drawProgressBar(week, percentage);
            System.out.printf("- %s: %s %d%%\n", week, bar, percentage);
        }
    }
}