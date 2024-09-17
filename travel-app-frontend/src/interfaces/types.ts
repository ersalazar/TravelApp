
export interface FlightDetailsDTO {
    flights: FlightDTO[];
    priceBreakdown: PriceBreakdown;
  }
  
  export interface FlightDTO {
    departureDateTime: string;
    arrivalDateTime: string;
    departureAirport: Airport;
    arrivalAirport: Airport;
    carrier: Airline;
    totalFlightTime: string;
    segments: FlightSegment[];
  }
  
  export interface Airport {
    cityName: string;
    iataCode: string;
  }
  
  export interface Airline {
    name: string;
    code: string;
  }
  
  export interface FlightSegment {
    departureDate: string;
    arrivalDate: string;
    departureAirport: Airport;
    arrivalAirport: Airport;
    airline: Airline;
    operatingAirline?: Airline;
    flightNumber: string;
    aircraftType: string;
    travelerfareDetails: TravelerFareDetails;
  }
  
  export interface TravelerFareDetails {
    cabin: string;
    flightClass: string;
    amenities: Amenities[];
  }
  
  export interface Amenities {
    description: string;
    isChargeable: boolean;
    amenityType: string;
  }
  
  export interface PriceBreakdown {
    currency: string;
    total: string;
    base: string;
    grandTotal: string;
    fees: Fee[];
    pricePerTraveler: String;
  }
  
  export interface Fee {
    amount: string;
    type: string;
  }
  
  