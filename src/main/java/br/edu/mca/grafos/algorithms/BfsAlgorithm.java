package br.edu.mca.grafos.algorithms;

import br.edu.mca.grafos.model.AlgorithmResult;
import br.edu.mca.grafos.model.Edge;
import br.edu.mca.grafos.model.Graph;

import java.util.*;

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

        // algoritmo


        AlgorithmResult result = new AlgorithmResult();

        // Validar origem
        if (source == null || !graph.getNodes().contains(source)) {
            result.setMessage("Vértice de origem não encontrado.");
            return result;
        }

        Map<String, List<Edge>> adjacency = graph.adjacencyList();

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Inicializacao
        queue.add(source);
        visited.add(source);

        result.getDistances().put(source, 0.0);
        result.getPredecessors().put(source, null);

        // BFS
        while (!queue.isEmpty()) {

            String current = queue.poll();

            result.getVisitedOrder().add(current);

            for (Edge edge : adjacency.getOrDefault(current, Collections.emptyList())) {

                String neighbor = edge.target();

                if (!visited.contains(neighbor)) {

                    visited.add(neighbor);
                    queue.add(neighbor);

                    result.getDistances().put(
                            neighbor,
                            result.getDistances().get(current) + 1
                    );

                    result.getPredecessors().put(
                            neighbor,
                            current
                    );
                }
            }
        }

        // Reconstrucao do caminho
        if (target != null && graph.getNodes().contains(target)) {

            if (visited.contains(target)) {

                LinkedList<String> path = new LinkedList<>();

                String current = target;

                while (current != null) {
                    path.addFirst(current);
                    current = result.getPredecessors().get(current);
                }

                result.getPath().addAll(path);

                result.setMessage(
                        "Caminho encontrado de "
                                + source
                                + " até "
                                + target
                );

            } else {

                result.setMessage(
                        "Não existe caminho entre "
                                + source
                                + " e "
                                + target
                );
            }

        } else {

            result.setMessage("Busca em largura executada com sucesso.");
        }

        return result;
    }
}
