import React from "react";
import "./Button.css";

export default (props) => {

    var classname = props.className || 'button';
    var disabled = props.disabled || false;
    var onclick = props.onclick;
    var buttonText = props.text;
    var iconButton = props.icon;
    var iconButtonBefore = props.iconBefore;
    var tooltipText = props.tooltipText;

    return (
        <button className={classname}  title={tooltipText} disabled={disabled} id={props.id} type= {props.type} onClick={onclick}>{iconButtonBefore}{buttonText}{iconButton} </button>
    );
};
