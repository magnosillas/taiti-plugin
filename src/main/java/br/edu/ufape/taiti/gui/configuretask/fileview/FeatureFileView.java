package br.edu.ufape.taiti.gui.configuretask.fileview;

import com.intellij.ui.table.JBTable;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Esta classe representa uma tabela onde é mostrado o conteúdo do arquivo.
 */
public class FeatureFileView extends JBTable {

    /**
     * Este método é responsável por mudar a largura da tabela de acordo com o conteúdo do arquivo.
     */
    public void setTableWidth(int parentComponentWidth) {
        int firstColumnWidth = 0;
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
                firstColumnWidth = preferredWidth + 20;
                tableColumn.setPreferredWidth(firstColumnWidth);
            } else {
                tableColumn.setPreferredWidth(Math.max(parentComponentWidth - firstColumnWidth - 15, preferredWidth + 20));
            }
        }
    }

}
