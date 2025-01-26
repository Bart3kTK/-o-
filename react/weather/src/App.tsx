import React, { useContext } from 'react';
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Link,
  useNavigate,
} from 'react-router-dom';
import styled from 'styled-components';
import Home from './components/Home';
import History from './components/History';
import Settings from './components/Settings';
import StartScreen from './components/StartScreen';
import logo from './assets/logo.png';
import LandingPage from './components/LandingPage';
import SignupComponent from './components/SignUp';
import { DarkModeProvider, DarkModeContext } from './context/DarkModeContext';
import { LanguageProvider, LanguageContext } from './context/LanguageContext';
import { UnitProvider } from './context/UnitContext';

import usFlag from './assets/gb_flag.jpg';
import esFlag from './assets/es_flag.jpg';
import plFlag from './assets/pl_flag.jpg';

const NavBar = styled.nav<{ darkMode: boolean }>`
  background: ${({ darkMode }) => (darkMode ? '#0d2782' : '#274ac7')};
  width: 100%;
  height: 100px;
  padding: 15px 0;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  display: flex;
  justify-content: space-between; /* This aligns items to opposite sides */
  align-items: center;
  padding: 0 20px;
`;

const LogoContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
`;

const Logo = styled.img`
  height: 75px;
  width: auto;
`;

const FlagIcon = styled.img`
  height: 30px;
  width: 45px;
  cursor: pointer;
`;

const NavList = styled.ul`
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  gap: 20px;
  align-items: center;
`;

const NavItem = styled.li``;

const StyledLink = styled(Link)`
  text-decoration: none;
  color: #ffffff;
  padding: 15px 30px;
  font-weight: 100;
  font-size: 40px;
  border-radius: 5px;
  transition: background 0.3s ease;
  &:hover {
    background: rgba(255, 255, 255, 0.2);
  }
`;

const MainContent = styled.main<{ darkMode: boolean }>`
  display: flex;
  background: ${({ darkMode }) => (darkMode ? '#274ac7' : '#7b8ddb')};
  flex-direction: column;
  justify-content: center;
  align-items: center;
  flex-grow: 1;
  height: 91.6vh;
  padding: 20px;
  box-sizing: border-box;
  overflow: hidden;
`;

const Layout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const navigate = useNavigate();
  const { darkMode } = useContext(DarkModeContext);
  const { language, setLanguage } = useContext(LanguageContext);

  const handle_logout = () => {
    sessionStorage.removeItem('token');
    navigate('/');
  };

  const handleLanguageChange = (lang: string) => {
    setLanguage(lang);
  };

  return (
    <>
      <NavBar darkMode={darkMode}>
        <LogoContainer>
          <Logo src={logo} alt="Logo" />
          <FlagIcon
            src={usFlag}
            alt="English"
            onClick={() => handleLanguageChange('English')}
          />
          <FlagIcon
            src={esFlag}
            alt="Spanish"
            onClick={() => handleLanguageChange('Spanish')}
          />
          <FlagIcon
            src={plFlag}
            alt="Polish"
            onClick={() => handleLanguageChange('Polish')}
          />
        </LogoContainer>

        <NavList>
          <NavItem>
            <StyledLink to="/home">
              {language === 'Spanish'
                ? 'Inicio'
                : language === 'Polish'
                  ? 'Strona główna'
                  : 'Home'}
            </StyledLink>
          </NavItem>
          <NavItem>
            <StyledLink to="/history">
              {language === 'Spanish'
                ? 'Historia'
                : language === 'Polish'
                  ? 'Historia'
                  : 'History'}
            </StyledLink>
          </NavItem>
          <NavItem>
            <StyledLink to="/settings">
              {language === 'Spanish'
                ? 'Ajustes'
                : language === 'Polish'
                  ? 'Ustawienia'
                  : 'Settings'}
            </StyledLink>
          </NavItem>
          <NavItem>
            <StyledLink to="/" onClick={handle_logout}>
              {language === 'Spanish'
                ? 'Cerrar sesión'
                : language === 'Polish'
                  ? 'Wyloguj'
                  : 'Logout'}
            </StyledLink>
          </NavItem>
        </NavList>
      </NavBar>

      <MainContent darkMode={darkMode}>{children}</MainContent>
    </>
  );
};

const App: React.FC = () => {
  return (
    <DarkModeProvider>
      <LanguageProvider>
        <UnitProvider>
          <Router>
            <Routes>
              <Route
                path="/"
                element={<LandingPage backgroundImage="/start.jpg" />}
              />
              <Route
                path="/signup"
                element={<SignupComponent backgroundImage="/start.jpg" />}
              />
              <Route
                path="/login"
                element={<StartScreen backgroundImage="/start.jpg" />}
              />
              <Route
                path="/home"
                element={
                  <Layout>
                    <Home />
                  </Layout>
                }
              />
              <Route
                path="/history"
                element={
                  <Layout>
                    <History />
                  </Layout>
                }
              />
              <Route
                path="/settings"
                element={
                  <Layout>
                    <Settings />
                  </Layout>
                }
              />
            </Routes>
          </Router>
        </UnitProvider>
      </LanguageProvider>
    </DarkModeProvider>
  );
};

export default App;
