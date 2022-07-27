import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import Select from 'react-select';
import { Spinner } from "reactstrap";
import Button from "../../components/layout/Button";
import Title from "../../components/layout/Title";
import Sidebar from "../../components/menu/Sidebar";
import picture1 from "../../images/TITANS1.png";
import picture2 from "../../images/TITANS2.png";
import picture3 from "../../images/TITANS3.png";
import { getUserByIdAPI, editUserProfileAPI, getUserInfoAPI, associatSkillToUserAPI, disassociateSkillToUserAPI, getUserSkillsAPI, getUserInterestsAPI, associateInterestToUserAPI, disassociateInterestToUserAPI } from '../../restApi';
import './EditUserProfile.css';
import Alert from "../../components/layout/Alert";
import EditInterest from "../../components/EditInterestSkill/EditInterest";
import EditSkill from "../../components/EditInterestSkill/EditSkill";


export default function EditUserProfile() {
    var token = sessionStorage.getItem("token")
    const [userInfo, setUserInfo] = useState('');
    const [idUser, setIdUser] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [biography, setBiography] = useState('');
    const email = userInfo.email;
    const [username, setUsername] = useState('');

    const [availability, setAvailability] = useState('');
    const [workplace, setWorkplace] = useState('');
    const images = [
        picture1,
        picture2,
        picture3,
    ];
    const [picture, setPicture] = useState(images[Math.floor(Math.random() * images.length)])
    const { id } = useParams();
    const [showSuccesssMessage, setShowSuccessMessage] = useState(true);
    let [messageAlert, setMessageAlert] = useState(true);
    const [alert, setAlert] = useState(false);

    useEffect(() => { setShowSuccessMessage(false) }, [availability, workplace, picture, username, firstName, lastName, biography]);

    useEffect(() => {
        if (id === "" || id === undefined) {
            getUserInfoAPI(
                token,
                (usersInfo) => {
                    setUserInfo(usersInfo)
                    setFirstName(usersInfo.firstName ? usersInfo.firstName : '')
                    setLastName(usersInfo.lastName ? usersInfo.lastName : '');
                    setBiography(usersInfo.biography ? usersInfo.biography : '');
                    setAvailability(usersInfo.availablehours ? parseInt(usersInfo.availablehours) : '');
                    setWorkplace(usersInfo.workplace ? usersInfo.workplace : '');
                    setUsername(usersInfo.username ? usersInfo.username : '');
                    if (usersInfo.pictureUrl != null) {
                        setPicture(usersInfo.pictureUrl ? usersInfo.pictureUrl : images[Math.floor(Math.random() * images.length)]);
                    }
                    setIdUser(usersInfo.idUser)
                }, (error) => { });
        } else {
            getUserByIdAPI(id, token, (usersInfo) => {
                setUserInfo(usersInfo)
                setFirstName(usersInfo.firstName ? usersInfo.firstName : '')
                setLastName(usersInfo.lastName ? usersInfo.lastName : '');
                setBiography(usersInfo.biography ? usersInfo.biography : '');
                setAvailability(usersInfo.availablehours ? parseInt(usersInfo.availablehours) : '');
                setWorkplace(usersInfo.workplace ? usersInfo.workplace : '');
                setUsername(usersInfo.username ? usersInfo.username : '');
                setPicture(usersInfo.pictureUrl ? usersInfo.pictureUrl : '');
                setIdUser(usersInfo.idUser)

            }, (error) => { });
        }
    }, []);

    var [loading, setLoading] = useState(false);
    const [selectedFile, setSelectedFile] = useState(userInfo.pictureUrl)
    const [preview, setPreview] = useState('')
    const [show, setShow] = useState(true);

    const options = [
        { value: 'COIMBRA', label: 'Coimbra' },
        { value: 'LISBOA', label: 'Lisboa' },
        { value: 'PORTO', label: 'Porto' },
        { value: 'TOMAR', label: 'Tomar' },
        { value: 'VILAREAL', label: 'Vila Real' },
        { value: 'VISEU', label: 'Viseu' }]



    useEffect(() => {
        if (!selectedFile) { return }
        const objectUrl = URL.createObjectURL(selectedFile)
        setPreview(objectUrl)
        // free memory when ever this component is unmounted
        return () => URL.revokeObjectURL(objectUrl)
    }, [selectedFile])

    const onSelectFile = e => {
        if (!e.target.files || e.target.files.length === 0) {
            setSelectedFile(undefined)
            return
        }
        // I've kept this example simple by using the first image instead of multiple
        setSelectedFile(e.target.files[0])
    }

    function validateForm() {
        return firstName.length > 0 && lastName.length > 0 && workplace != null;
    }

    function handleSubmit(event) {
        event.preventDefault();


        setAlert(false)
        if (firstName == "" || lastName == "" || workplace == undefined) {
            setMessageAlert("It is not possible to complete the action please, fill all the mandatory fields.")
            setAlert(true)

        } else {
            setLoading(true)
            if (username === '') {
                setUsername(firstName + lastName)
            }

            var myJSON = JSON.stringify({
                idUser: idUser,
                firstName: firstName,
                email: email,
                lastName: lastName,
                username: username,
                workplace: workplace,
                availablehours: availability,
                biography: biography,
            });

            const formData = new FormData();
            /* if (selectedFile === undefined) {
                 formData.append("image", userInfo.pictureUrl);
             } else {*/
            if (selectedFile !== undefined) {
                formData.append("image", selectedFile);
            }

            formData.append("userJSON", myJSON);
            editUserProfileAPI(token, formData, (res) => { setShowSuccessMessage(true); setLoading(false) }, (e) => {
                setLoading(false)
            })
        }
    }


    function resetAlert(e) {
        setLoading(false)
        setAlert(false)
    }


    useEffect(() => { resetAlert() }, [biography, firstName, lastName, workplace, username, availability])

    return (
        <div>
            <Sidebar />
            <div className={"alert-warningEdit"} style={{ display: alert ? "block" : "none" }}>
                <Alert className={"alert-warning "} text={messageAlert} />
            </div>
            <div className="EditUserProfile">

                <div className="EditUserProfileDIVbuttonsAndFormAndTitle">
                    <div className="EditUserProfileRectangle">
                        <div className="registerFormandButton" style={{ display: show ? "block" : "none" }}>
                            <div className="titleAndImage">
                                <Title className="editprofileTitle" title='Edit your profile' />
                                <img className='profilePicture' src={preview === '' ? picture : preview} /><br />
                            </div>
                            <div className="formEditUserProfile">
                                <div className="FistColumn_EditProfile-form">
                                    <label className="label-register">First Name:*</label>
                                    <input className="input-register" placeholder="First Name" type="text" value={firstName} onChange={(e) => { setFirstName(e.target.value) }} /><br />
                                    <label className="label-register">Last Name:*</label>
                                    <input className="input-register" placeholder="Last Name" type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} /><br />
                                    <label className="label-register">Username:</label>
                                    <input className="input-register" placeholder="Username" type="text" value={username} onChange={(e) => setUsername(e.target.value)} /><br />
                                    <label className="label-register">Availability:</label>
                                    <div className='inputhoursWeekDIV'>
                                        <input className="input-register numberEditProfile" min="0" placeholder="Availability" type="number" value={availability} onChange={(e) => setAvailability(e.target.value)} /> <span>&nbsp; hours / week </span><br /></div>
                                </div>
                                <div className="SecondColumn_EditProfile-form">
                                    <label className="label-register">Picture:</label>
                                    <input className="picture-register" placeholder="Profile picture" type="file" onChange={onSelectFile} />  <br />
                                    <label className="label-register">Workplace:*</label>
                                    <select className="select-register" value={workplace} onChange={(e) => { setWorkplace(e.target.value) }} >
                                        {options.map(option => { return (<option value={option.value}>{option.label}</option>) })}
                                    </select><br />
                                    <label className="label-register">Biography:</label>
                                    <textarea className="textarea-register" placeholder="Tell us all about you..." type="text" value={biography} onChange={(e) => setBiography(e.target.value)} />
                                </div>
                            </div>
                            <div className="buttonDiv">
                                {showSuccesssMessage ? <span className='showSuccesssMessageEditProfile'>  Your profile was edited sucessfully !

                                </span> :
                                    loading ? <Button className={!validateForm() ? 'editprofileBtn btnDisabled' : 'editprofileBtn'} type="submit" text={<Spinner style={{ width: '1.8rem', height: '1.8rem' }} />}
                                        children={false} /> : <Button className={!validateForm() ? 'editprofileBtn btnDisabled' : 'editprofileBtn'} onclick={handleSubmit} type="submit" text='Edit your profile' />}
                            </div>
                        </div>
                    </div>
                </div>
                <div className="searchInterestSkill-editUserProfile">
                    <div className="searchInterest-userprofile">
                        <EditInterest resetAlert={resetAlert} functionGetAll={getUserInterestsAPI} functionAssociateInterestToEntity={associateInterestToUserAPI} functionDisassociateInterestToEntity={disassociateInterestToUserAPI}></EditInterest>
                    </div>
                    <div className="searchSkill-userprofile">
                        <EditSkill resetAlert={resetAlert} functionGetAll={getUserSkillsAPI} functionAssociateSkillToEntity={associatSkillToUserAPI} functionDisassociateSkillToEntity={disassociateSkillToUserAPI}></EditSkill>
                    </div>
                </div>
            </div>
        </div>
    );
};


