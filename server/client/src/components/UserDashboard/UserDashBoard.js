import React, { useState, useEffect } from 'react';

import Container from './Container';

import '../../styles/UserDashboard.css';

const UserDashboard = () => {

    const triggerText = 'Create Application';

    const [isSubmitted, setSubmitted] = useState(false);
    const [jobId, setJobId] = useState('');
    const [jobTitle, setJobTitle] = useState('');
    const [company, setCompany] = useState('');
    const [jobDescription, setJobDescription] = useState('');
    const [haveApplied, setHaveApplied] = useState();

    // Do POST create job here
    const onSubmit = ((event) => {      
        event.preventDefault(event);

        setJobId(event.target.jobId.value);
        console.log(event.target.jobId.value);
        console.log(jobId);
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
                    'Authorization' : 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJudSIsImV4cCI6MTYwNDk3MTM1MSwiaWF0IjoxNjA0MzY2NTUxfQ.IjpsOacMSc_6FKOsJhPEcY64H_RfmppoEn7UK4fmPXGQcthA5I4LqFUr4tXBqBxkdNf8B1xMezV-sQMT_c0n7A',
                },
                mode: 'cors',
                body: JSON.stringify({jobId, jobTitle, company, jobDescription, haveApplied})
            })
            .then(resp => resp.text())
            .catch(err => console.log(err))
        }
    }, [isSubmitted]);


    return (
        <div className="UserDashboard">
        <Container triggerText={triggerText} onSubmit={onSubmit} />
        </div>
    );
};

export default UserDashboard;