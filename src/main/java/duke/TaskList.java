package duke;

import java.util.ArrayList;

/**
 * Class that contains all the tasks.
 */
public class TaskList {
    ArrayList<Task> tasks;
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Deletes the task with index taskNumber.
     * @param taskNumber the index of task to remove.
     * @throws DukeException if given task does not exist.
     */
    public void deleteTask(int taskNumber) throws DukeException {
        if (taskNumber < tasks.size()) {
            Ui.showMessage("Noted. I've removed this task:");
            System.out.println(tasks.get(taskNumber).toString());
            tasks.remove(taskNumber);
        } else {
            throw new DukeException("☹ OOPS!!! The delete command needs to be followed by an existing task number.");
        }
    }

    /**
     * Completes the task with index taskNumber.
     * @param taskNumber the index of task to complete.
     * @throws DukeException if given task does not exist.
     */
    public void markTask(int taskNumber) throws DukeException {
        // Assumption: "mark" is not allowed as a task name & you can mark already done tasks.
        // Issue: "mark" by itself crashes.
        if (taskNumber < tasks.size()) {
            tasks.get(taskNumber).completeTask();
            Ui.showMessage("Nice! I've marked this task as done:");
            Ui.showMessage(tasks.get(taskNumber).toString());
        } else {
            throw new DukeException("☹ OOPS!!! The mark command needs to be followed by an existing task number.");
        }
    }

    /**
     * Adds the task newTask.
     * @param newTask task to add.
     */
    public void addTask(Task newTask) {
        this.tasks.add(newTask);
    }
}
