import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { activationAPI } from "../../restApi";
import logo_critical from "../../../src/images/logo_critical.png";
import Button from "../../components/layout/Button";
import Text from "../../components/layout/Text";
import Title from "../../components/layout/Title";
import './ValidateAccount.css';

export default function ValidateAccount(props) {
    var [success, setSuccess] = useState(false);
    var [error, setError] = useState(false);
    const navigate = useNavigate();
    const{id}=useParams()

    useEffect(() => {
        activationAPI(id,
            () => { setSuccess(true) },
            () => { setError(true) });
    },[]);

    function handleSubmit(event) {
        event.preventDefault();
        navigate('/')
    }

    return (
        <div className="ValidateAccount" >
            <div className='rectangule'>
                <div>
                <img className='logoCritical' src={logo_critical} />
                </div>
                {success===true?
                <Title className="validateAccountTitle" title={"Your account has been validated."}/>:
                <Title className="validateAccountTitle" title={"Your token is not valid. Please, contact our administrator"}/>
                }
                <Text className="validateAccountText" text='Thank You!'/>
                <Button className='validateAccountButton' text='Go to Login page'  type="submit"  onclick={handleSubmit}/>
            </div>
        </div>
    )
}

