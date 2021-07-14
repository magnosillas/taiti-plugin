package br.edu.ufape.taiti.gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class FileTree extends MouseAdapter {

    private JTree tree;
    private ContentPanel contentPanel;

    public FileTree(JTree tree, ContentPanel contentPanel) {
        this.tree = tree;
        this.contentPanel = contentPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            /* if nothing is selected */
            if (node == null) return;

            /* retrieve the node that was selected */
            Object nodeInfo = node.getUserObject();
            if (node.isLeaf() && !node.getAllowsChildren()) {
                FileTreeNode fileTreeNode = (FileTreeNode) nodeInfo;
                contentPanel.updateCenterPanel(fileTreeNode.getFile());
            }
        }
    }

    public File findFeatureDir(String path) {
        File root = new File(path);
        File[] listFiles = root.listFiles();

        if (listFiles == null) return null;

        for (File file : listFiles) {
            if (file.isDirectory()) {
                if (file.getName().equals("features")) {
                    return file;
                }
                findFeatureDir(file.getAbsolutePath());
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
                // ler o conteudo do arquivo e já adicionar na classe. (é uma boa fazer isso?)
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileTreeNode(file), false);
                node.add(newNode);
            }
        }
    }

}
