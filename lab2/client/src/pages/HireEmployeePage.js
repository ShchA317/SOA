import React, { useState } from "react";
import {hireEmployOrganizations} from "../api/organizationApi";

const HireEmployeePage = () => {
    const [organizationId, setOrganizationId] = useState("");
    const [employeeName, setEmployeeName] = useState("");
    const [position, setPosition] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleHire = async () => {
        if (!organizationId || !employeeName || !position) {
            setError("Пожалуйста, заполните все поля.");
            return;
        }

        try {
            setLoading(true);
            setError(null);
            setSuccess(null);

            const response = await  hireEmployOrganizations(organizationId, JSON.stringify({ employeeName, position }) )

            if (response.status !== 200) {
                if (response.status === 404) {
                    throw new Error("Организация не найдена.");
                } else if (response.status === 400) {
                    throw new Error("Ошибка валидации данных сотрудника.");
                } else {
                    throw new Error("Произошла внутренняя ошибка сервера.");
                }
            }

            setSuccess(`Сотрудник успешно добавлен.`);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h1>Добавление Сотрудника</h1>
            <div>
                <label>ID организации:</label>
                <input
                    type="number"
                    value={organizationId}
                    onChange={(e) => setOrganizationId(e.target.value)}
                    placeholder="ID организации"
                />
            </div>

            <div>
                <label>Имя сотрудника:</label>
                <input
                    type="text"
                    value={employeeName}
                    onChange={(e) => setEmployeeName(e.target.value)}
                    placeholder="Имя сотрудника"
                />
            </div>

            <div>
                <label>Должность сотрудника:</label>
                <input
                    type="text"
                    value={position}
                    onChange={(e) => setPosition(e.target.value)}
                    placeholder="Должность"
                />
            </div>

            <button onClick={handleHire} disabled={loading}>
                {loading ? "Добавление..." : "Добавить сотрудника"}
            </button>

            {error && <p style={{ color: "red" }}>Ошибка: {error}</p>}
            {success && <p style={{ color: "green" }}>{success}</p>}
        </div>
    );
};

export default HireEmployeePage;
