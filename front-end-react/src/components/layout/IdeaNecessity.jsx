import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaTrashRestoreAlt } from "react-icons/fa";
import { MdFavoriteBorder, MdFavorite, MdOutlineWatchLater, MdHowToVote } from "react-icons/md";
import { BiEditAlt, BiTrash } from "react-icons/bi";
import { deleteIdeaNecessityAPI, availabilityIdeaNecessityAPI, removeAvailabilityIdeaNecessityAPI, favoriteIdeaNecessityAPI, removeFavoriteIdeaNecessityAPI, voteIdeaNecessityAPI, removeVoteIdeaNecessityAPI } from "../../restApi";
import imageExample from "../../images/Wh1dlojD-imagem_video.jpg"
import Title from "./Title";
import Text from "./Text";
import Button from "./Button";
import "./IdeaNecessity.css";
import InterestAndSkill from "./InterestAndSkill";

export default (props) => {
    var classname = props.className || 'see-alltextideanecessity';
    const [numVote, setNumVote] = useState(props.vote)
    const [listfavorite, setListFavorite] = useState(props.favoriteList)
    const [availabilityList, setAvailabilityList] = useState(props.availabilityList)
    const [voteList, setVoteList] = useState(props.voteList)
    const [deletedIdeaNecessity,setDeletedIdeaNecessity]=useState(props.deletedIdeaNecessity);
    const navigate = useNavigate();
    const id = props.id;
    var token = sessionStorage.getItem("token");
    var userType = props.userType;
    var creationTimeAux = props.creationTime;


    const creationTime = new Date(creationTimeAux);
    var creationTimeEnd = creationTime.getDate() + "/" + (creationTime.getMonth() + 1) + "/" + creationTime.getFullYear() +
        " " + creationTime.getHours() + ":" + creationTime.getMinutes();

    function favoriteIdeaNecessity(event) {
        props.setAlertChange(false);
        let array = listfavorite;
        let newArray = [];
        newArray.push(listfavorite)
        newArray.push(props.idUser)
        setListFavorite(newArray)
        favoriteIdeaNecessityAPI(props.id, token, (e) => (setListFavorite(newArray)), (e) => {setListFavorite(array) })
    }

    function removeFavoriteIdeaNecessity(event) {
        props.setAlertChange(false);
        let array = listfavorite;
        let newArray = [];
        for (let i = 0; i < listfavorite.length; i++) {
            if (listfavorite[i] != props.idUser) {
                newArray.push(listfavorite[i])
            }
        }
        setListFavorite(newArray)
        removeFavoriteIdeaNecessityAPI(props.id, token, (e) => (setListFavorite(newArray)), (onError) => (setListFavorite(array)))
    }

    function voteIdeaNecessity(event) {
        props.setAlertChange(false);
        let array = voteList;
        let newArray = [];
        newArray.push(voteList);
        newArray.push(props.idUser);
        setNumVote(numVote * 1 + 1);
        setVoteList(newArray);
        voteIdeaNecessityAPI(props.id, token, () => (setVoteList(newArray)), (onError) => (setVoteList(array)))
    }

    function removeVoteIdeaNecessity(event) {
        props.setAlertChange(false);
        let array = voteList;
        let newArray = [];
        for (let i = 0; i < voteList.length; i++) {
            if (voteList[i] != props.idUser) {
                newArray.push(voteList[i])
            }
        }
        setNumVote(numVote * 1 - 1);
        setVoteList(newArray);
        removeVoteIdeaNecessityAPI(props.id, token, () => (setVoteList(newArray)), (onError) => (setVoteList(array)))
    }

    function availabilityIdeaNecessity(event) {
        props.setAlertChange(false);
        let array = availabilityList;
        let newArray = [];
        newArray.push(availabilityList)
        newArray.push(props.idUser)
        setAvailabilityList(newArray)
        availabilityIdeaNecessityAPI(props.id, token, () => (setAvailabilityList(newArray)), (onError) => (setAvailabilityList(array)))
    }

    function removeAvailabilityIdeaNecessity(event) {
        props.setAlertChange(false);
        let array = availabilityList;
        let newArray = [];
        for (let i = 0; i < availabilityList.length; i++) {
            if (availabilityList[i] != props.idUser) {
                newArray.push(availabilityList[i])
            }
        }
        setAvailabilityList(newArray)
        removeAvailabilityIdeaNecessityAPI(props.id, token, () => (setAvailabilityList(newArray)), (onError) => (setAvailabilityList(array)))
    }

    function deleteIdeaNecessity(e) {
        props.setAlertChange(false);
        deleteIdeaNecessityAPI(props.id, token,((response) => {
            props.resetDeleteIdeaNecessity(e);
            setDeletedIdeaNecessity(e);
        }),((error) => {
            props.setAlertChange(true);
        }));
    }

    return (
        <>
            {props.page === 'commentIdeaNecessity' ?
                <div className={classname}>
                    <div className="header-seeideaNecessity">
                        <div className="header1-seeideaNecessity">
                            <Title className={"title-ideaNecessityToSee"} title={props.title} />
                            {/*button vote*/}
                            {(userType !== "VISITOR" && props.deletedIdeaNecessity == false) ?
                                <div>{voteList.includes(props.idUser) ?
                                    <Button className='lovebtn-ideaNecessity' id={props.id} text={numVote + " votes"} iconBefore={<MdHowToVote className="" size={"2em"} id={props.id} color="green" />} onclick={(event) => { removeVoteIdeaNecessity(event) }} />
                                    : <Button className='lovebtn-ideaNecessity' id={props.id} text={numVote + " votes"} iconBefore={<MdHowToVote className="" size={"2em"} id={props.id} />} onclick={(event) => { voteIdeaNecessity(event) }} />
                                }</div>
                                : <Text className={"text-numbervote"} iconBefore={<MdHowToVote className="" size={"2em"} id={props.id} color="green" />} text={numVote + " votes"} />}
                            {/*button favorite*/}
                            {(userType !== "VISITOR" && props.deletedIdeaNecessity == false) ?
                                <div>{listfavorite.includes(props.idUser) ?
                                    <Button className='lovebtn-ideaNecessity' id={props.id} text={<MdFavorite size={"2em"} id={props.id} color="#C01111" />} onclick={(event) => { removeFavoriteIdeaNecessity(event) }} />
                                    :<Button className='lovebtn-ideaNecessity' id={props.id} text={<MdFavoriteBorder size={"2em"} id={props.id} i color="#C01111" />} onclick={(event) => { favoriteIdeaNecessity(event) }} />
                                }</div>
                                : ''}
                            {/*button availability*/}
                            {(userType !== "VISITOR" && props.deletedIdeaNecessity == false) ?
                                <div>{availabilityList.includes(props.idUser) ?
                                    <Button className='availablebtn-ideaNecessity' id={props.id} iconBefore={<MdOutlineWatchLater className="" size={"2em"} id={props.id} color="blue" />} onclick={(event) => { removeAvailabilityIdeaNecessity(event) }} />
                                    :<Button className='availablebtn-ideaNecessity' id={props.id} iconBefore={<MdOutlineWatchLater className="" size={"2em"} id={props.id} />} onclick={(event) => { availabilityIdeaNecessity(event) }} />
                                }</div>
                                : ''}
                        </div>
                        <div className="header2-seeideaNecessity">
                            {(userType === "ADMIN") ? <>{(deletedIdeaNecessity== false) ?
                                <div>
                                    <Button className='editbtn-ideaNecessity' id={props.id} text={<BiEditAlt size={"2em"} id={props.id} color="#142E56" />} onclick={(event) => { navigate('/editIdeaNecessity/' + props.id) }} />
                                    <Button className='trashbtn-ideaNecessity' id={props.id} text={<BiTrash size={"2em"} id={props.id} color="#142E56" />} onclick={(event) => { deleteIdeaNecessity(true) }} />
                                </div>
                                : <div>
                                    <Button className='trashRestore-ideaNecessity' id={props.id} text={<FaTrashRestoreAlt size={"2em"} id={props.id} color="#142E56" />} onclick={(event) => { deleteIdeaNecessity(false) }} />
                                </div>
                            }</> : ""}
                        </div>
                    </div>
                    <div className="body-ideanecessityToSee">
                        <div className="description-ideaNecessityToSee" dangerouslySetInnerHTML={{ __html: props.description }} />
                        <img className={'picture-ideaNecessityToSee'} src={props.pictureUrl==null? imageExample : props.pictureUrl} />
                    </div>
                    <InterestAndSkill allSkill={props.allSkill} allInterest={props.allInterest} />

                    <div className="divrow-ideanecessity">
                        <div className="author-divIdeaNecessity">
                            <Text className={"autor-ideaNecessityToSee"} text={"Author: "} />
                            <Button className="authorbtn1-ideaNecessity" tooltipText={"See user profile"} text={props.nameAuthor} onclick={(event) => { navigate('/userProfile/' + props.idAuthor) }} />
                        </div>
                        {userType!=="VISITOR"?
                        <div className="divrtl-ideanecessity">
                            <Button className='usersavailable-ideaNecessity' id={props.id} text={"See users available to work!"} onclick={(event) => { navigate("/usersAvailabilityIdeaNecessity/" + id) }} />
                        </div>
                        :
                        <div className="divdate-ideaNecessity2">
                        <Text className={"date-ideaNecessityComment2"} text={creationTimeEnd} />
                        </div>
                        }
                    </div>
                    {userType!=="VISITOR"?
                    <div className="divdate-ideaNecessity">
                        <Button className='postlink-ideaNecessity' id={props.id} text={"See posts related to this one!"} onclick={(event) => { navigate("/postRelatedIdeaNecessity/" + id) }} />
                        <Text className={"date-ideaNecessityComment"} text={creationTimeEnd} />
                    </div>:""}
                </div>
                :
                <div className={classname}>
                    <Title className={"title-ideaNecessityToSee"} title={props.title} />
                    <div className="body-ideanecessityToSee">
                        <div dangerouslySetInnerHTML={{ __html: props.description }}></div>
                        {/*<img className={'picture-ideaNecessityToSee'} src={props.pictureUrl} />*/}
                    </div>
                    <div className="footer-ideanecessity">
                        <div className="divauthor-ideanecessity">
                            {(props.page == 'postRelatedMyIdeaNecessity') ? "" :
                                <div className="divdate-ideaNecessity">
                                    <Text className={"autor-ideaNecessityToSee"} text={"Author: "} />
                                    <Button className="authorbtn1-ideaNecessity" tooltipText={"See user profile"} text={props.nameAuthor} onclick={(event) => { navigate('/userProfile/' + props.idAuthor) }} />
                                </div>
                            }
                        </div>
                        {((props.page == 'postRelatedIdeaNecessity' || props.page == 'postRelatedMyIdeaNecessity') && props.deletedIdeaNecessity == false) ? <Button className="linkMore-ideanecessity" id={props.id} text={"You believe another idea should be linked to this ? Tell us about it!"} onclick={(event) => { navigate("/linkIdeaNecessity/" + id) }} /> : ""}
                    </div>
                </div>
            }
        </>
    );
};