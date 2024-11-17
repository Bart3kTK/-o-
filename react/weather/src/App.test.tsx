import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import App from './App';

jest.mock('./components/Home', () => () => <div>Home Page</div>);
jest.mock('./components/History', () => () => <div>History Page</div>);
jest.mock('./components/Settings', () => () => <div>Settings Page</div>);

describe('App Component', () => {
  test('renders home page by default', () => {
    render(
      <Router>
        <App />
      </Router>
    );

    expect(screen.getByText('Home Page')).toBeInTheDocument();
  });

  test('navigates to History page when History link is clicked', async () => {
    render(
      <Router>
        <App />
      </Router>
    );

    fireEvent.click(screen.getByText('History'));

    await waitFor(() => screen.getByText('History Page'));

    expect(screen.getByText('History Page')).toBeInTheDocument();
  });

  test('navigates to Settings page when Settings link is clicked', async () => {
    render(
      <Router>
        <App />
      </Router>
    );

    fireEvent.click(screen.getByText('Settings'));

    await waitFor(() => screen.getByText('Settings Page'));

    expect(screen.getByText('Settings Page')).toBeInTheDocument();
  });

  test('has the correct navigation links', () => {
    render(
      <Router>
        <App />
      </Router>
    );

    expect(screen.getByText('Home')).toBeInTheDocument();
    expect(screen.getByText('History')).toBeInTheDocument();
    expect(screen.getByText('Settings')).toBeInTheDocument();
  });
});
