import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

class BubbleSort {
	public static void main(String[] args) throws IOException {
		int a, b, temp_value, counter = 0;
		String[] temporarysplit = new String(Files.readAllBytes(Paths.get(new Scanner(System.in).nextLine())))
				.split(" ");
		int[] integer = new int[temporarysplit.length];
		for (a = 0; a < temporarysplit.length; a++) {
			integer[a] = Integer.parseInt(temporarysplit[a]);
		}
		for (a = 1; a < integer.length; a++) {
			for (b = 0; b < integer.length - a; b++) {
				if (integer[b] > integer[b + 1]) {
					counter++;
					temp_value = integer[b];
					integer[b] = integer[b + 1];
					integer[b + 1] = temp_value;
				}
			}
		}
		System.out.println(counter);
		for (a = 0; a < integer.length; a++)
			if (a == integer.length - 1) {
				System.out.print(integer[a]);
			} else {
				System.out.print(integer[a] + " ");
			}
	}

}
