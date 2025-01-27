import React, { useState, useContext } from 'react';
import styled from 'styled-components';
import { DarkModeContext } from '../context/DarkModeContext';
import { LanguageContext } from '../context/LanguageContext';
import { UnitContext } from '../context/UnitContext';

interface WeatherDTO {
  pressure: number;
  temperature1: number;
  temperature2: number;
  rainDetected: number;
  humidity: number;
  lightIntensity: number;
  gasConcentration: number;
}

const HistoryContainer = styled.div<{ darkMode: boolean }>`
  background: ${(props) => (props.darkMode ? '#0d2782' : '#f5f5f5')};
  color: ${(props) => (props.darkMode ? '#fff' : '#000')};
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  max-width: 1000px;
  width: 100%;
  text-align: center;
  margin: 0 auto;
`;

const Title = styled.h1<{ darkMode: boolean }>`
  color: ${(props) => (props.darkMode ? '#ffcc00' : '#000')};
  font-size: 24px;
  margin-bottom: 10px;
`;

const Subtitle = styled.p`
  font-size: 16px;
  margin-bottom: 20px;
`;

const DatePickerWrapper = styled.div`
  margin: 20px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const DatePickerInput = styled.input<{ darkMode: boolean }>`
  padding: 10px;
  font-size: 16px;
  border-radius: 8px;
  border: 1px solid ${(props) => (props.darkMode ? '#fff' : '#ccc')};
  margin-bottom: 10px;
  background: ${(props) => (props.darkMode ? '#274ac7' : '#fff')};
  color: ${(props) => (props.darkMode ? '#fff' : '#000')};
  transition:
    background 0.3s ease,
    color 0.3s ease;

  &:focus {
    outline: none;
    border-color: #ffcc00;
  }
`;

const ShowDateButton = styled.button`
  padding: 10px 20px;
  font-size: 16px;
  color: #ffffff;
  background-color: #274ac7;
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
  border-radius: 10px;
  overflow: hidden;
`;

const TableHeader = styled.th`
  padding: 10px;
  background-color: #274ac7;
  color: #ffffff;
  text-align: left;
  font-weight: 600;
  font-size: 16px;
`;

const TableRow = styled.tr<{ darkMode: boolean }>`
  border-bottom: 1px solid #ddd;

  &:nth-child(even) {
    background: ${(props) => (props.darkMode ? '#1d3b9a' : '#f9f9f9')};
  }
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
  margin-bottom: 50px;
