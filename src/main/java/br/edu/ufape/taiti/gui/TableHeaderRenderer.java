package br.edu.ufape.taiti.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TableHeaderRenderer implements TableCellRenderer {

    private JCheckBox checkbox;

    public TableHeaderRenderer() {
        this.checkbox = new JCheckBox();
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

                header.add(this.checkbox);
                header.addMouseListener(new MouseAdapter() {
                    // nao funciona direito
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (header.getResizingColumn() == null && e.getClickCount() == 1) {
                            Point p = e.getPoint();

                            int col = header.getTable().columnAtPoint(p);
                            if (col != column || col == -1) return;

                            int index = header.getColumnModel().getColumnIndexAtX(p.x);
                            if (index == -1) return;

                            checkbox.setSelected(!checkbox.isSelected());


                            TableModel tableModel = header.getTable().getModel();
                            for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++) {
                                tableModel.setValueAt("", rowIndex, 0);
                            }

                            header.repaint();
                        }
                    }
                });
            }
        }

        return this.checkbox;
    }

}
