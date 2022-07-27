import React from "react";
import Content from "../components/layout/Content";
import SessionTimeout from "../components/layout/SessionTimeout";

const App = props => {
    return (
        <div className="App">
            <Content />
            <SessionTimeout/>
        </div>
    )
}

export default App;