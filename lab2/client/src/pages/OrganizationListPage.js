import React, { useState, useEffect } from "react";
import { getOrganizations } from "../api/organizationApi";

const OrganizationListPage = () => {
    const [organizations, setOrganizations] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const [filters, setFilters] = useState({
        creationDate: "",
        annualTurnover: "",
        sort: "",
    });

    // Функция для загрузки списка организаций
    const fetchOrganizations = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await getOrganizations(filters); // Предполагается, что в API передаются параметры фильтрации
            if (response.status !== 200) {
                throw new Error("Ошибка при загрузке списка организаций");
            }
            setOrganizations(response.data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    // Обновляем список при изменении фильтров
    useEffect(() => {
        fetchOrganizations();
    }, [filters]);

    // Обработчик изменения фильтров
    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters((prevFilters) => ({ ...prevFilters, [name]: value }));
    };

    return (
        <div>
            <h1>Список организаций</h1>

            <div>
                <h2>Фильтры</h2>
                <label>
                    Дата создания после:
                    <input
                        type="date"
                        name="creationDate"
                        value={filters.creationDate}
                        onChange={handleFilterChange}
                    />
                </label>
                <br />
                <label>
                    Годовой оборот меньше чем:
                    <input
                        type="number"
                        name="annualTurnover"
                        value={filters.annualTurnover}
                        onChange={handleFilterChange}
                    />
                </label>
                <br />
                <label>
                    Сортировка:
                    <select name="sort" value={filters.sort} onChange={handleFilterChange}>
                        <option value="">Без сортировки</option>
                        <option value="name,asc">Имя (по возрастанию)</option>
                        <option value="name,desc">Имя (по убыванию)</option>
                        <option value="creationDate,asc">Дата создания (по возрастанию)</option>
                        <option value="creationDate,desc">Дата создания (по убыванию)</option>
                    </select>
                </label>
                <br />
                <button onClick={fetchOrganizations} disabled={loading}>
                    Применить фильтры
                </button>
            </div>

            {loading && <p>Загрузка...</p>}
            {error && <p style={{ color: "red" }}>Ошибка: {error}</p>}

            <div>
                <h2>Организации</h2>
                {organizations.length === 0 && !loading && <p>Организации не найдены</p>}
                <table border="1">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Дата создания</th>
                        <th>Годовой оборот</th>
                        <th>Количество сотрудников</th>
                        <th>Тип</th>
                    </tr>
                    </thead>
                    <tbody>
                    {organizations.map((org) => (
                        <tr key={org.id}>
                            <td>{org.id}</td>
                            <td>{org.name}</td>
                            <td>{org.creationDate}</td>
                            <td>{org.annualTurnover}</td>
                            <td>{org.employeesCount}</td>
                            <td>{org.type}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default OrganizationListPage;
