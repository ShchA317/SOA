import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import OrganizationsList from './pages/OrganizationsList';
import OrganizationDetails from './pages/OrganizationDetails';
import OrganizationForm from "./components/form/OrganizationForm";

function Home() {
    return (
        <div>
            <h2>Добро пожаловать на главную страницу</h2>
            <p>Используйте навигацию для перехода к форме добавления организации.</p>
        </div>
    );
}

function App() {
  return (
      <Router>
          <nav>
              <ul>
                  <li>
                      <Link to="/">Главная</Link>
                  </li>
                  <li>
                      <Link to="/add-organization">Добавить организацию</Link>
                  </li>
              </ul>
          </nav>
          <Routes>
              {/*<Route path="/" element={<OrganizationsList/>}/>*/}
              <Route path="/" element={<Home />} />
              <Route path="/organizations/:id" element={<OrganizationDetails/>}/>
              <Route path="/add-organization" element={<OrganizationForm onSubmit={(data) => console.log("Данные формы:", data)} />} />
          </Routes>
      </Router>
  );
}

export default App;