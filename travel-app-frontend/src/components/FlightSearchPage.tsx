'use client';

import React, { useState } from 'react';
import axios from 'axios';
import SearchFlightForm from './SearchFlightForm';
import FlightList from './FlightList';
import { FlightDetailsDTO } from '@/interfaces/types';

const FlightSearchPage: React.FC = () => {
  const [flights, setFlights] = useState<FlightDetailsDTO[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [showForm, setShowForm] = useState<boolean>(true);
  const [error, setError] = useState<string>('');

  const handleSearch = async (searchParams: any) => {
    setLoading(true);
    setShowForm(false);
    setError(''); 
    try {
      const response = await axios.get('http://localhost:8080/flights', {
        params: searchParams,
      });
      console.log(response.data);
      setFlights(response.data);
    } catch (error: any) {
      console.error('Error fetching flight data:', error);
      setError('Error fetching flight data. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleReturnToSearch = () => {
    setFlights([]);
    setShowForm(true);
  };

  return (
    <div className="flex flex-col w-full h-full justify-center p-10">
      
      {showForm && <SearchFlightForm onSearch={handleSearch} loading={loading} />}

     
      {loading && (
        <div className="flex justify-center items-center mt-4">
          <div className="loader">Loading...</div>
        </div>
      )}

      {!showForm && !loading && (
        <div>
          <button
            className="bg-gray-300 text-black p-2 rounded-md mb-4"
            onClick={handleReturnToSearch}
          >
            Return to search
          </button>

          {error && (
            <div className="text-center text-red-500">
              {error}
            </div>
          )}

          {flights.length > 0 ? (
            <FlightList flights={flights} />
          ) : (
            !error && (
              <div className="text-center">
                <p>No flights available. Please try again.</p>
              </div>
            )
          )}
        </div>
      )}
    </div>
  );
};

export default FlightSearchPage;
