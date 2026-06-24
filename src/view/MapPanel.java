package view;

import controller.GuideController;
import model.Guide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MapPanel extends JPanel {

    private GuideController guideController = new GuideController();
    private List<Guide> guides = new ArrayList<>();
    private JLabel statusLabel;
    private MapCanvas mapCanvas;

    public MapPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        buildUI();
        loadGuideLocations();
    }

    private void buildUI() {
        JLabel pageTitle = new JLabel("GPS Tracking - Guide Locations");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pageTitle.setForeground(new Color(38, 50, 56));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));
        add(pageTitle, BorderLayout.NORTH);

        mapCanvas = new MapCanvas();
        mapCanvas.setBackground(new Color(200, 220, 200));
        mapCanvas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 20, 10, 20),
                BorderFactory.createLineBorder(new Color(0, 105, 92), 2)
        ));
        add(mapCanvas, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        statusLabel = new JLabel("Loading guide locations...");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Refresh Locations");
        btnRefresh.setBackground(new Color(0, 150, 136));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadGuideLocations());
        bottomPanel.add(btnRefresh, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadGuideLocations() {
        try {
            guides = guideController.getAllGuides();
            mapCanvas.setGuides(guides);
            statusLabel.setText("Showing " + guides.size() + " guide(s). Click a marker to view details.");
        } catch (Exception e) {
            statusLabel.setText("Error loading locations: " + e.getMessage());
        }
    }

    class MapCanvas extends JPanel {

        private List<Guide> guides = new ArrayList<>();
        private double minLat = 5.9, maxLat = 9.9;
        private double minLng = 79.6, maxLng = 81.9;

        public MapCanvas() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Guide guide : guides) {
                        int x = lngToX(guide.getLongitude(), getWidth());
                        int y = latToY(guide.getLatitude(), getHeight());
                        if (Math.abs(e.getX() - x) <= 15 && Math.abs(e.getY() - y) <= 15) {
                            showGuideInfo(guide);
                            return;
                        }
                    }
                }
            });
        }

        public void setGuides(List<Guide> guides) {
            this.guides = guides;
            repaint();
        }

        private void showGuideInfo(Guide guide) {
            String info = "<html>" +
                    "<b>Name:</b> " + guide.getFullName() + "<br>" +
                    "<b>Phone:</b> " + guide.getPhone() + "<br>" +
                    "<b>Language:</b> " + guide.getLanguage() + "<br>" +
                    "<b>Experience:</b> " + guide.getExperienceYears() + " years<br>" +
                    "<b>Status:</b> " + guide.getStatus() + "<br>" +
                    "<b>Location:</b> " + guide.getLatitude() + ", " + guide.getLongitude() +
                    "</html>";
            JOptionPane.showMessageDialog(
                    MapCanvas.this, info,
                    "Guide: " + guide.getFullName(),
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Background
            g2.setColor(new Color(173, 216, 200));
            g2.fillRect(0, 0, w, h);

            // Grid lines
            g2.setColor(new Color(150, 200, 180));
            g2.setStroke(new BasicStroke(0.5f));
            for (int i = 0; i < w; i += 50) g2.drawLine(i, 0, i, h);
            for (int i = 0; i < h; i += 50) g2.drawLine(0, i, w, i);

            // Sri Lanka outline
            double[] lats = {9.8, 9.4, 8.5, 7.5, 6.8, 6.0, 5.9, 6.2, 6.5, 7.5, 8.5, 9.2, 9.8};
            double[] lngs = {80.0, 80.5, 81.2, 81.8, 81.6, 81.0, 80.5, 80.0, 79.7, 79.6, 79.8, 80.0, 80.0};

            int[] xPoints = new int[lats.length];
            int[] yPoints = new int[lats.length];
            for (int i = 0; i < lats.length; i++) {
                xPoints[i] = lngToX(lngs[i], w);
                yPoints[i] = latToY(lats[i], h);
            }

            g2.setColor(new Color(144, 238, 144));
            g2.fillPolygon(xPoints, yPoints, xPoints.length);
            g2.setColor(new Color(0, 105, 92));
            g2.setStroke(new BasicStroke(2f));
            g2.drawPolygon(xPoints, yPoints, xPoints.length);

            // Guide markers
            Color[] markerColors = {
                    new Color(244, 67, 54),
                    new Color(33, 150, 243),
                    new Color(156, 39, 176),
                    new Color(255, 152, 0),
                    new Color(0, 150, 136)
            };

            for (int i = 0; i < guides.size(); i++) {
                Guide guide = guides.get(i);
                int x = lngToX(guide.getLongitude(), w);
                int y = latToY(guide.getLatitude(), h);
                Color color = markerColors[i % markerColors.length];

                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillOval(x - 8, y - 8, 18, 18);

                g2.setColor(color);
                g2.fillOval(x - 10, y - 10, 20, 20);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(x - 10, y - 10, 20, 20);

                g2.setColor(new Color(38, 50, 56));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String name = guide.getFullName().split(" ")[0];
                g2.drawString(name, x + 12, y + 4);

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.setColor(color);
                g2.drawString(guide.getStatus(), x + 12, y + 16);
            }

            // Legend
            g2.setColor(new Color(255, 255, 255, 200));
            g2.fillRoundRect(10, 10, 160, 30 + guides.size() * 20, 8, 8);
            g2.setColor(new Color(38, 50, 56));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString("Guide Locations", 20, 28);

            for (int i = 0; i < guides.size(); i++) {
                Color color = markerColors[i % markerColors.length];
                g2.setColor(color);
                g2.fillOval(20, 35 + i * 20, 10, 10);
                g2.setColor(new Color(38, 50, 56));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.drawString(guides.get(i).getFullName(), 36, 44 + i * 20);
            }
        }

        private int lngToX(double lng, int w) {
            return (int) ((lng - minLng) / (maxLng - minLng) * w);
        }

        private int latToY(double lat, int h) {
            return (int) ((maxLat - lat) / (maxLat - minLat) * h);
        }
    }
}