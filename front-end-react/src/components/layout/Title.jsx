import React from "react";
import "./Title.css";

export default (props) => {
    var classname = props.className || 'title';

    return (
        <div className={classname}>{props.title}{props.iconBefore}</div>
    );
};
