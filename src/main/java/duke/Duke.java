package duke;

import java.util.ArrayList;
import java.util.Objects;

public class Duke {
    private Storage storage;
    private TaskList tasks;

    public Duke(String filePath) {
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            Ui.showLoadingError();
            tasks = new TaskList(new ArrayList<Task>());
        }
    }

    public void run() {
        Ui.showWelcome();
        boolean isExit = false;
        try {
            while (!isExit) {
                String fullCommand = Ui.readCommand();
                String commandType = Parser.parseCommandType(fullCommand);
                switch (Objects.requireNonNull(commandType)) {
                case "bye" -> isExit = true;
                case "list" -> Ui.listTasks(tasks.getTasks());
                case "delete" -> {
                    tasks.deleteTask(Parser.taskNumber(fullCommand));
                    Ui.listTasks(tasks.getTasks());
                    Storage.writeToDisk(tasks.getTasks());
                }
                case "mark" -> {
                    tasks.markTask(Parser.taskNumber(fullCommand));
                    Ui.listTasks(tasks.getTasks());
                    Storage.writeToDisk(tasks.getTasks());
                }
                case "task" -> {
                    tasks.addTask(Parser.newTask(fullCommand));
                    Ui.listTasks(tasks.getTasks());
                    Storage.writeToDisk(tasks.getTasks());
                }
                case "find" -> {
                    Ui.showFoundTasks(tasks.findTasks(Parser.findKeyword(fullCommand)));
                }
                default -> throw new DukeException("Invalid task, uncaught by duke.Parser.");
                }
            }
        } catch (DukeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Duke("data").run();
    }
}
