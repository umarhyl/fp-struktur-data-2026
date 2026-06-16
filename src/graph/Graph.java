package graph;

import model.LocationNode;
import model.RouteEdge;

import java.util.*;

public class Graph {
    private Map<String, LocationNode> nodes;
    private Map<String, List<RouteEdge>> adjList;

    public Graph() {
        nodes = new HashMap<>();
        adjList = new HashMap<>();
    }

    public void addNode(LocationNode node) {
        nodes.put(node.getId(), node);
        adjList.putIfAbsent(node.getId(), new ArrayList<>());
    }

    public boolean addEdge(String sourceId, String destId, double distance) {
        if (!nodes.containsKey(sourceId) || !nodes.containsKey(destId)) {
            System.out.println("Error: Salah satu node tidak ditemukan.");
            return false;
        }

        if (hasEdge(sourceId, destId)) {
            System.out.println("Jalur " + sourceId + " - " + destId + " sudah ada. Duplikasi edge dibatalkan.");
            return false;
        }
        
        // Undirected graph for distribution routes
        adjList.get(sourceId).add(new RouteEdge(sourceId, destId, distance));
        adjList.get(destId).add(new RouteEdge(destId, sourceId, distance));
        return true;
    }

    public boolean hasEdge(String sourceId, String destId) {
        for (RouteEdge edge : adjList.getOrDefault(sourceId, new ArrayList<>())) {
            if (edge.getDestinationId().equals(destId)) {
                return true;
            }
        }
        return false;
    }

    public void toggleEdge(String sourceId, String destId, boolean activeStatus) {
        boolean found = false;
        for (RouteEdge edge : adjList.getOrDefault(sourceId, new ArrayList<>())) {
            if (edge.getDestinationId().equals(destId)) {
                edge.setActive(activeStatus);
                found = true;
            }
        }
        for (RouteEdge edge : adjList.getOrDefault(destId, new ArrayList<>())) {
            if (edge.getDestinationId().equals(sourceId)) {
                edge.setActive(activeStatus);
                found = true;
            }
        }
        if (found) {
            System.out.println("Jalur " + sourceId + " - " + destId + " statusnya menjadi: " + (activeStatus ? "AKTIF" : "RUSAK/TERTUTUP"));
        } else {
            System.out.println("Jalur tidak ditemukan.");
        }
    }

    public Map<String, LocationNode> getNodes() {
        return nodes;
    }

    public List<RouteEdge> getNeighbors(String nodeId) {
        return adjList.getOrDefault(nodeId, new ArrayList<>());
    }
    
    public Map<String, List<RouteEdge>> getAdjList() {
        return adjList;
    }

    public void printGraph() {
        System.out.println("\n\u001B[36m\u001B[1m=== Peta Jaringan Distribusi Saat Ini ===\u001B[0m");
        
        // Urutkan ID agar tampilannya rapi
        List<String> sortedIds = new ArrayList<>(adjList.keySet());
        Collections.sort(sortedIds);

        for (String id : sortedIds) {
            LocationNode node = nodes.get(id);
            System.out.printf("\u001B[33m\u001B[1m[%s]\u001B[0m %s (%s)\n", id, node.getName(), node.getType());
            
            List<RouteEdge> edges = adjList.get(id);
            if (edges == null || edges.isEmpty()) {
                System.out.println("    \u001B[31m(Tidak terhubung ke mana pun)\u001B[0m");
            } else {
                for (int i = 0; i < edges.size(); i++) {
                    RouteEdge edge = edges.get(i);
                    LocationNode dest = nodes.get(edge.getDestinationId());
                    String connector = (i == edges.size() - 1) ? "└──" : "├──";
                    
                    if (edge.isActive()) {
                        System.out.printf("    %s \u001B[32m%s\u001B[0m [%s] -> \u001B[36m%.1f km\u001B[0m\n", 
                                connector, dest.getName(), dest.getId(), edge.getDistance());
                    } else {
                        System.out.printf("    %s \u001B[31m\u001B[9m%s\u001B[0m [%s] -> \u001B[31m[JALUR RUSAK/TERTUTUP]\u001B[0m\n", 
                                connector, dest.getName(), dest.getId());
                    }
                }
            }
            System.out.println();
        }
    }
}
