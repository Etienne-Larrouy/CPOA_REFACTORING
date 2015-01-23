package com.codurance.training.tasks;

import java.util.Calendar;
import java.util.Observable;
import java.text.SimpleDateFormat;



public final class Task extends Observable implements Comparable<Task>{


	private final long id;
    private final String description;
    private boolean done;
    private boolean dateDef = false;
    private Calendar deadLine;
    private SimpleDateFormat dateDeadline;
    private Calendar date;
    private SimpleDateFormat dateCreation;
 
    public Task(long id, String description, boolean done) {
        this.id = id;
        this.description = description;
        this.done = done;
        this.deadLine = Calendar.getInstance();
        this.dateDeadline = new SimpleDateFormat("dd/MM/yyyy");
        this.date = Calendar.getInstance();
        this.dateCreation = new SimpleDateFormat("dd/MM/yyyy");
        this.date.setTime(this.date.getTime());
    }

    /**
     * Retourne un string de la date de deadLine
     */
    public String getSDeadLine() {
    	if(this.dateDef)
    		return this.dateDeadline.format(this.deadLine.getTime());
    	else
    		return "No DeadLine";
    }
    
    public Calendar getDeadLine() {
    	return this.deadLine;
    }
    
    public String getDateCreation() {
    		return this.dateCreation.format(this.date.getTime());
    }
    
    public void setDeadLine(String date) {
    	this.dateDef = true;
    	int[] intDate = new int[3];
    	String[] tabDate = date.split("/", 3);
    	
    	for(int i=0; i< tabDate.length;i++){
    		intDate[i] = Integer.parseInt(tabDate[i]);
    	}
    	
    	this.deadLine.set(intDate[2], intDate[1]-1,intDate[0] );
    	this.dateDeadline.setCalendar(this.deadLine);
    }
    
    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    /**
     * Fixe une tâche comme terminée ou non terminée
     * @param done
     */
    public void setDone(boolean done) {
    		this.done = done;
	        this.setChanged();
	        this.notifyObservers(done);
    }
    
    
    /**
     * Permet de lier une tache a un projet
     * @param obs
     */
    public void link(Project obs){
    	this.addObserver(obs);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Task o) {
        return getDeadLine().getTime().compareTo(o.getDeadLine().getTime());
    }
    
}
