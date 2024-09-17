//package com.example.TravelAppBackend.utils;
//
//import com.example.TravelAppBackend.DTO.*;
//import com.example.TravelAppBackend.model.*;
//import com.example.TravelAppBackend.service.AmadeusService;
//import com.example.TravelAppBackend.service.CityService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//@Component
//public class AmadeusFlightMapper {
//
//    public  HashMap<String, Airport> airportCodeCityMap = new HashMap<>();
//
//
//    public static List<OneWayFlightDTO> mapToOneWayFlightDTOs(AmadeusFlightResponse amadeusResponse) {
//        List<OneWayFlightDTO> oneWayFlights = new ArrayList<>();
//
//        for (AmadeusFlightResponse.FlightOffer offer : amadeusResponse.getData()) {
//            FlightDTO departureFlight = mapToFlightDTO(offer.getItineraries().get(0));
//
//            double totalPrice = Double.parseDouble(offer.getPrice().getGrandTotal());
//            double pricePerPerson = offer.getTravelerPricings().isEmpty() ? totalPrice :
//                    Double.parseDouble(offer.getTravelerPricings().get(0).getPrice().getTotal());
//            String currency = offer.getPrice().getCurrency();
//
//            oneWayFlights.add(new OneWayFlightDTO(departureFlight, totalPrice, pricePerPerson, currency));
//        }
//
//        return oneWayFlights;
//    }
//
//    public static List<TwoWayFlightDTO> mapToTwoWayFlightDTOs(AmadeusFlightResponse amadeusResponse) {
//        List<TwoWayFlightDTO> twoWayFlights = new ArrayList<>();
//
//        for (AmadeusFlightResponse.FlightOffer offer : amadeusResponse.getData()) {
//            List<AmadeusFlightResponse.Itinerary> itineraries = offer.getItineraries();
//            FlightDTO departureFlight = mapToFlightDTO(itineraries.get(0));
//            FlightDTO returnFlight = itineraries.size() > 1 ? mapToFlightDTO(itineraries.get(1)) : null;
//
//            double totalPrice = Double.parseDouble(offer.getPrice().getGrandTotal());
//            double pricePerPerson = offer.getTravelerPricings().isEmpty() ? totalPrice :
//                    Double.parseDouble(offer.getTravelerPricings().get(0).getPrice().getTotal());
//            String currency = offer.getPrice().getCurrency();
//
//            twoWayFlights.add(new TwoWayFlightDTO(departureFlight, returnFlight, totalPrice, pricePerPerson, currency));
//        }
//
//        return twoWayFlights;
//    }
//
//    public List<FlightDetailsDTO> mapToFlightDetailsDTO(AmadeusFlightResponse amadeusResponse) {
//        CityService cityService = new CityService();
//        ArrayList<FlightDetailsDTO> flightDetailsDTOS = new ArrayList<>();
//        for (AmadeusFlightResponse.FlightOffer offer : amadeusResponse.getData()) {
//
//            FlightDetailsDTO flightDetailsDTO = new FlightDetailsDTO();
//
//            String currency =  offer.getPrice().getCurrency();
//            String total = offer.getPrice().getGrandTotal();
//            String base = offer.getPrice().getBase();
//            List<AmadeusFlightResponse.Price.Fee> fees = offer.getPrice().getFees();
//
//            PriceBreakdown priceBreakdown = new PriceBreakdown(currency, total, base, fees);
//
//            flightDetailsDTO.setPriceBreakdown(priceBreakdown);
//
//            for (AmadeusFlightResponse.Itinerary intinerary : offer.getItineraries()) {
//                String departureDateTime = intinerary.getSegments().getFirst().getDeparture().getAt();
//                String arrivalDateTime = intinerary.getSegments().getLast().getArrival().getAt();
//
//                String departureAirportCode = intinerary.getSegments().getFirst().getDeparture().getIataCode();
//
//                Airport departureAirport;
//
//                if (this.airportCodeCityMap.containsKey(departureAirportCode)) {
//                    departureAirport = airportCodeCityMap.get(departureAirportCode);
//                } else {
//
//                    String cityCode = amadeusResponse.getDictionaries().getLocations().get(departureAirportCode).getCityCode();
//                    String countryCode = amadeusResponse.getDictionaries().getLocations().get(departureAirportCode).getCountryCode();
//                    String departureAirportCityName =  cityService.searchCity(cityCode, countryCode);
//                    departureAirport = new Airport(departureAirportCode, departureAirportCityName);
//                    airportCodeCityMap.put(departureAirportCode, departureAirport);
//
//                }
//
//                String arrivalAirportCode = intinerary.getSegments().getLast().getArrival().getIataCode();
//
//                Airport arrivalAirport;
//
//                if (this.airportCodeCityMap.containsKey(arrivalAirportCode)) {
//                    arrivalAirport = airportCodeCityMap.get(arrivalAirportCode);
//                } else {
//
//                    String cityCode = amadeusResponse.getDictionaries().getLocations().get(arrivalAirportCode).getCityCode();
//                    String countryCode = amadeusResponse.getDictionaries().getLocations().get(arrivalAirportCode).getCountryCode();
//
//                    String arrivalAirportCityName = cityService.searchCity(cityCode, countryCode);
//
//                    arrivalAirport = new Airport(arrivalAirportCode, arrivalAirportCityName);
//                    airportCodeCityMap.put(arrivalAirportCode, arrivalAirport);
//                }
//
//
//                String carrierCode = intinerary.getSegments().getFirst().getCarrierCode();
//                String carrierName = amadeusResponse.getDictionaries().getCarriers().get(carrierCode);
//
//                Airline carrier = new Airline(carrierName, carrierCode);
//
//                String totalFlightTime = intinerary.getDuration();
//
//                FlightDTO flightDTO = new FlightDTO();
//                flightDTO.setDepartureDateTime(departureDateTime);
//                flightDTO.setArrivalDateTime(arrivalDateTime);
//                flightDTO.setDepartureAirport(departureAirport);
//                flightDTO.setArrivalAirport(arrivalAirport);
//                flightDTO.setCarrier(carrier);
//                flightDTO.setTotalFlightTime(totalFlightTime);
//
//                List<FlightSegment> flightSegments = new ArrayList<>();
//                for (AmadeusFlightResponse.Segment segment : intinerary.getSegments()) {
//                    FlightSegment flightSegment = new FlightSegment();
//                    flightSegment.setDepartureDate(segment.getDeparture().getAt());
//                    flightSegment.setArrivalDate(segment.getArrival().getAt());
//
//                    Airline airline = new Airline();
//                    airline.setCode(segment.getCarrierCode());
//                    airline.setName(amadeusResponse.getDictionaries().getCarriers().get(segment.getCarrierCode()));
//                    flightSegment.setAirline(airline);
//                    flightSegment.setFlightNumber(segment.getNumber());
//                    String aircraftCode = segment.getAircraft().getCode();
//
//                    flightSegment.setAircraftType(amadeusResponse.getDictionaries().getAircraft().get(aircraftCode));
//
//                    TravelerFareDetails travelerFareDetails = new TravelerFareDetails();
//
//                    travelerFareDetails.setFlightClass();
//
//
//                }
//
//
//
//
//            }
//
//
//
//
//
//
//        }
//
//    }
//
//
//
//    private static FlightDTO mapToFlightDTO(AmadeusFlightResponse.Itinerary itinerary) {
//        List<FlightDTO.StopDTO> stops = new ArrayList<>();
//        for (AmadeusFlightResponse.Segment segment : itinerary.getSegments()) {
//            FlightDTO.StopDTO stop = new FlightDTO.StopDTO(
//                    segment.getDuration(),
//                    segment.getDeparture().getIataCode(),
//                    segment.getDeparture().getIataCode()
//            );
//            stops.add(stop);
//        }
//
//        AmadeusFlightResponse.Segment firstSegment = itinerary.getSegments().get(0);
//        AmadeusFlightResponse.Segment lastSegment = itinerary.getSegments().get(itinerary.getSegments().size() - 1);
//
//        return new FlightDTO(
//                firstSegment.getDeparture().getAt(),
//                lastSegment.getArrival().getAt(),
//                firstSegment.getDeparture().getIataCode(),
//                lastSegment.getArrival().getIataCode(),
//                firstSegment.getDeparture().getIataCode(),
//                lastSegment.getArrival().getIataCode(),
//                firstSegment.getCarrierCode(),
//                firstSegment.getCarrierCode(),
//                itinerary.getDuration(),
//                stops
//        );
//    }
//
//
//
//
//}
