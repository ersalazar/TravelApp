package com.example.TravelAppBackend.model;


import com.example.TravelAppBackend.ApiFlightResponse.Fee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceBreakdown {
    private String currency;
    private String total;
    private String base;
    private List<Fee> fees;
    private String pricePerTraveler;



}
