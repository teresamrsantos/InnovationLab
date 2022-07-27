import React from "react";
import logo_critical from "../../images/logo_critical.png"
import './EmailVerification.css'
import { useNavigate } from "react-router-dom";
import Button from '../../components/layout/Button'
import Title from "../../components/layout/Title";
import Text from "../../components/layout/Text";

export default function EmailVerification() {
    const navigate = useNavigate();
    function handleSubmit(event) {
        event.preventDefault();
        navigate('/')
    }

    return (
        <div className="EmailVerification">
            <div className="emailVerification-lateral">
                <img className='logo-critical' src={logo_critical} />
                <ul className="ul-title">
                    <Title className="emailVerificationTitle1" title='INNOVATION LAB MANAGEMENT SOLUTION' />
                    <Title className="emailVerificationTitle2" title='Be a part of something great!' />
                </ul>
            </div>
            <div className='emailVerification-rectangule'>
                <Title className="emailVerificationTitle3" title='YOUR ACCOUNT HAS BEEN SUCCESSFULLY CREATED !' />
                <Text className="emailVerificationText1" text='Please, wait for our email to activate your account.' />
                <Text className="emailVerificationText2" text='Meanwhile you can sign-in and check all the cool things we are doing...'/>
                <Button className='emailVerificationButton' text='Go Back To Main Page' type="submit"  onclick={handleSubmit} />
            </div>
        </div>
    )
}

