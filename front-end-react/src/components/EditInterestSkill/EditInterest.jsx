import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { addInterestAPI, searchInterestAPI } from "../../restApi";
import Title from "../layout/Title";
import './EditInterest.css';

export default function EditInterest(props) {
    const [interestsList, setInterestsList] = useState('');
    const [input, setInput] = useState("");
    const [data, setData] = useState("");
    const { id } = useParams();
    var functionGetAll_API = props.functionGetAll;
    var functionAssociateInterestToEntity_API = props.functionAssociateInterestToEntity;
    var functionDisassociateInterestToEntity_API = props.functionDisassociateInterestToEntity;
    var token = sessionStorage.getItem("token");

    useEffect(() => {
        functionGetAll_API(id, token, (interest) => {
            setInterestsList(interest)
        });
    }, []);

    useEffect(() => {
        if (input.length > 0) {
            searchInterestAPI(token, input, (response) => {
                response.length > 0 ? setData(response) : setData('')
            }, (e) => setData(''))
        } else if (input.length === 0) {
            setData('')
        }
    }, [input]);


    function addInterest(e) {
        if (data.length === 0) {
            addInterestAPI(token, input, (e) => { associateInterestToEntity(e) }, (e) => { console.log(e) })
        } else {
            associateInterestToEntity(e)
        }
    }

    function associateInterestToEntity(idInterest) {
        functionAssociateInterestToEntity_API(token, id, idInterest, (e) => {
            setInterestsList(e);
            setInput('');
        }, (e) => console.log(e))
    }

    function disassociateInterestFromEntity(idInterest) {
        props.resetAlert();
        functionDisassociateInterestToEntity_API(token, id, idInterest, (e) => { setInterestsList(e) })
    }

    var isContainedOnData = data.length > 0 ? data.some(item => item.description === input) : false;

    return (
        <div>
            <div className="search-container-InterestsToIdeaNecessity">
                <Title title='Search Interests' className='searchInterestsToIdeaNecessity' />
                <input type="text" placeholder="Search interests..." onChange={(e) => {  props.resetAlert(); setInput(e.target.value) }} value={input} />
            </div>
            <div>
                <ul>
                    {data.length > 0 ? (data).map((interest, index) => {
                        return (
                            <li key={interest.idInterest} id={interest.idInterest}> {interest.description}
                                {interestsList?.some(element => { return (element.description === interest.description) }) ?
                                    <button className='addInterestToIdeaNecessity' onClick={(e) => { disassociateInterestFromEntity(e.target.parentElement.id) }}> - </button> :
                                    <button className='addInterestToIdeaNecessity' onClick={(e) => { addInterest(e.target.parentElement.id) }}>+ </button>}
                            </li>
                        )
                    }) : ''}
                    {input.trim().length > 0 && !isContainedOnData ? (<li key={input.length}> {input} { } <button className='addInterest' onClick={addInterest}> + </button> </li>) : ''}
                </ul>
                <div>
                    <Title className='titleIdeaNecessityInterests' title='Your Interests:' />
                    <ul className='ulInterests-IdeaNecessity'>
                        {interestsList.length > 0 ? (interestsList).map((interest, index) => {
                            return (
                                <div className="divEachIdeaNecessityInterest">
                                    <li key={interest.idInterest} id={interest.idInterest}> {interest.description}
                                        <button className='addInterest' onClick={(e) => { disassociateInterestFromEntity(e.target.parentElement.id) }}>-</button>
                                    </li>
                                </div>
                            )
                        }) : ''}
                    </ul>
                </div>
            </div>
        </div >
    )
}
