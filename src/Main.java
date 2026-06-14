import graph.Dijkstra;
import graph.Graph;
import graph.KruskalMST;
import model.LocationNode;
import model.RouteEdge;
import tree.MinHeap;
import util.ConsoleInput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.LinkedList;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BOLD = "\u001B[1m";

    private static Graph graph = new Graph();
    private static Scanner scanner = new Scanner(System.in);
    private static ConsoleInput input = new ConsoleInput(scanner);

    public static void main(String[] args) {
        loadData();
        
        while (true) {
            System.out.println("\n" + ANSI_CYAN + ANSI_BOLD + "=========================================================");
            System.out.println("      DISASTER RELIEF DISTRIBUTION NETWORK - FP 2026     ");
            System.out.println("=========================================================" + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "  [1]  " + ANSI_RESET + "Tampilkan Semua Lokasi Posko & Gudang");
            System.out.println(ANSI_YELLOW + "  [2]  " + ANSI_RESET + "Cari Lokasi Berdasarkan ID/Nama");
            System.out.println(ANSI_YELLOW + "  [3]  " + ANSI_RESET + "Tambah Data Posko & Kebutuhan");
            System.out.println(ANSI_YELLOW + "  [4]  " + ANSI_RESET + "Update Kebutuhan Logistik Lokasi");
            System.out.println(ANSI_YELLOW + "  [5]  " + ANSI_RESET + "Tampilkan Jaringan Peta Saat Ini");
            System.out.println(ANSI_YELLOW + "  [6]  " + ANSI_GREEN + "Prioritaskan Pengiriman Bantuan (Min-Heap)");
            System.out.println(ANSI_YELLOW + "  [7]  " + ANSI_GREEN + "Cari Rute Tercepat Pengiriman (Dijkstra)");
            System.out.println(ANSI_YELLOW + "  [8]  " + ANSI_GREEN + "Rancang Jaringan Distribusi Minimum (Kruskal MST)");
            System.out.println(ANSI_YELLOW + "  [9]  " + ANSI_RED + "Simulasi Jalan Rusak / Bencana Susulan");
            System.out.println(ANSI_YELLOW + " [10]  " + ANSI_RED + "Cek Konektivitas Jaringan");
            System.out.println(ANSI_YELLOW + " [11]  " + ANSI_RESET + "Keluar");
            System.out.println(ANSI_CYAN + "---------------------------------------------------------" + ANSI_RESET);
            System.out.print(ANSI_BOLD + "Pilih menu (1-11): " + ANSI_RESET);
            
            int menu = -1;
            try {
                menu = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Pilihan tidak valid. Silakan pilih menu 1-11.");
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
                    updateLogisticsNeeded();
                    break;
                case 5:
                    graph.printGraph();
                    break;
                case 6:
                    prioritizeRelief();
                    break;
                case 7:
                    findShortestRoute();
                    break;
                case 8:
                    KruskalMST mst = new KruskalMST(graph);
                    mst.printMinimumSpanningTree();
                    break;
                case 9:
                    simulateRoadStatus();
                    break;
                case 10:
                    checkNetworkConnectivity();
                    break;
                case 11:
                    System.out.println("Terima kasih telah menggunakan sistem ini.");
                    return;
                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih menu 1-11.");
            }
        }
    }

    private static void loadData() {
        System.out.println(ANSI_GREEN + "Memuat dataset buatan sendiri..." + ANSI_RESET);
        // Load Nodes
        try (BufferedReader br = new BufferedReader(new FileReader("data/nodes.csv"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
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
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split(",");
                if (data.length == 3) {
                    graph.addEdge(data[0], data[1], Double.parseDouble(data[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca edges.csv: " + e.getMessage());
        }
        System.out.println(ANSI_GREEN + "Dataset berhasil dimuat (" + graph.getNodes().size() + " node)." + ANSI_RESET);
    }

    private static void displayAllNodes() {
        System.out.println("\n" + ANSI_CYAN + ANSI_BOLD + "=== Daftar Lokasi ===" + ANSI_RESET);
        for (LocationNode node : getSortedLocations()) {
            printLocationDetail(node);
        }
    }

    private static void printLocationDetail(LocationNode node) {
        System.out.printf("%s[%s]%s %-15s | Tipe: %-6s | Populasi: %-5d | Kritis: %s%-2d%s | Risiko: %s%-2d%s | Kebutuhan: %s%.1f Ton%s%n",
                ANSI_YELLOW, node.getId(), ANSI_RESET, 
                node.getName(), 
                node.getType(), 
                node.getPopulation(),
                ANSI_RED, node.getCriticalLevel(), ANSI_RESET,
                ANSI_RED, node.getRiskLevel(), ANSI_RESET,
                ANSI_GREEN, node.getLogisticsNeeded(), ANSI_RESET);
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
        String name = input.promptNonEmptyText("Nama lokasi: ");
        String type = input.promptLocationType();
        int population = input.promptIntInRange("Jumlah penduduk/pengungsi: ", 0, Integer.MAX_VALUE);
        int criticalLevel = input.promptIntInRange("Tingkat kritis (1-5): ", 1, 5);
        double logisticsNeeded = input.promptDoubleInRange("Kebutuhan logistik (ton): ", 0.0, Double.MAX_VALUE);
        int riskLevel = input.promptIntInRange("Tingkat risiko akses (1-5): ", 1, 5);

        LocationNode node = new LocationNode(id, name, type, population, criticalLevel, logisticsNeeded, riskLevel);
        if (!appendNodeToCsv(node)) {
            System.out.println("Data lokasi tidak jadi ditambahkan karena gagal menyimpan ke nodes.csv.");
            return;
        }

        graph.addNode(node);
        System.out.println("Data lokasi berhasil ditambahkan dan disimpan ke data/nodes.csv.");

        if (input.promptYesNo("Hubungkan lokasi ini ke jaringan jalan sekarang? (y/n): ")) {
            displayLocationIds();
            String neighborId = promptValidLocationId("Masukkan ID lokasi yang terhubung: ");
            if (neighborId.equals(id)) {
                System.out.println("Lokasi baru tidak bisa dihubungkan ke dirinya sendiri.");
                return;
            }
            if (graph.hasEdge(id, neighborId)) {
                System.out.println("Jalur " + id + " - " + neighborId + " sudah ada. Duplikasi edge dibatalkan.");
                return;
            }

            double distance = input.promptDoubleInRange("Jarak/bobot jalan (km): ", 0.1, Double.MAX_VALUE);
            if (!graph.addEdge(id, neighborId, distance)) {
                return;
            }
            if (appendEdgeToCsv(id, neighborId, distance)) {
                System.out.println("Jalur " + id + " - " + neighborId + " berhasil ditambahkan dan disimpan ke data/edges.csv.");
            } else {
                System.out.println("Jalur " + id + " - " + neighborId + " berhasil ditambahkan ke sesi program, tetapi gagal disimpan ke data/edges.csv.");
            }
        }
    }

    private static void updateLogisticsNeeded() {
        System.out.println("\n=== Update Kebutuhan Logistik Lokasi ===");
        displayLocationIds();
        String id = promptValidLocationId("Masukkan ID lokasi yang akan diupdate: ");
        LocationNode node = graph.getNodes().get(id);

        printLocationDetail(node);
        double oldValue = node.getLogisticsNeeded();
        double newValue = input.promptDoubleInRange("Kebutuhan logistik baru (ton): ", 0.0, Double.MAX_VALUE);

        node.setLogisticsNeeded(newValue);
        if (saveAllNodesToCsv()) {
            System.out.printf("Kebutuhan logistik %s berhasil diupdate dari %.1f Ton menjadi %.1f Ton dan disimpan ke data/nodes.csv.%n",
                              id, oldValue, newValue);
        } else {
            node.setLogisticsNeeded(oldValue);
            System.out.println("Update dibatalkan karena gagal menyimpan perubahan ke data/nodes.csv.");
        }
    }

    private static void prioritizeRelief() {
        // Implementasi Tree (Min-Heap) 
        // Kita jadikan prioritas utama (paling darurat) sebagai nilai terkecil di MinHeap.
        // Asumsi: Tingkat Kritis 5 adalah paling darurat. Agar jadi MinHeap, kita ubah jadi (6 - Tingkat Kritis)
        // Jadi tingkat 5 jadi 1 (prioritas tertinggi/paling atas).
        
        class Task implements Comparable<Task> {
            LocationNode node;
            double emergencyScore;

            Task(LocationNode node, double emergencyScore) {
                this.node = node;
                this.emergencyScore = emergencyScore;
            }

            @Override
            public int compareTo(Task o) {
                int scoreComparison = Double.compare(o.emergencyScore, this.emergencyScore);
                if (scoreComparison != 0) {
                    return scoreComparison;
                }
                return this.node.getId().compareTo(o.node.getId());
            }
        }

        MinHeap<Task> pq = new MinHeap<>();
        for (LocationNode node : graph.getNodes().values()) {
            if (!node.getType().equalsIgnoreCase("Gudang") && node.getLogisticsNeeded() > 0) {
                pq.insert(new Task(node, calculateEmergencyScore(node)));
            }
        }

        System.out.println("\n" + ANSI_CYAN + ANSI_BOLD + "=== Prioritas Pengiriman Bantuan Teratas (Min-Heap) ===" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Rumus Skor: (Kritis x 100) + (Risiko x 40) + (Kebutuhan x 5) + (Populasi / 100)" + ANSI_RESET);
        int count = 1;
        while (!pq.isEmpty() && count <= 5) {
            Task task = pq.extractMin();
            System.out.printf(ANSI_BOLD + ANSI_RED + "%d. %-15s" + ANSI_RESET + " | Skor: %s%.1f%s | Kritis: %d | Risiko: %d | Kebutuhan: %.1f Ton | Populasi: %d%n",
                              count,
                              task.node.getName(),
                              ANSI_GREEN, task.emergencyScore, ANSI_RESET,
                              task.node.getCriticalLevel(),
                              task.node.getRiskLevel(),
                              task.node.getLogisticsNeeded(),
                              task.node.getPopulation());
            count++;
        }
    }

    private static double calculateEmergencyScore(LocationNode node) {
        return (node.getCriticalLevel() * 100.0)
                + (node.getRiskLevel() * 40.0)
                + (node.getLogisticsNeeded() * 5.0)
                + (node.getPopulation() / 100.0);
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

    private static void checkNetworkConnectivity() {
        System.out.println("\n" + ANSI_CYAN + ANSI_BOLD + "=== Cek Konektivitas Jaringan ===" + ANSI_RESET);

        Set<String> visited = new HashSet<>();
        List<List<String>> components = new ArrayList<>();

        for (LocationNode node : getSortedLocations()) {
            if (!visited.contains(node.getId())) {
                components.add(exploreActiveComponent(node.getId(), visited));
            }
        }

        if (components.size() == 1) {
            System.out.println("Status: " + ANSI_GREEN + ANSI_BOLD + "TERHUBUNG" + ANSI_RESET);
            System.out.println("Semua lokasi masih dapat dijangkau melalui jalan aktif.");
            System.out.println("Total lokasi dalam jaringan aktif: " + components.get(0).size());
            return;
        }

        System.out.println("Status: " + ANSI_RED + ANSI_BOLD + "TERPUTUS" + ANSI_RESET);
        System.out.println("Jaringan aktif terpecah menjadi " + components.size() + " komponen.");
        for (int i = 0; i < components.size(); i++) {
            System.out.println(ANSI_YELLOW + "Komponen " + (i + 1) + " (" + components.get(i).size() + " lokasi): " + ANSI_RESET + String.join(", ", components.get(i)));
        }
        System.out.println(ANSI_RED + "Dampak demo: Dijkstra mungkin tidak menemukan rute jika asal dan tujuan berada di komponen berbeda." + ANSI_RESET);
    }

    private static List<String> exploreActiveComponent(String startId, Set<String> visited) {
        List<String> component = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();

        visited.add(startId);
        queue.add(startId);

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            LocationNode currentNode = graph.getNodes().get(currentId);
            component.add(currentId + "-" + currentNode.getName());

            for (RouteEdge edge : graph.getNeighbors(currentId)) {
                if (!edge.isActive()) {
                    continue;
                }

                String neighborId = edge.getDestinationId();
                if (!visited.contains(neighborId)) {
                    visited.add(neighborId);
                    queue.add(neighborId);
                }
            }
        }

        component.sort(String::compareTo);
        return component;
    }

    private static boolean appendNodeToCsv(LocationNode node) {
        try {
            appendCsvLine("data/nodes.csv", locationToCsvLine(node));
            return true;
        } catch (IOException e) {
            System.out.println("Gagal menyimpan nodes.csv: " + e.getMessage());
            return false;
        }
    }

    private static boolean appendEdgeToCsv(String sourceId, String destId, double distance) {
        try {
            appendCsvLine("data/edges.csv", sourceId + "," + destId + "," + distance);
            return true;
        } catch (IOException e) {
            System.out.println("Gagal menyimpan edges.csv: " + e.getMessage());
            return false;
        }
    }

    private static boolean saveAllNodesToCsv() {
        File file = new File("data/nodes.csv");
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(0);
            raf.write("id,name,type,population,criticalLevel,logisticsNeeded,riskLevel".getBytes(StandardCharsets.UTF_8));
            for (LocationNode node : getSortedLocations()) {
                raf.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
                raf.write(locationToCsvLine(node).getBytes(StandardCharsets.UTF_8));
            }
            return true;
        } catch (IOException e) {
            System.out.println("Gagal menyimpan nodes.csv: " + e.getMessage());
            return false;
        }
    }

    private static String locationToCsvLine(LocationNode node) {
        return node.getId() + "," +
               node.getName() + "," +
               node.getType() + "," +
               node.getPopulation() + "," +
               node.getCriticalLevel() + "," +
               node.getLogisticsNeeded() + "," +
               node.getRiskLevel();
    }

    private static void appendCsvLine(String filePath, String line) throws IOException {
        File file = new File(filePath);
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            long length = raf.length();
            if (length > 0) {
                raf.seek(length - 1);
                int lastByte = raf.read();
                if (lastByte != '\n' && lastByte != '\r') {
                    raf.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
                }
            }

            raf.seek(raf.length());
            raf.write(line.getBytes(StandardCharsets.UTF_8));
        }
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

}
