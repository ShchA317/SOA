import React, { useState, useEffect } from "react";
import { getOrganizations } from "../api/organizationApi";

const OrganizationListPage = () => {
    const [organizations, setOrganizations] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const formatDate = (timestamp) => {
        const date = new Date(timestamp);
        return date.toLocaleDateString(); // Преобразует дату в локальный читаемый формат
    };

    const [filters, setFilters] = useState({
        creationDate: "",
        annualTurnover: "",
        sort: "",
    });

    const [currentPage, setCurrentPage] = useState(1); // Текущая страница
    const [pageSize, setPageSize] = useState(5); // Количество записей на странице

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

    // Подсчет индексов начала и конца для текущей страницы
    const startIndex = (currentPage - 1) * pageSize;
    const endIndex = startIndex + pageSize;
    const paginatedOrganizations = organizations.slice(startIndex, endIndex);

    // Обработчики для пагинации
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handlePageSizeChange = (e) => {
        setPageSize(Number(e.target.value));
        setCurrentPage(1); // Сбросить текущую страницу при изменении размера
    };

    const totalPages = Math.ceil(organizations.length / pageSize);

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

                <label>
                    Количество записей на странице:
                    <select value={pageSize} onChange={handlePageSizeChange}>
                        <option value={5}>5</option>
                        <option value={10}>10</option>
                        <option value={15}>15</option>
                    </select>
                </label>

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
                    {paginatedOrganizations.map((org) => (
                        <tr key={org.id}>
                            <td>{org.id}</td>
                            <td>{org.name}</td>
                            <td>{formatDate(org.creationDate)}</td>
                            <td>{org.annualTurnover}</td>
                            <td>{org.employeesCount}</td>
                            <td>{org.type}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                <div>
                    <button
                        disabled={currentPage === 1}
                        onClick={() => handlePageChange(currentPage - 1)}
                    >
                        Предыдущая
                    </button>
                    {Array.from({ length: totalPages }, (_, index) => (
                        <button
                            key={index}
                            disabled={currentPage === index + 1}
                            onClick={() => handlePageChange(index + 1)}
                        >
                            {index + 1}
                        </button>
                    ))}
                    <button
                        disabled={currentPage === totalPages}
                        onClick={() => handlePageChange(currentPage + 1)}
                    >
                        Следующая
                    </button>
                </div>
            </div>
        </div>
    );
};

export default OrganizationListPage;
