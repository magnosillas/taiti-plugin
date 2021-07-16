package br.edu.ufape.taiti.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TableHeaderRenderer implements TableCellRenderer {

    private JCheckBox checkbox;

    public TableHeaderRenderer(JCheckBox checkbox) {
        this.checkbox = checkbox;
        this.checkbox.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                this.checkbox.setForeground(header.getForeground());
                this.checkbox.setBackground(header.getBackground());
                this.checkbox.setFont(header.getFont());
                this.checkbox.setEnabled(true);
                this.checkbox.setVisible(true);

                // como marcar e desmarcar o checkbox do header?
                header.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (checkbox.isSelected()) {
                            checkbox.setSelected(false);
                        } else {
                            checkbox.setSelected(true);
                        }
                    }
                });

            }
        }

        return this.checkbox;
    }
}
