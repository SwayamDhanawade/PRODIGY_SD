package PRODIGY_SD_04;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Sudoku extends JFrame {
    private JTextField[][] cells = new JTextField[9][9];
    private JButton solveButton, clearButton, exitButton;
    private JLabel messageLabel; // Label to display messages (e.g., Solved or Invalid Input)

    public Sudoku() {
        setTitle("Sudoku Solver");
        setSize(400, 550);  // Adjusted size to fit the message label
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the grid of text fields
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(9, 9));

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                // Add thicker borders between 3x3 grids for better visual separation
                if (row % 3 == 0 && row != 0) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, Color.BLACK));
                }
                if (col % 3 == 0 && col != 0) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 3, 1, 1, Color.BLACK));
                }
                if (row % 3 == 0 && row != 0 && col % 3 == 0 && col != 0) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.BLACK));
                }

                gridPanel.add(cells[row][col]);
            }
        }

        // Solve button
        solveButton = new JButton("Solve");
        solveButton.addActionListener(new SolveButtonListener());

        // Clear button
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearGrid());

        // Exit button
        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));  // Close the application

        // Message Label for status updates
        messageLabel = new JLabel("Enter Sudoku puzzle and click Solve.");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(Color.BLUE);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(solveButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(exitButton);  // Add exit button to the panel

        // Add components to the frame
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(messageLabel, BorderLayout.NORTH);  // Message label at the bottom

        setVisible(true);
    }

    // Listener for the solve button
    private class SolveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[][] board = new int[9][9];

            // Read the input from the text fields
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String text = cells[row][col].getText();
                    if (text.isEmpty()) {
                        board[row][col] = 0; // Empty cell
                    } else {
                        try {
                            int value = Integer.parseInt(text);
                            if (value >= 1 && value <= 9) {
                                board[row][col] = value; // Valid number input
                            } else {
                                messageLabel.setText("Invalid input: Numbers must be between 1 and 9.");
                                messageLabel.setForeground(Color.RED);
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            messageLabel.setText("Invalid input: Please enter only numbers.");
                            messageLabel.setForeground(Color.RED);
                            return;
                        }
                    }
                }
            }

            // Check if the input board configuration is valid
            if (!isValidSudoku(board)) {
                messageLabel.setText("Invalid Sudoku configuration: Duplicate values found.");
                messageLabel.setForeground(Color.RED);
                return;
            }

            // Solve the Sudoku and update the grid
            if (solveSudoku(board)) {
                updateGrid(board);
                messageLabel.setText("Sudoku Solved!");
                messageLabel.setForeground(Color.GREEN);
            } else {
                messageLabel.setText("No solution exists for this puzzle.");
                messageLabel.setForeground(Color.RED);
            }
        }
    }

    // Clear the grid
    private void clearGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col].setText("");
            }
        }
        messageLabel.setText("Enter Sudoku puzzle and click Solve.");
        messageLabel.setForeground(Color.BLUE);
    }

    // Update the grid with the solved Sudoku
    private void updateGrid(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col].setText(String.valueOf(board[row][col]));
            }
        }
    }

    // Method to solve the Sudoku puzzle (Backtracking)
    public boolean solveSudoku(int[][] board) {
        int row = -1, col = -1;
        boolean isEmpty = true;

        // Find an empty cell in the board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }

        // If no empty cell is found, the Sudoku is solved
        if (isEmpty) {
            return true;
        }

        // Try placing numbers 1-9 in the empty cell
        for (int num = 1; num <= 9; num++) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;

                // Recursively try to solve the rest of the puzzle
                if (solveSudoku(board)) {
                    return true;
                }

                // If it leads to a dead-end, reset the cell and backtrack
                board[row][col] = 0;
            }
        }

        return false;
    }

    // Method to check if placing a number is safe (no conflicts)
    public boolean isSafe(int[][] board, int row, int col, int num) {
        // Check the row
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        // Check the column
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }

        // Check the 3x3 subgrid
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    // Method to check if the current board configuration is valid
    public boolean isValidSudoku(int[][] board) {
        // Check each row, column, and 3x3 subgrid for duplicates
        for (int i = 0; i < 9; i++) {
            if (hasDuplicate(board[i]) || hasDuplicate(getColumn(board, i)) || hasDuplicate(getSubgrid(board, i))) {
                return false;
            }
        }
        return true;
    }

    // Helper method to check if an array contains duplicate non-zero values
    private boolean hasDuplicate(int[] array) {
        boolean[] seen = new boolean[10]; // Array to track numbers 1-9
        for (int num : array) {
            if (num != 0 && seen[num]) {
                return true;
            }
            seen[num] = true;
        }
        return false;
    }

    // Helper method to get a column from the board
    private int[] getColumn(int[][] board, int col) {
        int[] column = new int[9];
        for (int i = 0; i < 9; i++) {
            column[i] = board[i][col];
        }
        return column;
    }

    // Helper method to get a 3x3 subgrid from the board
    private int[] getSubgrid(int[][] board, int index) {
        int[] subgrid = new int[9];
        int startRow = (index / 3) * 3;
        int startCol = (index % 3) * 3;
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                subgrid[k++] = board[startRow + i][startCol + j];
            }
        }
        return subgrid;
    }

    public static void main(String[] args) {
        // Create the Sudoku Solver GUI
        new Sudoku();
    }
}
