package blackjack;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Top-level Swing frame for Blackjack.
 * Pure view: delegates to BlackjackPanel.
 */
public class BlackjackFrame extends JFrame {

    public BlackjackFrame(BlackjackController controller) {
        super("Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new BlackjackTablePanel(controller));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void launch(BlackjackController controller) {
        SwingUtilities.invokeLater(() -> new BlackjackFrame(controller));
    }
}