package br.edu.ufape.taiti.exceptions;

/**
 * Exceção usada quando um erro de requisição ocorre ao consultar o Pivotal Tracker.
 */
public class HttpException extends Exception {

    private final String statusText;
    private final int statusNumber;

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
