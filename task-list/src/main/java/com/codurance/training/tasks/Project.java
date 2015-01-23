package com.codurance.training.tasks;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Project implements Observer{
	private ArrayList<Task> listTaskDone;
	private ArrayList<Task> listTaskNotDone;
	private String projectName;

	/**
	 * Constructeur sans paramètre
	 */
	public Project() {
		this.listTaskDone = new ArrayList<Task>();
		this.listTaskNotDone = new ArrayList<Task>();
		this.projectName = "";
	}
	
    /**
     * Constructeur avec paramètre
     * @param projectName
     */
    public Project(String projectName) {
    	this.listTaskDone = new ArrayList<Task>();
    	this.listTaskNotDone = new ArrayList<Task>();
		this.projectName = projectName;
	}    
    
    /**
     * Retourne toutes les tâches finies
     * @return ArrayList<Task>
     */
    public ArrayList<Task> getTasksDone(){
    	return this.listTaskDone;
    }
    
    /**
     * Retourne toutes les tâches non finies
     * @return ArrayList<Task>
     */
    public ArrayList<Task> getTasksNotDone(){
    	return this.listTaskNotDone;
    }

	/**
	 * Retourne le nom du projet
	 * @return String
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Permet d'ajouter une tâche
	 * @param description
	 */
	public void addTask(Task newTask) {
		
		listTaskNotDone.add(newTask);
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof Boolean) {
			boolean done = (Boolean)arg1;
			Task laTache = (Task)arg0;
			if(done == true){
				this.listTaskNotDone.remove(arg0);
				this.listTaskDone.add(laTache);
			}
			else{
				this.listTaskDone.remove(arg0);
				this.listTaskNotDone.add(laTache);
			}
		} else {
			System.out.println("Erreur : un booléen est requis");
		}
	}


}
