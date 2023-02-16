package br.edu.ufape.taiti.gui.configuretask.table;

import java.io.File;

public class TestRow {
    private File file;
    private Boolean checkbox;
    private String test;

    public TestRow(File file, Boolean checkbox, String test) {
        this.file = file;
        this.checkbox = checkbox;
        this.test = test;
    }

    public File getFile() {
        return file;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public String getTest() {
        return test;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setCheckbox(Boolean checkbox) {
        this.checkbox = checkbox;
    }

    public void setTest(String test) {
        this.test = test;
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
}