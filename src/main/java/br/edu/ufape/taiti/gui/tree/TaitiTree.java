package br.edu.ufape.taiti.gui.tree;

import com.intellij.ui.treeStructure.Tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

public class TaitiTree extends Tree {

    private final String CUCUMBER_FILES_DIRECTORY = "features";

    public TaitiTree(DefaultTreeModel treeModel) {
        super(treeModel);
    }

    public File findFeatureDirectory(String path) {
        File root = new File(path);
        File[] listFiles = root.listFiles();

        if (listFiles == null) return null;

        for (File file : listFiles) {
            if (file.isDirectory()) {
                if (file.getName().equals(CUCUMBER_FILES_DIRECTORY)) {
                    return file;
                }
                findFeatureDirectory(file.getAbsolutePath());
            }
        }

        return null;
    }

    public void addNodesToTree(String path, DefaultMutableTreeNode node) {
        File root = new File(path);
        File[] listFiles = root.listFiles();

        if (listFiles == null) return;

        for (File file : listFiles) {
            if (file.isDirectory()) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file.getName());
                node.add(newNode);
                addNodesToTree(file.getAbsolutePath(), newNode);
            } else if (file.isFile()) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new TaitiTreeFileNode(file), false);
                node.add(newNode);
            }
        }
    }

}
