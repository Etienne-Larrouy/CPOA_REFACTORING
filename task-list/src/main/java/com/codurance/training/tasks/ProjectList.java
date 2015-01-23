package com.codurance.training.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public final class ProjectList implements Runnable {
    private static final String QUIT = "quit";

    private final ArrayList<Project> projectList = new ArrayList<Project>();
    private final ArrayList<Task> taskList = new ArrayList<Task>();
    private final BufferedReader in;
    private final PrintWriter out;
    private Calendar date;
    private SimpleDateFormat todaysDate;
    private long lastId = 0;
    

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
            case "add":
                add(commandRest[1]);
                break;
            case "link":
                link(commandRest[1]);
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
            case "view":
            	this.viewBy(commandRest[1]);
            	break;
            default:
                error(command);
                break;
        }
    }
    
    /**
     * Permet d'afficher les tâches dont la deadline est aujourd'hui
     */
    private void today(){
 	   for (Project projet : projectList) {
 		   out.println(projet.getProjectName());
            for (Task task : projet.getTasksNotDone()) {
         	   if(task.getSDeadLine().equals(this.todaysDate.format(this.date.getTime()))){
                    out.printf("    [%s] %d: %s%n", task.getSDeadLine(), task.getId(), task.getDescription());
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
    	String[] subcommandRest = commandLine.split(" ", 2);

    	Task tache = this.getTask(subcommandRest[0]);
    	tache.setDeadLine(subcommandRest[1]);
    	
    }
    
    /**
     * Permet d'afficher les tâches en fonction de date, deadline ou project
     */
    private void viewBy(String commandLine) {
    	String[] subcommandRest = commandLine.split(" ", 2);
        String subcommand = subcommandRest[1];
        
        if (subcommand.equals("date")) {
            viewByDate();
            
        } else if (subcommand.equals("deadline")) {
            viewByDeadline();
            
        } else if (subcommand.equals("project")) {
        	viewByProject();
        }
    }
    
    /**
     * Affiche toutes les tâches en fonction de leur date de création
     */
    private void viewByDate() {
    	for(Task task : taskList) {
    		out.printf(" %d: %s - %s%n", task.getId(), task.getDescription(), task.getSDateCreation());
    	}
    }
    
    /**
     * Affiche toutes les tâches en fonction de leur deadLine.
     */
    private void viewByDeadline() {
        ArrayList<Task> tempList = taskList;
        
        //Supprimme les tâches n'ayant pas de deadLine
        for (int i=0 ; i< tempList.size() ; i++) {
        	if(tempList.get(i).getSDeadLine().equals("No DeadLine"))
       		 	tempList.remove(i);
       }
        
        Collections.sort(tempList);
        
        for (Task task : tempList) {
        	 out.printf(" %d: %s - %s%n", task.getId(), task.getDescription(), task.getSDeadLine());
        }
        
    }
    
    /**
     * Affiche tous les projets avec leurs tâches en affichant si elles sont terminées.
     */
    private void viewByProject() {
        for (Project projet : projectList) {
            out.println(projet.getProjectName());
            for (Task task : projet.getTasksNotDone()) {
                out.printf("    [ ] %d: %s - %s%n", task.getId(), task.getDescription(), task.getSDeadLine());
            }
            for (Task task : projet.getTasksDone()) {
                out.printf("    [x] %d: %s - %s%n", task.getId(), task.getDescription(), task.getSDeadLine());
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
        
        if(subcommandRest.length < 2){
        	out.printf("Not enough arguments ...");
        	out.println();
        }else {
	        String subcommand = subcommandRest[0];
	        if (subcommand.equals("project")) {
	            addProject(subcommandRest[1]);
	        } else if (subcommand.equals("task")) {
	        	String[] projectTask = subcommandRest[1].split(" ", 2);
	        	
	        	if(projectTask.length >= 2)
	        		addTask(projectTask[0], projectTask[1]);
	        	else{
	        		out.printf("Not enough arguments ...");
	        		out.println();
	        	}
	        }
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
        	   
        	   Task myTask = new Task(nextId(), description, false);
        	   taskList.add(myTask);
        	   projet.addTask(myTask);
        	   trouve = true;
        	   myTask.link(projet);
           }
    	}
        if (!trouve) {
            out.printf("Could not find a project with the name \"%s\".", project);
            out.println();
            return;
        }
    }
    
    /**
     * Permet de lier une tâche existante à un projet
     * @param commandLine
     */
    private void link(String commandLine) {
    	String[] subcommandRest = commandLine.split(" ", 2);
    	boolean trouve = false;
    	
    	for (Project projet : projectList) {
           if (subcommandRest[0].equals(projet.getProjectName())){
        	   projet.addTask(getTask(subcommandRest[1]));
        	   trouve = true;
        	   getTask(subcommandRest[1]).link(projet);
           }
    	}
        if (!trouve) {
            out.printf("Could not find a project with the name \"%s\".", subcommandRest[0]);
            out.println();
            return;
        }
    }

    /**
     * Fonction permettant d'identifier une tâche comme accomplie
     * @param commandLine
     */
    private void check(String commandLine) {

        setDone(commandLine, true);
    }

    /**
     * Fonction permettant d'identifier une tâche comme non accomplie
     * @param commandLine
     */
    private void uncheck(String commandLine) {
    	
    	setDone(commandLine, false);
    }

    /**
     * Fixe une tâche comme accomplie ou non
     * @param projectName
     * @param idString
     * @param done
     */
    private void setDone(String idString, boolean done) {
        int id = Integer.parseInt(idString);
        for (Task task : taskList) {
        	if (id == task.getId()){
        		task.setDone(done);
	            return;
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
        out.println("  add project <project name>");
        out.println("  add task <project name> <task description>");
        out.println("  link <project name> <task description>");
        out.println("  deadLine <task ID> <dd/MM/yyyy>");
        out.println("  check <task ID>");
        out.println("  uncheck <task ID>");
        out.println("  delete <task ID>");
        out.println("  view by date");
        out.println("  view by deadline");
        out.println("  view by project");
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
    	 int id = Integer.parseInt(commandLine);
    	 
    	 boolean trouveTache = false;
    	 
    	 for (Task task : taskList) {
    		 if (task.getId() == id) {
 	                	
 	              taskList.remove(task);
 	              trouveTache = true;
 	         }

    	 }
    	 
         if (!trouveTache) {
        	 out.printf("Could not find a task with the id \"%s\".", id);
        	 out.println();
        	 return;
        	 
         }

	}
    
    /**
     * Permet de retourner une tâche en fonction de son ID
     * @param ID
     * @return
     */
    private Task getTask(String ID) {
    	int myId = Integer.parseInt(ID);
    	Task myTask = null;
    	
    	for(Task task : taskList){
    		if(task.getId() == myId){
    			myTask = task;
    		}
    	}
    	
        return myTask;
    }
    
    /**
     * Retourne le prochain ID de tâche
     * @return
     */
    private long nextId() {
        return ++lastId;
    }
    
    
}
