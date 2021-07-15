package br.edu.ufape.taiti.gui;

public class TestRow {
    private Boolean checkbox;
    private String test;

    public TestRow(Boolean checkbox, String test) {
        this.checkbox = checkbox;
        this.test = test;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public String getTest() {
        return test;
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
            return this.test.equals(testRow.getTest());
        }

        return false;
    }
}
