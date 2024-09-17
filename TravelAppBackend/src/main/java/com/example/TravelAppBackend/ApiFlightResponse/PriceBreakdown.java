package com.example.TravelAppBackend.ApiFlightResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class PriceBreakdown {
    private String currency;
    private String total;
    private String base;
    private String grandTotal;
    private List<Fee> fees;
    private String pricePerTraveler;
}
