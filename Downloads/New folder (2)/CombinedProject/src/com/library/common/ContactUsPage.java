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
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setOpaque(false);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Horizontal padding for balance

        // 1. Support Info Section (Centered)
        JPanel infoHeaderPanel = new JPanel();
        infoHeaderPanel.setLayout(new BoxLayout(infoHeaderPanel, BoxLayout.Y_AXIS));
        infoHeaderPanel.setOpaque(false);
        infoHeaderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoTitle = new JLabel("How can we help?");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Larger title
        infoTitle.setForeground(woodDark);
        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        infoHeaderPanel.add(infoTitle);

        JPanel contactCards = new JPanel(new GridLayout(1, 2, 40, 0));
        contactCards.setOpaque(false);
        contactCards.setMaximumSize(new Dimension(850, 110)); // Wide and centered
        contactCards.setAlignmentX(Component.CENTER_ALIGNMENT);

        contactCards.add(createContactCard("Email Support", "support@rafeqlibrary.com"));
        contactCards.add(createContactCard("Staff Hotline", "+1 (555) 123-4567"));

        infoHeaderPanel.add(contactCards);
        scrollContent.add(infoHeaderPanel);
        scrollContent.add(Box.createVerticalStrut(50));

        // 2. Message Form Section (Centered & Large)
        JPanel formSection = createSectionContainer();
        formSection.setAlignmentX(Component.CENTER_ALIGNMENT);
        formSection.setMaximumSize(new Dimension(850, 520)); // Substantial presence

        JLabel formTitle = new JLabel("Send us a Message");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        formTitle.setForeground(woodDark);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formSection.add(formTitle);

        JTextField subjectField = new JTextField();
        subjectField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subjectField.setBackground(Color.WHITE);
        subjectField.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 180, 140)), "Subject"));
        subjectField.setMaximumSize(new Dimension(1000, 65));
        formSection.add(subjectField);
        formSection.add(Box.createVerticalStrut(20));

        JTextArea messageArea = new JTextArea(10, 20);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        messageArea.setBackground(Color.WHITE);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollMsg = new JScrollPane(messageArea);
        scrollMsg.setBackground(Color.WHITE);
        scrollMsg.getViewport().setBackground(Color.WHITE);
        scrollMsg.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 180, 140)), "Message"));
        scrollMsg.setMaximumSize(new Dimension(1000, 240));
        formSection.add(scrollMsg);
        formSection.add(Box.createVerticalStrut(30));

        JButton sendBtn = new JButton("Send Message");
        sendBtn.setBackground(woodDark);
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        sendBtn.setFocusPainted(false);
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        sendBtn.setMaximumSize(new Dimension(220, 55));
        sendBtn.addActionListener(e -> {
            if (subjectField.getText().trim().isEmpty() || messageArea.getText().trim().isEmpty()) {
                UIUtils.showStyledMessage(this, "Error", "Please fill in all fields.", false);
                return;
            }
            UIUtils.showStyledMessage(this, "Sent", "Thank you! Our team will respond shortly.", true);
            subjectField.setText("");
            messageArea.setText("");
        });
        formSection.add(sendBtn);

        scrollContent.add(formSection);
        scrollContent.add(Box.createVerticalGlue());

        // Main Wrapper with Scroll
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
        return container;
    }

    private JPanel createContactCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 253, 245)); // Light cream
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(245, 235, 210)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI Bold", Font.PLAIN, 14));
        valueLabel.setForeground(new Color(80, 80, 80));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    @Override
    protected String getPortalTitle() {
        return "CONTACT SUPPORT";
    }

    @Override
    protected void addSidebarButtons(JPanel sideBar) {
        UserSession.Role role = UserSession.getInstance().getUserRole();

        if (role == null) {
            JButton BackBtn = createSideButton("Back Overview", Icons.getDashboardIcon(18, Color.GRAY));
            sideBar.add(BackBtn);
            BackBtn.addActionListener(e -> controller.goBack());
            return;
        }

        if (role == UserSession.Role.MEMBER) {
            JButton DashboardBtn = createSideButton("Dashboard", Icons.getDashboardIcon(18, Color.GRAY));
            JButton BrowseBooksBtn = createSideButton("Books", Icons.getBookIcon(18, Color.GRAY));
            JButton EBooksBtn = createSideButton("E-Books", Icons.getFileIcon(18, Color.GRAY));
            JButton ProfileBtn = createSideButton("Profile", Icons.getUserIcon(18, Color.GRAY));
            JButton ContactBtn = createSideButton("Contact Us", Icons.getHelpIcon(18, Color.GRAY));
            JButton HelpBtn = createSideButton("Help", Icons.getHelpIcon(18, Color.GRAY));

            sideBar.add(DashboardBtn);
            sideBar.add(BrowseBooksBtn);
            sideBar.add(EBooksBtn);
            sideBar.add(ProfileBtn);
            sideBar.add(ContactBtn);
            sideBar.add(HelpBtn);

            DashboardBtn.addActionListener(e -> controller.navigateTo("HomeMember"));
            BrowseBooksBtn.addActionListener(e -> controller.navigateTo("MemberBookView"));
            EBooksBtn.addActionListener(e -> controller.navigateTo("MemberEBookView"));
            ProfileBtn.addActionListener(e -> controller.navigateTo("ProfilePage"));
            ContactBtn.addActionListener(e -> controller.navigateTo("ContactUsPage"));
            HelpBtn.addActionListener(e -> controller.navigateTo("HelpPage"));

        } else {
            JButton DashboardBtn = createSideButton("Dashboard", Icons.getDashboardIcon(18, Color.GRAY));
            JButton BooksBtn = createSideButton("Books", Icons.getBookIcon(18, Color.GRAY));
            JButton EBooksBtn = createSideButton("E-Books", Icons.getFileIcon(18, Color.GRAY));
            JButton HelpBtn = createSideButton("Help", Icons.getHelpIcon(18, Color.GRAY));

            sideBar.add(DashboardBtn);
            sideBar.add(BooksBtn);
            sideBar.add(EBooksBtn);
            sideBar.add(HelpBtn);

            DashboardBtn.addActionListener(e -> controller.navigateTo("Dashboard"));
            BooksBtn.addActionListener(e -> controller.navigateTo("BookManagement"));
            EBooksBtn.addActionListener(e -> controller.navigateTo("EBookManagement"));
            HelpBtn.addActionListener(e -> controller.navigateTo("HelpPage"));
        }
    }
}
