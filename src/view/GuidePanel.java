package view;

import controller.GuideController;
import model.Guide;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GuidePanel extends JPanel {

    private GuideController guideController = new GuideController();

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtName, txtEmail, txtPhone, txtLanguage, txtExperience, txtLat, txtLng;
    private JComboBox<String> cmbStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    private int selectedGuideId = -1;

    public GuidePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        buildUI();
        loadGuides();
    }

    private void buildUI() {
        // ── PAGE TITLE ──
        JLabel pageTitle = new JLabel("Manage Guides");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(new Color(38, 50, 56));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));
        add(pageTitle, BorderLayout.NORTH);

        // ── SPLIT: TABLE LEFT, FORM RIGHT ──
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(620);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // ── TABLE ──
        String[] cols = {"ID", "Name", "Email", "Phone", "Language", "Experience", "Status"};
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Guide List"));
        splitPane.setLeftComponent(scrollPane);

        // ── FORM ──
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Guide Details"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        txtName       = new JTextField();
        txtEmail      = new JTextField();
        txtPhone      = new JTextField();
        txtLanguage   = new JTextField();
        txtExperience = new JTextField();
        txtLat        = new JTextField("6.9271");
        txtLng        = new JTextField("79.8612");
        cmbStatus     = new JComboBox<>(new String[]{"Available", "On Tour", "Off Duty"});

        addFormRow(formPanel, gbc, 0, "Full Name:",       txtName);
        addFormRow(formPanel, gbc, 1, "Email:",           txtEmail);
        addFormRow(formPanel, gbc, 2, "Phone:",           txtPhone);
        addFormRow(formPanel, gbc, 3, "Language(s):",     txtLanguage);
        addFormRow(formPanel, gbc, 4, "Experience (yrs):",txtExperience);
        addFormRow(formPanel, gbc, 5, "Status:",          cmbStatus);
        addFormRow(formPanel, gbc, 6, "Latitude:",        txtLat);
        addFormRow(formPanel, gbc, 7, "Longitude:",       txtLng);

        // ── BUTTONS ──
        btnAdd    = createButton("Add Guide",    new Color(0, 150, 136));
        btnUpdate = createButton("Update",       new Color(33, 150, 243));
        btnDelete = createButton("Delete",       new Color(244, 67, 54));
        btnClear  = createButton("Clear",        new Color(120, 120, 120));

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
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setBackground(Color.WHITE);
        formPanel.add(filler, gbc);
        gbc.weighty = 0;

// Buttons
        gbc.gridy = 9;
        gbc.insets = new Insets(10, 5, 5, 5);
        formPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(formPanel);
        add(splitPane, BorderLayout.CENTER);

        // ── BUTTON ACTIONS ──
        btnAdd.addActionListener(e -> addGuide());
        btnUpdate.addActionListener(e -> updateGuide());
        btnDelete.addActionListener(e -> deleteGuide());
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

    private void loadGuides() {
        try {
            tableModel.setRowCount(0);
            List<Guide> guides = guideController.getAllGuides();
            for (Guide g : guides) {
                tableModel.addRow(new Object[]{
                        g.getGuideId(), g.getFullName(), g.getEmail(),
                        g.getPhone(), g.getLanguage(), g.getExperienceYears() + " yrs",
                        g.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading guides: " + e.getMessage());
        }
    }

    private void populateForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedGuideId = (int) tableModel.getValueAt(row, 0);
        try {
            Guide g = guideController.getGuideById(selectedGuideId);
            if (g != null) {
                txtName.setText(g.getFullName());
                txtEmail.setText(g.getEmail());
                txtPhone.setText(g.getPhone());
                txtLanguage.setText(g.getLanguage());
                txtExperience.setText(String.valueOf(g.getExperienceYears()));
                cmbStatus.setSelectedItem(g.getStatus());
                txtLat.setText(String.valueOf(g.getLatitude()));
                txtLng.setText(String.valueOf(g.getLongitude()));
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                btnAdd.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void addGuide() {
        try {
            guideController.addGuide(
                    txtName.getText(), txtEmail.getText(), txtPhone.getText(),
                    txtLanguage.getText(), txtExperience.getText(),
                    (String) cmbStatus.getSelectedItem(),
                    Double.parseDouble(txtLat.getText()),
                    Double.parseDouble(txtLng.getText())
            );
            JOptionPane.showMessageDialog(this, "Guide added successfully!");
            clearForm();
            loadGuides();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateGuide() {
        try {
            guideController.updateGuide(
                    selectedGuideId,
                    txtName.getText(), txtEmail.getText(), txtPhone.getText(),
                    txtLanguage.getText(), txtExperience.getText(),
                    (String) cmbStatus.getSelectedItem(),
                    Double.parseDouble(txtLat.getText()),
                    Double.parseDouble(txtLng.getText())
            );
            JOptionPane.showMessageDialog(this, "Guide updated successfully!");
            clearForm();
            loadGuides();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteGuide() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this guide?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                guideController.deleteGuide(selectedGuideId);
                JOptionPane.showMessageDialog(this, "Guide deleted successfully!");
                clearForm();
                loadGuides();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedGuideId = -1;
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtLanguage.setText("");
        txtExperience.setText("");
        txtLat.setText("6.9271");
        txtLng.setText("79.8612");
        cmbStatus.setSelectedIndex(0);
        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        table.clearSelection();
    }
}