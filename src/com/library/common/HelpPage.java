package com.library.common;

import com.library.main.NavigationController;
import com.library.common.Icons;
import javax.swing.*;
import java.awt.*;

public class HelpPage extends BaseMainPage {

    public HelpPage(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("Help & FAQ");
        highlightSidebarButton("Help");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addFaqItem(contentPanel, "How do I borrow a book?",
                "Navigate to 'Browse Books', find the book you want, and take it to the reception desk. " +
                        "In the future, you will be able to reserve it directly from the app.");

        addFaqItem(contentPanel, "How do I return a book?",
                "Bring the book back to the library and hand it to a staff member. " +
                        "They will update the status in the system.");

        addFaqItem(contentPanel, "How do I read E-Books?",
                "Go to the 'E-Books' section. Click on any book card to open the file on your device. " +
                        "Make sure you have a PDF viewer installed.");

        addFaqItem(contentPanel, "I forgot my password.",
                "Go to the login screen and click 'Forgot Password'. You will be able to reset it there.");

        return contentPanel;
    }

    private void addFaqItem(JPanel panel, String question, String answer) {
        JPanel item = new JPanel(new BorderLayout());
        item.setOpaque(false);
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        item.setMaximumSize(new Dimension(1000, 120));

        JLabel qLabel = new JLabel("Q: " + question);
        qLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        qLabel.setForeground(new Color(139, 69, 19));
        qLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JTextArea aArea = new JTextArea(answer);
        aArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aArea.setForeground(new Color(80, 80, 80));
        aArea.setLineWrap(true);
        aArea.setWrapStyleWord(true);
        aArea.setEditable(false);
        aArea.setOpaque(false);
        aArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        item.add(qLabel, BorderLayout.NORTH);
        item.add(aArea, BorderLayout.CENTER);

        panel.add(item);
        panel.add(Box.createVerticalStrut(10));
    }

    @Override
    protected String getPortalTitle() {
        // This page might be shared, but for now we fallback to generic or check user
        return "LIBRARY HELP";
    }

    @Override
    protected void addSidebarButtons(JPanel sideBar) {
        // If this page is used, it usually reuses the sidebar of the caller.
        // But since we inherit BaseMainPage, we must implement.
        // Ideally we shouldn't duplicate sidebar logic.
        // For simplicity, we can just put a "Back" button or standard member buttons if
        // we assume Member context.
        // Let's implement standard Member buttons for consistency if accessed by
        // Member.

        JButton BackBtn = createSideButton("Back", Icons.getListIcon(18, Color.GRAY));
        sideBar.add(BackBtn);
        BackBtn.addActionListener(e -> controller.goBack());
    }
}
