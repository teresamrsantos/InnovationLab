import { useEffect, useState } from "react";
import { BiTrash } from "react-icons/bi";
import { useNavigate, useParams } from "react-router-dom";
import Alert from "../../components/layout/Alert";
import LineUser from "../../components/layout/LineUser";
import Text from "../../components/layout/Text";
import Title from "../../components/layout/Title";
import Sidebar from "../../components/menu/Sidebar";
import picture1 from "../../images/TITANS1.png";
import picture2 from "../../images/TITANS2.png";
import picture3 from "../../images/TITANS3.png";
import CircleLoader from "../../components/layout/CircleLoader";
import {
    approveMemberOrNotToBePartOfProject,
    changeMemberTypeProject_API, deleteParticipationAPI, findAllMembersOfProject, findAllProjectRequestsPending_api, findAllProjectsRequestsPendingAPI, findAllUsersNotParticipatingInProject_API, getSpecificProjectAPI, getUserInfoAPI, inviteToJoinProject_Api, removeMemberProject_API
} from "../../restApi";
import './ManageUsersProject.css';

export default function ManageUsersProjects(props) {
    const navigate = useNavigate();
    const [showInfoProject, setShowInfoProject] = useState(0);
    const { id } = useParams();
    let [listRequests, setListRequests] = useState('');
    let [listMember, setListMembers] = useState('');
    let [showAtLeastOneAdmin, setShowAtLeastOneAdmin] = useState(false);
    const token = sessionStorage.getItem('token');
    let [userInfo, setUserInfo] = useState('');
    let [allUser, setAllUser] = useState('');
    let [userSearch, setUserSearch] = useState('');
    let [invitesPending, setInvitesPending] = useState('');
    let [userTypeProject, setUserTypeProject] = useState('');
    let [userTypeGeneral, setUserTypeGeneral] = useState('');
    let [maxNumberMembers, setMaxNumberMembers] = useState('');
    const images = [
        picture1,
        picture2,
        picture3,
    ];


    useEffect(() => {
        getUserInfoAPI(
            token,
            (usersInfo) => {
                setUserInfo(usersInfo)
                setUserTypeGeneral(usersInfo.userType);
            },
            (e) => {
                console.log(e)
            }
        );

        getSpecificProjectAPI(token, id, (e) => {
            setMaxNumberMembers(e.numberMaxMembers)
        }, (e) => {
   
        })


    }, []);


    useEffect(() => {
        findAllUsersNotParticipatingInProject_API(token, id, (e) => {
    
            setAllUser(e)
        },
            (error) => {
                console.log(error)
            });

    }, []);


    function resetAllUsersList() {
        findAllUsersNotParticipatingInProject_API(token, id, (e) => {
           setAllUser(e)
        },
            (error) => {
                console.log(error)
            });
    }

    useEffect(() => {
        findAllProjectRequestsPending_api(
            token, id,
            (invites) => {

                setInvitesPending(invites)
            },
            (error) => {

            }
        );
    }, []);



    useEffect(() => {
        findAllProjectsRequestsPendingAPI(
            token, id,
            (ideaNecessitiesList) => {
                setListRequests(ideaNecessitiesList)

            }, (error) => {
                console.log(error)
            }
        );
    }, []);


    useEffect(() => {
        findAllMembersOfProject(
            token, id,
            (membersList) => {
                setListMembers(membersList)

                for (var i = 0; i < membersList.length; i++) {
                    if (membersList[i].idMember == userInfo.idUser) {
                        setUserTypeProject(membersList[i].role)
                    }
                }
            }, (error) => {
                console.log(error)
            }
        );
    }, []);



    function acceptRequestOrNot(acceptOrNot, e) {
        setShowAtLeastOneAdmin(false)
        var idUser = (e.target.id).split('/')[0];
        var myJson = JSON.stringify({
            idUser: idUser.toString(),
            approved: acceptOrNot
        })


        var idToRemove = idUser + '/requestLi'
        approveMemberOrNotToBePartOfProject(token, id, myJson, (e) => {
            let array = listRequests;
            for (var i = 0; i < array.length; i++) {
                if (array[i].idUser == idUser) {
                    array.splice(i, 1);
                    setListRequests(array)
                }
            }
            //document.getElementById(idToRemove)?.remove();

            findAllMembersOfProject(
                token, id,
                (membersList) => {
                    setListMembers(membersList)

                }, (error) => {
                    console.log(error)
                }
            );

        }, (e) => console.log(e))
    }

    function inviteUserToProject(e) {


        inviteToJoinProject_Api(token, id, e.target.parentElement.id, (e) => {
            setInvitesPending(e);
            resetAllUsersList();
        }, (e) => { console.log(e) }
        )

    }

    useEffect(() => {

        if (listMember !== '' && userInfo.id !== undefined && userInfo.userType!=='ADMIN') {
            listMember.some(member => member.idMember === userInfo.id && member.role === 'MEMBER') ?
                navigate("/myprojects") : console.log('');

            listMember.some(member => member.idMember === userInfo.id) ? console.log('') : navigate("/myprojects");
        }

    }, [listMember]);


    function changeUserRole(e) {

        if (listMember.filter(member => member.role === 'ADMIN').length === 1 && e.target.value !== 'ADMIN') {
            setShowAtLeastOneAdmin(true)
            setTimeout(() => { setShowAtLeastOneAdmin(false) }, 3000)
            return
        }
        var idUser = (e.target.id).split('/')[0];
        var participationDTO = JSON.stringify(
            {
                idMember: idUser,
                role: e.target.value
            }
        )
        changeMemberTypeProject_API(token, id, participationDTO, (e) => {
            setListMembers(e)


        }, (e) => { console.log(e) })
    }

    function removeUser(e) {

        var idLi = e.target.parentElement.id;
        var idUser = e.target.parentElement.id.split('/')[0]

        var role = e.target.parentElement.firstChild.firstChild.value;

        if (listMember.filter(member => member.role === 'ADMIN').length === 1 && role === 'ADMIN') {
            setShowAtLeastOneAdmin(true)
            setTimeout(() => { setShowAtLeastOneAdmin(false) }, 3000)
            return
        }
        removeMemberProject_API(token, id, idUser, (e) => {
            setListMembers(e)
            resetAllUsersList();
        }, (e) => { console.log(e) })

        resetAllUsersList();
    }



    function deleteInvite(e) {

        var idLi = e.target.id + 'Li';
        var idUser = e.target.id.split('/')[0];
        deleteParticipationAPI(token, id, idUser, (e) => {
            /* document.getElementById(idLi).remove();*/
            let array = invitesPending;
            for (var i = 0; i < array.length; i++) {
                if (array[i].idUser == idUser) {
                    array.splice(i, 1);
                    setInvitesPending(array)
                    resetAllUsersList();
                }
            }
        })
    }
    function changeSeparators(e) {


        if (e.target.id === 'setInfoEditProject-0') {
            setShowInfoProject(0)
            document.getElementById('setInfoEditProject-0').classList.add('activatedSeparator')
            if (document.getElementById('setInfoEditProject-1').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoEditProject-1').classList.remove('activatedSeparator')
            }

        } else if (e.target.id === 'setInfoEditProject-1') {
            setShowInfoProject(1)
            document.getElementById('setInfoEditProject-1').classList.add('activatedSeparator')
            if (document.getElementById('setInfoEditProject-0').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoEditProject-0').classList.remove('activatedSeparator')
            }

        }
    }

    return (
        <div>
            <Sidebar></Sidebar>
            <div className="rectangleManageUsersProject">
                <div className="oneAdminDiv" style={{ display: showAtLeastOneAdmin ? "block" : "none" }}> <Alert className={"alert-warning"} text={'You have to have at least one Admin for your project'} /></div>
                <div className="separatorButtonsDIV_EditProjects">
                    <button id='setInfoEditProject-0' className="projectDetailsButton activatedSeparator" onClick={(e) => (changeSeparators(e))}> Manage Users & Requests </button>
                    <button className='projectDetailsButton' id='setInfoEditProject-1' onClick={(e) => (changeSeparators(e))}> Invite Users </button>
                </div>

                <div className="containerManageUsersProject" style={{ display: showInfoProject == 0 ? "flex" : "none" }}>
                    <Title title='Manage Users from your project' className='project-title'> </Title>
                    <div className='manageUsersAcceptUsersDIV'>
                        <div className="manageUsers">
                            <ul className="ul-membersRequest">
                                <div><p> Number of members: {listMember.length} / {maxNumberMembers} </p></div>

                                {listMember !== undefined && listMember.length > 0 ? listMember.map((participation) =>
                                    <li className='liUserRole' id={participation.idMember + '/member'}>

                                        <div className="img-user-requests">
                                            <img className='picture-user-requests' src={participation.pictureUser != null ? participation.pictureUser : images[Math.floor(Math.random() * images.length)]} />
                                        </div>
                                        <div className="info-user-ManageUsersProject">
                                            <Text className="name-user-ManageUsersProject" id={participation.idMember + '/memberName'} text={participation.usernameMember} />

                                        </div>

                                        <div className="DIV-select-role-ManageUsersProject" id={participation.idMember + '/member'} >
                                            <div className="select-roleUserDIV">
                                                <select className="select-roleUser" value={participation.role} id={participation.idMember + '/member'} onChange={(e) => { changeUserRole(e) }} >
                                                    <option value="ADMIN">Admin</option>
                                                    <option value="MEMBER">Member</option>
                                                </select></div>
                                            <button className="btn-removeUser-ManageUsersProject" onClick={(e) => { removeUser(e) }}> <BiTrash size={"1em"} id={props.id} color="#000" onClick={(e) => { e.stopPropagation() }} /> </button>
                                        </div>

                                    </li>
                                ) : <div className="div-loading">
                                    <CircleLoader />
                                    <Title title="Loading..." />
                                </div>}
                            </ul></div>
                        <div className="acceptUsers">
                            <Title title='Accept users requests:' className='title-manageUsersProject'> </Title>
                            <ul className="ul-membersRequest">
                                {listRequests !== undefined && listRequests.length > 0 ? listRequests.map((request) =>
                                    <li className='liUserInvited' id={request.idUser + '/requestLi'}>

                                        <div className="img-user-requests">
                                            <img className='picture-user-requests' src={request.pictureUrl} />
                                        </div>
                                        <div className="info-user-ManageUsersProject">
                                            <Text className="name-user-ManageUsersProject" id={request.idUser + '/requestName'} text={request.firstName + ' ' + request.lastName + ' | ' + request.username} />

                                        </div>

                                        <div className="DIV-btn-user-ManageUsersProject">
                                            {maxNumberMembers === listMember.length ? '' : <><button className="btn-accept-manageUsersProject" id={request.idUser + '/request'} onClick={(e) => { acceptRequestOrNot(true, e); }}>Accept</button></>} <>
                                                <button className="btn-decline-manageUsersProject" id={request.idUser + '/request'} onClick={(e) => { acceptRequestOrNot(false, e); }}>Decline</button></>

                                        </div>

                                    </li>
                                ) : 'No request have been made to participate on this project'}
                            </ul></div>
                    </div>
                </div>

                <div className="inviteUsersPendingInvitesDIV" id='div1' style={{ display: showInfoProject == 1 ? "flex" : "none" }}>
                    <Title title='Invite Users to your Project' className='project-title'> </Title>
                    <div className="inviteUsersDIV">
                        <div className="inviteUsersProject">
                            <div className="inviteUsersInput">
                                <label htmlFor="" className="searUserLabelManageUsers">Search user: </label>
                                {maxNumberMembers === listMember.length ? <p className="warningAllVacanciesFilled">All the vacancies are filled in this project</p> :
                                    <input className="search-usernameEmail_manageUser" name="search" placeholder="Search by name/username" onChange={(e) => { setUserSearch(e.target.value) }} />}
                            </div>
                            <div className="list-userToTalk_privacy">
                                <ul >{userSearch.length > 0 ?
                                    allUser !== '' && allUser.length > 0 ?
                                        invitesPending.length > 0 ?
                                            allUser.filter(x => !invitesPending.filter(y => y.id === x.id).length).filter(
                                                person => person.username.toLowerCase().includes(userSearch.toLowerCase()) ||
                                                    (person.firstName.toLowerCase() + " " + person.lastName.toLowerCase()).includes(userSearch.toLowerCase()))
                                                .map((user) =>
                                                    <LineUser
                                                        page='privacy'
                                                        idUserAuth={userInfo.id}
                                                        idUser={user.id}
                                                        pictureUrl={user.pictureUrl != null ? user.pictureUrl : images[Math.floor(Math.random() * images.length)]}
                                                        firstName={user.firstName}
                                                        lastName={user.lastName}
                                                        username={user.username}
                                                        button={'+'}
                                                        action={inviteUserToProject}
                                                        className={'lineUserPrivacy'}
                                                    />) : allUser.filter(
                                                        person => person.username.toLowerCase().includes(userSearch.toLowerCase()) ||
                                                            (person.firstName.toLowerCase() + " " + person.lastName.toLowerCase()).includes(userSearch.toLowerCase()))
                                                        .map((user) =>
                                                            <LineUser
                                                                page='privacy'
                                                                idUserAuth={userInfo.id}
                                                                idUser={user.id}
                                                                pictureUrl={user.pictureUrl != null ? user.pictureUrl : images[Math.floor(Math.random() * images.length)]}
                                                                firstName={user.firstName}
                                                                lastName={user.lastName}
                                                                username={user.username}
                                                                button={'+'}
                                                                action={inviteUserToProject}
                                                                className={'lineUserPrivacy'}
                                                            />) : '' : ''}
                                </ul>


                            </div>
                        </div>
                        <div className="pendingInvites">
                            <Title title='Invites pending:' className='title-manageUsersProject'> </Title>
                            <ul className="ul-membersRequest">
                                {invitesPending !== undefined && invitesPending.length > 0 ? invitesPending.map((request) =>
                                    <li className='liUserInvited' id={request.idUser + '/pendingLi'}>

                                        <div className="img-user-requests">
                                            <img className='picture-user-requests' src={request.pictureUrl != null ? request.pictureUrl : images[Math.floor(Math.random() * images.length)]} />
                                        </div>
                                        <div className="info-user-ManageUsersProject">
                                            <Text className="name-user-ManageUsersProject" id={request.idUser + '/pendingName'} text={request.firstName + ' ' + request.lastName + ' | ' + request.username} />

                                        </div>

                                        <div className="DIV-btn-user-ManageUsersProject">
                                            <button className="btn-deleteInvite" id={request.idUser + '/pending'} onClick={(e) => { deleteInvite(e) }}>Cancel invite</button>

                                        </div>

                                    </li>
                                ) : 'No invitations waiting for a response.'}
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>




    )
}