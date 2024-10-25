import React, { useEffect, useState } from 'react';
import { getOrganizationById } from '../api/organizationApi';
import { useParams } from 'react-router-dom';

const OrganizationDetails = () => {
    const { id } = useParams();
    const [organization, setOrganization] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchOrganization = async () => {
            try {
                const response = await getOrganizationById(id);
                setOrganization(response.data);
                setLoading(false);
            } catch (err) {
                setError(err);
                setLoading(false);
            }
        };

        fetchOrganization();
    }, [id]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error loading organization details</p>;

    return (
        <div>
            <h1>{organization.name}</h1>
            <p>Full Name: {organization.fullName}</p>
            <p>Annual Turnover: {organization.annualTurnover}</p>
            <p>Employees Count: {organization.employeesCount}</p>
        </div>
    );
};

export default OrganizationDetails;
