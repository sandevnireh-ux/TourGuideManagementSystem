package view;

import javax.swing.*;
import java.awt.*;
import reports.ReportPanel;

public class MainFrame extends JFrame {

    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Panels
    private DashboardPanel dashboardPanel;
    private GuidePanel guidePanel;
    private TouristPanel touristPanel;
    private SchedulePanel schedulePanel;
    private AssignmentPanel assignmentPanel;
    private ReviewPanel reviewPanel;
    private MapPanel mapPanel;
    private ReportPanel reportPanel;

    public MainFrame() {
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tour Guide Management System");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ── HEADER ──
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 105, 92));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("🗺 Tour Guide Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Hospitality & Tourism");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(178, 223, 219));
        header.add(subtitle, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ── SIDEBAR ──
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(38, 50, 56));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        addSidebarButton("  Dashboard",   "DASHBOARD",   "icons8-dashboard-32.png");
        addSidebarButton("  Guides",      "GUIDES",      "icons8-manual-32.png");
        addSidebarButton("  Tourists",    "TOURISTS",    "icons8-tourist-32.png");
        addSidebarButton("  Schedules",   "SCHEDULES",   "icons8-task-32.png");
        addSidebarButton("  Assignments", "ASSIGNMENTS", "icons8-date-to-32.png");
        addSidebarButton("  Reviews",     "REVIEWS",     "icons8-review-32.png");
        addSidebarButton("  Map / GPS",   "MAP",         "icons8-map-32.png");
        addSidebarButton("  Reports",     "REPORTS",     "icons8-reports-32.png");

        add(sidebarPanel, BorderLayout.WEST);

        // ── CONTENT AREA ──
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(245, 245, 245));

        dashboardPanel   = new DashboardPanel();
        guidePanel       = new GuidePanel();
        touristPanel     = new TouristPanel();
        schedulePanel    = new SchedulePanel();
        assignmentPanel  = new AssignmentPanel();
        reviewPanel      = new ReviewPanel();
        mapPanel         = new MapPanel();
        reportPanel      = new ReportPanel();

        contentPanel.add(dashboardPanel,  "DASHBOARD");
        contentPanel.add(guidePanel,      "GUIDES");
        contentPanel.add(touristPanel,    "TOURISTS");
        contentPanel.add(schedulePanel,   "SCHEDULES");
        contentPanel.add(assignmentPanel, "ASSIGNMENTS");
        contentPanel.add(reviewPanel,     "REVIEWS");
        contentPanel.add(mapPanel,        "MAP");
        contentPanel.add(reportPanel,     "REPORTS");

        add(contentPanel, BorderLayout.CENTER);

        // Show dashboard by default
        cardLayout.show(contentPanel, "DASHBOARD");
    }

    private void addSidebarButton(String text, String cardName, String iconFile) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(38, 50, 56));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

        try {
            String iconPath = "C:\\Users\\user\\IdeaProjects\\TourGuideManagementSystem\\icons\\" + iconFile;
            java.io.File f = new java.io.File(iconPath);
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(iconPath);
                Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(scaled));
                btn.setIconTextGap(10);
            } else {
                System.out.println("Not found: " + iconPath);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(0, 105, 92));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(38, 50, 56));
            }
        });

        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            if (cardName.equals("DASHBOARD"))   dashboardPanel.loadStats();
            if (cardName.equals("MAP"))         mapPanel.loadGuideLocations();
            if (cardName.equals("ASSIGNMENTS")) assignmentPanel.refreshDropdowns();
            if (cardName.equals("REVIEWS"))     reviewPanel.refreshData();
        });

        sidebarPanel.add(btn);
        sidebarPanel.add(Box.createVerticalStrut(2));
    }
}