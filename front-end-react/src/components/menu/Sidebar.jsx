import React, { useEffect, useState } from 'react';
import * as AiIcons from 'react-icons/ai';
import * as FaIcons from 'react-icons/fa';
import { FaPowerOff } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { Menu, SubMenu } from 'react-pro-sidebar';
import 'react-pro-sidebar/dist/css/styles.css';
import { Link } from "react-router-dom";
import { logoutAPI, countUnreadMessagesAPI, countUnreadNotificationsAPI, getUserInfoAPI, getTimeoutTimeAPI } from "../../restApi";
import profilePicture from "../../images/Group226.png";
import picture1 from "../../images/TITANS1.png";
import picture2 from "../../images/TITANS2.png";
import picture3 from "../../images/TITANS3.png";

import Text from "../layout/Text";
import Button from "../layout/Button";
import './Sidebar.css';
import { SidebarData, SidebarDataAdmin, SidebarDataVisitor } from './SidebarData';
import Notifications from './Notifications';


export default (props) => {
    var classname = props.className || 'menu';
    const [showNotification, setShowNotification] = useState(false)
    const [sidebar, setSidebar] = useState(false);
    const images = [
        picture1,
        picture2,
        picture3,
    ];
    const [picture, setPicture] = useState(images[Math.floor(Math.random() * images.length)])
    const [unreadNotifications, setUnreadNotifications] = useState(0);
    const [unreadMessages, setUnreadMessages] = useState(0);
    var token = sessionStorage.getItem("token")
    const [userInfo, setUserInfo] = useState('');
    const [menuInfo, setMenuInfo] = useState(SidebarData)
    const navigate = useNavigate();

    useEffect(() => {
        countUnreadNotifications();
        countUnreadMessages()
        getUserInfoAPI(token, (usersInfo) => {
            setUserInfo(usersInfo)

            if (usersInfo.pictureUrl != null) {
                setPicture(usersInfo.pictureUrl)
            }

            if (usersInfo.userType === 'VISITOR') {
                setMenuInfo(SidebarDataVisitor)
            } else if (usersInfo.userType === 'ADMIN') {
                setMenuInfo(SidebarDataAdmin)
            }
        }, (error) => { });
    }, []);

    var username = userInfo.username ? userInfo.username : 'Username';
    var userType = userInfo.userType ? userInfo.userType : 'Role';


    function countUnreadNotifications() {

        countUnreadNotificationsAPI(token, (notifNumber) => {
            setUnreadNotifications(notifNumber);
        });
    }

    function countUnreadMessages(){
        countUnreadMessagesAPI(token, (messageNumber) => {
            setUnreadMessages(messageNumber)
        })
    }
    setTimeout(() => {
        countUnreadNotifications()
        countUnreadMessages()
    }, 5000);

    function logout() {
        logoutAPI(token, () => {
            sessionStorage.removeItem('token');
            sessionStorage.clear();
            navigate('/');
        })
    }

    /*  const [timeoutTime, setTimeoutTime] = useState(10);
  
  
     useEffect(() => {
          getTimeoutTimeAPI(token, (e) => { console.log(e); setTimeoutTime(e.sessionTimeOutTime) }, (error) => {
  
              console.log(error)
          })
      }, []);
  
  
          const [intervalId, setIntervalId] = useState(null);
  
      useEffect(() => { start() }, []);
  
  function start() {
          const id = window.setInterval(() => { logout() }, timeoutTime * 1000);
          setIntervalId(id);
      }
  
      function handleMouseMove(event) {
          window.clearInterval(intervalId)

          
          start();
      }*/

    const showSidebar = () => setSidebar(!sidebar);
    const openNotification = () => setShowNotification(!showNotification)
    return (
        <>
            <div className='topbar ' /*onMouseMove={handleMouseMove}*/>
                <div className='navbar-toggle open' onClick={showSidebar} >
                    <FaIcons.FaBars />
                </div>
                <div className='topbar_text'>
                    <Link to='/forum' className="link topbarLink"> Forum </Link>
                    {userType !== 'VISITOR' ? <Link to='/projects' className="link topbarLink"> Projects </Link> : ''}
                </div>
                <div className='topbar_iconDIV'>
                    {userType !== 'VISITOR' ? <>
                        <span>
                            <div onClick={openNotification} className="link topbarIcon">
                                <AiIcons.AiFillBell size={20} /> {unreadNotifications > 0 ? <div className='divCircle' >
                                    <p className="circleText">{unreadNotifications}</p> </div> : ''}
                            </div>
                        </span>
                        <span>
                            <Link to='/messages' className="link topbarIcon">
                                <AiIcons.AiFillMail size={20} /> {unreadMessages > 0 ? <div className='divCircle' >
                                    <p className="circleText">{unreadMessages}</p> </div> : ''}
                            </Link>
                        </span></> : ""}
                    <span>
                        <div className="link topbarIcon">
                            <li onClick={logout}><FaPowerOff size={15} /></li>
                        </div>
                    </span>
                </div>
                <div style={{ display: showNotification ? "block" : "none" }}>
                    {userInfo.idUser !== undefined ? <Notifications unreadNotifications={unreadNotifications} idUser={userInfo.idUser}></Notifications> : ''} </div>
            </div>
            <main className={classname}>
                <div className={sidebar ? 'nav-menu active' : 'nav-menu'}>
                    <div className='nav-menu-items'>
                        <div className='navbar-toggle close' onClick={showSidebar}>
                            <AiIcons.AiOutlineClose />
                        </div>
                        <div className='userInfoContainer'>
                            <img className='picture' src={picture} />
                            <div className='userInfoContainer_text'>
                                <Text className="menu-username" text={username} />
                                <Text className="menu-role" text={userType} />
                            </div>
                        </div>
                        <div className='menu-Content-container'>
                            <Menu>
                                {menuInfo.map((item, index) => {
                                    return (
                                        <div className={item.cName}>
                                            {item.submenu ? (
                                                <SubMenu title={<span> &nbsp; {item.title}  &nbsp;</span>} className="submenu" icon={item.icon} suffix={<span className='arrow'><AiIcons.AiFillCaretDown /></span>}>
                                                    {item.submenu.map((submenu, index) => (
                                                        <Link key={index} to={submenu.path} className="link">{submenu.title} <br /></Link>
                                                    ))}
                                                </SubMenu>) : (
                                                <Link className="link" to={item.path}>
                                                    {item.icon}
                                                    <span> &nbsp; {item.title}</span>
                                                </Link>
                                            )}
                                        </div>);
                                })}
                            </Menu>
                        </div>
                    </div>
                </div>
            </main>
        </>
    )
}
