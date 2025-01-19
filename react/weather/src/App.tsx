import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import Home from './components/Home';
import History from './components/History';
import Settings from './components/Settings';
import StartScreen from './components/StartScreen';
import logo from './assets/logo.png';

const NavBar = styled.nav`
  background: #274ac7;
  width: 100%;
  height: 100px;
  padding: 15px 0; 
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
`;

const Logo = styled.img`
  height: 75px;
  width: auto;
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

const Layout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const navigate = useNavigate();
  const handle_logout = () => {
    sessionStorage.removeItem('token');
    navigate('/');
  }
  return (
    <>
      <NavBar>
        <Logo src={logo} alt="Logo" />
        <NavList>
          <NavItem>
            <StyledLink to="/home">Home</StyledLink>
          </NavItem>
          <NavItem>
            <StyledLink to="/history">History</StyledLink>
          </NavItem>
          <NavItem>
            <StyledLink to="/settings">Settings</StyledLink>
          </NavItem>
          <NavItem>
            <StyledLink to="/" onClick={handle_logout}>Logout</StyledLink>
          </NavItem>
        </NavList>
      </NavBar>
      <MainContent>{children}</MainContent>
    </>
  );
};

const MainContent = styled.main`
  display: flex;
  background: #7b8ddb;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  flex-grow: 1;
  height: 91.6vh;
  padding: 20px;
  box-sizing: border-box;
  overflow: hidden;
`;

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<StartScreen backgroundImage='/start.jpg'/>} />
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
  );
};

export default App;
