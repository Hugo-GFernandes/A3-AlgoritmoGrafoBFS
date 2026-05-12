package br.edu.mca.grafos.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlgorithmResult {
    private final List<String> visitedOrder = new ArrayList<>();
    private final Map<String, Double> distances = new LinkedHashMap<>();
    private final Map<String, String> predecessors = new LinkedHashMap<>();
    private final List<String> path = new ArrayList<>();
    private String message = "";
    private boolean hasNegativeCycle = false;

    public List<String> getVisitedOrder() {
        return visitedOrder;
    }

    public Map<String, Double> getDistances() {
        return distances;
    }

    public Map<String, String> getPredecessors() {
        return predecessors;
    }

    public List<String> getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasNegativeCycle() {
        return hasNegativeCycle;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHasNegativeCycle(boolean hasNegativeCycle) {
        this.hasNegativeCycle = hasNegativeCycle;
    }
}
