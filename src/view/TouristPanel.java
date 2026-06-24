package view;

import controller.TouristController;
import model.Tourist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TouristPanel extends JPanel {

    private TouristController touristController = new TouristController();

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtName, txtEmail, txtPhone, txtNationality, txtPassport;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    private int selectedTouristId = -1;

    public TouristPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        buildUI();
        loadTourists();
    }

    private void buildUI() {
        JLabel pageTitle = new JLabel("Manage Tourists");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(new Color(38, 50, 56));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));
        add(pageTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(620);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // ── TABLE ──
        String[] cols = {"ID", "Name", "Email", "Phone", "Nationality", "Passport No"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0, 105, 92));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(178, 223, 219));
        table.getColumnModel().getColumn(0).setMaxWidth(40);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateForm();
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tourist List"));
        splitPane.setLeftComponent(scrollPane);

        // ── FORM ──
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Tourist Details"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        txtName        = new JTextField();
        txtEmail       = new JTextField();
        txtPhone       = new JTextField();
        txtNationality = new JTextField();
        txtPassport    = new JTextField();

        addFormRow(formPanel, gbc, 0, "Full Name:",    txtName);
        addFormRow(formPanel, gbc, 1, "Email:",        txtEmail);
        addFormRow(formPanel, gbc, 2, "Phone:",        txtPhone);
        addFormRow(formPanel, gbc, 3, "Nationality:",  txtNationality);
        addFormRow(formPanel, gbc, 4, "Passport No:",  txtPassport);

        // ── BUTTONS ──
        btnAdd    = createButton("Add Tourist", new Color(0, 150, 136));
        btnUpdate = createButton("Update",      new Color(33, 150, 243));
        btnDelete = createButton("Delete",      new Color(244, 67, 54));
        btnClear  = createButton("Clear",       new Color(120, 120, 120));

        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        // Filler
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setBackground(Color.WHITE);
        formPanel.add(filler, gbc);
        gbc.weighty = 0;

// Buttons
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 5, 5, 5);
        formPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(formPanel);
        add(splitPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addTourist());
        btnUpdate.addActionListener(e -> updateTourist());
        btnDelete.addActionListener(e -> deleteTourist());
        btnClear.addActionListener(e -> clearForm());
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(field, gbc);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadTourists() {
        try {
            tableModel.setRowCount(0);
            List<Tourist> tourists = touristController.getAllTourists();
            for (Tourist t : tourists) {
                tableModel.addRow(new Object[]{
                        t.getTouristId(), t.getFullName(), t.getEmail(),
                        t.getPhone(), t.getNationality(), t.getPassportNo()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading tourists: " + e.getMessage());
        }
    }

    private void populateForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedTouristId = (int) tableModel.getValueAt(row, 0);
        try {
            Tourist t = touristController.getTouristById(selectedTouristId);
            if (t != null) {
                txtName.setText(t.getFullName());
                txtEmail.setText(t.getEmail());
                txtPhone.setText(t.getPhone());
                txtNationality.setText(t.getNationality());
                txtPassport.setText(t.getPassportNo());
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                btnAdd.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void addTourist() {
        try {
            touristController.addTourist(
                    txtName.getText(), txtEmail.getText(), txtPhone.getText(),
                    txtNationality.getText(), txtPassport.getText()
            );
            JOptionPane.showMessageDialog(this, "Tourist added successfully!");
            clearForm();
            loadTourists();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTourist() {
        try {
            touristController.updateTourist(
                    selectedTouristId, txtName.getText(), txtEmail.getText(),
                    txtPhone.getText(), txtNationality.getText(), txtPassport.getText()
            );
            JOptionPane.showMessageDialog(this, "Tourist updated successfully!");
            clearForm();
            loadTourists();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTourist() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this tourist?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                touristController.deleteTourist(selectedTouristId);
                JOptionPane.showMessageDialog(this, "Tourist deleted successfully!");
                clearForm();
                loadTourists();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedTouristId = -1;
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtNationality.setText("");
        txtPassport.setText("");
        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        table.clearSelection();
    }
}