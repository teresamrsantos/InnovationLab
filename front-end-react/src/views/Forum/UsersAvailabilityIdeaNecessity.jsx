import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { FaExclamationCircle } from "react-icons/fa";
import { getAllUsersWithAvailableIdeaNecessityAPI, getAllIdeaNecessityByIdAPI, getUserInfoAPI } from "../../restApi"
import IdeaNecessity from "../../components/layout/IdeaNecessity";
import CircleLoader from "../../components/layout/CircleLoader";
import LineUser from "../../components/layout/LineUser";
import Sidebar from "../../components/menu/Sidebar";
import Title from "../../components/layout/Title";
import './UsersAvailabilityIdeaNecessity.css'

export default function UsersAvailabilityIdeaNecessity() {
    const [userInfo, setUserInfo] = useState('');
    const [users, setUsers] = useState('users');
    const [wordSearch, setWordSearch] = useState("");
    const [ideaNecessity, setIdeaNecessity] = useState('UsersAvailabilityIdeaNecessity');
    const { id } = useParams();
    const navigate = useNavigate();
    var token = sessionStorage.getItem("token")

    useEffect(() => {
        if (sessionStorage.getItem("token") === null) {
            navigate("/")
        }
    }, []);

    useEffect(() => {
        getUserInfoAPI(token,(usersInfo) => {
            setUserInfo(usersInfo)
                if (usersInfo.userType === 'VISITOR') {
                    navigate("/Home");
                }
        }, () => { 
            sessionStorage.clear(); 
            navigate("/");
        });
    }, []);

    useEffect(() => {
        getAllIdeaNecessityByIdAPI(id, token, (ideaNecessity) => {
            setIdeaNecessity(ideaNecessity)});
    }, []);

    useEffect(() => {
        getAllUsersWithAvailableIdeaNecessityAPI(id, token, (user) => {
            setUsers(user)});
    }, []);

    let listUser = [];
    if(users != "users"){
        listUser = users.filter(person => (person.firstName.toLowerCase() + " " + person.lastName.toLowerCase())
        .includes(wordSearch.toLowerCase()) || person.username.toLowerCase().includes(wordSearch.toLowerCase()))
        .map((user) =>
            <LineUser
                page='availability'
                idUser={user.id}
                pictureUrl={user.pictureUrl}
                firstName={user.firstName}
                lastName={user.lastName}
                username={user.username}
                className={"lineUserAvailability"}
            />
        );
    }

    return (
        <div className="usersAvailibility-ideanecessity">
            <Sidebar />
            {ideaNecessity!="" &&users != "users"?<>
                <Title className="title-usersAvailibilityIdeaNecessity" title={"Available users:"} />
                <IdeaNecessity title={ideaNecessity.title} id={ideaNecessity.id} description={ideaNecessity.description}
                pictureUrl={ideaNecessity.imageIdeaNecessity} nameAuthor={ideaNecessity.nameAuthor} userType={userInfo.userType}
                page={"usersAvailabilityIdeaNecessity"} />
                {users!="users"?
                <>
                    {users!=""?
                    <>
                        <input className="search-usernameEmail" name="search" placeholder="Search by name/username" onChange={(e) => { setWordSearch(e.target.value) }} />
                        <div className="list-usersAvailibilityIdeaNecessity">
                            {listUser}
                        </div>
                    </>
                    :<div className="no-usersAvailibility">
                        <FaExclamationCircle size={"20%"} color={"gray"}/>
                        <Title title="There are no available users!"/>
                    </div>}
                </>
                :""}
            </>
            :<div className="div-loading">
                <CircleLoader/>
                <Title title="Loading..."/>
            </div>}
        </div>
    )
}