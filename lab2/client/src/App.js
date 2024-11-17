import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import OrganizationForm from "./pages/OrganizationForm";
import OrganizationPage from "./pages/OrganizationPage";
import DeleteOrganizationPage from "./pages/DeleteOrganizationPage";
import UpdateOrganizationPage from "./pages/UpdateOrganizationPage";
import OrganizationListPage from "./pages/OrganizationListPage";

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
                  <li> <Link to="/">Главная</Link> </li>
                  <li> <Link to="/add-organization">Добавить организацию</Link> </li>
                  <li> <Link to="/get-organization/">Получить организацию</Link> </li>
                  <li> <Link to="/delete-organization/">Удалить организацию</Link> </li>
                  <li> <Link to="/update-organization/">Обновить организацию</Link> </li>
                  <li> <Link to="/list-organization/">Получить список организаций</Link> </li>
              </ul>
          </nav>
          <Routes>
              <Route path="/" element={<Home/>}/>
              <Route path="/get-organization" element={<OrganizationPage/>}/>
              <Route path="/add-organization" element={<OrganizationForm onSubmit={(data) => console.log("Данные формы:", data)} />} />
              <Route path="/delete-organization" element={<DeleteOrganizationPage />} />
              <Route path="/update-organization" element={<UpdateOrganizationPage />} />
              <Route path="/list-organization" element={<OrganizationListPage />} />
          </Routes>
      </Router>
  );
}

export default App;