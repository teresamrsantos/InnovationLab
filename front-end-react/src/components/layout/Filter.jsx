import React, { useEffect, useState } from "react";
import Select from 'react-select'
import { useSelector } from "react-redux";
import { getAllInterestSelectAPI, getAllSkillSelectAPI, getAllIdeaNecessitySELECT } from "../../restApi"
import "./Filter.css";

export default (props) => {
    var page = props.page;
    const [interestList, setInterestList] = useState("");
    const [skillList, setSkillList] = useState("");
    const [ideaNecessityList, setIdeaNecessityList] = useState("");
    var token = sessionStorage.getItem("token")

    var classname = props.className || 'filter';

    const options1 = [{ value: 'IDEA', label: 'Idea' }, { value: 'NECESSITY', label: 'Necessity' }, { value: '', label: 'All' }]
    const options2 = [{ value: 'creationTime-1', label: 'Creation Date ⬇' },
    { value: 'creationTime-2', label: 'Creation Date ⬆' },
    { value: 'updateTime-1', label: 'Update Date ⬇' },
    { value: 'updateTime-2', label: 'Update Date ⬆' },
    { value: 'vote-1', label: 'Vote ⬇' },
    { value: 'vote-2', label: 'Vote ⬆' }]

    const options3 = [{ value: 'creationTime-1', label: 'Creation Date ⬇' },
    { value: 'creationTime-2', label: 'Creation Date ⬆' },
    { value: 'numberVacancies-2', label: 'Vacancies ⬇' },
    { value: 'numberVacancies-1', label: 'Vacancies ⬆' }]

    const options4 = [{ value: 'INPROGRESS', label: 'In Progress' }, { value: 'CONCLUDED', label: 'Concluded' }, { value: '', label: 'All' }]
    useEffect(() => {
        if (interestList === "") {
            getAllInterestSelectAPI(token, (interest) => {
                setInterestList(interest)
            })
        }
    }, []);

    useEffect(() => {
        if (skillList === "") {
            getAllSkillSelectAPI(token, (skill) => {
                setSkillList(skill)
            })
        }
    }, []);


    useEffect(() => {
        if (ideaNecessityList === "") {
            getAllIdeaNecessitySELECT(token, (idNecessity) => {
                setIdeaNecessityList(idNecessity)
            }, (e) => { console.log(e) })
        }
    }, []);


    return (
        <div>

            {page === 'favoriteprojects' ||  page ==='myProjects' ?

                <div className={classname}>
                    <Select className="select-projectConcludedInProgress" options={options4} placeholder="Concluded/InProgress" onChange={(e) => { props.filterProjectsBy(e) }} />
                    <Select className="select-order-Projects" options={options3} placeholder="Order by" onChange={(e) => { props.orderBy(e) }} />
                    <Select isClearable isMulti className="multiselect-interest-project" options={interestList} displayValue="name" placeholder="Filter by Interest" onChange={(e) => { props.interestSelect(e) }} />
                    <Select isClearable isMulti className="multiselect-skill-project" options={skillList} displayValue="name" placeholder="Filter by Skill and type" onChange={(e) => { props.skillSelect(e) }} />
                    <Select isClearable isMulti className="multiselect-idea-project" options={ideaNecessityList} displayValue="name" placeholder="Filter by Idea/Necessity" onChange={(e) => { props.ideaNecessitySelect(e) }} />
                </div>
                : page === 'projects' ?

                    <div className={classname}>

                        <Select className="select-order-Projects" options={options3} placeholder="Order by" onChange={(e) => { props.orderBy(e) }} />
                        <Select isClearable isMulti className="multiselect-interest-project" options={interestList} displayValue="name" placeholder="Filter by Interest" onChange={(e) => { props.interestSelect(e) }} />
                        <Select isClearable isMulti className="multiselect-skill-project" options={skillList} displayValue="name" placeholder="Filter by Skill and type" onChange={(e) => { props.skillSelect(e) }} />
                        <Select isClearable isMulti className="multiselect-idea-project" options={ideaNecessityList} displayValue="name" placeholder="Filter by Idea/Necessity" onChange={(e) => { props.ideaNecessitySelect(e) }} />
                    </div>

                    :
                    <div>

                        <div className={classname}>
                            <Select className="select-order" options={options2} placeholder="Order by" onChange={(e) => { props.orderBy(e) }} />
                            <Select className="select-ideanecessity" options={options1} placeholder="Filter by Idea/Necessity" onChange={(e) => { props.ideaNecessityBy(e) }} />
                            <Select isClearable isMulti className="multiselect-interest" options={interestList} displayValue="name" placeholder="Filter by Interest" onChange={(e) => { props.interestSelect(e) }} />
                            <Select isClearable isMulti className="multiselect-skill" options={skillList} displayValue="name" placeholder="Filter by Skill and type" onChange={(e) => { props.skillSelect(e) }} />
                        </div>
                    </div>
            }</div>
    )
};