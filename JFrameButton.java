package myJFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class JFrameButton {
    private JFrame frame;
    private JButton button;
    private JLabel label;
    private JProgressBar progressBar;
    private int clickCount = 0;
    private ConfettiPanel confettiPanel;

    public JFrameButton() {
        initialize();
    }

    // Initialize the GUI components
    private void initialize() {
        setupMainFrame();
        setupMenuBar();
        setupButton();
        setupLabel();
        setupProgressBar();
        setupConfettiPanel();
        setupButtonActionListener();
        frame.setVisible(true);
    }

    // Set up the main frame
    private void setupMainFrame() {
        frame = new JFrame("Click Me!");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
    }

    // Set up the menu bar and "Options" button
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Program By: Max Conrad"));
        menu.add(aboutItem);
        menuBar.add(menu);

        // Customize the "Options" button
        menu.setForeground(Color.WHITE); // Set text color to white
        Font menuFont = menu.getFont();
        menu.setFont(menuFont.deriveFont(Font.BOLD)); // Set font style to bold
        menu.setBackground(new Color(30, 144, 255)); // Set background color of the "Options" button

        // Customize the menu bar to have a different color
        menuBar.setBackground(new Color(100, 149, 237)); // Set background color of the menu bar
        frame.setJMenuBar(menuBar);
    }

    // Set up the button
    private void setupButton() {
        button = new JButton("Click Me");
        int buttonWidth = 100;
        int buttonHeight = 40;
        int buttonX = (frame.getWidth() - buttonWidth) / 2 - 20; // Re centered the button
        int buttonY = 100;
        button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        styleButton();
        frame.getContentPane().add(button);
    }

    // Apply styling to the button
    private void styleButton() {
        button.setBackground(new Color(70, 130, 180)); // Steel Blue color
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false); // Remove focus border
        button.setOpaque(true);
        button.setBorderPainted(true);
    }

    // Set up the label
    private void setupLabel() {
        label = new JLabel("Fill the progress bar by clicking the button.");
        int labelX = 40; // Adjusted to move a little more to the right
        int labelY = 0; // Adjusted to move a little lower
        int labelWidth = 350;
        int labelHeight = 50;
        label.setBounds(labelX, labelY, labelWidth, labelHeight);
        styleLabel();
        frame.getContentPane().add(label);
    }

    // Apply styling to the label
    private void styleLabel() {
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(33, 150, 243)); // Vibrant blue color
        label.setOpaque(true);
        label.setBackground(Color.WHITE); // White background to contrast shadow
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(200, 200, 200))); // Light gray shadow
    }

    // Set up the progress bar
    private void setupProgressBar() {
        progressBar = new JProgressBar();
        progressBar.setBounds(105, 160, 200, 20);
        progressBar.setForeground(new Color(102, 204, 0)); 
        progressBar.setBackground(Color.LIGHT_GRAY);
        progressBar.setBorder(BorderFactory.createRaisedBevelBorder());
        frame.getContentPane().add(progressBar);
    }

    // Set up the confetti panel
    private void setupConfettiPanel() {
        confettiPanel = new ConfettiPanel();
        confettiPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.getContentPane().add(confettiPanel);
        frame.getContentPane().setComponentZOrder(confettiPanel, 0);
    }

    // Set up the button action listener
    private void setupButtonActionListener() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clickCount++;
                updateProgressBar();
                checkProgressAndDisplayConfetti();
            }
        });
    }

    // Update the progress bar based on click count
    private void updateProgressBar() {
        int progress = (int) ((double) clickCount / 5 * 100);
        progressBar.setValue(progress);
    }

    // Check progress and display confetti if necessary
    private void checkProgressAndDisplayConfetti() {
        int progress = progressBar.getValue();
        if (progress >= 100) {
            updateLabelForCompletion();
            if (clickCount == 5) { // First round of confetti
                confettiPanel.initiateNewConfetti();
            } else if (clickCount > 5) { // Only one round of confetti per click
                confettiPanel.initiateAdditionalConfetti();
            }
        }
    }

    // Update the label when progress reaches 100%
    private void updateLabelForCompletion() {
        if (clickCount == 5) {
            label.setText("Go Hatters!");
            label.setForeground(new Color(0, 128, 0));
            int labelWidth = label.getPreferredSize().width;
            int labelX = button.getX() + (button.getWidth() - labelWidth) / 2;
            int labelY = 20;
            label.setBounds(labelX, labelY, labelWidth, label.getHeight());
        }
    }

    // Confetti panel to display falling confetti
    class ConfettiPanel extends JPanel {
        private final int CONFETTI_COUNT = 100;
        private Point[] confettiPoints;
        private Color[] confettiColors;
        private Random random = new Random();
        private Timer timer;
        private boolean isAnimationActive = false;

        // Constructor
        public ConfettiPanel() {
            setOpaque(false);
            confettiPoints = new Point[CONFETTI_COUNT];
            confettiColors = new Color[CONFETTI_COUNT];
            for (int i = 0; i < CONFETTI_COUNT; i++) {
                confettiPoints[i] = new Point(random.nextInt(450), -20 - random.nextInt(300));
                confettiColors[i] = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
            }
        }

        // Initiate a new round of confetti
        public void initiateNewConfetti() {
            if (!isAnimationActive) {
                resetConfetti();
                startConfettiAnimation();
                isAnimationActive = true;
            }
        }

        // Initiate additional confetti
        public void initiateAdditionalConfetti() {
            if (isAnimationActive) {
                resetConfetti();
            }
        }

        // Reset confetti positions
        private void resetConfetti() {
            for (int i = 0; i < CONFETTI_COUNT; i++) {
                confettiPoints[i] = new Point(random.nextInt(450), -20 - random.nextInt(300));
            }
        }

        // Start confetti animation
        private void startConfettiAnimation() {
            if (timer == null) {
                timer = new Timer(100, e -> {
                    for (int i = 0; i < CONFETTI_COUNT; i++) {
                        confettiPoints[i].y += 7;
                        if (confettiPoints[i].y > 300) {
                            confettiPoints[i].y = -20;
                        }
                    }
                    repaint();
                });
            }
            timer.start();
        }

        // Paint confetti on the panel
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < CONFETTI_COUNT; i++) {
                g.setColor(confettiColors[i]);
                g.fillOval(confettiPoints[i].x, confettiPoints[i].y, 10, 10);
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFrameButton());
    }
}
