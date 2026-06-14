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
        System.out.println("--- Jaringan Distribusi Bencana ---");
        for (String id : adjList.keySet()) {
            LocationNode node = nodes.get(id);
            System.out.print(node.getName() + " terhubung ke: ");
            for (RouteEdge edge : adjList.get(id)) {
                if (edge.isActive()) {
                    System.out.print(nodes.get(edge.getDestinationId()).getName() + "(" + edge.getDistance() + "km) ");
                } else {
                    System.out.print("[" + nodes.get(edge.getDestinationId()).getName() + " - RUSAK] ");
                }
            }
            System.out.println();
        }
    }
}
