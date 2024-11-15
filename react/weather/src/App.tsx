import React from 'react';
import logo from './logo.svg';
import './App.css';
import { mockApiFunction } from './lib/api';

async function App() {

  const textFromApi = await mockApiFunction();

  console.log(textFromApi);

  return (
    <div className="bg-neutral-50">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="bottom-2"
          href="https://pwr.com"
          target="_blank"
          rel="noopener noreferrer"
        >
          {/* {textFromApi.finally()} */}
        </a>
      </header>
    </div>
  );
}

export default App;
