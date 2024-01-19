package com.mashreq.conferencebooking.exceptions;

public class RecordNotFoundException extends RuntimeException{
    String message;
    public RecordNotFoundException() {
        super();
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}
