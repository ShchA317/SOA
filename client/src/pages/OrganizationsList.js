import React, { useEffect, useState } from 'react';
import { getOrganizations } from '../api/organizationApi';
import { Link } from 'react-router-dom';

const OrganizationsList = () => {
    const [organizations, setOrganizations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchOrganizations = async () => {
            try {
                const response = await getOrganizations();
                setOrganizations(response.data);
                setLoading(false);
            } catch (err) {
                setError(err);
                setLoading(false);
            }
        };

        fetchOrganizations();
    }, []);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error loading organizations</p>;

    return (
        <div>
            <h1>Organizations List</h1>
            <ul>
                {organizations.map(org => (
                    <li key={org.id}>
                        <Link to={`/organizations/${org.id}`}>{org.name}</Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default OrganizationsList;
