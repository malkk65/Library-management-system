package com.library.staff;

import com.library.main.NavigationController;
import javax.swing.*;
import java.awt.*;

public class TransactionsWindow extends StaffBasePage {

    public TransactionsWindow(NavigationController controller) {
        super(controller);
        pageTitleLabel.setText("Transaction Logs");
        highlightSidebarButton("Transaction");
    }

    @Override
    protected JPanel getContentPanel() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(new Color(250, 250, 245)); // Cream/White match
        main.setOpaque(false);
        main.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // ---------- Cards ----------
        main.add(createBookCard("Clean Code", "Menna Elsayed", "2025-01-10", "2025-01-15"));
        main.add(Box.createVerticalStrut(15));
        main.add(createBookCard("Design Patterns", "Ahmed Ali", "2025-01-15", "2025-01-20"));
        main.add(Box.createVerticalStrut(15));
        main.add(createBookCard("Intro to Java", "Sarah Jones", "2025-01-22", "2025-01-29"));

        // Wrap in ScrollPane if list is long?
        // BaseMainPage puts content in a wrapper, ideally we return a scrollable panel
        // if needed.
        // But for consistency let's return a container that holds these.

        return main;
    }

    private JPanel createBookCard(String bookName, String borrower, String borrowDate, String dueDate) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(255, 255, 255)); // White

        // Brown edge + padding
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(102, 51, 0)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setPreferredSize(new Dimension(0, 100)); // Fixed height

        Color brown = new Color(102, 51, 0);

        // LEFT SIDE (book + borrower)
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel title = new JLabel(bookName);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(brown);

        JLabel name = new JLabel("Borrower: " + borrower);
        name.setFont(new Font("Arial", Font.PLAIN, 14));
        name.setForeground(brown);

        left.add(title);
        left.add(Box.createVerticalStrut(8));
        left.add(name);

        // RIGHT SIDE (dates)
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        right.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JLabel borrow = new JLabel("Borrow Date: " + borrowDate);
        borrow.setFont(new Font("Arial", Font.PLAIN, 13));
        borrow.setForeground(brown);

        JLabel date = new JLabel("Due Date: " + dueDate);
        date.setFont(new Font("Arial", Font.PLAIN, 13));
        date.setForeground(brown);

        right.add(borrow);
        right.add(Box.createVerticalStrut(8));
        right.add(date);

        card.add(left, BorderLayout.WEST);
        card.add(right, BorderLayout.EAST);

        return card;
    }
}
