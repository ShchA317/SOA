import React, { useState } from 'react';
import {getOrganizationById, refreshOrganization} from "../api/organizationApi";

// Компонент для обновления данных об организации
const UpdateOrganizationPage = () => {
    const [organizationId, setOrganizationId] = useState(''); // ID организации
    const [organization, setOrganization] = useState(null); // Данные организации
    const [loading, setLoading] = useState(false); // Состояние загрузки
    const [error, setError] = useState(null); // Ошибки
    const [success, setSuccess] = useState(null); // Успех

    // Функция для загрузки данных об организации
    const fetchOrganization = async () => {
        if (!organizationId) {
            setError('Пожалуйста, введите ID организации');
            return;
        }

        try {
            setLoading(true);
            setError(null);
            const response = await getOrganizationById(organizationId);
            if (response.status!==200) {
                if (response.status === 404) {
                    throw new Error('Организация не найдена');
                } else {
                    throw new Error(`Ошибка: ${response.status}`);
                }
            }
            const data = await response.data;
            setOrganization(data);
            setSuccess(null); // Очистка сообщения об успехе
        } catch (error) {
            setError(error.message);
            setOrganization(null);
        } finally {
            setLoading(false);
        }
    };

    // Функция для отправки обновленных данных
    const updateOrganization = async () => {
        if (!organization) {
            setError('Данные об организации не загружены');
            return;
        }

        try {
            setLoading(true);
            setError(null);
            const result = await refreshOrganization(organizationId, organization);

            if (result.status !== 200) {
                throw new Error(`Ошибка: ${result.status}`);
            }

            const updatedData = await result.data;
            setOrganization(updatedData);
            setSuccess('Организация успешно обновлена');
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    // Обновление полей в объекте организации
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setOrganization({ ...organization, [name]: value });
    };

    return (
        <div>
            <h1>Обновление Организации</h1>

            <div>
                <label htmlFor="orgIdInput">Введите ID организации:</label>
                <input
                    type="text"
                    id="orgIdInput"
                    value={organizationId}
                    onChange={(e) => setOrganizationId(e.target.value)}
                    placeholder="ID организации"
                />
                <button onClick={fetchOrganization} disabled={loading}>
                    Загрузить организацию
                </button>
            </div>

            {loading && <p>Загрузка...</p>}
            {error && <p style={{ color: 'red' }}>Ошибка: {error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}

            {organization && (
                <form
                    onSubmit={(e) => {
                        e.preventDefault();
                        updateOrganization();
                    }}
                >
                    <h2>Редактирование данных организации</h2>

                    <label>
                        Название:
                        <input
                            type="text"
                            name="name"
                            value={organization.name || ''}
                            onChange={handleInputChange}
                        />
                    </label>
                    <br />

                    <label>
                        Полное название:
                        <input
                            type="text"
                            name="fullName"
                            value={organization.fullName || ''}
                            onChange={handleInputChange}
                        />
                    </label>
                    <br />

                    <label>
                        Годовой оборот:
                        <input
                            type="number"
                            name="annualTurnover"
                            value={organization.annualTurnover || ''}
                            onChange={handleInputChange}
                        />
                    </label>
                    <br />

                    <label>
                        Количество сотрудников:
                        <input
                            type="number"
                            name="employeesCount"
                            value={organization.employeesCount || ''}
                            onChange={handleInputChange}
                        />
                    </label>
                    <br />

                    <label>
                        Тип:
                        <select
                            name="type"
                            value={organization.type || ''}
                            onChange={handleInputChange}
                        >
                            <option value="PUBLIC">PUBLIC</option>
                            <option value="TRUST">TRUST</option>
                            <option value="OPEN_JOINT_STOCK_COMPANY">OPEN_JOINT_STOCK_COMPANY</option>
                        </select>
                    </label>
                    <br />

                    <label>
                        Координаты X:
                        <input
                            type="number"
                            name="coordinates.x"
                            value={organization.coordinates?.x || ''}
                            onChange={(e) =>
                                setOrganization({
                                    ...organization,
                                    coordinates: {
                                        ...organization.coordinates,
                                        x: Number(e.target.value),
                                    },
                                })
                            }
                        />
                    </label>
                    <br />

                    <label>
                        Координаты Y:
                        <input
                            type="number"
                            name="coordinates.y"
                            value={organization.coordinates?.y || ''}
                            onChange={(e) =>
                                setOrganization({
                                    ...organization,
                                    coordinates: {
                                        ...organization.coordinates,
                                        y: Number(e.target.value),
                                    },
                                })
                            }
                        />
                    </label>
                    <br />

                    <label>
                        Почтовый индекс:
                        <input
                            type="text"
                            name="officialAddress.zipCode"
                            value={organization.officialAddress?.zipCode || ''}
                            onChange={(e) =>
                                setOrganization({
                                    ...organization,
                                    officialAddress: {
                                        ...organization.officialAddress,
                                        zipCode: e.target.value,
                                    },
                                })
                            }
                        />
                    </label>
                    <br />

                    <button type="submit" disabled={loading}>
                        Обновить данные
                    </button>
                </form>
            )}
        </div>
    );
};

export default UpdateOrganizationPage;
