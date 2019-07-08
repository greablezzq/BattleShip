package battleship.utilities;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class offers utility functions related to keyboard inputs by the user. 
 */
public class Input {
	private static Scanner scanner;

	public static void initialize() {
		scanner = new Scanner(System.in);
	}

	public static String next() {
		return scanner.next();
	}

	public static int nextInt() {
		return scanner.nextInt();
	}

	public static boolean nextBoolean() {
		return scanner.nextBoolean();
	}

	public static Coordinates nextCoordinates() throws Exception {
		return Coordinates.fromString(next());
	}

	public static void waitForAnyInput() throws IOException {
		System.in.read();
	}
}
