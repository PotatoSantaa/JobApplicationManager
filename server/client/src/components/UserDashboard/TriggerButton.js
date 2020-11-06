import React from 'react';

import Button from '@material-ui/core/Button';

const TriggerButton = ({ triggerText, buttonRef, showModal }) => {
    
  return (
    <Button
      variant="contained" color="primary"
      ref={buttonRef}
      onClick={showModal}
    >
      {triggerText}
    </Button>
  );
};
export default TriggerButton;