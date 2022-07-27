import React, { useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate, useParams } from "react-router-dom";
import logo_critical from "../../../src/images/logo_critical.png";
import Button from "../../components/layout/Button";
import Warning from "../../components/layout/ErrorMessage";
import Title from "../../components/layout/Title";
import { resetPasswordAPI } from "../../restApi";
import { Spinner } from "reactstrap";
import validator from 'validator'
import './ResetPassword.css';

export default function ResetPassword() {
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const navigate = useNavigate();
    const { resetToken } = useParams()
    var [success, setSuccess] = useState(false);
    var [error, setError] = useState(false);
    var [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState(false)

    function validatePassword() {
        if (password.length > 0 && repeatPassword.length > 0) {
            return password === repeatPassword;
        } else return true;
    }


    function validateForm() {
        if (password.length > 0 && repeatPassword.length > 0) {

            return password !== repeatPassword;
        } else return true;
    }

    function resetPassword() {
        if (password === repeatPassword) {
            setLoading(true)

            if (validator.isStrongPassword(password, {
                minLength: 8, minLowercase: 1,
                minUppercase: 1, minNumbers: 1
            })) {


                resetPasswordAPI(resetToken, password, (() => {
                    setSuccess(true);
                }), (e) => {
                    setError(true);
                })


                setTimeout(() => {
                    navigate("/")
                }, 3000); setLoading(false)

            } else { setLoading(false); setErrorMessage(true) }
        }
    }

    return (
        <div className="ResetPassword" >
            <div className='rectanguleResetPassword'>
                <div>
                    <img className='logoCritical' src={logo_critical} />
                </div>

                {error ? <div className='resetPasswordDIV'>  <Warning className="resetPassword success" text='An error has ocurred.Please, try again later.' />  <Warning className="resetPassword success" text='Thank you' />  </div> : success ? <div className='resetPasswordDIV'>  <Warning className="resetPassword success" text='Your password has been changed! ' />  <Warning className="resetPassword success" text='You will be redirected to the Login page. Thank you' /> </div> :
                    <><Title className="resetPasswordTitle" title='INSERT YOUR NEW PASSWORD:'></Title><div className="formDiv-resetPassword">
                        <form className="formResetPassword">
                            <input className="input-reset" placeholder="Insert new Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                            <input className="input-reset" placeholder="Repeat new Password" type="password" value={repeatPassword} onChange={(e) => setRepeatPassword(e.target.value)} />
                            <div>{validatePassword() ? '' : <Warning text="The passwords do not match" className='matchingPasswordError resetPassword' />}</div>
                        </form>

                        {errorMessage ? <div className="password-messageErroreResetPassword">
                            
                            <p>Your password must contain:
                                <br /> - At least 8 characters, <br />
                                - At least one uppercase,  one lowercase, one numeric, and one special character.</p></div> :
                            <div className="password-messageResetPassword">
                                <p>To help keep your information safe, your password must contain:
                                    <br />
                                    - At least 8 characters,
                                    <br />
                                    - At least one uppercase,  one lowercase, one numeric, and one special character.</p></div>}

                        {loading ? <Button className='recoverButton' type="submit" text={<Spinner style={{ width: '1.8rem', height: '1.8rem' }} />}
                            children={false} /> :
                            <Button className='resetButton' type="submit" text='Define New Password' onclick={resetPassword} disabled={validateForm()} />}
                    </div></>}
            </div>
        </div>
    )
}


