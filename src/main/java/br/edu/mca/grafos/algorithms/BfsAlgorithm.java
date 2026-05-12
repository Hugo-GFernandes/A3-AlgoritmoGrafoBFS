package br.edu.mca.grafos.algorithms;

import br.edu.mca.grafos.model.AlgorithmResult;
import br.edu.mca.grafos.model.Graph;

public class BfsAlgorithm implements GraphAlgorithm {

    @Override
    public AlgorithmResult run(Graph graph, String source, String target) {
        /*
         * Implementação esperada:
         *
         * 1. Validar se source existe no grafo.
         * 2. Executar o algoritmo.
         * 3. Preencher:
         *    - visitedOrder
         *    - distances
         *    - predecessors
         *    - path
         *    - message
         *
         * Exemplo:
         *
         * AlgorithmResult result = new AlgorithmResult();
         * result.getVisitedOrder().add("A");
         * result.getDistances().put("A", 0.0);
         * result.getPredecessors().put("A", null);
         * result.getPath().add("A");
         * result.setMessage("Algoritmo executado com sucesso.");
         * return result;
         */
        throw new UnsupportedOperationException("Grupo BFS: implemente a busca em largura neste arquivo.");
    }
}
