package ui.console;

import java.util.Scanner;

/*
Represents an application that displays information and gets user input from
a console.
 */
public abstract class ConsoleApp {
    protected Scanner scanner;

    // Modelled on code from tellerApp shown in class example
    // (https://github.students.cs.ubc.ca/CPSC210/TellerApp)
    // Modifies: this
    // Effects: processes user input
    protected void runApplication() {
        boolean keepGoing = true;
        String command = null;
        init();
        while (keepGoing) {
            displayMenu();
            command = scanner.next().toLowerCase();
            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    // Modifies: this
    // Effects: Initializes system before entry into main user menu.
    protected void init() {
        this.scanner = new Scanner(System.in);
        this.scanner.useDelimiter("\n");
    }

    // Modifies: this
    // Effects: Processes user commands
    protected abstract void processCommand(String command);

    // Effects: displays a menu of command options to user
    protected abstract void displayMenu();
}
