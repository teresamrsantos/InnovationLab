import React from "react";
import { useNavigate } from "react-router-dom";
import Text from "../../components/layout/Text";
import "./LineConversationUser.css";

export default (props) => {
    var classname = props.className || 'lineConversationUser';
    const navigate = useNavigate();
    var timestamp = props.dateLastMessage;
    const date = new Date(timestamp*1);

    function seeConversation(event) {
        navigate('/conversation/'+ props.idUser+"/"+props.name) 
    }

    return (
        <li key= {props.idUser} className={classname} id={props.idUser+"/"+props.name} onClick={(event) => {  seeConversation(event) }}>
            <div className="img-conversation">
                <img className='picture-userConversation' src={props.photo} />
            </div>
            <div className="info-conversation">
                <Text className="name-userConversation" text={props.name}/>
                {props.messageToRead > 0 ? 
                <div className='divCircle-Conversation'>
                    <p className="circleText-Conversation">{props.messageToRead}</p> 
                </div> : ''} 
            </div>
            <div className="date-conversation">
                <Text className="titleDate-userConversation" text={"Last message on: "} />&nbsp;&nbsp;
                <Text className="dateLastMessage-userConversation" text={date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getFullYear()
                +" "+date.getHours()+":"+date.getMinutes()}/>
            </div>
        </li>
    );
};