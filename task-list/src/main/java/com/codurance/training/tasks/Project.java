package com.codurance.training.tasks;

import java.util.ArrayList;

public class Project {
	private ArrayList<Task> listTask;
	private String projectName;
	private int idTask;

	/**
	 * Constructeur sans paramètre
	 */
	public Project() {
		this.listTask = new ArrayList<Task>();
		this.projectName = "";
		this.idTask = 1;
	}
	
    /**
     * Constructeur avec paramètre
     * @param projectName
     */
    public Project(String projectName) {
    	this.listTask = new ArrayList<Task>();
		this.projectName = projectName;
		this.idTask = 1;
	}    
    
    /**
     * Retourne toutes les tâches
     * @return ArrayList<Task>
     */
    public ArrayList<Task> getTasks(){
    	return this.listTask;
    }

	/**
	 * Retourne le nom du projet
	 * @return String
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * Retourne le nouvel ID de tâche
	 * @return int
	 */
	private int getNewId(){
		return this.idTask;
	}
	
	/**
	 * Incrémente le prochain ID
	 */
	private void setNewId(){
		this.idTask++;
	}

	/**
	 * Permet d'ajouter une tâche
	 * @param description
	 */
	public void addTask(String description) {
		Task myTask = new Task(getNewId(),description, false);
		
		listTask.add(myTask);
		setNewId();
    }
	
	/**
	 * Permet de supprimer la tâche passée en paramètre
	 * @param idTask
	 */
	public void deleteTask(Task task) {
		
		listTask.remove(task.getId());

    }

}
