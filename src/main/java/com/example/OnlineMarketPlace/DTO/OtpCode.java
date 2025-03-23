package com.example.OnlineMarketPlace.DTO;

import javax.validation.constraints.NotEmpty;

public class OtpCode {
    @NotEmpty
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

