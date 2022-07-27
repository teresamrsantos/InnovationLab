import React from "react";
import Text from "./Text";
import "./Message.css";

export default (props) => {
var user, text, date, general;
var timestamp = props.messageTime;
const messageTime = new Date(timestamp*1);

if(props.idUser===props.idUserOn){
    user="sent-user";
    text="sent-text" ;
    date="sent-date";
    general="sent-message";
}else{
    user="received-user";
    text="received-text" ;
    date="received-date";
    general="received-message";
}

    return (
        <div className={general}>
            <Text className={user} text={props.name} />
            <Text className={text} text={props.message} />
            <Text className={date} text={messageTime.getDate()+"/"+(messageTime.getMonth()+1)+"/"+messageTime.getFullYear()+
            " "+messageTime.getHours()+":"+messageTime.getMinutes()} />
        </div>
    );
};