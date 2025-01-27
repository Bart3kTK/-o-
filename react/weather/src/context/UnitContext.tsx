import React, { createContext, useState, useEffect, ReactNode } from 'react';

interface UnitContextType {
  unit: string;
  setUnit: (unit: string) => void;
}

export const UnitContext = createContext<UnitContextType>({
  unit: 'Celsius',
  setUnit: () => {},
});

interface UnitProviderProps {
  children: ReactNode;
}

export const UnitProvider: React.FC<UnitProviderProps> = ({ children }) => {
  const [unit, setUnit] = useState<string>('Celsius');

  useEffect(() => {
    const savedUnit = sessionStorage.getItem('unit') || 'Celsius';
    setUnit(savedUnit);
  }, []);

  const handleUnitChange = (unit: string) => {
    setUnit(unit);
    sessionStorage.setItem('unit', unit);
  };

  return (
    <UnitContext.Provider value={{ unit, setUnit: handleUnitChange }}>
      {children}
    </UnitContext.Provider>
  );
};
