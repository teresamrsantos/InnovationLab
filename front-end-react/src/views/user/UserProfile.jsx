import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { FaUserEdit } from "react-icons/fa";
import { GiSuitcase } from "react-icons/gi";
import { getUserInfoAPI, getUserInfoByIdAPI, getAllProjectUserIsAuthorByPermissionAPI } from '../../restApi';
import CircleLoader from "../../components/layout/CircleLoader";
import Sidebar from "../../components/menu/Sidebar";
import Button from "../../components/layout/Button";
import Title from "../../components/layout/Title";
import Text from "../../components/layout/Text";
import './UserProfile.css';

export default function UserProfile() {
    const [userInfo, setUserInfo] = useState('');
    const [usertype, setUsertype] = useState('');
    const [projectsList, setProjectsList] = useState('projectsList');
    const [interestsList, setInterestsList] = useState(['']);
    const [skillsList, setSkillsList] = useState(['']);
    const [knowledge, setKnowledge] = useState('');
    const [software, setSoftware] = useState('');
    const [hardware, setHardware] = useState('');
    const [tools, setTools] = useState('');
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
        getUserInfoByIdAPI(id, token, (usersInfo) => {
            if (usersInfo.userType === 'VISITOR') {
                navigate("/Home");
            }
            setUserInfo(usersInfo);
            setInterestsList(usersInfo.interestsList);
            setSkillsList(usersInfo.skillsList ? usersInfo.skillsList : '')
            setKnowledge(usersInfo.skillsList ? usersInfo.skillsList.filter(skill => skill.skillType === 'KNOWLEDGE') : '')
            setSoftware(usersInfo.skillsList ? usersInfo.skillsList.filter(skill => skill.skillType === 'SOFTWARE') : '');
            setHardware(usersInfo.skillsList ? usersInfo.skillsList.filter(skill => skill.skillType === 'HARDWARE') : '');
            setTools(usersInfo.skillsList ? usersInfo.skillsList.filter(skill => skill.skillType === 'TOOLS') : '');
        }, (error) => {
            (error.toString()).includes('403') ? setShow(false) : setShow(true)
        });
    }, []);

    useEffect(() => {
        getAllProjectUserIsAuthorByPermissionAPI(id, token, (projects) => setProjectsList(projects));
    }, []);

    useEffect(() => {
        getUserInfoAPI(token, (user) => { setUsertype(user.userType) }, (error) => { });
    }, []);

    let listProjects, hardwareList, softwareList, knowledgeList, toolsList, interestList = [];
    if (projectsList != "projectsList") {
        listProjects = projectsList.map((project) =>
            <div className="div-project">
                <Title className="titleProject-userProfile" title={project.title} />
                <div className="descriptionProject-userProfile" dangerouslySetInnerHTML={{ __html: project.projectContent }} />
                <div id="linha-horizontal"></div>
                <div className="align-right"><Button className={"read-more"} text={"read more"} onclick={(event) => { navigate('/projectDetails/' + project.idProject) }} /></div>
            </div>);

        interestList = interestsList.map((interest) => {
            return <Button className={"btn-interestAndSkill"} text={interest.description}/>
        });

        hardwareList = skillsList.map((skill) => {
            if (skill.skillType == "HARDWARE") {
                return <Button className={"btn-interestAndSkill"} text={skill.description} />
            }
        });

        softwareList = skillsList.map((skill) => {
            if (skill.skillType == "SOFTWARE") {
                return <Button className={"btn-interestAndSkill"} text={skill.description} />
            }
        });

        knowledgeList = skillsList.map((skill) => {
            if (skill.skillType == "KNOWLEDGE") {
                return <Button className={"btn-interestAndSkill"} text={skill.description} />
            }
        });

        toolsList = skillsList.map((skill) => {
            if (skill.skillType == "TOOLS") {
                return <Button className={"btn-interestAndSkill"} text={skill.description} />
            }
        });
    }

    return (
        <div className="userProfile">
            <Sidebar />
            <div style={{ display: show ? "block" : "none" }}>
                {(userInfo!="" && projectsList != "projectsList")?
                <>
                    <div className="block-row">
                        <div className="left-columnUserProfile">
                            <div className="block-row block-userProfile">
                                <div className="block-columnLeft">
                                    <Title className="name-userProfile" title={userInfo.firstName + " " + userInfo.lastName} />
                                    <img className={'userProfilePicture'} src={userInfo.pictureUrl} />
                                </div>
                                <div className="block-columnRight">
                                    <Title className="subtitle-userProfile" title={"About " + userInfo.firstName + "..."} />
                                    <div className="biography-userProfile" dangerouslySetInnerHTML={{ __html: userInfo.biography }} />
                                    <div className="block-row">
                                        <Title className="subtitle-userProfile" title={"Works in: "} />&nbsp;&nbsp;
                                        <Text className="workplace-userProfile" text={userInfo.workplace} />
                                    </div>
                                    <div className="block-row">
                                        <Title className="subtitle-userProfile" title={"Availability: "} />&nbsp;&nbsp;
                                        <Text className="available-userProfile" text={userInfo.availablehours + " hours/week"} />
                                    </div>
                                </div>
                            </div>
                            <Title className="project-userProfile" title={userInfo.firstName +"'s Projects:"} />
                            {projectsList!=""?
                            <div className="div-projects">
                                {listProjects}
                            </div>
                            :<div className="no-userprofile">
                                <GiSuitcase size={"20%"} color={"gray"}/>
                                <Title title="It hasn't associated projects."/>
                            </div>}
                        </div>
                        <div className="right-columnUserProfile">
                            {usertype=="ADMIN"?
                            <div className="div-editUserProfileBtn">
                                <Button className={"editUserProfileBtn"} iconBefore={<FaUserEdit  className="FaUserEditIcon"/>} text={" Edit Profile "} onclick={(event) => { navigate("/editprofile/"+id) }}/>
                            </div>
                            :""}
                            <Title className="interest-userProfile" title={userInfo.firstName +"'s interests:"} />
                            {interestsList!=""?<>{interestList}</>:<span>It hasn't associated interests.</span>}<br/><br/>
                            <Title className="skill-userProfile" title={userInfo.firstName +"'s skills:"} />
                            {skillsList!=""?
                            <>
                                {(hardware.length > 0) ?<><Title className="typeSkill-userProfile" title={"Hardware"} />{hardwareList}</>:''}
                                {(software.length > 0) ?<><Title className="typeSkill-userProfile" title={"Software"} />{softwareList}</>:''}
                                {(knowledge.length > 0) ?<><Title className="typeSkill-userProfile" title={"Knowledge"} />{knowledgeList}</>:''}
                                {(tools.length > 0) ?<><Title className="typeSkill-userProfile" title={"Tools"} />{toolsList}</>:''}
                            </>
                            :<span>It hasn't associated skills.</span>}
                        </div>
                    </div>
                </>
                :<div className="div-loading">
                    <CircleLoader/>
                    <Title title="Loading..."/>
                </div>}
            </div>
            <div className='successAddIdea' style={{ display: show ? "none" : "block" }}>
                <span className='spanSuccessAddIdea'>
                    You don't have permissions to view this profile!
                </span>
                <br/><br/>
                Click <Link to="/Home" className='spanSuccessAddProjectLink'> here </Link> to go back to the forum or
                <Link to="/projects" className='spanSuccessAddProjectLink'> here </Link>to go back to the projects.
            </div>
        </div>
    );
};