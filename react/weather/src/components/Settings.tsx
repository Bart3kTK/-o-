import React, { useState, useEffect, useContext } from 'react';
import styled from 'styled-components';
import { DarkModeContext } from '../context/DarkModeContext';
import { LanguageContext } from '../context/LanguageContext';
import { UnitContext } from '../context/UnitContext';

interface EspUserSettingsDTO {
  unit: string;
  language: string;
  darkModeOn: boolean;
}

const SettingsContainer = styled.div<{ darkMode: boolean }>`
  background: ${(props) => (props.darkMode ? '#0d2782' : '#f5f5f5')};
  color: ${(props) => (props.darkMode ? '#fff' : '#000')};
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  max-width: 600px;
  width: 100%;
  text-align: center;
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

const Select = styled.select<{ darkMode: boolean }>`
  color: ${(props) => (props.darkMode ? '#ffffff' : '#000000')};
  background: ${(props) => (props.darkMode ? '#274ac7' : '#ffffff')};
  padding: 10px;
  font-size: 16px;
  border-radius: 8px;
  border: 1px solid ${(props) => (props.darkMode ? '#ffffff' : '#ccc')};
  margin-bottom: 10px;
  width: 100%;
  box-shadow: ${(props) =>
    props.darkMode ? '0 2px 5px rgba(255, 255, 255, 0.2)' : 'none'};
  transition:
    background 0.3s ease,
    color 0.3s ease;

  &:focus {
    outline: none;
    border-color: ${(props) => (props.darkMode ? '#ffcc00' : '#274ac7')};
  }
`;

const Switch = styled.input<{ darkMode: boolean }>`
  margin-right: 10px;
  width: 20px;
  height: 20px;
  accent-color: ${(props) => (props.darkMode ? '#ffcc00' : '#274ac7')};
`;

const Settings: React.FC = () => {
  const { darkMode, toggleDarkMode } = useContext(DarkModeContext);
  const { language, setLanguage } = useContext(LanguageContext);
  const { unit, setUnit } = useContext(UnitContext);

  const initialSettings: EspUserSettingsDTO = {
    unit: unit,
    language: language,
    darkModeOn: darkMode,
  };

  const [settings, setSettings] = useState<EspUserSettingsDTO>(initialSettings);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const loggedInUser = sessionStorage.getItem('username');

  useEffect(() => {
    setSettings((prev) => ({ ...prev, darkModeOn: darkMode, language, unit }));
  }, [darkMode, language, unit]);

  const handleUnitChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setUnit(event.target.value);
    setSettings({ ...settings, unit: event.target.value });
  };

  const handleLanguageChange = (
    event: React.ChangeEvent<HTMLSelectElement>
  ) => {
    setLanguage(event.target.value);
    setSettings({ ...settings, language: event.target.value });
  };

  const handleDarkModeChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    toggleDarkMode(event.target.checked);
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

      const settingsData: EspUserSettingsDTO = await response.json();
      setSettings(settingsData);
      setError(null);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <SettingsContainer darkMode={darkMode}>
      <Title darkMode={darkMode}>
        {language === 'Spanish'
          ? 'Configuraciones'
          : language === 'Polish'
            ? 'Ustawienia'
            : 'Settings'}
      </Title>

      <Subtitle>
        {language === 'Spanish'
          ? 'Aquí puedes configurar tus preferencias.'
          : language === 'Polish'
            ? 'Tutaj możesz skonfigurować swoje preferencje.'
            : 'Here you can configure your preferences.'}
      </Subtitle>

      <div>
        <label htmlFor="unit">
          {language === 'Spanish'
            ? 'Unidad:'
            : language === 'Polish'
              ? 'Jednostka:'
              : 'Unit:'}
        </label>
        <Select
          darkMode={darkMode}
          id="unit"
          value={settings.unit}
          onChange={handleUnitChange}
        >
          <option value="Celsius">
            {language === 'Spanish'
              ? 'Celsius'
              : language === 'Polish'
                ? 'Celsjusz'
                : 'Celsius'}
          </option>
          <option value="Fahrenheit">
            {language === 'Spanish'
              ? 'Fahrenheit'
              : language === 'Polish'
                ? 'Fahrenheit'
                : 'Fahrenheit'}
          </option>
        </Select>
      </div>

      <div>
        <label htmlFor="language">
          {language === 'Spanish'
            ? 'Idioma:'
            : language === 'Polish'
              ? 'Język:'
              : 'Language:'}
        </label>
        <Select
          darkMode={darkMode}
          id="language"
          value={settings.language}
          onChange={handleLanguageChange}
        >
          <option value="English">
            {language === 'Spanish'
              ? 'Inglés'
              : language === 'Polish'
                ? 'Angielski'
                : 'English'}
          </option>
          <option value="Spanish">
            {language === 'Spanish'
              ? 'Español'
              : language === 'Polish'
                ? 'Hiszpański'
                : 'Spanish'}
          </option>
          <option value="Polish">
            {language === 'Spanish'
              ? 'Polaco'
              : language === 'Polish'
                ? 'Polski'
                : 'Polish'}
          </option>
        </Select>
      </div>

      <div>
        <Switch
          darkMode={darkMode}
          type="checkbox"
          id="darkMode"
          checked={settings.darkModeOn}
          onChange={handleDarkModeChange}
        />
        <label htmlFor="darkMode">
          {language === 'Spanish'
            ? 'Modo oscuro'
            : language === 'Polish'
              ? 'Tryb ciemny'
              : 'Dark Mode'}
        </label>
      </div>

      <SaveButton onClick={handleSave}>
        {loading
          ? language === 'Spanish'
            ? 'Guardando...'
            : language === 'Polish'
              ? 'Zapisywanie...'
              : 'Saving...'
          : language === 'Spanish'
            ? 'Guardar configuraciones'
            : language === 'Polish'
              ? 'Zapisz ustawienia'
              : 'Save Settings'}
      </SaveButton>

      {error && <div>{error}</div>}
    </SettingsContainer>
  );
};

export default Settings;
