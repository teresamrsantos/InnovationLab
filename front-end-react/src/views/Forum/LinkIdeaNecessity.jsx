import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { getAllIdsIdeaNecessityAssociateAPI,associateIdeaNecessityAPI, getAllIdeaNecessityNoDeleteAPI, getAllIdeaNecessityByIdAPI, getUserInfoAPI } from "../../restApi"
import { FaExclamationCircle } from "react-icons/fa";
import IdeaNecessity from "../../components/layout/IdeaNecessity";
import CircleLoader from "../../components/layout/CircleLoader";
import Title from "../../components/layout/Title";
import Text from "../../components/layout/Text";
import Sidebar from "../../components/menu/Sidebar";
import Button from "../../components/layout/Button";
import Alert from "../../components/layout/Alert";
import './LinkIdeaNecessity.css'

export default function LinkIdeaNecessity() {
    const [userInfo, setUserInfo] = useState('');
    const [title, setTitle] = useState('');
    const [justification, setJustification] = useState('');
    const [allIdeaNecessity, setAllIdeaNecessity] = useState([]);
    const [ideaNecessityLink, setIdeaNecessityLink] = useState('');
    const [ideaNecessity, setIdeaNecessity] = useState([]);
    const [idIdeaNecessity, setIdIdeaNecessity] = useState([]);
    const [alert, setAlert] = useState(false);
    const [messageAlert, setMessageAlert] = useState(true);
    const [show, setShow] = useState(true);
    const { id } = useParams();
    const navigate = useNavigate();
    var token = sessionStorage.getItem("token");

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
        getAllIdsIdeaNecessityAssociateAPI(id, token, (ids) => {
            setIdIdeaNecessity(ids)
        });
    }, []);

    useEffect(() => {
        getAllIdeaNecessityNoDeleteAPI(token, (allIdeaNecessity) => {
            setAllIdeaNecessity(allIdeaNecessity)
        });
    }, []);

    useEffect(() => {
        getAllIdeaNecessityByIdAPI(id, token, (ideaNecessity) => {
            setIdeaNecessity(ideaNecessity)
        });
    }, []);

    let listItems = [];
    if(allIdeaNecessity != "allIdeaNecessity"){
        listItems = allIdeaNecessity.filter(ideaNecessity => ideaNecessity.title.toLowerCase().includes(title.toLowerCase()) 
        && ideaNecessity.id != id && !idIdeaNecessity.includes(ideaNecessity.id)).map((ideaNecessity1) =>
            <button className={(ideaNecessity1.id == ideaNecessityLink) ? "btn-select" : "btn-idea"}value={ideaNecessity1.id} 
            id={ideaNecessity1.id} onClick={(event) => { setIdeaNecessityLink(event.target.id) }}>
                {ideaNecessity1.title}
            </button>
        );
    }

    function linkTwoIdeaNecessity(e) {
        setAlert(false);
        e.preventDefault();
        if(ideaNecessityLink==""&&justification==""){
            setMessageAlert("It is not possible to complete the action, because you didn't select the idea/necessity to associate and didn't write the justification.")
            setAlert(true)
        }else if(ideaNecessityLink!=""&&justification==""){
            setMessageAlert("It is not possible to complete the action, because you didn't write the justification ")
            setAlert(true)
        }else if(ideaNecessityLink==""&&justification!=""){
            setMessageAlert("It is not possible to complete the action, because you didn'tselect the idea/necessity.")
            setAlert(true)
        }else{
            var myJSON = JSON.stringify({ idAdd: ideaNecessityLink, description: justification });
            associateIdeaNecessityAPI(id, myJSON, token,((response) => {
                setShow(false);
                }),((error) => {
                    setMessageAlert("This association has already been!")
                    setAlert(true)
            }));
        }
    }

    function validateForm() {
        return ideaNecessityLink.length > 0 && justification.length > 0;
    }

    return (
        <div className="link-ideanecessity">
            <Sidebar />
            <div style={{ display: alert ? "block" : "none" }}>
                <Alert className={"alert-warning "} text={messageAlert}/>
            </div>
            <div style={{ display: show ? "block" : "none" }}>
            {(ideaNecessity!="" && allIdeaNecessity!=="allIdeaNecessity")?
            <>  
                <Title className="title-linkIdeaNecessity" title={"Related Posts to:"} />
                <IdeaNecessity  title={ideaNecessity.title} id={ideaNecessity.id} nameAuthor={ideaNecessity.nameAuthor}
                pictureUrl={ideaNecessity.imageIdeaNecessity} description={ideaNecessity.description} userType= {userInfo.userType}/>
                <div className="divGeral">
                    <div className="divJustification2">
                        <div className="search-linkIdeaNecessity">
                            <Text className={"legend-searchIdeaNecessity"} text={"The idea/necessity you want to link:"} />
                            <input className="input-searchIdeaNecessitybyTitle" type="text" placeholder="Search idea/necessity by title" onChange={(e) => {setAlert(false);;setTitle(e.target.value)}} />
                        </div>
                        {allIdeaNecessity!==""?
                        <div className="list-allIdeaNecessityTolink">
                            {listItems}
                        </div>
                        :<div className="no-linkIdeaNecessity">
                            <FaExclamationCircle size={"40%"} color={"gray"}/>
                            <Title title="There is no post to relate."/>
                        </div>}
                    </div>
                    <div className="divJustification1">
                        <textarea className="textarea-justification" placeholder="Justification link between ideas/necessities*" type="text" onChange={(e) => setJustification(e.target.value)} />
                        <Button className={"button-justification"} text={"Save"} type="submit" onclick={linkTwoIdeaNecessity} /*disabled={!validateForm()}*//>
                    </div>
                </div>
            </>
            :<div className="div-loading">
                <CircleLoader/>
                <Title title="Loading..."/>
            </div>}
            </div>
            <div className='successAddIdea' style={{ display: show ? "none" : "block" }}>
                <span className='spanSuccessAddIdea'>Association was successful! <br />Thank you for sharing ! ❤️ </span><br/><br/>
                <Link to={'/postRelatedIdeaNecessity/'+id} className='spanSuccessAddIdeaLink'> 
                    Click here to go back to Post Related!
                </Link> 
            </div>
        </div>
    )
}