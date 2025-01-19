import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const LandingContainer = styled.div<{ backgroundImage: string }>`
  background-image: url(${(props) => props.backgroundImage});
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-size: cover;
  height: 100vh;
  background-color: #f5f5f5;
`;

const Heading = styled.h1`
  font-size: 48px;
  color: #ffffff;
  margin-bottom: 50px;
`;

const Button = styled.button`
  padding: 20px 40px;
  font-size: 25px;
  color: white;
  background-color: #4e2f96;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin: 20px;
  &:hover {
    background-color: #6b3fbb;
  }
`;

const LandingPage: React.FC<{ backgroundImage: string }> = ({
  backgroundImage,
}) => {
  const navigate = useNavigate();

  return (
    <LandingContainer backgroundImage={backgroundImage}>
      <Heading>Welcome!</Heading>
      <Button onClick={() => navigate('/login')}>Login</Button>
      <Button onClick={() => navigate('/signup')}>Sign Up</Button>
    </LandingContainer>
  );
};

export default LandingPage;
