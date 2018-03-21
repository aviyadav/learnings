package simple.thread.demo.swingDemo;

import javax.swing.SwingUtilities;

public class SwingAppDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame("SwingWorker Demo");
        });
    }
}
