import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getUserInfoAPI, getInfoUserConversationAPI, getConversationBetweenUsersAPI, sendMessageAPI, readMessageAPI } from "../../restApi"
import CircleLoader from "../../components/layout/CircleLoader";
import { GrSend } from "react-icons/gr";
import { IoHandRightSharp } from "react-icons/io5";
import Sidebar from "../../components/menu/Sidebar";
import Text from "../../components/layout/Text";
import Message from "../../components/layout/Message";
import Button from "../../components/layout/Button";
import Title from "../../components/layout/Title";
import './Messages.css'

export default function Conversation() {
    const [userInfo, setUserInfo] = useState('');
    const [conversation, setConversation] = useState([]);
    const [messageSend, setMessageSend] = useState("");
    const [loading, setLoading] = useState("");
    const [photo, setPhoto]=useState();
    const navigate = useNavigate();
    const { id } = useParams();
    const { name } = useParams();
    var idUserOn = userInfo.idUser;
    var token = sessionStorage.getItem("token");

    useEffect(() => {
        if (sessionStorage.getItem("token") === null) {
            navigate("/")
        }
    }, []);

    useEffect(() => {
        getUserInfoAPI(token,(usersInfo) => {
            setUserInfo(usersInfo)
            setLoading("active")
                if (usersInfo.userType === 'VISITOR') {
                    navigate("/Home");
                }
        }, () => { 
            sessionStorage.clear(); 
            navigate("/");
        });
    }, []);

    useEffect(() => {
        getInfoUserConversationAPI(id,token,(userInfo) => {
            setPhoto(userInfo.photo)
            },(error) => {});
    }, []);

    const sort_lists = (key, list) => [...list].sort((b, a) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))

    useEffect(() => {
        getConversationBetweenUsersAPI(token, id, (conversation) => {
            let newSortedList = sort_lists('messageTime', conversation)
            setConversation(newSortedList);
        },(error) => {});
    }, []);

    setTimeout(() => {
        getConversationBetweenUsersAPI(token, id, (conversation) => {
            let newSortedList = sort_lists('messageTime', conversation)
            setConversation(newSortedList);
        })
        readMessageAPI(id, token)
    }, 3000);


    const listItems = conversation.map((conversationsend) =>
        <Message
            message={conversationsend.message}
            idUser={conversationsend.userSender}
            idUserOn={idUserOn}
            messageTime={conversationsend.messageTime}
            name={(conversationsend.userSender === idUserOn) ? "You" : conversationsend.nameSender}
            photo = {(conversationsend.userReceiver === idUserOn) ? userInfo.pictureUrl : conversationsend.photoSender}
        />
    );

    function sendMessage(event) {
        var myJSON = JSON.stringify({ message: messageSend, userReceiver: id });
        setMessageSend("")
        sendMessageAPI(myJSON, token)
    }

    function sendHello(event) {
        var myJSON = JSON.stringify({ message: "Hello ✋", userReceiver: id });
        setMessageSend("")
        sendMessageAPI(myJSON, token)
    }

    return (
        <div>
            <Sidebar />
            {loading!==""?
            <div className="body-messages">
                <div className="user-received">
                    <div className="img-user">
                        <img className='picture-user' src={photo} />
                    </div>
                    <div className="info-user">
                        <Text className="name-user" text={name} />
                    </div>
                </div>
                {conversation!=""?
                <div className="dialogue-message">
                    {listItems}
                </div>
                :<Button className={"saidHelloBtn"} text={"Say Hello"} icon={<IoHandRightSharp/>}  onclick={(event) => { sendHello(event) }}/>}
                <div className="send-message">
                    <input className="input-message" placeholder="Message...." value={messageSend} onChange={(e) => setMessageSend(e.target.value)} />
                    <Button className='send-btn' icon={<GrSend size={"100%"} />} onclick={(event) => { sendMessage(event) }} />
                </div>
            </div>
            :<div className="div-loading">
                <CircleLoader/>
                <Title title="Loading..."/>
            </div>}
        </div>
    )
}