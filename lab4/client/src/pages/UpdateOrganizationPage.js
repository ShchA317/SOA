import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { getOrganizationById, refreshOrganization } from "../api/organizationApi";

// Определяем схему валидации с помощью Yup
const schema = yup.object().shape({
    name: yup.string().required("Название организации обязательно"),
    coordinates: yup.object({
        x: yup.number().max(76, "X не может быть больше 76").required("Координата X обязательна"),
        y: yup.number().min(-155, "Y не может быть меньше -155").required("Координата Y обязательна"),
    }),
    creationDate: yup.date().required("Дата создания обязательна"),
    annualTurnover: yup.number().positive().required("Годовой оборот обязателен"),
    fullName: yup.string().max(1678, "Полное название не может превышать 1678 символов").required("Полное название обязательно"),
    employeesCount: yup.number().positive().integer().required("Количество сотрудников обязательно"),
    orgType: yup
        .string()
        .oneOf(["PUBLIC", "TRUST", "OPEN_JOINT_STOCK_COMPANY"])
        .required("Тип организации обязателен"),
    officialAddress: yup.object({
        zipCode: yup.string().nullable(),
    }),
});

const UpdateOrganizationPage = () => {
    const [organizationId, setOrganizationId] = useState("");
    const [organization, setOrganization] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const {
        register,
        handleSubmit,
        setValue,
        formState: { errors },
    } = useForm({
        resolver: yupResolver(schema),
    });

    // Загрузка данных об организации
    const fetchOrganization = async () => {
        if (!organizationId) {
            setError("Пожалуйста, введите ID организации");
            setOrganization(null); // Сбрасываем прошлые данные, если введен пустой ID
            return;
        }

        try {
            setLoading(true);
            setError(null);
            const response = await getOrganizationById(organizationId);

            if (response.status !== 200) {
                throw new Error(response.status === 404 ? "Организация не найдена" : `Ошибка: ${response.status}`);
            }

            const data = response.data;
            setOrganization(data);

            // Устанавливаем значения в форму
            Object.keys(data).forEach((key) => setValue(key, data[key]));

            setSuccess(null);
        } catch (err) {
            setError(err.message);
            setOrganization(null); // Удаляем прошлые данные при ошибке
        } finally {
            setLoading(false);
        }
    };

    // Обновление данных
    const onSubmit = async (data) => {
        try {
            setLoading(true);
            setError(null);
            const result = await refreshOrganization(organizationId, data);

            if (result.status !== 200) {
                throw new Error(`Ошибка: ${result.status}`);
            }

            setSuccess("Организация успешно обновлена");
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
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
            {error && <p style={{ color: "red" }}>Ошибка: {error}</p>}
            {success && <p style={{ color: "green" }}>{success}</p>}

            {/* Условный рендеринг формы */}
            {organization && (
                <form onSubmit={handleSubmit(onSubmit)}>
                    <h2>Редактирование данных организации</h2>

                    <div>
                        <label>Название организации:</label>
                        <input type="text" {...register("name")} />
                        <p>{errors.name?.message}</p>
                    </div>

                    <div>
                        <label>Координата X:</label>
                        <input type="number" {...register("coordinates.x")} />
                        <p>{errors.coordinates?.x?.message}</p>
                    </div>

                    <div>
                        <label>Координата Y:</label>
                        <input type="number" {...register("coordinates.y")} />
                        <p>{errors.coordinates?.y?.message}</p>
                    </div>

                    <div>
                        <label>Дата создания:</label>
                        <input type="date" {...register("creationDate")} />
                        <p>{errors.creationDate?.message}</p>
                    </div>

                    <div>
                        <label>Годовой оборот:</label>
                        <input type="number" {...register("annualTurnover")} />
                        <p>{errors.annualTurnover?.message}</p>
                    </div>

                    <div>
                        <label>Полное название:</label>
                        <input type="text" {...register("fullName")} />
                        <p>{errors.fullName?.message}</p>
                    </div>

                    <div>
                        <label>Количество сотрудников:</label>
                        <input type="number" {...register("employeesCount")} />
                        <p>{errors.employeesCount?.message}</p>
                    </div>

                    <div>
                        <label>Тип организации:</label>
                        <select {...register("orgType")}>
                            <option value="">Выберите тип</option>
                            <option value="PUBLIC">PUBLIC</option>
                            <option value="TRUST">TRUST</option>
                            <option value="OPEN_JOINT_STOCK_COMPANY">OPEN_JOINT_STOCK_COMPANY</option>
                        </select>
                        <p>{errors.orgType?.message}</p>
                    </div>

                    <div>
                        <label>Почтовый индекс:</label>
                        <input type="text" {...register("officialAddress.zipCode")} />
                        <p>{errors.officialAddress?.zipCode?.message}</p>
                    </div>

                    <button type="submit" disabled={loading}>
                        Обновить данные
                    </button>
                </form>
            )}
        </div>
    );
};

export default UpdateOrganizationPage;
