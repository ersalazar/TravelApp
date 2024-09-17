import React, { useState } from 'react';
import { FlightDetailsDTO } from '@/interfaces/types';
import FlightCard from './FlightCard';

interface FlightListProps {
  flights: FlightDetailsDTO[];
}

const FlightList: React.FC<FlightListProps> = ({ flights }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const flightsPerPage = 5;

  const [sortCriteria, setSortCriteria] = useState<'duration' | 'price'>('duration');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');

  const parseDuration = (duration: string): number => {
    const regex = /P(?:T(?:(\d+)H)?(?:(\d+)M)?)?/;
    const matches = duration.match(regex);
    if (!matches) return 0;

    const hours = matches[1] ? parseInt(matches[1]) : 0;
    const minutes = matches[2] ? parseInt(matches[2]) : 0;

    return hours * 60 + minutes;
  };

  const sortedFlights = [...flights].sort((a, b) => {
    let aValue: number;
    let bValue: number;

    if (sortCriteria === 'duration') {
      const aDuration = parseDuration(a.flights[0].totalFlightTime);
      const bDuration = parseDuration(b.flights[0].totalFlightTime);
      aValue = aDuration;
      bValue = bDuration;
    } else {
      const aPrice = parseFloat(a.priceBreakdown.total);
      const bPrice = parseFloat(b.priceBreakdown.total);
      aValue = aPrice;
      bValue = bPrice;
    }

    if (sortOrder === 'asc') {
      return aValue - bValue;
    } else {
      return bValue - aValue;
    }
  });

  const totalPages = Math.ceil(sortedFlights.length / flightsPerPage);

  const validCurrentPage = Math.min(Math.max(currentPage, 1), totalPages);

  const startIndex = (validCurrentPage - 1) * flightsPerPage;
  const endIndex = startIndex + flightsPerPage;
  const currentFlights = sortedFlights.slice(startIndex, endIndex);

  const handlePageChange = (pageNumber: number) => {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      setCurrentPage(pageNumber);
    }
  };

  const pageNumbers = [];
  for (let i = 1; i <= totalPages; i++) {
    pageNumbers.push(i);
  }

  return (
    <div className="flex flex-col items-center space-y-4">
      <div className="flex space-x-4 mb-4">
        <div>
          <label htmlFor="sortCriteria" className="mr-2">Sort by:</label>
          <select
            id="sortCriteria"
            value={sortCriteria}
            onChange={(e) => {
              setSortCriteria(e.target.value as 'duration' | 'price');
              setCurrentPage(1); 
            }}
            className="border border-gray-300 rounded px-2 py-1"
          >
            <option value="duration">Duration</option>
            <option value="price">Price</option>
          </select>
        </div>

        <div>
          <label htmlFor="sortOrder" className="mr-2">Order:</label>
          <select
            id="sortOrder"
            value={sortOrder}
            onChange={(e) => {
              setSortOrder(e.target.value as 'asc' | 'desc');
              setCurrentPage(1); 
            }}
            className="border border-gray-300 rounded px-2 py-1"
          >
            <option value="asc">Ascending</option>
            <option value="desc">Descending</option>
          </select>
        </div>
      </div>

      {currentFlights.map((flightDetails, index) => (
        <FlightCard key={index + startIndex} flightDetails={flightDetails} />
      ))}

      {totalPages > 1 && (
        <div className="flex space-x-2 mt-4">
          <button
            onClick={() => handlePageChange(validCurrentPage - 1)}
            disabled={validCurrentPage === 1}
            className={`px-3 py-1 rounded ${
              validCurrentPage === 1 ? 'bg-gray-300 cursor-not-allowed' : 'bg-blue-500 text-white'
            }`}
          >
            Previous
          </button>

          {pageNumbers.map((number) => (
            <button
              key={number}
              onClick={() => handlePageChange(number)}
              className={`px-3 py-1 rounded  ${
                validCurrentPage === number ? 'bg-blue-700 text-white' : 'bg-blue-500 text-white'
              }`}
            >
              {number}
            </button>
          ))}

          <button
            onClick={() => handlePageChange(validCurrentPage + 1)}
            disabled={validCurrentPage === totalPages}
            className={`px-3 py-1 rounded ${
              validCurrentPage === totalPages
                ? 'bg-gray-300 cursor-not-allowed'
                : 'bg-blue-500 text-white'
            }`}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default FlightList;
