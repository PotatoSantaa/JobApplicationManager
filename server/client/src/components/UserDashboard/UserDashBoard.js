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
           fetch("http://localhost:8080/jobapp/createJob", {
                method: 'POST',
                headers: {
                    'Content-Type' : 'application/json',
                    'Origin' : 'http://localhost:3000',
                    'Authorization' : 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJudSIsImV4cCI6MTYwNTkxNTE3NSwiaWF0IjoxNjA1MzEwMzc1fQ.RhgI40rc1CeHkL5i2HwiFvntJKeD4CmoNULYZu1HkNuJ6fS2UHOU_jLXk3Ui75yrsdPoOolwYm_KxStXhVVglg',
                },
                mode: 'cors',
                body: JSON.stringify({jobID, jobTitle, company, jobDescription, haveApplied})
            })
            .then(resp => {
                resp.text();
                return;
            })
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