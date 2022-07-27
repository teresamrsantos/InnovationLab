import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { MdOutlineAddCircleOutline, MdOutlineHideSource } from "react-icons/md";
import { getAllIdeaNecessityAPI, getUserInfoAPI } from "../../restApi"
import CircleLoader from "../../components/layout/CircleLoader";
import Filter from "../../components/layout/Filter";
import Button from "../../components/layout/Button";
import Sidebar from "../../components/menu/Sidebar";
import Title from "../../components/layout/Title";
import List from "../../components/layout/List";
import './AllIdeaNecessity.css'

export default function AllIdeaNecessity() {
    const [userInfo, setUserInfo] = useState('');
    const [allIdeaNecessity, setAllIdeaNecessity] = useState(['allIdeaNecessity']);
    const [filterIdeaNecessity, setFilterIdeaNecessity] = useState("");
    const [interest, setInterest] = useState([]);
    const [skill, setSkill] = useState([]);
    const navigate = useNavigate();
    var token =   sessionStorage.getItem("token");
    var userType = userInfo.userType;
    const idUser =  userInfo.idUser;


    useEffect(() => {
        getUserInfoAPI(token, (usersInfo) => {
            setUserInfo(usersInfo)
        }, () => { sessionStorage.clear(); 
            navigate("/") 
        })
    }, []);

    useEffect(() => {
        getAllIdeaNecessityAPI(token, (allIdeaNecessity) => {
            setAllIdeaNecessity(allIdeaNecessity)
        });
    }, []);

    useEffect(() => {
        if (sessionStorage.getItem("token") === null) {
            navigate("/")
        }
    }, []);

    const sort_lists_decrescent = (key, list) => [...list].sort((b, a) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))
    const sort_lists_crescent = (key, list) => [...list].sort((a, b) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))

    function orderBy(event) {
        var orderselect = event.value;
        var orderselectAux = orderselect.split("-")
        let newSortedList = [];

        if (orderselectAux[1] === '1') {
            newSortedList = sort_lists_crescent(orderselectAux[0], allIdeaNecessity)
        } else {
            newSortedList = sort_lists_decrescent(orderselectAux[0], allIdeaNecessity)
        }
        setAllIdeaNecessity(newSortedList)
    }

    function ideaNecessityBy(event) {
        setFilterIdeaNecessity(event.value)
    }

    function resetListIdeaNecessity() {
        setAllIdeaNecessity([''])
        getAllIdeaNecessityAPI(token, (allIdeaNecessity) => {
            setAllIdeaNecessity(allIdeaNecessity)
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

    return (
        <>
            <Sidebar />
            {allIdeaNecessity != 'allIdeaNecessity'?
            <div className="allIdeaNecessity">
                <div className="header-allIdeaNecessity">
                    <Title className="title-forum" title={"Ideas & Necessities Forum"}/>
                    {userType!=="VISITOR"?
                    <Button className='add-ideaNecessity' text={"Add new Idea/Necessity"} icon= { <MdOutlineAddCircleOutline/>} onclick={(event) => {navigate('/addIdea') }}/>
                    :""}
                </div>
                <Filter orderBy={orderBy} ideaNecessityBy={ideaNecessityBy} interestSelect={interestSelect} skillSelect={skillSelect} />
                <div className="list-allIdeaNecessity">
                    {userInfo!=''?
                    <>
                        {(allIdeaNecessity != '' && userInfo!='') ? 
                        <List page='allIdeaNecessity'  resetListIdeaNecessity={resetListIdeaNecessity} testinformation={allIdeaNecessity} 
                        filterIdeaNecessity={filterIdeaNecessity} interest={interest} skill={skill} idUser={idUser} userType={userType}/>
                        :<div className="no-allIdeaNecessity">
                            <MdOutlineHideSource size={"20%"} color={"gray"}/>
                            <Title title="There are no ideas and necessities!"/>
                        </div>}
                    </>
                    :""}
                </div>
            </div>
            :<div className="div-loading">
                <CircleLoader/>
                <Title title="Loading..."/>
            </div>}
        </>
    )
}