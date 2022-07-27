import React, { useState } from "react";
import { getAllProjectsAPI, getUserInfoAPI } from "../../restApi"
import Sidebar from "../../components/menu/Sidebar";
import { useEffect } from "react";
import './AllProjects.css'
import Title from "../../components/layout/Title";
import Button from "../../components/layout/Button";
import Filter from "../../components/layout/Filter";
import ListProjects from "./ListProjects";
import { useNavigate } from "react-router-dom";
import CircleLoader from "../../components/layout/CircleLoader";
import { MdOutlineAddCircleOutline, MdOutlineHideSource } from "react-icons/md";

export default function AllProjects(props) {
    const token = sessionStorage.getItem("token");
    const [userInfo, setUserInfo] = useState('');

    const [allProjects, setAllProjects] = useState('allProjects')
    const [filterProjects, setFilterProjects] = useState("");
    const [interest, setInterest] = useState([]);
    const [ideaNecessity, setIdeaNecessity] = useState([]);
    const [skill, setSkill] = useState([]);
    var userType = userInfo.userType;
    const idUser = userInfo.idUser;
    const navigate = useNavigate();


    useEffect(() => {
        getUserInfoAPI(token, (usersInfo) => {
            setUserInfo(usersInfo)
            if(usersInfo.userType === 'VISITOR'){
                navigate("/Home")
            }
        }, ()=> {sessionStorage.clear(); navigate("/")});
    },
        []);

        useEffect(() => {
            if(sessionStorage.getItem("token") === null) 
            navigate("/")
            
        }, []);

        

    useEffect(() => {
        getAllProjectsAPI(
            token,
            (projectInfo) => {
                setAllProjects(projectInfo)
            },
            (error) => {
                console.log(error)
            }
        );
    }, []);

    const sort_lists_decrescent = (key, list) => [...list].sort((b, a) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))
    const sort_lists_crescent = (key, list) => [...list].sort((a, b) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))

    function orderBy(event) {
        var orderselect = event.value;
        var orderselectAux = orderselect.split("-")
        let newSortedList = [];

        if (orderselectAux[1] === '1') {
            newSortedList = sort_lists_crescent(orderselectAux[0], allProjects)
        } else {
            newSortedList = sort_lists_decrescent(orderselectAux[0], allProjects)
        }
        setAllProjects(newSortedList)
    }

    function resetListProjects() {
        getAllProjectsAPI(token, (projectInfo) => {
            setAllProjects(projectInfo)
        })
    }

    function interestSelect(e) {
        let array = [];
        if (Array.isArray(e)) {
            e.map(x => {
                array.push(x.id)
            })
        }
        setInterest(array);
    }

    function skillSelect(e) {
        let array = [];
        if (Array.isArray(e)) {
            e.map(x => {
                array.push(x.id)
            })
        }
        setSkill(array);
    }


    function ideaNecessitySelect(e) {
        let array = [];
        if (Array.isArray(e)) {
            e.map(x => {
                array.push(x.id)
            })
        }
        setIdeaNecessity(array);
    }

    return (
        <div>
            <Sidebar />

            <div className="allProjetctsRectangle">

                <div className='projectsHeader'>
                    <Title title='Projects' className='project-title'> </Title>
                    {userInfo == '' ? '' : userInfo.numberOfActiveProjects === 0 || userInfo.numberOfActiveProjects === undefined ?
                        <Button className='add-projects' text={"Add new Project "} icon={<MdOutlineAddCircleOutline />} onclick={(event) => { navigate('/addProject') }} /> :
                       ''}
                </div>


                <div className='filterProjectDiv'>
                    <Filter page='projects' classname='filterProjects' orderBy={orderBy} interestSelect={interestSelect} ideaNecessitySelect={ideaNecessitySelect} skillSelect={skillSelect} /></div>
                <div className="list-projects">

                    {(allProjects == 'allProjects' || userInfo == '') ? <div className="div-loading">
                        <CircleLoader />
                        <Title title="Loading..." /> </div> : allProjects.length > 0 ? <ListProjects page='allProjects' resetListProject={resetListProjects} testinformation={allProjects} interest={interest} skill={skill} ideaNecessity={ideaNecessity} idUser={idUser} userType={userType} /> : <div className="no-allIdeaNecessity">
                            <MdOutlineHideSource size={"20%"} color={"gray"} />
                            <Title title="There are no projects!" />
                        </div>}

                </div>

            </div>

        </div>


    )
}

{/* */ }