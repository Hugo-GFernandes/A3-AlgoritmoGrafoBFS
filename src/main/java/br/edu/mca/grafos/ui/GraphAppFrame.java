package br.edu.mca.grafos.ui;

import br.edu.mca.grafos.algorithms.BellmanFordAlgorithm;
import br.edu.mca.grafos.algorithms.BfsAlgorithm;
import br.edu.mca.grafos.algorithms.DfsAlgorithm;
import br.edu.mca.grafos.algorithms.DijkstraAlgorithm;
import br.edu.mca.grafos.algorithms.GraphAlgorithm;
import br.edu.mca.grafos.model.AlgorithmResult;
import br.edu.mca.grafos.model.Edge;
import br.edu.mca.grafos.model.Graph;
import br.edu.mca.grafos.util.GraphJsonUtil;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphAppFrame extends JFrame {
    private Graph graph = new Graph(false);
    private final GraphPanel graphPanel = new GraphPanel(graph);

    private final JTextField nodeField = new JTextField();
    private final JTextField sourceField = new JTextField();
    private final JTextField targetField = new JTextField();
    private final JTextField weightField = new JTextField("1");

    private final JCheckBox directedCheck = new JCheckBox("Grafo direcionado");
    private final JComboBox<String> algorithmCombo = new JComboBox<>(
        new String[]{
            "BFS - Busca em Largura",
            "DFS - Busca em Profundidade",
            "Dijkstra - Menor Caminho",
            "Bellman-Ford - Menor Caminho"
        }
    );

    private final JComboBox<String> sourceCombo = new JComboBox<>();
    private final JComboBox<String> targetCombo = new JComboBox<>();

    private final JTextArea infoArea = new JTextArea();

    public GraphAppFrame() {
        super("Visualizador de Grafos e Algoritmos — Java");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setLocationRelativeTo(null);

        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildControlPanel(), buildMainPanel());
        split.setDividerLocation(360);
        add(split, BorderLayout.CENTER);

        refreshAll();
    }

    private JPanel buildControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));

        form.add(new JLabel("Configuração", SwingConstants.LEFT));
        form.add(directedCheck);

        directedCheck.addActionListener(e -> {
            graph.setDirected(directedCheck.isSelected());
            refreshAll();
        });

        JButton clearButton = new JButton("Limpar grafo");
        clearButton.addActionListener(e -> {
            graph = new Graph(directedCheck.isSelected());
            graphPanel.setGraph(graph);
            refreshAll();
        });
        form.add(clearButton);

        form.add(new JLabel("Adicionar vértice"));
        form.add(nodeField);

        JButton addNodeButton = new JButton("Adicionar vértice");
        addNodeButton.addActionListener(e -> {
            try {
                graph.addNode(nodeField.getText());
                nodeField.setText("");
                refreshAll();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
        form.add(addNodeButton);

        form.add(new JLabel("Adicionar aresta"));
        form.add(new JLabel("Origem"));
        form.add(sourceField);
        form.add(new JLabel("Destino"));
        form.add(targetField);
        form.add(new JLabel("Peso"));
        form.add(weightField);

        JButton addEdgeButton = new JButton("Adicionar aresta");
        addEdgeButton.addActionListener(e -> {
            try {
                double weight = Double.parseDouble(weightField.getText().trim());
                graph.addEdge(sourceField.getText(), targetField.getText(), weight);
                refreshAll();
            } catch (Exception ex) {
                showError("Erro ao adicionar aresta: " + ex.getMessage());
            }
        });
        form.add(addEdgeButton);

        JButton importButton = new JButton("Importar JSON");
        importButton.addActionListener(e -> importJson());
        form.add(importButton);

        JButton exportButton = new JButton("Exportar JSON");
        exportButton.addActionListener(e -> exportJson());
        form.add(exportButton);

        form.add(new JLabel("Executar algoritmo"));
        form.add(algorithmCombo);
        form.add(new JLabel("Origem"));
        form.add(sourceCombo);
        form.add(new JLabel("Destino opcional"));
        form.add(targetCombo);

        JButton runButton = new JButton("Executar");
        runButton.addActionListener(e -> runAlgorithm());
        form.add(runButton);

        panel.add(form, BorderLayout.NORTH);

        return panel;
    }

    private JPanel buildMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(graphPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createTitledBorder("Representações e resultado"));
        bottom.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshAll() {
        directedCheck.setSelected(graph.isDirected());
        graphPanel.repaint();
        refreshCombos();
        refreshInfo(null);
    }

    private void refreshCombos() {
        sourceCombo.removeAllItems();
        targetCombo.removeAllItems();

        targetCombo.addItem("Nenhum");

        for (String node : graph.getNodes()) {
            sourceCombo.addItem(node);
            targetCombo.addItem(node);
        }
    }

    private void refreshInfo(AlgorithmResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append("Resumo\n");
        sb.append("Vértices: ").append(graph.getNodes().size()).append("\n");
        sb.append("Arestas: ").append(graph.getEdges().size()).append("\n");
        sb.append("Tipo: ").append(graph.isDirected() ? "Direcionado" : "Não direcionado").append("\n\n");

        sb.append("Lista de arestas\n");
        for (Edge edge : graph.getEdges()) {
            sb.append("- ").append(edge).append("\n");
        }

        sb.append("\nLista de adjacência\n");
        Map<String, List<Edge>> adjacency = graph.adjacencyList();
        for (Map.Entry<String, List<Edge>> entry : adjacency.entrySet()) {
            sb.append(entry.getKey()).append(" -> ");
            List<String> items = new ArrayList<>();
            for (Edge edge : entry.getValue()) {
                items.add(edge.target() + "(" + edge.weight() + ")");
            }
            sb.append(items).append("\n");
        }

        sb.append("\nMatriz de adjacência\n");
        List<String> nodes = new ArrayList<>(graph.getNodes());
        double[][] matrix = graph.adjacencyMatrix();

        sb.append("     ");
        for (String node : nodes) {
            sb.append(String.format("%8s", node));
        }
        sb.append("\n");

        for (int i = 0; i < nodes.size(); i++) {
            sb.append(String.format("%5s", nodes.get(i)));
            for (int j = 0; j < nodes.size(); j++) {
                sb.append(String.format("%8.1f", matrix[i][j]));
            }
            sb.append("\n");
        }

        if (result != null) {
            sb.append("\nResultado do algoritmo\n");
            sb.append("Mensagem: ").append(result.getMessage()).append("\n");
            sb.append("Ordem de visita: ").append(result.getVisitedOrder()).append("\n");
            sb.append("Distâncias: ").append(result.getDistances()).append("\n");
            sb.append("Predecessores: ").append(result.getPredecessors()).append("\n");
            sb.append("Caminho final: ").append(result.getPath()).append("\n");
            sb.append("Ciclo negativo: ").append(result.hasNegativeCycle()).append("\n");
        }

        infoArea.setText(sb.toString());
    }

    private void runAlgorithm() {
        if (sourceCombo.getSelectedItem() == null) {
            showError("Selecione um vértice de origem.");
            return;
        }

        String source = sourceCombo.getSelectedItem().toString();
        String target = null;

        if (targetCombo.getSelectedItem() != null && !"Nenhum".equals(targetCombo.getSelectedItem().toString())) {
            target = targetCombo.getSelectedItem().toString();
        }

        GraphAlgorithm algorithm = switch (algorithmCombo.getSelectedItem().toString()) {
            case "BFS - Busca em Largura" -> new BfsAlgorithm();
            case "DFS - Busca em Profundidade" -> new DfsAlgorithm();
            case "Dijkstra - Menor Caminho" -> new DijkstraAlgorithm();
            case "Bellman-Ford - Menor Caminho" -> new BellmanFordAlgorithm();
            default -> throw new IllegalStateException("Algoritmo inválido.");
        };

        try {
            AlgorithmResult result = algorithm.run(graph, source, target);
            graphPanel.setHighlights(result.getPath(), result.getVisitedOrder());
            refreshInfo(result);
        } catch (UnsupportedOperationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Algoritmo não implementado", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            showError("Erro ao executar algoritmo: " + ex.getMessage());
        }
    }

    private void importJson() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Arquivos JSON", "json"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                String content = Files.readString(file.toPath());
                graph = GraphJsonUtil.fromJson(content);
                graphPanel.setGraph(graph);
                refreshAll();
            } catch (Exception ex) {
                showError("Erro ao importar JSON: " + ex.getMessage());
            }
        }
    }

    private void exportJson() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("grafo.json"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                Files.writeString(file.toPath(), GraphJsonUtil.toJson(graph));
            } catch (Exception ex) {
                showError("Erro ao exportar JSON: " + ex.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
