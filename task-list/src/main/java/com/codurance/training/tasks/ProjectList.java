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
     * Constructeur avec param�tres
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
     * Permet d'executer la commande re�u en param�tre 
     * @param commandLine	String r�cup�re la commande
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
     * Permet de cr�er une deadLine en fonction d'un projet et d'une t�che
     * @param commandLine	String r�cup�re les arguments pass�s en entr�e
     */
    private void deadLine(String commandLine){
    	String[] subcommandRest = commandLine.split(" ", 2);

    	Task tache = this.getTask(subcommandRest[0]);
    	tache.setDeadLine(subcommandRest[1]);
    	
    }

    /**
     * Affiche tous les projets avec leurs t�ches en affichant si celles-ci sont termin�es.
     */
    private void show() {
        for (Project projet : projectList) {
            out.println(projet.getProjectName());
            for (Task task : projet.getTasks()) {
                out.printf("    [%c] %d: %s - %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription(), task.getDeadLine());
            }
            out.println();
        }
    }

    /**
     * Permet d'ajouter un projet ou une t�che
     * @param commandLine
     */
    private void add(String commandLine) {
        String[] subcommandRest = commandLine.split(" ", 2);
        String subcommand = subcommandRest[0];
        if (subcommand.equals("project")) {
            addProject(subcommandRest[1]);
        } else if (subcommand.equals("task")) {
            addTask(subcommandRest[1]);
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
     * Permet d'ajouter une t�che
     * @param project
     * @param description
     */
    private void addTask(String description) {
    	
    	taskList.add(new Task(nextId(), description, false));
    }
    
    /**
     * @param commandLine
     */
    private void link(String commandLine) {
    	String[] subcommandRest = commandLine.split(" ", 2);
    	boolean trouve = false;
    	
    	for (Project projet : projectList) {
           if (subcommandRest[0].equals(projet.getProjectName())){
        	   projet.addTask(getTask(subcommandRest[1]));
        	   trouve = true;
           }
    	}
        if (!trouve) {
            out.printf("Could not find a project with the name \"%s\".", subcommandRest[0]);
            out.println();
            return;
        }
    }

    /**
     * Fonction permettant d'identifier une t�che comme accomplie
     * @param commandLine
     */
    private void check(String commandLine) {

        setDone(commandLine, true);
    }

    /**
     * Fonction permettant d'identifier une t�che comme non accomplie
     * @param commandLine
     */
    private void uncheck(String commandLine) {
    	
    	setDone(commandLine, false);
    }

    /**
     * Fixe une t�che comme accomplie ou non
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
        out.println("  show");
        out.println("  add project <project name>");
        out.println("  add task <task description>");
        out.println("  deadLine <task ID> <dd/MM/yyyy>");
        out.println("  check <task ID>");
        out.println("  uncheck <task ID>");
        out.println("  delete <task ID>");
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
     * Permet de supprimer une t�che en v�rifiant si le projet et la t�che existe
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
     * Permet de retourner une t�che en fonction de son ID
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
     * Retourne le prochain ID
     * @return
     */
    private long nextId() {
        return ++lastId;
    }

}
