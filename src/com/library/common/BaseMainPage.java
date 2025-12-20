package com.library.common;

import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;

public abstract class BaseMainPage extends JPanel {

    protected NavigationController controller;
    protected JPanel sideBar;
    protected JPanel content;
    protected JLabel pageTitleLabel;

    protected boolean isSideBarVisible = true;
    protected final int SIDEBAR_WIDTH = 220;
    protected String activeSectionName = ""; // Explicitly track active section

    // Unified brown colors
    protected final Color woodDark = new Color(139, 69, 19); // Primary Brown
    protected final Color woodLight = new Color(210, 169, 110); // Secondary/Lighter Wood
    protected final Color whiteColor = Color.WHITE;
    protected final Color buttonTextDefault = Color.BLACK;
    protected final Color contentDefault = new Color(250, 250, 245);

    public BaseMainPage(NavigationController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // 1. Navbar
        add(createNavBar(), BorderLayout.NORTH);

        // 2. Main Container for Sidebar and Content
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(contentDefault);

        createSideBar();
        mainContainer.add(sideBar, BorderLayout.WEST);

        // 3. Content
        content = new JPanel(new BorderLayout());
        content.setBackground(contentDefault);

        // Content Title Header
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(contentDefault);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        pageTitleLabel = new JLabel("Dashboard");
        pageTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        pageTitleLabel.setForeground(new Color(40, 40, 40));
        titlePanel.add(pageTitleLabel);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(contentDefault);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        contentWrapper.add(titlePanel, BorderLayout.NORTH);
        contentWrapper.add(getContentPanel(), BorderLayout.CENTER);

        content.add(contentWrapper, BorderLayout.CENTER);
        mainContainer.add(content, BorderLayout.CENTER);

        add(mainContainer, BorderLayout.CENTER);
    }

    protected abstract JPanel getContentPanel();

    protected abstract String getPortalTitle(); // e.g. "MEMBER PORTAL" or "STAFF PORTAL"

    protected abstract void addSidebarButtons(JPanel sideBar); // Subclasses add their specific buttons here

    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setPreferredSize(new Dimension(0, 70));
        navBar.setBackground(whiteColor);

        // Subtle shadow/border
        navBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 235, 235)),
                BorderFactory.createEmptyBorder(0, 20, 0, 20)));

        // Left Side: Toggle & Logo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 18));
        leftPanel.setOpaque(false);

        JButton menuToggleBtn = new JButton(Icons.getMenuIcon(22, new Color(50, 50, 50))) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0, 0, 0, 20));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        menuToggleBtn.setPreferredSize(new Dimension(38, 38));
        menuToggleBtn.setFocusPainted(false);
        menuToggleBtn.setContentAreaFilled(false);
        menuToggleBtn.setBorderPainted(false);
        menuToggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuToggleBtn.addActionListener(e -> toggleSideBar());

        // App Logo/Title
        JLabel appTitle = new JLabel("Rafeq Library");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appTitle.setForeground(woodDark);

        leftPanel.add(menuToggleBtn);
        leftPanel.add(appTitle);

        navBar.add(leftPanel, BorderLayout.WEST);

        // Right Side: User Profile & Actions
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        rightPanel.setOpaque(false);

        JLabel userLabel = new JLabel("User Account");
        userLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        userLabel.setForeground(new Color(80, 80, 80));

        JLabel avatar = new JLabel(Icons.getUserIcon(32, woodDark));

        rightPanel.add(userLabel);
        rightPanel.add(avatar);
        rightPanel.add(Box.createHorizontalStrut(10));

        // Logout Button
        JButton logoutBtn = new JButton(Icons.getLogoutIcon(20, new Color(220, 80, 80))) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(220, 80, 80, 30));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        logoutBtn.setPreferredSize(new Dimension(38, 38));
        logoutBtn.setToolTipText("Logout System");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            boolean confirm = UIUtils.showConfirmDialog(this, "Logout", "Are you sure you want to log out?");
            if (confirm) {
                UserSession.getInstance().logout();
                controller.navigateTo("Welcome");
            }
        });

        rightPanel.add(logoutBtn);

        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }

    private JPanel createSideBar() {
        sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        sideBar.setBackground(whiteColor);
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(240, 240, 240)));
        sideBar.add(Box.createVerticalStrut(30));

        addSidebarButtons(sideBar);

        return sideBar;
    }

    private void toggleSideBar() {
        isSideBarVisible = !isSideBarVisible;
        if (isSideBarVisible) {
            sideBar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        } else {
            sideBar.setPreferredSize(new Dimension(0, 0));
        }
        revalidate();
        repaint();
    }

    // Helper to create styled sidebar buttons
    protected JButton createSideButton(String text, Icon icon) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // If not standard white, we paint a custom background
                if (!getBackground().equals(whiteColor)) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    // Full-width hover with rounded corners
                    g2.fillRoundRect(5, 4, getWidth() - 10, getHeight() - 8, 10, 10);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconTextGap(15);
        }
        btn.setMaximumSize(new Dimension(SIDEBAR_WIDTH, 50));
        btn.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(whiteColor);
        btn.setForeground(new Color(60, 60, 60));
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 10));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.getBackground().equals(whiteColor)) {
                    btn.setBackground(new Color(139, 69, 19, 25));
                } else if (btn.getBackground().equals(woodDark)) {
                    btn.setBackground(new Color(160, 80, 20)); // Stronger brown on hover when active
                }
                btn.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn.getText().equalsIgnoreCase(activeSectionName)) {
                    btn.setBackground(woodDark);
                } else {
                    btn.setBackground(whiteColor);
                }
                btn.repaint();
            }
        });

        return btn;
    }

    protected void setActiveButton(JButton activeBtn) {
        activeSectionName = activeBtn.getText(); // Update active section state
        for (Component c : sideBar.getComponents()) {
            if (c instanceof JButton btn) {
                btn.setBackground(whiteColor);
                btn.setForeground(new Color(60, 60, 60));
                btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
            }
        }
        activeBtn.setBackground(woodDark);
        activeBtn.setForeground(Color.WHITE);
        activeBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        activeBtn.repaint();
    }

    protected void highlightSidebarButton(String name) {
        for (Component c : sideBar.getComponents()) {
            if (c instanceof JButton btn && btn.getText().equalsIgnoreCase(name)) {
                setActiveButton(btn);
                break;
            }
        }
    }
}
