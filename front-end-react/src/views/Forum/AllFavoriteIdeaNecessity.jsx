import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { MdOutlineAddCircleOutline } from "react-icons/md";
import { IoMdHeartDislike } from "react-icons/io";
import { getAllFavoriteIdeaNecessityAPI, getUserInfoAPI } from "../../restApi";
import CircleLoader from "../../components/layout/CircleLoader";
import Filter from "../../components/layout/Filter";
import Button from "../../components/layout/Button";
import Sidebar from "../../components/menu/Sidebar";
import Title from "../../components/layout/Title";
import List from "../../components/layout/List";
import './AllFavoriteIdeaNecessity.css'

export default function AllIdeaNecessity() {
    const [userInfo, setUserInfo] = useState('');
    const [allFavoriteIdeaNecessity, setAllFavoriteIdeaNecessity] = useState([]);
    const [filterIdeaNecessity, setFilterIdeaNecessity] = useState("");
    const [interest, setInterest] = useState([]);
    const [skill, setSkill] = useState([]);
    const [loading, setLoading] = useState("");
    const [size, setSize] = useState(0);
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
        getAllFavoriteIdeaNecessityAPI(token, (allIdeaNecessity) => {
            setAllFavoriteIdeaNecessity(allIdeaNecessity)
            setSize(allIdeaNecessity.length);
            setLoading("active");

        });
    },[userInfo]);

    const sort_lists_decrescent = (key, list) => [...list].sort((b, a) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))
    const sort_lists_crescent = (key, list) => [...list].sort((a, b) => (a[key] > b[key] ? 1 : a[key] < b[key] ? -1 : 0))

    function orderBy(event) {
        var orderselect = event.value;
        var orderselectAux = orderselect.split("-")
        let newSortedList=[];

        if(orderselectAux[1]==='1'){
            newSortedList = sort_lists_crescent(orderselectAux[0], allFavoriteIdeaNecessity)
        }else{
            newSortedList = sort_lists_decrescent(orderselectAux[0], allFavoriteIdeaNecessity)
        }
        setAllFavoriteIdeaNecessity(newSortedList)
    }

    function ideaNecessityBy(event) {
        setFilterIdeaNecessity(event.value)
    }

    function resetListIdeaNecessity() {
        getAllFavoriteIdeaNecessityAPI(token, (allIdeaNecessity) => {
            setAllFavoriteIdeaNecessity(allIdeaNecessity)
        })
    }

    function resetSize() {
        let newSize = (size*1)-1;
        setSize(newSize);
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
            <div className="allFavoriteIdeaNecessity">
                {loading != '' && userInfo!=''? 
                <>
                    <div className="header-allFavoriteIdeaNecessity">
                        <Title className="title-forum" title={"My Favorite Ideas & Necessities"}/>
                        <Button className='add-ideaNecessity' text={"Add new Idea/Necessity"} icon= { <MdOutlineAddCircleOutline/>} onclick={(event) => {navigate('/addIdea') }}/>
                    </div>
                    <Filter orderBy={orderBy} ideaNecessityBy={ideaNecessityBy} interestSelect={interestSelect} skillSelect={skillSelect} />
                    <div className="list-allIdeaNecessity">
                        {allFavoriteIdeaNecessity != '' && size > 0 ? 
                        <List page='allFavoriteIdeaNecessity' testinformation={allFavoriteIdeaNecessity} filterIdeaNecessity={filterIdeaNecessity}
                        resetListIdeaNecessity={resetListIdeaNecessity} resetSize={resetSize} userType={userType} interest={interest} skill={skill} idUser={idUser}/>
                        :<div className="no-postRelated">
                            <IoMdHeartDislike size={"20%"} color={"gray"}/>
                            <Title title="You don't have favorites!"/>
                        </div>}
                    </div>
                </>
                :<div className="div-loading">
                    <CircleLoader/>
                    <Title title="Loading..."/>
                </div>}
            </div>
        </>
    )
}