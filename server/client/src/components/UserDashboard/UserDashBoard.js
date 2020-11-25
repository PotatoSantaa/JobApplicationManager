import React, { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../Auth/Auth'
import Container from './Container';
import CardList from './CardList';

import '../../styles/UserDashboard.css';

const UserDashboard = () => {
    const { user } = useContext(AuthContext);    
    
    const triggerText = 'Create Application';

    const [isSubmitted, setSubmitted] = useState(false);
    const [isClicked, setClicked] = useState(false);
    const [jobID, setJobId] = useState('');
    const [jobTitle, setJobTitle] = useState('');
    const [company, setCompany] = useState('');
    const [jobDescription, setJobDescription] = useState('');
    const [haveApplied, setHaveApplied] = useState();
    const [updateCardList, setUpdateCardList] = useState(false);

    const onSubmit = ((event) => {      
        event.preventDefault(event);

        setJobId(event.target.jobId.value);
        
        setJobTitle(event.target.jobTitle.value);

        setCompany(event.target.company.value);

        setJobDescription(event.target.jobDescription.value);
        
        setHaveApplied(event.target.haveApplied.value);

        setSubmitted(true);
    });

    const onClick = ((event) => {
        setClicked(true);
    })

    useEffect(() => {
        if(user && isSubmitted) {
           createJob(user.uid)
        }
        // eslint-disable-next-line
    }, [isSubmitted]);

    const createJob = (userID) => {
        fetch(`/jobapp/createJob/${userID}`, {
            method: 'POST',
            headers: {
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify({jobID: jobID, jobTitle: jobTitle , company: company, jobDescription : jobDescription , haveApplied : haveApplied})
        })
        .then(resp => {
            resp.json();
            setUpdateCardList(true);
        })
        .catch(err => console.log(err))
    }

    return (
        <div className="UserDashboard">
            {isClicked ? null :<Container triggerText={triggerText} onSubmit={onSubmit} />}      
            <CardList 
                onClick={onClick}
                updateCardList
                setUpdateCardList
            />
        </div>
    );
};

export default UserDashboard;