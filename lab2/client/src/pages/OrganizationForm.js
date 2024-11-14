import React from "react";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import {createOrganization} from "../api/organizationApi";

const schema = yup.object().shape({
    id: yup.number().positive().integer().required("ID обязателен"),
    name: yup.string().required("Название организации обязательно"),
    coordinates: yup.object({
        x: yup.number().max(76, "X не может быть больше 76").required("Координата X обязательна"),
        y: yup.number().min(-155, "Y не может быть меньше -155").required("Координата Y обязательна")
    }),
    creationDate: yup.date().required("Дата создания обязательна"),
    annualTurnover: yup.number().positive().required("Годовой оборот обязателен"),
    fullName: yup.string().max(1678, "Полное название не может превышать 1678 символов").required("Полное название обязательно"),
    employeesCount: yup.number().positive().integer().required("Количество сотрудников обязательно"),
    type: yup.string().oneOf(["PUBLIC", "TRUST", "OPEN_JOINT_STOCK_COMPANY"]).required("Тип организации обязателен"),
    officialAddress: yup.object({
        zipCode: yup.string().nullable()
    })
});

const OrganizationForm = ( ) => {
    const { register, handleSubmit, formState: { errors } } = useForm({
        resolver: yupResolver(schema)
    });

    const onSubmit = async (data) => {
        try {
            await createOrganization(data);
            alert("Организация успешно добавлена");
        } catch (error) {
            console.error("Ошибка при добавлении организации:", error);
            alert("Не удалось добавить организацию");
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <div>
                <label>ID:</label>
                <input type="number" {...register("id")} />
                <p>{errors.id?.message}</p>
            </div>

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
                <select {...register("type")}>
                    <option value="">Выберите тип</option>
                    <option value="PUBLIC">PUBLIC</option>
                    <option value="TRUST">TRUST</option>
                    <option value="OPEN_JOINT_STOCK_COMPANY">OPEN_JOINT_STOCK_COMPANY</option>
                </select>
                <p>{errors.type?.message}</p>
            </div>

            <div>
                <label>Почтовый индекс:</label>
                <input type="text" {...register("officialAddress.zipCode")} />
                <p>{errors.officialAddress?.zipCode?.message}</p>
            </div>

            <button type="submit">Добавить организацию</button>
        </form>
    );
};

export default OrganizationForm;
