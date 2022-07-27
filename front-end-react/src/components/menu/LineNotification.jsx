import React, { useState } from "react";
import { Link } from "react-router-dom";
import './LineNotifications.css'
import moment from 'moment';
import * as BSIcons from 'react-icons/bs'
import { deleteNotificationAPI, markNotificationasReadAPI } from '../../restApi';
import { MdOutlineMarkEmailUnread } from "react-icons/md";


export default (props) => {
    var token = sessionStorage.getItem('token');
    var idUser = props.idUser;
    var description = props.description;
    var date = new Date(props.date);
    var [read, setRead] = useState(props.read);
    var idProject = props.idProject;
    var idUserThatNotificationIsAbout = props.idUserThatNotificationIsAbout;
    var path = "/myprojects" 


    function markNotificationasRead() {
        markNotificationasReadAPI(props.idNotif, token, (e) => { setRead(!read); })
    }


    function deleteNotification() {
            deleteNotificationAPI(props.idNotif, token, (e) => { document.getElementById(props.idNotif + '/notif').style.display = 'none'; });
    }
/*   {moment.utc(date.getFullYear() + '-' 
            + (date.getMonth() ) + "-" + date.getDate() +
                " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()).local().startOf('seconds').fromNow()}*/
    return (
        <li key={props.date} id={props.idNotif + '/notif'} className={read ? 'readNotification' : 'unreadNotification'}>

            <Link className='descriptionLinkNotif ' rel="stylesheet" to={path}  onClick={markNotificationasRead}>{description} </Link>
            <p className="dateNotification">
 
                {moment([date.getFullYear(), date.getMonth() , date.getDate(), date.getHours(), date.getMinutes(),date.getSeconds()]).startOf('seconds').fromNow()}
                {" "+date.getFullYear() + '-' 
            + (date.getMonth() ) + "-" + date.getDate() +
                " " + date.getHours() + ":" + date.getMinutes() }

             </p>
            <div className="buttonsNotification">
                <button className='bntTrashNotifications' onClick={deleteNotification}> <BSIcons.BsTrash onClick={e => e.stopPropagation()}></BSIcons.BsTrash> </button>
                {read ?
                    <button className='bntNotificationRead' onClick={markNotificationasRead}><BSIcons.BsEnvelopeOpen/></button> : <button className='bntNotificationUnread ' onClick={markNotificationasRead}><MdOutlineMarkEmailUnread /></button>}</div>
        </li>
    )

}