package com.codurance.training.tasks;

import static java.lang.System.lineSeparator;
import static org.hamcrest.MatcherAssert.assertThat;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

public final class ApplicationTest {
	public static final String PROMPT = "> ";
	private PipedOutputStream inStream;
	private PrintWriter inWriter;
	private PipedInputStream outStream;
	private BufferedReader outReader;

	@Before
	public void startApplication() throws IOException {
		inStream = new PipedOutputStream();
		inWriter = new PrintWriter(inStream, true);
		outStream = new PipedInputStream();
		outReader = new BufferedReader(new InputStreamReader(outStream));
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new PipedInputStream(inStream)));
		PrintWriter out = new PrintWriter(new PipedOutputStream(outStream),
				true);
		TaskList taskList = new TaskList(in, out);
		new Thread(taskList).start();
	}

	@Test
	public void itWorks() throws IOException {
		execute("show");

		execute("add project secrets");
		execute("add task secrets Eat more donuts.");
		execute("add task secrets Destroy all humans.");

		execute("show");
		readLines("secrets", "    [ ] 1: Eat more donuts.",
				"    [ ] 2: Destroy all humans.", "");

		execute("add project training");
		execute("add task training Four Elements of Simple Design");
		execute("add task training SOLID");
		execute("add task training Coupling and Cohesion");
		execute("add task training Primitive Obsession");
		execute("add task training Outside-In TDD");
		execute("add task training Interaction-Driven Design");

		execute("check 1");
		execute("check 3");
		execute("check 5");
		execute("check 6");

		execute("show");
		readLines("secrets", "    [x] 1: Eat more donuts.",
				"    [ ] 2: Destroy all humans.", "", "training",
				"    [x] 3: Four Elements of Simple Design",
				"    [ ] 4: SOLID", "    [x] 5: Coupling and Cohesion",
				"    [x] 6: Primitive Obsession", "    [ ] 7: Outside-In TDD",
				"    [ ] 8: Interaction-Driven Design", "");

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
		assertThat(String.valueOf(buffer), is(expectedOutput));
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
