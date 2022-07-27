import './Login.css';
//import * as AiIcons from 'react-icons/ai';
import { useState }  from "react";
import GoogleButton from 'react-google-button';
import { AiFillEye, AiFillEyeInvisible } from 'react-icons/ai';
import { MdPersonOutline } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import logo_critical from "../../../src/images/logo_critical.png";
import Button from "../../components/layout/Button";
import ErrorMessage from "../../components/layout/ErrorMessage";
import Title from "../../components/layout/Title";
import { loginAPI } from "../../restApi";
import Alert from '../../components/layout/Alert';


export default function Login() {
    const [email, setEmail] = useState("");
    const [error, setError] = useState(false);
    const [password, setPassword] = useState("");
    const [passwordType, setPasswordType] = useState("password");
    
    var token =   sessionStorage.getItem("token")

    const navigate = useNavigate();
    var validator = require('validator');

    function validateForm() {
        return email.length > 0 && password.length > 0 && validator.isEmail(email);
    }


    if(token!==null && token !== undefined){
        navigate('/Home')
    }


    function handleSubmit(event) {
        event.preventDefault();
        loginAPI(email, password,
            ((response) => {
                sessionStorage.setItem("token", response.token)
                navigate('/Home')
            }),((error) => {
                setError(true)
                setTimeout(()=> setError(false), 3000)
            }))
    }

    const togglePassword =(e)=>{
        e.preventDefault()
        if(passwordType==="password")
        { setPasswordType("text")
        } else {
        setPasswordType("password")}
      }

    return (
        <div className="Login" >
            <div className='rectanguleLogin'>
                <div>
                    <img className='logoCritical' src={logo_critical} />
                </div>
                <Title className="loginTitle" title='INNOVATION LAB MANAGEMENT SOLUTION'></Title>
                <div className="formDivLogin">
                    <form className="formLogin">
                        <div className="divInput-login">
                            <input className="input-login" placeholder="Email" autoFocus type="email" value={email} onChange={(e) => setEmail(e.target.value)}/>
                            <MdPersonOutline size={"8%"} color={"#616368"}/>
                        </div>
                        <div className="divInput-login">
                            <input className="input-login" placeholder="Password" type={passwordType} value={password} onChange={(e) => setPassword(e.target.value)}/>
                            <Button className="smallBtn" onclick={(e) =>{togglePassword(e)}} text= {passwordType === "password" ? <AiFillEyeInvisible color={"#616368"}/>:<AiFillEye color={"#616368"}/>}/>
                        </div>
                    </form>
                        <a className="forgotPassword" href="recoverPassword">FORGOT YOUR PASSWORD?</a><br />
                        <Button className='loginButton' onclick={handleSubmit} type="submit" text='Login' disabled={!validateForm()} />
                    <div className="googleBtn_div">
                        <GoogleButton className="googleBtn" type="light" /*onClick={() => { console.log("Google button clicked"); }}*//>
                    </div>
                </div>
                <div className='loginError'> 
                    {error ? <Alert className={"alert-danger"} text='Your password or your email address is incorrect. Please, try again.' />
                        : ''}   
                </div>
                <div className="divRegisterLink">
                    <a className="link-register" href="register">Don't have an account? Register here</a>
                </div>
            </div>
        </div>
    )
}

