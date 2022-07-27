import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { CKEditor } from '@ckeditor/ckeditor5-react';
import { addIdeaNecessityAPI, getUserInfoAPI } from '../../restApi';
import SearchInterest from '../../components/SearchInterestSkill/SearchInterest';
import SearchSkill from '../../components/SearchInterestSkill/SearchSkill';
import SamplePicture from "../../images/Wh1dlojD-imagem_video.jpg"
import Button from "../../components/layout/Button";
import Sidebar from "../../components/menu/Sidebar";
import Alert from "../../components/layout/Alert";
import Title from "../../components/layout/Title";
import './AddIdeaNecessity.css';

export default function AddIdeaNecesity(props) {
    const navigate = useNavigate();
    const [selectedFile, setSelectedFile] = useState(undefined);
    const [userInfo, setUserInfo] = useState('');
    const [interestList, setInterestsList] = useState([]);
    const [skillList, setSkillList] = useState([]);
    const [type, setType] = useState('IDEA');
    const [title, setTitle] = useState('');
    const [input, setInput] = useState('');
    const [show, setShow] = useState(true);
    const [messageAlert, setMessageAlert] = useState(true);
    const [alert, setAlert] = useState(false);
    const [preview, setPreview] = useState(SamplePicture);
    var token = sessionStorage.getItem("token");
    var id = userInfo.id;

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

    function validateForm() {
        return title.length > 0 && type.length > 0 && input.length > 0
    }

    function fillInterestList(e) {
        setAlert(false)
        let array = interestList;
        array.push(e);
        setInterestsList(array);
    }

    function removeFromInterestList(e) {
        setAlert(false)
        let array = interestList;
        for (var i = 0; i < array.length; i++) {
            if (array[i] === e) {
                array.splice(i, 1);
            }
        }
        setInterestsList(array);
    }

    function fillSkillList(e) {
        setAlert(false)
        let array = skillList;
        array.push(e);
        setSkillList(array);
    }

    function removeFromskillList(e) {
        setAlert(false)
        let array = skillList;
        for (var i = 0; i < array.length; i++) {
            if (array[i] === e) {
                array.splice(i, 1);
            }
        }
        setSkillList(array);
    }

    function addIdeaNecessityBTN() {
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
        var myJSON = JSON.stringify({title: title,description: input, ideaOrNecessity: type, 
            interestAssociatedList: interestList, skillAssociatedList: skillList})

        const formData = new FormData();
        if (selectedFile != undefined) {
            formData.append("image", selectedFile);
        }

        formData.append("ideaNecessityJson", myJSON);
        addIdeaNecessityAPI(formData, token, id, (s) => { setShow(false) }, (e) => { })
        }
    }

    const onSelectFile = e => {
        setAlert(false)
        if (!e.target.files || e.target.files.length === 0) {
            setSelectedFile(undefined)
            return
        }
        // I've kept this example simple by using the first image instead of multiple
        setSelectedFile(e.target.files[0])
    }

    useEffect(() => {
        if (!selectedFile) { return }
        const objectUrl = URL.createObjectURL(selectedFile);
        setPreview(objectUrl);
        // free memory when ever this component is unmounted
        return () => URL.revokeObjectURL(objectUrl)
    }, [selectedFile])


    return (
        <>
            {/*https://www.youtube.com/watch?v=kykC7i9VUE4&t=93s*/}
            <Sidebar />
            <div className="AddIdeaNecesityRectangle" style={{ display: show ? "block" : "none" }}>
            <div style={{ display: alert ? "block" : "none" }}>
                <Alert className={"alert-warning "} text={messageAlert}/>
            </div><br/>
                <div className='AddIdeaNecesity_titleFirst'>
                    <Title title='Add a new Idea or Necessity ' className='AddIdeaNecesity' />
                </div>
                <div className='containerAddIdeaNecesity'>
                    <div className='addIdeaNecesityContent'>
                        <div className='addIdeaNecesityContent2'>
                            <div className='addIdeaNecesityContentWithTitleAndType'>
                                <div className="cointainerTitleTypeContent">
                                    <Title className={"typeIdeaOrIdea"} title={"Type:* "} />
                                    {type == 'IDEA' ?
                                    <div className="wrapper">
                                        <input type="radio" name="select" id="option-1" value="IDEA" checked onChange={(e) => { setAlert(false);setType(e.target.value); }} />
                                        <input type="radio" name="select" id="option-2" value="NECESSITY" onChange={(e) => { setAlert(false);setType(e.target.value); }} />
                                        <label htmlFor="option-1" className="option option-1"><div className="dot" /><span> Idea</span></label>
                                        <label htmlFor="option-2" className="option option-2"><div className="dot" /><span>Necessity</span></label>
                                    </div> :
                                    <div className="wrapper">
                                        <input type="radio" name="select" id="option-1" value="IDEA" onChange={(e) => { setAlert(false);setType(e.target.value); }} />
                                        <input type="radio" name="select" id="option-2" value="NECESSITY" checked onChange={(e) => { setAlert(false);setType(e.target.value); }} />
                                        <label htmlFor="option-1" className="option option-1"><div className="dot" /><span> Idea</span></label>
                                        <label htmlFor="option-2" className="option option-2"><div className="dot" /><span>Necessity</span></label>
                                    </div>}
                                </div>
                                <div className="titleInputDIV" >
                                    <span className="titleSpan">{type ? type === 'NECESSITY' ? "Necessity's title:* " : "Idea's title:* " : 'Title:* '} </span>
                                    <input type="text" maxLength="50" className="titleInput" value={title} onChange={(e) => { setAlert(false);setTitle(e.target.value) }} />
                                </div>
                                <div className="inputPicture_AddIdea">
                                    <span className="inputPictureSpan_AddIdea" >
                                        {type ? type === 'NECESSITY' ? "Add a picture to your necessity:" : "Add a picture to your idea:" : 'Add a picture:'}
                                    </span><br/>
                                    <input className="picture-Idea" placeholder="Add a picture to your idea.." type="file" onChange={onSelectFile} />
                                </div>
                            </div>
                            <div className="imgDiv_AddIdeaNecesity">
                                <img className='imgImg' src={preview} alt="" />
                            </div>
                        </div>
                        <Title className={"descriptionEditor-AddIdeaNecesity"} title={"Description:* "} />
                        <div className="editor-container">
                            <CKEditor editor={ClassicEditor} data={input} onChange={(event, editor) => {
                                setAlert(false)
                                const data = editor.getData();
                                setInput(data);
                            }}/>
                        </div>
                        <div className="addIdeaNecessityBTNDIV">
                            <Button className='addIdeaNecessityBTN' onclick={addIdeaNecessityBTN} type="submit" text='Submit new post' /*disabled={!validateForm()}*/ />
                        </div>
                    </div>
                    <div className='searchInterestSkill_addIdea'>
                        <div className='searchInterestDIV_addIdea'>
                            <span className='addInterestsSpan_addIdeaNecessity'>Add Interests:</span>
                            <div className='SearchInterestSearch_addIdea'>
                                <SearchInterest interestList={fillInterestList} removeFromInterestList={removeFromInterestList} />
                            </div>
                        </div>
                        <div className='searchSkillDIV__addIdea'>
                            <span className='addSkillsSpan_addIdeaNecessity'>Add Skills:</span>
                            <div className='SearchSkillSearch_addIdea'>
                                <SearchSkill skillList={fillSkillList} removeFromskillList={removeFromskillList} />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div className='successAddIdea' style={{ display: show ? "none" : "block" }}>
                <span className='spanSuccessAddIdea'>
                    Your {type ? type === 'NECESSITY' ? "necessity" : "idea" : 'post'} was added ! <br/> Thank you for sharing ! ❤️ 
                </span>
                <br /><br />
                <Link to="/Home" className='spanSuccessAddIdeaLink'> 
                    Click here to go back to the forum
                </Link>
            </div>
        </>
    )
}