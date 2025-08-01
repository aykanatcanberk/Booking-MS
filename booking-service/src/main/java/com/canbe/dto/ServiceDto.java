package com.canbe.dto;

import lombok.Data;

@Data
public class ServiceDto {

    private Long  id;
    private String  name;
    private String  description;
    private Integer  price;
    private int duration;
    private Long salonId;
    private Long categoryId;
    private String image;
}
