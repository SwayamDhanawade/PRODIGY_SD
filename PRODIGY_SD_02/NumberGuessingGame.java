
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class NumberGuessingGame extends JFrame {
    private int numberToGuess;
    private int attempts;

    public NumberGuessingGame() {
        // Generate a random number between 1 and 100
        Random random = new Random();
        numberToGuess = random.nextInt(100) + 1;
        attempts = 0;

        // Create the main window
        setTitle("Number Guessing Game");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel and label for instructions
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Guess a number between 1 and 100:");
        panel.add(label);

        // Create a text field for user input
        JTextField guessField = new JTextField(10);
        panel.add(guessField);

        // Create a button to submit the guess
        JButton guessButton = new JButton("Guess");
        panel.add(guessButton);

        // Add action listener to the button
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = guessField.getText();
                try {
                    int guess = Integer.parseInt(input);
                    attempts++;
                    if (guess < numberToGuess) {
                        JOptionPane.showMessageDialog(null, "Too low! Try again.");
                    } else if (guess > numberToGuess) {
                        JOptionPane.showMessageDialog(null, "Too high! Try again.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Congratulations! You guessed the number " + numberToGuess + " correctly in " + attempts + " attempts.");
                        resetGame();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                }
            }
        });

        // Add the panel to the frame
        add(panel);
    }

    // Method to reset the game
    private void resetGame() {
        numberToGuess = new Random().nextInt(100) + 1;
        attempts = 0;
    }

    public static void main(String[] args) {
        // Run the GUI on the event dispatching thread
        SwingUtilities.invokeLater(() -> {
            NumberGuessingGame game = new NumberGuessingGame();
            game.setVisible(true);
        });
    }
}
