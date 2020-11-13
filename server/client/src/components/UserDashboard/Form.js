import React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';


export const Form = ({ onSubmit }) => {

  return (
    <form onSubmit={onSubmit} >
        <TextField label="Job ID" id="jobId" />
        <TextField label="Job Title" id="jobTitle" />
        <TextField label="Company" id="company" />
        <TextField label="Job Description" id="jobDescription" />
        <TextField label="Status" id="haveApplied" />
        <Button variant="contained" color="primary" type="submit" style={{marginBlock : '10em'}}>
          Submit
        </Button>
    </form>
  );
};
export default Form;