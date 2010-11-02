package com.xebia.devradar.domain;

import javax.persistence.Entity;

@Entity
public class Event {

    private String type;

    private String message;


    public String getType() {
        return this.type;
    }


    public void setType(final String type) {
        this.type = type;
    }


    public String getMessage() {
        return this.message;
    }


    public void setMessage(final String message) {
        this.message = message;
    }


}
