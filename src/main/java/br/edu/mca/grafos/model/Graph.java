package br.edu.mca.grafos.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
    private boolean directed;
    private final Set<String> nodes = new LinkedHashSet<>();
    private final List<Edge> edges = new ArrayList<>();

    public Graph(boolean directed) {
        this.directed = directed;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public void clear() {
        nodes.clear();
        edges.clear();
    }

    public void addNode(String node) {
        String cleaned = clean(node);
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("O nome do vértice não pode ser vazio.");
        }
        nodes.add(cleaned);
    }

    public void removeNode(String node) {
        String cleaned = clean(node);
        nodes.remove(cleaned);
        edges.removeIf(e -> e.source().equals(cleaned) || e.target().equals(cleaned));
    }

    public void addEdge(String source, String target, double weight) {
        String s = clean(source);
        String t = clean(target);

        if (s.isEmpty() || t.isEmpty()) {
            throw new IllegalArgumentException("Origem e destino são obrigatórios.");
        }

        nodes.add(s);
        nodes.add(t);
        edges.add(new Edge(s, t, weight));
    }

    public void removeEdge(String source, String target) {
        String s = clean(source);
        String t = clean(target);
        edges.removeIf(e -> e.source().equals(s) && e.target().equals(t));
    }

    public Set<String> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public Map<String, List<Edge>> adjacencyList() {
        Map<String, List<Edge>> adjacency = new LinkedHashMap<>();

        for (String node : nodes) {
            adjacency.put(node, new ArrayList<>());
        }

        for (Edge edge : edges) {
            adjacency.computeIfAbsent(edge.source(), k -> new ArrayList<>()).add(edge);

            if (!directed) {
                Edge reverse = new Edge(edge.target(), edge.source(), edge.weight());
                adjacency.computeIfAbsent(edge.target(), k -> new ArrayList<>()).add(reverse);
            }
        }

        return adjacency;
    }

    public double[][] adjacencyMatrix() {
        List<String> nodeList = new ArrayList<>(nodes);
        Map<String, Integer> index = new LinkedHashMap<>();

        for (int i = 0; i < nodeList.size(); i++) {
            index.put(nodeList.get(i), i);
        }

        double[][] matrix = new double[nodeList.size()][nodeList.size()];

        for (Edge edge : edges) {
            int i = index.get(edge.source());
            int j = index.get(edge.target());
            matrix[i][j] = edge.weight();

            if (!directed) {
                matrix[j][i] = edge.weight();
            }
        }

        return matrix;
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
