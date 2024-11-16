import React, { useState } from 'react';
import {removeOrganization} from "../api/organizationApi";

// Компонент для удаления организации
const DeleteOrganizationPage = () => {
    const [organizationId, setOrganizationId] = useState(''); // Состояние для хранения введенного ID
    const [loading, setLoading] = useState(false); // Состояние для отображения загрузки
    const [error, setError] = useState(null); // Состояние для отображения ошибки
    const [success, setSuccess] = useState(null); // Состояние для отображения успеха

    // Функция для удаления организации по ID
    const deleteOrganization = async () => {
        if (!organizationId) {
            setError('Пожалуйста, введите ID организации');
            return;
        }

        try {
            setLoading(true);
            setError(null);
            setSuccess(null);
            const response = await removeOrganization(organizationId);

            if (response.status!==204) {
                if (response.status === 404) {
                    throw new Error('Организация не найдена');
                } else {
                    throw new Error(`Ошибка: ${response.status}`);
                }
            }

            setSuccess('Организация успешно удалена');
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h1>Удаление Организации</h1>

            <div>
                <label htmlFor="orgIdInput">Введите ID организации для удаления:</label>
                <input
                    type="text"
                    id="orgIdInput"
                    value={organizationId}
                    onChange={(e) => setOrganizationId(e.target.value)}
                    placeholder="ID организации"
                />
                <button onClick={deleteOrganization} disabled={loading}>
                    Удалить организацию
                </button>
            </div>

            {loading && <p>Загрузка...</p>}
            {error && <p style={{ color: 'red' }}>Ошибка: {error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
        </div>
    );
};

export default DeleteOrganizationPage;
