package br.edu.mca.grafos.ui;

import br.edu.mca.grafos.model.Edge;
import br.edu.mca.grafos.model.Graph;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphPanel extends JPanel {
    private Graph graph;
    private List<String> highlightedPath = List.of();
    private List<String> visitedOrder = List.of();

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setBackground(Color.WHITE);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        repaint();
    }

    public void setHighlights(List<String> path, List<String> visitedOrder) {
        this.highlightedPath = path == null ? List.of() : path;
        this.visitedOrder = visitedOrder == null ? List.of() : visitedOrder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (graph == null || graph.getNodes().isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Grafo vazio", getWidth() / 2 - 60, getHeight() / 2);
            return;
        }

        Map<String, Point2> positions = calculateCircularLayout();
        Set<String> visited = new HashSet<>(visitedOrder);
        Set<String> pathNodes = new HashSet<>(highlightedPath);
        Set<String> pathEdges = buildPathEdgeKeys();

        drawEdges(g, positions, pathEdges);
        drawNodes(g, positions, visited, pathNodes);
    }

    private Map<String, Point2> calculateCircularLayout() {
        Map<String, Point2> positions = new HashMap<>();
        int n = graph.getNodes().size();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.max(120, Math.min(getWidth(), getHeight()) / 2 - 80);

        int i = 0;
        for (String node : graph.getNodes()) {
            double angle = 2 * Math.PI * i / n;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            positions.put(node, new Point2(x, y));
            i++;
        }

        return positions;
    }

    private void drawEdges(Graphics2D g, Map<String, Point2> positions, Set<String> pathEdges) {
        for (Edge edge : graph.getEdges()) {
            Point2 p1 = positions.get(edge.source());
            Point2 p2 = positions.get(edge.target());

            if (p1 == null || p2 == null) continue;

            String edgeKey = edge.source() + "->" + edge.target();
            String reverseKey = edge.target() + "->" + edge.source();
            boolean highlighted = pathEdges.contains(edgeKey) || (!graph.isDirected() && pathEdges.contains(reverseKey));

            g.setStroke(new BasicStroke(highlighted ? 4f : 2f));
            g.setColor(highlighted ? new Color(22, 163, 74) : new Color(100, 116, 139));

            g.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));

            if (graph.isDirected()) {
                drawArrowHead(g, p1, p2, highlighted);
            }

            int labelX = (p1.x + p2.x) / 2;
            int labelY = (p1.y + p2.y) / 2;

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(String.valueOf(edge.weight()), labelX, labelY);
        }
    }

    private void drawArrowHead(Graphics2D g, Point2 from, Point2 to, boolean highlighted) {
        double phi = Math.toRadians(25);
        int barb = 14;

        double dy = to.y - from.y;
        double dx = to.x - from.x;
        double theta = Math.atan2(dy, dx);

        int nodeRadius = 24;
        double tipX = to.x - nodeRadius * Math.cos(theta);
        double tipY = to.y - nodeRadius * Math.sin(theta);

        g.setColor(highlighted ? new Color(22, 163, 74) : new Color(100, 116, 139));

        for (int j = 0; j < 2; j++) {
            double rho = theta + (j == 0 ? phi : -phi);
            double x = tipX - barb * Math.cos(rho);
            double y = tipY - barb * Math.sin(rho);
            g.draw(new Line2D.Double(tipX, tipY, x, y));
        }
    }

    private void drawNodes(Graphics2D g, Map<String, Point2> positions, Set<String> visited, Set<String> pathNodes) {
        int r = 26;

        for (Map.Entry<String, Point2> entry : positions.entrySet()) {
            String node = entry.getKey();
            Point2 p = entry.getValue();

            Color fill = new Color(220, 235, 255);
            Color border = new Color(30, 58, 138);

            if (visited.contains(node)) {
                fill = new Color(253, 230, 138);
                border = new Color(146, 64, 14);
            }

            if (pathNodes.contains(node)) {
                fill = new Color(187, 247, 208);
                border = new Color(22, 101, 52);
            }

            g.setColor(fill);
            g.fillOval(p.x - r, p.y - r, 2 * r, 2 * r);

            g.setColor(border);
            g.setStroke(new BasicStroke(2.5f));
            g.drawOval(p.x - r, p.y - r, 2 * r, 2 * r);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 13));

            int textWidth = g.getFontMetrics().stringWidth(node);
            int textHeight = g.getFontMetrics().getAscent();
            g.drawString(node, p.x - textWidth / 2, p.y + textHeight / 3);
        }
    }

    private Set<String> buildPathEdgeKeys() {
        Set<String> keys = new HashSet<>();

        for (int i = 0; i < highlightedPath.size() - 1; i++) {
            keys.add(highlightedPath.get(i) + "->" + highlightedPath.get(i + 1));
        }

        return keys;
    }

    private static class Point2 {
        int x;
        int y;

        Point2(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
