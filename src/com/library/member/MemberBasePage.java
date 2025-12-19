package com.library.member;

import com.library.common.BaseMainPage;
import com.library.common.Icons;
import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;

public abstract class MemberBasePage extends BaseMainPage {

    public MemberBasePage(NavigationController controller) {
        super(controller);
    }

    @Override
    protected String getPortalTitle() {
        return "MEMBER PORTAL";
    }

    @Override
    protected void addSidebarButtons(JPanel sideBar) {
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
        ProfileBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Profile Page (Coming Soon)"));
        ContactBtn.addActionListener(e -> controller.navigateTo("ContactUsPage"));
        HelpBtn.addActionListener(e -> controller.navigateTo("HelpPage"));
    }
}
