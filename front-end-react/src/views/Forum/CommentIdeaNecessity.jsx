import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { RiSendPlane2Fill } from "react-icons/ri";
import { getAllSkillAssociateIdeaNecessityAPI, getAllInterestAssociateIdeaNecessityAPI, addAnswerAPI, addCommentAPI, getAllPostsAPI, getAllIdeaNecessityByIdAPI, getUserInfoAPI } from "../../restApi"
import IdeaNecessity from "../../components/layout/IdeaNecessity";
import CircleLoader from "../../components/layout/CircleLoader";
import Comment from "../../components/layout/Comment";
import Button from "../../components/layout/Button";
import Sidebar from "../../components/menu/Sidebar";
import Alert from "../../components/layout/Alert";
import Title from "../../components/layout/Title";
import Text from "../../components/layout/Text";
import './CommentIdeaNecessity.css'

export default function CommentIdeaNecessity() {
    const navigate = useNavigate();
    const [userInfo, setUserInfo] = useState([]);
    const [ideaNecessity, setIdeaNecessity] = React.useState('ideaNecessity');
    const [allSkill, setAllSkill] = React.useState(['']);
    const [allInterest, setAllInterest] = React.useState(['']);
    const [allPost, setAllPost] = useState('allPost');
    const [idComment, setIdComment] = useState('');
    const [newDescription, setNewDescription] = useState([]);
    const [seeMore, setSeeMore] = useState(2);
    const [placeholder, setPlaceholder] = useState('Write your comment...');
    const [deletedIdeaNecessity,setDeletedIdeaNecessity]=useState('');
    const [alert, setAlert] = useState(false);
    const [username, setUsername] = useState('');

    var token = sessionStorage.getItem("token")
    var userType = userInfo.userType;
    var idUser = userInfo.idUser;
    const { id } = useParams();

    useEffect(() => {
        getUserInfoAPI(token, (usersInfo) => {
            setUserInfo(usersInfo)
        }, () => { sessionStorage.clear(); 
            navigate("/") 
        })
    }, []);

    useEffect(() => {
        getAllIdeaNecessityByIdAPI(id, token, (ideaNecessity) => {
            setDeletedIdeaNecessity(ideaNecessity.deletedIdeaNecessity)
            setIdeaNecessity(ideaNecessity)
        });
    }, []);

    useEffect(() => {
        getAllSkillAssociateIdeaNecessityAPI(id, token, (skill) => {
            setAllSkill(skill)
        });
    }, []);

    useEffect(() => {
        getAllInterestAssociateIdeaNecessityAPI(id, token, (interest) => {
            setAllInterest(interest)
        });
    }, []);

    useEffect(() => {
        getAllPostsAPI(id, token, (post) => {
            setAllPost(post)
        });
    }, [])

    function resetIdeaNecessity() {
        setAlert(false);
        getAllIdeaNecessityByIdAPI(id, token, (ideaNecessity) => {
            setIdeaNecessity(ideaNecessity)
        });
    }

    function resetDeleteIdeaNecessity(e) {
        if(deletedIdeaNecessity===false){
            setDeletedIdeaNecessity(true);
        }else{
            setDeletedIdeaNecessity(false);
        }
        
        getAllIdeaNecessityByIdAPI(id, token, (ideaNecessity) => {
            setIdeaNecessity(ideaNecessity)
        });
    }

    function addCommentOrAnswer(e) {
        setAlert(false);
        if(newDescription!=""){
        if (idComment == '') {
            var myJSON = JSON.stringify({ description: newDescription, idIdeaNecessity: id });
            addCommentAPI(myJSON, token, (e) => {
                setAllPost(allPost => [...allPost, e])
            });
        } else {
            var myJSON = JSON.stringify({ description: newDescription, idComment: idComment });
            addAnswerAPI(myJSON, token, (e) => {
                allPost.forEach(element => {
                    if (element.idPost === e.idComment) {
                        element.answerList.push(e)
                        //estou a substituir no array allpost pelo elemento que alterei.
                        const newPost = allPost.map(obj => {
                            if (obj.id === element.idPost) {
                                return element;
                            }
                            return obj;
                        });
                        setAllPost(newPost)
                    }
                });
            })
        }
    }
        setNewDescription('');
        setIdComment('');
        setPlaceholder('Write your comment...');
    }

    function replyAnswer(username,e) {
        setAlert(false);
        setNewDescription("@"+username);
        setIdComment(e.target.id)
        setPlaceholder("Write your reply...")
    }

    function setAlertChange(e){
        setAlert(e);
    }

    return (
        <div className="comment-ideanecessity">
            <Sidebar />
            <div style={{ display: alert ? "block" : "none" }}>
                <Alert className={"alert-warning "} text={"It cannot be deleted. It is associated with a Project."}/>
            </div><br/>
            {(ideaNecessity!=="ideaNecessity" && Array.isArray(ideaNecessity.favoriteList)&&allPost!=="allPost")?
            <>
                <IdeaNecessity setAlertChange={setAlertChange} key={id} resetIdeaNecessity={resetIdeaNecessity} title={ideaNecessity.title} 
                vote={ideaNecessity.vote} page={"commentIdeaNecessity"} userType={userType} idUser={idUser} allInterest={allInterest}
                favoriteList={ideaNecessity.favoriteList} availabilityList={ideaNecessity.availabilityList} id={id}  allSkill={allSkill}
                voteList={ideaNecessity.voteList} nameAuthor={ideaNecessity.nameAuthor} creationTime={ideaNecessity.creationTime} 
                description={ideaNecessity.description} pictureUrl={ideaNecessity.imageIdeaNecessity} idAuthor={ideaNecessity.idAuthor}
                deletedIdeaNecessity={deletedIdeaNecessity} resetDeleteIdeaNecessity={resetDeleteIdeaNecessity}/>
                <Title className="title-commentIdeaNecessity" title={"Comment:"} />
                {(seeMore >= allPost.length) ? '' 
                :<Button className={"seemore-comment"} text={"View previous comment..."} onclick={(event) => { setSeeMore(seeMore + 2) }} />}
                {allPost.length > 0 ? allPost.map((post, index) => {
                    if (index > (allPost.length - seeMore - 1)) {
                        return <Comment
                            key={index}
                            description={post.description}
                            firstName={post.userJoinPost.firstName}
                            lastName={post.userJoinPost.lastName}
                            username={post.userJoinPost.username}
                            creationTime={post.creationTime}
                            answerList={post.answerList}
                            idPost={post.idPost}
                            replyAnswer={replyAnswer}
                        />
                    }
                }) 
                :<Text className="noComment" text={"No Comments"}/>}
                {(userType !== "VISITOR" && ideaNecessity.deletedIdeaNecessity==false) ?
                <div className="Send-Comment">
                    <textarea className="textarea-comment" value={newDescription} placeholder={placeholder} type="text" onChange={(e) => setNewDescription(e.target.value)} />
                    {newDescription!=""? 
                    <>
                    {/*<Button className={"add-comment"} text={"Add comment"} onclick={(event) => { addCommentOrAnswer(event) }} />*/}
                    <Button className='add-comment' icon={<RiSendPlane2Fill size={"100%"} color="white" />} onclick={(event) => { addCommentOrAnswer(event)}} />
                    </>
                    :""}
                </div>:""}
            </>
            :<div className="div-loading">
                <CircleLoader/>
                <Title title="Loading..."/>
            </div>}
        </div>
    )
}