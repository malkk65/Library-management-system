package com.library.auth;

import com.library.main.NavigationController;
import com.library.common.Icons;
import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int CARD_ARC = 30;
    private NavigationController controller;

    // Dimensions
    private int baseCardWidth = 400;
    private int baseCardHeight = 420;
    private JPanel card;
    private JLabel iconLabel;
    private JLabel welcomeLabel;
    private JLabel subtitleLabel;
    private JLabel footerLabel;
    private JButton staffBtn;
    private JButton memberBtn;

    public WelcomePage(NavigationController controller) {
        this.controller = controller;

        setLayout(new GridBagLayout());

        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(10, 10, 10),
                        getWidth(), getHeight(), new Color(120, 70, 20));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new GridBagLayout());

        // Add background to this panel taking full space
        GridBagConstraints bgGbc = new GridBagConstraints();
        bgGbc.gridx = 0;
        bgGbc.gridy = 0;
        bgGbc.weightx = 1.0;
        bgGbc.weighty = 1.0;
        bgGbc.fill = GridBagConstraints.BOTH;
        add(background, bgGbc);

        // ===== Create Components =====
        createCardComponents();

        // ===== Add Card =====
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel cardContainer = new JPanel(new GridBagLayout());
        cardContainer.setOpaque(false);

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.gridx = 0;
        cardGbc.gridy = 0;
        cardGbc.weightx = 0;
        cardGbc.weighty = 0;
        cardGbc.fill = GridBagConstraints.NONE;
        cardGbc.anchor = GridBagConstraints.CENTER;

        cardContainer.add(card, cardGbc);
        background.add(cardContainer, gbc);

        // ===== Resizing Listener =====
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                // Calculate new card size
                int newCardWidth = (int) (width * 0.65);
                int newCardHeight = (int) (height * 0.7);

                newCardWidth = Math.max(300, Math.min(800, newCardWidth));
                newCardHeight = Math.max(350, Math.min(650, newCardHeight));

                card.setPreferredSize(new Dimension(newCardWidth, newCardHeight));
                card.setMaximumSize(new Dimension(newCardWidth, newCardHeight));

                double widthScale = (double) newCardWidth / baseCardWidth;
                double heightScale = (double) newCardHeight / baseCardHeight;
                double scale = Math.min(widthScale, heightScale);

                updateAllComponents(scale);

                card.revalidate();
                card.repaint();
            }
        });

        updateAllComponents(1.0);
    }

    private void createCardComponents() {
        // ===== Card Panel =====
        card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 0, 0, 180),
                        getWidth(), getHeight(), new Color(0, 0, 0, 80));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CARD_ARC, CARD_ARC);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(80, 80, 80, 180));
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, CARD_ARC, CARD_ARC);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        card.setPreferredSize(new Dimension(baseCardWidth, baseCardHeight));

        // ===== Icon Label =====
        iconLabel = new JLabel(Icons.getAppLogo(80, 80));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(20));

        // ===== Welcome Label =====
        welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(welcomeLabel);
        card.add(Box.createVerticalStrut(10));

        // ===== Subtitle Label =====
        subtitleLabel = new JLabel("Please select your role");
        subtitleLabel.setForeground(new Color(200, 160, 80));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(30));

        // ===== Buttons =====
        staffBtn = createStyledButton("👥  Staff Login");
        memberBtn = createStyledButton("👤  Member Login");

        staffBtn.addActionListener(e -> {
            controller.navigateTo("StaffLogin");
        });
        memberBtn.addActionListener(e -> {
            controller.navigateTo("MemberLogin");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        staffBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        memberBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(staffBtn);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(memberBtn);

        card.add(buttonPanel);
        card.add(Box.createVerticalGlue());

        // ===== Footer =====
        footerLabel = new JLabel("Library Management System");
        footerLabel.setForeground(new Color(180, 150, 90));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footerLabel);
    }

    private void updateAllComponents(double scale) {
        int welcomeFontSize = (int) (32 * scale);
        int subtitleFontSize = (int) (16 * scale);
        int buttonFontSize = (int) (16 * scale);
        int footerFontSize = (int) (12 * scale);
        int iconFontSize = (int) (60 * scale); // This was previously declared but not used for iconLabel's icon size.
        int iconSize = Math.max(60, iconFontSize);
        iconLabel.setIcon(Icons.getAppLogo(iconSize, iconSize));
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, Math.max(24, welcomeFontSize)));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(12, subtitleFontSize)));
        staffBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, Math.max(14, buttonFontSize)));
        memberBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, Math.max(14, buttonFontSize)));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(10, footerFontSize)));

        int buttonWidth = (int) (300 * scale);
        int buttonHeight = (int) (50 * scale);
        Dimension buttonSize = new Dimension(buttonWidth, buttonHeight);

        staffBtn.setPreferredSize(buttonSize);
        staffBtn.setMaximumSize(buttonSize);
        staffBtn.setMinimumSize(new Dimension((int) (buttonWidth * 0.8), buttonHeight));

        memberBtn.setPreferredSize(buttonSize);
        memberBtn.setMaximumSize(buttonSize);
        memberBtn.setMinimumSize(new Dimension((int) (buttonWidth * 0.8), buttonHeight));

        int paddingVertical = (int) (30 * scale);
        int paddingHorizontal = (int) (50 * scale);
        card.setBorder(BorderFactory.createEmptyBorder(paddingVertical, paddingHorizontal,
                paddingVertical, paddingHorizontal));

        updateVerticalSpacings(scale);
    }

    private void updateVerticalSpacings(double scale) {
        card.removeAll();
        card.add(iconLabel);
        card.add(Box.createVerticalStrut((int) (20 * scale)));

        card.add(welcomeLabel);
        card.add(Box.createVerticalStrut((int) (10 * scale)));

        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut((int) (30 * scale)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        staffBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        memberBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(staffBtn);
        buttonPanel.add(Box.createVerticalStrut((int) (15 * scale)));
        buttonPanel.add(memberBtn);

        card.add(buttonPanel);
        card.add(Box.createVerticalGlue());
        card.add(footerLabel);
    }

    private JButton createStyledButton(String text) {
        Color normalColor = new Color(100, 100, 100);
        Color hoverColor = new Color(139, 69, 19);

        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 30;

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 30;
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));

                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);

                g2.dispose();
            }
        };

        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(normalColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });

        return button;
    }
}
