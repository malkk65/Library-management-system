package com.library.common;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class Icons {

    private static final Color PRIMARY_COLOR = new Color(60, 60, 60);
    private static final Color ACCENT_COLOR = new Color(210, 169, 110); // Gold/Wood

    // Helper to enable anti-aliasing
    private static void setupGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    public static Icon getDashboardIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);

                int gap = size / 5;
                int rectSize = (size - gap) / 2;

                // Four squares with rounded corners
                g2.fill(new RoundRectangle2D.Float(x, y, rectSize, rectSize, 4, 4));
                g2.fill(new RoundRectangle2D.Float(x + rectSize + gap, y, rectSize, rectSize, 4, 4));
                g2.fill(new RoundRectangle2D.Float(x, y + rectSize + gap, rectSize, rectSize, 4, 4));
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 120));
                g2.fill(new RoundRectangle2D.Float(x + rectSize + gap, y + rectSize + gap, rectSize, rectSize, 4, 4));

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

    public static Icon getBookIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);

                int w = size * 3 / 4;
                int h = size;
                int curve = size / 4;

                Path2D path = new Path2D.Float();
                path.moveTo(x + w, y);
                path.lineTo(x + w, y + h);
                path.lineTo(x + curve, y + h);
                path.curveTo(x, y + h, x, y + h - curve / 2, x, y + h - curve);
                path.lineTo(x, y + curve);
                path.curveTo(x, y, x + curve, y, x + curve, y);
                path.closePath();

                g2.fill(path);

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(Math.max(size / 15f, 1f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + w - size / 5, y + size / 5, x + w - size / 5, y + h - size / 5);

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

    public static Icon getFileIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);

                int w = size * 3 / 4;
                int h = size;
                int fold = size / 4;

                Path2D doc = new Path2D.Float();
                doc.moveTo(x, y);
                doc.lineTo(x + w - fold, y);
                doc.lineTo(x + w, y + fold);
                doc.lineTo(x + w, y + h);
                doc.lineTo(x, y + h);
                doc.closePath();
                g2.fill(doc);

                g2.setColor(new Color(255, 255, 255, 100));
                Path2D foldShape = new Path2D.Float();
                foldShape.moveTo(x + w - fold, y);
                foldShape.lineTo(x + w - fold, y + fold);
                foldShape.lineTo(x + w, y + fold);
                foldShape.closePath();
                g2.fill(foldShape);

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

    public static Icon getUserIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);

                int headSize = (int) (size * 0.4);
                int centerX = x + size / 2;

                g2.fillOval(centerX - headSize / 2, y, headSize, headSize);

                int bodyW = (int) (size * 0.8);
                int bodyH = (int) (size * 0.5);
                g2.fillArc(centerX - bodyW / 2, y + size - bodyH, bodyW, bodyH * 2, 0, 180);

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

    public static Icon getHelpIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);

                g2.fillOval(x, y, size, size);

                g2.setColor(Color.WHITE);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, Math.max(size * 0.7f, 1f)));
                FontMetrics fm = g2.getFontMetrics();
                String text = "?";
                int textX = x + (size - fm.stringWidth(text)) / 2;
                int textY = y + (size + fm.getAscent()) / 2 - fm.getDescent();
                g2.drawString(text, textX, textY);

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

    public static Icon getListIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);

                int r = size / 6;
                int gap = size / 3;

                for (int i = 0; i < 3; i++) {
                    int cy = y + r / 2 + (i * gap);
                    g2.fillOval(x, cy, r, r);
                    g2.fillRoundRect(x + r + 4, cy, size - r - 4, r, 2, 2);
                }

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

    // ===== Navbar icons =====

    public static Icon getMenuIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(size / 8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int h = size;
                int step = h / 3;

                g2.drawLine(x, y + step / 2, x + size, y + step / 2);
                g2.drawLine(x, y + h / 2, x + size, y + h / 2);
                g2.drawLine(x, y + h - step / 2, x + size, y + h - step / 2);

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

    public static Icon getLogoutIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);

                int w = size * 3 / 4;
                int h = size;

                g2.setStroke(new BasicStroke(size / 10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + w, y, x + w, y + h);
                g2.drawLine(x + w / 2, y, x + w, y);
                g2.drawLine(x + w / 2, y + h, x + w, y + h);

                Path2D arrow = new Path2D.Float();
                int arrowY = y + h / 2;
                arrow.moveTo(x + w / 2 + 2, arrowY);
                arrow.lineTo(x, arrowY);
                g2.draw(arrow);

                Path2D head = new Path2D.Float();
                head.moveTo(x + size / 4, arrowY - size / 4);
                head.lineTo(x, arrowY);
                head.lineTo(x + size / 4, arrowY + size / 4);
                g2.draw(head);

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

    public static Icon getLockIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);

                int w = size * 6 / 10;
                int h = size * 5 / 10;
                int bodyY = y + size - h;
                int bodyX = x + (size - w) / 2;

                g2.fillRoundRect(bodyX, bodyY, w, h, size / 5, size / 5);

                int shackleW = w - size / 5;
                int shackleH = size / 2;
                g2.setStroke(new BasicStroke(size / 8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(x + (size - shackleW) / 2, y + size / 10, shackleW, shackleH, 0, 180);

                g2.setColor(Color.WHITE);
                g2.fillOval(x + size / 2 - size / 10, bodyY + h / 2 - size / 10, size / 5, size / 5);

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

    public static Icon getEmailIcon(int size, Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                setupGraphics(g2);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(size / 12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int w = size;
                int h = size * 3 / 4;
                int oy = y + (size - h) / 2;

                g2.drawRoundRect(x, oy, w, h, size / 5, size / 5);

                Path2D flap = new Path2D.Float();
                flap.moveTo(x, oy + h / 4);
                flap.lineTo(x + w / 2, oy + h / 2 + size / 10);
                flap.lineTo(x + w, oy + h / 4);
                g2.draw(flap);

                g2.dispose();
            }

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }
        };
    }

}
