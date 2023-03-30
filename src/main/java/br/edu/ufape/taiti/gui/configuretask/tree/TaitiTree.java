package br.edu.ufape.taiti.gui.configuretask.tree;

import com.intellij.ui.treeStructure.Tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.util.ArrayList;

public class TaitiTree extends Tree {

    private final String CUCUMBER_FILES_DIRECTORY = "features";

    public TaitiTree(DefaultTreeModel treeModel) {
        super(treeModel);
    }

    public File findFeatureDirectory(String path) {
        File root = new File(path);
        File[] listFiles = root.listFiles();
        File featuresFolder = null;

        if (listFiles == null) {
            return featuresFolder;
        }

        for (File file : listFiles) {
            if(featuresFolder == null) {
                if (file.isDirectory()) {
                    if (file.getAbsolutePath().endsWith(File.separator + CUCUMBER_FILES_DIRECTORY)) {
                        featuresFolder = file;
                    } else {
                        featuresFolder = findFeatureDirectory(file.getAbsolutePath());
                    }
                }
            }
        }

        return featuresFolder;
    }

    public void addParentsNodeToTree(ArrayList<DefaultMutableTreeNode> parentsNodes, DefaultMutableTreeNode node, int index) {
        if (index < 0) {
            return;
        }
        DefaultMutableTreeNode parentNode = parentsNodes.get(index);
        node.add(parentNode);
        addParentsNodeToTree(parentsNodes, parentNode, index - 1);
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