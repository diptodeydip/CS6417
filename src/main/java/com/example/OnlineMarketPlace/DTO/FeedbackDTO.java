package com.example.OnlineMarketPlace.DTO;

import javax.validation.constraints.NotEmpty;

public class FeedbackDTO {

    @NotEmpty
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
