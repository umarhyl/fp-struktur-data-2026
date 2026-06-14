package graph;

import model.LocationNode;
import model.RouteEdge;
import tree.MinHeap;

import java.util.*;

public class Dijkstra {
    private Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    private static class DijkstraNode implements Comparable<DijkstraNode> {
        String id;
        double distance;

        public DijkstraNode(String id, double distance) {
            this.id = id;
            this.distance = distance;
        }

        @Override
        public int compareTo(DijkstraNode other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    public void findShortestPath(String sourceId, String destId) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        MinHeap<DijkstraNode> pq = new MinHeap<>();

        for (String id : graph.getNodes().keySet()) {
            distances.put(id, Double.MAX_VALUE);
        }

        distances.put(sourceId, 0.0);
        pq.insert(new DijkstraNode(sourceId, 0.0));

        while (!pq.isEmpty()) {
            DijkstraNode current = pq.extractMin();
            String currentId = current.id;

            if (currentId.equals(destId)) {
                break; // Target found
            }

            if (current.distance > distances.get(currentId)) {
                continue; // Stale node in priority queue
            }

            for (RouteEdge edge : graph.getNeighbors(currentId)) {
                if (!edge.isActive()) continue; // Skip broken roads

                String neighborId = edge.getDestinationId();
                double newDist = distances.get(currentId) + edge.getDistance();

                if (newDist < distances.get(neighborId)) {
                    distances.put(neighborId, newDist);
                    previous.put(neighborId, currentId);
                    pq.insert(new DijkstraNode(neighborId, newDist));
                }
            }
        }

        printPath(sourceId, destId, distances, previous);
    }

    private void printPath(String sourceId, String destId, Map<String, Double> distances, Map<String, String> previous) {
        if (distances.get(destId) == Double.MAX_VALUE) {
            System.out.println("Tidak ada rute yang tersedia dari " + graph.getNodes().get(sourceId).getName() + " ke " + graph.getNodes().get(destId).getName());
            return;
        }

        List<String> path = new ArrayList<>();
        String curr = destId;
        while (curr != null) {
            path.add(curr);
            curr = previous.get(curr);
        }
        Collections.reverse(path);

        System.out.println("\n=== Rute Tercepat ===");
        System.out.println("Dari: " + graph.getNodes().get(sourceId).getName());
        System.out.println("Ke: " + graph.getNodes().get(destId).getName());
        System.out.printf("Total Jarak: %.1f km%n", distances.get(destId));
        System.out.print("Jalur: ");
        
        for (int i = 0; i < path.size(); i++) {
            System.out.print(graph.getNodes().get(path.get(i)).getName());
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}
