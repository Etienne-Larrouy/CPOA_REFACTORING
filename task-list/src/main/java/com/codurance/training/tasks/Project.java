package com.codurance.training.tasks;

import java.util.ArrayList;

public class Project {
	private ArrayList<Task> listTask;
	private String projectName;
	private int idTask;

	public Project() {
		this.listTask = new ArrayList<Task>();
		this.projectName = "";
		this.idTask = 1;
	}
	
    public Project(String projectName) {
    	this.listTask = new ArrayList<Task>();
		this.projectName = projectName;
		this.idTask = 1;
	}    

	public String getProjectName() {
		return projectName;
	}
	
	private int getLastId(){
		return this.idTask;
	}
	
	private void setNewId(){
		this.idTask++;
	}

	public void addTask(String description) {
		Task myTask = new Task(getLastId(),description, false);
		
		listTask.add(myTask);
		setNewId();
    }

}
