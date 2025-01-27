import React, { createContext, useState, useEffect, ReactNode } from 'react';

interface LanguageContextType {
  language: string;
  setLanguage: (language: string) => void;
}

export const LanguageContext = createContext<LanguageContextType>({
  language: 'English',
  setLanguage: () => {},
});

interface LanguageProviderProps {
  children: ReactNode;
}

export const LanguageProvider: React.FC<LanguageProviderProps> = ({
  children,
}) => {
  const [language, setLanguage] = useState<string>('English');

  useEffect(() => {
    const savedLanguage = sessionStorage.getItem('language') || 'English';
    setLanguage(savedLanguage);
  }, []);

  const handleLanguageChange = (language: string) => {
    setLanguage(language);
    sessionStorage.setItem('language', language);
  };

  return (
    <LanguageContext.Provider
      value={{ language, setLanguage: handleLanguageChange }}
    >
      {children}
    </LanguageContext.Provider>
  );
};
