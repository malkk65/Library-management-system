package com.library.auth;

import com.library.common.BasePage;
import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;

public class ResetPasswordPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private String role;

    // References
    private JTextField emailField;
    private JPanel emailPanel;
    private JButton resetButton;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel iconLabel;
    private JLabel footerLabel;

    public ResetPasswordPage(NavigationController controller) {
        super(controller);
        // Reset base height for this specific page as it's smaller
        baseCardHeight = 450;

        createCardComponents();
        addCardToBackground();
        updateAllComponents(1.0);
    }

    public void setRole(String role) {
        this.role = role;
    }

    private void createCardComponents() {
        card = createCardPanel();

        // Back Arrow
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backPanel.setOpaque(false);
        JLabel back = new JLabel("‹");
        back.setForeground(Color.GRAY);
        back.setFont(new Font("Segoe UI", Font.BOLD, 30));
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                controller.goBack();
            }
        });
        backPanel.add(back);
        card.add(Box.createVerticalStrut(20));

        // Icon
        iconLabel = new JLabel("🔑", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setOpaque(true);
        iconLabel.setBackground(buttonColor);
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);

        // Title
        titleLabel = new JLabel("Reset Password");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        // Subtitle
        subtitleLabel = new JLabel("Enter email to receive reset link");
        subtitleLabel.setForeground(hoverColor);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(25));

        // Email
        emailField = new JTextField();
        emailPanel = createRoundedFieldPanel(" ✉ Email", false, emailField, null);
        emailPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(emailPanel);
        card.add(Box.createVerticalStrut(20));

        // Reset Button
        resetButton = new JButton("Send Link") {
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
        resetButton.setContentAreaFilled(false);
        resetButton.setFocusPainted(false);
        resetButton.setBorderPainted(false);
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(buttonColor);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resetButton.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                resetButton.setBackground(buttonColor);
            }
        });
        resetButton.addActionListener(e -> validateReset());

        card.add(resetButton);
        card.add(Box.createVerticalGlue());

        // Footer
        footerLabel = new JLabel("Library Management System");
        footerLabel.setForeground(new Color(180, 150, 90));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footerLabel);
    }

    private void validateReset() {
        String email = emailField.getText().trim();
        if (email.isEmpty() || email.equals(" ✉ Email")) {
            showMessage("Error", "Please enter your email", false);
            return;
        }
        if (!email.contains("@")) {
            showMessage("Error", "Email must contain '@' symbol", false);
            return;
        }
        showMessage("Success", "Reset link sent to your email!", true);
    }

    @Override
    protected void updateAllComponents(double scale) {
        int titleFontSize = (int) (26 * scale);
        int subtitleFontSize = (int) (13 * scale);
        int buttonFontSize = (int) (17 * scale);
        int footerFontSize = (int) (10 * scale);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, Math.max(20, titleFontSize)));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(11, subtitleFontSize)));
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, Math.max(14, buttonFontSize)));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(8, footerFontSize)));

        int iconSize = (int) (50 * scale);
        iconLabel.setPreferredSize(new Dimension(iconSize, iconSize));
        iconLabel.setMaximumSize(new Dimension(iconSize, iconSize));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, (int) (32 * scale)));

        int fieldWidth = (int) (baseFieldWidth * scale);
        int fieldHeight = (int) (baseFieldHeight * scale);
        Dimension newFieldSize = new Dimension(fieldWidth, fieldHeight);

        updatePanelSize(emailPanel, newFieldSize);
        resetButton.setPreferredSize(newFieldSize);
        resetButton.setMaximumSize(newFieldSize);

        int padding = (int) (30 * scale);
        card.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

        updateVerticalSpacings(scale);
    }

    private void updateVerticalSpacings(double scale) {
        card.removeAll();
        // Re-construct
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backPanel.setOpaque(false);
        JLabel back = new JLabel("‹");
        back.setForeground(Color.GRAY);
        back.setFont(new Font("Segoe UI", Font.BOLD, (int) (30 * scale)));
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                controller.goBack();
            }
        });
        backPanel.add(back);
        card.add(backPanel);

        card.add(Box.createVerticalStrut((int) (20 * scale)));
        card.add(iconLabel);
        card.add(Box.createVerticalStrut((int) (20 * scale)));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut((int) (10 * scale)));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut((int) (25 * scale)));
        card.add(emailPanel);
        card.add(Box.createVerticalStrut((int) (20 * scale)));
        card.add(resetButton);
        card.add(Box.createVerticalGlue());
        card.add(footerLabel);
    }
}
