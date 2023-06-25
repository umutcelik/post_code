package com.wcc.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCodeResponse {
    private String postCode;
    private double latitude;
    private double longitude;
}
