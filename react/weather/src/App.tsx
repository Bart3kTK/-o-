import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import styled from 'styled-components';
import Home from './components/Home';
import History from './components/History';
import Settings from './components/Settings';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  flex-grow: 1;
  z-index: 1;
  position: relative;
`;

const NavBar = styled.nav`
  background: #4e2f96;
  width: 100%;
  padding: 10px 0;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
`;

const NavList = styled.ul`
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  gap: 15px;
`;

const NavItem = styled.li``;

const StyledLink = styled(Link)`
  text-decoration: none;
  color: #ffffff;
  padding: 10px 20px;
  font-weight: 500;
  border-radius: 5px;
  transition: background 0.3s ease;
  &:hover {
    background: rgba(255, 255, 255, 0.2);
  }
`;

const App: React.FC = () => {
  return (
    <>
      <Router>
        <Container>
          <NavBar>
            <NavList>
              <NavItem>
                <StyledLink to="/">Home</StyledLink>
              </NavItem>
              <NavItem>
                <StyledLink to="/history">History</StyledLink>
              </NavItem>
              <NavItem>
                <StyledLink to="/settings">Settings</StyledLink>
              </NavItem>
            </NavList>
          </NavBar>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/history" element={<History />} />
            <Route path="/settings" element={<Settings />} />
          </Routes>
        </Container>
      </Router>
    </>
  );
};

export default App;
