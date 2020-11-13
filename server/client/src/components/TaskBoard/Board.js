import "../../styles/Board.css";

import React, { useState , useEffect } from "react";
import Activities from "./Activities";

const Board = () => {
    // fetch here
   const [response, setResponse] = useState();

   useEffect(() => {
       fetch("http://localhost:8080/api/home", {
           method: 'GET',
           headers: {
               'Content-Type' : 'application/json',
               'Authorization' : 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJudSIsImV4cCI6MTYwNDk3MTM1MSwiaWF0IjoxNjA0MzY2NTUxfQ.IjpsOacMSc_6FKOsJhPEcY64H_RfmppoEn7UK4fmPXGQcthA5I4LqFUr4tXBqBxkdNf8B1xMezV-sQMT_c0n7A',
           }
       })
       .then(resp => resp.text())
       .then(text => setResponse(text))
       .then(text => console.log(text))
       .catch(err => console.log(err))
   }, []) 

  


    return (
      <div className="Board">
        <div className="Header"> {response}</div>
        <Activities />
      </div>
    );
}

export default Board;