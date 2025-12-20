package com.library.common;

import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public abstract class BasePage extends JPanel {

    protected NavigationController controller;

    // Common Constants
    protected static final int FIELD_ARC = 30;
    protected static final int CARD_ARC = 25;

    // Common Colors
    protected Color buttonColor = new Color(139, 69, 19); // 8B4513
    protected Color hoverColor = new Color(200, 160, 80);
    protected Color placeholderColor = new Color(180, 140, 40);

    // Base Dimensions
    protected int baseCardWidth = 400;
    protected int baseCardHeight = 500;
    protected int baseFieldWidth = 300;
    protected int baseFieldHeight = 40;
    protected int baseFontSize = 13;

    // UI Components references that subclasses might need to access
    protected JPanel card;

    public BasePage(NavigationController controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());

        // Setup background and basic resizing
        addComponentListener(new PageResizeListener());
    }

    private class PageResizeListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            performResize(getWidth(), getHeight());
        }
    }

    // --- Abstract Methods ---
    // Subclasses must implement this to update their specific components
    protected abstract void updateAllComponents(double scale);

    // --- Layout & Painting ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        // Common Gradient Background
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(10, 10, 10),
                getWidth(), getHeight(), new Color(120, 70, 20)); // SaddleBrownish
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    protected void performResize(int width, int height) {
        // Default Logic, can be overridden if needed
        int newCardWidth = (int) (width * 0.65);
        int newCardHeight = (int) (height * 0.75);

        newCardWidth = Math.max(400, Math.min(800, newCardWidth));
        newCardHeight = Math.max(500, Math.min(900, newCardHeight));

        if (card != null) {
            card.setPreferredSize(new Dimension(newCardWidth, newCardHeight));
            card.setMaximumSize(new Dimension(newCardWidth, newCardHeight));
        }

        double widthScale = (double) newCardWidth / baseCardWidth;
        double heightScale = (double) newCardHeight / baseCardHeight;
        double scale = Math.min(widthScale, heightScale);

        updateAllComponents(scale);

        if (card != null) {
            card.revalidate();
            card.repaint();
        }
    }

    protected void addCardToBackground() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(30, 30, 30, 30);

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
        add(cardContainer, gbc);
    }

    // --- Component Factories ---

    protected JPanel createCardPanel() {
        JPanel p = new CardPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        return p;
    }

    private class CardPanel extends JPanel {
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

            g2.setColor(new Color(80, 80, 80, 180));
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CARD_ARC, CARD_ARC);
            g2.dispose();
        }
    }

    // Field Creation Helper
    // Returns a panel containing the field
    protected JPanel createRoundedFieldPanel(String placeholder, boolean isPassword, JTextField fieldRef,
            JButton eyeButtonRef) {
        CustomPanel panel = new CustomPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setCustomSize(new Dimension(baseFieldWidth, baseFieldHeight));

        if (isPassword && fieldRef instanceof JPasswordField) {
            JPasswordField pf = (JPasswordField) fieldRef;
            pf.setOpaque(false);
            pf.setBackground(new Color(0, 0, 0, 0));
            // Use logical font if Segoe UI Emoji is not available, but usually safe on
            // Windows
            pf.setFont(new Font("Segoe UI Emoji", Font.PLAIN, baseFontSize));
            pf.setForeground(placeholderColor);
            pf.setCaretColor(Color.WHITE);
            pf.setText(placeholder);
            pf.setEchoChar((char) 0);
            pf.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 50));

            pf.addFocusListener(new PasswordFocusListener(pf, panel, placeholder));

            if (eyeButtonRef != null) {
                pf.setLayout(new BorderLayout());
                JPanel iconPanel = new JPanel(new BorderLayout());
                iconPanel.setOpaque(false);
                iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
                iconPanel.add(eyeButtonRef, BorderLayout.EAST);
                pf.add(iconPanel, BorderLayout.EAST);
            }
            panel.add(pf, BorderLayout.CENTER);

        } else {
            // Normal text field
            JTextField tf = fieldRef;
            tf.setOpaque(false);
            tf.setBackground(new Color(0, 0, 0, 0));
            tf.setFont(new Font("Segoe UI Emoji", Font.PLAIN, baseFontSize));
            tf.setForeground(placeholderColor);
            tf.setCaretColor(Color.WHITE);
            tf.setText(placeholder);
            tf.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

            tf.addFocusListener(new TextFieldFocusListener(tf, panel, placeholder));

            panel.add(tf, BorderLayout.CENTER);
        }
        return panel;
    }

    protected JButton createEyeButton(JPasswordField passwordField) {
        JButton button = new EyeButton();

        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setPreferredSize(new Dimension(35, 35));

        button.addActionListener(e -> {
            if (passwordField.getEchoChar() != '\u0000') {
                passwordField.setEchoChar((char) 0);
                button.setText("🙈");
            } else {
                passwordField.setEchoChar('•');
                button.setText("👁");
            }
        });

        return button;
    }

    // --- Helper Logic ---

    protected boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    // Custom Panel Implementation for fields
    protected class CustomPanel extends JPanel {
        private boolean fieldFocused = false;
        private Dimension customSize;

        public CustomPanel(LayoutManager layout) {
            super(layout);
        }

        public void setCustomSize(Dimension size) {
            this.customSize = size;
        }

        public void setFieldFocused(boolean focused) {
            this.fieldFocused = focused;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bgColor = new Color(35, 35, 35);
            Color borderColor = fieldFocused ? hoverColor : new Color(80, 80, 80);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), FIELD_ARC, FIELD_ARC);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, FIELD_ARC, FIELD_ARC);
            if (fieldFocused) {
                g2.setColor(new Color(200, 160, 80, 30));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, FIELD_ARC, FIELD_ARC);
            }
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return customSize != null ? customSize : super.getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return customSize != null ? customSize : super.getMaximumSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return customSize != null ? customSize : super.getMinimumSize();
        }
    }

    protected void updatePanelSize(JPanel panel, Dimension newSize) {
        if (panel instanceof CustomPanel) {
            ((CustomPanel) panel).setCustomSize(newSize);
        }
        panel.setPreferredSize(newSize);
        panel.setMaximumSize(newSize);
        panel.setMinimumSize(newSize);
    }

    protected void showMessage(String title, String message, boolean isSuccess) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow != null ? (Frame) parentWindow : null, title, true);
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(380, 250);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new MainPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new HeaderPanel(title, isSuccess);
        headerPanel.setPreferredSize(new Dimension(380, 50));
        headerPanel.setLayout(new BorderLayout());

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(40, 40));
        closeButton.addActionListener(e -> dialog.dispose());

        closeButton.addMouseListener(new CloseButtonMouseListener(closeButton));

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        closePanel.setOpaque(false);
        closePanel.add(closeButton);
        headerPanel.add(closePanel, BorderLayout.EAST);

        JPanel contentPanel = new ContentPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(isSuccess ? "✅" : "❌");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconLabel.setForeground(isSuccess ? new Color(0, 200, 0) : new Color(255, 100, 100));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("<html><div style='text-align: center; padding: 5px; color: #FFFFFF;'>" +
                message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(240, 240, 240));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton okButton = new OKButton();
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        okButton.setContentAreaFilled(false);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setPreferredSize(new Dimension(130, 50));
        okButton.setMaximumSize(new Dimension(130, 50));
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(e -> dialog.dispose());

        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(okButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel shadowPanel = new JPanel(new BorderLayout());
        shadowPanel.setBackground(new Color(0, 0, 0, 0));
        shadowPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        shadowPanel.add(mainPanel);

        dialog.add(shadowPanel);
        dialog.setVisible(true);
    }

    // --- Inner Classes for Stability ---

    private class PasswordFocusListener extends FocusAdapter {
        private JPasswordField pf;
        private CustomPanel panel;
        private String placeholder;

        public PasswordFocusListener(JPasswordField pf, CustomPanel panel, String placeholder) {
            this.pf = pf;
            this.panel = panel;
            this.placeholder = placeholder;
        }

        @Override
        public void focusGained(FocusEvent evt) {
            panel.setFieldFocused(true);
            if (String.valueOf(pf.getPassword()).equals(placeholder)) {
                pf.setText("");
                pf.setForeground(Color.WHITE);
                pf.setEchoChar('•');
            }
        }

        @Override
        public void focusLost(FocusEvent evt) {
            panel.setFieldFocused(false);
            if (pf.getPassword().length == 0) {
                pf.setText(placeholder);
                pf.setForeground(placeholderColor);
                pf.setEchoChar((char) 0);
            }
        }
    }

    private class TextFieldFocusListener extends FocusAdapter {
        private JTextField tf;
        private CustomPanel panel;
        private String placeholder;

        public TextFieldFocusListener(JTextField tf, CustomPanel panel, String placeholder) {
            this.tf = tf;
            this.panel = panel;
            this.placeholder = placeholder;
        }

        @Override
        public void focusGained(FocusEvent evt) {
            panel.setFieldFocused(true);
            if (tf.getText().equals(placeholder)) {
                tf.setText("");
                tf.setForeground(Color.WHITE);
            }
        }

        @Override
        public void focusLost(FocusEvent evt) {
            panel.setFieldFocused(false);
            if (tf.getText().isEmpty()) {
                tf.setText(placeholder);
                tf.setForeground(placeholderColor);
            }
        }
    }

    private class EyeButton extends JButton {
        public EyeButton() {
            super("👁");
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {
                g2.setColor(new Color(60, 60, 60, 100));
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
            }
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    private class CloseButtonMouseListener extends java.awt.event.MouseAdapter {
        private JButton button;

        public CloseButtonMouseListener(JButton button) {
            this.button = button;
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setForeground(new Color(255, 100, 100));
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setForeground(Color.WHITE);
        }
    }

    private class MainPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(25, 25, 25),
                    0, getHeight(), new Color(50, 50, 50));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            g2.setColor(new Color(255, 255, 255, 50));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 25, 25);
            g2.dispose();
        }
    }

    private class HeaderPanel extends JPanel {
        private String title;
        private boolean isSuccess;

        public HeaderPanel(String title, boolean isSuccess) {
            this.title = title;
            this.isSuccess = isSuccess;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color headerColor = isSuccess ? new Color(139, 69, 19, 150) : new Color(80, 80, 80, 150);
            g2.setColor(headerColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            g2.fillRect(0, 0, getWidth(), getHeight() - 12);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(title)) / 2;
            g2.drawString(title, x, 25);
            g2.dispose();
        }
    }

    private class ContentPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(35, 35, 35, 200));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
        }
    }

    private class OKButton extends JButton {
        public OKButton() {
            super("OK");
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int arc = 25;
            Color btnColor = getModel().isPressed() ? new Color(119, 59, 9)
                    : getModel().isRollover() ? hoverColor : buttonColor;
            g2.setColor(btnColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);
            g2.setColor(Color.WHITE);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), x, y);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
        }
    }
}
