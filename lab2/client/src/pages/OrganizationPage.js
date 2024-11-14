import React, { useState } from 'react';
import {getOrganizationById} from "../api/organizationApi";

const OrganizationPage = () => {
    const [organizationId, setOrganizationId] = useState(''); // Состояние для хранения введенного ID
    const [organization, setOrganization] = useState(null); // Состояние для хранения данных об организации
    const [loading, setLoading] = useState(false); // Состояние для отображения загрузки
    const [error, setError] = useState(null); // Состояние для отображения ошибки

    const fetchOrganization = async () => {
        if (!organizationId) {
            setError('Пожалуйста, введите ID организации');
            return;
        }

        try {
            setLoading(true);
            setError(null); // Сброс ошибок перед новым запросом
            console.log("запрашиваем организацию с id: " + organizationId);
            const response = await getOrganizationById(organizationId);
            console.log("статус получения организации: " + response.status);
            if (response.status!==200) {
                throw new Error(`Ошибка: ${response.status}`);
            }
            const data = await response.data;
            setOrganization(data);
        } catch (error) {
            setError(error.message);
            setOrganization(null);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h1>Поиск Организации</h1>

            <div>
                <label htmlFor="orgIdInput">Введите ID организации:</label>
                <input
                    type="text"
                    id="orgIdInput"
                    value={organizationId}
                    onChange={(e) => setOrganizationId(e.target.value)}
                    placeholder="ID организации"
                />
                <button onClick={fetchOrganization}>Загрузить организацию</button>
            </div>

            {loading && <p>Загрузка...</p>}
            {error && <p style={{ color: 'red' }}>Ошибка: {error}</p>}

            {organization && (
                <div>
                    <h2>Организация: {organization.name}</h2>
                    <p><strong>ID:</strong> {organization.id}</p>
                    <p><strong>Полное название:</strong> {organization.fullName}</p>
                    <p><strong>Дата создания:</strong> {organization.creationDate}</p>
                    <p><strong>Годовой оборот:</strong> {organization.annualTurnover}</p>
                    <p><strong>Количество сотрудников:</strong> {organization.employeesCount}</p>
                    <p><strong>Тип:</strong> {organization.type}</p>
                    <p><strong>Координаты:</strong> X: {organization.coordinates.x}, Y: {organization.coordinates.y}</p>
                    <p><strong>Адрес:</strong> {organization.officialAddress ? organization.officialAddress.zipCode : 'Не указан'}</p>
                </div>
            )}
        </div>
    );
};

export default OrganizationPage;
