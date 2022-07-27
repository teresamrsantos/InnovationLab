import { useEffect, useState } from "react";

import { useSelector } from "react-redux";
import Title from "../layout/Title";
import { addInterestAPI, associateInterestToUserAPI, disassociateInterestToUserAPI, searchInterestAPI } from "../../restApi";
import './SearchInterest.css';
import * as AiIcons from 'react-icons/ai';


export default function SearchInterest(props) {
    const [input, setInput] = useState("")
    const [data, setData] = useState("")
    const [interestsList, setInterestsList] = useState([])

    var token = sessionStorage.getItem("token")



    useEffect(() => {

        if (input.length > 0) {
            searchInterestAPI(token, input, (response) => {
                response.length > 0 ? setData(response
                ) : setData('')
            }, (e) => setData(''))
        } else if (input.length === 0) {
            setData('')
        }
    }, [input])


    function addToArray(id, description) {

        var data = { description: description, id: id }
        let array = interestsList;

        // array.push(data)
        setInterestsList([...interestsList, data]);
        //setInterestsList(array);
    }


    var isContainedOnData = data.length > 0 ? data.some(item => item.description === input) : false;



    function addInterest(id, description) {
        if (description === undefined) {
            addInterestAPI(token, input,
                (e) => {
                    props.interestList(e)
                    addToArray(e, input)
                }, (e) => { })
        } else {
            props.interestList(id)
            addToArray(id, description)
        }
        setInput('')
    }

    useEffect(() => {
        removeAll()
    }, [props.removeAll])


    function removeAll() {
        if (props.removeAll == true) {
            setInterestsList([])
        }
    }

    
    function removeInterest(id, description) {
        const arr = interestsList.filter((item) => item.id != id);
        setInterestsList(arr);
        props.removeFromInterestList(id)
    }


    return (

        <div className='menu-Content-container-Interests_forum'>
            <div className="search-container-Interests_forum">
                <Title title='Search Interests' className='searchInterests_forum' />
                <input type="text" placeholder="Search interests..." className="searchInterest_forum" onChange={(e) => { setInput(e.target.value) }} value={input} />
            </div>
            <div>
                <ul className="listDataInterest_ul_forum">
                    {data.length > 0 ? (data).map((interest, index) => {
                        return (


                            <li className="interestLi_forum" key={interest.idInterest} value={interest.description} id={interest.idInterest}> {interest.description}

                                {interestsList.length > 0 ?
                                    interestsList.some(element => { return (element.description === interest.description) }) ?
                                        '' :
                                        <button onClick={(e) => { addInterest(e.target.parentElement.id, interest.description) }} className="addInterest_forum">  + </button>
                                    : <button onClick={(e) => { addInterest(e.target.parentElement.id, interest.description) }} className="addInterest_forum">  + </button>}

                            </li>
                        )
                    }) : ''}

                    {input.trim().length > 0 && !isContainedOnData ? (<li key={input.length}> {input} { } <button className='addInterest_forum' onClick={addInterest}> + </button> </li>)
                        : ''}
                </ul>
                <div className="ideaInterests">
                    <Title className='ideaInterestsInterests' title='Interests:'></Title>
                    <div className="ideaInterests-Container">
                        <ul className='ulideaInterests'>
                            {interestsList.length > 0 ? (interestsList).map((interest, index) => {

                                return (
                                    <div className="divEachUserInterest_forum">
                                        <li key={interest.id} id={interest.id}> {interest.description} <button className='addInterest_forum' onClick={(e) => removeInterest(e.target.parentElement.id)}> - </button> </li>
                                    </div>
                                )
                            }) : ''}
                        </ul>
                    </div>
                </div>


            </div>
        </div >)
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