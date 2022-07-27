import React, {useState, useEffect, useCallback, useRef, Fragment} from 'react';
import moment from 'moment';
import { useNavigate } from "react-router-dom";
import {getTimeoutTimeAPI, logoutAPI} from "../../restApi";

const SessionTimeout = (props) => {
    const [events, setEvents] = useState(['click', 'load', 'scroll','mousemove']);
    let timeStamp;
    let warningInactiveInterval = useRef();
    let startTimerInterval = useRef();
    const navigate = useNavigate();
    let [timeoutTime, setTimeoutTime] = useState(60);
    const token = sessionStorage.getItem('token');

    useEffect(() => {
        if(token!=null){
            getTimeoutTimeAPI(token, (e) => {
                var time = (e.sessionTimeOutTime*1); 
                setTimeoutTime(time)
                timeStamp = moment();
                sessionStorage.setItem('lastTimeStamp', timeStamp);
            },(error) => {console.log(error)})
        }
    }, [token]);

    let timeChecker = () => {
        startTimerInterval.current = setTimeout(() => {
            let storedTimeStamp = sessionStorage.getItem('lastTimeStamp');
            warningInactive(storedTimeStamp);
        }, 1000);
    };

    let warningInactive = (timeString) => {
        clearTimeout(startTimerInterval.current);
    
        warningInactiveInterval.current = setInterval(() => {
            const diff = moment.duration(moment().diff(moment(timeString)));
            const minPast = diff.minutes()*60+diff.seconds();

            if (minPast === timeoutTime) {
                clearInterval(warningInactiveInterval.current);
                sessionStorage.removeItem('lastTimeStamp');
                logout();
            }
        }, 1000);
    };

    function logout() {
        logoutAPI(token, () => {
            sessionStorage.removeItem('token');
            sessionStorage.clear();
            setTimeoutTime(50)
            navigate('/');
        })
    }

    let resetTimer = useCallback(() => {
        clearTimeout(startTimerInterval.current);
        clearInterval(warningInactiveInterval.current);

        if (token!=null) {
            timeStamp = moment();
            sessionStorage.setItem('lastTimeStamp', timeStamp);
        } else {
            clearInterval(warningInactiveInterval.current);
            sessionStorage.removeItem('lastTimeStamp');
        }
        timeChecker();
    }, [token]);



    useEffect(() => {
        events.forEach((event) => {window.addEventListener(event, resetTimer);});
        timeChecker();
        return () => {clearTimeout(startTimerInterval.current);};
    }, [resetTimer, events, timeChecker]);

    return <Fragment />;
};

export default SessionTimeout;