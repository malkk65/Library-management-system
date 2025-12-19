package com.library.auth;

import com.library.common.BasePage;
import com.library.main.NavigationController;
import com.library.common.UserSession;
import com.library.common.Icons;
import javax.swing.*;
import java.awt.*;

public class StaffLoginPage extends BasePage {

    private static final long serialVersionUID = 1L;

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

    public StaffLoginPage(NavigationController controller) {
        super(controller);
        createCardComponents();
        addCardToBackground();
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
        card.add(Box.createVerticalStrut(20));

        // Icon
        iconLabel = new JLabel(Icons.getUserIcon(64, Color.WHITE)); // Use centralized User Icon
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(buttonColor);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Make it round-ish via border or just square? The old one was just text.
        // Let's keep it simple: A colored circle background is nice, but JLabel
        // background is rectangular.
        // Ideally we'd draw the background IN the icon or use a custom component.
        // For now, let's just set the icon.
        card.add(iconLabel);

        // Title
        titleLabel = new JLabel("Staff Login");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        // Subtitle
        subtitleLabel = new JLabel("Enter your credentials");
        subtitleLabel.setForeground(hoverColor);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(25));

        // Email
        emailField = new JTextField();
        // Updated to use Icon.getEmailIcon implicitly? No, createRoundedFieldPanel
        // takes string title.
        // We need to modify createRoundedFieldPanel or just pass text with unicode?
        // The user wants "Modern Icons". The field labels " ✉ Email" might be okay as
        // is or we can replace the emoji.
        // Let's assume for now we keep the field labels text-based as "Email" and
        // "Password" but maybe cleaner?
        // Or if we want icons inside the fields, that's a bigger refactor.
        // The user specifically asked about "Login Icons" (plural) referring likely to
        // the big top icon and maybe field icons.
        // Let's stick to the big top icon first, and maybe keys/email.

        emailPanel = createRoundedFieldPanel("Email", false, emailField, null);
        // We'll leave the text plain "Email" to look cleaner than mixed emoji+text if
        // we can't put a real icon easily.

        emailPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(emailPanel);
        card.add(Box.createVerticalStrut(15));

        // Password
        passwordField = new JPasswordField();
        eyeButton = createEyeButton(passwordField);
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
                loginButton.setBackground(hoverColor); // lighter on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(buttonColor);
            }
        });
        loginButton.addActionListener(e -> validateLogin());

        card.add(loginButton);
        card.add(Box.createVerticalStrut(15));

        // Sign up
        signupLabel = new JLabel("New staff? Sign up");
        signupLabel.setForeground(hoverColor);
        signupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                controller.navigateTo("SignUp", "Staff");
            }
        });
        card.add(signupLabel);
        card.add(Box.createVerticalStrut(5));

        // Forgot
        forgotLabel = new JLabel("Forgot Password?");
        forgotLabel.setForeground(hoverColor);
        forgotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                controller.navigateTo("ResetPassword", "Staff");
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
        if (password.length() < 6) {
            showMessage("Error", "Password must be at least 6 characters", false);
            return;
        }

        // --- SUCCESSFUL LOGIN ---
        UserSession.Role role = UserSession.Role.STAFF;
        if (email.toLowerCase().startsWith("admin")) {
            role = UserSession.Role.ADMIN;
        }

        UserSession.getInstance().login(email, role);
        showMessage("Success", "Successful login as " + role, true);
        controller.navigateTo("Dashboard"); // Navigate to Staff Dashboard
    }

    @Override
    protected void updateAllComponents(double scale) {
        int titleFontSize = (int) (26 * scale);
        int subtitleFontSize = (int) (13 * scale);
        int fieldFontSize = (int) (14 * scale);
        int buttonFontSize = (int) (17 * scale);
        int linkFontSize = (int) (12 * scale);
        int footerFontSize = (int) (10 * scale);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, Math.max(20, titleFontSize)));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(11, subtitleFontSize)));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, Math.max(14, buttonFontSize)));
        signupLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(10, linkFontSize)));
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(10, linkFontSize)));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(8, footerFontSize)));

        int iconSize = (int) (64 * scale);
        // Re-generate icon with new size
        iconLabel.setIcon(Icons.getUserIcon(iconSize, Color.WHITE));
        iconLabel.setText(""); // remove text
        // iconLabel.setPreferredSize(new Dimension(iconSize + 20, iconSize + 20)); //
        // padding
        // iconLabel.setMaximumSize(new Dimension(iconSize + 20, iconSize + 20));

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
        // Re-construct logic
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
