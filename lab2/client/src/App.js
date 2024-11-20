import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import './App.css';
import OrganizationForm from "./pages/OrganizationForm";
import OrganizationPage from "./pages/OrganizationPage";
import DeleteOrganizationPage from "./pages/DeleteOrganizationPage";
import UpdateOrganizationPage from "./pages/UpdateOrganizationPage";
import OrganizationListPage from "./pages/OrganizationListPage";
import CountByEmployeesPage from "./pages/CountByEmployeesPage";
import SearchByFullNamePage from "./pages/SearchByFullNamePage";
import OrganizationGroupByAddressPage from "./pages/OrganizationGroupByAddressPage";
import MergeOrganizationsPage from "./pages/MergeOrganizationsPage";

function Home() {
    return (
        <div className="home">
            <h2>Добро пожаловать! 🌟</h2>
            <p>Используйте навигацию, чтобы управлять организациями. Да, это правда важно. Наверное.</p>
            <button onClick={() => alert('Вы нажали на крайне полезную кнопку! 🚀')}>
                Нажми меня, если хочешь ничего
            </button>
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
                    <li> <Link to="/list-organization/">Список организаций</Link> </li>
                    <li> <Link to="/group-organization/">Группировать организации по адресу</Link> </li>
                    <li> <Link to="/count-organization/">Подсчитать организации</Link> </li>
                    <li> <Link to="/search-organization/">Искать организации</Link> </li>
                    <li> <Link to="/merge-organizations/">Слить организации</Link> </li>
                </ul>
            </nav>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/get-organization" element={<OrganizationPage />} />
                <Route path="/add-organization" element={<OrganizationForm onSubmit={(data) => console.log("Данные формы:", data)} />} />
                <Route path="/delete-organization" element={<DeleteOrganizationPage />} />
                <Route path="/update-organization" element={<UpdateOrganizationPage />} />
                <Route path="/list-organization" element={<OrganizationListPage />} />
                <Route path="/count-organization" element={<CountByEmployeesPage />} />
                <Route path="/search-organization" element={<SearchByFullNamePage />} />
                <Route path="/group-organization" element={<OrganizationGroupByAddressPage />} />
                <Route path="/merge-organizations" element={<MergeOrganizationsPage />} />
            </Routes>
        </Router>
    );
}

export default App;