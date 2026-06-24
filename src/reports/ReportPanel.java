package reports;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ReportPanel extends JPanel {

    public ReportPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        buildUI();
    }

    private void buildUI() {
        JLabel pageTitle = new JLabel("Reports");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(new Color(38, 50, 56));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));
        add(pageTitle, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsPanel.setBackground(new Color(245, 245, 245));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        cardsPanel.add(createReportCard(
                "Tour Assignment Report",
                "Full report of all tour assignments\njoining Guides, Tourists,\nSchedules and Reviews",
                "tour_report.jrxml"
        ));

        cardsPanel.add(createReportCard(
                "Guide Performance Report",
                "Performance summary of all guides\nshowing total tours, tourists\nand average ratings",
                "guide_report.jrxml"
        ));

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createReportCard(String title, String description, String reportFile) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 105, 92), 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 105, 92));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel descLabel = new JLabel("<html><center>" +
                description.replace("\n", "<br>") +
                "</center></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(100, 100, 100));

        JButton btnGenerate = new JButton("Generate Report");
        btnGenerate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGenerate.setBackground(new Color(0, 150, 136));
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setFocusPainted(false);
        btnGenerate.setBorderPainted(false);
        btnGenerate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerate.setPreferredSize(new Dimension(180, 40));
        btnGenerate.addActionListener(e -> generateReport(reportFile));

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrapper.setBackground(Color.WHITE);
        btnWrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        btnWrapper.add(btnGenerate);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(btnWrapper, BorderLayout.SOUTH);

        return card;
    }

    private void generateReport(String reportFile) {
        try {
            String reportPath = "C:\\Users\\user\\IdeaProjects\\TourGuideManagementSystem\\src\\reports\\" + reportFile;
            java.io.File file = new java.io.File(reportPath);

            if (!file.exists()) {
                JOptionPane.showMessageDialog(this,
                        "Report file not found: " + reportPath,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
            Connection conn = DBConnection.getConnection();
            Map<String, Object> params = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            JOptionPane.showMessageDialog(this,
                    sw.toString().substring(0, Math.min(500, sw.toString().length())),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}