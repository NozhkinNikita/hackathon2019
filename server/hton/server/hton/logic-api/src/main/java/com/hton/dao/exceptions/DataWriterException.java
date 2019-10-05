package com.hton.dao.exceptions;

public class DataWriterException extends Exception {

    public DataWriterException() {
    }

    public DataWriterException(String mes) {
        super(mes);
    }

    public DataWriterException(Exception ex) {
        super(ex);
    }

    public DataWriterException(String mes, Throwable cause) {
        super(mes, cause);
    }
}
