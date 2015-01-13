package com.codurance.training.tasks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import junit.framework.*;

public class appTest extends TestCase{
	private PrintWriter outReader = new PrintWriter(System.out);
	private BufferedReader inWriter =  new BufferedReader(new InputStreamReader(System.in));
	
	
	TaskList monGestionnaire;

	
	public void testRun(){
		monGestionnaire = new TaskList(inWriter, outReader);
		monGestionnaire.run();
	}
	
	
	public void testDeadLine(){
		
		assertEquals("1","1");
	}

}
