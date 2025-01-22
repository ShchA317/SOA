import React, { useState } from "react";
import { searchByFullName } from "../api/organizationApi";

const SearchByFullNamePage = () => {
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    const handlePageSizeChange = (e) => {
        setPageSize(Number(e.target.value));
        setCurrentPage(1); // Сбросить текущую страницу при изменении размера
    };

    const [currentPage, setCurrentPage] = useState(1); // Текущая страница

    const [pageSize, setPageSize] = useState(5); // Количество записей на странице
    const [substring, setSubstring] = useState("");

    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const startIndex = (currentPage - 1) * pageSize;

    const totalPages = Math.ceil(results.length / pageSize);
    const endIndex = startIndex + pageSize;
    const paginatedOrganizations = results.slice(startIndex, endIndex);

    const formatDate = (timestamp) => {
        const date = new Date(timestamp);
        return date.toLocaleDateString(); // Преобразует дату в локальный читаемый формат
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const data = await searchByFullName(substring);
            setResults(data);
        } catch (err) {
            setError(err.message || "Произошла ошибка");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h1>Поиск организаций по полному названию</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Подстрока для поиска:
                    <input
                        type="text"
                        value={substring}
                        onChange={(e) => setSubstring(e.target.value)}
                        required
                    />
                </label>
                <button type="submit" disabled={loading}>
                    Найти
                </button>
            </form>
            {loading && <p>Загрузка...</p>}
            {error && <p style={{ color: "red" }}>Ошибка: {error}</p>}
            {results.length > 0 && (
                <div>
                    <h2>Результаты поиска</h2>

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
                            <th>Полное название</th>
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
                                <td>{org.fullName}</td>
                                <td>{formatDate(org.creationDate)}</td>
                                <td>{org.annualTurnover}</td>
                                <td>{org.employeesCount}</td>
                                <td>{org.orgType}</td>
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
                        {Array.from({length: totalPages}, (_, index) => (
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
            )}
            {results.length === 0 && !loading && <p>Ничего не найдено</p>}
        </div>
    );
};

export default SearchByFullNamePage;
