import React from 'react';
import styled from 'styled-components';

const SettingsContainer = styled.div`
  background: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  max-width: 600px;
  width: 100%;
`;

const Settings: React.FC = () => {
  return (
    <SettingsContainer>
      <h1>Settings</h1>
      <p>Here you can configure your preferences.</p>
    </SettingsContainer>
  );
};

export default Settings;
