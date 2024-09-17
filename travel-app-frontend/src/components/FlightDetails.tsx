
import { FlightDetailsDTO } from '@/interfaces/types';
import React from 'react';


interface FlightDetailsProps {
  flightDetails: FlightDetailsDTO;
}

const FlightDetails: React.FC<FlightDetailsProps> = ({ flightDetails }) => {
    console.log(flightDetails.flights[0].segments[0].travelerfareDetails)
  return (
    <div className="p-4 border-t border-black border-2 rounded-md">
    
      {flightDetails.flights.map((flight, flightIndex) => (
        <div key={flightIndex} className="mb-6">
          <h2 className="text-xl font-semibold mb-2">
            {flight.departureAirport.cityName.split("/")[0]} ({flight.departureAirport.iataCode}) → {flight.arrivalAirport.cityName.split("/")[0]} ({flight.arrivalAirport.iataCode})
          </h2>
     
          {flight.segments.map((segment, segmentIndex) => (
            <div key={segmentIndex} className="mb-4">
              <div className="flex justify-between">
                <div>
                  <div className="text-gray-800">
                    {new Date(segment.departureDate).toLocaleString()} - {new Date(segment.arrivalDate).toLocaleString()}
                  </div>
                  <div className="text-gray-600">
                    {segment.departureAirport.cityName.split('/')[0]} ({segment.departureAirport.iataCode}) → {segment.arrivalAirport.cityName.split("/")[0]} ({segment.arrivalAirport.iataCode})
                  </div>
                  <div className="text-gray-600">
                    Airline: {segment.airline.name} ({segment.airline.code})
                  </div>
                  {segment.operatingAirline && (
                    <div className="text-gray-600">
                      Operating Airline: {segment.operatingAirline.name} ({segment.operatingAirline.code})
                    </div>
                  )}
                  <div className="text-gray-600">
                    Flight Number: {segment.flightNumber}
                  </div>
                  <div className="text-gray-600">
                    Aircraft Type: {segment.aircraftType}
                  </div>
                </div>
                <div>
                
                  <div className="text-gray-800">
                    Cabin: {segment.travelerfareDetails.cabin}
                  </div>
                  <div className="text-gray-800">
                    Class: {segment.travelerfareDetails.flightClass}
                  </div>
            
                  <div className="mt-2">
                    <div className="font-semibold">Amenities:</div>
                    {segment.travelerfareDetails.amenities.map((amenity, amenityIndex) => (
                      <div key={amenityIndex} className="text-gray-600">
                        - {amenity.description} ({amenity.isChargeable ? 'Chargeable' : 'Free'})
                      </div>
                    ))}
                  </div>
                </div>
              </div>
             
             
            </div>
          ))}
        </div>
      ))}


      <div className="border-t border-gray-200 pt-4">
        <h3 className="text-lg font-semibold">Price Breakdown</h3>
        <div className="text-gray-800">
          Base Price: {flightDetails.priceBreakdown.currency} {flightDetails.priceBreakdown.base}
        </div>
        
        <div className="text-gray-800">
          Fees:
          <ul>
            {flightDetails.priceBreakdown.fees.map((fee, feeIndex) => (
              <li key={feeIndex}>
                {fee.type}: {flightDetails.priceBreakdown.currency} {fee.amount}
              </li>
            ))}
          </ul>
        </div>
        <div className="text-gray-800 font-bold">
          Total Price: {flightDetails.priceBreakdown.currency} {flightDetails.priceBreakdown.total}
        </div>
        <div className="text-gray-800">
          Price per Traveler: {`USD ${flightDetails.priceBreakdown.pricePerTraveler}` }
        </div>
        <div className="text-sm text-gray-600 mt-2">
          * All amounts in {flightDetails.priceBreakdown.currency}
        </div>
      </div>
    </div>
  );
};

export default FlightDetails;
