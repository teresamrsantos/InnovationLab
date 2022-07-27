import { useEffect, useState } from "react";
import React from "react"
import { useSelector } from "react-redux";
import Select from 'react-select';
import Title from "../layout/Title";
import { addSkillAPI, getUserInfoAPI, searchSkillAPI } from "../../restApi";
import './SearchSkill.css'
import Alert from "../layout/Alert";

export default function SearchSkill(props) {
    const [userInfo, setUserInfo] = useState('');
    useEffect(() => {
        getUserInfoAPI(
            token,
            (usersInfo) => {
                setUserInfo(usersInfo)

            },
            (error) => {

            }
        );
    }, []);

    const [input, setInput] = useState("")
    const [data, setData] = useState("")
    const [category, setCategory] = useState('')
    const [show, setShow] = useState(true)
    const [skillsList, setSkillsList] = useState('')
    var [knowledgeList, setKnowledgeList] = useState('');
    var [softwareList, setSoftwareList] = useState('');
    var [hardwareList, setHardwareList] = useState('');
    var [toolsList, setToolsList] = useState('');
    var token = sessionStorage.getItem("token")

    useEffect(() => {
        if (input.length > 0) {
            searchSkillAPI(token, input, (response) => {

                response.length > 0 ? setData(response
                ) : setData('')
            }, (e) => setData(''))
        } else if (input.length === 0) {
            setCategory('')
            setData('')
        }
    }, [input])




    function addSkill(id, description, categorySkill) {
        if (description === undefined) {
            if (category === '') {
                setShow(false)
                setTimeout(() => { setShow(true) }, 3000)
            } else {
                setShow(true)
                var myJSON = JSON.stringify({
                    description: input,
                    skillType: category
                })
                addSkillAPI(token, myJSON,
                    (e) => { props.skillList(e); addToArray(e, input, category) }
                    , (e) => { console.log(e) })
            }
        } else {

            setShow(true);

            props.skillList(id);
            addToArray(id, description, categorySkill)
        }
        setInput('')
    }

    function addToArray(id, description, categoryEdit) {
        var data = { description: description, idSkill: id, category: categoryEdit }

        setSkillsList([...skillsList, data])

        if (categoryEdit === 'HARDWARE') {
            setHardwareList([...hardwareList, data]);
        } else if (categoryEdit === 'SOFTWARE') {
            setSoftwareList([...softwareList, data]);
        } else if (categoryEdit === 'TOOLS') {
            setToolsList([...toolsList, data]);
        } else if (categoryEdit === 'KNOWLEDGE') {
            setKnowledgeList([...knowledgeList, data]);
        }
    }


    useEffect(() => {
        removeAll()
    }, [props.removeAll])


    function removeAll() {
        if (props.removeAll == true) {
            setHardwareList([])
            setSoftwareList([])
            setToolsList([])
            setKnowledgeList([])
            setSkillsList([])
        }

    }

    const options = [
        { value: 'KNOWLEDGE', label: 'KNOWLEDGE' },
        { value: 'SOFTWARE', label: 'SOFTWARE' },
        { value: 'HARDWARE', label: 'HARDWARE' },
        { value: 'TOOLS', label: 'TOOLS' }]

    var isContainedOnData = data.length > 0 ? data.some(item => item.description === input && item.skillType === category) : false;

    function removeSkill(e) {
        var id = e.split('/')[0]
        let skillType = e.split('/')[1]

        const arr = skillsList.filter((item) => item.idSkill !== id);
        setSkillsList(arr);
        if (skillType == 'software') {
            let software = softwareList.filter((item) =>   item.idSkill != id) ;
            setSoftwareList(software);
        } else if (skillType == 'knowledge') {
            let knowledge = knowledgeList.filter((item) => item.idSkill != id);
            setKnowledgeList(knowledge);
        } else if (skillType == 'tools') {

            let tools = toolsList.filter((item) => item.idSkill != id);
            setToolsList(tools);
        } else if (skillType == 'hardware') {
            let hardware = hardwareList.filter((item) => item.idSkill != id);
            setHardwareList(hardware);
        }


        props.removeFromskillList(id)

    }






    return (

        <div className='menu-Content-container_SearchSkill'>
            <div className="search-container-Skills">
                <Title title='Search Skill' className='searchSkill' />
                <input type="text" placeholder="Search Skills ..." className='searchSkills' onChange={(e) => { setInput(e.target.value.toLowerCase()) }} value={input} />
                {input.toLowerCase().trim().length > 0 ? (<div>
                    <Select className="select-Skill" options={options} placeholder='Category' onChange={(e) => { setCategory(e.value) }} /> </div>) : ''}
                <div className="errorCategory" style={{ display: show ? "none" : "block" }}>
                    <Alert className={"alert-warning "} text={"You must choose a category !"} />   </div>
            </div>
            <div>
                <ul className='listDataSkill_ul'>
                    {data.length > 0 ? (data).map((skill, index) => {
                        return (


                            <li key={skill.idSkill} id={skill.idSkill}> {skill.description} | <b>{skill.skillType}</b>
                                {skillsList.length > 0 ?
                                    skillsList.some(element => { return (element.description === skill.description && element.category === skill.skillType) }) ?
                                        '' :
                                        <button onClick={(e) => { addSkill(e.target.parentElement.id, skill.description, skill.skillType) }} className="addSkill">  + </button>
                                    : <button onClick={(e) => { addSkill(e.target.parentElement.id, skill.description, skill.skillType) }} className="addSkill">  + </button>}
                            </li>
                        )
                    }) : ''}

                    {input.toLowerCase().trim().length > 0 && !isContainedOnData ? (<li key={input.length}> {input} | <b>{category ? category : ''}</b> { } <button onClick={addSkill} className="addSkill">  + </button> </li>)
                        : ''}

                </ul>


                <div className="addSkill">
                    <Title className='titleaddSkill' title=' Skills:'></Title>

                    {softwareList.length > 0 ?
                        <div className="containerEachTypeSkill">   <Title className='titleUserSkils-Types' title='SOFTWARE' />
                            <ul className="listEachTypeOfSkill_UL">
                                {softwareList.length > 0 ? (softwareList).map((skill, index) => {
                                    return (
                                        <div className="listEachTypeOfSkill_li">
                                            <li key={index} id={skill.idSkill + '/software'}> {skill.description} <button className="addSkill" onClick={(e) => { removeSkill(e.target.parentElement.id) }}> - </button> </li>
                                        </div>)
                                }) : ''}
                            </ul>
                        </div> : ''}

                    {hardwareList.length > 0 ?
                        <div className="containerEachTypeSkill">   <Title className='titleUserSkils-Types' title='HARDWARE' />
                            <ul className="listEachTypeOfSkill_UL">
                                {hardwareList.length > 0 ? (hardwareList).map((skill, index) => {
                                    return (
                                        <div className="listEachTypeOfSkill_li">
                                            <li key={index} id={skill.idSkill + '/hardware'}> {skill.description} <button className="addSkill" onClick={(e) => { removeSkill(e.target.parentElement.id) }}> - </button> </li>
                                        </div>)
                                }) : ''}
                            </ul>
                        </div> : ''}

                    {toolsList.length > 0 ?
                        <div className="containerEachTypeSkill">   <Title className='titleUserSkils-Types' title='TOOLS' />
                            <ul className="listEachTypeOfSkill_UL">
                                {toolsList.length > 0 ? (toolsList).map((skill, index) => {
                                    return (
                                        <div className="listEachTypeOfSkill_li">
                                            <li key={index} id={skill.idSkill + '/tools'}> {skill.description} <button className="addSkill" onClick={(e) => { removeSkill(e.target.parentElement.id) }}> - </button> </li>
                                        </div>)
                                }) : ''}
                            </ul>
                        </div> : ''}

                    {knowledgeList.length > 0 ?
                        <div className="containerEachTypeSkill">   <Title className='titleUserSkils-Types' title='KNOWLEDGE' />
                            <ul className="listEachTypeOfSkill_UL">
                                {knowledgeList.length > 0 ? (knowledgeList).map((skill, index) => {
                                    return (
                                        <div className="listEachTypeOfSkill_li">
                                            <li key={index} id={skill.idSkill + '/knowledge'}> {skill.description} <button className="addSkill" onClick={(e) => { removeSkill(e.target.parentElement.id) }}> - </button> </li>
                                        </div>)
                                }) : ''}
                            </ul>
                        </div> : ''}

                </div >


            </div >
        </div >
    )


}


/* let nba = [
     { name: "Manuel", team: "Lakers" },
     { name: "Miguel", team: "Porto" },
     { name: "Rosario", team: "Sporting" },
     { name: "Manuela", team: "Benfica" },
     { name: "Ana", team: "Porto" },
     { name: "Clara", team: "Raders" },
     { name: "David", team: "Raders" },
 ]

 const handleChange = (e) => {
     e.preventDefault();
     setInput(e.target.value);

     if (input.length > 0) {
 
         nba = nba.filter((i) => {
             return i.name.includes(input)
         })
     }
 }

 return (

     <div className='menu-Content-container'>
         <input type="text" placeholder="search" onChange= {(e)=>  setInput(e.target.value)} value={input}/>

         {nba.filter((val)=>{
             if(input==''){
                 return ''
             } else if(val.name.toLowerCase().includes(input.toLowerCase())){
                 return val
             }
         }).map((player, index) => {

             return (

                 <div key={index}>
                     <ul>
                         <li> {player.name} -{player.team} <button>  + </button> </li>
                     </ul>
                 </div>


             );
         })}
     </div>)*/
