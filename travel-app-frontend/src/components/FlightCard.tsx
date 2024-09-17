
import { FlightDetailsDTO } from '@/interfaces/types';
import React, { useState } from 'react';
import FlightDetails from './FlightDetails';


interface FlightCardProps {
  flightDetails: FlightDetailsDTO;
}

const FlightCard: React.FC<FlightCardProps> = ({ flightDetails }) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const flight = flightDetails.flights[0];

  function formatFlightTime(time: String) {
    const newTimes = time.split('H')
    const hours = newTimes[0].slice(2)
    const min = newTimes[1].split('M')[0]
    
    return `${hours} hr ${min} min`
  }

  const toggleExpanded = () => {
    setIsExpanded(!isExpanded);
  };

  return (
    <div
      className="w-4/5 bg-white shadow-md rounded-lg cursor-pointer border-2 border-black "
      onClick={toggleExpanded}
    >
      <div className="p-4 flex justify-between items-center">
        <div>
          <div className="text-lg font-semibold">
            {flight.departureAirport.cityName.split("/")[0]} ({flight.departureAirport.iataCode}) → {flight.arrivalAirport.cityName.split("/")[0]} ({flight.arrivalAirport.iataCode})
          </div>
          <div className="text-gray-600">
            {new Date(flight.departureDateTime).toLocaleString()} - {new Date(flight.arrivalDateTime).toLocaleString()}
          </div>
          <div className="text-gray-600">
            {flight.carrier.name} ({flight.carrier.code})
          </div>
          <div className="text-gray-600">
            Total Flight Time: {formatFlightTime(flight.totalFlightTime)}
          </div>
        </div>
        <div className='flex flex-col'>
            
            <div className="text-xl font-bold">
            {flightDetails.priceBreakdown.currency} {flightDetails.priceBreakdown.total}
            </div>
            <div className='text-sm'>
                Price per traveler: {`$${flightDetails.priceBreakdown.pricePerTraveler}`}
            </div>
        </div>
      </div>

      {isExpanded && (
        <FlightDetails flightDetails={flightDetails} />
      )}
    </div>
  );
};

export default FlightCard;
