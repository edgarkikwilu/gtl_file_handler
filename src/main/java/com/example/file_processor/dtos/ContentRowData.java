package com.example.file_processor.dtos;

import lombok.*;

//@Setter
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor

//@Data
public class ContentRowData {
    private String name;
    private String email;
    private String message;


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
