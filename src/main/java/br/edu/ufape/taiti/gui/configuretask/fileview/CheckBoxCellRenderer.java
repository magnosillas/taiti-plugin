package br.edu.ufape.taiti.gui.configuretask.fileview;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;

/**
 * Classe responsável por renderizar os checkboxs apenas na primeira linha e nas linhas que declaram scenarios, na
 * tabela responsável por mostrar o conteúdo dos arquivos cucumber.
 */
public class CheckBoxCellRenderer implements TableCellRenderer {

    private JCheckBox renderer;
    private File file;

    public CheckBoxCellRenderer(File file) {
        renderer = new JCheckBox();
        this.file = file;
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String fileLine = (String) table.getModel().getValueAt(row, 1);
        if (fileLine.strip().startsWith("Scenario")) {
            Color bg = isSelected ? table.getSelectionBackground() : table.getBackground();
            renderer.setBackground(bg);
            renderer.setEnabled(true);
            renderer.setVisible(true);
            renderer.setSelected(value != null && (Boolean)value);
        } else if (fileLine.strip().equals(file.getName()) && row == 0) {
            Color bg = table.getTableHeader().getBackground();
            renderer.setBackground(bg);
            renderer.setEnabled(true);
            renderer.setVisible(true);
            renderer.setSelected(value != null && (Boolean)value);
        } else {
            return new JLabel("");
        }

        return renderer;
    }
}
