import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaRegLightbulb, FaRegComment, FaHandsHelping,FaTrashRestoreAlt } from "react-icons/fa";
import { MdFavoriteBorder, MdFavorite,MdHowToVote } from "react-icons/md";
import { GrUpdate } from "react-icons/gr";
import { BiEditAlt,BiLinkAlt,BiTrash } from "react-icons/bi";
import { deleteIdeaNecessityAPI, desassociateIdeaNecessityAPI, favoriteIdeaNecessityAPI, removeFavoriteIdeaNecessityAPI } from "../../restApi"
import Title from "./Title";
import Text from "./Text";
import Button from "./Button";
import "./LineIdeaNecessity.css";

export default (props) => {
    const [favoriteEdit, setFavoriteEdit] = useState(props.favorite)
    const [view, setView] = useState(props.view)
    const [deletedIdeaNecessity,setDeletedIdeaNecessity]=useState(props.deletedIdeaNecessity);
    const [classname,setClassname]=useState(props.className);
    const navigate = useNavigate();
    var type = props.type === "IDEA";
    var vote = props.vote + ' votes';
    var author = 'Author:' + props.author;
    var token = sessionStorage.getItem("token")
    var userType = props.userType;
    

    //https://www.npmjs.com/package/@blac-sheep/html-to-text
    let btnFavorite;
    if (userType !== "VISITOR") {
        if (favoriteEdit === true) {
            btnFavorite = <Button className='love-ideaNecessity' tooltipText={"Remove as favorite"} id={props.id} text={<MdFavorite size={"100%"} id={props.id} color="C01111"/>} onclick={(event) => { removeFavoriteIdeaNecessity(event) }} />
        } else {
            btnFavorite = <Button className='love-ideaNecessity' tooltipText={"Choose as favorite"} id={props.id} text={<MdFavoriteBorder size={"100%"} id={props.id} color="#C01111"/> } onclick={(event) => { favoriteIdeaNecessity(event) }} />
        }
    } else {
        btnFavorite = "";
    }

    var creationTimeAux = props.creationTime;
    const creationTime = new Date(creationTimeAux);
    var creationTimeEnd = creationTime.getDate() + "/" + (creationTime.getMonth() + 1) + "/" + creationTime.getFullYear() +
        " " + creationTime.getHours() + ":" + creationTime.getMinutes();

    var updateTimeAux = props.updateTime;
    const updateTime = new Date(updateTimeAux);
    var updateTimeEnd = updateTime.getDate() + "/" + (updateTime.getMonth() + 1) + "/" + updateTime.getFullYear() +
        " " + updateTime.getHours() + ":" + updateTime.getMinutes();

        function favoriteIdeaNecessity(event) {
            setFavoriteEdit(true)
            favoriteIdeaNecessityAPI(props.id, token, () => {setFavoriteEdit(true)}, (error) => {setFavoriteEdit(false)})
            //props.resetListIdeaNecessity()
        }
    
        function removeFavoriteIdeaNecessity(event) {
            if(props.page === 'allFavoriteIdeaNecessity'){
                setView('hidden')
                removeFavoriteIdeaNecessityAPI(props.id, token, () => {setView('hidden');}, (error) => {setView('show')})
               // props.resetListIdeaNecessity()
                props.resetSize()
            }else{
                setFavoriteEdit(false)
                removeFavoriteIdeaNecessityAPI(props.id, token, () => {setFavoriteEdit(false)}, (error) => {setFavoriteEdit(true)})
                // props.resetListIdeaNecessity()
            }
        }

    function seeIdeaNecessity(event) {
        navigate('/commentIdeaNecessity/' + props.id)
    }

    function desassociateIdeaNecessity(e) {
        desassociateIdeaNecessityAPI(props.id,props.idAss, token,() => (setView('hidden')));
    }

    function deleteIdeaNecessity(e) {
        props.setAlertChange(false);
        deleteIdeaNecessityAPI(props.id, token,((response) => {
            setDeletedIdeaNecessity(e);
            (e === false) ? setClassname("ideaNecessity") : setClassname("ideaNecessityDelete");
        }),((error) => {
            props.setAlertChange(true);
        }));
    }

var description = props.description.replaceAll('&nbsp;', ' ').replaceAll('</p>', ' ').replaceAll('<p>', ' ')
.replaceAll('<figure class="table">', '').replaceAll('</figure>', ' ').replaceAll('<table>', ' ').replaceAll('</table>', ' ')
.replaceAll('<tbody>', ' ').replaceAll('</tbody>', ' ').replaceAll('<tr>', ' ').replaceAll('</tr>', ' ')
.replaceAll('<td>', ' ').replaceAll('</td>', ' ').replaceAll('</ol>', ' ').replaceAll('<ol>', ' ')
.replaceAll('<ul>', ' ').replaceAll('</ul>', ' ').replaceAll('<li>', ' ').replaceAll('</li>', ' ')
.replaceAll('<strong>', '').replaceAll('</strong>', '').replaceAll('<i>', ' ').replaceAll('</i>', ' ')
.replaceAll('<h1>', ' ').replaceAll('</h1>', ' ').replaceAll('<h2>', ' ').replaceAll('</h2>', ' ')
.replaceAll('</h3>', ' ').replaceAll('<h3>', ' ').replaceAll('</blockquote>', ' ').replaceAll('<blockquote>', ' ')
.replaceAll('</br>', ' ').replaceAll('<br>', ' ').replace(/<a href=".*?>(.*?)<\/a>/, '$1');


    return (
        <>
        {((props.page === 'allIdeaNecessity' || props.page==='allMyIdeaNecessity' || props.page==='allFavoriteIdeaNecessity')
        && view==='show')?
        <li className={classname} value={props.id} key = {props.key}>
            <div className="type-ideaNecessity">
                {type ? <FaRegLightbulb size={"40%"} className="simbol-idea" color="#090446" /> :
                <FaHandsHelping size={"70%"} className="simbol-necessity" color="#FEB95F" />}
            </div>
            <div className="content-ideaNecessity">
                <Title className="title-ideaNecessity" title={props.title} />
                {/*<div className="description-ideaNecessity" dangerouslySetInnerHTML={{ __html: props.description}}/>*/}
                <span className="description-ideaNecessity">{description}</span>
                <div className="info-ideaNecessity">
                {userType !== "VISITOR"?<>
                    <Text className="author-ideaNecessity" text={'Author:'} />
                    <Button className="authorbtn-ideaNecessity" tooltipText={"See user profile"} text={props.author} onclick={(event) => { navigate('/userProfile/' + props.idAuthor)}} />
                    </>:<>
                    <Text className="author-ideaNecessity" text={'Author: '} />
                    <Text className="dataPrint-ideaNecessity" text={props.author} /></>}
                    <MdHowToVote size={"1em"} color="green" className="triangule" />
                    <Text className="vote-ideaNecessity" text={vote} />
                    <Text className="date2-ideaNecessity" text={'Created on:'} />
                    <Text className="dataPrint-ideaNecessity" text={creationTimeEnd} />
                    <Text className="date1-ideaNecessity" text={'Update on:'} />
                    <Text className="dataPrint-ideaNecessity" text={updateTimeEnd} />
                </div>
            </div>

            <div className="action-ideaNecessity">
                {(props.page === 'allIdeaNecessity' || props.page==='allFavoriteIdeaNecessity')?
                <div className="action">
                {(props.page==='allIdeaNecessity'&&props.deletedIdeaNecessity==false || props.page==='allFavoriteIdeaNecessity')?
                <>{btnFavorite}</>:''}
                <Button className='see-ideaNecessity' id={props.id} tooltipText={"View the full content"} text={<FaRegComment size={"100%"} id={props.id} color="#C01111" />} onclick={(event) => { seeIdeaNecessity(event) }} />
                </div>
                :<div>{(props.page==='allMyIdeaNecessity'&&deletedIdeaNecessity==false)?
                    <>
                    <Button className='see-myIdeaNecessity' id={props.id} text={<BiEditAlt size={"100%"} id={props.id} color="#142E56" />} onclick={(event) => {navigate('/editIdeaNecessity/' + props.id)}} />
                    <Button className='see-myIdeaNecessity' id={props.id} text={<BiLinkAlt size={"100%"} id={props.id} color="#142E56" />} onclick={(event) => {navigate('/postRelatedMyIdeaNecessity/' + props.id) }} />
                    <Button className='see-myIdeaNecessity' id={props.id} text={<BiTrash size={"100%"} id={props.id} color="#142E56" />} onclick={(event) => {deleteIdeaNecessity(true)}} />
                    </>
                    :<Button className='see-myIdeaNecessity2' id={props.id} text={<FaTrashRestoreAlt size={"100%"} id={props.id} color="#142E56" />} onclick={(event) => {deleteIdeaNecessity(false)}} />}
                </div>}
            </div>
        </li>
        :
        <>
        {(view==='show')?
        <li className={classname} value={props.id} key = {props.key}> 
        <div className="type-ideaNecessity">
            {type ? <FaRegLightbulb size={"40%"} className="simbol-idea" color="#090446" /> :
                <FaHandsHelping size={"70%"} className="simbol-necessity" color="#FEB95F" />}
        </div>
        <div className="content-ideaNecessity">
            <Title className="title-ideaNecessity" title={props.title} />
            <div className="div-authorJustification">
                <Text className="authorJustification-legendIdeaNecessity" text={"Linked by: "} />
                <Text className="authorJustification-ideaNecessity" text={props.authorJustification} />
            </div>
            <div className="div-descriptionJustification">
                <Text className="justification-ideaNecessity" text={"Justification: "} />
                {/*<div className="description-ideaNecessity" dangerouslySetInnerHTML={{ __html: props.description}}/>*/}
                <span className="description-ideaNecessity">{description}</span>
            </div>
        </div>
        <div className="action-ideaNecessity">
            {(props.page=='justificationMyIdeaNecessity')?
            <Button className='delete-AssIdeaNecessity' id={props.idAss} text={<BiTrash size={"100%"} id={props.idAss} color="black" />} onclick={(event) => {desassociateIdeaNecessity(event)}} />
            :<>
            <Text className="authorJ-ideaNecessity" text={'Author:'} />
            <Button className="authorbtn-ideaNecessity" tooltipText={"See user profile"} text={props.author} onclick={(event) => { navigate('/userProfile/' + props.idAuthor)}} />
            <Button className="see-more" id={props.idAss} text={"see more"} onclick={(event) => { navigate('/commentIdeaNecessity/' + props.idAss) }}/>
            </>}
        </div>
        </li>
    :""}</>
    }
    </>
    );
};