package com.wcc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCodeDistance {
    private String postCode1;
    private double latitude1;
    private double longitude1;
    private String postCode2;
    private double latitude2;
    private double longitude2;
    private double distance;
}
