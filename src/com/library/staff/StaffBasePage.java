package com.library.staff;

import com.library.common.BaseMainPage;
import com.library.common.UserSession;
import com.library.common.Icons;
import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;

public abstract class StaffBasePage extends BaseMainPage {

    public StaffBasePage(NavigationController controller) {
        super(controller);
    }

    @Override
    protected String getPortalTitle() {
        UserSession.Role role = UserSession.getInstance().getUserRole();
        return (role == UserSession.Role.ADMIN) ? "ADMIN PORTAL" : "STAFF PORTAL";
    }

    @Override
    protected void addSidebarButtons(JPanel sideBar) {
        UserSession.Role role = UserSession.getInstance().getUserRole();
        if (role == null)
            role = UserSession.Role.STAFF; // Default fallback

        // --- Common Buttons ---
        JButton DashboardBtn = createSideButton("Dashboard", Icons.getDashboardIcon(18, Color.GRAY));
        sideBar.add(DashboardBtn);
        DashboardBtn.addActionListener(e -> controller.navigateTo("Dashboard"));

        // --- Role Specific Buttons ---
        if (role == UserSession.Role.STAFF) {
            // OPERATIONAL TASKS
            JButton BooksBtn = createSideButton("Books", Icons.getBookIcon(18, Color.GRAY));
            JButton EBooksBtn = createSideButton("E-Books", Icons.getFileIcon(18, Color.GRAY));
            JButton IssueBookBtn = createSideButton("Transaction", Icons.getListIcon(18, Color.GRAY));

            sideBar.add(BooksBtn);
            sideBar.add(EBooksBtn);
            sideBar.add(IssueBookBtn);

            BooksBtn.addActionListener(e -> controller.navigateTo("BookManagement"));
            EBooksBtn.addActionListener(e -> controller.navigateTo("EBookManagement"));
            IssueBookBtn.addActionListener(e -> controller.navigateTo("Transactions"));

        } else if (role == UserSession.Role.ADMIN) {
            // MANAGERIAL TASKS
            JButton ManageStaffBtn = createSideButton("Manage Staff", Icons.getUserIcon(18, Color.GRAY));
            JButton ReportsBtn = createSideButton("Reports", Icons.getListIcon(18, Color.GRAY));
            JButton SettingsBtn = createSideButton("Global Settings", Icons.getListIcon(18, Color.GRAY));

            sideBar.add(ManageStaffBtn);
            sideBar.add(ReportsBtn);
            sideBar.add(SettingsBtn);

            ManageStaffBtn
                    .addActionListener(e -> JOptionPane.showMessageDialog(this, "Staff Management (Coming Soon)"));
            ReportsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "System Reports (Coming Soon)"));
            SettingsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Global Settings (Coming Soon)"));
        }
    }
}
