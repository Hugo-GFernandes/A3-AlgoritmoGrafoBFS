package br.edu.mca.grafos;

import br.edu.mca.grafos.ui.GraphAppFrame;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphAppFrame frame = new GraphAppFrame();
            frame.setVisible(true);
        });
    }
}
