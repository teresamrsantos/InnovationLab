import React from "react";
import "./Alert.css";

export default (props) => {
    var classname = props.className;
    var text = props.text || '';

    return (
        <div class={classname}>
            <span class="closebtn" onClick={onclick}></span>
            {text}
        </div>
    );
};

