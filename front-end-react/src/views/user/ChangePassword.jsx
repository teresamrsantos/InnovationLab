import './ChangeUserPassword.css';
import React, { useState } from "react";
import { useSelector } from "react-redux";
import { Spinner } from "reactstrap";
import Button from "../../components/layout/Button";
import Warning from '../../components/layout/ErrorMessage';
import Title from "../../components/layout/Title";
import Sidebar from "../../components/menu/Sidebar";
import { changeUserPasswordAPI } from "../../restApi"
import { Link } from "react-router-dom";
import validator from 'validator'

export default function ChangePassword() {

    var token = sessionStorage.getItem("token")

    const [oldpassword, setOldPassword] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    var [loading, setLoading] = useState(false);
    var [oldPasswordIncorrect, setoldPasswordIncorrect] = useState(false);
    const [show, setShow] = useState(true);
    const [errorMessage, setErrorMessage] = useState(false)

    function validatePassword() {
        if (password.length > 0 && repeatPassword.length > 0) {
            return password === repeatPassword;
        } else return true;
    }

    function validateForm() {
        return password.length > 0 && repeatPassword.length > 0 && oldpassword.length > 0 && validatePassword();
    }

    function handleSubmit(event) {
        event.preventDefault();
        setLoading(true)
        if (validator.isStrongPassword(password, {
            minLength: 8, minLowercase: 1,
            minUppercase: 1, minNumbers: 1,  
        })) {
            var myJson = JSON.stringify({
                oldPassword: oldpassword,
                newPassword: password
            })



            changeUserPasswordAPI(token, myJson, () => { setLoading(false); setShow(false) }, (e) => {
                setLoading(false); (e.toString()).includes('403') ? setoldPasswordIncorrect(true) : setoldPasswordIncorrect(false)
            })
        } else { setLoading(false); setErrorMessage(true) }

    }

    return (
        <div>
            <Sidebar />

            <div className="ChangePassword">
                <div className="ChangePasswordRectangle">
                    <div className="formChangePassword" style={{ display: show ? "" : "none" }}>
                        <Title title="Change Password" className="changePassword"> </Title>
                        <label className="label-changePassword">Current Password:</label><br />
                        <input className="input-changePassword" placeholder="Insert your current password" type="password" value={oldpassword} onChange={(e) => setOldPassword(e.target.value)} />
                        <label className="label-changePassword">New Password:</label><br />
                        <input className="input-changePassword" placeholder="New Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                        <label className="label-changePassword">Repeat New Password:</label><br />
                        <input className="input-changePassword" placeholder="Repeat new Password" type="password" value={repeatPassword} onChange={(e) => setRepeatPassword(e.target.value)} />
                        {validatePassword() ? '' :
                            <div className='matchingPasswordErrorDIV'><Warning text="The passwords do not match" className='matchingPasswordError' /></div>}
                        {oldPasswordIncorrect ? <div className='matchingPasswordErrorDIV'><Warning text="Your old password is not correct please, try again" className='matchingPasswordError' /></div> : ''
                        }

                        {errorMessage ? <div className="password-messageErroreChangePassword">
                            <p>Your password must contain:
                                <br /> - At least 8 characters, <br />
                                - At least one uppercase,  one lowercase, one numeric, and one special character.</p></div> :
                            <div className="password-messageChangePassword">
                                <p>To help keep your information safe, your password must contain:
                                    <br />
                                    - At least 8 characters,
                                    <br />
                                    - At least one uppercase,  one lowercase, one numeric, and one special character.</p></div>}
                        <div className="buttonDivChangePassword">
                            {loading ? <Button className='changePasswordBtn' type="submit" text={<Spinner style={{ width: '1.8rem', height: '1.8rem' }} />}
                                children={false} /> : <Button className='changePasswordBtn' onclick={handleSubmit} type="submit" text='Set new password' disabled={!validateForm()} />}
                        </div>
                    </div>


                    <div className="changePasswordSuccessfull" style={{ display: show ? "none" : "" }}>
                        <Title title="Change Password" className="changePassword"> </Title>


                        <div className='DIVSuccess'>
                            <span className='spanPasswordSuccess'> Your password was successfuly changed! </span>
                            <br />
                            <Link to="/Home" className='spanPasswordSuccessLink'> Click here to go back to the forum</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

