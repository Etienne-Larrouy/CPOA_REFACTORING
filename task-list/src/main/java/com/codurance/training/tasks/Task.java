package com.codurance.training.tasks;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public final class Task {
    private final long id;
    private final String description;
    private boolean done;
    private Calendar deadLine;
    private SimpleDateFormat laDate;
    

    public Task(long id, String description, boolean done) {
        this.id = id;
        this.description = description;
        this.done = done;
        this.deadLine = Calendar.getInstance();
        this.laDate = new SimpleDateFormat("dd/MM/yyyy");
    }

    public String getDeadLine() {
        return this.laDate.format(this.deadLine.getTime());
    }
    
    public void setDeadLine(String date) {
    	int[] intDate = new int[3];
    	String[] tabDate = date.split("/", 3);
    	
    	for(int i=0; i< tabDate.length;i++){
    		intDate[i] = Integer.parseInt(tabDate[i]);
    	}
    	
    	this.deadLine.set(intDate[2], intDate[1]-1,intDate[0] );
    	this.laDate.setCalendar(this.deadLine);
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
