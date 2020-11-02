import React from "react";

import Board from "./Board";

import { Provider } from "react-redux";
import store from "../store";

import "../../styles/TaskBoard.css";

const TaskBoard = () => {
    return (
        <Provider store={store}>
            <Board />
        </Provider>
    );
    
}

export default TaskBoard;