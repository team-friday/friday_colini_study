package com.colini.study.api.constants;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FailureResponse {
    private String msg;
    private String errorCode;

    public FailureResponse(String msg, String errorCode) {
        this.msg = msg;
        this.errorCode = errorCode;
    }
}
