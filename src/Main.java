import graph.Dijkstra;
import graph.Graph;
import graph.KruskalMST;
import model.LocationNode;
import tree.MinHeap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
            System.out.println("2. Tampilkan Jaringan Peta Saat Ini");
            System.out.println("3. Prioritaskan Pengiriman Bantuan (Min-Heap)");
            System.out.println("4. Cari Rute Tercepat Pengiriman (Dijkstra)");
            System.out.println("5. Rancang Jaringan Distribusi Minimum (Kruskal MST)");
            System.out.println("6. Simulasi Jalan Rusak / Bencana Susulan");
            System.out.println("7. Keluar");
            System.out.print("Pilih menu: ");
            
            int menu = -1;
            try {
                menu = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid.");
                continue;
            }

            switch (menu) {
                case 1:
                    displayAllNodes();
                    break;
                case 2:
                    graph.printGraph();
                    break;
                case 3:
                    prioritizeRelief();
                    break;
                case 4:
                    findShortestRoute();
                    break;
                case 5:
                    KruskalMST mst = new KruskalMST(graph);
                    mst.printMinimumSpanningTree();
                    break;
                case 6:
                    simulateRoadStatus();
                    break;
                case 7:
                    System.out.println("Terima kasih telah menggunakan sistem ini.");
                    return;
                default:
                    System.out.println("Pilihan tidak ada.");
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
                if (data.length == 6) {
                    LocationNode node = new LocationNode(
                            data[0], data[1], data[2], 
                            Integer.parseInt(data[3]), 
                            Integer.parseInt(data[4]), 
                            Double.parseDouble(data[5])
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
        for (LocationNode node : graph.getNodes().values()) {
            System.out.println(node.getId() + " | " + node.getName() + " | Tipe: " + node.getType() + 
                               " | Tingkat Kritis: " + node.getCriticalLevel() + 
                               " | Kebutuhan: " + node.getLogisticsNeeded() + " Ton");
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
        System.out.print("Masukkan ID Lokasi Asal (contoh: G01): ");
        String source = scanner.nextLine().toUpperCase();
        System.out.print("Masukkan ID Lokasi Tujuan (contoh: P05): ");
        String dest = scanner.nextLine().toUpperCase();

        if (graph.getNodes().containsKey(source) && graph.getNodes().containsKey(dest)) {
            Dijkstra dijkstra = new Dijkstra(graph);
            dijkstra.findShortestPath(source, dest);
        } else {
            System.out.println("ID Asal atau Tujuan tidak valid.");
        }
    }

    private static void simulateRoadStatus() {
        System.out.print("Masukkan ID Lokasi 1: ");
        String u = scanner.nextLine().toUpperCase();
        System.out.print("Masukkan ID Lokasi 2: ");
        String v = scanner.nextLine().toUpperCase();
        System.out.print("Apakah jalan aktif? (true/false): ");
        boolean status = Boolean.parseBoolean(scanner.nextLine());

        graph.toggleEdge(u, v, status);
    }
}
