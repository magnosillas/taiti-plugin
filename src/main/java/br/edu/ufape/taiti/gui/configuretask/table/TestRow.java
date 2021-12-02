package br.edu.ufape.taiti.gui.configuretask.table;

import java.io.File;

public class TestRow {
    private File file;
    private String test;

    public TestRow(File file, String test) {
        this.file = file;
        this.test = test;
    }

    public File getFile() {
        return file;
    }

    public String getTest() {
        return test;
    }

    @Override
    public boolean equals(Object o) {
        TestRow testRow;
        if (o instanceof TestRow) {
            testRow = (TestRow) o;
            return this.test.equals(testRow.getTest()) && this.file.getAbsolutePath().equals(testRow.getFile().getAbsolutePath());
        }

        return false;
    }

    @Override
    public String toString() {
        return getTest();
    }
}
