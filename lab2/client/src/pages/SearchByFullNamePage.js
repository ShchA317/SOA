import React, { useState } from "react";
import { searchByFullName } from "../api/organizationApi";

const SearchByFullNamePage = () => {
    const [substring, setSubstring] = useState("");
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

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
                        {results.map((org) => (
                            <tr key={org.id}>
                                <td>{org.id}</td>
                                <td>{org.name}</td>
                                <td>{org.fullName}</td>
                                <td>{org.creationDate}</td>
                                <td>{org.annualTurnover}</td>
                                <td>{org.employeesCount}</td>
                                <td>{org.type}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
            {results.length === 0 && !loading && <p>Ничего не найдено</p>}
        </div>
    );
};

export default SearchByFullNamePage;
