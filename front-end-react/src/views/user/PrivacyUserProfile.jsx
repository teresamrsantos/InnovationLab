import React, { useEffect, useState } from "react";
import Sidebar from "../../components/menu/Sidebar";
import { getUserInfoAPI, addaVisibilityGroup, changeVisibilityUser, getAllUsersToTalkAPI, addUserSpecificVisibility, removeUserSpecificVisibility } from "../../restApi"
import Title from "../../components/layout/Title";
import SearchInterest from "../../components/SearchInterestSkill/SearchInterest";
import SearchSkill from "../../components/SearchInterestSkill/SearchSkill";
import LineGroupsUser from "./LineGroupsUser";
import './PrivacyUserProfile.css'
import LineUser from "../../components/layout/LineUser";
import ProfilePicture from '../../images/Group226.png'
import CircleLoader from "../../components/layout/CircleLoader";
import { MdOutlineAddCircleOutline, MdOutlineHideSource } from "react-icons/md";


export default function PrivacyUserProfile() {
    const token = sessionStorage.getItem("token");
    let [userInfo, setUserInfo] = useState('');
    let [privacy, setPrivacy] = useState('');
    let [groupList, setGroupList] = useState('');
    let [userAllowedToSeeProfile, setUserAllowedToSeeProfile] = useState('');
    let [workplace, setWorkplace] = useState('');
    let [skillList, setSkillList] = useState([]);
    let [interestList, setInterestsList] = useState([]);
    let [allUser, setAllUser] = useState([]);
    let [userSearch, setUserSearch] = useState("");
    let [removeAll, setRemoveAll] = useState(false)

    useEffect(() => {
        getUserInfoAPI(token, (usersInfo) => {

            setUserInfo(usersInfo)
            setPrivacy(usersInfo.visibility)
            setGroupList(usersInfo.groupList)
            setUserAllowedToSeeProfile(usersInfo.listOfUsersAllowedToSeeProfile)
        },
            (error) => {
            });
    }, []);

    function changePrivacy(event) {
        var privacyVar = event.target.value
          changeVisibilityUser(token, userInfo.id, privacyVar, (e) => {
            setPrivacy(privacyVar)
        },
            (error) => {
                console.log(error)
            });
    }

    const optionsPrivacy = [
        { value: 'EVERYONE', label: 'Everyone' },
        { value: 'SPECIFIC', label: 'Specific' },
        { value: 'PRIVATE', label: 'Private' }]


    const optionsWorkplace = [
        { value: '', label: 'Select workplace' },
        { value: 'COIMBRA', label: 'Coimbra' },
        { value: 'LISBOA', label: 'Lisboa' },
        { value: 'PORTO', label: 'Porto' },
        { value: 'TOMAR', label: 'Tomar' },
        { value: 'VILAREAL', label: 'Vila Real' },
        { value: 'VISEU', label: 'Viseu' }]


    function fillSkillList(e) {
        setRemoveAll(false);
        let array = skillList;
        array.push(e)
        setSkillList(array);
    }

    function removeFromskillList(e) {
        let array = skillList;
        for (var i = 0; i < array.length; i++) {
            if (array[i] === e) {
                array.splice(i, 1);
            }

        }
        setSkillList(array);
    }

    function fillInterestList(e) {
        setRemoveAll(false);
        let array = interestList;
        array.push(e)
        setInterestsList(array);
    }

    function removeFromInterestList(e) {


        let array = interestList;
        for (var i = 0; i < array.length; i++) {
            if (array[i] === e) {
                array.splice(i, 1);
            }

        }
        setInterestsList(array);
    }


    function addGroup() {
        var myJSON = JSON.stringify({
            idSkillList: skillList,
            idInterestList: interestList,
            workplace: workplace
        })

        addaVisibilityGroup(token, myJSON, (e) => {
            setGroupList(e)
            setWorkplace('');

            setInterestsList([]);
            setSkillList([]);
            setRemoveAll(true);
            //setInterestsList([]);
        }, (e) => {
            console.log(e)
        })
    }




    useEffect(() => {
        getAllUsersToTalkAPI(token, (e) => {
            setAllUser(e)
        },
            (error) => {
                console.log(error)
            });
    }, []);


    function addUserToProfileVisibility(e) {

        var idToAdd = e.target.parentElement.id;
        addUserSpecificVisibility(token, idToAdd, userInfo.id, (e) => {

            setUserAllowedToSeeProfile(userAllowedToSeeProfile => [...userAllowedToSeeProfile, JSON.parse(e)])


        }, (e) => { console.log(e) })
    }

function resetGroupList(e){
    setGroupList(e)
}

    function removeUserToProfileVisibility(e) {
        var idToRemove = e.target.parentElement.id;

        removeUserSpecificVisibility(token, idToRemove, userInfo.id, (e) => {

            const index = userAllowedToSeeProfile.findIndex(elm => elm.id == idToRemove)
            setUserAllowedToSeeProfile([...userAllowedToSeeProfile.slice(0, index),
            ...userAllowedToSeeProfile.slice(index + 1, userAllowedToSeeProfile.length)])

        }, (e) => { console.log(e) })
    }

    return (
        <div>
            <Sidebar />
            <div className="userPrivacyContainer">


                <div className="DIVtitleUserPrivacy">
                    <Title className="titleUserPrivacy" title="Who can see your profile ?" />
                    <select className="select-Userprivacy" value={privacy} onChange={(e) => { changePrivacy(e) }} >
                        {optionsPrivacy.map(option => {
                            return (
                                <option value={option.value}>{option.label}</option>
                            )
                        })}
                    </select>
                </div>

                <div className="rectangleUserPrivacy" style={{ display: privacy === 'SPECIFIC' ? "flex" : "none" }}>

                    <div className='groupVisibilityUsers'>

                        <div className='groupsAlreadyDefined'>
                            <span className='groupsAlreadyAllowedToSeeYourProfile'> Groups already allowed to see your profile: </span>
                            <ul className="listOfGroupsUser">
                                      {groupList.length > 0 ?
          
                                    (groupList).map((group, index) => {
                                      
                                        return (
                                
                                            <LineGroupsUser 
                                            workplace={group.workplace}
                                      
                                                id={group.idGroup}
                                                interest={group.interestDTOList}
                                                skill={group.skillDTOList}
                                                key={index}
                                                className='groupsFromUser' button='-'
                                                resetGroupList = {resetGroupList}>
                                                </LineGroupsUser>)
                                    }) : <div className="no-allIdeaNecessity">
                                    <MdOutlineHideSource size={"20%"} color={"gray"} />
                                    <Title title='No group has been specified.' />
                                </div>}
                                
                            </ul>
                        </div>

                        <div className="defineNewGroups">

                            <span className="titleGroups"> Define groups of Users that can see your profile: </span>

                            <div className="DIVSelectGroups">
                                <div className="select-privacyDIV">
                                    <Title className="select-privacyLabel" title='Select Workplace' />
                                    <select className="select-privacy" value={workplace} onChange={(e) => { setWorkplace(e.target.value) }} >
                                        {optionsWorkplace.map(option => {
                                            return (
                                                <option value={option.value}>{option.label}</option>
                                            )
                                        })}
                                    </select>
                                </div>


                                <div className="interests-privacyDIV">
                                    <SearchInterest interestList={fillInterestList} removeFromInterestList={removeFromInterestList} removeAll={removeAll}  ></SearchInterest>
                                </div>

                                <div className="skill-privacyDIV">
                                    <SearchSkill skillList={fillSkillList} removeFromskillList={removeFromskillList}  removeAll={removeAll}></SearchSkill>
                                </div>

                            </div>
                            <div className="divbtnAddGroup">
                                <button className='btnAddGroup' onClick={addGroup}> Add Group </button>
                            </div>
                        </div>


                    </div>


                    <div className='specificUsers'>

                        <span className="titleGroups users"> Users already allowed to see your profile: </span>
                        <div className="listOfUsers">

                            {userAllowedToSeeProfile.length > 0 ?

                                userAllowedToSeeProfile.map((user, index) => {

                                    return (
                                        <LineUser
                                            page='privacy'
                                            idUserAuth={userInfo.id}
                                            idUser={user.id}
                                            pictureUrl={user.pictureUrl ? user.pictureUrl : ProfilePicture}
                                            firstName={user.firstName}
                                            lastName={user.lastName}
                                            username={user.username}
                                            button={'-'}
                                            action={removeUserToProfileVisibility}
                                            className={'userOnTheList_Privacy'}
                               

                                        />);
                                }) :
                                 <div className="no-allIdeaNecessity">
                                    <MdOutlineHideSource size={"20%"} color={"gray"} />
                                    <Title title='No specific user is allowed to see your profile' />
                            
                                </div>}
                        </div>

                        <div className='titleAndInputDIV-privacy'>

                            <span className="titleGroups">Search users to see your profile: </span>
                            <input className="search-usernameEmail_privacy" name="search" placeholder="Search by name/username" onChange={(e) => { setUserSearch(e.target.value) }} />
                        </div>
                        <div className="list-userToTalk_privacy">

                            <ul >

                                {userSearch !== '' ? userAllowedToSeeProfile.length > 0 ?
                                    allUser.filter(x => !userAllowedToSeeProfile.filter(y => y.id === x.id).length).filter(

                                        person => person.username.toLowerCase().includes(userSearch.toLowerCase()) ||
                                            (person.firstName.toLowerCase() + " " + person.lastName.toLowerCase()).includes(userSearch.toLowerCase()))
                                        .map((user) =>
                                            <LineUser
                                                page='privacy'
                                                idUserAuth={userInfo.id}
                                                idUser={user.id}
                                                pictureUrl={user.pictureUrl ? user.pictureUrl : ProfilePicture}
                                                firstName={user.firstName}
                                                lastName={user.lastName}
                                                username={user.username}
                                                button={'+'}
                                                action={addUserToProfileVisibility}
                                                className={'lineUserPrivacy'}
                                            />) : allUser.filter(

                                                person => person.username.toLowerCase().includes(userSearch.toLowerCase()) ||
                                                    (person.firstName.toLowerCase() + " " + person.lastName.toLowerCase()).includes(userSearch.toLowerCase()))
                                                .map((user) =>
                                                    <LineUser
                                                        page='privacy'
                                                        idUserAuth={userInfo.id}
                                                        idUser={user.id}
                                                        pictureUrl={user.pictureUrl ? user.pictureUrl : ProfilePicture}
                                                        firstName={user.firstName}
                                                        lastName={user.lastName}
                                                        username={user.username}
                                                        button={'+'}
                                                        action={addUserToProfileVisibility}
                                                        className={'lineUserPrivacy'}
                                                    />) : ''}
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div >
    )
}
