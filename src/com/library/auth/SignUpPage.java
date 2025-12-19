package com.library.auth;

import com.library.common.BasePage;
import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;

public class SignUpPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private String role;

    // References
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPanel emailPanel;
    private JPanel passwordPanel;
    private JButton signUpButton;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel iconLabel;
    private JLabel loginLabel;
    private JLabel footerLabel;
    private JButton eyeButton;

    public SignUpPage(NavigationController controller) {
        super(controller);
        createCardComponents();
        addCardToBackground();
        updateAllComponents(1.0);
    }

    public void setRole(String role) {
        this.role = role;
        if (titleLabel != null) {
            titleLabel.setText(role + " Sign Up");
        }
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
        iconLabel = new JLabel("📖", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setOpaque(true);
        iconLabel.setBackground(buttonColor);
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);

        // Title
        titleLabel = new JLabel("Sign Up");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        // Subtitle
        subtitleLabel = new JLabel("Create your account");
        subtitleLabel.setForeground(hoverColor);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(25));

        // Email
        emailField = new JTextField();
        emailPanel = createRoundedFieldPanel(" ✉ Email", false, emailField, null);
        emailPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(emailPanel);
        card.add(Box.createVerticalStrut(15));

        // Password
        passwordField = new JPasswordField();
        eyeButton = createEyeButton(passwordField);
        passwordPanel = createRoundedFieldPanel("🔒 Password", true, passwordField, eyeButton);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passwordPanel);
        card.add(Box.createVerticalStrut(20));

        // Sign Up Button
        signUpButton = new JButton("Sign Up") {
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
        signUpButton.setContentAreaFilled(false);
        signUpButton.setFocusPainted(false);
        signUpButton.setBorderPainted(false);
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setBackground(buttonColor);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signUpButton.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                signUpButton.setBackground(buttonColor);
            }
        });
        signUpButton.addActionListener(e -> validateSignUp());

        card.add(signUpButton);
        card.add(Box.createVerticalStrut(15));

        // Login Link
        loginLabel = new JLabel("Already have an account? Log in");
        loginLabel.setForeground(hoverColor);
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ("Staff".equals(role)) {
                    controller.navigateTo("StaffLogin");
                } else {
                    controller.navigateTo("MemberLogin");
                }
            }
        });
        card.add(loginLabel);
        card.add(Box.createVerticalGlue());

        // Footer
        footerLabel = new JLabel("Library Management System");
        footerLabel.setForeground(new Color(180, 150, 90));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footerLabel);
    }

    private void validateSignUp() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || email.equals(" ✉ Email") ||
                password.isEmpty() || password.equals("🔒 Password")) {
            showMessage("Error", "Please fill in all fields", false);
            return;
        }
        if (!email.contains("@")) {
            showMessage("Error", "Email must contain '@' symbol", false);
            return;
        }
        if (!isValidEmail(email)) {
            showMessage("Error", "Please enter a valid email address", false);
            return;
        }
        if (password.length() < 6) {
            showMessage("Error", "Password must be at least 6 characters", false);
            return;
        }

        showMessage("Success", "Account created successfully!", true);

        // Navigate back to login
        if ("Staff".equals(role)) {
            controller.navigateTo("StaffLogin");
        } else {
            controller.navigateTo("MemberLogin");
        }
    }

    @Override
    protected void updateAllComponents(double scale) {
        int titleFontSize = (int) (26 * scale);
        int subtitleFontSize = (int) (13 * scale);
        int buttonFontSize = (int) (17 * scale);
        int linkFontSize = (int) (12 * scale);
        int footerFontSize = (int) (10 * scale);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, Math.max(20, titleFontSize)));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(11, subtitleFontSize)));
        signUpButton.setFont(new Font("Segoe UI", Font.BOLD, Math.max(14, buttonFontSize)));
        loginLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(10, linkFontSize)));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(8, footerFontSize)));

        int iconSize = (int) (50 * scale);
        iconLabel.setPreferredSize(new Dimension(iconSize, iconSize));
        iconLabel.setMaximumSize(new Dimension(iconSize, iconSize));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, (int) (32 * scale)));

        int fieldWidth = (int) (baseFieldWidth * scale);
        int fieldHeight = (int) (baseFieldHeight * scale);
        Dimension newFieldSize = new Dimension(fieldWidth, fieldHeight);

        updatePanelSize(emailPanel, newFieldSize);
        updatePanelSize(passwordPanel, newFieldSize);
        signUpButton.setPreferredSize(newFieldSize);
        signUpButton.setMaximumSize(newFieldSize);

        if (eyeButton != null) {
            int eyeSize = (int) (35 * scale);
            eyeButton.setPreferredSize(new Dimension(eyeSize, eyeSize));
            eyeButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, (int) (16 * scale)));
        }

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
        card.add(Box.createVerticalStrut((int) (15 * scale)));
        card.add(passwordPanel);
        card.add(Box.createVerticalStrut((int) (20 * scale)));
        card.add(signUpButton);
        card.add(Box.createVerticalStrut((int) (15 * scale)));
        card.add(loginLabel);
        card.add(Box.createVerticalGlue());
        card.add(footerLabel);
    }
}
