import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';

const SignupContainer = styled.div<{ backgroundImage: string }>`
  background-image: url(${(props) => props.backgroundImage});
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-size: cover;
  background-color: #f5f5f5;
`;

const SignupForm = styled(Form)`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: white;
  padding: 20px;
  height: 40vh;
  width: 30vh;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
`;

const InputField = styled.input`
  padding: 10px;
  margin-top: 20px;
  margin-bottom: 20px;
  font-size: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 100%;
`;

const ErrorText = styled.div`
  color: red;
  margin-bottom: 20px;
`;

const Button = styled.button`
  padding: 20px 40px;
  font-size: 18px;
  color: white;
  background-color: #4e2f96;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 20px;
  &:hover {
    background-color: #6b3fbb;
  }
`;

const validationSchema = Yup.object({
  login: Yup.string().required('login is required'),
  password: Yup.string().required('Password is required'),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('password')], 'Passwords must match')
    .required('Confirm Password is required'),
});

const SignupComponent: React.FC<{ backgroundImage: string }> = ({
  backgroundImage,
}) => {
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (values: { login: string; password: string }) => {
    setLoading(true);
    try {
      const response = await fetch(
        'http://localhost:8080/weather/users/login',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            login: values.login,
            password: values.password,
          }),
          credentials: 'include',
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          throw new Error('Unauthorized (401)');
        } else if (response.status === 503) {
          throw new Error('Backend is unavailable. Please try again later.');
        } else {
          throw new Error(`Error: ${response.status}`);
        }
      }

      const data = await response.text();
      console.log(data);
      sessionStorage.setItem('token', data);
      sessionStorage.setItem('username', values.login);
      console.log('Logged in successfully as ' + values.login);
      setError(null);

      navigate('/home');
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleSignup = async (values: { login: string; password: string }) => {
    try {
      console.log(values);
      const response = await fetch('http://localhost:8080/weather/users/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },

        body: JSON.stringify(values),
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }
      handleLogin(values);
    } catch (err: any) {
      setError(err.message);
    }
  };

  return (
    <SignupContainer backgroundImage={backgroundImage}>
      <Formik
        initialValues={{ login: '', password: '', confirmPassword: '' }}
        validationSchema={validationSchema}
        onSubmit={(values) => {
          handleSignup({ login: values.login, password: values.password });
        }}
      >
        {({ isSubmitting }) => (
          <SignupForm>
            <Field
              type="text"
              name="login"
              placeholder="Username"
              as={InputField}
            />
            <ErrorMessage name="login" component={ErrorText} />
            <Field
              type="password"
              name="password"
              placeholder="Password"
              as={InputField}
            />
            <ErrorMessage name="password" component={ErrorText} />
            <Field
              type="password"
              name="confirmPassword"
              placeholder="Confirm Password"
              as={InputField}
            />
            <ErrorMessage name="confirmPassword" component={ErrorText} />
            <Button type="submit" disabled={isSubmitting}>
              Sign Up
            </Button>
            {error && <ErrorText>{error}</ErrorText>}
          </SignupForm>
        )}
      </Formik>
    </SignupContainer>
  );
};

export default SignupComponent;
