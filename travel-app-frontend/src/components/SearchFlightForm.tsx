'use client'
// components/SearchFlightForm.tsx

import React, { useState } from 'react';
import axios from 'axios';

interface Airport {
  airportCity: string;
  airportName: string;
  airportCode: string;
}

interface SearchFlightFormProps {
  onSearch: (searchParams: any) => void;
  loading: boolean;
}

const SearchFlightForm: React.FC<SearchFlightFormProps> = ({ onSearch, loading }) => {
  const [departureOptions, setDepartureOptions] = useState<Airport[]>([]);
  const [arrivalOptions, setArrivalOptions] = useState<Airport[]>([]);
  const [departureCode, setDepartureCode] = useState<string>('');
  const [arrivalCode, setArrivalCode] = useState<string>('');
  const [departureDate, setDepartureDate] = useState<string>('');
  const [returnDate, setReturnDate] = useState<string>('');
  const [numberOfPassengers, setNumberOfPassengers] = useState<number>(1);
  const [currency, setCurrency] = useState<string>('USD');
  const [directFlightOnly, setDirectFlightOnly] = useState<boolean>(false);

  const fetchAirports = async (query: string, setOptions: React.Dispatch<React.SetStateAction<Airport[]>>) => {
    try {
      const response = await axios.get<Airport[]>(`http://localhost:8080/airports`, { params: { query } });
      setOptions(response.data);
    } catch (error) {
      console.error('Error fetching airport data:', error);
    }
  };

  const handleDepartureInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const query = e.target.value;
    setDepartureCode(query);
    if (query.length > 2) {
      fetchAirports(query, setDepartureOptions);
    } else {
      setDepartureOptions([]);
    }
  };

  const handleArrivalInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const query = e.target.value;
    setArrivalCode(query);
    if (query.length > 2) {
      fetchAirports(query, setArrivalOptions);
    } else {
      setArrivalOptions([]);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const params = {
      departureAirport: departureCode,
      arrivalAirport: arrivalCode,
      departureDate,
      returnDate: returnDate || null,
      adults: numberOfPassengers,
      currency,
      nonStopOnly: directFlightOnly,
    };

    onSearch(params);
  };

  const handleSelectDeparture = (code: string) => {
    setDepartureCode(code);
    setDepartureOptions([]);
  };

  const handleSelectArrival = (code: string) => {
    setArrivalCode(code);
    setArrivalOptions([]);
  };

  return (
    <div className="flex flex-col w-full h-full justify-center">
      <div className="font-bold w-1/2 text-center mb-8 min-h-full bg-slate-400 flex justify-center m-auto">
        <h1>Travel App</h1>
      </div>

      <div className="w-1/2 self-center justify-center flex flex-col space-y-2 m-auto">
        <form onSubmit={handleSubmit}>
          <div className="flex flex-row items-center justify-between">
            <label htmlFor="DepartureCode" className="w-1/4 text-left h-full">
              Departure Airport:
            </label>
            <div className="w-3/4 relative">
              <input
                type="text"
                id="DepartureCode"
                className="border border-black rounded-md w-full text-center"
                value={departureCode}
                onChange={handleDepartureInput}
                autoComplete="off"
              />
              {departureOptions.length > 0 && (
                <ul className="absolute top-full left-0 right-0 border border-black rounded-md bg-white max-h-40 overflow-y-auto z-10">
                  {departureOptions.map((option) => (
                    <li
                      key={option.airportCode}
                      className="cursor-pointer p-2 hover:bg-gray-200"
                      onClick={() => handleSelectDeparture(option.airportCode)}
                    >
                      {option.airportCity} ({option.airportCode})
                    </li>
                  ))}
                </ul>
              )}
            </div>
          </div>

          <div className="flex flex-row items-center justify-between mt-2">
            <label htmlFor="ArrivalCode" className="w-1/4 text-left h-full">
              Arrival Airport:
            </label>
            <div className="w-3/4 relative">
              <input
                type="text"
                id="ArrivalCode"
                className="border border-black rounded-md w-full text-center"
                value={arrivalCode}
                onChange={handleArrivalInput}
                autoComplete="off"
              />
              {arrivalOptions.length > 0 && (
                <ul className="absolute top-full left-0 right-0 border border-black rounded-md bg-white max-h-40 overflow-y-auto z-10">
                  {arrivalOptions.map((option) => (
                    <li
                      key={option.airportCode}
                      className="cursor-pointer p-2 hover:bg-gray-200"
                      onClick={() => handleSelectArrival(option.airportCode)}
                    >
                      {option.airportCity} ({option.airportCode})
                    </li>
                  ))}
                </ul>
              )}
            </div>
          </div>

          <div className="flex flex-row items-center mt-2">
            <label htmlFor="DepartureDate" className="w-1/4 text-left">
              Departure Date:
            </label>
            <input
              type="date"
              id="DepartureDate"
              className="border border-black rounded-md text-left w-1/4"
              value={departureDate}
              onChange={(e) => setDepartureDate(e.target.value)}
            />
          </div>

          <div className="flex flex-row items-center mt-2">
            <label htmlFor="ReturnDate" className="w-1/4 text-left">
              Return Date:
            </label>
            <input
              type="date"
              id="ReturnDate"
              className="border border-black rounded-md text-left w-1/4"
              value={returnDate}
              onChange={(e) => setReturnDate(e.target.value)}
            />
          </div>

          <div className="flex flex-row items-center mt-2">
            <label htmlFor="NumberOfPassengers" className="w-1/4 text-left">
              Number of passengers:
            </label>
            <input
              type="number"
              id="NumberOfPassengers"
              className="border border-black rounded-md text-left w-1/4"
              value={numberOfPassengers}
              min={1}
              onChange={(e) => setNumberOfPassengers(Number(e.target.value))}
            />
          </div>

          <div className="flex flex-row items-center mt-2">
            <label htmlFor="Currency" className="w-1/4 text-left">
              Currency:
            </label>
            <select
              id="Currency"
              className="border border-black rounded-md w-1/4"
              value={currency}
              onChange={(e) => setCurrency(e.target.value)}
            >
              <option value="USD">American Dollars</option>
              <option value="EUR">Euros</option>
              <option value="MXN">Mexican Pesos</option>
            </select>
          </div>

          <div className="flex flex-row items-center mt-2">
            <div className="w-1/4"></div>
            <div className="w-3/4 flex flex-row text-left justify-left items-center">
              <input
                type="checkbox"
                id="DirectFlightOnly"
                className="border border-black rounded-xl text-black size-6 mr-2"
                checked={directFlightOnly}
                onChange={(e) => setDirectFlightOnly(e.target.checked)}
              />
              <label htmlFor="DirectFlightOnly">Non-stop only</label>
            </div>
          </div>

          <div className="relative">
            <button
              type="submit"
              id="SearchBtn"
              className="mt-5 bg-slate-500 rounded-lg p-5 font-medium text-slate-100"
              disabled={loading}
            >
              {loading ? 'Searching...' : 'Search'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SearchFlightForm;
