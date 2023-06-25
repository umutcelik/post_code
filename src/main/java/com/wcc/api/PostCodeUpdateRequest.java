package com.wcc.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class PostCodeUpdateRequest {

    @Size(min = 7, max = 7)
    private String postCode;

    @Min(-90)
    @Max(90)
    @NotNull
    private Double latitude;

    @Min(-180)
    @Max(180)
    @NotNull
    private Double longitude;
}
