package com.example.TravelAppBackend.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelerFareDetails {
    private String cabin;
    private String flightClass;
    private List<Amenities> amenities;

}
