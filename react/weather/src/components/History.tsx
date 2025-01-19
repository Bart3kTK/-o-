import React, { useState, useEffect } from 'react';
import styled from 'styled-components';

interface WeatherDTO {
  pressure: number;
  temperature1: number;
  temperature2: number;
  rainDetected: number;
  humidity: number;
  lightIntensity: number;
  gasConcentration: number;
}

const HistoryContainer = styled.div`
  background: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  max-width: 1000px;
  width: 100%;
  text-align: center;
  background: #f5f5f5;
`;

const DatePickerWrapper = styled.div`
  margin: 20px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const DatePickerInput = styled.input`
  padding: 10px;
  font-size: 16px;
  border-radius: 8px;
  border: 1px solid #ccc;
  margin-bottom: 10px;
`;

const ShowDateButton = styled.button`
  padding: 10px 20px;
  font-size: 16px;
  color: #ffffff;
  background-color: #274ac7; /* matching NavBar color */
  border: none;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  transition: background 0.3s ease;

  &:hover {
    background: rgba(255, 255, 255, 0.2);
  }
`;

const Table = styled.table`
  width: 100%;
  margin-top: 30px;
  border-collapse: collapse;
  border: 1px solid #ddd;
`;

const TableHeader = styled.th`
  padding: 10px;
  background-color: #274ac7; /* matching NavBar color */
  color: #ffffff;
  text-align: left;
  font-weight: 600;
`;

const TableRow = styled.tr`
  border-bottom: 1px solid #ddd;
`;

const TableData = styled.td`
  padding: 10px;
  text-align: left;
  font-size: 16px;
`;

const NoDataMessage = styled.p`
  font-size: 18px;
  color: red;
  text-align: center;
  margin-top: 20px;
`;

const WeatherSection = styled.div`
  margin-bottom: 50px; /* Add extra spacing below the specific date data */
`;

const History: React.FC = () => {
  const [selectedDate, setSelectedDate] = useState<string>('');
  const [weatherData, setWeatherData] = useState<WeatherDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [noData, setNoData] = useState<boolean>(false);
  const [historyData, setHistoryData] = useState<WeatherDTO[]>([]);
  const [showHistory, setShowHistory] = useState<boolean>(false);
  const [specificDate, setSpecificDate] = useState<boolean>(false);

  const handleDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSelectedDate(event.target.value);
    setSpecificDate(false);
  };

  const handleButtonClick = async () => {
    try {
      if (!selectedDate) {
        throw new Error('Please select a date.');
      }
    } catch (err) {
      setNoData(true);
      setWeatherData(null);
      setSpecificDate(false);
      return;
    }

    const [year, month, day] = selectedDate.split('-');
    setLoading(true);
    setNoData(false);

    try {
      const response = await fetch(
        `http://localhost:8080/weather/data/average/${year}/${month}/${day}`
      );

      if (!response.ok) {
        throw new Error('No data found for the selected date.');
      }

      const data = await response.json();

      if (!data || Object.keys(data).length === 0) {
        setNoData(true);
        setWeatherData(null);
        setSpecificDate(false);
      } else {
        setWeatherData(data);
        setSpecificDate(true);
        setNoData(false);
      }
    } catch (err) {
      setNoData(true);
      setWeatherData(null);
      setSpecificDate(false);
    } finally {
      setLoading(false);
    }
  };

  const fetchWeatherHistory = async () => {
    setLoading(true);
    try {
      const response = await fetch(
        'http://localhost:8080/weather/data/history'
      );
      if (!response.ok) {
        throw new Error('Unable to fetch weather history.');
      }
      const data = await response.json();
      setHistoryData(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const toggleHistoryView = () => {
    setShowHistory(!showHistory);
    if (!showHistory) {
      fetchWeatherHistory();
    }
  };

  const toggleSpecificDateView = () => {
    setSpecificDate(!specificDate);
    if (!specificDate) {
      handleButtonClick();
    }
  };

  return (
    <HistoryContainer>
      <h1 style={{ color: '#274ac7' }}>Weather History</h1>
      <p>See past weather data here.</p>

      <DatePickerWrapper>
        <DatePickerInput
          type="date"
          value={selectedDate}
          onChange={handleDateChange}
        />
        <ShowDateButton onClick={toggleSpecificDateView}>
          {specificDate ? 'Hide Weather' : 'Get Weather'}
        </ShowDateButton>
      </DatePickerWrapper>

      {loading && <p>Loading...</p>}

      {noData && !loading && (
        <NoDataMessage>
          No data for the selected date. Please select another date.
        </NoDataMessage>
      )}

      {specificDate && weatherData && !loading && !noData && (
        <WeatherSection>
          <h2 style={{ color: '#274ac7' }}>Weather for {selectedDate}</h2>
          <Table>
            <thead>
              <tr>
                <TableHeader>Temperature (°C)</TableHeader>
                <TableHeader>Humidity (%)</TableHeader>
                <TableHeader>Pressure (hPa)</TableHeader>
                <TableHeader>Light Intensity (lx)</TableHeader>
                <TableHeader>Rain Detected</TableHeader>
                <TableHeader>Gas Concentration</TableHeader>
              </tr>
            </thead>
            <tbody>
              <TableRow>
                <TableData>{weatherData.temperature2}°C</TableData>
                <TableData>{weatherData.humidity}%</TableData>
                <TableData>{weatherData.pressure} hPa</TableData>
                <TableData>{weatherData.lightIntensity} lx</TableData>
                <TableData>{weatherData.rainDetected ? 'Yes' : 'No'}</TableData>
                <TableData>{weatherData.gasConcentration}</TableData>
              </TableRow>
            </tbody>
          </Table>
        </WeatherSection>
      )}

      <ShowDateButton onClick={toggleHistoryView}>
        {showHistory ? 'Hide History' : 'Show Weather History'}
      </ShowDateButton>

      {showHistory && !loading && historyData.length > 0 && (
        <Table>
          <thead>
            <tr>
              <TableHeader>Temperature (°C)</TableHeader>
              <TableHeader>Humidity (%)</TableHeader>
              <TableHeader>Pressure (hPa)</TableHeader>
              <TableHeader>Light Intensity (lx)</TableHeader>
              <TableHeader>Rain Detected</TableHeader>
              <TableHeader>Gas Concentration</TableHeader>
            </tr>
          </thead>
          <tbody>
            {historyData.map((data, index) => (
              <TableRow key={index}>
                <TableData>{data.temperature2}°C</TableData>
                <TableData>{data.humidity}%</TableData>
                <TableData>{data.pressure} hPa</TableData>
                <TableData>{data.lightIntensity} lx</TableData>
                <TableData>{data.rainDetected ? 'Yes' : 'No'}</TableData>
                <TableData>{data.gasConcentration}</TableData>
              </TableRow>
            ))}
          </tbody>
        </Table>
      )}
    </HistoryContainer>
  );
};

export default History;
