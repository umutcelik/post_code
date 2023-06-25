package com.wcc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "post_code")
@Getter
public class PostCodeEntity {
    @Id
    private String postCode;
    private double latitude;
    private double longitude;

}
