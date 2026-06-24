package view;

import controller.AssignmentController;
import controller.ScheduleController;
import controller.TouristController;
import model.Assignment;
import model.Schedule;
import model.Tourist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssignmentPanel extends JPanel {

    private AssignmentController assignmentController = new AssignmentController();
    private ScheduleController scheduleController = new ScheduleController();
    private TouristController touristController = new TouristController();

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbSchedule, cmbTourist;
    private JComboBox<String> cmbStatus;
    private JButton btnAssign, btnUpdate, btnDelete, btnClear;

    private List<Schedule> scheduleList;
    private List<Tourist> touristList;
    private int selectedAssignmentId = -1;

    public AssignmentPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        buildUI();
        loadDropdowns();
        loadAssignments();
    }

    public void refreshDropdowns() {
        loadDropdowns();
        loadAssignments();
    }

    private void buildUI() {
        JLabel pageTitle = new JLabel("Tourist Assignments");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(new Color(38, 50, 56));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));
        add(pageTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(620);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        String[] cols = {"ID", "Tour", "Tourist", "Assigned At", "Status"};
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Assignment List"));
        splitPane.setLeftComponent(scrollPane);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Assignment Details"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        cmbSchedule = new JComboBox<>();
        cmbTourist  = new JComboBox<>();
        cmbStatus   = new JComboBox<>(new String[]{"Confirmed", "Cancelled", "Completed"});

        addFormRow(formPanel, gbc, 0, "Schedule:", cmbSchedule);
        addFormRow(formPanel, gbc, 1, "Tourist:",  cmbTourist);
        addFormRow(formPanel, gbc, 2, "Status:",   cmbStatus);

        btnAssign = createButton("Assign Tourist", new Color(0, 150, 136));
        btnUpdate = createButton("Update Status",  new Color(33, 150, 243));
        btnDelete = createButton("Remove",         new Color(244, 67, 54));
        btnClear  = createButton("Clear",          new Color(120, 120, 120));

        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        btnPanel.add(btnAssign);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        // Filler
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setBackground(Color.WHITE);
        formPanel.add(filler, gbc);
        gbc.weighty = 0;

// Buttons
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(formPanel);
        add(splitPane, BorderLayout.CENTER);

        btnAssign.addActionListener(e -> assignTourist());
        btnUpdate.addActionListener(e -> updateStatus());
        btnDelete.addActionListener(e -> deleteAssignment());
        btnClear.addActionListener(e -> clearForm());
    }

    private void loadDropdowns() {
        try {
            cmbSchedule.removeAllItems();
            scheduleList = scheduleController.getAllSchedules();
            for (Schedule s : scheduleList)
                cmbSchedule.addItem(s.getScheduleId() + " - " + s.getTourName());

            cmbTourist.removeAllItems();
            touristList = touristController.getAllTourists();
            for (Tourist t : touristList)
                cmbTourist.addItem(t.getTouristId() + " - " + t.getFullName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void loadAssignments() {
        try {
            tableModel.setRowCount(0);
            List<Assignment> list = assignmentController.getAllAssignments();
            for (Assignment a : list) {
                tableModel.addRow(new Object[]{
                        a.getAssignmentId(), a.getTourName(), a.getTouristName(),
                        a.getAssignedAt(), a.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading assignments: " + e.getMessage());
        }
    }

    private void populateForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedAssignmentId = (int) tableModel.getValueAt(row, 0);
        String status = (String) tableModel.getValueAt(row, 4);
        cmbStatus.setSelectedItem(status);
        btnUpdate.setEnabled(true);
        btnDelete.setEnabled(true);
        btnAssign.setEnabled(false);
    }

    private void assignTourist() {
        try {
            int scheduleIdx = cmbSchedule.getSelectedIndex();
            int touristIdx  = cmbTourist.getSelectedIndex();
            if (scheduleIdx == -1 || touristIdx == -1) {
                JOptionPane.showMessageDialog(this, "Please select a schedule and tourist.");
                return;
            }
            int scheduleId = scheduleList.get(scheduleIdx).getScheduleId();
            int touristId  = touristList.get(touristIdx).getTouristId();
            assignmentController.assignTourist(scheduleId, touristId);
            JOptionPane.showMessageDialog(this, "Tourist assigned successfully!");
            clearForm();
            loadAssignments();

            // Send confirmation email
            try {
                model.Tourist t = touristController.getTouristById(touristId);
                model.Schedule s = scheduleController.getScheduleById(scheduleId);
                model.Guide g = new controller.GuideController().getGuideById(s.getGuideId());

                boolean sent = util.EmailUtil.sendAssignmentConfirmation(
                        t.getEmail(),
                        t.getFullName(),
                        s.getTourName(),
                        s.getTourDate().toString(),
                        g.getFullName()
                );

                if (sent) {
                    JOptionPane.showMessageDialog(this, "Confirmation email sent to " + t.getEmail());
                }
            } catch (Exception ex) {
                System.out.println("Email not sent: " + ex.getMessage());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatus() {
        try {
            assignmentController.updateStatus(selectedAssignmentId,
                    (String) cmbStatus.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Status updated successfully!");
            clearForm();
            loadAssignments();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAssignment() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove this assignment?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                assignmentController.deleteAssignment(selectedAssignmentId);
                JOptionPane.showMessageDialog(this, "Assignment removed!");
                clearForm();
                loadAssignments();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedAssignmentId = -1;
        if (cmbSchedule.getItemCount() > 0) cmbSchedule.setSelectedIndex(0);
        if (cmbTourist.getItemCount() > 0)  cmbTourist.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        btnAssign.setEnabled(true);
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