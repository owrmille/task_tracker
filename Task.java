public class Task {
    private String name;
    private boolean isDone;
    private String project;
    private int weekNumber;

    public Task(String name, int weekNumber, String project, boolean isDone) {
        this.name = name;
        this.weekNumber = weekNumber;
        this.project = project;
        this.isDone = isDone;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public String getProject() {
        return this.project;
    }

    public int getWeekNumber() {
        return this.weekNumber;
    }

    public void markDone() {
        this.isDone = true;
    }

    public static String cutText(String s) {
        if (s.length() > 20) {
            return s.substring(0, 16).concat("...");
        }
        return s;
    }

    public String fromTaskToString(boolean needToCut) {
        String taskName = needToCut ? cutText(this.name) : this.name;
        String projectName = needToCut ? cutText(this.project) : this.project;

        return String.format("%-6s | %-20s | %-20s | %s",
                        (this.isDone ? "[x]" : "[ ]"),
                        taskName,
                        projectName,
                        this.weekNumber);

    }

    public static Task fromStringToTask(String line) {
        String[] fields = line.split(" \\| ");
        boolean isDone = fields[0].trim().equals("[x]");
        String name = fields[1].trim();
        String project = fields[2].trim();
        int weekNumber = Integer.parseInt(fields[3].trim());
        Task task = new Task(name, weekNumber, project, isDone);
        return task;
    }

    public Task copyTask() {
        return new Task(this.name, this.weekNumber, this.project, this.isDone);
    }
}