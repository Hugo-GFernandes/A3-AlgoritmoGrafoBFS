package br.edu.mca.grafos.util;

import br.edu.mca.grafos.model.Edge;
import br.edu.mca.grafos.model.Graph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GraphJsonUtil {
    private GraphJsonUtil() {}

    public static String toJson(Graph graph) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"directed\": ").append(graph.isDirected()).append(",\n");

        sb.append("  \"nodes\": [");
        int count = 0;
        for (String node : graph.getNodes()) {
            if (count++ > 0) sb.append(", ");
            sb.append("\"").append(escape(node)).append("\"");
        }
        sb.append("],\n");

        sb.append("  \"edges\": [\n");
        for (int i = 0; i < graph.getEdges().size(); i++) {
            Edge edge = graph.getEdges().get(i);
            sb.append("    {");
            sb.append("\"source\": \"").append(escape(edge.source())).append("\", ");
            sb.append("\"target\": \"").append(escape(edge.target())).append("\", ");
            sb.append("\"weight\": ").append(edge.weight());
            sb.append("}");
            if (i < graph.getEdges().size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}\n");
        return sb.toString();
    }

    public static Graph fromJson(String json) {
        boolean directed = json.contains("\"directed\"") && json.matches("(?s).*\"directed\"\\s*:\\s*true.*");
        Graph graph = new Graph(directed);

        Pattern nodesPattern = Pattern.compile("\"nodes\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher nodesMatcher = nodesPattern.matcher(json);
        if (nodesMatcher.find()) {
            String nodesText = nodesMatcher.group(1);
            Pattern nodePattern = Pattern.compile("\"(.*?)\"");
            Matcher nodeMatcher = nodePattern.matcher(nodesText);
            while (nodeMatcher.find()) {
                graph.addNode(unescape(nodeMatcher.group(1)));
            }
        }

        Pattern edgePattern = Pattern.compile(
            "\\{\\s*\"source\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"target\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"weight\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)\\s*\\}",
            Pattern.DOTALL
        );

        Matcher edgeMatcher = edgePattern.matcher(json);
        while (edgeMatcher.find()) {
            String source = unescape(edgeMatcher.group(1));
            String target = unescape(edgeMatcher.group(2));
            double weight = Double.parseDouble(edgeMatcher.group(3));
            graph.addEdge(source, target, weight);
        }

        return graph;
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescape(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
