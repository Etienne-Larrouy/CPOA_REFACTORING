package com.codurance.training.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ProjectList implements Runnable {
    private static final String QUIT = "quit";

    private final ArrayList<Project> projectList = new ArrayList<Project>();
    private final BufferedReader in;
    private final PrintWriter out;

    private long lastId = 0;

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        new ProjectList(in, out).run();
    }

    public ProjectList(BufferedReader reader, PrintWriter writer) {
        this.in = reader;
        this.out = writer;
    }

    public void run() {
        while (true) {
            out.print("> ");
            out.flush();
            String command;
            try {
                command = in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (command.equals(QUIT)) {
                break;
            }
            execute(command);
        }
    }

    private void execute(String commandLine) {
        String[] commandRest = commandLine.split(" ", 2);
        String command = commandRest[0];
        switch (command) {
            case "show":
                show();
                break;
            case "add":
                add(commandRest[1]);
                break;
            case "check":
                check(commandRest[1], commandRest[2]);
                break;
            case "uncheck":
                uncheck(commandRest[1], commandRest[2]);
                break;
            case "help":
                help();
                break;
            case "deadLine":
            	deadLine(commandRest[1]);
            	break;
            default:
                error(command);
                break;
        }
    }
    
    private void deadLine(String commandLine){
    	String[] subcommandRest = commandLine.split(" ", 2);

    	Task tache = this.getTask(subcommandRest[0]);
    	tache.setDeadLine(subcommandRest[1]);
    	out.printf("deadLine : %s",tache.getDeadLine());
    	out.println();
    	
    }
    
    //Refactoring fait
    private void show() {
        for (Project projet : projectList) {
            out.println(projet.getProjectName());
            for (Task task : projet.getTasks()) {
                out.printf("    [%c] %d: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
            }
            out.println();
        }
    }

    private void add(String commandLine) {
        String[] subcommandRest = commandLine.split(" ", 2);
        String subcommand = subcommandRest[0];
        if (subcommand.equals("project")) {
            addProject(subcommandRest[1]);
        } else if (subcommand.equals("task")) {
            String[] projectTask = subcommandRest[1].split(" ", 2);
            addTask(projectTask[0], projectTask[1]);
        }
    }

    //Refactoring fait
    private void addProject(String name) {
    	this.projectList.add(new Project(name));
    }
    
    //Refactoring fait
    private void addTask(String project, String description) {
    	boolean trouve = false;
    	for (Project projet : projectList) {
           if (project.equals(projet.getProjectName())){
        	   projet.addTask(description);
        	   trouve = true;
           }
    	}
        if (!trouve) {
            out.printf("Could not find a project with the name \"%s\".", project);
            out.println();
            return;
        }
    }

    //Refactoring fait
    private void check(String projectName, String idString) {
        setDone(idString, projectName, true);
    }

    //Refactoring fait
    private void uncheck(String projectName, String idString) {
        setDone(idString, projectName, false);
    }
    
    //Refactoring fait
    private Task getTask(String ID, String projectName){
    	int id = Integer.parseInt(ID);
    	for (Project projet : projectList) {
            for (Task task : projet.getTasks()) {
                if (task.getId() == id) {
                    return task;
                }
            }
    	}
        out.printf("Could not find a task with an ID of %d.", id);
        out.println();
        return null;
    }
    
    //Refactoring fait
    private void setDone(String idString, String projectName, boolean done) {
        int id = Integer.parseInt(idString);
        for (Project projet : projectList) {
            for (Task task : projet.getTasks()) {
                if (task.getId() == id) {
                    task.setDone(done);
                    return;
                }
            }
        }
        out.printf("Could not find a task with an ID of %d.", id);
        out.println();
    }

  //Refactoring fait
    private void help() {
        out.println("Commands:");
        out.println("  show");
        out.println("  add project <project name>");
        out.println("  add task <project name> <task description>");
        out.println("  deadLine <project name> <task ID> <dd/MM/yyyy>");
        out.println("  check <project name> <task ID>");
        out.println("  uncheck <project name> <task ID>");
        out.println();
    }

    //Refactoring fait
    private void error(String command) {
        out.printf("I don't know what the command \"%s\" is.", command);
        out.println();
    }

    //Refactoring fait
    private long nextId() {
        return ++lastId;
    }
}
