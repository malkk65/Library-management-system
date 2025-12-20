package com.library.member;

import com.library.common.*;
import com.library.main.NavigationController;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProfilePage extends MemberBasePage {

    private Color woodDark = new Color(101, 87, 78);
    private Color accentGold = new Color(210, 169, 110);

    public ProfilePage(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("My Profile");
        highlightSidebarButton("Profile");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setOpaque(false);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        // 1. User Info Header
        scrollContent.add(createProfileHeader());
        scrollContent.add(Box.createVerticalStrut(30));

        // 2. Statistics Row (Small cards)
        scrollContent.add(createStatsRow());
        scrollContent.add(Box.createVerticalStrut(40));

        // 3. Purchase History (E-Books)
        scrollContent.add(createSectionTitle("Purchased E-Books"));
        scrollContent.add(createPurchaseHistory());
        scrollContent.add(Box.createVerticalStrut(40));

        // 4. Borrowing History (Physical)
        scrollContent.add(createSectionTitle("Borrowing History"));
        scrollContent.add(createBorrowingHistoryTable());
        scrollContent.add(Box.createVerticalStrut(40));

        // 5. Fines & Penalties
        scrollContent.add(createSectionTitle("Fines & Fees"));
        scrollContent.add(createFinesSection());
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

    private JPanel createProfileHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(1200, 100));

        JLabel avatar = new JLabel(Icons.getUserIcon(80, accentGold));
        header.add(avatar, BorderLayout.WEST);

        JPanel details = new JPanel(new GridLayout(2, 1, 0, 5));
        details.setOpaque(false);

        String email = UserSession.getInstance().getUserEmail();
        JLabel nameLabel = new JLabel("Welcome, Member");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        nameLabel.setForeground(woodDark);

        JLabel emailLabel = new JLabel(email != null ? email : "member@rafeqlibrary.com");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailLabel.setForeground(Color.GRAY);

        details.add(nameLabel);
        details.add(emailLabel);
        header.add(details, BorderLayout.CENTER);

        return header;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 20, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(1200, 100));

        row.add(createStatCard("Total Purchases", "2 Items", new Color(240, 248, 255)));
        row.add(createStatCard("Active Borrowings", "1 Book", new Color(255, 250, 240)));
        row.add(createStatCard("Pending Fines", "$5.50", new Color(255, 240, 240)));

        return row;
    }

    private JPanel createStatCard(String title, String value, Color bg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, bg.darker()),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        t.setForeground(Color.GRAY);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI Bold", Font.PLAIN, 22));
        v.setForeground(woodDark);

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }

    private JLabel createSectionTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(woodDark);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        return title;
    }

    private JPanel createPurchaseHistory() {
        JPanel container = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        container.setOpaque(false);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        var history = MemberActivityService.getInstance().getPurchaseHistory();
        for (var item : history) {
            container.add(createMicroPurchaseCard(item));
        }
        return container;
    }

    private JPanel createMicroPurchaseCard(MemberActivityService.PurchasedEBook item) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(20, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        card.setPreferredSize(new Dimension(280, 80));

        JLabel icon = new JLabel(Icons.getFileIcon(35, accentGold));
        card.add(icon, BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel title = new JLabel(item.title);
        title.setFont(new Font("Segoe UI Bold", Font.PLAIN, 14));
        JLabel date = new JLabel("Bought: " + item.purchaseDate);
        date.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        date.setForeground(Color.GRAY);
        info.add(title);
        info.add(date);
        card.add(info, BorderLayout.CENTER);

        return card;
    }

    private JPanel createBorrowingHistoryTable() {
        String[] columns = { "Book Title", "Borrow Date", "Due Date", "Status" };
        var history = MemberActivityService.getInstance().getBorrowingHistory();
        Object[][] data = new Object[history.size()][4];

        for (int i = 0; i < history.size(); i++) {
            var b = history.get(i);
            data[i][0] = b.title;
            data[i][1] = b.borrowDate;
            data[i][2] = b.dueDate;
            data[i][3] = b.status;
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI Bold", Font.PLAIN, 14));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(250, 240, 220));

        // Center render
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < 4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new RoundedBorder(20, new Color(230, 230, 230)));
        wrapper.setMaximumSize(new Dimension(1200, 300));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pane.getViewport().setBackground(Color.WHITE);
        wrapper.add(pane, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createFinesSection() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        var fines = MemberActivityService.getInstance().getFines();
        if (fines.isEmpty()) {
            JLabel empty = new JLabel("No pending fines. Good job!");
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setForeground(new Color(0, 150, 50));
            container.add(empty);
        } else {
            for (var fine : fines) {
                container.add(createFineItem(fine));
                container.add(Box.createVerticalStrut(10));
            }
        }
        return container;
    }

    private JPanel createFineItem(MemberActivityService.Fine fine) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setBackground(new Color(255, 245, 245));
        item.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, new Color(255, 200, 200)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        item.setMaximumSize(new Dimension(600, 60));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel reason = new JLabel(fine.reason);
        reason.setFont(new Font("Segoe UI Bold", Font.PLAIN, 14));
        reason.setForeground(new Color(200, 50, 50));

        JLabel amount = new JLabel(String.format("$%.2f", fine.amount));
        amount.setFont(new Font("Segoe UI Bold", Font.PLAIN, 16));
        amount.setForeground(new Color(150, 0, 0));

        item.add(reason, BorderLayout.CENTER);
        item.add(amount, BorderLayout.EAST);

        return item;
    }
}
