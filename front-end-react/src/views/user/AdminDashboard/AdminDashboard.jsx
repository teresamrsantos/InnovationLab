import { useEffect, useState } from "react";
import { FaUserEdit } from "react-icons/fa";
import Button from "../../../components/layout/Button";
import Text from "../../../components/layout/Text";
import Title from "../../../components/layout/Title";
import Sidebar from "../../../components/menu/Sidebar";
import Select from 'react-select'
import { changeUseTypeAPI, findAllUserExceptHimselfAPI, getTimeoutTimeAPI, setTimeoutTimeAPI } from "../../../restApi";
import './AdminDashboard.css';
import CircleLoader from "../../../components/layout/CircleLoader";
import picture1 from "../../../images/TITANS1.png";
import picture2 from "../../../images/TITANS1.png";
import picture3 from "../../../images/TITANS1.png";

import { useNavigate } from "react-router-dom";

export default function AdminDashBoard() {

    const navigate = useNavigate();
    let [allUsers, setAllUsers] = useState('');
    let [timeoutTime, setTimeoutTime] = useState('')
    const token = sessionStorage.getItem('token')
    const [userSearch, setUserSearch] = useState("");
    const [showSuccesssMessage, setShowSuccessMessage] = useState(true);
    var [userTypeFilter, setuserTypeFilter] = useState('')
    const images = [
        picture1,
        picture2,
        picture3,
    ];
    useEffect(() => { setShowSuccessMessage(false) }, [timeoutTime]);

    useEffect(() => {
        findAllUserExceptHimselfAPI(token, (e) => {
            setAllUsers(e)
        },
            (error) => {
                console.log(error)
            });


        getTimeoutTimeAPI(token, (e) => { setTimeoutTime(e.sessionTimeOutTime) })
    }, []);



    function changeTimeout(value) {
        var myJson = JSON.stringify(
            {
                sessionTimeOutTime: timeoutTime
            }
        )
        setTimeoutTimeAPI(token, myJson, ()=>setShowSuccessMessage(true), (e) => console.log('error', e))
    }

    function verificationIncluded(list, array) {

        if (userTypeFilter != '') {
            if (list != array) {
                return false;
            }
        }
        return true;
    }


   


    function changeUserRole(e) {
        changeUseTypeAPI(token, e.target.id.split('/')[0], e.target.value, (s) => { setAllUsers(s) }, (er) => { console.log(er) })
    }

    const options = [{ value: '', label: 'All' },{ value: 'ADMIN', label: 'Admin' },
    { value: 'MEMBER', label: 'Member' },
    { value: 'VISITOR', label: 'Visitor' }]


    return (
        <div>
            <Sidebar></Sidebar>
            <div className="rectangleManageUsersAdmin">
                <Title title='Admin Management' className='project-title'> </Title>
                <div className='setTimeOut_Div'>
                    <label className="settimeoutLabel">Set Time-out:</label><br />
                    <div className="settimeout_inputDIV">

                        <input className="settimeoutDIV_INPUT" min="60" placeholder="timeout" type="number" value={timeoutTime} onChange={(e) => setTimeoutTime(e.target.value)} />
                        <label className="settimeoutDIV_span"> seconds</label><br />
                        <div className="buttonDivAdmindDashboard">
                            {showSuccesssMessage ? <span className='showSuccesssMessageEditTimeOut'>Time-out time set sucessfully !

                            </span> :
                                <button className="settimeoutDIV_Button" onClick={changeTimeout}> Change </button>}
                        </div>




                    </div>
                </div>
                <div className="containerManageUsersProject">
                    <Title title='Manage Users' className='project-title'> </Title>
                    <div className='manageUsersAcceptUsersDIV'>
                        <div className="manageUsers">
                            <div className='selectSearch-manageUsers'>
                                <label className="search-usernameEmail_AdminDashboard_label ">Search by name/username: </label>
                                <input className="search-usernameEmail_AdminDashboard" name="search" placeholder="Search by name/username" onChange={(e) => { setUserSearch(e.target.value) }} />
                                <Select className="multiselect-userType" options={options} displayValue="name" placeholder="Filter by UserType" onChange={(e) => { setuserTypeFilter(e.value) }} /></div>
                            <ul className="ul-membersRequest">
                                {allUsers !== undefined && allUsers.length > 0 ?

                                    allUsers.filter(
                                        person => (person.username.toLowerCase().includes(userSearch.toLowerCase()) ||
                                            (person.firstName.toLowerCase() + " " + person.lastName.toLowerCase()).includes(userSearch.toLowerCase())) && verificationIncluded(person.userType, userTypeFilter)).map((user) =>


                                                <li className='liUserRole' id={user.idUser + '/memberLi'} key={user.idUser} >
                                     
                                                    <div className="img-user-requests">
                                                        <img className='picture-user-requests' src={user.pictureUrl != null ? user.pictureUrl : images[Math.floor(Math.random() * images.length)]} />
                                                    </div>
                                                    <div className="info-user-ManageUsersProject">
                                                        <Text className="name-user-ManageUsersProject" id={user.idUser + '/memberName'} text={user.firstName + " " + user.lastName} />

                                                    </div>

                                                    <div className="DIV-select-role-ManageUsersAdmin" id={user.idUser + '/memberSelectDiv'} >
                                                        <select className="select-roleUser" value={user.userType} id={user.idUser + '/memberSelect'} onChange={(e) => { changeUserRole(e) }} >
                                                            <option value='ADMIN'>Admin</option>
                                                            <option value='MEMBER'>Member</option>
                                                            {user.userType === 'VISITOR' ?
                                                                <option value='VISITOR'>Visitor</option> : ''}
                                                        </select>


                                                        <Button className={"editUserProfileBtn_ManageUsersAdmin"} iconBefore={<FaUserEdit />} onclick={(event) => { navigate("/editprofile/" + user.idUser) }} />


                                                    </div>

                                                </li>
                                            ) : <div className="div-loading">
                                        <CircleLoader />
                                        <Title title="Loading..." />
                                    </div>
                                }
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>)
}

