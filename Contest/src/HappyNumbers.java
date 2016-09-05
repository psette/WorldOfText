import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

class HappyNumbers {
	public static void main(String[] args) throws IOException {
		int a, b, c, temp_value, sum, counter = 0;
		StringBuilder tryrepeat = new StringBuilder("");
		String attempt = "";
		int[] values_square = new int[7];
		String[] temporarysplit = new String(Files.readAllBytes(Paths.get(new Scanner(System.in).nextLine())))
				.split(" ");
		int[] integer = new int[temporarysplit.length];
		for (a = 0; a < temporarysplit.length; a++) {
			integer[0] = Integer.parseInt(temporarysplit[a]);
			while (tryrepeat.indexOf(attempt) == -1) {
				counter++;
				attempt = attempt + integer[a] + "";
				b = 0;
				sum = 0;
				while (integer[0] > 0) {
					integer[b+1]=0;
					integer[b] = integer[0] % 10;
					integer[0] = integer[0] / 10;
					b++;
				}
				for (c = 0; c <= b; c++) {
					if(integer[b]!=)
					sum = (int) (sum + Math.pow(integer[b], 2));
				}
			}
			if(integer[a]==1){
			System.out.println("happy "+counter );
			}
			else{System.out.println("unhappy "+counter );}
			}

		}
	}

}
