import axios from 'axios';
import * as https from "https";

const httpsAgent = new https.Agent({
    rejectUnauthorized: false,
});

const organizationApiClient = axios.create({
    baseURL: 'http://localhost:8080/organization-1.0.9-SNAPSHOT/api',
    headers: {
        'Content-Type': 'application/json',
    }, httpsAgent
});

const orgManagerApiClient = axios.create({
    baseURL: 'https://localhost:28791',
    headers: {
        'Content-Type': 'application/json',
    },
    httpsAgent,
});

export const getOrganizations = async (filters) => {
    const params = {};
    if (filters.creationDate) params.creationDate = filters.creationDate;
    if (filters.annualTurnover) params.annualTurnover = filters.annualTurnover;
    if (filters.sort) params.sort = filters.sort;

    return await organizationApiClient.get("/organizations", { params });
};

export const getOrganizationById = (id) => organizationApiClient.get(`/organizations/${id}`);

export const createOrganization = (data) => organizationApiClient.post('/organizations', data);

export const refreshOrganization = (id, data) => organizationApiClient.put(`/organizations/${id}`, data);

export const removeOrganization = (id) => organizationApiClient.delete(`/organizations/${id}`);

export const countByEmployeesCount = async (count) => {
    try {
        const response = await organizationApiClient.get("/organizations/count-by-employees", {
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
        const response = await organizationApiClient.get("/organizations/search-by-fullname", {
            params: { substring },
        });
        return response.data;
    } catch (error) {
        console.error("Ошибка при поиске организаций:", error);
        throw error;
    }
};

export const groupByOfficialAddress = () => organizationApiClient.get('/organizations/group-by-address');

export const mergeOrganizations = (id1, id2, newName, newAddress) => orgManagerApiClient.post(`/orgmanager/merge/${id1}/${id2}/${encodeURIComponent(newName)}/${encodeURIComponent(newAddress)}`)

export const hireEmployOrganizations = (organizationId, data) => orgManagerApiClient.post(`/orgmanager/hire/${organizationId}`, data)
