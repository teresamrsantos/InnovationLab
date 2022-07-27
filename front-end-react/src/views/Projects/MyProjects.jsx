import React, { useEffect, useState } from "react";
import { GiSuitcase } from "react-icons/gi";
import { MdOutlineHideSource } from "react-icons/md";
import { Link, useNavigate } from "react-router-dom";
import CircleLoader from "../../components/layout/CircleLoader";
import Filter from "../../components/layout/Filter";
import Title from "../../components/layout/Title";
import Sidebar from "../../components/menu/Sidebar";
import { acceptInviteToBePartOfProject, approveMemberOrNotToBePartOfProject, getAllProjectsMemberFromUserAPI, getUserInfoAPI } from "../../restApi";
import ListProjects from "./ListProjects";
import './MyProjects.css';
import ProjectSquare from "./ProjectSquare";

export default function MyProjects(props) {
    let [showInfoProject, setShowInfoProject] = useState(0);
    const token = sessionStorage.getItem("token");
    let [userInfo, setUserInfo] = useState('');
    let [projectsUserIsMember, setProjectsUserIsMember] = useState('projectsUserIsMember');
    let [projectsUserWASMember, setProjectsUserWASMember] = useState('projectsUserWASMember');
    let [projectRequestsUser, setProjectRequestsUser] = useState('projectRequestsUser');
    let [projectInvitesUser, setProjectInvitesUser] = useState('projectInvitesUser');
    let [filterProjects, setFilterProjects] = useState("");
    let [interest, setInterest] = useState([]);
    let [ideaNecessity, setIdeaNecessity] = useState([]);
    let [skill, setSkill] = useState([]);
    var userType = userInfo.userType;
    const idUser = userInfo.idUser;
    const navigate = useNavigate();


    useEffect(() => {
        if (sessionStorage.getItem("token") === null) {
            navigate("/")
        }
    }, []);


    useEffect(() => {
        getUserInfoAPI(
            token,
            (usersInfo) => {
                setUserInfo(usersInfo)
                if (usersInfo.userType === 'VISITOR') {
                    navigate("/Home")
                }
            }, () => { sessionStorage.clear(); navigate("/") });
    }, []);


    function changeSeparators(e) {

        if (e.target.id === 'setInfoProject-0') {
            setShowInfoProject(0)
            document.getElementById('setInfoProject-0').classList.add('activatedSeparator')
            if (document.getElementById('setInfoProject-1').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoProject-1').classList.remove('activatedSeparator')
            }
            if (document.getElementById('setInfoProject-2').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoProject-2').classList.remove('activatedSeparator')
            }
        } else if (e.target.id === 'setInfoProject-1') {
            setShowInfoProject(1)
            document.getElementById('setInfoProject-1').classList.add('activatedSeparator')
            if (document.getElementById('setInfoProject-0').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoProject-0').classList.remove('activatedSeparator')
            }
            if (document.getElementById('setInfoProject-2').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoProject-2').classList.remove('activatedSeparator')
            }
        } else if (e.target.id === 'setInfoProject-2') {
            setShowInfoProject(2)
            document.getElementById('setInfoProject-2').classList.add('activatedSeparator')
            if (document.getElementById('setInfoProject-0').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoProject-0').classList.remove('activatedSeparator')
            }
            if (document.getElementById('setInfoProject-1').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoProject-1').classList.remove('activatedSeparator')
            }
        }
    }

    useEffect(() => {
        if (userInfo !== undefined && userInfo !== '') {

            getAllProjectsMemberFromUserAPI(
                token, userInfo.idUser,

                (projectInfo) => {
                    setProjectsUserIsMember(projectInfo.filter(project => (project.membersList.some(e => (e.idMember === userInfo.idUser && e.role === 'ADMIN') || (e.idMember === userInfo.idUser && e.role === 'MEMBER'))) && project.projectStatus === 'INPROGRESS'))
                    setProjectsUserWASMember(projectInfo.filter(
                        project =>
                            (project.membersList.some(e => ((e.idMember === userInfo.idUser && e.role === 'NOTPARTICIPATINGANYMORE'))) && project.projectStatus === 'INPROGRESS') ||
                            (project.membersList.some(e => (e.idMember === userInfo.idUser && e.role === 'ADMIN') || (e.idMember === userInfo.idUser && e.role === 'MEMBER'))) && project.projectStatus === 'CONCLUDED'))
                    setProjectRequestsUser(projectInfo.filter(project => (project.membersList.some(e => e.idMember === userInfo.idUser && e.role === 'REQUEST') && project.projectStatus === 'INPROGRESS')))
                    setProjectInvitesUser(projectInfo.filter(project => (project.membersList.some(e => e.idMember === userInfo.idUser && e.role === 'INVITE') && project.projectStatus === 'INPROGRESS')))

                },
                (error) => {
                    console.log(error)
                }
            );
        }
    }, [userInfo])

    const sort_lists_decrescent = (key, list) => [...list].sort((b, a) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))
    const sort_lists_crescent = (key, list) => [...list].sort((a, b) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))

    function orderBy(event) {
        var orderselect = event.value;
        var orderselectAux = orderselect.split("-")
        let newSortedList = [];

        if (orderselectAux[1] === '1') {
            newSortedList = sort_lists_crescent(orderselectAux[0], projectsUserIsMember)

        } else {
            newSortedList = sort_lists_decrescent(orderselectAux[0], projectsUserIsMember)
        }
        setProjectsUserIsMember(newSortedList)
    }

    function resetListProjects() {
        getAllProjectsMemberFromUserAPI(token, userInfo.idUser, (projectInfo) => {

            setProjectsUserIsMember(projectInfo.filter(project => (project.membersList.some(e => (e.idMember === userInfo.idUser && e.role === 'ADMIN') || (e.idMember === userInfo.idUser && e.role === 'MEMBER'))) && project.projectStatus === 'INPROGRESS'))
            setProjectsUserWASMember(projectInfo.filter(
                project =>
                    (project.membersList.some(e => ((e.idMember === userInfo.idUser && e.role === 'NOTPARTICIPATINGANYMORE'))) && project.projectStatus === 'INPROGRESS') ||
                    (project.membersList.some(e => (e.idMember === userInfo.idUser && e.role === 'ADMIN') || (e.idMember === userInfo.idUser && e.role === 'MEMBER'))) && project.projectStatus === 'CONCLUDED'))

        }, (e) => { console.log(e) })
    }

    function filterProjectsBy(event) {
        setFilterProjects(event.value)
    }


    function interestSelect(e) {
        let array = [];
        if (Array.isArray(e)) {
            e.map(x => {
                array.push(x.id)
            })
        }
        setInterest(array);
    }

    function skillSelect(e) {
        let array = [];
        if (Array.isArray(e)) {
            e.map(x => {
                array.push(x.id)
            })
        }
        setSkill(array);
    }


    function ideaNecessitySelect(e) {
        let array = [];
        if (Array.isArray(e)) {
            e.map(x => {
                array.push(x.id)
            })
        }
        setIdeaNecessity(array);
    }


    function deleteRequest(e) {
        var idProject = (e.target.parentElement.parentElement.id).split('/')[0];

        var myJson = JSON.stringify({
            idUser: (userInfo.idUser).toString(),
            approved: false
        })

        var id = idProject + '/request'
        approveMemberOrNotToBePartOfProject(token, idProject, myJson, (e) => document.getElementById(id)?.remove(), (e) => console.log(e))

    }

    function acceptBeingAMember(acceptOrNot, e,) {
        var idProject = e.target.parentElement.parentElement.id.split('/')[0];

        acceptInviteToBePartOfProject(token, idProject, acceptOrNot, () => {
            if (acceptOrNot === true) {
                setUserInfo(userInfo => ({ ...userInfo, numberOfActiveProjects: 1 }));
                setShowInfoProject(0)

                //  document.getElementById(id).remove();
            } else {
                var id = idProject + '/invite'
                document.getElementById(id).remove();
            }
        }, (e) => console.log(e))
    }

    function removeActiveProject() {
        setUserInfo(userInfo => ({ ...userInfo, numberOfActiveProjects: 0 }))
    }

    return (
        <div>
            <Sidebar />
            <div className="myProjetctRectangle">
                <div className="separatorButtonsDIVMyProjects">
                    <button id='setInfoProject-0' className="projectDetailsButton activatedSeparator" onClick={(e) => (changeSeparators(e))}> Show Projects Where I am a member </button>
                    <button className='projectDetailsButton' id='setInfoProject-1' onClick={(e) => (changeSeparators(e))} style={{ display: userInfo.numberOfActiveProjects === 0 ? "block" : "none" }} >  Show pending invites </button>
                    <button className='projectDetailsButton' id='setInfoProject-2' onClick={(e) => (changeSeparators(e))} style={{ display: userInfo.numberOfActiveProjects === 0 ? "block" : "none" }}>  Show pending requests </button>
                </div>
                <div className='showYourProjects' style={{ display: showInfoProject === 0 ? "block" : "none" }}>
                    <div className='projectsHeader'>
                        {(projectsUserIsMember === 'projectsUserIsMember') ? '' : projectsUserIsMember.length > 0 ?
                            <Title title='Your Active Project:' className='project-title'> </Title> :
                            <div className='myProjects-NOACTIVEPROJECTS'>
                                <Title title='Your Projects:' className='project-title'> </Title>
                                <div className="no-ACTIVEPROJECTS">

                                    <Title title="You are not a member of any project in progress" />
                                </div> </div>
                        }</div>
                    <div className="activeProject">
                        {(projectsUserIsMember === 'projectsUserIsMember') ? '' : projectsUserIsMember.length > 0 ?
                            projectsUserIsMember.map(projectInfo =>
                                <ProjectSquare
                                    key={projectInfo.idProject}
                                    page={'myProjects'}
                                    pictureProject={projectInfo.imageProject}
                                    idUser={userInfo.id}
                                    id={projectInfo.idProject}
                                    title={projectInfo.title}
                                    idAuthor={projectInfo.userDTOJoinProject.idUser}
                                    author={projectInfo.userDTOJoinProject.username}
                                    creationDate={projectInfo.creationTime}
                                    numberMaxMembers={projectInfo.numberMaxMembers}
                                    numberOfMembers={projectInfo.numberOfMembers}
                                    membersList={projectInfo.membersList}
                                    className={projectInfo.projectStatus === 'CONCLUDED' ? 'concluded' : 'inprogress'}
                                    favorite={(projectInfo.userListFavorites.includes(userInfo.id)) ? true : false}
                                    removeActiveProject={removeActiveProject}
                                ></ProjectSquare>) : ''

                        }  </div>
                    <div className="list-projects">
                        <div className="titleWASprojectsDIV">
                            <Title title='Projects you were a member:' className='project-title'> </Title>
                        </div>
                        <div className='filterProjectDiv'>
                            <Filter page='myProjects' classname='filterProjects' orderBy={orderBy} interestSelect={interestSelect} ideaNecessitySelect={ideaNecessitySelect} skillSelect={skillSelect} filterProjectsBy={filterProjectsBy} /></div>
                        {(projectsUserWASMember === 'projectsUserWASMember') ? <div className="div-loading">
                            <CircleLoader />
                            <Title title="Loading..." /> </div> : projectsUserWASMember.length > 0 ? <ListProjects page='myProjects' resetListProject={resetListProjects} testinformation={projectsUserWASMember} concludedInProgress={filterProjects}
                                interest={interest} skill={skill} ideaNecessity={ideaNecessity} idUser={idUser} userType={userType} filterProjectsBy={filterProjectsBy} removeActiveProject={removeActiveProject} /> : <div className="no-allIdeaNecessity">
                            <MdOutlineHideSource size={"20%"} color={"gray"} />
                            <Title title="You were not a member of any project, yet..." />
                        </div>}

                    </div>
                </div>
                <div className='showYourInvites' style={{ display: showInfoProject === 1 ? "block" : "none" }}>
                    <div className='invitesProject'>
                        <div className='projectsHeader'>
                            <Title title='Your Invites' className='project-title'> </Title>
                        </div>
                        <div className='projectsInvitedTitle'>
                            <span> Project's you were invited to join: </span>

                        </div>

                        <div>
                            <ul className='ListOfInvites'>
                                {projectInvitesUser !== 'projectInvitesUser' && projectInvitesUser.length > 0 ? projectInvitesUser.map(project =>
                                    project.numberVacancies == 0 ? '' :
                                        <li className='eachInvite' key={project.idProject} id={project.idProject + '/invite'}>

                                            <Link className='linkInvite' to={"/projectDetails/" + project.idProject} >{project.title}  </Link>
                                            {userInfo.numberOfActiveProjects === 0 ?

                                                <div className='ButtonsEachInviteDIV'>
                                                    <button className='btnEachInvite acceptBtn' onClick={(e) => acceptBeingAMember(true, e, project)}> Accept </button>
                                                    <button className='btnEachInvite declineBtn' onClick={(e) => acceptBeingAMember(false, e, project)}> Decline </button>
                                                </div> : ''}

                                        </li>
                                ) :
                                    <div className="no-allIdeaNecessity">
                                        <MdOutlineHideSource size={"20%"} color={"gray"} />
                                        <Title title="You weren't invited to any project." />
                                    </div>}</ul>
                        </div>


                    </div>
                </div>
                <div className='showYourRequests' style={{ display: showInfoProject === 2 ? "block" : "none" }}>
                    <div className='requestProject'>
                        <div className='projectsHeader'>
                            <Title title='Your Requests' className='project-title'> </Title>
                        </div>
                        <div className='projectsInvitedTitle'>
                            <span> Project's you asked to be a member: </span>

                        </div>

                        <div>
                            <ul className='listOfRequests'>

                                {projectRequestsUser !== 'projectRequestsUser' && projectRequestsUser.length > 0 ? projectRequestsUser.map(project =>
                                    <li className='eachInvite' key={project.idProject} id={project.idProject + '/request'}>
                                        <Link className='linkInvite' to={"/projectDetails/" + project.idProject} >{project.title}  </Link>
                                        {userInfo.numberOfActiveProjects === 0 ?

                                            <div className='ButtonsEachInviteDIV'>
                                                <button className='btnEachInvite acceptBtn' onClick={(e) => deleteRequest(e)}> Delete Request </button>
                                            </div> : ''}

                                    </li>
                                ) : <div className="no-allIdeaNecessity">
                                    <MdOutlineHideSource size={"20%"} color={"gray"} />
                                    <Title title="You have not requested to be a part of any project." />
                                </div>
                                }</ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    )
}

