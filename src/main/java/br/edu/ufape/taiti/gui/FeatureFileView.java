package br.edu.ufape.taiti.gui;

import com.intellij.ui.table.JBTable;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class FeatureFileView extends JBTable {

    public void setTableWidth() {
        for (int column = 0; column < this.getColumnCount(); column++) {
            TableColumn tableColumn = this.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < this.getRowCount(); row++) {
                TableCellRenderer cellRenderer = this.getCellRenderer(row, column);
                Component c = this.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + this.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            if (column == 0) {
                tableColumn.setPreferredWidth(preferredWidth + 20);
            } else if (column == 1) {
                if (preferredWidth > 530) {
                    tableColumn.setPreferredWidth(preferredWidth + 20);
                } else {
                    tableColumn.setPreferredWidth(530);
                }
            }

        }
    }

}
