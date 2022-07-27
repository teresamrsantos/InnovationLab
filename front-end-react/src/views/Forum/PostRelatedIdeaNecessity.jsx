import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { FaExclamationCircle } from "react-icons/fa";
import { getAllIdeaNecessityByIdAPI, getUserInfoAPI } from "../../restApi";
import ListJustification from "../../components/layout/ListJustification";
import IdeaNecessity from "../../components/layout/IdeaNecessity";
import Title from "../../components/layout/Title";
import Sidebar from "../../components/menu/Sidebar";
import CircleLoader from "../../components/layout/CircleLoader";
import './PostRelatedIdeaNecessity.css'

export default function PostRelatedIdeaNecessity() {
    const [userInfo, setUserInfo] = useState('');
    const [ideaNecessity, setIdeaNecessity] = useState(' ');
    const [wordSearch, setWordSearch] = useState("");
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
            setIdeaNecessity(ideaNecessity)
        });
    }, []);

    return (
        <div className="postRelated-ideanecessity">
            <Sidebar />
            {(ideaNecessity !== "PostRelatedIdeaNecessity" &&Array.isArray(ideaNecessity.ideaNecessityAssociatedList)) ?
            <>
                <Title className="title-postRelatedIdeaNecessity" title={"Related Posts to:"} />
                <IdeaNecessity title={ideaNecessity.title} id={ideaNecessity.id} description={ideaNecessity.description}
                pictureUrl={ideaNecessity.imageIdeaNecessity} nameAuthor={ideaNecessity.nameAuthor} userType={userInfo.userType}
                page={"postRelatedIdeaNecessity"} deletedIdeaNecessity={ideaNecessity.deletedIdeaNecessity}/>
                {ideaNecessity.ideaNecessityAssociatedList!=""?
                <>
                    <input className="search-usernameEmail" name="search" placeholder="Search idea/necessity by title" onChange={(e) => { setWordSearch(e.target.value) }}/>
                    <div className="list-allAssIdeaNecessity">
                        <ListJustification wordSearch={wordSearch} page={"justificationIdeaNecessity"} 
                        testinformation={ideaNecessity.ideaNecessityAssociatedList} className="justificationIdeaNecessity" id={id}/>
                    </div>
                </>
                :<div className="no-postRelated">
                    <FaExclamationCircle size={"20%"} color={"gray"}/>
                    <Title title="There are no related posts!"/>
                </div>}
            </>
            :<div className="div-loading">
                <CircleLoader/>
                <Title title="Loading..."/>
            </div>}
        </div>
    )
}