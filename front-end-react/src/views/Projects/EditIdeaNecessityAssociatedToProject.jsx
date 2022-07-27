
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Filter from '../../components/layout/Filter';
import List from '../../components/layout/List';
import { associateIdeaNecessityProjectAPI, disassociateIdeaNecessityProjectAPI, getAllIdeaNecessityNotDeletedAPI, getAllIdeasNecessitiedFromProjectAPI } from '../../restApi';
import './EditProject.css';
import Alert from "../../components/layout/Alert";




export default function EditIdeaNecessityAssociatedToProject(props) {
    const navigate = useNavigate();
    const token = sessionStorage.getItem('token');
    const { id } = useParams();
    const [allIdeaNecessity, setAllIdeaNecessity] = useState('');
    const [interest, setInterest] = useState([]);
    const [skill, setSkill] = useState([]);
    const [listOfAssociatedIdeas, setListOfAssociatedIdeas] = useState([])
    const [filterIdeaNecessity, setFilterIdeaNecessity] = useState("");
    const [youhavetochooseatleastonetoproceed, setYouhavetochooseatleastonetoproceed] = useState(true)
    var email = props.userInfo.email;


    useEffect(() => {
        getAllIdeasNecessitiedFromProjectAPI(
            token, id,
            (ideaNecessitiesList) => {

                setListOfAssociatedIdeas(ideaNecessitiesList)
            }, (error) => {
                console.log(error)
            }
        );

        getAllIdeaNecessityNotDeletedAPI(token,
            (ideaNecessitiesList) => {

                setAllIdeaNecessity(ideaNecessitiesList)
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
        getAllIdeaNecessityNotDeletedAPI(token, (allIdeaNecessity1) => {
            setAllIdeaNecessity(allIdeaNecessity1)
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


    function fillIdeaNecessityList(e) {
        setYouhavetochooseatleastonetoproceed(true)
        var idIdea = e.split('/')[0]
        associateIdeaNecessityProjectAPI(token, id, idIdea, (e) => (
            setListOfAssociatedIdeas(e)), (e) => (console.log(e)))

    }

    function removeIdeaNecessityList(e) {
        var idIdea = e.split('/')[0]

        if (listOfAssociatedIdeas.length === 1) {
 
            setYouhavetochooseatleastonetoproceed(false)
            setTimeout(() =>  setYouhavetochooseatleastonetoproceed(true), 5000)
return
        } else{
        disassociateIdeaNecessityProjectAPI(token, id, idIdea, (e) => (
            setListOfAssociatedIdeas(e)), (e) => (console.log(e)))
          }

    }

    return (

        <div className="divEditIdeasNecessitiesContainer">


            <span className='associateIdeaToProject'>Edit the associated ideas/necessities: </span>
            <div className='DIV-Youhavetochooseatleastone' style={{ display: youhavetochooseatleastonetoproceed ? "none" : "block" }}> <Alert className={"alert-warning"} text={'You have to choose at least one to proceed !'} /></div>
            <Filter orderBy={orderBy} ideaNecessityBy={ideaNecessityBy} interestSelect={interestSelect} skillSelect={skillSelect} />
            <div className="list-allIdeaNecessity">
                {(allIdeaNecessity === '' || listOfAssociatedIdeas === '') ? '' : <List page='editProject' fillIdeaNecessityList={fillIdeaNecessityList} removeIdeaNecessityList={removeIdeaNecessityList} resetListIdeaNecessity={resetListIdeaNecessity} testinformation={allIdeaNecessity} listOfAssociatedIdeas={listOfAssociatedIdeas}
                    filterIdeaNecessity={filterIdeaNecessity} interest={interest} skill={skill} email={email} token={token} />}
            </div>

        </div>
    )
}