package com.example.TravelAppBackend.utils;

// FlightDetailsMapper.java
import com.example.TravelAppBackend.ApiFlightResponse.*;
import com.example.TravelAppBackend.ApiFlightResponse.PriceBreakdown;
import com.example.TravelAppBackend.DTO.FlightDTO;
import com.example.TravelAppBackend.DTO.FlightDetailsDTO;
import com.example.TravelAppBackend.model.*;
import com.example.TravelAppBackend.service.AmadeusService;
import com.example.TravelAppBackend.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class FlightDetailsMapper {

    private static ConcurrentMap<String, String> cityNameCache = new ConcurrentHashMap<>();

    private final CityService cityService;

    @Autowired
    public FlightDetailsMapper(CityService cityService) {
        this.cityService = cityService;
    }

    private String getCityName(String cityCode)  {
        return cityService.getCityNameByCode(cityCode);
    }



    public  List<FlightDetailsDTO> mapToFlightDetailsDTOList(ApiResponse apiResponse) {
        List<FlightDetailsDTO> flightDetailsDTOList = new ArrayList<>();
        Map<String, LocationDictionary> locationDicts = apiResponse.getDictionaries().getLocations();
        Map<String, String> carriersDict = apiResponse.getDictionaries().getCarriers();
        Map<String, String> aircraftDict = apiResponse.getDictionaries().getAircraft();

        for (FlightOffer flightOffer : apiResponse.getData()) {
            FlightDetailsDTO flightDetailsDTO = new FlightDetailsDTO();
            List<FlightDTO> flightDTOList = new ArrayList<>();

            for (Itinerary itinerary : flightOffer.getItineraries()) {
                FlightDTO flightDTO = new FlightDTO();
                List<Segment> segments = itinerary.getSegments();

                if (segments != null && !segments.isEmpty()) {
                    Segment firstSegment = segments.get(0);
                    Segment lastSegment = segments.get(segments.size() - 1);

                    flightDTO.setDepartureDateTime(firstSegment.getDeparture().getAt());
                    flightDTO.setArrivalDateTime(lastSegment.getArrival().getAt());

                    // Departure Airport for FlightDTO
                    Airport departureAirportDTO = new Airport();
                    departureAirportDTO.setIataCode(firstSegment.getDeparture().getIataCode());
                    LocationDictionary departureLocationDTO = locationDicts.get(firstSegment.getDeparture().getIataCode());
                    if (departureLocationDTO != null) {
                        String departureCityCode = departureLocationDTO.getCityCode();
                        String departureCityName = cityNameCache.computeIfAbsent(departureCityCode, code -> getCityName(code));
                        departureAirportDTO.setCityName(departureCityName);
                    }
                    flightDTO.setDepartureAirport(departureAirportDTO);

                    // Arrival Airport for FlightDTO
                    Airport arrivalAirportDTO = new Airport();
                    arrivalAirportDTO.setIataCode(lastSegment.getArrival().getIataCode());
                    LocationDictionary arrivalLocationDTO = locationDicts.get(lastSegment.getArrival().getIataCode());
                    if (arrivalLocationDTO != null) {
                        String arrivalCityCode = arrivalLocationDTO.getCityCode();
                        String arrivalCityName = cityNameCache.computeIfAbsent(arrivalCityCode, code -> getCityName(code));
                        arrivalAirportDTO.setCityName(arrivalCityName);
                    }
                    flightDTO.setArrivalAirport(arrivalAirportDTO);

                    // Carrier
                    Airline carrier = new Airline();
                    carrier.setCode(firstSegment.getCarrierCode());
                    carrier.setName(carriersDict.get(firstSegment.getCarrierCode()));
                    flightDTO.setCarrier(carrier);

                    // Total Flight Time
                    flightDTO.setTotalFlightTime(itinerary.getDuration());

                    // Segments
                    List<FlightSegment> flightSegments = new ArrayList<>();
                    for (Segment segment : segments) {
                        FlightSegment flightSegment = new FlightSegment();
                        flightSegment.setDepartureDate(segment.getDeparture().getAt());
                        flightSegment.setArrivalDate(segment.getArrival().getAt());

                        // Departure Airport for FlightSegment
                        Airport segmentDepartureAirport = new Airport();
                        segmentDepartureAirport.setIataCode(segment.getDeparture().getIataCode());
                        LocationDictionary segmentDepLocation = locationDicts.get(segment.getDeparture().getIataCode());
                        if (segmentDepLocation != null) {
                            String departureCityCode = segmentDepLocation.getCityCode();
                            String departureCityName = cityNameCache.computeIfAbsent(departureCityCode, code -> getCityName(code));
                            segmentDepartureAirport.setCityName(departureCityName);
                        }
                        flightSegment.setDepartureAirport(segmentDepartureAirport);

                        // Arrival Airport for FlightSegment
                        Airport segmentArrivalAirport = new Airport();
                        segmentArrivalAirport.setIataCode(segment.getArrival().getIataCode());
                        LocationDictionary segmentArrLocation = locationDicts.get(segment.getArrival().getIataCode());
                        if (segmentArrLocation != null) {
                            String arrivalCityCode = segmentArrLocation.getCityCode();
                            String arrivalCityName = cityNameCache.computeIfAbsent(arrivalCityCode, code -> getCityName(code));
                            segmentArrivalAirport.setCityName(arrivalCityName);
                        }
                        flightSegment.setArrivalAirport(segmentArrivalAirport);

                        // Airline
                        Airline airline = new Airline();
                        airline.setCode(segment.getCarrierCode());
                        airline.setName(carriersDict.get(segment.getCarrierCode()));
                        flightSegment.setAirline(airline);

                        // Operating Airline
                        Airline operatingAirline = null;
                        if (segment.getOperating() != null) {
                            operatingAirline = new Airline();
                            operatingAirline.setCode(segment.getOperating().getCarrierCode());
                            operatingAirline.setName(carriersDict.get(segment.getOperating().getCarrierCode()));
                        }
                        flightSegment.setOperatingAirline(operatingAirline);

                        flightSegment.setFlightNumber(segment.getNumber());
                        flightSegment.setAircraftType(aircraftDict.get(segment.getAircraft().getCode()));

                        // Traveler Fare Details
                        TravelerFareDetails travelerFareDetails = new TravelerFareDetails();

                        // Assuming first travelerPricing for fare details
                        TravelerPricing travelerPricing = flightOffer.getTravelerPricings().get(0);
                        for (FareDetailsBySegment fareDetails : travelerPricing.getFareDetailsBySegment()) {
                            if (fareDetails.getSegmentId().equals(segment.getId())) {
                                travelerFareDetails.setCabin(fareDetails.getCabin());
                                travelerFareDetails.setFlightClass(fareDetails.getFlightClass());

                                // Amenities
                                List<Amenities> amenitiesList = new ArrayList<>();
                                if (fareDetails.getAmenities() != null) {
                                    for (Amenity amenity : fareDetails.getAmenities()) {
                                        Amenities amenities = new Amenities();
                                        amenities.setDescription(amenity.getDescription());
                                        amenities.setIsChargeable(amenity.isChargeable());
                                        amenities.setAmenityType(amenity.getAmenityType());
                                        amenitiesList.add(amenities);
                                    }
                                }
                                travelerFareDetails.setAmenities(amenitiesList);
                                break;
                            }
                        }
                        flightSegment.setTravelerfareDetails(travelerFareDetails);

                        flightSegments.add(flightSegment);

                    }

                    flightDTO.setSegments(flightSegments);
                }
                flightDTOList.add(flightDTO);
            }
            flightDetailsDTO.setFlights(flightDTOList);

            // Price Breakdown
            PriceBreakdown priceBreakdown = new PriceBreakdown();
            Price price = flightOffer.getPrice();
            if (price != null) {
                priceBreakdown.setCurrency(price.getCurrency());
                priceBreakdown.setTotal(price.getTotal());
                priceBreakdown.setBase(price.getBase());
                priceBreakdown.setGrandTotal(price.getGrandTotal());
                priceBreakdown.setFees(price.getFees());
                priceBreakdown.setPricePerTraveler(flightOffer.getTravelerPricings().get(0).getPrice().getTotal());

            }
            flightDetailsDTO.setPriceBreakdown(priceBreakdown);

            flightDetailsDTOList.add(flightDetailsDTO);
        }
        return flightDetailsDTOList;
    }
}
