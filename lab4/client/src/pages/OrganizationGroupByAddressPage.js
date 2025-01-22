import React, { useState, useEffect } from "react";
import { groupByOfficialAddress } from "../api/organizationApi";

const OrganizationGroupByAddressPage = () => {
    const [groups, setGroups] = useState([]); // Состояние для хранения групп
    const [loading, setLoading] = useState(false); // Состояние для отображения загрузки
    const [error, setError] = useState(null); // Состояние для отображения ошибки

    useEffect(() => {
        const fetchGroups = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await groupByOfficialAddress();
                if (response.status !== 200) {
                    throw new Error("Ошибка при загрузке групп");
                }
                if (response.length === 0){

                }
                console.log("data:")
                console.log(response.data)
                setGroups(response.data);
                console.log("groups:")
                console.log(groups)
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchGroups();
    }, []);


    return (
        <div>
            <h1>Группировка организаций по адресу</h1>

            {loading && <p>Загрузка...</p>}
            {error && <p style={{ color: "red" }}>Ошибка: {error}</p>}

            {!loading && !error && groups.length === 0 && <p>Данные отсутствуют</p>}

            {!loading && groups.length > 0 && (
                <table border="1">
                    <thead>
                    <tr>
                        <th>Адрес</th>
                        <th>Количество организаций</th>
                    </tr>
                    </thead>
                    <tbody>
                    {groups.map((group, index) => (
                        <tr key={index}>
                            <td>{group.officialAddress ? group.officialAddress.zipCode : "Не указан"}</td>
                            <td>{group.count}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default OrganizationGroupByAddressPage;
