package com.mashreq.conferencebooking.exceptions;

public class InvaildBookingDateTimeException extends RuntimeException
{
    String message;
    public InvaildBookingDateTimeException() {
        super();
    }

    public InvaildBookingDateTimeException(String message) {
        super(message);
    }


}
