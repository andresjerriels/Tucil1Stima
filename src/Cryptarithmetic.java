import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.StringTokenizer;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

public class Cryptarithmetic {
    public static int totalTest = 0;

    // Method ini digunakan untuk membaca isi dari sebuah file .txt dan mengembalikannya sebagai sebuah string
    public static String readFileAsString(String fileName) {
        String text = "";

        try {
            text = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    // Method ini digunakan untuk melakukan tokenization terhadap string menjadi sebuah ekspresi penjumlahan matematika
    // dan mengembalikan nilai hasil penjumlahannya
    static int check(String exp) {
        exp = exp.replaceAll("\\+\n", "");
        int val = 0;
        StringTokenizer tokenizer = new StringTokenizer(exp, "\n", true);

        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken().trim();
            if (nextToken.equals("\n") || nextToken.equals("")) {
                val += Integer.parseInt(tokenizer.nextToken().trim());
            } else {
                val = Integer.parseInt(nextToken);
            }
        }
        return val;
    }

    // Method ini digunakan untuk melakukan permutasi terhadap problem cryptarithmetic dan mengembalikan solusi dalam
    // bentuk string jika ada, dan mengembalikan string kosong jika tidak ada solusi
    static String solve(String puzzle, String delimiter) {
        char cc = 0; // Current character

        // Melakukan iterasi ke dalam string dan menyimpan alfabet ke dalam cc
        for (int i = 0; i < puzzle.length(); i++) {
            if (isAlphabetic(puzzle.charAt(i))) {
                cc = puzzle.charAt(i);
                break;
            }
        }

        if (cc == 0) {
            // Jika seluruh karakter dalam string sudah diganti dengan digit angka, maka akan dilakukan pengujian
            // terhadap kombinasi angka

            totalTest++;    // Menghitung total tes yang dilakukan untuk menemukan kombinasi angka yang benar
            String[] operands = puzzle.split(delimiter);
            int op1 = check(operands[0]);
            int op2 = check(operands[1]);
            if (op1 == op2) {
                return puzzle;
            } else {
                return "";
            }
        } else {
            // Buat array of numbers [0..9] untuk menyimpan angka-angka yang sudah digunakan
            boolean[] numbers = new boolean[10];

            // Melakukan iterasi ke dalam string untuk menandai angka-angka yang sudah digunakan
            for (int i = 0; i < puzzle.length(); i++)
                if (isDigit(puzzle.charAt(i)))
                    numbers[puzzle.charAt(i) - '0'] = true;

            for (int i = 0; i < 10; i++) {
                if (!numbers[i]) {

                    // Melakukan substitusi alfabet dengan angka sampai ketemu kombinasi angka yang sesuai
                    String solution = solve(puzzle.replaceAll(String.valueOf(cc),
                            String.valueOf(i)), delimiter);

                    // Jika kombinasi angka sudah teruji benar secara matematis,
                    // kita akan cek apakah ada angka 0 di depan
                    if (!solution.isEmpty()) {
                        String[] split = solution.split("\n");
                        boolean zeroAtLeft = false;

                        for (int j = 0; j < split.length; j++){
                            split[j] = split[j].trim();
                            if (split[j].charAt(0) == '0'){
                                zeroAtLeft = true;
                                break;
                            }
                        }
                        // Jika tidak ada angka 0 di depan, solusi ditemukan dan di-return ke main
                        if (!zeroAtLeft) {
                            return solution;
                        }
                    }
                }
            }
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Cryptarithmetic Solver Program");
        System.out.println("Enter your file name with directory (ex. ../test/test1.txt): ");
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();

        String data = readFileAsString(filename);
        String[] split = data.split("\n");
        String delimiter = split[split.length - 2] + "\n";

        long startTime = System.nanoTime();
        String result = solve(data, delimiter);

        if (result.isEmpty()) {
            System.out.println("Puzzle cannot be solved");
            return;
        }

        String[] result_split = result.split("\n");
        System.out.println("\nSolution: ");

        for (int i = 0; i < result_split.length; i++) {
            if ((i == result_split.length - 3) || (i == result_split.length - 2)) {
                System.out.println(split[i] + "        " + result_split[i]);
            } else {
                System.out.println(split[i] + "         " + result_split[i]);
            }
        }

        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime);

        System.out.println("\nTotal tests: " + totalTest);
        System.out.println("Execution time: " + ((float) elapsedTime / 1000000000) + " seconds");
    }
}
