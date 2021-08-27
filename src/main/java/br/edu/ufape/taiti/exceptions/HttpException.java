package br.edu.ufape.taiti.exceptions;

public class HttpException extends Exception {

    private String statusText;
    private int statusNumber;

    public HttpException(String statusText, int statusNumber) {
        super(statusText + statusNumber);
        this.statusText = statusText;
        this.statusNumber = statusNumber;
    }

    public String getStatusText() {
        return statusText;
    }

    public int getStatusNumber() {
        return statusNumber;
    }
}
