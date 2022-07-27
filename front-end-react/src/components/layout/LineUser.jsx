import React from "react";
import { useNavigate } from "react-router-dom";
import Text from "./Text";
import "./LineUser.css";
import * as FaIcons from 'react-icons/fa';
import * as AiIcons from 'react-icons/ai';

export default (props) => {
    var classname = props.className || 'lineUser';
    const navigate = useNavigate();
    var name = props.firstName + " " + props.lastName;
    var nameMoreUsername = props.firstName + " " + props.lastName +" | "+props.username;
    var finalName=(props.page==="conversation")?name:nameMoreUsername;
    var page = props.page;
    var username = props.username;
    var token = sessionStorage.getItem('token');

    function chooseOption(event) {
        if(page === 'conversation'){
            navigate('/conversation/'+ props.idUser+"/"+name);
        }else{
            navigate('/userProfile/' + props.idUser); 
        }
    }

    return (
        <div>
            {page === 'conversation' || page === 'availability'?
                <div>
                    <li className={classname} onClick={(event) => { chooseOption(event) }}>
                        <div className="img-user">
                            <img className='picture-user' src={props.pictureUrl} />
                        </div>
                        <div className="info-user">
                            <Text className="name-user" text={finalName}/>
                        </div>
                    </li>
                </div>
                : 
                
                <div className={classname + 'DIV'}>
                    <li className={classname} id={props.idUser} >
                        <div className="img-user">
                            <img className='picture-user-privacy' id={props.idUser + "/" + name} src={props.pictureUrl} />
                        </div>
                        <div className="info-user-privacy">
                            <Text className="name-user-privacy" id={props.idUser + "/" + name} text={name + ' | '
                                + username} />
                        </div>
                        {props.button === '+' ?
                            <button className="buttonUserPrivacyAdd" onClick={  props.action }>+</button> : <button className="buttonUserPrivacyRemove" onClick={ props.action }>  <FaIcons.FaTrashAlt  onClick={e=> e.stopPropagation() }/>  </button>}
                    </li>
                </div>}
        </div>
    )
}