import React, { useState } from "react";
import * as FaIcons from 'react-icons/fa';
import * as BSIcons from 'react-icons/bs';
import NoPictureProject from '../../images/noproject.png'
import { removeProjectFavoritesAPI, addProjectFavoritesAPI, removeMemberProject, changeParticipationAPI, markProjectAsConcludedAPI } from '../../restApi'
import './ProjectSquare.css'
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
export default (props) => {
    const navigate = useNavigate();
    const token = sessionStorage.getItem('token');
    let [show, setShow] = useState(true)
    let pictureProject = props.pictureProject === null ? NoPictureProject : props.pictureProject;
    let title = props.title;
    let author = 'Author: ' + props.author;
    let creationDate = props.creationDate;
    let numberMaxMembers = props.numberMaxMembers;
    let numberOfMembers = props.numberOfMembers;
    let idProject = props.id;
    let [favorite, setFavorite] = useState(props.favorite)

    let [leaveProjectVariable, setLeaveProject] = useState(false);
    const creationTime = new Date(creationDate);
    let creationTimeEnd = creationTime.getDate() + "/" + (creationTime.getMonth() + 1) + "/" + creationTime.getFullYear() +
        " " + creationTime.getHours() + ":" + creationTime.getMinutes();

    let idUser = props.idUser;
    if (props.page === 'myProjects') {

        var roleUser = props.membersList.filter(member => (member.idMember === idUser))[0].role
    }



    function addProjectFavorite(e) {

        setFavorite(true)
        addProjectFavoritesAPI(token, idProject, (e) => {
            setFavorite(true); 
        }, (e) => {
            setFavorite(false)
        })
    }


    function removeProjectFavorite(e) {

        if (props.page === 'allFavoriteProjects') {
            props.resetSize()
            removeProjectFavoritesAPI(token, idProject, (e) => {
                setShow(false); 
            }, (e) => {
                setShow(true)
            })

        } else {

            removeProjectFavoritesAPI(token, idProject, (e) => {
                setFavorite(false)
            }, (e) => {
                setFavorite(true)
            })
        }


    }


    function leaveProjectForReal(e) {

        changeParticipationAPI(token, idProject, idUser, (e) => {
            props.removeActiveProject(e); setLeaveProject(false)
        }, (e) => console.log(e))

    }

    var classNameLI = 'divEachProject ' + props.className 
    var idLi = props.id + '/projectLi'



    return (
        <div style={{ display: show ? "block" : "none" }}>

            {props.page === 'allFavoriteProjects' || props.page === 'allProjects' ?
                <li className={classNameLI} id={idLi} key={idLi} >

                    <div className='projectTitleAndButtons_ProjectSquare'>

                        <div className='projectTitle_ProjectSquare'>
                            <h4 className="nameEachProject">{title.substring(0, 28)}</h4></div>

                        <div className="favoriteDetailsButton_ProjectSquare">
                            <div className='btn_Favorite'> {favorite ?
                                <button className='btn-favorite-project' onClick={removeProjectFavorite}> <FaIcons.FaHeart size='30px' color="C01111"   /></button> :
                                <button className='btn-favorite-project' onClick={addProjectFavorite}> <FaIcons.FaRegHeart size='30px' color="C01111"  /></button>}

                            </div>
                            <button className='btn-navigate-details-project' onClick={(e) => { navigate('/projectDetails/' + props.id) }} ><  BSIcons.BsThreeDotsVertical size='30px' onClick={(e) => { navigate('/projectDetails/' + props.id) }} /> </button>
                        </div>
                    </div>

                    <div className='creationDateEachProjectDIV'>
                        <span className='creationDateEachProject'>{creationTimeEnd}  {props.className === 'concluded' ? '- Project Concluded' : ''}</span>
                    </div>
                    <div className='pictureProject_DIV'>
                        <img src={pictureProject} className='profileImgEachProject' alt="" />
                    </div>
                    <div className="projectFooter">


                        <div className='authorProject_nameDIV'>
                            <span className="usernameAuthorEachProject" onClick={(event) => { navigate('/userProfile/' + props.idAuthor) }}>{author}</span>
                        </div>


                        <>
                            <div className='btns_admin_vacanciesEachProject' >
                                <h5 className='vacanciesEachProject'>  <FaIcons.FaUsers onClick={e => e.stopPropagation()} />  {numberOfMembers} / {numberMaxMembers}</h5>
                            </div>

                        </>




                    </div>


                </li> : props.page === 'myProjects' ?

                    <>
                        <li className={classNameLI} id={idLi}  key={idLi}>

                            <div style={{ display: leaveProjectVariable ? "none" : "block" }}>
                                <div className='projectTitleAndButtons_ProjectSquare'>

                                    <div className='projectTitle_ProjectSquare'>
                                        <h4 className="nameEachProject">{title.substring(0, 28)}</h4></div>

                                    <div className="favoriteDetailsButton_ProjectSquare">

                                        <button className='btn-navigate-details-project' onClick={(e) => { navigate('/projectDetails/' + props.id) }} ><  BSIcons.BsThreeDotsVertical size='30px' onClick={(e) => { navigate('/projectDetails/' + props.id) }} /> </button>
                                    </div>
                                </div>
                                <div className='creationDateEachProjectDIV'>
                                    <span className='creationDateEachProject'>{creationTimeEnd} {props.className === 'concluded' ? 'Project Concluded' : ''}</span>
                                </div>
                                <img src={pictureProject} className='profileImgEachProject' alt="" />
                                <div className="projectFooter">
                                    <div className='authorProject_nameDIV'>
                                        <span className="usernameAuthorEachProject" onClick={(event) => { navigate('/userProfile/' + props.idAuthor) }}>{author}</span>
                                    </div>

                                    {props.className == 'concluded' ? '' : roleUser === 'ADMIN' ?
                                        <div className='adminButtonsMyProject'>

                                            <button className='buttonMyProjectsEdit' onClick={(e) => { navigate('/editProject/' + props.id); }}>  <FaIcons.FaEdit size={'18px'} onClick={(e) => { navigate('/editProject/' + props.id); }} /> </button>

                                            <button className='buttonMyProjectsEdit-ManageUsers' onClick={(e) => { navigate('/manageUsersProject/' + props.id); }} >
                                                <FaIcons.FaUserEdit size={'18px'} onClick={(e) => { navigate('/manageUsersProject/' + props.id); }} /> </button>


                                        </div> : roleUser === 'MEMBER' ?
                                        <button className='buttonMyProjectsEdit-LeaveProject' onClick={(e) => setLeaveProject(true)}> Leave Project </button> : ''
                                    }
                                </div>
                            </div>
                            <div style={{ display: leaveProjectVariable ? "block" : "none" }} className='leaveTheProjectDiv'>
                                <p> Are you sure you want to leave the Project ?</p>
                                <button className='buttonLeaveProject' onClick={(e) => leaveProjectForReal(e)}> Yes </button>
                                <button className='buttonLeaveProject' onClick={(e) => setLeaveProject(false)}> Cancel </button>
                                <p> You will only be able to join the project again if accepted by the admin </p>
                            </div>

                        </li>
                    </>

                    : ''}
        </div>

    )
}


