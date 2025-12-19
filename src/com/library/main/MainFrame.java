package com.library.main;

import com.library.auth.*;
import com.library.member.HomeMember;
import com.library.member.MemberBookView;
import com.library.member.MemberEBookView;
import com.library.staff.EBookManagement;
import com.library.staff.BookManagement;
import com.library.staff.Dashboard;
import com.library.staff.TransactionsWindow;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MainFrame extends JFrame implements NavigationController {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Stack<String> navigationHistory;

    // References to pages
    private SignUpPage signUpPage;
    private ResetPasswordPage resetPasswordPage;

    public MainFrame() {
        // Setup Window
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 800); // Larger size for portals
        setLocationRelativeTo(null);
        setResizable(true);

        // Apply Theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            // Optionally set lighter theme if preferred, or handle per panel
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatDarkLaf");
        }

        // Initialize Navigation
        navigationHistory = new Stack<>();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize Auth Pages
        WelcomePage welcomePage = new WelcomePage(this);
        StaffLoginPage staffLoginPage = new StaffLoginPage(this);
        MemberLoginPage memberLoginPage = new MemberLoginPage(this);
        signUpPage = new SignUpPage(this);
        resetPasswordPage = new ResetPasswordPage(this);

        // Initialize System Portals
        HomeMember homeMember = new HomeMember(this);
        MemberBookView memberBookView = new MemberBookView(this);

        Dashboard staffDashboard = new Dashboard(this);
        BookManagement bookManagement = new BookManagement(this);
        EBookManagement ebookManagement = new EBookManagement(this);
        TransactionsWindow transactionsWindow = new TransactionsWindow(this); // Sub-panel often?

        // Add pages to CardLayout
        cardPanel.add(welcomePage, "Welcome");
        cardPanel.add(staffLoginPage, "StaffLogin");
        cardPanel.add(memberLoginPage, "MemberLogin");
        cardPanel.add(signUpPage, "SignUp");
        cardPanel.add(resetPasswordPage, "ResetPassword");

        cardPanel.add(homeMember, "HomeMember");
        cardPanel.add(memberBookView, "MemberBookView");
        cardPanel.add(new MemberEBookView(this), "MemberEBookView");
        cardPanel.add(new com.library.common.HelpPage(this), "HelpPage");
        cardPanel.add(new com.library.common.ContactUsPage(this), "ContactUsPage");

        cardPanel.add(staffDashboard, "Dashboard");
        cardPanel.add(bookManagement, "BookManagement");
        cardPanel.add(ebookManagement, "EBookManagement");
        cardPanel.add(transactionsWindow, "Transactions"); // Use if needed as top
        // level

        add(cardPanel);

        // Start with Welcome Page
        navigateTo("Welcome");
    }

    @Override
    public void navigateTo(String viewName) {
        if (!navigationHistory.isEmpty() && !navigationHistory.peek().equals(viewName)) {
            // Logic to avoid simple duplicate pushes can be added here
        }
        navigationHistory.push(viewName);
        cardLayout.show(cardPanel, viewName);
    }

    @Override
    public void navigateTo(String viewName, Object... args) {
        // Handle argument passing
        if ("SignUp".equals(viewName) && args.length > 0 && args[0] instanceof String) {
            signUpPage.setRole((String) args[0]);
        }
        if ("ResetPassword".equals(viewName) && args.length > 0 && args[0] instanceof String) {
            resetPasswordPage.setRole((String) args[0]);
        }

        navigateTo(viewName);
    }

    @Override
    public void goBack() {
        if (navigationHistory.size() > 1) {
            navigationHistory.pop(); // Remove current page
            String previousPage = navigationHistory.peek(); // Get previous page
            cardLayout.show(cardPanel, previousPage);
        } else {
            // If unlimited back? usually limit at Welcome
        }
    }
}
