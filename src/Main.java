import graph.Dijkstra;
import graph.Graph;
import graph.KruskalMST;
import model.LocationNode;
import tree.MinHeap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Graph graph = new Graph();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();
        
        while (true) {
            System.out.println("\n================================================");
            System.out.println("  DISASTER RELIEF DISTRIBUTION NETWORK - FP 2026  ");
            System.out.println("================================================");
            System.out.println("1. Tampilkan Semua Lokasi Posko & Gudang");
            System.out.println("2. Cari Lokasi Berdasarkan ID/Nama");
            System.out.println("3. Tambah Data Posko & Kebutuhan");
            System.out.println("4. Tampilkan Jaringan Peta Saat Ini");
            System.out.println("5. Prioritaskan Pengiriman Bantuan (Min-Heap)");
            System.out.println("6. Cari Rute Tercepat Pengiriman (Dijkstra)");
            System.out.println("7. Rancang Jaringan Distribusi Minimum (Kruskal MST)");
            System.out.println("8. Simulasi Jalan Rusak / Bencana Susulan");
            System.out.println("9. Keluar");
            System.out.print("Pilih menu: ");
            
            int menu = -1;
            try {
                menu = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Pilihan tidak valid. Silakan pilih menu 1-9.");
                continue;
            }

            switch (menu) {
                case 1:
                    displayAllNodes();
                    break;
                case 2:
                    searchLocation();
                    break;
                case 3:
                    addReliefPost();
                    break;
                case 4:
                    graph.printGraph();
                    break;
                case 5:
                    prioritizeRelief();
                    break;
                case 6:
                    findShortestRoute();
                    break;
                case 7:
                    KruskalMST mst = new KruskalMST(graph);
                    mst.printMinimumSpanningTree();
                    break;
                case 8:
                    simulateRoadStatus();
                    break;
                case 9:
                    System.out.println("Terima kasih telah menggunakan sistem ini.");
                    return;
                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih menu 1-9.");
            }
        }
    }

    private static void loadData() {
        System.out.println("Memuat dataset buatan sendiri...");
        // Load Nodes
        try (BufferedReader br = new BufferedReader(new FileReader("data/nodes.csv"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    LocationNode node = new LocationNode(
                            data[0], data[1], data[2], 
                            Integer.parseInt(data[3]), 
                            Integer.parseInt(data[4]), 
                            Double.parseDouble(data[5]),
                            Integer.parseInt(data[6])
                    );
                    graph.addNode(node);
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca nodes.csv: " + e.getMessage());
        }

        // Load Edges
        try (BufferedReader br = new BufferedReader(new FileReader("data/edges.csv"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    graph.addEdge(data[0], data[1], Double.parseDouble(data[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca edges.csv: " + e.getMessage());
        }
        System.out.println("Dataset berhasil dimuat (" + graph.getNodes().size() + " node).");
    }

    private static void displayAllNodes() {
        System.out.println("\n=== Daftar Lokasi ===");
        for (LocationNode node : getSortedLocations()) {
            printLocationDetail(node);
        }
    }

    private static void printLocationDetail(LocationNode node) {
        System.out.println(node.getId() + " | " + node.getName() + " | Tipe: " + node.getType() +
                           " | Populasi: " + node.getPopulation() +
                           " | Tingkat Kritis: " + node.getCriticalLevel() +
                           " | Kebutuhan: " + node.getLogisticsNeeded() + " Ton" +
                           " | Risiko: " + node.getRiskLevel());
    }

    private static void displayLocationIds() {
        System.out.println("\nDaftar ID Lokasi:");
        for (LocationNode node : getSortedLocations()) {
            System.out.println(node.getId() + " - " + node.getName());
        }
        System.out.println();
    }

    private static List<LocationNode> getSortedLocations() {
        List<LocationNode> locations = new ArrayList<>(graph.getNodes().values());
        locations.sort(Comparator.comparing(LocationNode::getId));
        return locations;
    }

    private static void searchLocation() {
        System.out.print("Masukkan ID atau nama lokasi: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println("Kata kunci pencarian tidak boleh kosong.");
            return;
        }

        List<LocationNode> results = new ArrayList<>();
        for (LocationNode node : getSortedLocations()) {
            if (node.getId().toLowerCase().contains(keyword) || node.getName().toLowerCase().contains(keyword)) {
                results.add(node);
            }
        }

        if (results.isEmpty()) {
            System.out.println("Lokasi tidak ditemukan untuk kata kunci: " + keyword);
            return;
        }

        System.out.println("\n=== Hasil Pencarian Lokasi ===");
        for (LocationNode node : results) {
            printLocationDetail(node);
        }
    }

    private static void addReliefPost() {
        System.out.println("\n=== Tambah Data Posko & Kebutuhan ===");
        String id = promptNewLocationId();
        String name = promptNonEmptyText("Nama lokasi: ");
        String type = promptLocationType();
        int population = promptIntInRange("Jumlah penduduk/pengungsi: ", 0, Integer.MAX_VALUE);
        int criticalLevel = promptIntInRange("Tingkat kritis (1-5): ", 1, 5);
        double logisticsNeeded = promptDoubleInRange("Kebutuhan logistik (ton): ", 0.0, Double.MAX_VALUE);
        int riskLevel = promptIntInRange("Tingkat risiko akses (1-5): ", 1, 5);

        LocationNode node = new LocationNode(id, name, type, population, criticalLevel, logisticsNeeded, riskLevel);
        graph.addNode(node);
        System.out.println("Data lokasi berhasil ditambahkan ke sesi program.");

        if (promptYesNo("Hubungkan lokasi ini ke jaringan jalan sekarang? (y/n): ")) {
            displayLocationIds();
            String neighborId = promptValidLocationId("Masukkan ID lokasi yang terhubung: ");
            if (neighborId.equals(id)) {
                System.out.println("Lokasi baru tidak bisa dihubungkan ke dirinya sendiri.");
                return;
            }

            double distance = promptDoubleInRange("Jarak/bobot jalan (km): ", 0.1, Double.MAX_VALUE);
            graph.addEdge(id, neighborId, distance);
            System.out.println("Jalur " + id + " - " + neighborId + " berhasil ditambahkan.");
        }
    }

    private static void prioritizeRelief() {
        // Implementasi Tree (Min-Heap) 
        // Kita jadikan prioritas utama (paling darurat) sebagai nilai terkecil di MinHeap.
        // Asumsi: Tingkat Kritis 5 adalah paling darurat. Agar jadi MinHeap, kita ubah jadi (6 - Tingkat Kritis)
        // Jadi tingkat 5 jadi 1 (prioritas tertinggi/paling atas).
        
        class Task implements Comparable<Task> {
            LocationNode node;
            int priorityScore;

            Task(LocationNode node, int priorityScore) {
                this.node = node;
                this.priorityScore = priorityScore;
            }

            @Override
            public int compareTo(Task o) {
                return Integer.compare(this.priorityScore, o.priorityScore);
            }
        }

        MinHeap<Task> pq = new MinHeap<>();
        for (LocationNode node : graph.getNodes().values()) {
            if (!node.getType().equalsIgnoreCase("Gudang") && node.getLogisticsNeeded() > 0) {
                pq.insert(new Task(node, 6 - node.getCriticalLevel()));
            }
        }

        System.out.println("\n=== Prioritas Pengiriman Bantuan Teratas (Min-Heap) ===");
        int count = 1;
        while (!pq.isEmpty() && count <= 5) {
            Task task = pq.extractMin();
            System.out.println(count + ". " + task.node.getName() + " (Tingkat Kritis: " + task.node.getCriticalLevel() + ", Kebutuhan: " + task.node.getLogisticsNeeded() + " Ton)");
            count++;
        }
    }

    private static void findShortestRoute() {
        displayLocationIds();
        String source = promptValidLocationId("Masukkan ID lokasi asal: ");
        String dest = promptValidLocationId("Masukkan ID lokasi tujuan: ");

        Dijkstra dijkstra = new Dijkstra(graph);
        dijkstra.findShortestPath(source, dest);
    }

    private static void simulateRoadStatus() {
        displayLocationIds();
        String u = promptValidLocationId("Masukkan ID lokasi 1: ");
        String v = promptValidLocationId("Masukkan ID lokasi 2: ");

        System.out.println("Pilih status jalan:");
        System.out.println("1. Aktif");
        System.out.println("2. Rusak");

        boolean status = promptRoadStatus();

        graph.toggleEdge(u, v, status);
    }

    private static String promptValidLocationId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String locationId = scanner.nextLine().trim().toUpperCase();

            if (graph.getNodes().containsKey(locationId)) {
                return locationId;
            }

            System.out.println("ID lokasi tidak valid. Silakan masukkan ID yang ada di daftar.");
        }
    }

    private static boolean promptRoadStatus() {
        while (true) {
            System.out.print("Pilihan status: ");
            String statusChoice = scanner.nextLine().trim();

            if (statusChoice.equals("1")) {
                return true;
            }
            if (statusChoice.equals("2")) {
                return false;
            }

            System.out.println("Pilihan status tidak valid. Silakan pilih 1 untuk Aktif atau 2 untuk Rusak.");
        }
    }

    private static String promptNewLocationId() {
        while (true) {
            System.out.print("ID lokasi baru: ");
            String id = scanner.nextLine().trim().toUpperCase();

            if (id.isEmpty()) {
                System.out.println("ID lokasi tidak boleh kosong.");
                continue;
            }
            if (graph.getNodes().containsKey(id)) {
                System.out.println("ID lokasi sudah digunakan. Masukkan ID lain.");
                continue;
            }

            return id;
        }
    }

    private static String promptNonEmptyText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input tidak boleh kosong.");
        }
    }

    private static String promptLocationType() {
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

    private static int promptIntInRange(String prompt, int min, int max) {
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

    private static double promptDoubleInRange(String prompt, double min, double max) {
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

    private static boolean promptYesNo(String prompt) {
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
