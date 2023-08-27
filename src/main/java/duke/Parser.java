package duke;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class Parser {
    public static String parseCommandType(String response) throws DukeException {
        if (Objects.equals(response, "bye")) {
            return "bye";
        } else if (Objects.equals(response, "list")) {
            return "list";
        } else if (response.startsWith("delete")) {
            return "delete";
        } else if (response.startsWith("mark")) {
            return "mark";
        } else if (response.startsWith("todo") || response.startsWith("deadline") || response.startsWith("event")) {
            return "task";
        } else {
            throw new DukeException("Invalid command");
        }
    }
    public static int taskNumber(String response) {
        String[] array = response.split(" ");
        String lastVal = array[array.length - 1];
        return parseInt(lastVal) - 1;
    }
    public static Task newTask(String response) throws DukeException {
        String[] array = response.split(" ");
        StringBuilder title = new StringBuilder();
        String mode = "title";
        if (response.startsWith("todo")) {
            for (String command : array) {
                if (Objects.equals(command, "todo")) {
                    continue;
                }
                if (!title.isEmpty()) {
                    title.append(" ");
                }
                title.append(command);
            }
            if (!title.isEmpty()) {
                return new Todo(title.toString());
            } else {
                throw new DukeException("☹ OOPS!!! The title of a todo cannot be empty.");
            }
        } else if (response.startsWith("deadline")) {
            StringBuilder deadline = new StringBuilder();
            for (String command : array) {
                if (Objects.equals(command, "deadline")) {
                    continue;
                }
                if (Objects.equals(command, "/by")) {
                    mode = "deadline";
                    continue;
                }
                if (Objects.equals(mode, "title")) {
                    if (!title.isEmpty()) {
                        title.append(" ");
                    }
                    title.append(command);
                } else {
                    if (!deadline.isEmpty()) {
                        deadline.append(" ");
                    }
                    deadline.append(command);
                }
            }
            if (!title.isEmpty() && !deadline.isEmpty()) {
                try {
                    LocalDate fromDate = LocalDate.parse(deadline);
                    deadline = new StringBuilder(fromDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
                } catch (DateTimeException ignored) {
                }
                return new Deadline(title.toString(), deadline.toString());
            } else {
                throw new DukeException("☹ OOPS!!! The title and given deadline cannot be empty.");
            }
        } else if (response.startsWith("event")) {
            StringBuilder from = new StringBuilder();
            StringBuilder to = new StringBuilder();
            for (String command : array) {
                if (Objects.equals(command, "event")) {
                    continue;
                }
                if (Objects.equals(command, "/from")) {
                    mode = "from";
                    continue;
                }
                if (Objects.equals(command, "/to")) {
                    mode = "to";
                    continue;
                }
                if (Objects.equals(mode, "title")) {
                    if (!title.isEmpty()) {
                        title.append(" ");
                    }
                    title.append(command);
                } else if (Objects.equals(mode, "from")) {
                    if (!from.isEmpty()) {
                        from.append(" ");
                    }
                    from.append(command);
                } else {
                    if (!to.isEmpty()) {
                        to.append(" ");
                    }
                    to.append(command);
                }
            }
            if (!title.isEmpty() && !from.isEmpty() && !to.isEmpty()) {
                try {
                    LocalDate fromDate = LocalDate.parse(from);
                    from = new StringBuilder(fromDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
                } catch (DateTimeException ignored) {
                }
                try {
                    LocalDate fromDate = LocalDate.parse(to);
                    to = new StringBuilder(fromDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
                } catch (DateTimeException ignored) {
                }
                return new Event(title.toString(), from.toString(), to.toString());
            } else {
                throw new DukeException("☹ OOPS!!! The title, from and to sections cannot be empty.");
            }
        }
        return null;
    }
}
