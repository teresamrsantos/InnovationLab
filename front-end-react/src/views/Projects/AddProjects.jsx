import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import React from "react";
import { CKEditor } from '@ckeditor/ckeditor5-react';
import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import Button from "../../components/layout/Button";
import Title from "../../components/layout/Title";
import Sidebar from "../../components/menu/Sidebar";
import { addProjectAPI, getUserInfoAPI, getAllIdeaNecessityNotDeletedAPI } from '../../restApi';
import SamplePicture from "../../images/Wh1dlojD-imagem_video.jpg"
import { Link } from 'react-router-dom';
import { Spinner } from "reactstrap";
import SearchInterest from '../../components/SearchInterestSkill/SearchInterest';
import './AddProjects.css';
import Alert from "../../components/layout/Alert";
import SearchSkill from '../../components/SearchInterestSkill/SearchSkill';
import Filter from '../../components/layout/Filter'
import List from '../../components/layout/List'

export default function AddProject(props) {
    const navigate = useNavigate();
    const [selectedFile, setSelectedFile] = useState(undefined)
    var token = sessionStorage.getItem("token")
    const [preview, setPreview] = useState(SamplePicture)
    const [title, setTitle] = useState('')
    const [input, setInput] = useState('')
    const [resources, setResources] = useState('')
    const [schedulle, setSchedulle] = useState('')
    const [maxNumberMembers, setMaxNumberMemebers] = useState(4)
    const [show, setShow] = useState(true)
    const [allIdeaNecessity, setAllIdeaNecessity] = useState('');
    const [interest, setInterest] = useState([]);
    const [skill, setSkill] = useState([]);
    const [filterIdeaNecessity, setFilterIdeaNecessity] = useState("");
    const [viewAssociateIdeaNecessity, setViewAssociateIdeaNecessity] = useState(true)
    let [messageAlert, setMessageAlert] = useState(true);
    let [alert, setAlert] = useState(false);
    const [interestList, setInterestsList] = useState([]);
    const [skillList, setSkillList] = useState([]);
    const [listOfAssociatedIdeas, setListOfAssociatedIdeas] = useState([])
    const [userInfo, setUserInfo] = useState('');
    var [loading, setLoading] = useState(false);

    useEffect(() => {
        if (sessionStorage.getItem("token") === null) {
            navigate("/")
        }
    }, []);


    useEffect(() => {
        getUserInfoAPI(
            token,
            (usersInfo) => {
                setUserInfo(usersInfo)
                if (usersInfo.userType === 'VISITOR') {
                    navigate("/Home")
                }
            }, () => { sessionStorage.clear(); navigate("/") });
    }, []);
    var email = userInfo.email;

    function validateForm() {
        return title.length > 0 && input.length > 0
    }


    function fillInterestList(e) {
        let array = interestList;
        array.push(e)
        setInterestsList(array);
    }

    function removeFromInterestList(e) {
        let array = interestList;
        for (var i = 0; i < array.length; i++) {
            if (array[i] === e) {
                array.splice(i, 1);
            }

        }
        setInterestsList(array);
    }


    function fillSkillList(e) {
        let array = skillList;
        array.push(e)
        setSkillList(array);
    }


    function removeFromskillList(e) {

        let array = skillList;
        for (var i = 0; i < array.length; i++) {
            if (array[i] === e) {

                array.splice(i, 1);
            }

        }
        setSkillList(array);

    }



    function fillIdeaNecessityList(e) {
        setYouhavetochooseatleastonetoproceed(true)

        let array = listOfAssociatedIdeas;

        array.push(e)
        setListOfAssociatedIdeas(array);
    }

    function removeIdeaNecessityList(e) {
        let array = listOfAssociatedIdeas;
        for (var i = 0; i < array.length; i++) {
            if (array[i] === e) {
                array.splice(i, 1);
            }

        }


        setListOfAssociatedIdeas(array);

    }

    function resetAlert(e) {
        setLoading(false)
        setAlert(false)
    }

    useEffect(() => { resetAlert() }, [title, input, schedulle, resources])

    function addProjectBTN() {
        setAlert(false)


        if (title == "" && input == "") {
            setMessageAlert("It is not possible to complete the action, because the mandatory fields title and content are not filled.")
            setAlert(true)
        } else if (title == "" && input != "") {
            setMessageAlert("It is not possible to complete the action, because the mandatory field title is not filled.")
            setAlert(true)
        } else if (title != "" && input == "") {
            setMessageAlert("It is not possible to complete the action, because the mandatory field content is not filled.")
            setAlert(true)
        } else {
            setLoading(true)
            var myJSON = JSON.stringify({
                numberMaxMembers: maxNumberMembers,
                title: title,
                projectContent: input,
                projectResources: resources,
                projectPlan: schedulle,
                interestAssociatedList: interestList,
                skillAssociatedList: skillList
            })


            const formData = new FormData();
            if (selectedFile != undefined) {
                formData.append("image", selectedFile);
            }
            formData.append("projectJson", myJSON);
            let uniqueIdeaAssociated = [...new Set(listOfAssociatedIdeas)];
            var idPIdeas = uniqueIdeaAssociated.slice(0, uniqueIdeaAssociated.length).join(',')

            addProjectAPI(formData, token, idPIdeas, userInfo.id, (s) => { setShow(false); setLoading(false) }, (e) => { setLoading(false) })
        }
    }


    const onSelectFile = e => {
        if (!e.target.files || e.target.files.length === 0) {
            setSelectedFile(undefined)
            return
        }
        // I've kept this example simple by using the first image instead of multiple
        setSelectedFile(e.target.files[0])
    }

    useEffect(() => {
        if (!selectedFile) {
            return
        }

        const objectUrl = URL.createObjectURL(selectedFile)
        setPreview(objectUrl)
        // free memory when ever this component is unmounted
        return () => URL.revokeObjectURL(objectUrl)
    }, [selectedFile])

    useEffect(() => {
        getAllIdeaNecessityNotDeletedAPI(token, (allIdeaNecessity1) => {
            if (allIdeaNecessity === '') {
                setAllIdeaNecessity(allIdeaNecessity1)
            }
        })
    })

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

    const [youhavetochooseatleastonetoproceed, setYouhavetochooseatleastonetoproceed] = useState(true)

    return (
        <>
            {/*https://www.youtube.com/watch?v=kykC7i9VUE4&t=93s*/}
            <Sidebar />
            <div className='alertMesage-Youhavetochooseatleastonetoproceed-addProject' style={{ display: youhavetochooseatleastonetoproceed ? "none" : "block" }}>
                <Alert className={"alert-warning "} text={"You have to choose at least one to proceed !"} />

            </div>

            <div className={"alert-warningEdit"} style={{ display: alert ? "block" : "none" }}>
                <Alert className={"alert-warning "} text={messageAlert} />
            </div>
            <div className="AddProjectRectangle" style={{ display: show ? "block" : "none" }}>


                <div className='AddProject_titleFirst'>

                    <Title title='Add a new Project ' className='AddProject'> </Title>

                </div>

                <div className='ADDPROJECT_associateIdeaToProject' style={{ display: viewAssociateIdeaNecessity ? "block" : "none" }}>

                    <span className='associateIdeaToProject'>Start by defining what Idea/Necessity are you attending to: </span>

                    <div className='DIVbuttonNext_AddToProject' >
                        <button className='buttonNext_AddToProject'
                            onClick={(e) => {
                                if (listOfAssociatedIdeas == '') { setYouhavetochooseatleastonetoproceed(false) }
                                else { setViewAssociateIdeaNecessity(false) }
                            }} >Next to Project Details</button>
                    </div>
                    <Filter orderBy={orderBy} ideaNecessityBy={ideaNecessityBy} interestSelect={interestSelect} skillSelect={skillSelect} />
                    <div className="list-allIdeaNecessity">
                        {(allIdeaNecessity === '') ? '' : <List page='addProject' fillIdeaNecessityList={fillIdeaNecessityList} removeIdeaNecessityList={removeIdeaNecessityList} resetListIdeaNecessity={resetListIdeaNecessity} testinformation={allIdeaNecessity}
                            filterIdeaNecessity={filterIdeaNecessity} interest={interest} skill={skill} email={email} token={token} />}
                    </div>
                </div>
                <div className="containerAddProject" style={{ display: viewAssociateIdeaNecessity ? "none" : "flex" }}>

                    <div className='addProjectContent'>

                        <div className='container_AddProject_PICTUREMEMBERSTITLE'>

                            <div className='DIVInputMembersAndPhoto'>

                                <div className="titleInputDIV_AddProject" >
                                    <div className="titleDiv_AddProject">
                                        <span className="titleSpan_AddProject"> Title Project:* </span>
                                    </div>

                                    <input type="text" maxLength="50" className="titleInput_AddProject" value={title} onChange={(e) => { setTitle(e.target.value) }} />
                                </div>

                                <div className="inputPicture_AddProject">
                                    <span className="inputPictureSpan_AddProject" >Add a picture to your project: </span> <br />
                                    <input className="picture_AddProject" placeholder="Add a picture to your project" type="file" onChange={onSelectFile} />  <br />
                                </div>

                                <div className="numberMaxMembers_inputDIV">
                                    <label className="numberMaxMembers_label_AddProject">Number Maximum of members:*</label><br />
                                    <input className="numberMaxMembers_input" min="1" max='10' placeholder="Members" type="number" value={maxNumberMembers} onChange={(e) => setMaxNumberMemebers(e.target.value)} /> <span>&nbsp; members </span><br />
                                </div>
                            </div>
                            <div className="imgDiv_AddProject"> <img className='imgImg' src={preview} alt="" /></div>





                        </div>
                        <div className='containerContentProject_AddProject'>
                            <span className="titleEditor_AddProject"> Tell us what your project will achieve:* </span>
                            <div className="editor-container_AddProject">


                                <CKEditor
                                    editor={ClassicEditor}
                                    data={input}
                                    onChange={(event, editor) => {
                                        const data = editor.getData();
                                        setInput(data)
                                    }}

                                />



                            </div>
                        </div>
                        <div className="BigContainer_AddProject">
                            <div className="ResourcesBigContainer_AddProject">
                                <span className="titleResources_AddProject"> Project Resources: </span>

                                <div className="resources-container_AddProject">


                                    <CKEditor
                                        editor={ClassicEditor}
                                        data={resources}
                                        onChange={(event, editor) => {
                                            const dataResources = editor.getData();
                                            setResources(dataResources)
                                        }}

                                    />


                                </div>
                            </div>
                            <div className="SchedulleBigContainer_AddProject">
                                <span className="titleschedulle_AddProject"> Project Schedulle </span>
                                <div className="schedulle-container_AddProject">


                                    <CKEditor
                                        editor={ClassicEditor}
                                        data={schedulle}
                                        onChange={(event, editor) => {
                                            const dataSchedulle = editor.getData();
                                            setSchedulle(dataSchedulle)
                                        }}

                                    />


                                </div>
                            </div>
                        </div>

                        <div className="addButtonDiv">
                            {loading ? <Button className={!validateForm() ? 'AddProjectNecessityBTN btnDisabled' : 'AddProjectNecessityBTN'} type="submit" text={<Spinner style={{ width: '1.8rem', height: '1.8rem' }} />}
                                children={false} /> :
                                <Button className={!validateForm() ? 'AddProjectNecessityBTN btnDisabled' : 'AddProjectNecessityBTN '} onclick={addProjectBTN} type="submit" text='Submit new project' />}
                        </div>
                    </div>


                    <div className='searchInterestSkill_AddProject'>
                        <div className='searchInterestDIV_AddProject'>
                            <span className='addInterestsSpan_AddProject'> Add Interests:  </span>
                            <div className='SearchInterestSearch_AddProject'>
                                <SearchInterest interestList={fillInterestList} removeFromInterestList={removeFromInterestList}   ></SearchInterest>
                            </div>
                        </div>
                        <div className='searchSkillDIV__AddProject'>
                            <span className='addSkillsSpan_AddProject'> Add Skills:  </span>
                            <div className='SearchSkillSearch_AddProject'>
                                <SearchSkill skillList={fillSkillList} removeFromskillList={removeFromskillList}></SearchSkill>
                            </div>
                        </div>
                    </div>
                </div>

            </div>  <div className='successAddProject' style={{ display: show ? "none" : "block" }}>
                <span className='spanSuccessAddProject'>Your project was added ! <br />
                    Thank you for sharing ! ❤️ </span>
                <br /><br /> Click
                <Link to="/myprojects" className='spanSuccessAddProjectLink'> here </Link> to go back to your projects !</div></>)
}