import React from 'react';
import styled from 'styled-components';

const HistoryContainer = styled.div`
  background: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  max-width: 600px;
  width: 100%;
`;

const History: React.FC = () => {
  return (
    <HistoryContainer>
      <h1>Weather History</h1>
      <p>See past weather data here.</p>
    </HistoryContainer>
  );
};

export default History;
