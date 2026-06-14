package util;

import java.util.Scanner;

public class ConsoleInput {
    private Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String promptNonEmptyText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                if (value.contains(",")) {
                    System.out.println("Input tidak boleh mengandung koma karena akan disimpan ke CSV.");
                    continue;
                }
                return value;
            }

            System.out.println("Input tidak boleh kosong.");
        }
    }

    public String promptLocationType() {
        while (true) {
            System.out.println("Pilih tipe lokasi:");
            System.out.println("1. Gudang");
            System.out.println("2. Posko");
            System.out.println("3. Desa");
            System.out.print("Pilihan tipe: ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) return "Gudang";
            if (choice.equals("2")) return "Posko";
            if (choice.equals("3")) return "Desa";

            System.out.println("Pilihan tipe tidak valid. Silakan pilih 1-3.");
        }
    }

    public int promptIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to validation message
            }

            if (max == Integer.MAX_VALUE) {
                System.out.println("Input harus berupa angka minimal " + min + ".");
            } else {
                System.out.println("Input harus berupa angka antara " + min + " dan " + max + ".");
            }
        }
    }

    public double promptDoubleInRange(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to validation message
            }

            if (max == Double.MAX_VALUE) {
                System.out.println("Input harus berupa angka minimal " + min + ".");
            } else {
                System.out.println("Input harus berupa angka antara " + min + " dan " + max + ".");
            }
        }
    }

    public boolean promptYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("y") || answer.equals("ya")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("tidak")) {
                return false;
            }

            System.out.println("Pilihan tidak valid. Masukkan y atau n.");
        }
    }
}
