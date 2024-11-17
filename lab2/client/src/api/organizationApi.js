import axios from 'axios';

const apiClient = axios.create({
    baseURL: '/organization-1.0.6-SNAPSHOT/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

export const getOrganizations = async (filters) => {
    const params = {};
    if (filters.creationDate) params.creationDate = filters.creationDate;
    if (filters.annualTurnover) params.annualTurnover = filters.annualTurnover;
    if (filters.sort) params.sort = filters.sort;

    return await apiClient.get("/organizations", { params });
};

// Получение организации по ID
export const getOrganizationById = (id) => apiClient.get(`/organizations/${id}`);

// Создание новой организации
export const createOrganization = (data) => apiClient.post('/organizations', data);

// Обновление организации по ID
export const refreshOrganization = (id, data) => apiClient.put(`/organizations/${id}`, data);

// Удаление организации
export const removeOrganization = (id) => apiClient.delete(`/organizations/${id}`);

// Прочие API методы, такие как группировка и фильтрация
export const groupByOfficialAddress = () => apiClient.get('/organizations/group-by-address');
export const searchByFullName = (substring) => apiClient.get('/organizations/search-by-fullname', { params: { substring } });
