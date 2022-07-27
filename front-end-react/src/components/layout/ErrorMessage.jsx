import React from "react";
import "./ErrorMessage.css";


export default (props) => {

    var classname = props.className || 'error';
    var text = props.text || ''; 
    return (
        <div className={classname}>{props.text }</div>
    );
};

