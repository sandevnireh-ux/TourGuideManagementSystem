package view;

import controller.AssignmentController;
import controller.ReviewController;
import model.Assignment;
import model.Review;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReviewPanel extends JPanel {

    private ReviewController reviewController = new ReviewController();
    private AssignmentController assignmentController = new AssignmentController();

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbAssignment;
    private JComboBox<String> cmbRating;
    private JTextArea txtComment;
    private JButton btnAdd, btnDelete, btnClear;

    private List<Assignment> completedAssignments;
    private int selectedReviewId = -1;

    public ReviewPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        buildUI();
        loadDropdowns();
        loadReviews();
    }

    public void refreshData() {
        loadDropdowns();
        loadReviews();
    }

    private void buildUI() {
        JLabel pageTitle = new JLabel("Ratings & Reviews");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(new Color(38, 50, 56));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));
        add(pageTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(620);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        String[] cols = {"ID", "Tourist", "Tour", "Rating", "Comment", "Date"};
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Reviews List"));
        splitPane.setLeftComponent(scrollPane);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Add Review"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        cmbAssignment = new JComboBox<>();
        cmbRating     = new JComboBox<>(new String[]{"5 - Excellent", "4 - Good", "3 - Average", "2 - Poor", "1 - Terrible"});
        txtComment    = new JTextArea(4, 20);
        txtComment.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        JScrollPane commentScroll = new JScrollPane(txtComment);

        addFormRow(formPanel, gbc, 0, "Assignment:", cmbAssignment);
        addFormRow(formPanel, gbc, 1, "Rating:",     cmbRating);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Comment:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(commentScroll, gbc);

        btnAdd    = createButton("Add", new Color(0, 150, 136));
        btnDelete = createButton("Delete",     new Color(244, 67, 54));
        btnClear  = createButton("Clear",      new Color(120, 120, 120));

        btnDelete.setEnabled(false);

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 8, 8));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        // Filler
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setBackground(Color.WHITE);
        formPanel.add(filler, gbc);
        gbc.weighty = 0;

// Buttons
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(formPanel);
        add(splitPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addReview());
        btnDelete.addActionListener(e -> deleteReview());
        btnClear.addActionListener(e -> clearForm());
    }

    private void loadDropdowns() {
        try {
            cmbAssignment.removeAllItems();
            completedAssignments = assignmentController.getCompletedWithoutReview();
            for (Assignment a : completedAssignments)
                cmbAssignment.addItem(a.getAssignmentId() + " - " + a.getTouristName() + " | " + a.getTourName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadReviews() {
        try {
            tableModel.setRowCount(0);
            List<Review> reviews = reviewController.getAllReviews();
            for (Review r : reviews) {
                tableModel.addRow(new Object[]{
                        r.getReviewId(), r.getTouristName(), r.getTourName(),
                        r.getRating() + " ⭐", r.getComment(), r.getReviewedAt()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading reviews: " + e.getMessage());
        }
    }

    private void populateForm() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedReviewId = (int) tableModel.getValueAt(row, 0);
        btnDelete.setEnabled(true);
        btnAdd.setEnabled(false);
    }

    private void addReview() {
        try {
            int idx = cmbAssignment.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this, "No completed assignments available.");
                return;
            }
            int assignmentId = completedAssignments.get(idx).getAssignmentId();
            String ratingStr = String.valueOf(cmbRating.getSelectedIndex() + 1);
            // rating is reversed: index 0 = 5 stars
            int rating = 5 - cmbRating.getSelectedIndex();
            reviewController.addReview(assignmentId, String.valueOf(rating), txtComment.getText());
            JOptionPane.showMessageDialog(this, "Review added successfully!");
            clearForm();
            loadDropdowns();
            loadReviews();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteReview() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this review?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                reviewController.deleteReview(selectedReviewId);
                JOptionPane.showMessageDialog(this, "Review deleted!");
                clearForm();
                loadReviews();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedReviewId = -1;
        if (cmbAssignment.getItemCount() > 0) cmbAssignment.setSelectedIndex(0);
        cmbRating.setSelectedIndex(0);
        txtComment.setText("");
        btnAdd.setEnabled(true);
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