import React, { useState } from "react";
import { getFavoriteProjectsFromUser, getUserInfoAPI } from "../../restApi"
import Sidebar from "../../components/menu/Sidebar";
import { useEffect } from "react";
import './AllProjects.css'
import Title from "../../components/layout/Title";
import Button from "../../components/layout/Button";
import Filter from "../../components/layout/Filter";
import ListProjects from "./ListProjects";
import { MdOutlineAddCircleOutline } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import CircleLoader from "../../components/layout/CircleLoader";
import { IoMdHeartDislike } from "react-icons/io";

export default function AllProjects(props) {
    const token = sessionStorage.getItem("token");
    const [userInfo, setUserInfo] = useState('');
    const [allProjects, setAllProjects] = useState('ap')
    const [filterProjects, setFilterProjects] = useState("");
    const [interest, setInterest] = useState([]);
    const [ideaNecessity, setIdeaNecessity] = useState([]);
    const [skill, setSkill] = useState([]);
    let [size, setSize] = useState(0);
    var userType = userInfo.userType;
    const idUser = userInfo.idUser;
    const navigate = useNavigate();


    function filterProjectsBy(event) {
        setFilterProjects(event.value)
    }

    useEffect(() => {
        getUserInfoAPI(token, (usersInfo) => {
            setUserInfo(usersInfo)
        }, (error) => {
        });
    },
        []);

function resetSize(){
    
    setSize( (size*1)-1)
}
    useEffect(() => {
        if (userInfo !== '') {
            getFavoriteProjectsFromUser(
                token, userInfo.idUser,
                (projectInfo) => {
                    setAllProjects(projectInfo)
                    setSize(projectInfo.length)
                },
                (error) => {
                    console.log(error)
                }
            );
        }
    }, [userInfo]);

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
        getFavoriteProjectsFromUser(token, userInfo.idUser, (projectInfo) => {
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
                    <Title title='Your Favorite Projects' className='project-title'> </Title>

                </div>

                <div className='filterProjectDiv'>
                    <Filter page='favoriteprojects' classname='filterProjects' orderBy={orderBy} interestSelect={interestSelect} ideaNecessitySelect={ideaNecessitySelect} skillSelect={skillSelect} filterProjectsBy={filterProjectsBy} /></div>
                <div className="list-projects">

                    {(allProjects == 'ap' || userInfo == '') ? <div className="div-loading">
                        <CircleLoader />
                        <Title title="Loading..." />
                    </div> : size>0 ? <ListProjects resetSize={resetSize} page='allFavoriteProjects' filterProjectsBy={filterProjectsBy} resetListProject={resetListProjects} testinformation={allProjects} interest={interest} skill={skill} ideaNecessity={ideaNecessity} idUser={idUser} userType={userType} concludedInProgress={filterProjects} /> : <div className="no-postRelated">
                        <IoMdHeartDislike size={"20%"} color={"gray"} />
                        <Title title="You don't have favorites!" />
                    </div>}

                </div>

            </div>

        </div>


    )
}

{/* */ }