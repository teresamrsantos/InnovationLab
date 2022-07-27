import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Select from 'react-select';
import Button from '../../components/layout/Button';
import Warning from '../../components/layout/ErrorMessage';
import Title from "../../components/layout/Title";
import { registerAPI } from '../../restApi';
import profilePicture from "../../images/Group226.png";
import logo_critical from "../../images/logo_critical.png";
import { Spinner } from "reactstrap";
import validator from 'validator'
import './Register.css';

export default function Register() {
    const navigate = useNavigate();
    const [emailExists, setEmailExists] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [biography, setBiography] = useState("");
    const [username, setUsername] = useState("");
    const [workplace, setWorkplace] = useState(null);
    var [loading, setLoading] = useState(false);
    const [selectedFile, setSelectedFile] = useState(undefined)
    const [preview, setPreview] = useState()
    const [picture, setPicture] = useState();
    const [show, setShow] = useState(true);


    const [errorMessage, setErrorMessage] = useState(false)




    useEffect(() => {
        if (!selectedFile) {
            setPreview(profilePicture)
            return
        }

        const objectUrl = URL.createObjectURL(selectedFile)
        setPreview(objectUrl)
        setPicture(objectUrl)
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

    const options = [
        { value: 'COIMBRA', label: 'Coimbra' },
        { value: 'LISBOA', label: 'Lisboa' },
        { value: 'PORTO', label: 'Porto' },
        { value: 'TOMAR', label: 'Tomar' },
        { value: 'VILAREAL', label: 'Vila Real' },
        { value: 'VISEU', label: 'Viseu' }]

    function validatePassword() {
        if (password.length > 0 && repeatPassword.length > 0) {

            return password === repeatPassword;
        } else return true;
    }

    function validateForm() {
        return email.length > 0 && firstName.length > 0 && lastName.length > 0 && workplace != null && validatePassword();
    }


    function handleSubmit(event) {

        event.preventDefault();
        setLoading(true)
  
   if (validator.isStrongPassword(password, {
            minLength: 8, minLowercase: 1,
            minUppercase: 1, minNumbers: 1
        })) {
            setErrorMessage(false)
            var myJSON = JSON.stringify({
                firstName: firstName,
                lastName: lastName,
                username: username,
                password: password,
                email: email,
                workplace: workplace,
                pictureUrl: picture,
                biography: biography,
            });


            const formData = new FormData();


            if (selectedFile != undefined) {
                formData.append("image", selectedFile);
            }
            formData.append("userJSON", myJSON);

            registerAPI(formData, (res) => { setShow(false) },
                (e) => { setLoading(false); ((e.toString()).includes('406') ? setEmailExists(true) : setEmailExists(false)) })
        } else { setLoading(false); setErrorMessage(true)}
    }



    return (
        <div className="Register">

            <div className="register-lateral">
                <img className='logo-critical' src={logo_critical} />
                <ul className="ul-title">
                    <Title className="registerTitle1" title='INNOVATION LAB MANAGEMENT SOLUTION' />
                    <Title className="registerTitle2" title='Be a part of something great!' />
                </ul>
            </div>
            <div className='register-rectangule'>

                <div className="registerFormandButton" style={{ display: show ? "block" : "none" }}>
                    <div className="formRegister">
                        <form>
                            <ul className="ul-form">
                                <Title className="registerTitle3" title='Sign up' />
                                <label className="label-register1">Enter your email address:*</label><br />
                                <input className="input-register1" placeholder="Email" autoFocus type="email" value={email} onChange={(e) => setEmail(e.target.value)} /><br />
                                <label className="label-register1">First Name:*</label><br />
                                <input className="input-register1" placeholder="First Name" type="text" value={firstName} onChange={(e) => setFirstName(e.target.value)} /><br />
                                <label className="label-register1">Last Name:*</label><br />
                                <input className="input-register1" placeholder="Last Name" type="text" value={lastName} onChange={(e) => setLastName(e.target.value)} /><br />
                                <label className="label-register1">Username:</label><br />
                                <input className="input-register1" placeholder="Username" type="text" value={username} onChange={(e) => setUsername(e.target.value)} /><br />
                                <label className="label-register1">Password:*</label><br />
                                <input className="input-register1" placeholder="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} /><br />

                                <label className="label-register1">Repeat Password:*</label><br />
                                <input className="input-register1" placeholder="Repeat Password" type="password" value={repeatPassword} onChange={(e) => setRepeatPassword(e.target.value)} /><br />
                                <div>{validatePassword() ? '' : <Warning text="The passwords do not match" className='matchingPasswordError' />}</div>

                            </ul>
                            <ul className="ul-form">
                                <div className="back-pageLogin">
                                    <label className="title-backLogin">Have an Account?</label>
                                    <a className="link-backLogin" href="\">Sign in</a>
                                </div>
                                <img className='profilePicture1' src={preview} /><br />
                                <label className="label-register1">Picture:</label><br />
                                <input className="picture-register1" placeholder="Profile picture" type="file" onChange={onSelectFile} />
                                {/*selectedFile &&  <img src={} /> */}
                                <br />
                                <label className="label-register1">Workplace:*</label><br />
                                <Select className="select-register1" placeholder="Select your workplace" options={options} onChange={(e) => { setWorkplace(e.value) }} />
                                <label className="label-register1">Biography:</label><br />
                                <textarea className="textarea-register1" placeholder="Tell us all about you..." type="text" value={biography} onChange={(e) => setBiography(e.target.value)} />
                            </ul>
                        </form>
                    </div>
                    {emailExists ? <div className='emailExistsError'> <span className='emailExistsText'> Email already exists. If you'd like to sign in please, </span><Link className="emailExistsLink" to='/'> click here! </Link>  </div> : ''}
                    {errorMessage ? <div className="password-messageError">
                        <p>Your password must contain:
                            <br /> - At least 8 characters, <br />
                            - At least one uppercase,  one lowercase, one numeric, and one special character.</p></div> :
                        <div className="password-message">
                            <p>To help keep your information safe, your password must contain:
                                <br /> - At least 8 characters, <br />
                                - At least one uppercase,  one lowercase, one numeric, and one special character.</p></div>}
                    <>
                        {loading ? <Button className='registerButton' type="submit" text={<Spinner style={{ width: '1.8rem', height: '1.8rem' }} />}
                            children={false} /> : <Button className='registerButton' onclick={handleSubmit} type="submit" text='Sign up' disabled={!validateForm()} />}
                    </>
                </div>

                <div className="registerSuccess" style={{ display: show ? "none" : "block" }}>
                    <br /><br />
                    <Title className="registerSuccessBIG" title='YOUR ACCOUNT HAS BEEN SUCCESSFULLY CREATED !'></Title> <br /><br /><br />
                    <Title className="registerSuccessBIG" title='Please, wait for our email to activate your account.'></Title>
                    <Title className="registerSuccessSmall" title='Meanwhile you can sign-in and check all the cool things we are doing... '></Title>
                    <Button className='backToMainPage' onclick={() => { navigate('/') }} type="submit" text='Go Back To Main Page' />
                </div>
            </div>
        </div>
    )
}