`;

const History: React.FC = () => {
  const { language } = useContext(LanguageContext);
  const { darkMode } = useContext(DarkModeContext);
  const { unit } = useContext(UnitContext);
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
    if (!selectedDate) return;

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
      setWeatherData(data);
      setSpecificDate(true);
      setNoData(false);
    } catch {
      setNoData(true);
      setWeatherData(null);
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
      const data = await response.json();
      setHistoryData(data);
    } catch {
      console.error('Failed to fetch history data.');
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

  const convertTemperature = (temp: number): string => {
    if (unit === 'Fahrenheit') {
      return ((temp * 9) / 5 + 32).toFixed(1);
    }
    return temp.toFixed(1);
  };

  return (
    <HistoryContainer darkMode={darkMode}>
      <Title darkMode={darkMode}>
        {language === 'Spanish'
          ? 'Historial del Clima'
          : language === 'Polish'
            ? 'Historia pogody'
            : 'Weather History'}
      </Title>
      <Subtitle>
        {language === 'Spanish'
          ? 'Seleccione una fecha para ver el historial del clima'
          : language === 'Polish'
            ? 'Wybierz datę, aby zobaczyć historię pogody'
            : 'Select a date to view the weather history'}
      </Subtitle>

      <DatePickerWrapper>
        <DatePickerInput
          type="date"
          value={selectedDate}
          onChange={handleDateChange}
          darkMode={darkMode}
        />
        <ShowDateButton onClick={handleButtonClick}>
          {language === 'Spanish'
            ? 'Mostrar Clima'
            : language === 'Polish'
              ? 'Pokaż pogodę'
              : 'Show Weather'}
        </ShowDateButton>
      </DatePickerWrapper>

      {loading && (
        <p>
          {language === 'Spanish'
            ? 'Cargando...'
            : language === 'Polish'
              ? 'Ładowanie...'
              : 'Loading...'}
        </p>
      )}

      {noData && (
        <NoDataMessage>
          {language === 'Spanish'
            ? 'No hay datos para la fecha seleccionada.'
            : language === 'Polish'
              ? 'Brak danych dla wybranej daty.'
              : 'No data for the selected date.'}
        </NoDataMessage>
      )}

      {specificDate && weatherData && (
        <WeatherSection>
          <h2 style={{ color: '#274ac7' }}>
            {language === 'Spanish'
              ? `Clima para el ${selectedDate}`
              : language === 'Polish'
                ? `Pogoda dla ${selectedDate}`
                : `Weather for ${selectedDate}`}
          </h2>
          <Table>
            <thead>
              <tr>
                <TableHeader>
                  {unit === 'Fahrenheit'
                    ? language === 'Spanish'
                      ? 'Temperatura (°F)'
                      : language === 'Polish'
                        ? 'Temperatura (°F)'
                        : 'Temperature (°F)'
                    : language === 'Spanish'
                      ? 'Temperatura (°C)'
                      : language === 'Polish'
                        ? 'Temperatura (°C)'
                        : 'Temperature (°C)'}
                </TableHeader>
                <TableHeader>
                  {language === 'Spanish'
                    ? 'Humedad (%)'
                    : language === 'Polish'
                      ? 'Wilgotność (%)'
                      : 'Humidity (%)'}
                </TableHeader>
                <TableHeader>
                  {language === 'Spanish'
                    ? 'Presión (hPa)'
                    : language === 'Polish'
                      ? 'Ciśnienie (hPa)'
                      : 'Pressure (hPa)'}
                </TableHeader>
                <TableHeader>
                  {language === 'Spanish'
                    ? 'Intensidad Lumínica (lx)'
                    : language === 'Polish'
                      ? 'Natężenie światła (lx)'
                      : 'Light Intensity (lx)'}
                </TableHeader>
                <TableHeader>
                  {language === 'Spanish'
                    ? 'Lluvia Detectada'
                    : language === 'Polish'
                      ? 'Deszcz Wykryty'
                      : 'Rain Detected'}
                </TableHeader>
                <TableHeader>
                  {language === 'Spanish'
                    ? 'Concentración de Gas'
                    : language === 'Polish'
                      ? 'Stężenie gazu'
                      : 'Gas Concentration'}
                </TableHeader>
              </tr>
            </thead>
            <tbody>
              <TableRow darkMode={darkMode}>
                <TableData>
                  {convertTemperature(weatherData.temperature2)}°
                  {unit === 'Fahrenheit' ? 'F' : 'C'}
                </TableData>
                <TableData>{weatherData.humidity}%</TableData>
                <TableData>{weatherData.pressure} hPa</TableData>
                <TableData>{weatherData.lightIntensity} lx</TableData>
                <TableData>
                  {weatherData.rainDetected
                    ? language === 'Spanish'
                      ? 'Sí'
                      : 'Yes'
                    : language === 'Spanish'
                      ? 'No'
                      : 'No'}
                </TableData>
                <TableData>{weatherData.gasConcentration}</TableData>
              </TableRow>
            </tbody>
          </Table>
        </WeatherSection>
      )}

      <ShowDateButton onClick={toggleHistoryView}>
        {showHistory
          ? language === 'Spanish'
            ? 'Ocultar Historial'
            : language === 'Polish'
              ? 'Ukryj historię'
              : 'Hide History'
          : language === 'Spanish'
            ? 'Mostrar Historial del Clima'
            : language === 'Polish'
              ? 'Pokaż historię'
              : 'Show Weather History'}
      </ShowDateButton>

      {showHistory && historyData.length > 0 && (
        <Table>
          <thead>
            <tr>
              <TableHeader>
                {unit === 'Fahrenheit'
                  ? language === 'Spanish'
                    ? 'Temperatura (°F)'
                    : language === 'Polish'
                      ? 'Temperatura (°F)'
                      : 'Temperature (°F)'
                  : language === 'Spanish'
                    ? 'Temperatura (°C)'
                    : language === 'Polish'
                      ? 'Temperatura (°C)'
                      : 'Temperature (°C)'}
              </TableHeader>
              <TableHeader>
                {language === 'Spanish'
                  ? 'Humedad (%)'
                  : language === 'Polish'
                    ? 'Wilgotność (%)'
                    : 'Humidity (%)'}
              </TableHeader>
              <TableHeader>
                {language === 'Spanish'
                  ? 'Presión (hPa)'
                  : language === 'Polish'
                    ? 'Ciśnienie (hPa)'
                    : 'Pressure (hPa)'}
              </TableHeader>
              <TableHeader>
                {language === 'Spanish'
                  ? 'Intensidad Lumínica (lx)'
                  : language === 'Polish'
                    ? 'Natężenie światła (lx)'
                    : 'Light Intensity (lx)'}
              </TableHeader>
              <TableHeader>
                {language === 'Spanish'
                  ? 'Lluvia Detectada'
                  : language === 'Polish'
                    ? 'Deszcz Wykryty'
                    : 'Rain Detected'}
              </TableHeader>
              <TableHeader>
                {language === 'Spanish'
                  ? 'Concentración de Gas'
                  : language === 'Polish'
                    ? 'Stężenie gazu'
                    : 'Gas Concentration'}
              </TableHeader>
            </tr>
          </thead>
          <tbody>
            {historyData.map((data, index) => (
              <TableRow key={index} darkMode={darkMode}>
                <TableData>
                  {convertTemperature(data.temperature2)}°
                  {unit === 'Fahrenheit' ? 'F' : 'C'}
                </TableData>
                <TableData>{data.humidity}%</TableData>
                <TableData>{data.pressure} hPa</TableData>
                <TableData>{data.lightIntensity} lx</TableData>
                <TableData>
                  {data.rainDetected
                    ? language === 'Spanish'
                      ? 'Sí'
                      : language === 'Polish'
                        ? 'Tak'
                        : 'Yes'
                    : language === 'Spanish'
                      ? 'No'
                      : language === 'Polish'
                        ? 'Nie'
                        : 'No'}
                </TableData>
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
