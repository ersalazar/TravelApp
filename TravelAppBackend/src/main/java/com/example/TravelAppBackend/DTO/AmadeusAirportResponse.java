package com.example.TravelAppBackend.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmadeusAirportResponse {

    private Meta meta;

    @JsonProperty("data")
    private List<Location> locations;

    @Getter
    @Setter
    public static class Meta {
        private int count;
        private Links links;

        @Getter
        @Setter
        public static class Links {
            private String self;
            private String next;
            private String last;

        }
    }

    @Getter
    @Setter
    public static class Location {
        private String type;
        private String subType;
        private String name;
        private String detailedName;
        private String id;
        private Self self;
        private String iataCode;
        private Address address;

        @Getter
        @Setter
        public static class Self {
            private String href;
            private List<String> methods;

        }

        @Getter
        @Setter
        public static class Address {
            private String cityName;
            private String countryName;

        }
    }
}
