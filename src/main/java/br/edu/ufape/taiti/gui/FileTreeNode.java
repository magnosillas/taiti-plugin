package br.edu.ufape.taiti.gui;

import java.io.File;

public class FileTreeNode {
    private File file;

    public FileTreeNode(File file) {
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
