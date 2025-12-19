package com.library.staff;

import com.library.common.UserSession;
import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;

public class Dashboard extends StaffBasePage {

    public Dashboard(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("System Overview");
        highlightSidebarButton("Dashboard");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel mainContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 60));
        mainContent.setOpaque(false);

        UserSession.Role role = UserSession.getInstance().getUserRole();
        if (role == null)
            role = UserSession.Role.STAFF; // Fallback

        if (role == UserSession.Role.ADMIN) {
            // ADMIN WIDGETS
            mainContent
                    .add(createModernCard("Total Staff", "12", "👔", new Color(60, 60, 60), new Color(100, 100, 100)));
            mainContent.add(createModernCard("System Revenue", "$5,240", "💰", new Color(46, 139, 87),
                    new Color(60, 179, 113)));
            mainContent.add(
                    createModernCard("Total Users", "1,042", "👥", new Color(70, 130, 180), new Color(100, 149, 237)));
            mainContent.add(
                    createModernCard("Server Health", "99.9%", "⚙", new Color(255, 140, 0), new Color(255, 165, 0)));
        } else {
            // STAFF WIDGETS
            mainContent.add(createModernCard("Total Physical Books", "7", "📚", new Color(101, 87, 78),
                    new Color(139, 117, 102)));
            mainContent.add(
                    createModernCard("Active Members", "3", "👥", new Color(165, 105, 65), new Color(185, 135, 95)));
            mainContent.add(
                    createModernCard("Books Borrowed", "2", "📖", new Color(210, 135, 65), new Color(230, 165, 95)));
            mainContent.add(
                    createModernCard("Fines Pending", "$15.00", "💵", new Color(200, 60, 60), new Color(220, 90, 90)));
        }

        return mainContent;
    }

    private JPanel createModernCard(String title, String value, String icon, Color color1, Color color2) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(new GradientPaint(0, 0, color1, 0, getHeight(), color2));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
            }
        };
        card.setPreferredSize(new Dimension(220, 160));
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("<html><center>" + title + "</center></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(valueLabel);
        centerPanel.add(titleLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);

        return card;
    }
}
