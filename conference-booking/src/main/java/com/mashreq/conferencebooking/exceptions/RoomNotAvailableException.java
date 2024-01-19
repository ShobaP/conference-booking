package com.mashreq.conferencebooking.exceptions;

public class RoomNotAvailableException extends RuntimeException{
    String message;
    public RoomNotAvailableException() {
        super();
    }

    public RoomNotAvailableException(String message) {
        super(message);
    }

}
