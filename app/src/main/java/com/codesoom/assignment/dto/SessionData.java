package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Accessors(fluent = true)
@FieldDefaults(makeFinal = true)
@Getter
public class SessionData {
    @NotBlank
    @JsonProperty("accessToken")
    String accessToken;

    @JsonCreator
    public SessionData(
            @JsonProperty("accessToken") String accessToken
    ) {
        this.accessToken = accessToken;
    }
}
