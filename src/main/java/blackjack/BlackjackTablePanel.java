package blackjack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Graphical Blackjack table:
 * - Dealer area at the top
 * - Player area at the bottom
 * - Central control panel with buttons and status
 *
 * The "cards" are labels in card-like panels.
 */
public class BlackjackTablePanel extends JPanel {

    private final BlackjackController controller;

    // Dealer
    private final JPanel dealerCardsPanel = new JPanel();
    private final JLabel dealerLabel = new JLabel("Dealer");
    private final JLabel dealerScoreLabel = new JLabel("Score: 0");
    private final JLabel dealerRuleLabel = new JLabel("Dealer hits until 17, stands on 17 or more.");

    // Player
    private final JPanel playerCardsPanel = new JPanel();
    private final JLabel playerLabel = new JLabel("Player");
    private final JLabel playerScoreLabel = new JLabel("Score: 0");

    // Controls
    private final JButton startButton = new JButton("Start Round");
    private final JButton hitButton = new JButton("Hit");
    private final JButton standButton = new JButton("Stand");
    private final JLabel statusLabel = new JLabel("Welcome to Blackjack");

    public BlackjackTablePanel(BlackjackController controller) {
        this.controller = controller;
        initLayout();
        initActions();
        refreshState();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 100, 0)); // green table

        // Common font
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        dealerLabel.setFont(labelFont);
        playerLabel.setFont(labelFont);
        dealerLabel.setForeground(Color.WHITE);
        playerLabel.setForeground(Color.WHITE);
        dealerScoreLabel.setForeground(Color.WHITE);
        playerScoreLabel.setForeground(Color.WHITE);
        statusLabel.setForeground(Color.WHITE);
        dealerRuleLabel.setForeground(Color.LIGHT_GRAY);
        dealerRuleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Dealer area (top)
        JPanel dealerArea = new JPanel(new BorderLayout());
        dealerArea.setOpaque(false);

        JPanel dealerInfo = new JPanel(new BorderLayout());
        dealerInfo.setOpaque(false);
        dealerInfo.add(dealerLabel, BorderLayout.WEST);
        dealerInfo.add(dealerScoreLabel, BorderLayout.EAST);

        JPanel dealerHeader = new JPanel(new BorderLayout());
        dealerHeader.setOpaque(false);
        dealerHeader.add(dealerInfo, BorderLayout.NORTH);
        dealerHeader.add(dealerRuleLabel, BorderLayout.SOUTH);

        dealerArea.add(dealerHeader, BorderLayout.NORTH);

        dealerCardsPanel.setOpaque(false);
        dealerCardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dealerArea.add(dealerCardsPanel, BorderLayout.CENTER);

        // Player area (bottom) with buttons near cards
        JPanel playerArea = new JPanel(new BorderLayout());
        playerArea.setOpaque(false);

        JPanel playerInfo = new JPanel(new BorderLayout());
        playerInfo.setOpaque(false);
        playerInfo.add(playerLabel, BorderLayout.WEST);
        playerInfo.add(playerScoreLabel, BorderLayout.EAST);
        playerArea.add(playerInfo, BorderLayout.NORTH);

        playerCardsPanel.setOpaque(false);
        playerCardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerArea.add(playerCardsPanel, BorderLayout.CENTER);

        // Controls near player (Hit / Stand)
        JPanel playerControls = new JPanel();
        playerControls.setOpaque(false);
        playerControls.add(hitButton);
        playerControls.add(standButton);
        playerArea.add(playerControls, BorderLayout.SOUTH);

        // Top-left Start Round button + status centered
        JPanel topControlArea = new JPanel(new BorderLayout());
        topControlArea.setOpaque(false);

        JPanel startPanel = new JPanel();
        startPanel.setOpaque(false);
        startPanel.add(startButton);
        topControlArea.add(startPanel, BorderLayout.WEST);

        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        topControlArea.add(statusLabel, BorderLayout.CENTER);

        // Add areas to main panel
        add(dealerArea, BorderLayout.NORTH);
        add(topControlArea, BorderLayout.CENTER);
        add(playerArea, BorderLayout.SOUTH);

        // Horizontal card layout
        dealerCardsPanel.setLayout(new GridBagLayout());
        playerCardsPanel.setLayout(new GridBagLayout());

        setPreferredSize(new Dimension(800, 600));
    }

    private void initActions() {
        startButton.addActionListener(e -> {
            try {
                controller.startRound();
                statusLabel.setText("New hand started. Your turn.");
                hitButton.setEnabled(true);
                standButton.setEnabled(true);
                refreshState();
            } catch (Exception ex) {
                statusLabel.setText("Error starting hand: " + ex.getMessage());
            }
        });

        hitButton.addActionListener(e -> {
            try {
                controller.hit();
                refreshState();
                if (controller.getModel().getCurrentPlayer().getHand().isBust()) {
                    statusLabel.setText("You busted! Dealer wins.");
                    hitButton.setEnabled(false);
                    standButton.setEnabled(false);
                    flashBust(playerCardsPanel);
                } else {
                    statusLabel.setText("You hit. Choose Hit or Stand.");
                }
            } catch (Exception ex) {
                statusLabel.setText("Error on hit: " + ex.getMessage());
            }
        });

        standButton.addActionListener(e -> {
            try {
                controller.stand();
            } catch (Exception ex) {
                statusLabel.setText("Error on stand: " + ex.getMessage());
                return;
            }

            refreshState();

            BlackjackModel.Winner winner = controller.getModel().getWinner();
            switch (winner) {
                case PLAYER -> {
                    statusLabel.setText("You win!");
                    flashWin(playerCardsPanel);
                }
                case DEALER -> {
                    statusLabel.setText("Dealer wins.");
                    flashWin(dealerCardsPanel);
                }
                case PUSH -> statusLabel.setText("Push (tie).");
            }
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        });

        // Initially disabled until first hand starts
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
    }

    private void refreshState() {
        BlackjackModel model = controller.getModel();
        Player player = model.getCurrentPlayer();
        Player dealer = model.getDealer();

        // Update labels
        playerLabel.setText(player.getName());
        playerScoreLabel.setText("Score: " + player.getHand().score());
        dealerLabel.setText("Dealer");
        dealerScoreLabel.setText("Score: " + dealer.getHand().score());

        // Rebuild card panels
        rebuildCards(playerCardsPanel, player);
        rebuildCards(dealerCardsPanel, dealer);

        revalidate();
        repaint();
    }

    private void rebuildCards(JPanel panel, Player p) {
        panel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 0, 5);

        int x = 0;
        for (Card c : p.getHand().getCards()) {
            gbc.gridx = x++;
            panel.add(createCardComponent(c), gbc);
        }
    }

    /**
     * Creates a Swing component showing a real card image.
     * PNG files are under /cards, named like 2C.png, 10H.png, AS.png.
     */
    private JPanel createCardComponent(Card card) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setOpaque(false); // let the PNG's shape/alpha show
        cardPanel.setPreferredSize(new Dimension(100, 145)); // typical card ratio

        String imageName = imageNameForCard(card);
        java.net.URL url = getClass().getResource("/cards/" + imageName);

        if (url != null) {
            javax.swing.ImageIcon rawIcon = new javax.swing.ImageIcon(url);

            // scale to fit preferred size while keeping aspect ratio
            int targetW = cardPanel.getPreferredSize().width;
            int targetH = cardPanel.getPreferredSize().height;

            int srcW = rawIcon.getIconWidth();
            int srcH = rawIcon.getIconHeight();
            // keep aspect ratio of source
            double scale = Math.min((double) targetW / srcW, (double) targetH / srcH);
            int scaledW = (int) (srcW * scale);
            int scaledH = (int) (srcH * scale);

            java.awt.Image scaled = rawIcon.getImage().getScaledInstance(
                    scaledW,
                    scaledH,
                    java.awt.Image.SCALE_SMOOTH
            );
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(scaled);

            javax.swing.JLabel label = new javax.swing.JLabel(icon);
            label.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            label.setVerticalAlignment(javax.swing.JLabel.CENTER);
            label.setOpaque(false);

            cardPanel.add(label, BorderLayout.CENTER);
        } else {
            // Fallback: simple text card if image missing
            cardPanel.setOpaque(true);
            cardPanel.setBackground(Color.WHITE);
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            javax.swing.JLabel rankLabel = new javax.swing.JLabel(card.rank().name());
            javax.swing.JLabel suitLabel = new javax.swing.JLabel(card.suit().name());
            rankLabel.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            suitLabel.setHorizontalAlignment(javax.swing.JLabel.CENTER);

            cardPanel.add(rankLabel, BorderLayout.NORTH);
            cardPanel.add(suitLabel, BorderLayout.SOUTH);
        }

        return cardPanel;
    }

    /**
     * Visual feedback: flash red when bust.
     */
    private void flashBust(JPanel panel) {
        Color original = panel.getBackground();
        panel.setBackground(Color.RED);
        Timer t = new Timer(400, e -> panel.setBackground(original));
        t.setRepeats(false);
        t.start();
    }

    /**
     * Visual feedback: flash yellow on win.
     */
    private void flashWin(JPanel panel) {
        Color original = panel.getBackground();
        panel.setBackground(Color.YELLOW);
        Timer t = new Timer(400, e -> panel.setBackground(original));
        t.setRepeats(false);
        t.start();
    }

    private String imageNameForCard(Card card) {
        String rankCode;
        switch (card.rank()) {
            case Two -> rankCode = "2";
            case Three -> rankCode = "3";
            case Four -> rankCode = "4";
            case Five -> rankCode = "5";
            case Six -> rankCode = "6";
            case Seven -> rankCode = "7";
            case Eight -> rankCode = "8";
            case Nine -> rankCode = "9";
            case Ten -> rankCode = "10";
            case Jack -> rankCode = "J";
            case Queen -> rankCode = "Q";
            case King -> rankCode = "K";
            case Ace -> rankCode = "A";
            default -> throw new IllegalStateException("Unknown rank: " + card.rank());
        }

        String suitCode;
        switch (card.suit()) {
            case Club -> suitCode = "C";
            case Diamond -> suitCode = "D";
            case Heart -> suitCode = "H";
            case Spade -> suitCode = "S";
            default -> throw new IllegalStateException("Unknown suit: " + card.suit());
        }

        return rankCode + suitCode + ".png"; // e.g. "10H.png"
    }
}