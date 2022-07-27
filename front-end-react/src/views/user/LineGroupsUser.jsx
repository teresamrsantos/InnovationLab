import React, { useEffect, useState } from "react";
import './LineGroupsUser.css'
import Text from '../../components/layout/Text'
import { getInterestById, getskillById, removeGroupAPI } from '../../restApi'
import * as FaIcons from 'react-icons/fa';

export default (props) => {

    var workplace = props.workplace ? props.workplace.toString() : '';
    var interest = props.interest ? props.interest : [];
    var skill = props.skill ? props.skill : [];
    var className = props.className;
    var token = sessionStorage.getItem("token");
    var idGroup = props.id;
    var index = props.index;

    function removeGroup(e) {
        var id =idGroup
             removeGroupAPI(token, idGroup, (e) => 
           props.resetGroupList(e)
       )
    }

    
    function getText(){
   

var interestDescriptionList=[];
var skillDescriptionList =[]

    if(interest.length > 0) {
        interest.forEach(element => {
       
             interestDescriptionList.push(element.description);
         });
    }

    if(skill.length > 0) { 
        skill.forEach(element => {
            skillDescriptionList.push(element.description + ' | ' + element.skillType)
         });
    }


    var text;
    if (workplace !== '' && interest.length === 0 && skill.length === 0) {

        text = 'You allowed users from ' + workplace + ' to see your profile.'

    } else if (workplace === '' && interest.length > 0 && skill.length === 0) {

        text = 'You allowed users with interest in ' + interestDescriptionList + ' to see your profile.'

    } else if (workplace === '' && interest.length === 0 && skill.length > 0) {

        text = 'You allowed users with the following skills: ' + skillDescriptionList + ' to see your profile.'

    } else if (workplace === '' && interest.length > 0 && skill.length > 0) {

        text = 'You allowed users with interest in ' + interestDescriptionList + ' and the following skills: ' + skillDescriptionList + ' to see your profile.'

    } else if (workplace !== '' && interest.length === 0 && skill.length > 0) {

        text = 'You allowed users from ' + workplace + ' with the following skills: ' + skillDescriptionList + ' to see your profile.'

    } else if (workplace !== '' && interest.length > 0 && skill.length === 0) {

        text = 'You allowed users from ' + workplace + ' and with interest in ' + interestDescriptionList + ' to see your profile.'

    } else if (workplace !== '' && interest.length > 0 && skill.length > 0) {

        text = 'You allowed users from ' + workplace + ' with interest in ' + interestDescriptionList + ' and the following skills: ' + skillDescriptionList + ' to see your profile.'
    }

 return text;
    }


   
    return (


        <div className="info-group">

            <li className={className} key={index} id={idGroup+'/groupLine'}>
                <span id='spanTextUserGroups'>{getText()}</span>
                <button className='btnRemoveGroup' onClick={props.button === '-' ? (e) => { removeGroup(e) } : ''}>     <FaIcons.FaTrashAlt onClick={ (e) => { removeGroup(e) }} /> </button>
            </li>
        </div>

    );
};

