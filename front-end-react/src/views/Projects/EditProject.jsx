import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { CKEditor } from '@ckeditor/ckeditor5-react';
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import EditInterest from '../../components/EditInterestSkill/EditInterest';
import EditSkill from '../../components/EditInterestSkill/EditSkill';
import Button from "../../components/layout/Button";
import Title from "../../components/layout/Title";
import Sidebar from "../../components/menu/Sidebar";
import SamplePicture from "../../images/kq8pf47zL-HG.jpg";
import Alert from "../../components/layout/Alert";
import { Spinner } from "reactstrap";
import {
    markProjectAsConcludedAPI,
    associateInterestProjectAPI, associateSkillProjectAPI, disassociateInterestProjectAPI, disassociateSkillProjectAPI, editProjectApi, getAllInterestsFromProjectAPI,
    getAllSkillsFromProjectAPI, getSpecificProjectAPI, getUserInfoAPI
} from '../../restApi';
import './EditProject.css';
import EditIdeaNecessityAssociatedToProject from './EditIdeaNecessityAssociatedToProject';
import * as FaIcons from 'react-icons/fa';

export default function EditProject(props) {
    const navigate = useNavigate();
    const token = sessionStorage.getItem('token');
    const [userInfo, setUserInfo] = useState('');
    const [showInfoProject, setShowInfoProject] = useState(0);
    const [title, setTitle] = useState('');
    const [maxNumberMembers, setMaxNumberMemebers] = useState('');
    const [input, setInput] = useState('');
    const [resources, setResources] = useState('');
    const [schedulle, setSchedulle] = useState('');
    const [selectedFile, setSelectedFile] = useState(undefined)
    const [preview, setPreview] = useState(SamplePicture)
    const [project, setProject] = useState('');
    const [interestList, setInterestList] = useState(['']);
    const [skillList, setSkillList] = useState(['']);
    let [concludeProjectVariable, setConcludeProject] = useState(false)
    let [messageAlert, setMessageAlert] = useState(true);
    let [alert, setAlert] = useState(false);
    var [loading, setLoading] = useState(false);
    const { id } = useParams();
    function validateForm() {
        return title.length > 0 && input.length > 0
    }


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


    useEffect(() => {
        getSpecificProjectAPI(token, id, (project) => {
            if (userInfo !== '' && userInfo.userType !== 'ADMIN') {
                if (project.membersList.some(member => member.idMember === userInfo.id && member.role === 'ADMIN')) {
                } else {
                    navigate('/myprojects')
                }
            }
            setProject(project);
            setInput(project.projectContent);
            setResources(project.projectResources);
            setSchedulle(project.projectPlan);
            setInterestList(project.interestAssociatedList);
            setSkillList(project.skillAssociatedList);
            setMaxNumberMemebers(project.numberMaxMembers);
            setTitle(project.title);
            setPreview(project.imageProject != null ? project.imageProject : SamplePicture);
        },
            (error) => {

            }
        );
    }, [userInfo]);


    function editProjectBTN() {
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

            })

            const formData = new FormData();
            formData.append("projectJSON", myJSON);

            if (selectedFile !== undefined) {
                formData.append("image", selectedFile);
            }

            editProjectApi(token, id, formData, (e) => { setShowSuccessMessage(true); setLoading(false) }, (e) => { setLoading(false) })
        }
    }

    useEffect(() => { setShowSuccessMessage(false) }, [maxNumberMembers, title, input, resources, schedulle, selectedFile]);
    const [showSuccesssMessage, setShowSuccessMessage] = useState(true);


    function projectAsConcluded(e) {
        markProjectAsConcludedAPI(token, id, (e) => {
            navigate('/myprojects')
        }, (e) => console.log(e))
    }


    function resetAlert(e) {
        setLoading(false)
        setAlert(false)
    }

    useEffect(() => { resetAlert() }, [title, input, schedulle, resources])

    function changeSeparators(e) {


        if (e.target.id === 'setInfoEditProject-0') {
            setShowInfoProject(0)
            document.getElementById('setInfoEditProject-0').classList.add('activatedSeparator')
            if (document.getElementById('setInfoEditProject-1').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoEditProject-1').classList.remove('activatedSeparator')
            }

        } else if (e.target.id === 'setInfoEditProject-1') {
            setShowInfoProject(1)
            document.getElementById('setInfoEditProject-1').classList.add('activatedSeparator')
            if (document.getElementById('setInfoEditProject-0').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoEditProject-0').classList.remove('activatedSeparator')
            }

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

    return (
        <>
            {/*https://www.youtube.com/watch?v=kykC7i9VUE4&t=93s*/}
            <Sidebar />


            <div className='EditProjectRectangle'>
                <div className={"alert-warningEdit"} style={{ display: alert ? "block" : "none" }}>
                    <Alert className={"alert-warning "} text={messageAlert} />
                </div>
                <div className="separatorButtonsDIV_EditProjects">
                    <button id='setInfoEditProject-0' className="projectDetailsButton activatedSeparator" onClick={(e) => (changeSeparators(e))}> Edit content Project </button>
                    <button className='projectDetailsButton' id='setInfoEditProject-1' onClick={(e) => (changeSeparators(e))}> Ideas/Necessities Associated </button>
                </div>


                <div style={{ display: concludeProjectVariable ? "block" : "none" }} className='concludeTheProjectDiv'>
                    <p> Are you sure you want to set the Project as concluded ?</p>
                    <button className='buttonLeaveProject' onClick={(e) => projectAsConcluded(e)}> Yes </button>
                    <button className='buttonLeaveProject' onClick={(e) => setConcludeProject(false)}> Cancel </button>
                    <p> This action cannot be undone. </p>
                </div>

                {project == '' || schedulle == '' || input == '' || resources == '' ? '' :
                    <div className='BIGcontainerEditProject' style={{ display: !concludeProjectVariable ? "block" : "none" }}>
                        <div className='containerEditProject' style={{ display: showInfoProject == 0 ? "flex" : "none" }}>
                            <div className='editProjectContent'>
                                <div className='EditProject_titleFirst'>
                                    <Title title='Edit Project ' className='EditProject'> </Title>
                                    <button className='buttonEditProjects-ManageUsers' onClick={(e) => { navigate('/manageUsersProject/' + id); }} >
                                        <FaIcons.FaUserEdit size={'18px'} onClick={(e) => { navigate('/manageUsersProject/' + id); }} /> </button>
                                    <button className='buttonMyProjectsEdit-ConcludeProject' onClick={(e) => setConcludeProject(true)}> Conclude Project </button>
                                </div>


                                <div className='container_EditProject_PICTUREMEMBERSTITLE'>

                                    <div className='DIVInputMembersAndPhoto_EditProject'>

                                        <div className="titleInputDIV_EditProject" >
                                            <div className="titleDiv_EditProject">
                                                <span className="titleSpan_EditProject"> Title Project: </span>
                                            </div>
                                            <input type="text" maxLength="50" className="titleInput_EditProject" value={title} onChange={(e) => { setTitle(e.target.value) }} />
                                        </div>
                                        <div className="inputPicture_EditProject">
                                            <span className="inputPictureSpan_EditProject" >Edit picture : </span> <br />
                                            <input className="picture_EditProject" type="file" onChange={onSelectFile} />  <br />
                                        </div>
                                        <div className="numberMaxMembers_inputDIV_EditProject">
                                            <label className="numberMaxMembers_label_EditProject">Number Maximum of members:</label><br />
                                            <input className="numberMaxMembers_input_EditProject" min={project.membersList.filter(e => e.role == 'ADMIN' || e.role == 'MEMBER').length} max='10' placeholder="Members" type="number" value={maxNumberMembers} onChange={(e) => setMaxNumberMemebers(e.target.value)} /> <span>&nbsp; members </span><br />
                                        </div>
                                    </div>
                                    <div className="imgDiv_EditProject"> <img className='imgImg' src={preview} alt="" /></div>
                                </div>
                                <div className='containerContentProject_EditProject'>
                                    <span className="titleEditor_EditProject"> Edit the content of the project: </span>
                                    <div className="editor-container_EditProject">


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
                                <div className="BigContainer_EditProject">
                                    <div className="ResourcesBigContainer_EditProject">
                                        <span className="titleResources_EditProject"> Project Resources: </span>

                                        <div className="resources-container_EditProject">


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
                                    <div className="SchedulleBigContainer_EditProject">
                                        <span className="titleschedulle_EditProject"> Project Schedulle </span>
                                        <div className="schedulle-container_EditProject">


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

                                <div className="editButtonDiv">
                                    {showSuccesssMessage ? 
                                    <span className='showSuccesssMessageEditProject'> ! Your project was edited sucessfully  </span> 
                                    : loading ? <Button className={!validateForm() ? 'EditProjectNecessityBTN btnDisabled' : 'EditProjectNecessityBTN'} type="submit" text={<Spinner style={{ width: '1.8rem', height: '1.8rem' }} />}
                                        children={false} /> :
                                        <Button className={!validateForm() ? 'EditProjectNecessityBTN btnDisabled' : 'EditProjectNecessityBTN'} onclick={editProjectBTN} type="submit" text='Edit project' />}
                                </div>
                            </div>
                            <div className='searchInterestSkill_EditProject'>
                                <div className='searchInterestDIV_EditProject'>
                                    <span className='addInterestsSpan_EditProject'> Edit Interests:  </span>
                                    <div className='SearchInterestSearch_EditProject'>
                                        <EditInterest resetAlert={resetAlert} functionGetAll={getAllInterestsFromProjectAPI} functionAssociateInterestToEntity={associateInterestProjectAPI} functionDisassociateInterestToEntity={disassociateInterestProjectAPI}></EditInterest>
                                    </div>
                                </div>
                                <div className='searchSkillDIV__EditProject'>
                                    <span className='addSkillsSpan_EditProject'> Edit Skills:  </span>
                                    <div className='SearchSkillSearch_EditProject'>
                                        <EditSkill resetAlert={resetAlert} functionGetAll={getAllSkillsFromProjectAPI} functionAssociateSkillToEntity={associateSkillProjectAPI} functionDisassociateSkillToEntity={disassociateSkillProjectAPI}></EditSkill>


                                    </div>
                                </div>
                            </div>
                        </div>


                        <div className='containerIdeasNecerssityAssociated' style={{ display: showInfoProject == 1 ? "flex" : "none" }}>
                            <div className='EditProject_Ideas/Necessities_titleFirst'>
                                <Title title='Ideas/Necessities associated to Project:' className='EditProject'> </Title>
                            </div>

                            <EditIdeaNecessityAssociatedToProject userInfo={userInfo} />
                        </div>
                    </div>}
            </div>
        </>)
}