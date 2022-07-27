import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { CKEditor } from '@ckeditor/ckeditor5-react';
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { associateInterestIdeaNecessityAPI, associateSkillIdeaNecessityAPI, disassociateInterestIdeaNecessityAPI, disassociateSkillIdeaNecessityAPI, editIdeaNecessityAPI, getAllIdeaNecessityByIdAPI, getAllInterestAssociateIdeaNecessityAPI, getAllSkillAssociateIdeaNecessityAPI, getUserInfoAPI } from "../../restApi";
import EditInterest from "../../components/EditInterestSkill/EditInterest";
import EditSkill from "../../components/EditInterestSkill/EditSkill";
import SamplePicture from "../../images/Wh1dlojD-imagem_video.jpg";
import CircleLoader from "../../components/layout/CircleLoader";
import Sidebar from "../../components/menu/Sidebar";
import Button from "../../components/layout/Button";
import Alert from "../../components/layout/Alert";
import Title from "../../components/layout/Title";
import './EditIdeaNecessity.css';

export default function LinkIdeaNecessity() {
    const [userInfo, setUserInfo] = useState('');
    const [ideaNecessity, setIdeaNecessity] = useState('');
    const [type, setType] = useState('')
    const [picture, setPicture] = useState(SamplePicture);
    const [selectedFile, setSelectedFile] = useState()
    const [preview, setPreview] = useState('');
    const [title, setTitle] = useState('');
    const [input, setInput] = useState('')
    const navigate = useNavigate();
    const { id } = useParams();
    const [messageAlert, setMessageAlert] = useState(true);
    const [alert, setAlert] = useState(false);
    var token = sessionStorage.getItem("token")
    const [showSuccesssMessage, setShowSuccessMessage] = useState(true);
    var [loading, setLoading] = useState(false);

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
        getAllIdeaNecessityByIdAPI(id, token, (ideaNecessity) => {
            setIdeaNecessity(ideaNecessity)
            setType(ideaNecessity.ideaOrNecessity)
            setPicture(ideaNecessity.imageIdeaNecessity ? ideaNecessity.imageIdeaNecessity : SamplePicture);
            setTitle(ideaNecessity.title);
            setTitle(ideaNecessity.title);
            setInput(ideaNecessity.description);
        });
    }, []);

    const onSelectFile = e => {
        if (!e.target.files || e.target.files.length === 0) {
            setSelectedFile(undefined)
            return
        }

        // I've kept this example simple by using the first image instead of multiple
        setSelectedFile(e.target.files[0])
    }

    useEffect(() => {
        if (!selectedFile) {return}

        const objectUrl = URL.createObjectURL(selectedFile)
        setPreview(objectUrl)
        setPicture(objectUrl)
        // free memory when ever this component is unmounted
        return () => URL.revokeObjectURL(objectUrl)
    }, [selectedFile])


    function editIdeaNecessityBTN() {
        setAlert(false)
        if(title =="" && input==""){
            setMessageAlert("It is not possible to complete the action, because the mandatory fields title and description are not filled.")
            setAlert(true)
        }else if(title =="" &&input !=""){
            setMessageAlert("It is not possible to complete the action, because the mandatory field title is not filled.")
            setAlert(true)
        }else if(title !="" &&input ==""){
            setMessageAlert("It is not possible to complete the action, because the mandatory field description is not filled.")
            setAlert(true)
        }else{
        var myJSON = JSON.stringify({ id: id, title: title, description: input, ideaOrNecessity: type})
        const formData = new FormData();
        if (selectedFile !== undefined) {
            formData.append("image", selectedFile);
        }

        formData.append("ideaNecessityJson", myJSON);
        editIdeaNecessityAPI(formData, id, token, (s) => {setShowSuccessMessage(true);setLoading(false)}, (e) => {setLoading(false) })
    }
    }

    useEffect(() => { setShowSuccessMessage(false) }, [title, input]);


    function resetAlert(e) {
        setLoading(false)
        setAlert(false)
    }

    useEffect(() => { resetAlert() }, [title, input, type])

    return (
        <>
        <Sidebar />
        <div className="EditIdeaNecesityRectangle">
                <div style={{ display: alert ? "block" : "none" }}>
                    <Alert className={"alert-warning "} text={messageAlert}/>
                </div>
                <div className='EditIdeaNecesity_titleFirst'>
                    <Title title='Edit Idea or Necessity ' className='EditIdeaNecessity' />
                </div>
                <div className='containerEditIdeaNecesity'>
                    <div className='editIdeaNecesityContent'>
                        <div className='editIdeaNecesityContent2'>
                            <div className='editIdeaNecesityContentWithTitleAndType'>
                                <div className="cointainerTitleTypeContent">
                                    <Title className={"typeIdeaOrIdea"} title={"Type:* "}/>
                                    {type == 'IDEA' ?
                                    <div className="wrapper">
                                        <input type="radio" name="select" id="option-1" value="IDEA" checked onChange={(e) => {setAlert(false); setType(e.target.value); }} />
                                        <input type="radio" name="select" id="option-2" value="NECESSITY" onChange={(e) => { setAlert(false); setType(e.target.value); }} />
                                        <label htmlFor="option-1" className="option option-1"><div className="dot" /><span> Idea</span></label>
                                        <label htmlFor="option-2" className="option option-2"><div className="dot" /><span>Necessity</span></label>
                                    </div> :
                                    <div className="wrapper">
                                        <input type="radio" name="select" id="option-1" value="IDEA" onChange={(e) => { setAlert(false); setType(e.target.value); }} />
                                        <input type="radio" name="select" id="option-2" value="NECESSITY" checked onChange={(e) => {setAlert(false); setType(e.target.value); }} />
                                        <label htmlFor="option-1" className="option option-1"><div className="dot" /><span> Idea</span></label>
                                        <label htmlFor="option-2" className="option option-2"><div className="dot" /><span>Necessity</span></label>
                                    </div>}

                                </div>
                                <div className="titleInputDIV" >
                                    <span className="span-editIdeaNecessity">{type ? type === 'NECESSITY' ? "Necessity's title:* " : "Idea's title:* " : 'Title:* '} </span>
                                    <input type="text" maxLength="50" className="titleInput" value={title} onChange={(e) => {setAlert(false); setTitle(e.target.value) }} />
                                </div>

                                <div className="inputPicture_EditIdea">
                                    <span className="inputPictureSpan_EditIdea" >
                                        {type ? type === 'NECESSITY' ? "Add a picture to your necessity:" : "Add a picture to your idea:" : 'Add a picture:'}
                                    </span> <br />
                                    <input className="picture-Idea" placeholder="Add a picture to your idea.." type="file" onChange={onSelectFile} />
                                </div>
                            </div>
                            <div className="imgDiv_EditIdeaNecesity">
                            <img className='imgImg' src={picture} />
                            </div>
                        </div>
                        <Title className={"descriptionEditor-EditIdeaNecesity"} title={"Description:* "}/>
                        <CKEditor editor={ClassicEditor} data={input} onChange={(event, editor) => {
                                const data = editor.getData();
                                setInput(data)
                                setAlert(false); 
                                }}
                            />
                        
                        <div className="editIdeaNecessityBTNDIV">
                            {showSuccesssMessage ?
                            <span className="showSuccesssMessageEditIdeaNecessity">
                            {type === 'NECESSITY' ? "Your necessity was edited sucessfully" :  "Your idea was edited sucessfully" } </span>:<>
                            {loading?"":<Button text={"Save"} className={"editIdeaNecessityBTN"} onclick={editIdeaNecessityBTN} type="submit"/>}</>
                            }



                        </div>
                    </div>
                    
                    <div className='searchInterestSkill_editIdea'>
                        <div className='searchInterestDIV_editIdea'>
                            <span className='editInterestsSpan_editIdeaNecessity'>Add Interests:</span>
                            <div className='SearchInterestSearch_editIdea'>
                            <EditInterest resetAlert={resetAlert} functionGetAll={getAllInterestAssociateIdeaNecessityAPI} functionAssociateInterestToEntity={associateInterestIdeaNecessityAPI} functionDisassociateInterestToEntity={disassociateInterestIdeaNecessityAPI}></EditInterest>
                            </div>
                        </div>
                        <div className='searchSkillDIV__editIdea'>
                            <span className='editSkillsSpan_editIdeaNecessity'>Add Skills:</span>
                            <div className='SearchSkillSearch_editIdea'>
                            <EditSkill resetAlert={resetAlert} functionGetAll={getAllSkillAssociateIdeaNecessityAPI} functionAssociateSkillToEntity={associateSkillIdeaNecessityAPI} functionDisassociateSkillToEntity={disassociateSkillIdeaNecessityAPI}></EditSkill>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}