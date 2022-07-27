import React, {useState,useEffect} from "react";
import { useNavigate } from "react-router-dom";
import { TbMessagePlus,TbMessagesOff } from "react-icons/tb";
import { getUserInfoAPI, getAllUserWithConversationAPI} from "../../restApi"
import LineConversationUser from "../../components/layout/LineConversationUser";
import CircleLoader from "../../components/layout/CircleLoader";
import Sidebar from "../../components/menu/Sidebar";
import Button from "../../components/layout/Button";
import Title from "../../components/layout/Title";
import './Messages.css'

export default function AllConversation() {
    const [allConversation, setAllConversation] = useState([]);
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
        getAllUserWithConversationAPI(token, (allConversation) => {
            setAllConversation(allConversation)
            setLoading("active")
        })
    }, []);

    setTimeout(() => {
        getAllUserWithConversationAPI(token, (allConversation) => {
            setAllConversation(allConversation)
        })
    }, 3000);

    const listConversation = allConversation
    .filter(person => person.name.toLowerCase()
    .includes(wordSearch.toLowerCase())||person.username.toLowerCase().includes(wordSearch.toLowerCase()))
    .map((conversation) =>
        <LineConversationUser
        idUser={conversation.idUser}
        photo={conversation.photo}
        name={conversation.name}
        messageToRead={conversation.messageToRead}
        dateLastMessage={conversation.dateLastMessage}/>
        );

        return (
            <div >
                <Sidebar/>
                {loading!=""?
                <div className="body-messages">
                    <div className="firstline-allconversation">
                        {allConversation!=""?
                        <input className="search-usernameEmail" name="search" placeholder="Search by name/username" onChange={(e) => { setWordSearch(e.target.value) }}/>
                        :""}
                        <div className="div-simbolNewMessage">
                            <Button className='simbol-newMessage' icon= { <TbMessagePlus size={"20%"} color="gray"/>} onclick={(event) => {navigate('/newconversation') }}/>
                        </div>
                    </div>
                    {allConversation!=""?
                    <div className="list-allConversation">
                        <ul>
                            {listConversation}
                        </ul>
                    </div>
                    :<div className="no-allConversation">
                        <TbMessagesOff size={"20%"} color={"gray"}/>
                        <Title title="There are no conversations!"/>
                    </div>}
                </div>
                :<div className="div-loading">
                    <CircleLoader/>
                    <Title title="Loading..."/>
                </div>}
            </div>
        )
    }