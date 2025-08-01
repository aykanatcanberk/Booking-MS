package com.canbe.modal;

import lombok.Data;

@Data
public class SalonReport {

    private Long salonId;
    private String salonName;
    private Double totalEarnings;
    private Integer totalBookings;
    private Integer cancelledBookings;
    private Double totalRefund;
}
