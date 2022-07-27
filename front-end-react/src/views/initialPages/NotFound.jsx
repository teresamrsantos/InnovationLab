import React from "react";
import logo_critical from "../../../src/images/logo_critical.png"
import './NotFound.css';
import Title from "../../components/layout/Title";
import Text from "../../components/layout/Text";

const NotFound = props =>(
    <div className="NotFound" >
        <div className='rectangule'>
            <div>
                <img className='logoCritical' src={logo_critical} />
            </div>
            <Title className="notFoundTitle" title='404'/>
            <Text className="notFoundText" text='Opss....Page not found!'/>
        </div>
    </div>
)

export default NotFound