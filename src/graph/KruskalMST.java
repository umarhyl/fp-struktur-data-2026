package graph;

import model.LocationNode;
import model.RouteEdge;

import java.util.*;

public class KruskalMST {
    private Graph graph;

    public KruskalMST(Graph graph) {
        this.graph = graph;
    }

    private static class Edge implements Comparable<Edge> {
        String u, v;
        double weight;

        public Edge(String u, String v, double weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }
    }

    private class DisjointSet {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> rank = new HashMap<>();

        public void makeSet(String id) {
            parent.put(id, id);
            rank.put(id, 0);
        }

        public String find(String i) {
            if (!parent.get(i).equals(i)) {
                parent.put(i, find(parent.get(i))); // Path compression
            }
            return parent.get(i);
        }

        public void union(String i, String j) {
            String rootI = find(i);
            String rootJ = find(j);

            if (!rootI.equals(rootJ)) {
                if (rank.get(rootI) < rank.get(rootJ)) {
                    parent.put(rootI, rootJ);
                } else if (rank.get(rootI) > rank.get(rootJ)) {
                    parent.put(rootJ, rootI);
                } else {
                    parent.put(rootJ, rootI);
                    rank.put(rootI, rank.get(rootI) + 1);
                }
            }
        }
    }

    public void printMinimumSpanningTree() {
        List<Edge> edges = new ArrayList<>();
        Set<String> processedEdges = new HashSet<>();

        // Extract all edges
        for (String u : graph.getAdjList().keySet()) {
            for (RouteEdge edge : graph.getNeighbors(u)) {
                if (!edge.isActive()) continue;

                String v = edge.getDestinationId();
                // Ensure edge is added only once for undirected graph
                String edgeKey = u.compareTo(v) < 0 ? u + "-" + v : v + "-" + u;
                if (!processedEdges.contains(edgeKey)) {
                    edges.add(new Edge(u, v, edge.getDistance()));
                    processedEdges.add(edgeKey);
                }
            }
        }

        Collections.sort(edges);

        DisjointSet ds = new DisjointSet();
        for (String id : graph.getNodes().keySet()) {
            ds.makeSet(id);
        }

        List<Edge> mst = new ArrayList<>();
        double totalCost = 0;

        for (Edge edge : edges) {
            String rootU = ds.find(edge.u);
            String rootV = ds.find(edge.v);

            if (!rootU.equals(rootV)) {
                mst.add(edge);
                totalCost += edge.weight;
                ds.union(rootU, rootV);
            }
        }

        System.out.println("\n=== Minimum Spanning Tree (Jaringan Distribusi Minimum) ===");
        System.out.println("Jalur yang dipertahankan untuk menghemat biaya operasional:");
        for (Edge edge : mst) {
            System.out.println(graph.getNodes().get(edge.u).getName() + " -- " +
                               graph.getNodes().get(edge.v).getName() + " == " + edge.weight + " km");
        }
        System.out.println("Total Jarak Jaringan: " + totalCost + " km");
    }
}
