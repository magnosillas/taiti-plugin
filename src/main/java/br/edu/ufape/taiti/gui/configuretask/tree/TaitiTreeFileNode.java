package br.edu.ufape.taiti.gui.configuretask.tree;

import java.io.File;

/**
 * Esta classe representa um nó da árvore de arquivos.
 */
public class TaitiTreeFileNode {
    private File file;

    public TaitiTreeFileNode(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
