package br.edu.mca.grafos.model;

public class Edge {
    private final String source;
    private final String target;
    private final double weight;

    public Edge(String source, String target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public String source() {
        return source;
    }

    public String target() {
        return target;
    }

    public double weight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " -> " + target + " (" + weight + ")";
    }
}
