import React, { useState } from "react";
import { countByEmployeesCount } from "../api/organizationApi";

const CountByEmployeesPage = () => {
    const [employeeCount, setEmployeeCount] = useState("");
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const data = await countByEmployeesCount(employeeCount);
            setResult(data.count);
        } catch (err) {
            setError(err.message || "Произошла ошибка");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h1>Количество организаций с количеством сотрудников больше заданного</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Количество сотрудников:
                    <input
                        type="number"
                        value={employeeCount}
                        onChange={(e) => setEmployeeCount(e.target.value)}
                        required
                    />
                </label>
                <button type="submit" disabled={loading}>
                    Получить количество
                </button>
            </form>
            {loading && <p>Загрузка...</p>}
            {error && <p style={{ color: "red" }}>Ошибка: {error}</p>}
            {result !== null && <p>Количество организаций: {result}</p>}
        </div>
    );
};

export default CountByEmployeesPage;
