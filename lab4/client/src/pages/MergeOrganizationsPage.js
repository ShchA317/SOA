import React, { useState } from "react";
import {mergeOrganizations} from "../api/organizationApi";

const MergeOrganizationsPage = () => {
    const [id1, setId1] = useState("");
    const [id2, setId2] = useState("");
    const [newName, setNewName] = useState("");
    const [newAddress, setNewAddress] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleMerge = async () => {
        if (!id1 || !id2 || !newName || !newAddress) {
            setError("Пожалуйста, заполните все поля.");
            return;
        }

        try {
            setLoading(true);
            setError(null);
            setSuccess(null);

            const response = await mergeOrganizations(id1, id2, newName, newAddress)

            if (response.status !== 200) {
                if (response.status === 404) {
                    throw new Error("Одна или обе организации не найдены.");
                } else if (response.status === 400) {
                    throw new Error("Ошибка валидации параметров.");
                } else {
                    throw new Error("Произошла внутренняя ошибка сервера.");
                }
            }

            console.log(response.data)
            setSuccess(`Организации успешно объединены.`);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h1>Объединение Организаций</h1>
            <div>
                <label>ID первой организации:</label>
                <input
                    type="number"
                    value={id1}
                    onChange={(e) => setId1(e.target.value)}
                    placeholder="ID 1"
                />
            </div>

            <div>
                <label>ID второй организации:</label>
                <input
                    type="number"
                    value={id2}
                    onChange={(e) => setId2(e.target.value)}
                    placeholder="ID 2"
                />
            </div>

            <div>
                <label>Новое имя объединённой организации:</label>
                <input
                    type="text"
                    value={newName}
                    onChange={(e) => setNewName(e.target.value)}
                    placeholder="Новое имя"
                />
            </div>

            <div>
                <label>Новый адрес объединённой организации:</label>
                <input
                    type="text"
                    value={newAddress}
                    onChange={(e) => setNewAddress(e.target.value)}
                    placeholder="Новый адрес"
                />
            </div>

            <button onClick={handleMerge} disabled={loading}>
                {loading ? "Объединение..." : "Объединить"}
            </button>

            {error && <p style={{ color: "red" }}>Ошибка: {error}</p>}
            {success && <p style={{ color: "green" }}>{success}</p>}
        </div>
    );
};

export default MergeOrganizationsPage;
