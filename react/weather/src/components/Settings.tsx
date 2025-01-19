import React, { useState, useEffect } from 'react';
import styled from 'styled-components';

interface EspUserSettingsDTO {
  unit: string;
  language: string;
  darkModeOn: boolean;
}

const SettingsContainer = styled.div`
  background: #f5f5f5;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  max-width: 600px;
  width: 100%;
  text-align: center;
`;

const SettingsInput = styled.input`
  padding: 10px;
  font-size: 16px;
  border-radius: 8px;
  border: 1px solid #ccc;
  margin-bottom: 10px;
  width: 100%;
`;

const Select = styled.select`
  padding: 10px;
  font-size: 16px;
  border-radius: 8px;
  border: 1px solid #ccc;
  margin-bottom: 10px;
  width: 100%;
`;

const Switch = styled.input`
  margin-right: 10px;
`;

const SaveButton = styled.button`
  padding: 10px 20px;
  font-size: 16px;
  color: #ffffff;
  background-color: #274ac7;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  margin-top: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  transition: background 0.3s ease;

  &:hover {
    background: rgba(255, 255, 255, 0.2);
  }
`;

const ErrorMessage = styled.div`
  color: red;
  margin-top: 10px;
`;

const Settings: React.FC = () => {
  const initialSettings: EspUserSettingsDTO = {
    unit: 'Celsius',
    language: 'English',
    darkModeOn: false,
  };

  const [settings, setSettings] = useState<EspUserSettingsDTO>(initialSettings);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const loggedInUser = sessionStorage.getItem('username');

  useEffect(() => {
    if (settings.darkModeOn) {
      document.body.style.backgroundColor = '#333';
      document.body.style.color = '#fff';
    } else {
      document.body.style.backgroundColor = '#fff';
      document.body.style.color = '#000';
    }
  }, [settings.darkModeOn]);

  const handleUnitChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSettings({ ...settings, unit: event.target.value });
  };

  const handleLanguageChange = (
    event: React.ChangeEvent<HTMLSelectElement>
  ) => {
    setSettings({ ...settings, language: event.target.value });
  };

  const handleDarkModeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSettings({ ...settings, darkModeOn: event.target.checked });
  };

  const handleSave = async () => {
    if (!loggedInUser) {
      setError('Please log in to save settings');
      return;
    }

    sessionStorage.setItem('userSettings', JSON.stringify(settings));

    try {
      setLoading(true);
      const response = await fetch(
        `http://localhost:8080/weather/users/settings?login=${loggedInUser}`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(settings),
        }
      );

      if (!response.ok) {
        throw new Error('Failed to save settings');
      }

      alert('Settings saved successfully!');
      setError(null);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <SettingsContainer>
      <h1>Settings</h1>
      <p>Here you can configure your preferences.</p>

      <div>
        <label htmlFor="unit">Unit:</label>
        <Select id="unit" value={settings.unit} onChange={handleUnitChange}>
          <option value="Celsius">Celsius</option>
          <option value="Fahrenheit">Fahrenheit</option>
        </Select>
      </div>

      <div>
        <label htmlFor="language">Language:</label>
        <Select
          id="language"
          value={settings.language}
          onChange={handleLanguageChange}
        >
          <option value="English">English</option>
          <option value="Spanish">Spanish</option>
          <option value="French">French</option>
        </Select>
      </div>

      <div>
        <label htmlFor="darkMode">
          <Switch
            type="checkbox"
            id="darkMode"
            checked={settings.darkModeOn}
            onChange={handleDarkModeChange}
          />
          Dark Mode
        </label>
      </div>

      <SaveButton onClick={handleSave} disabled={loading}>
        {loading ? 'Saving...' : 'Save Settings'}
      </SaveButton>
      {error && <div>{error}</div>}
    </SettingsContainer>
  );
};

export default Settings;
