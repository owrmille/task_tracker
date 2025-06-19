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

    public void markDone() {
        this.isDone = true;
    }

    public String fromTaskToString() {
        return String.format("%-6s | %-15s | %-15s | %s",
                            (this.isDone ? "[x]" : "[ ]"),
                            this.name,
                            this.project,
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

}