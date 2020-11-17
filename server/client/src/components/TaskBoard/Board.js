import "../../styles/Board.css";

import React, { Component } from "react";
import Activities from "./Activities";

class Board extends Component {
  render() {
    return (
      <div className="Board">
        <Activities />
      </div>
    );
  }
}

export default Board;