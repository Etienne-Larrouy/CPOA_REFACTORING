package com.codurance.training.tasks;

import java.util.ArrayList;

public class Project {
	private ArrayList<Task> listTask;
	private String projectName;
	private int idTask;

	/**
	 * Constructeur sans param�tre
	 */
	public Project() {
		this.listTask = new ArrayList<Task>();
		this.projectName = "";
		this.idTask = 1;
	}
	
    /**
     * Constructeur avec param�tre
     * @param projectName
     */
    public Project(String projectName) {
    	this.listTask = new ArrayList<Task>();
		this.projectName = projectName;
		this.idTask = 1;
	}    
    
    /**
     * Retourne toutes les t�ches
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
	 * Retourne le nouvel ID de t�che
	 * @return int
	 */
	private int getNewId(){
		return this.idTask;
	}
	
	/**
	 * Incr�mente le prochain ID
	 */
	private void setNewId(){
		this.idTask++;
	}

	/**
	 * Permet d'ajouter une t�che
	 * @param description
	 */
	public void addTask(String description) {
		Task myTask = new Task(getNewId(),description, false);
		
		listTask.add(myTask);
		setNewId();
    }
	
	/**
	 * Permet de supprimer la t�che pass�e en param�tre
	 * @param idTask
	 */
	public void deleteTask(Task task) {
		
		listTask.remove(task.getId());

    }

}
