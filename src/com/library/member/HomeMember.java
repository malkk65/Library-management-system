package com.library.member;

import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;

public class HomeMember extends MemberBasePage {

    public HomeMember(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("Dashboard");
        highlightSidebarButton("Dashboard");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel mainContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 60));
        mainContent.setOpaque(false);

        // Member Stats Cards
        mainContent
                .add(createModernCard("My Borrowed Books", "2", "📖", new Color(139, 69, 19), new Color(160, 82, 45)));
        mainContent.add(createModernCard("Due Soon", "1", "⏳", new Color(205, 92, 92), new Color(240, 128, 128)));
        mainContent.add(createModernCard("My Favorites", "5", "⭐", new Color(218, 165, 32), new Color(255, 215, 0)));

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
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
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
