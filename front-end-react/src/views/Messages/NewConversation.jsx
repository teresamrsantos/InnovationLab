import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaUserSlash } from "react-icons/fa";
import { getUserInfoAPI, getAllUsersToTalkAPI } from "../../restApi";
import CircleLoader from "../../components/layout/CircleLoader";
import LineUser from "../../components/layout/LineUser";
import ProfilePicture from '../../images/Group226.png';
import Sidebar from "../../components/menu/Sidebar";
import Title from "../../components/layout/Title";
import picture1 from "../../images/TITANS1.png";
import picture2 from "../../images/TITANS2.png";
import picture3 from "../../images/TITANS3.png";
import './Messages.css';
const images = [picture1,picture2,picture3];

export default function NewConversation() {
    const [allUser, setAllUser] = useState([]);
    const [wordSearch, setWordSearch] = useState("");
    const [loading, setLoading] = useState("");
    const navigate = useNavigate();
    var token = sessionStorage.getItem("token")

    useEffect(() => {
        if (sessionStorage.getItem("token") === null) {
            navigate("/")
        }
    }, []);

    useEffect(() => {
        getUserInfoAPI(token,(usersInfo) => {
                if (usersInfo.userType === 'VISITOR') {
                    navigate("/Home");
                }
        }, () => { 
            sessionStorage.clear(); 
            navigate("/");
        });
    }, []);

    useEffect(() => {
        getAllUsersToTalkAPI(token, (allUser) => {
            setAllUser(allUser);
            setLoading("active");
        })
    });

    const listUser = allUser.filter(person => person.username.toLowerCase().includes(wordSearch.toLowerCase()) ||
        (person.firstName.toLowerCase() + " " + person.lastName.toLowerCase()).includes(wordSearch.toLowerCase()))
        .map((user) =>
            <LineUser
                page='conversation'
                idUser={user.id}
                pictureUrl={user.pictureUrl ? user.pictureUrl : ProfilePicture}
                firstName={user.firstName}
                lastName={user.lastName}
                username={user.username}
            />
        );

        return (
            <div className="">
                <Sidebar />
                {loading!=""?
                <div className="body-messages">
                    {allUser!=""?
                    <>
                        <input className="search-usernameEmail" name="search" placeholder="Search by name/username" onChange={(e) => { setWordSearch(e.target.value) }} />
                        <div className="list-userToTalk">
                            <ul >
                                {listUser}
                            </ul>
                        </div>
                    </>
                    :<div className="no-newconversation">
                        <FaUserSlash size={"20%"} color={"gray"}/>
                        <Title title="There are no users!"/>
                    </div>}
                </div>
                :<div className="div-loading">
                    <CircleLoader/>
                    <Title title="Loading..."/>
                </div>}
            </div>
        )
    }