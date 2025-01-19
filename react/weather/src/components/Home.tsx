import React, { useEffect, useState } from 'react';
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

const HomeContainer = styled.div<{ backgroundImage: string }>`
  background-image: url(${(props) =>
    props.backgroundImage}); /* Obrazek jako tło */
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  max-width: 1000px;
  width: 100%;
  text-align: center;
`;

const Temperature = styled.h2<{ color: string }>`
  font-size: 100px;
  margin: 10px 0;
  color: ${(props) => props.color};
`;

const WeatherDetails = styled.div`
  margin-top: 40px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  text-align: center;
`;

const DetailItem = styled.div`
  flex: 1 1 45%;
  padding: 10px;
  background: #f0f0f0;
  border-radius: 8px;
  height: 100px;
  font-size: 25px;
  margin: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
`;

const NoDataMessage = styled.p`
  font-size: 18px;
  color: red;
  text-align: center;
  margin-top: 20px;
`;

const Home: React.FC = () => {
  const [weatherData, setWeatherData] = useState<WeatherDTO | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [noData, setNoData] = useState<boolean>(false);
  const [backgroundImage, setBackgroundImage] = useState<string>('');
  const [temperatureColor, setTemperatureColor] = useState<string>('#000000');

  const fetchWeatherData = async () => {
    try {
      const response = await fetch('http://localhost:8080/weather/measurments');
      if (!response.ok) {
        if (response.status === 404) {
          throw new Error('Data not found (404)');
        } else if (response.status === 503) {
          throw new Error('Backend is unavailable. Please try again later.');
        } else {
          throw new Error(`Error: ${response.status}`);
        }
      }
      const data: WeatherDTO = await response.json();
      setWeatherData(data);
      setNoData(false);
      setError(null);
    } catch (err: any) {
      setError(err.message);
      setWeatherData(null);
      setNoData(true);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const interval = setInterval(() => {
      fetchWeatherData();
    }, 5000);

    fetchWeatherData();

    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    if (!weatherData) return;

    if (weatherData.temperature2 < 0 && weatherData.rainDetected) {
      setBackgroundImage('/snow.jpg');
      setTemperatureColor('#000000');
    } else if (weatherData.temperature2 < 0) {
      setBackgroundImage('/cold.jpg');
      setTemperatureColor('#ffffff');
    } else if (weatherData.rainDetected) {
      setBackgroundImage('/rain.jpg');
      setTemperatureColor('#ffffff');
    } else if (weatherData.temperature2 <= 30) {
      setBackgroundImage('/sunny.jpg');
      setTemperatureColor('#000000');
    } else {
      setBackgroundImage('/hot.jpg');
      setTemperatureColor('#000000');
    }
  }, [weatherData]);

  const getgasConcentrationIcon = () => {
    if (!weatherData) return '';
    if (weatherData.gasConcentration <= 50) return '🟢';
    if (weatherData.gasConcentration <= 100) return '🟡';
    return '🔴';
  };

  if (loading) return <p>Loading...</p>;
  if (noData)
    return (
      <NoDataMessage>
        Data is currently unavailable. Please try again later.
      </NoDataMessage>
    );

  return (
    <HomeContainer backgroundImage={backgroundImage}>
      {error && <p style={{ color: 'red' }}>Error: {error}</p>}{' '}
      {weatherData && (
        <>
          <Temperature color={temperatureColor}>
            {weatherData.temperature2}°C
          </Temperature>
          <WeatherDetails>
            <DetailItem>
              <strong>Humidity:</strong> {weatherData.humidity}%
            </DetailItem>
            <DetailItem>
              <strong>Pressure:</strong> {weatherData.pressure} hPa
            </DetailItem>
            <DetailItem>
              <strong>Light Intensity:</strong> {weatherData.lightIntensity} lx
            </DetailItem>
            <DetailItem>
              <strong>Gas Concentration:</strong> {weatherData.gasConcentration}{' '}
              {getgasConcentrationIcon()}
            </DetailItem>
          </WeatherDetails>
        </>
      )}
    </HomeContainer>
  );
};

export default Home;
