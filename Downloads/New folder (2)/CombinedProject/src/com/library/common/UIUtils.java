package com.library.common;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UIUtils {
    public static final Color BACKGROUND_CREAM = new Color(248, 245, 240);
    public static final Color SIDEBAR_BROWN = new Color(54, 38, 27);
    public static final Color HEADER_BROWN = new Color(89, 66, 50);
    public static final Color ACTIVE_TEXT_COLOR = new Color(225, 203, 168);

    // Styled Message Dialog (Notification)
    public static void showStyledMessage(Component parent, String title, String message, boolean isSuccess) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), title,
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parent);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(248, 245, 240)); // Cream
        content.setBorder(BorderFactory.createLineBorder(new Color(210, 169, 110), 2));

        JLabel iconLabel = new JLabel(isSuccess ? "✅" : "❌", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        content.add(iconLabel, BorderLayout.NORTH);

        JLabel msgLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        msgLabel.setForeground(new Color(80, 80, 80));
        content.add(msgLabel, BorderLayout.CENTER);

        JButton ok = new JButton("OK");
        ok.setBackground(new Color(139, 69, 19));
        ok.setForeground(Color.WHITE);
        ok.setFocusPainted(false);
        ok.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(ok);
        content.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(content);
        dialog.setVisible(true);
    }

    // Styled Confirmation Dialog
    public static boolean showConfirmDialog(Component parent, String title, String message) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), title,
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parent);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(248, 245, 240)); // Cream
        content.setBorder(BorderFactory.createLineBorder(new Color(210, 169, 110), 2));

        JLabel iconLabel = new JLabel("❓", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        content.add(iconLabel, BorderLayout.NORTH);

        JLabel msgLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        msgLabel.setForeground(new Color(80, 80, 80));
        content.add(msgLabel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        JButton yesBtn = new JButton("Yes");
        yesBtn.setBackground(new Color(139, 69, 19));
        yesBtn.setForeground(Color.WHITE);
        yesBtn.setFocusPainted(false);
        yesBtn.setPreferredSize(new Dimension(80, 35));

        JButton noBtn = new JButton("No");
        noBtn.setBackground(new Color(160, 160, 160));
        noBtn.setForeground(Color.WHITE);
        noBtn.setFocusPainted(false);
        noBtn.setPreferredSize(new Dimension(80, 35));

        // Use array to hold result since lambda variables must be final
        final boolean[] result = { false };

        yesBtn.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        noBtn.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        btnPanel.add(yesBtn);
        btnPanel.add(noBtn);
        content.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(content);
        dialog.setVisible(true);

        return result[0];
    }

    // Legacy icons - kept for compatibility
    public static Image createDashboardIcon() {
        BufferedImage img = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(5, 5, 9, 9);
        g2.fillRect(16, 5, 9, 9);
        g2.fillRect(5, 16, 9, 9);
        g2.fillRect(16, 16, 9, 9);
        g2.dispose();
        return img;
    }

    public static Image createActiveIcon() {
        BufferedImage img = new BufferedImage(10, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(ACTIVE_TEXT_COLOR);
        g2.fillPolygon(new int[] { 0, 10, 0 }, new int[] { 0, 10, 20 }, 3);
        g2.dispose();
        return img;
    }
}
