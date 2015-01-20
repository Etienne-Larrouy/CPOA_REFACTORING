package com.codurance.training.tasks;

import static java.lang.System.lineSeparator;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public final class ApplicationTest {
	public static final String PROMPT = "> ";
	private PipedOutputStream inStream;
	private PrintWriter inWriter;
	private PipedInputStream outStream;
	private BufferedReader outReader;
    private SimpleDateFormat todaysDate;
    private Calendar date;

	@Before
	public void startApplication() throws IOException {
		this.date = Calendar.getInstance();
		todaysDate = new SimpleDateFormat("dd/MM/yyyy");
		inStream = new PipedOutputStream();
		inWriter = new PrintWriter(inStream, true);
		outStream = new PipedInputStream();
		outReader = new BufferedReader(new InputStreamReader(outStream));
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new PipedInputStream(inStream)));
		PrintWriter out = new PrintWriter(new PipedOutputStream(outStream),
				true);
		ProjectList projectList = new ProjectList(in, out);
		new Thread(projectList).start();
	}

	@Test
	public void itWorks() throws IOException {
		execute("show");

		execute("add project secrets");
		execute("add task secrets Eat more donuts.");
		execute("add task secrets Destroy all humans.");

		execute("show");
		readLines("secrets", "    [ ] 1: Eat more donuts. - No DeadLine",
				"    [ ] 2: Destroy all humans. - No DeadLine", "");

		execute("add project training");
		execute("add task training Four Elements of Simple Design");
		execute("add task training SOLID");
		execute("add task training Coupling and Cohesion");
		execute("add task training Primitive Obsession");
		execute("add task training Outside-In TDD");
		execute("add task training Interaction-Driven Design");

		execute("check secrets 1");
		execute("check training 1");
		execute("check training 3");
		execute("check training 4");

		execute("show");
		readLines("secrets", "    [x] 1: Eat more donuts. - No DeadLine",
				"    [ ] 2: Destroy all humans. - No DeadLine", "", "training",
				"    [x] 1: Four Elements of Simple Design - No DeadLine",
				"    [ ] 2: SOLID - No DeadLine", "    [x] 3: Coupling and Cohesion - No DeadLine",
				"    [x] 4: Primitive Obsession - No DeadLine", "    [ ] 5: Outside-In TDD - No DeadLine",
				"    [ ] 6: Interaction-Driven Design - No DeadLine", "");
		
		execute("deadLine secrets 1 12/12/1900");
		execute("deadLine training 1 12/12/1900");
		execute("deadLine training 5 "+this.todaysDate.format(this.date.getTime()));
		
		execute("show");
		readLines("secrets", "    [x] 1: Eat more donuts. - 12/12/1900",
				"    [ ] 2: Destroy all humans. - No DeadLine", "", "training",
				"    [x] 1: Four Elements of Simple Design - 12/12/1900",
				"    [ ] 2: SOLID - No DeadLine", "    [x] 3: Coupling and Cohesion - No DeadLine", 
				"    [x] 4: Primitive Obsession - No DeadLine", "    [ ] 5: Outside-In TDD - "+this.todaysDate.format(this.date.getTime()),
				"    [ ] 6: Interaction-Driven Design - No DeadLine", "");
		
		execute("today");
		readLines("secrets", "","training", "    ["+this.todaysDate.format(this.date.getTime())+"] 5: Outside-In TDD", "");
		
		execute("quit");
	}

	private void execute(String command) throws IOException {
		read(PROMPT);
		write(command);
	}

	private void read(String expectedOutput) throws IOException {
		int length = expectedOutput.length();
		char[] buffer = new char[length];
		outReader.read(buffer, 0, length);
		assertEquals(expectedOutput, String.valueOf(buffer));
	}

	private void readLines(String... expectedOutput) throws IOException {
		for (String line : expectedOutput) {
			read(line + lineSeparator());
		}
	}

	private void write(String input) {
		inWriter.println(input);
	}
}
