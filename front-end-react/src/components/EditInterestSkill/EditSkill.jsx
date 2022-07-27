import React from "react";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Select from 'react-select';
import Title from "../../components/layout/Title";
import { addSkillAPI, searchSkillAPI } from "../../restApi";
import './EditSkill.css';

export default function SearchSkill(props) {

    const [skillsList, setSkillsList] = useState('');
    const [knowledgeList, setKnowledgeList] = useState('');
    const [softwareList, setSoftwareList] = useState('');
    const [hardwareList, setHardwareList] = useState('');
    const [toolsList, setToolsList] = useState('');
    const [input, setInput] = useState("");
    const [data, setData] = useState("");
    const [category, setCategory] = useState('');
    const [show, setShow] = useState(true);
    const { id } = useParams();
    var token = sessionStorage.getItem("token");
    var functionGetAll_API = props.functionGetAll;
    var functionAssociateSkillToEntity_API = props.functionAssociateSkillToEntity;
    var functionDisassociateSkillToEntity_API = props.functionDisassociateSkillToEntity;

    useEffect(() => {
        functionGetAll_API(id, token, (skillList) => {
            setSkillsList(skillList ? skillList : '');
            setKnowledgeList(skillList ? skillList.filter(skill => skill.skillType === 'KNOWLEDGE') : '')
            setSoftwareList(skillList ? skillList.filter(skill => skill.skillType === 'SOFTWARE') : '');
            setHardwareList(skillList ? skillList.filter(skill => skill.skillType === 'HARDWARE') : '');
            setToolsList(skillList ? skillList.filter(skill => skill.skillType === 'TOOLS') : '');
        });
    }, []);

    useEffect(() => {
        if (input.length > 0) {
            searchSkillAPI(token, input, (response) => {
                response.length > 0 ? setData(response) : setData('')
            }, (e) => setData(''))
        } else if (input.length === 0) {
            setCategory('')
            setData('')
        }
    }, [input]);

    function addSkill(e, description, categorySkill) {
        if (description === undefined) {
            if (category === '') {
                setShow(false)
            } else {
                setShow(true)
                var myJSON = JSON.stringify({
                    description: input,
                    skillType: category
                })
                addSkillAPI(token, myJSON,(e) => associateSkillToEntity(e), (e) => { })
            }
        }else { 
            setShow(true); 
            associateSkillToEntity(e) 
        }
        setInput('')
    }

    function setLists(e) {
        setInput('');
        setSkillsList(e);
        setKnowledgeList(e.filter(skill => skill.skillType === 'KNOWLEDGE'));
        setSoftwareList(e.filter(skill => skill.skillType === 'SOFTWARE'));
        setHardwareList(e.filter(skill => skill.skillType === 'HARDWARE'));
        setToolsList(e.filter(skill => skill.skillType === 'TOOLS'));
        setCategory('');
    }

    function associateSkillToEntity(idSkill) {
        functionAssociateSkillToEntity_API(token, id, idSkill, (e) => { setLists(e) });
    }

    function disassociateSkillFromEntity(idSkill) {
        props.resetAlert();
        functionDisassociateSkillToEntity_API(token, id, idSkill, (e) => { setLists(e) });
    }

    const options = [
        { value: 'KNOWLEDGE', label: 'KNOWLEDGE' },
        { value: 'SOFTWARE', label: 'SOFTWARE' },
        { value: 'HARDWARE', label: 'HARDWARE' },
        { value: 'TOOLS', label: 'TOOLS' }]

    var isContainedOnData = data.length > 0 ? data.some(item => item.description === input && item.skillType === category) : false;

    return (
        <div>
            <div className="search-container-SkillsToIdeaNecessity">
                <Title title='Search Skill' className='searchSkillToIdeaNecessity' />
                <input type="text" placeholder="Search Skills ..." className='searchSkills-input' onChange={(e) => { props.resetAlert(); setInput(e.target.value) }} value={input} />
                {input.trim().length > 0 ? (<div>
                    <Select className="select-typeSkill" options={options} placeholder='Category' onChange={(e) => { setCategory(e.value) }} /> </div>) : ''}
                <div className="errorCategory-Skills" style={{ display: show ? "none" : "block" }}> <span> You must choose a category !</span> </div>
            </div>
            <div>
                <ul>
                    {data.length > 0 ? (data).map((skill, index) => {
                        return (
                            <li key={skill.idSkill} id={skill.idSkill}> {skill.description} | <b>{skill.skillType}</b>
                
                                {skillsList?.some(element => {   return(element.description === skill.description && element.skillType === skill.skillType) }) ?
                                
                                    <button onClick={(e) => { disassociateSkillFromEntity(e.target.parentElement.id) }} className="addSkillToIdeaNecessity">  - </button> :
                                    <button onClick={(e) => { addSkill(e.target.parentElement.id, skill.description, skill.skillType) }} className="addSkillToIdeaNecessity">  + </button>}
                            </li>
                        )
                    }) : ''}
                    {input.trim().length > 0 && !isContainedOnData ? (<li key={input.length}> {input} | <b>{category ? category : ''}</b> { } <button onClick={addSkill} className="addSkill">  + </button> </li>) : ''}
                </ul>
                <div>
                    <Title className='titleIdeaNecessitySkills' title='Your Skills:' />
                    {softwareList.length > 0 ?
                        <div className="containerEachTypeSkillIN">
                            <Title className='titleUserSkils-Types' title='SOFTWARE' />
                            <ul className="listEachTypeOfSkillIN_UL">
                                {softwareList.length > 0 ? (softwareList).map((skill, index) => {
                                    return (
                                        <div className="listEachTypeOfSkillIN_li">
                                            <li key={index} id={skill.idSkill}>
                                                {skill.description}
                                                <button className="addSkill" onClick={(e) => { disassociateSkillFromEntity(e.target.parentElement.id) }}> - </button>
                                            </li>
                                        </div>
                                    )
                                }) : ''}
                            </ul>
                        </div> : ''}

                    {hardwareList.length > 0 ?
                        <div className="containerEachTypeSkillIN">
                            <Title className='titleUserSkils-Types' title='HARDWARE' />
                            <ul className="listEachTypeOfSkillIN_UL">
                                {hardwareList.length > 0 ? (hardwareList).map((skill, index) => {
                                    return (
                                        <div className="listEachTypeOfSkillIN_li">
                                            <li key={index} id={skill.idSkill}>
                                                {skill.description}
                                                <button className="addSkill" onClick={(e) => { disassociateSkillFromEntity(e.target.parentElement.id) }}> - </button> </li>
                                        </div>
                                    )
                                }) : ''}
                            </ul>
                        </div> : ''}

                    {toolsList.length > 0 ?
                        <div className="containerEachTypeSkillIN">
                            <Title className='titleUserSkils-Types' title='TOOLS' />
                            <ul className="listEachTypeOfSkillIN_UL">
                                {toolsList.length > 0 ? (toolsList).map((skill, index) => {
                                    return (
                                        <div className="listEachTypeOfSkillIN_li">
                                            <li key={index} id={skill.idSkill}>
                                                {skill.description}
                                                <button className="addSkill" onClick={(e) => { disassociateSkillFromEntity(e.target.parentElement.id) }}> - </button>
                                            </li>
                                        </div>
                                    )
                                }) : ''}
                            </ul>
                        </div> : ''}

                    {knowledgeList.length > 0 ?
                        <div className="containerEachTypeSkillIN">
                            <Title className='titleUserSkils-Types' title='KNOWLEDGE' />
                            <ul className="listEachTypeOfSkillIN_UL">
                                {knowledgeList.length > 0 ? (knowledgeList).map((skill, index) => {
                                    return (
                                        <div className="listEachTypeOfSkillIN_li">
                                            <li key={index} id={skill.idSkill}>
                                                {skill.description}
                                                <button className="addSkill" onClick={(e) => { disassociateSkillFromEntity(e.target.parentElement.id) }}> - </button>
                                            </li>
                                        </div>)
                                }) : ''}
                            </ul>
                        </div> : ''}
                </div >
            </div >
        </div >
    )
}