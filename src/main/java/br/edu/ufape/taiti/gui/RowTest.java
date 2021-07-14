package br.edu.ufape.taiti.gui;

public class RowTest {
    private Boolean checkbox;
    private String test;

    public RowTest(Boolean checkbox, String test) {
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
        RowTest rowTest;
        if (o instanceof RowTest) {
            rowTest = (RowTest) o;
            return this.test.equals(rowTest.getTest());
        }

        return false;
    }
}
