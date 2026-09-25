package blackjack;

/**
 * Command-line entry point for the Blackjack Swing application.
 */
public class Main {
    public static void main(String[] args) {
        BlackjackModel model = new BlackjackModel("Player");
        BlackjackController controller = new BlackjackController(model);
        BlackjackFrame.launch(controller);
    }
}