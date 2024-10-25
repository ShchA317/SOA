import React from 'react';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import { createOrganization } from '../api/organizationApi';

const OrganizationSchema = Yup.object().shape({
    name: Yup.string().required('Required'),
    fullName: Yup.string().required('Required'),
    annualTurnover: Yup.number().min(1).required('Required'),
    employeesCount: Yup.number().min(1).required('Required'),
});

const OrganizationForm = () => {
    const handleSubmit = async (values) => {
        try {
            await createOrganization(values);
            alert('Organization created successfully');
        } catch (error) {
            alert('Error creating organization');
        }
    };

    return (
        <Formik
            initialValues={{
                name: '',
                fullName: '',
                annualTurnover: 0,
                employeesCount: 0,
            }}
            validationSchema={OrganizationSchema}
            onSubmit={handleSubmit}
        >
            {({ errors, touched }) => (
                <Form>
                    <div>
                        <label>Name</label>
                        <Field name="name" />
                        {errors.name && touched.name ? <div>{errors.name}</div> : null}
                    </div>

                    <div>
                        <label>Full Name</label>
                        <Field name="fullName" />
                        {errors.fullName && touched.fullName ? <div>{errors.fullName}</div> : null}
                    </div>

                    <div>
                        <label>Annual Turnover</label>
                        <Field name="annualTurnover" type="number" />
                        {errors.annualTurnover && touched.annualTurnover ? <div>{errors.annualTurnover}</div> : null}
                    </div>

                    <div>
                        <label>Employees Count</label>
                        <Field name="employeesCount" type="number" />
                        {errors.employeesCount && touched.employeesCount ? <div>{errors.employeesCount}</div> : null}
                    </div>

                    <button type="submit">Submit</button>
                </Form>
            )}
        </Formik>
    );
};

export default OrganizationForm;
