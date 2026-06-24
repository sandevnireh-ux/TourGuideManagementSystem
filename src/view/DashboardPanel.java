package view;

import controller.GuideController;
import controller.ReviewController;
import controller.AssignmentController;
import controller.ScheduleController;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private GuideController guideController = new GuideController();
    private ScheduleController scheduleController = new ScheduleController();
    private AssignmentController assignmentController = new AssignmentController();
    private ReviewController reviewController = new ReviewController();

    // Stat labels
    private JLabel totalGuidesVal   = new JLabel("0");
    private JLabel availGuidesVal   = new JLabel("0");
    private JLabel totalToursVal    = new JLabel("0");
    private JLabel totalTouristsVal = new JLabel("0");
    private JLabel totalReviewsVal  = new JLabel("0");
    private JLabel avgRatingVal     = new JLabel("0.0");

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        buildUI();
        loadStats();
    }

    private void buildUI() {
        // ── PAGE TITLE ──
        JLabel pageTitle = new JLabel("Dashboard Overview");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(new Color(38, 50, 56));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));
        add(pageTitle, BorderLayout.NORTH);

        // ── STATS CARDS PANEL ──
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setBackground(new Color(245, 245, 245));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        cardsPanel.add(createCard("Total Guides",       totalGuidesVal,   new Color(0, 150, 136)));
        cardsPanel.add(createCard("Available Guides",   availGuidesVal,   new Color(76, 175, 80)));
        cardsPanel.add(createCard("Total Tours",        totalToursVal,    new Color(33, 150, 243)));
        cardsPanel.add(createCard("Total Assignments",  totalTouristsVal, new Color(156, 39, 176)));
        cardsPanel.add(createCard("Total Reviews",      totalReviewsVal,  new Color(255, 152, 0)));
        cardsPanel.add(createCard("Avg Rating",      avgRatingVal,     new Color(233, 30, 99)));

        // ── INFO PANEL ──
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));

        JPanel welcomeBox = new JPanel(new BorderLayout());
        welcomeBox.setBackground(Color.WHITE);
        welcomeBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 136), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel welcomeTitle = new JLabel("Welcome to Tour Guide Management System");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeTitle.setForeground(new Color(0, 105, 92));

        JLabel welcomeText = new JLabel("<html>Manage your tour guides, schedule tours, assign tourists,<br>" +
                "collect reviews and track guide locations — all in one place.</html>");
        welcomeText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        welcomeText.setForeground(new Color(80, 80, 80));
        welcomeText.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        welcomeBox.add(welcomeTitle, BorderLayout.NORTH);
        welcomeBox.add(welcomeText, BorderLayout.CENTER);
        infoPanel.add(welcomeBox, BorderLayout.CENTER);

        // ── CENTER WRAPPER ──
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(infoPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Color accent bar at top
        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setPreferredSize(new Dimension(0, 6));
        card.add(colorBar, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(100, 100, 100));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(valueLabel, BorderLayout.CENTER);

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    public void loadStats() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            int guides, availGuides, tours, assignments, reviews;
            double avgRating;

            @Override
            protected Void doInBackground() {
                try {
                    guides      = guideController.getTotalGuides();
                    availGuides = guideController.getAvailableGuidesCount();
                    tours       = scheduleController.getTotalSchedules();
                    assignments = assignmentController.getTotalAssignments();
                    reviews     = reviewController.getTotalReviews();
                    avgRating   = reviewController.getOverallAverageRating();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                totalGuidesVal.setText(String.valueOf(guides));
                availGuidesVal.setText(String.valueOf(availGuides));
                totalToursVal.setText(String.valueOf(tours));
                totalTouristsVal.setText(String.valueOf(assignments));
                totalReviewsVal.setText(String.valueOf(reviews));
                avgRatingVal.setText(String.format("%.1f", avgRating));
            }
        };
        worker.execute();
    }
}