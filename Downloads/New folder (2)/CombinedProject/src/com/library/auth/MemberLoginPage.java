package com.library.auth;

import com.library.common.BasePage;
import com.library.main.NavigationController;
import com.library.common.Icons;
import javax.swing.*;
import java.awt.*;

public class MemberLoginPage extends BasePage {

    private static final long serialVersionUID = 1L;

    // References for components
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPanel emailPanel;
    private JPanel passwordPanel;
    private JButton loginButton;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel iconLabel;
    private JLabel signupLabel;
    private JLabel forgotLabel;
    private JLabel footerLabel;
    private JButton eyeButton;

    public MemberLoginPage(NavigationController controller) {
        super(controller);

        // Setup initial components
        createCardComponents();

        // Add card to background (handled by BasePage)
        addCardToBackground();

        // Initial update
        updateAllComponents(1.0);
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
        card.add(backPanel);
        card.add(Box.createVerticalStrut(20));

        // Icon
        iconLabel = new JLabel(Icons.getUserIcon(64, Color.WHITE));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(buttonColor);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);

        // Title
        titleLabel = new JLabel("Member Login");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        // Subtitle
        subtitleLabel = new JLabel("Enter your credentials");
        subtitleLabel.setForeground(hoverColor);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(25));

        // Email Field
        emailField = new JTextField();
        emailPanel = createRoundedFieldPanel("Email", false, emailField, null);
        emailPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(emailPanel);
        card.add(Box.createVerticalStrut(15));

        // Password Field
        passwordField = new JPasswordField();
        eyeButton = createEyeButton(passwordField); // Use helper from BasePage
        passwordPanel = createRoundedFieldPanel("Password", true, passwordField, eyeButton);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passwordPanel);
        card.add(Box.createVerticalStrut(20));

        // Login Button
        loginButton = new JButton("Log In") {
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
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(buttonColor);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(buttonColor);
            }
        });
        loginButton.addActionListener(e -> validateLogin());

        card.add(loginButton);
        card.add(Box.createVerticalStrut(15));

        // Sign up
        signupLabel = new JLabel("New member? Sign up");
        signupLabel.setForeground(hoverColor);
        signupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                controller.navigateTo("SignUp", "Member");
            }
        });
        card.add(signupLabel);
        card.add(Box.createVerticalStrut(4));

        // Forgot
        forgotLabel = new JLabel("Forgot Password?");
        forgotLabel.setForeground(hoverColor);
        forgotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                controller.navigateTo("ResetPassword", "Member");
            }
        });
        card.add(forgotLabel);
        card.add(Box.createVerticalGlue());

        // Footer
        footerLabel = new JLabel("Library Management System");
        footerLabel.setForeground(new Color(180, 150, 90));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footerLabel);
    }

    private void validateLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || email.equals("Email") ||
                password.isEmpty() || password.equals("Password")) {
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

        // --- DATABASE LOGIN VERIFICATION ---
        String sql = "SELECT UserID, Role FROM Users WHERE Email = ?";
        try (java.sql.Connection conn = com.library.common.DatabaseConnection.getConnection();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            java.sql.ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("UserID");
                String dbRoleStr = rs.getString("Role");
                if (dbRoleStr != null && dbRoleStr.equalsIgnoreCase("Member")) {
                    com.library.common.UserSession.getInstance().login(userId, email,
                            com.library.common.UserSession.Role.MEMBER);
                    showMessage("Success", "Successful login!", true);
                    controller.navigateTo("HomeMember");
                } else {
                    showMessage("Error", "Access Denied: You are not a member (Role: " + dbRoleStr + ")", false);
                }
            } else {
                showMessage("Error", "Email not found. Please sign up.", false);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            showMessage("Error", "Database error: " + e.getMessage(), false);
        }
    }

    @Override
    protected void updateAllComponents(double scale) {
        // Update basic fonts
        int titleFontSize = (int) (26 * scale);
        int subtitleFontSize = (int) (13 * scale);
        int buttonFontSize = (int) (17 * scale);
        int linkFontSize = (int) (12 * scale);
        int footerFontSize = (int) (10 * scale);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, Math.max(20, titleFontSize)));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(11, subtitleFontSize)));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, Math.max(14, buttonFontSize)));
        signupLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(10, linkFontSize)));
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(10, linkFontSize)));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(8, footerFontSize)));

        // Update Icon
        int iconSize = (int) (64 * scale);
        iconLabel.setIcon(Icons.getUserIcon(iconSize, Color.WHITE));
        iconLabel.setText("");
        // iconLabel.setPreferredSize(new Dimension(iconSize, iconSize));
        // iconLabel.setMaximumSize(new Dimension(iconSize, iconSize));

        // Update Dimensions
        int fieldWidth = (int) (baseFieldWidth * scale);
        int fieldHeight = (int) (baseFieldHeight * scale);
        Dimension newFieldSize = new Dimension(fieldWidth, fieldHeight);

        updatePanelSize(emailPanel, newFieldSize);
        updatePanelSize(passwordPanel, newFieldSize);
        loginButton.setPreferredSize(newFieldSize);
        loginButton.setMaximumSize(newFieldSize);

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

        // Re-construct card content logic (simplified for now to just re-add)
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
        card.add(loginButton);
        card.add(Box.createVerticalStrut((int) (15 * scale)));
        card.add(signupLabel);
        card.add(Box.createVerticalStrut((int) (5 * scale)));
        card.add(forgotLabel);
        card.add(Box.createVerticalGlue());
        card.add(footerLabel);
    }
}
