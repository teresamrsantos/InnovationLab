import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { FaExclamationCircle } from "react-icons/fa";
import { getAllIdeaNecessityByIdAPI, getUserInfoAPI } from "../../restApi";
import ListJustification from "../../components/layout/ListJustification";
import IdeaNecessity from "../../components/layout/IdeaNecessity";
import CircleLoader from "../../components/layout/CircleLoader";
import Sidebar from "../../components/menu/Sidebar";
import Title from "../../components/layout/Title";
import './PostRelatedMyIdeaNecessity.css';

export default function PostRelatedIdeaNecessity() {
    const [userInfo, setUserInfo] = useState('');
    const [ideaNecessity, setIdeaNecessity] = useState('PostRelatedIdeaNecessity');
    const [wordSearch, setWordSearch] = useState("");
    const { id } = useParams();
    const navigate = useNavigate();
    var token = sessionStorage.getItem("token");

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
        <div className="postRelated-myideanecessity">
            <Sidebar />
            {ideaNecessity!="PostRelatedIdeaNecessity"?<>
                <div className="div-title">
                    <Title className="title-postRelatedIdeaNecessity" title={"Related Posts to:"} />
                    <Title className="title-postRelatedMyIdeaNecessity" title={ideaNecessity.title} />    
                </div>
                <IdeaNecessity id={ideaNecessity.id} description={ideaNecessity.description} nameAuthor={ideaNecessity.nameAuthor} userType={userInfo.userType}
                pictureUrl={ideaNecessity.imageIdeaNecessity} page={"postRelatedMyIdeaNecessity"} deletedIdeaNecessity={ideaNecessity.deletedIdeaNecessity}/>
                {(ideaNecessity!="" && Array.isArray(ideaNecessity.ideaNecessityAssociatedList)) ?
                <>
                    <input className="search-usernameEmail" name="search" placeholder="Search idea/necessity by title" onChange={(e) => { setWordSearch(e.target.value) }}/>
                    <div className="list-allAssMyIdeaNecessity">
                        <ListJustification  wordSearch={wordSearch} page={"justificationMyIdeaNecessity"} 
                        testinformation={ideaNecessity.ideaNecessityAssociatedList} className="justificationIdeaNecessity" id={id}/>
                    </div>
                </>
                :<div className="no-mypostRelated">
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