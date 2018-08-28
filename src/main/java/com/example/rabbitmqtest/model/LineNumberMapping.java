package com.example.rabbitmqtest.model;

import lombok.Data;

@Data
public class LineNumberMapping {
    private String id;
    private String externalLineNumber;

    public LineNumberMapping() {
    }

    public LineNumberMapping(String id, String externalLineNumber) {
        this.id = id;
        this.externalLineNumber = externalLineNumber;
    }

}
