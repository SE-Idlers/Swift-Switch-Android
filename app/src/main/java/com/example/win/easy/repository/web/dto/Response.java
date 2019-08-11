package com.example.win.easy.repository.web.dto;

public class Response {

    private Object body;
    private Status status;
    private String message;

    public static Response success(Object body,String message){
        return new Response(body,Status.Success,message);
    }

    public static Response error(Object body,String message){
        return new Response(body,Status.Error,message);
    }
    public Response(Object body,Status status,String message){
        this.body=body;
        this.status=status;
        this.message=message;
    }

    public Object getBody(){return body;}
    public Status getStatus(){return status;}
    public String getMessage(){return message;}

    public enum Status{
        Success,
        Error
    }
}
