import React, { useEffect, useState } from "react";
import { getAllallUserNotificationsAPI, getUserInfoAPI } from '../../restApi'
import LineNotification from "./LineNotification";
import { useNavigate } from "react-router-dom";

import './Notifications.css'
export default (props) => {
    const [notificationList, setNotificationList] = useState([]);
    var idUser = props.idUser;
    var token = sessionStorage.getItem('token');

     useEffect(() => {
        getAllallUserNotificationsAPI(
            idUser, token,
            (notifList) => {
                setNotificationList(notifList)
            },
            (error) => {

            }
        );
    }, [props.unreadNotifications]);





    return (

        <div className='rectangleNotification'>
            <ul className='ulNotifications'>
                {notificationList.length > 0 ? notificationList.map(notif => {
                    if (!notif.deleted) {
                        return (
                            <LineNotification idNotif={notif.idNotif} idUser={idUser} description={notif.description} date={notif.notificationDate} read={notif.read} idProject={notif.idProject} idUserThatNotificationIsAbout={notif.idUserThatNotificationIsAbout} />
                        )
                    }
                })
                    : <li> Sorry, no notifications to show...</li> }
            </ul>

        </div>
    )



}