import React, { useState, useEffect } from 'react';

import Container from './Container';
import CardList from './CardList';

import '../../styles/UserDashboard.css';

const UserDashboard = () => {

    const triggerText = 'Create Application';

    const [isSubmitted, setSubmitted] = useState(false);
    const [jobID, setJobId] = useState('');
    const [jobTitle, setJobTitle] = useState('');
    const [company, setCompany] = useState('');
    const [jobDescription, setJobDescription] = useState('');
    const [haveApplied, setHaveApplied] = useState();

    const onSubmit = ((event) => {      
        event.preventDefault(event);

        setJobId(event.target.jobId.value);
        
        setJobTitle(event.target.jobTitle.value);

        setCompany(event.target.company.value);

        setJobDescription(event.target.jobDescription.value);
        
        setHaveApplied(event.target.haveApplied.value);

        setSubmitted(true);
    });


    useEffect(() => {
        if(isSubmitted) {
           fetch(`${process.env.REACT_APP_API_URL}/jobapp/createJob`, {
                method: 'POST',
                headers: {
                    'Content-Type' : 'application/json',
                    // 'Origin' : 'http://localhost:3000',
                },
                body: JSON.stringify({jobID: jobID, jobTitle: jobTitle , company: company, jobDescription : jobDescription , haveApplied : haveApplied})
            })
            .then(resp => resp.json)
            .catch(err => console.log(err))
        }
    }, [isSubmitted]);


    return (
        <div className="UserDashboard">
            <Container triggerText={triggerText} onSubmit={onSubmit} />
            <CardList/>
        </div>
    );
};

export default UserDashboard;