package com.library.common;

import com.library.main.NavigationController;
import com.library.common.Icons;
import javax.swing.*;
import java.awt.*;

public class ContactUsPage extends BaseMainPage {

    public ContactUsPage(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("Contact Support");
        highlightSidebarButton("Contact Us");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Info
        JLabel info = new JLabel("<html>We'd love to hear from you! Send us a message below.</html>");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setForeground(new Color(80, 80, 80));
        gbc.gridwidth = 2;
        contentPanel.add(info, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;

        // Form
        addFormRow(contentPanel, gbc, gbc.gridy, "Subject:", new JTextField(30));

        JTextArea messageArea = new JTextArea(8, 30);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        gbc.gridy++;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Message:"), gbc);
        gbc.gridx = 1;
        contentPanel.add(new JScrollPane(messageArea), gbc);

        // Send Button
        JButton sendBtn = new JButton("Send Message");
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setBackground(new Color(139, 69, 19));
        sendBtn.setFocusPainted(false);
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        sendBtn.addActionListener(e -> {
            UIUtils.showStyledMessage(this, "Message Sent", "Thank you! We will get back to you soon.", true);
            controller.goBack();
        });

        gbc.gridy++;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        contentPanel.add(sendBtn, gbc);

        return contentPanel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(comp, gbc);
    }

    @Override
    protected String getPortalTitle() {
        return "CONTACT SUPPORT";
    }

    @Override
    protected void addSidebarButtons(JPanel sideBar) {
        JButton BackBtn = createSideButton("Back", Icons.getListIcon(18, Color.GRAY));
        sideBar.add(BackBtn);
        BackBtn.addActionListener(e -> controller.goBack());
    }
}
