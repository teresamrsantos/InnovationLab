import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { MdOutlineAddCircleOutline,MdOutlineHideSource } from "react-icons/md";
import { getAllIdeaNecessityUserIsCreatorAPI, getUserInfoAPI } from "../../restApi"
import CircleLoader from "../../components/layout/CircleLoader";
import List from "../../components/layout/List";
import Filter from "../../components/layout/Filter";
import Title from "../../components/layout/Title";
import Button from "../../components/layout/Button";
import Sidebar from "../../components/menu/Sidebar";
import Alert from "../../components/layout/Alert";
import './AllMyIdeaNecessity.css'

export default function AllIdeaNecessity() {
    const [userInfo, setUserInfo] = useState('');
    const [allMyIdeaNecessity, setAllMyIdeaNecessity] = useState(['allMyIdeaNecessity']);
    const [filterIdeaNecessity, setFilterIdeaNecessity] = useState("");
    const [interest, setInterest] = useState([]);
    const [skill, setSkill] = useState([]);
    const [alert, setAlert] = useState(false);
    const idUser =  userInfo.idUser;
    const navigate = useNavigate();
    var token =   sessionStorage.getItem("token");
    var userType = userInfo.userType;

    useEffect(() => {
        if (sessionStorage.getItem("token") === null) {
            navigate("/")
        }
    }, []);

    useEffect(() => {
        getUserInfoAPI(token,(usersInfo) => {
            setUserInfo(usersInfo)
                if (usersInfo.userType === 'VISITOR') {
                    navigate("/Home");
                }
        }, () => { 
            sessionStorage.clear(); 
            navigate("/");
        });
    }, []);

    useEffect(() => {
        getAllIdeaNecessityUserIsCreatorAPI(token, (allIdeaNecessity) => {
            setAllMyIdeaNecessity(allIdeaNecessity)
        })
    },[]);

    const sort_lists_decrescent = (key, list) => [...list].sort((b, a) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))
    const sort_lists_crescent = (key, list) => [...list].sort((a, b) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))

    function orderBy(event) {
        setAlert(false);
        var orderselect = event.value;
        var orderselectAux = orderselect.split("-")
        let newSortedList=[];

        if(orderselectAux[1]==='1'){
            newSortedList = sort_lists_crescent(orderselectAux[0], allMyIdeaNecessity)
        }else{
            newSortedList = sort_lists_decrescent(orderselectAux[0], allMyIdeaNecessity)
        }
        setAllMyIdeaNecessity(newSortedList)
    }

    function ideaNecessityBy(event) {
        setAlert(false);
        setFilterIdeaNecessity(event.value)
    }

    function resetListIdeaNecessity() {
        setAlert(false);
        getAllIdeaNecessityUserIsCreatorAPI(token, (allIdeaNecessity) => {
            setAllMyIdeaNecessity(allIdeaNecessity)
        } )
    }

    function interestSelect(e) {
        setAlert(false);
        let array = [];
        if (Array.isArray(e)) {
            e.map(x => {
                array.push(x.id)
            })
        }
        setInterest(array);
    }

    function skillSelect(e) {
        setAlert(false);
        let array = [];
        if (Array.isArray(e)) {
            e.map(x => {
                array.push(x.id)
            })
        }
        setSkill(array);
    }

    function setAlertChange(e){
        setAlert(e);
    }

    return (
        <>
            <Sidebar />
            {allMyIdeaNecessity != 'allMyIdeaNecessity'?
            <>
                <div style={{ display: alert ? "block" : "none" }}>
                    <Alert className={"alert-warning "} text={"It cannot be deleted. It is associated with a Project."}/>
                </div>
                <div className="allMyIdeaNecessity">
                    <div className="header-allMyIdeaNecessity">
                        <Title className="title-forum" title={"My Ideas & Necessities"}/>
                        <Button className='add-ideaNecessity' text={"Add new Idea/Necessity"} icon= { <MdOutlineAddCircleOutline/>} onclick={(event) => {navigate('/addIdea') }}/>
                    </div>
                    <Filter orderBy={orderBy} ideaNecessityBy={ideaNecessityBy} interestSelect={interestSelect} skillSelect={skillSelect} />
                    <div className="list-allMyIdeaNecessity">
                        { userInfo!=''?
                        <>
                            {(allMyIdeaNecessity != '' && userInfo!='') ? 
                            <List setAlertChange={setAlertChange} page='allMyIdeaNecessity'  resetListIdeaNecessity={resetListIdeaNecessity} userType={userType}
                            testinformation={allMyIdeaNecessity} filterIdeaNecessity={filterIdeaNecessity} interest={interest} skill={skill} idUser={idUser} />
                            :<div className="no-allmyIdeaNecessity">
                                <MdOutlineHideSource size={"20%"} color={"gray"}/>
                                <Title title="There are no ideas and necessities!"/>
                            </div>}
                        </>:""}
                    </div>
                </div>
            </>
            :<div className="div-loading">
                <CircleLoader/>
                <Title title="Loading..."/>
            </div>}
        </>
    )
}