import React from "react";
import "./Text.css";

export default (props) => {
    var classname = props.className || 'text';
    var iconBefore = props.iconBefore;

    return (
        <div className={classname}>{iconBefore}{props.text}</div>
    );
};

