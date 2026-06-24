package view;

import controller.GuideController;
import controller.ScheduleController;
import model.Guide;
import model.Schedule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class SchedulePanel extends JPanel {

    private ScheduleController scheduleController = new ScheduleController();
    private GuideController guideController = new GuideController();

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<String> cmbGuide;
    private JTextField txtTourName, txtLocation, txtLat, txtLng, txtMaxTourists;
    private JTextField txtDate, txtStartTime, txtEndTime;
    private JComboBox<String> cmbStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;

    private int selectedScheduleId = -1;
    private List<Guide> guideList;

    public SchedulePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        buildUI();
        loadGuides();
        loadSchedules();
    }

    private void buildUI() {
        JLabel pageTitle = new JLabel("Guide Scheduling");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(new Color(38, 50, 56));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));
        add(pageTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(620);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // ── TABLE ──
        String[] cols = {"ID", "Guide", "Tour Name", "Location", "Date", "Start", "End", "Max", "Status"};
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Schedule List"));
        splitPane.setLeftComponent(scrollPane);

        // ── FORM ──
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Schedule Details"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        cmbGuide      = new JComboBox<>();
        txtTourName   = new JTextField();
        txtLocation   = new JTextField();
        txtLat        = new JTextField("6.9271");
        txtLng        = new JTextField("79.8612");
        txtDate       = new JTextField("2026-12-01");
        txtStartTime  = new JTextField("09:00");
        txtEndTime    = new JTextField("17:00");
        txtMaxTourists = new JTextField("10");
        cmbStatus     = new JComboBox<>(new String[]{"Upcoming", "Ongoing", "Completed", "Cancelled"});

        addFormRow(formPanel, gbc, 0, "Guide:",        cmbGuide);
        addFormRow(formPanel, gbc, 1, "Tour Name:",    txtTourName);
        addFormRow(formPanel, gbc, 2, "Location:",     txtLocation);
        addFormRow(formPanel, gbc, 3, "Latitude:",     txtLat);
        addFormRow(formPanel, gbc, 4, "Longitude:",    txtLng);
        addFormRow(formPanel, gbc, 5, "Date (YYYY-MM-DD):", txtDate);
        addFormRow(formPanel, gbc, 6, "Start (HH:MM):",txtStartTime);
        addFormRow(formPanel, gbc, 7, "End (HH:MM):",  txtEndTime);
        addFormRow(formPanel, gbc, 8, "Max Tourists:", txtMaxTourists);
        addFormRow(formPanel, gbc, 9, "Status:",       cmbStatus);

        // ── BUTTONS ──
        btnAdd    = createButton("Add Schedule", new Color(0, 150, 136));
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
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setBackground(Color.WHITE);
        formPanel.add(filler, gbc);
        gbc.weighty = 0;

// Buttons
        gbc.gridy = 11;
        gbc.insets = new Insets(10, 5, 5, 5);
        formPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(formPanel);
        add(splitPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addSchedule());
        btnUpdate.addActionListener(e -> updateSchedule());
        btnDelete.addActionListener(e -> deleteSchedule());
        btnClear.addActionListener(e -> clearForm());
    }

    private void loadGuides() {
        try {
            cmbGuide.removeAllItems();
            guideList = guideController.getAllGuides();
            for (Guide g : guideList) {
                cmbGuide.addItem(g.getGuideId() + " - " + g.getFullName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading guides: " + e.getMessage());
        }
    }

    private void loadSchedules() {
        try {
            tableModel.setRowCount(0);
            List<Schedule> schedules = scheduleController.getAllSchedules();
            for (Schedule s : schedules) {
                tableModel.addRow(new Object[]{
                        s.getScheduleId(), s.getGuideName(), s.getTourName(),
                        s.getLocationName(), s.getTourDate(), s.getStartTime(),
                        s.getEndTime(), s.getMaxTourists(), s.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading schedules: " + e.getMessage());
        }
    }

    private void populateForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedScheduleId = (int) tableModel.getValueAt(row, 0);
        try {
            Schedule s = scheduleController.getScheduleById(selectedScheduleId);
            if (s != null) {
                // Select guide in combo
                for (int i = 0; i < guideList.size(); i++) {
                    if (guideList.get(i).getGuideId() == s.getGuideId()) {
                        cmbGuide.setSelectedIndex(i);
                        break;
                    }
                }
                txtTourName.setText(s.getTourName());
                txtLocation.setText(s.getLocationName());
                txtLat.setText(String.valueOf(s.getLatitude()));
                txtLng.setText(String.valueOf(s.getLongitude()));
                txtDate.setText(s.getTourDate().toString());
                txtStartTime.setText(s.getStartTime().toString().substring(0, 5));
                txtEndTime.setText(s.getEndTime().toString().substring(0, 5));
                txtMaxTourists.setText(String.valueOf(s.getMaxTourists()));
                cmbStatus.setSelectedItem(s.getStatus());
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                btnAdd.setEnabled(false);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private int getSelectedGuideId() {
        int idx = cmbGuide.getSelectedIndex();
        if (idx == -1 || guideList == null) return -1;
        return guideList.get(idx).getGuideId();
    }

    private String getSelectedGuideName() {
        int idx = cmbGuide.getSelectedIndex();
        if (idx == -1 || guideList == null) return "";
        return guideList.get(idx).getFullName();
    }

    private void addSchedule() {
        try {
            Date date = Date.valueOf(txtDate.getText().trim());
            Time start = Time.valueOf(txtStartTime.getText().trim() + ":00");
            Time end   = Time.valueOf(txtEndTime.getText().trim() + ":00");

            scheduleController.addSchedule(
                    getSelectedGuideId(), getSelectedGuideName(),
                    txtTourName.getText(), txtLocation.getText(),
                    Double.parseDouble(txtLat.getText()),
                    Double.parseDouble(txtLng.getText()),
                    date, start, end, txtMaxTourists.getText()
            );
            JOptionPane.showMessageDialog(this, "Schedule added successfully!");
            clearForm();
            loadSchedules();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSchedule() {
        try {
            Date date = Date.valueOf(txtDate.getText().trim());
            Time start = Time.valueOf(txtStartTime.getText().trim() + ":00");
            Time end   = Time.valueOf(txtEndTime.getText().trim() + ":00");

            scheduleController.updateSchedule(
                    selectedScheduleId, getSelectedGuideId(),
                    txtTourName.getText(), txtLocation.getText(),
                    Double.parseDouble(txtLat.getText()),
                    Double.parseDouble(txtLng.getText()),
                    date, start, end, txtMaxTourists.getText(),
                    (String) cmbStatus.getSelectedItem()
            );
            JOptionPane.showMessageDialog(this, "Schedule updated successfully!");
            clearForm();
            loadSchedules();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSchedule() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this schedule?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                scheduleController.deleteSchedule(selectedScheduleId);
                JOptionPane.showMessageDialog(this, "Schedule deleted successfully!");
                clearForm();
                loadSchedules();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedScheduleId = -1;
        if (cmbGuide.getItemCount() > 0) cmbGuide.setSelectedIndex(0);
        txtTourName.setText("");
        txtLocation.setText("");
        txtLat.setText("6.9271");
        txtLng.setText("79.8612");
        txtDate.setText("2026-12-01");
        txtStartTime.setText("09:00");
        txtEndTime.setText("17:00");
        txtMaxTourists.setText("10");
        cmbStatus.setSelectedIndex(0);
        btnAdd.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        table.clearSelection();
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
}