package br.edu.mca.grafos.algorithms;

import br.edu.mca.grafos.model.AlgorithmResult;
import br.edu.mca.grafos.model.Graph;

public interface GraphAlgorithm {
    AlgorithmResult run(Graph graph, String source, String target);
}
