'use client'
import { useState, useEffect } from 'react';
import axios from 'axios';

import { Airport, FlightDetailsDTO } from '@/interfaces/types';
import FlightSearchPage from '@/components/FlightSearchPage';

export default function FlightSearchAndOffersPage() {
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(true);
  const [flights, setFlights] = useState<FlightDetailsDTO[]>([]);
  const [airports, setAirports] = useState<{ airportCode: string; airportCity: string }[]>([]);

  useEffect(() => {
    const fetchAirports = async () => {
      try {
        const response = await axios.get('http://localhost:8080/airports');
        setAirports(response.data);
      } catch (error) {
        console.error('Error fetching airports', error);
      }
    };

    fetchAirports();
  }, []);


  const handleReturnToSearch = () => {
    setShowForm(true);
    setFlights([]);
  };




  return (
    <div className="w-full h-full p-5">
      {showForm && (
        <FlightSearchPage />

      )}

      {loading && (
        <div className="flex items-center justify-center h-full">
          <div className="loader border-t-transparent border-solid rounded-full border-blue-400 border-8 w-16 h-16 animate-spin"></div>
        </div>
      )}

      {!showForm && !loading && flights.length === 0 && (
        <div className="text-center">
          <p>No flights available. Please try again.</p>
          <button
            className="bg-gray-300 text-black p-2 rounded-md mt-4"
            onClick={handleReturnToSearch}
          >
            Return to search
          </button>
        </div>
      )}
    </div>
  );
}
