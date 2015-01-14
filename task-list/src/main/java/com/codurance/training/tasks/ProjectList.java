package com.codurance.training.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public final class ProjectList implements Runnable {
    private static final String QUIT = "quit";

    private final ArrayList<Project> projectList = new ArrayList<Project>();
    private final BufferedReader in;
    private final PrintWriter out;
    private Calendar date;
    private SimpleDateFormat todaysDate;
    

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        new ProjectList(in, out).run();
    }

    /**
     * Constructeur avec paramètres
     * @param reader
     * @param writer
     */
    public ProjectList(BufferedReader reader, PrintWriter writer) {
        this.in = reader;
        this.out = writer;
        this.date = Calendar.getInstance();
        this.todaysDate = new SimpleDateFormat("dd/MM/yyyy");
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

    /**
     * Permet d'executer la commande reçu en paramètre 
     * @param commandLine	String récupère la commande
     */
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
                check(commandRest[1]);
                break;
            case "uncheck":
                uncheck(commandRest[1]);
                break;
            case "help":
                help();
                break;
            case "deadLine":
            	deadLine(commandRest[1]);
            	break;
            case "delete":
            	delete(commandRest[1]);
            	break;
            case "today":
            	this.today();
            	break;
            default:
                error(command);
                break;
        }
    }
    
    private void today(){
 	   for (Project projet : projectList) {
 		   out.println(projet.getProjectName());
            for (Task task : projet.getTasks()) {
         	   if(task.getDeadLine().equals(this.todaysDate.format(this.date.getTime()))){
                    out.printf("    [%s] %d: %s%n", task.getDeadLine(), task.getId(), task.getDescription());
         	   }
            }
            out.println();
        }
    }


	/**
     * Permet de créer une deadLine en fonction d'un projet et d'une tâche
     * @param commandLine	String récupère les arguments passés en entrée
     */
    private void deadLine(String commandLine){
    	String[] subcommandRest = commandLine.split(" ", 3);

    	Task tache = this.getTask(subcommandRest[1], subcommandRest[0]);
    	tache.setDeadLine(subcommandRest[2]);
    	out.printf("deadLine : %s",tache.getDeadLine());
    	out.println();
    	
    }

    /**
     * Affiche tous les projets avec leurs tâches en affichant si celles-ci sont terminées.
     */
    private void show() {
        for (Project projet : projectList) {
            out.println(projet.getProjectName());
            for (Task task : projet.getTasks()) {
                out.printf("    [%c] %d: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
            }
            out.println();
        }
    }

    /**
     * Permet d'ajouter un projet ou une tâche
     * @param commandLine
     */
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

    /**
     * Permet d'ajouter un projet
     * @param name
     */
    private void addProject(String name) {
    	this.projectList.add(new Project(name));
    }

    /**
     * Permet d'ajouter une tâche
     * @param project
     * @param description
     */
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

    /**
     * Fonction permettant d'identifier une tâche comme accomplie
     * @param commandLine
     */
    private void check(String commandLine) {
    	String[] subcommandRest = commandLine.split(" ", 2);
    	
        setDone(subcommandRest[0], subcommandRest[1], true);
    }

    /**
     * Fonction permettant d'identifier une tâche comme non accomplie
     * @param commandLine
     */
    private void uncheck(String commandLine) {
    	String[] subcommandRest = commandLine.split(" ", 2);
    	
    	setDone(subcommandRest[0], subcommandRest[1], false);
    }

    /**
     * Permet de retourner une tâche en fonction de son ID et de son projet
     * @param ID
     * @param projectName
     * @return Task
     */
    private Task getTask(String ID, String projectName){
    	int id = Integer.parseInt(ID);
    	for (Project projet : projectList) {
    		if (projectName.equals(projet.getProjectName())){
	            for (Task task : projet.getTasks()) {
	                if (task.getId() == id) {
	                    return task;
	                }
	            }
    		}
    	}
        out.printf("Could not find a task with an ID of %d.", id);
        out.println();
        return null;
    }


    /**
     * Fixe une tâche comme accomplie ou non
     * @param projectName
     * @param idString
     * @param done
     */
    private void setDone(String projectName, String idString, boolean done) {
        int id = Integer.parseInt(idString);
        for (Project projet : projectList) {
        	if (projectName.equals(projet.getProjectName())){
	            for (Task task : projet.getTasks()) {
	                if (task.getId() == id) {
	                    task.setDone(done);
	                    return;
	                }
	            }
        	}
        }
        out.printf("Could not find a task with an ID of %d.", id);
        out.println();
    }

    /**
     * Affiche l'aide des commandes
     */
    private void help() {
        out.println("Commands:");
        out.println("  show");
        out.println("  add project <project name>");
        out.println("  add task <project name> <task description>");
        out.println("  deadLine <project name> <task ID> <dd/MM/yyyy>");
        out.println("  check <project name> <task ID>");
        out.println("  uncheck <project name> <task ID>");
        out.println("  delete <project name> <task ID>");
        out.println();
    }

    /**
     * Affiche une commande invalide
     * @param command
     */
    private void error(String command) {
        out.printf("I don't know what the command \"%s\" is.", command);
        out.println();
    }
    

    /**
     * Permet de supprimer une tâche en vérifiant si le projet et la tâche existe
     * @param string
     */
    private void delete(String commandLine) {
    	 String[] subcommandRest = commandLine.split(" ", 2);
    	 int id = Integer.parseInt(subcommandRest[1]);
    	 
    	 boolean trouveProjet = false;
    	 boolean trouveTache = false;
    	 
    	 for (Project projet : projectList) {
    		 if (subcommandRest[0].equals(projet.getProjectName())){
    			 for (Task task : projet.getTasks()) {
 	                if (task.getId() == id) {
 	                	
 	                	projet.deleteTask(id);
 	                	trouveTache = true;
 	                }
    			 }
    		 }
    	 }
    	 
         if (!trouveProjet) {
        	 if(!trouveTache){
	             out.printf("Could not find a task with the id \"%s\".", subcommandRest[1]);
	             out.println();
	             return;
        	 }
        	 else{
        		 out.printf("Could not find a project with the name \"%s\".", subcommandRest[0]);
	             out.println();
	             return;
        	 }
         }

	}

}
