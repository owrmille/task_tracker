import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TaskTracker {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String CYAN = "\033[0;36m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD = "\033[1;34m";
    
    private static final LocalDate BEGIN_DATE = LocalDate.of(2025, 6, 9);
    private static final int CURRENT_WEEK = getCurrentWeek();
    private static List<Task> tasks = TaskStorage.loadData();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            TaskStorage.backupFile();
            while (true) {
                printMenu();
                String option = scanner.nextLine();

                switch (option) {
                    case "1" -> {
                        printText("\nAdding a New Task...\n", CYAN);
                        addTask();
                    }
                    case "2" -> {
                        printText("\nShowing List of Tasks...\n", CYAN);
                        showTasksOfThisWeek();
                    }
                    case "3" -> {
                        printText("\nMarking Task as Done...\n", CYAN);
                        markTaskAsDone();
                    }
                    case "4" -> {
                        printText("\nDeleting Task...\n", CYAN);
                        deleteTask();
                    }
                    case "5" -> {
                        printText("\nDuplicating Task...\n", CYAN);
                        duplicateTask();
                    }
                    case "6" -> {
                        printText("\nShowing This Week Progress...\n", CYAN);
                        showProjectProgressBar(CURRENT_WEEK);
                    }
                    // lost its meaning after some changes in TaskStorage -> 
                    // TO DO: find a way to display general progress using several input files
                    // case "6" -> {
                    //     printText("\nShowing General Progress...(currently unavailable)\n", CYAN);
                    //     showProjectProgressBar();
                    //     showWeekProgressBar();
                    // }
                    case "7" -> { 
                        TaskStorage.saveData(tasks);
                        printText("\nExiting...\n", RED);
                        scanner.close();
                        return;
                    }
                    default -> {
                        printText("\nInvalid option: use numbers 1-7!\n", RED);
                    }
                    
                }
            } 
        } catch (Exception e) {
            TaskStorage.saveData(tasks);
            scanner.close();
            printText("\n❗ ERROR OCCURED ❗\n", RED);  // TO DO: specify each case and delete this one
        }
    }

    public static void printMenu() {
        printDots();
        printText("\n" + ". ".repeat(6) + " T A S K   T R A C K E R   M E N U " 
                    + ". ".repeat(6) + "\n", BLUE_BOLD);
        printDots();
        printText("1️⃣  Add Task\n", GREEN);
        printText("2️⃣  Show Tasks\n", GREEN);
        printText("3️⃣  Mark Task as Done\n", GREEN);
        printText("4️⃣  Delete Task\n", GREEN);
        printText("5️⃣  Duplicate Task\n", GREEN);
        printText("6️⃣  Show This Week Progress\n", GREEN);
        // printText("6️⃣  Show General Progress (currently unavailable)\n", RED);
        printText("7️⃣  ❌ Exit\n", RED);

        printText("\n👉 Your choice: ", BLACK);
    }

    public static void printText(String text, String color) {
        System.out.print(color + text + RESET);
    }

    public static void printDots() {
        printText("\n" + ". ".repeat(30) + "\n", BLUE_BOLD);
    }
    
    public static void addTask() {
        printText("\nEnter task name: ", BLACK);
        String name = scanner.nextLine();
        printText("Enter project: ", BLACK);
        String project = scanner.nextLine();
        printText("Is this task finished? (y/n): ", BLACK);
        boolean isDone = scanner.nextLine().equals("y");
        Task task = new Task(name, CURRENT_WEEK, project, isDone);
        tasks.add(task);
        TaskStorage.saveData(tasks);
    }

    public static boolean isTasksEmpty() {
        if (tasks.isEmpty()) {
            printText("\nEmpty! There are no tasks.\n", RED);
            return true;
        }
        return false;
    }

    public static void showTasksOfThisWeek() {
        if (isTasksEmpty()) return;
        int size = tasks.size();
        printText(String.format("%-6s | %-6s | %-20s | %-20s | %s", 
                        "No.", "Done", "Task", "Project", "Week No."), BLACK);
        printText("\n" + "-".repeat(71) + "\n", BLACK);
        for (int i = 0; i < size; i++) {
            if (tasks.get(i).getWeekNumber() == CURRENT_WEEK) {
                printText(String.format("%-6s | %s\n", 
                            i + 1, tasks.get(i).fromTaskToString(true)), BLACK);
            }
        }
    }

    public static LocalDate getToday() {
        return LocalDate.now();
    }

    public static int getCurrentWeek() {
        int diff = (int) ChronoUnit.WEEKS.between(BEGIN_DATE, getToday());
        return diff + 1;
    }

    public static void markTaskAsDone() {
        if (isTasksEmpty()) return;
        showTasksOfThisWeek();
        printText("\nEnter No. of the task to mark done (or -1 to exit input): ", BLACK);
        try {
            int idxToMark = Integer.parseInt(scanner.nextLine()) - 1;
            if (tasks.get(idxToMark).getWeekNumber() != CURRENT_WEEK) {
                printText("\nInvalid number: choose tasks only from the current week!\n", RED);
                return;
            }
            tasks.get(idxToMark).markDone();
            printText("\nUpdating task..\n", CYAN);
            printText(String.format("%-6s | %s\n", 
                    idxToMark + 1, tasks.get(idxToMark).fromTaskToString(true)), BLACK);  
            TaskStorage.saveData(tasks);
        } catch (NumberFormatException ne) {
            printText("\nInvalid input: use numbers!\n", RED);
        } catch (IndexOutOfBoundsException ie) {
            printText("\nInvalid input: use valid values for index!\n", RED);
        }
    }

    public static void deleteTask() {
        if (isTasksEmpty()) return;
        showTasksOfThisWeek();
        printText("\nEnter No. of the task to delete (or -1 to exit input): ", BLACK);
        try {
            int idxToMark = Integer.parseInt(scanner.nextLine()) - 1;
            if (tasks.get(idxToMark).getWeekNumber() != CURRENT_WEEK) {
                printText("\nInvalid number: choose tasks only from the current week!\n", RED);
                return;
            }
            printText("Do you really want to delete task No. " + (idxToMark+1) + "? (y/n): ", RED);
            boolean isConfirmed = scanner.nextLine().equals("y");
            if (isConfirmed) {
                tasks.remove(idxToMark);
                printText("\nDeleting task No. " + (idxToMark+1) + "...\n", RED);
            } else {
                printText("\nCancelling... Task No. " + (idxToMark+1) + " was not deleted.\n", CYAN);
            }
            TaskStorage.saveData(tasks);
        } catch (NumberFormatException ne) {
            printText("\nInvalid input: use numbers!\n", RED);
        } catch (IndexOutOfBoundsException ie) {
            printText("\nInvalid input: use valid values for index!\n", RED);
        }
    }

    public static void duplicateTask() {
        if (isTasksEmpty()) return;
        printText("\nEnter No. of the task to duplicate (or -1 to exit input): ", BLACK);
        try {
            int idxToMark = Integer.parseInt(scanner.nextLine()) - 1;
            if (tasks.get(idxToMark).getWeekNumber() != CURRENT_WEEK) {
                printText("\nInvalid number: choose tasks only from the current week!\n", RED);
                return;
            }
            tasks.add(tasks.get(idxToMark).copyTask());
            printText("\nDuplicating task No. " + (idxToMark+1) + "...\n", CYAN);
            TaskStorage.saveData(tasks);
        } catch (NumberFormatException ne) {
            printText("\nInvalid input: use numbers!\n", RED);
        } catch (IndexOutOfBoundsException ie) {
            printText("\nInvalid input: use valid values for index!\n", RED);
        }
    }

    public static void showProjectProgressBar() {
        showProjectProgressBar(-1);
    }

    public static void showProjectProgressBar(int week) {
        if (isTasksEmpty()) return;
        Map<String, List<Task>> projectMap = new HashMap<>();
        if (week != -1) {
            printText("\nWeek " + week + ": ", YELLOW_BOLD);
        }
        for (Task task: tasks) {
            if (week == -1 || task.getWeekNumber() == week) {
                projectMap.computeIfAbsent(task.getProject(), 
                    newProject -> new ArrayList<>()).add(task);
            }
        }
        printText("\nProgress by Project\n", GREEN);
        for (String project : projectMap.keySet()) {
            List<Task> tasksInProject = projectMap.get(project);
            long doneCount = tasksInProject.stream().filter(Task::getIsDone).count();
            int percentage = (int) (100 * doneCount/tasksInProject.size());
            String bar = drawProgressBar(project, percentage);
            printText(String.format("\n- %-20s: %s %d%%\n", Task.cutText(project), bar, percentage), BLACK);
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

    // lost its meaning after some changes in TaskStorage -> 
    // TO DO: find a way to display general progress using several input files
//     public static void showWeekProgressBar() {
//         if (isTasksEmpty()) return;
//         Map<String, List<Task>> weekMap = new HashMap<>();
//         for (Task task: tasks) {
//             weekMap.computeIfAbsent(String.valueOf(task.getWeekNumber()), 
//                                     newWeek -> new ArrayList<>()).add(task);
//         }
//         printText("\nProgress by Week\n", GREEN);
//         for (String week : weekMap.keySet()) {
//             List<Task> tasksInWeek = weekMap.get(week);
//             long doneCount = tasksInWeek.stream().filter(Task::getIsDone).count();
//             int percentage = (int) (100 * doneCount/tasksInWeek.size());
//             String bar = drawProgressBar(week, percentage);

//             printText(String.format("\n- %-6s: %s %d%%\n", week, bar, percentage), BLACK);
//         }
//     }
}