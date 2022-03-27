package com.yogesh.blog.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private Integer status;
    private String message;
    private long timestamp;
}
