import "../../styles/Board.css";

import React, { Component } from "react";
import Activities from "./Activities";

class Board extends Component {
  render() {
    return (
      <div className="Board">
        {/* <div className="Header">Task Board</div> */}
        <Activities />
      </div>
    );
  }
}

export default Board;