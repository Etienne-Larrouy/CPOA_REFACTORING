package com.codurance.training.tasks;

import java.util.Calendar;


public final class Task {
    private final long id;
    private final String description;
    private boolean done;
    private Calendar deadLine;
    

    public Task(long id, String description, boolean done) {
        this.id = id;
        this.description = description;
        this.done = done;
        this.deadLine = Calendar.getInstance();
    }

    public String getDeadLine() {
        return deadLine.toString();
    }
    
    public void setDeadLine(String date) {
    	int[] intDate = new int[3];
    	String[] tabDate = date.split("/", 3);
    	
    	for(int i=0; i< tabDate.length;i++){
    		intDate[i] = Integer.parseInt(tabDate[i]);
    	}
    	
    	deadLine.set(intDate[0], intDate[1], intDate[2]);
        
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

    public void setDone(boolean done) {
        this.done = done;
    }
}
