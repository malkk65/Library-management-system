package com.library.common;

import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;

public class HelpPage extends BaseMainPage {

    public HelpPage(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("Frequently Asked Questions");
        highlightSidebarButton("Help");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setOpaque(false);

        // 1. FAQ Section Container
        JPanel faqSection = createSectionContainer();
        faqSection.setLayout(new BorderLayout()); // Use BorderLayout for stability

        JLabel faqTitle = new JLabel("Common Questions");
        faqTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        faqTitle.setForeground(woodDark);
        faqTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        faqSection.add(faqTitle, BorderLayout.NORTH);

        JPanel accordionContainer = new JPanel();
        accordionContainer.setLayout(new BoxLayout(accordionContainer, BoxLayout.Y_AXIS));
        accordionContainer.setOpaque(false);

        // Add FAQ items to the internal container
        addFaqItem(accordionContainer, "How do I borrow a book?",
                "Navigate to 'Books', find your desired title, and click 'Borrow'. If it's a physical book, you'll need to pick it up at the desk.");
        addFaqItem(accordionContainer, "Where can I find E-Books?",
                "E-Books are available in the 'E-Books' section. You can read them directly in your browser or download the PDF.");
        addFaqItem(accordionContainer, "How do I reset my password?",
                "Click on 'Forgot Password' on the login screen and follow the instructions sent to your email.");
        addFaqItem(accordionContainer, "How can I contact staff?",
                "You can use the 'Contact Us' page in the sidebar to send a direct message to our support team.");

        faqSection.add(accordionContainer, BorderLayout.CENTER);

        scrollContent.add(faqSection);
        scrollContent.add(Box.createVerticalGlue());

        // Wrapper with Scroll
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        JScrollPane mainScroll = new JScrollPane(scrollContent);
        mainScroll.setBorder(null);
        mainScroll.setOpaque(false);
        mainScroll.getViewport().setOpaque(false);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(mainScroll, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createSectionContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(25, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.setMaximumSize(new Dimension(1000, 1000));
        return container;
    }

    private void addFaqItem(JPanel container, String question, String answer) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setOpaque(false);
        itemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));

        // Question Row
        JPanel qRow = new JPanel(new BorderLayout());
        qRow.setOpaque(false);
        qRow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        qRow.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel qLabel = new JLabel(question);
        qLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        qLabel.setForeground(new Color(60, 60, 60));

        JLabel chevron = new JLabel("›");
        chevron.setFont(new Font("Segoe UI", Font.BOLD, 20));
        chevron.setForeground(Color.GRAY);

        qRow.add(qLabel, BorderLayout.WEST);
        qRow.add(chevron, BorderLayout.EAST);

        // Answer Row (Hidden by default)
        JPanel aRow = new JPanel(new BorderLayout());
        aRow.setOpaque(false);
        aRow.setVisible(false);
        aRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 50));

        JTextArea aText = new JTextArea(answer);
        aText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aText.setForeground(new Color(100, 100, 100));
        aText.setLineWrap(true);
        aText.setWrapStyleWord(true);
        aText.setEditable(false);
        aText.setOpaque(false);
        aRow.add(aText, BorderLayout.CENTER);

        // Click Logic
        qRow.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                boolean isExpanded = aRow.isVisible();
                aRow.setVisible(!isExpanded);
                chevron.setText(isExpanded ? "›" : "⌄");
                chevron.setForeground(isExpanded ? Color.GRAY : woodDark);
                qLabel.setForeground(isExpanded ? new Color(60, 60, 60) : woodDark);
                container.revalidate();
                container.repaint();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                qLabel.setForeground(woodDark);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!aRow.isVisible()) {
                    qLabel.setForeground(new Color(60, 60, 60));
                }
            }
        });

        itemPanel.add(qRow);
        itemPanel.add(aRow);
        container.add(itemPanel);
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
