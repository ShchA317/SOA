import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import OrganizationsList from './pages/OrganizationsList';
import OrganizationDetails from './pages/OrganizationDetails';

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<OrganizationsList />} />
          <Route path="/organizations/:id" element={<OrganizationDetails />} />
        </Routes>
      </Router>
  );
}

export default App;