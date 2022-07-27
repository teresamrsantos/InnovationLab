import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Spinner } from "reactstrap";
import logo_critical from "../../../src/images/logo_critical.png";
import Button from "../../components/layout/Button";
import Text from "../../components/layout/Text";
import Title from "../../components/layout/Title";
import { recoverPasswordAPI } from "../../restApi";
import './RecoverPassword.css';

export default function RecoverPassword() {
    const navigate = useNavigate();
    var validator = require('validator');
    const [email, setEmail] = useState("");
    var [success, setSuccess] = useState(false);
    var [error, setError] = useState(false);
    var [loading, setLoading] = useState(false);

    function validateForm() {
        return email.length > 0 && validator.isEmail(email);
    }


    function recoverPassword(event) {
        event.preventDefault();
        setLoading(true);
        recoverPasswordAPI(email,
            () => { setSuccess(true) },
            () => { setError(true) })

        setTimeout(() => {
            navigate("/")
        }, 3000);
    }

    return (

        <div className="RecoverPassword" >
            <div className='rectangule-recoverPassword'>
                <div>
                    <img className='logoCritical' src={logo_critical} />
                </div>
                <Title className="recoverPasswordTitle" title='LOST PASSWORD'></Title>
                <div className="text">
                    <Text className="recoverPasswordText" text='Follow these simple steps to reset your account:' />
                    <Text className="recoverPasswordText" text='1. Enter your  email address' />
                    <Text className="recoverPasswordText" text='2. Wait for your recovery email to be sent' />
                    <Text className="recoverPasswordText" text='3. Follow instructions and be re-united with your account' />
                </div>
                {error ? <div className='recoverPasswordTextSuccess'>  <Text className="recoverPasswordText success" text='An error has ocurred.Please, try again later.' />  <Text className="recoverPasswordText success" text='Thank you' /> </div> : success ? <div className='recoverPasswordTextSuccess'>  <Text className="recoverPasswordText success" text='An email has been sent.' />  <Text className="recoverPasswordText success" text='Thank you' /> </div> : <div className="formDiv-recoverPassword">
                    <form className="formRecoverPassword">
                        <input className="input-recover" placeholder="Insert your email" autoFocus value={email} onChange={(e) => setEmail(e.target.value)} type="email" />
                    </form>
                    {loading ?
                        <Button className='recoverButton' type="submit" text={<Spinner style={{ width: '1.8rem', height: '1.8rem' }}
                            children={false} />} /> :
                        <Button className='recoverButton' onclick={recoverPassword} type="submit" text='Get New Password' disabled={!validateForm()} />}
                </div>}
            </div>
        </div>
    )
}

