package br.edu.ufape.taiti.gui.tree;

import java.io.File;

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
