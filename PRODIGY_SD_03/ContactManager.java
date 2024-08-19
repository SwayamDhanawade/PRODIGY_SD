import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ContactManager extends JFrame {
    private HashMap<String, String[]> contacts;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, phoneField, emailField;
    private static final String CONTACTS_FILE = "contacts.dat";

    public ContactManager() {
        setTitle("Contact Manager");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize contacts
        contacts = loadContacts();

        // Create UI components
        tableModel = new DefaultTableModel(new String[]{"Name", "Phone", "Email"}, 0);
        table = new JTable(tableModel);
        loadTableData();

        nameField = new JTextField(10);
        phoneField = new JTextField(10);
        emailField = new JTextField(10);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteContactButton = new JButton("Remove Contact");

        // Panel for form inputs
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.add(new JLabel("Name:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Phone:"));
        fieldsPanel.add(phoneField);
        fieldsPanel.add(new JLabel("Email:"));
        fieldsPanel.add(emailField);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteContactButton);

        inputPanel.add(fieldsPanel);
        inputPanel.add(buttonsPanel);

        // Main layout
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addContact());
        editButton.addActionListener(e -> editContact());
        deleteContactButton.addActionListener(e -> deleteContact());

        setVisible(true);
    }

    // Load contacts from file
    private HashMap<String, String[]> loadContacts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CONTACTS_FILE))) {
            return (HashMap<String, String[]>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    // Save contacts to file
    private void saveContacts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONTACTS_FILE))) {
            oos.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load data into table
    private void loadTableData() {
        tableModel.setRowCount(0);
        for (Map.Entry<String, String[]> entry : contacts.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()[0], entry.getValue()[1]});
        }
    }

    // Add contact
    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            contacts.put(name, new String[]{phone, email});
            saveContacts();
            loadTableData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edit selected contact
    private void editContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            String phone = phoneField.getText();
            String email = emailField.getText();

            if (!phone.isEmpty() && !email.isEmpty()) {
                contacts.put(name, new String[]{phone, email});
                saveContacts();
                loadTableData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Phone and email fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No contact selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete selected contact
    private void deleteContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            contacts.remove(name);
            saveContacts();
            loadTableData();
        } else {
            JOptionPane.showMessageDialog(this, "No contact selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clear input fields
    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContactManager::new);
    }
}
