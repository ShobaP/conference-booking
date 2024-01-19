package com.mashreq.conferencebooking.dto;

import org.springframework.http.HttpStatusCode;

public record Response(HttpStatusCode status, String message, String error)  {

//    private HttpStatusCode status;
//    private String message;
//
//    private String error;
//
//    public Response(HttpStatusCode status, String message, Throwable ex) {
//        this.status = status;
//        this.message = message;
//        this.error = ex.getLocalizedMessage();
//    }
}
