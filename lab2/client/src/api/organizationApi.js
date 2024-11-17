import axios from 'axios';

const apiClient = axios.create({
    baseURL: '/organization-1.0.7-SNAPSHOT/api',
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

export const getOrganizationById = (id) => apiClient.get(`/organizations/${id}`);

export const createOrganization = (data) => apiClient.post('/organizations', data);

export const refreshOrganization = (id, data) => apiClient.put(`/organizations/${id}`, data);

export const removeOrganization = (id) => apiClient.delete(`/organizations/${id}`);

export const countByEmployeesCount = async (count) => {
    try {
        const response = await apiClient.get("/organizations/count-by-employees", {
            params: { count },
        });
        return response.data;
    } catch (error) {
        console.error("Ошибка при получении количества организаций:", error);
        throw error;
    }
};

export const searchByFullName = async (substring) => {
    try {
        const response = await apiClient.get("/organizations/search-by-fullname", {
            params: { substring },
        });
        return response.data;
    } catch (error) {
        console.error("Ошибка при поиске организаций:", error);
        throw error;
    }
};

export const groupByOfficialAddress = () => apiClient.get('/organizations/group-by-address');
