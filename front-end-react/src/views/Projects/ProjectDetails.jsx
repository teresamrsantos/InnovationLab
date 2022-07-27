import { useState } from "react";
import { useEffect } from "react";
import { useNavigate, useParams, Link} from "react-router-dom";
import * as BsIcons from 'react-icons/bs';
import * as FaIcons from 'react-icons/fa';
import Title from "../../components/layout/Title";
import Sidebar from "../../components/menu/Sidebar";
import NoPictureProject from '../../images/noproject.png';
import LineIdeaNecessity from '../../components/layout/LineIdeaNecessity'
import {
    getSpecificProjectAPI, getUserInfoAPI, requestToJoinProjectAPI, removeProjectFavoritesAPI, addProjectFavoritesAPI, getAllSkillAssociateProjectAPI, getAllInterestAssociateProjectAPI, getAllIdeasNecessityFromProjectAPI
} from "../../restApi";
import InterestAndSkill from "../../components/layout/InterestAndSkill";
import './ProjectDetails.css';
import picture1 from "../../images/TITANS1.png";
import picture2 from "../../images/TITANS2.png";
import picture3 from "../../images/TITANS3.png";

export default function ProjectDetails() {
    var token = sessionStorage.getItem("token")
    const navigate = useNavigate();
    const [showInfoProject, setShowInfoProject] = useState(true);
    const [projectInfo, setProjectInfo] = useState();
    const { id } = useParams();
    const [userAlreadyRequested, setUserAlreadyRequested] = useState(false);
    const [userAlreadyInvited, setUserAlreadyInvited] = useState(false);
    const [userAlreadyMember, setUserAlreadyMember] = useState(false);
    const [noVacancies, setNoVacancies] = useState(false);
    const [membersList, setMembersList] = useState([]);
    const [favorite, setFavorite] = useState(false)
    const [skillList, setSkillList] = useState([])
    const [interestList, setInterestList] = useState([])
    const [ideaNecessityList, setIdeaNecessityList] = useState([])
    const images = [
        picture1,
        picture2,
        picture3,
    ];
    {/*availabilityList.includes(props.idUser)*/ }


    const [userInfo, setUserInfo] = useState('');
    useEffect(() => {
        getUserInfoAPI(
            token,
            (usersInfo) => {
                setUserInfo(usersInfo)


            },
            (error) => {

            }
        );
    }, []);


    useEffect(() => {


        getSpecificProjectAPI(token, id, (project) => {

            setProjectInfo(project)
            setNoVacancies(project.numberVacancies === 0 ? true : false)
            setUserAlreadyRequested(project.membersList.some(e => e.idMember === userInfo.id && e.role === 'REQUEST') ? true : false)
            setUserAlreadyMember(project.membersList.some(e => e.idMember === userInfo.id && e.role === 'MEMBER' || e.idMember === userInfo.id && e.role === 'ADMIN') ? true : false)
            setUserAlreadyInvited(project.membersList.some(e => e.idMember === userInfo.id && e.role === 'INVITE') ? true : false)
            setMembersList(project.membersList.filter(e => e.role !== 'REQUEST' && e.role !== 'INVITE'  && e.role !== 'NOTPARTICIPATINGANYMORE'))
            setFavorite((project.userListFavorites.includes(userInfo.id)) ? true : false)

        })

    }, [userInfo]);


    function changeSeparators(e) {

        if (e.target.id === 'setInfoProject-true') {
            setShowInfoProject(true)
            document.getElementById('setInfoProject-true').classList.add('activatedSeparator')
            if (document.getElementById('setInfoProject-false').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoProject-false').classList.remove('activatedSeparator')
            }
        } else if (e.target.id === 'setInfoProject-false') {
            setShowInfoProject(false)
            document.getElementById('setInfoProject-false').classList.add('activatedSeparator')
            if (document.getElementById('setInfoProject-true').classList.contains('activatedSeparator')) {
                document.getElementById('setInfoProject-true').classList.remove('activatedSeparator')
            }
        }
    }



    function askToBeAMember() {
        requestToJoinProjectAPI(token, id, () => { setUserAlreadyRequested(true) }, (error) => { console.log(error) })

    }


    function addProjectFavorite(e) {
        addProjectFavoritesAPI(token, id, (e) => {
            setFavorite(true)
        }, (e) => console.log(e))
    }


    function removeProjectFavorite(e) {

        removeProjectFavoritesAPI(token, id, (e) => {
            setFavorite(false)
        }, (e) => console.log(e))
    }


    useEffect(() => {
        getAllSkillAssociateProjectAPI(id, token, (skill) => {
            setSkillList(skill)
        });
    }, []);

    useEffect(() => {
        getAllInterestAssociateProjectAPI(id, token, (interest) => {
            setInterestList(interest)
        });
    }, []);


    useEffect(() => {
        getAllIdeasNecessityFromProjectAPI(id, token, (ideasNecessity) => {
            setIdeaNecessityList(ideasNecessity)
        });
    }, []);

    useEffect(() => {
        if (sessionStorage.getItem("token") === null) {
            navigate("/")
        }
    }, []);
    
    return (
        <div>
            <Sidebar />
            <div className='detailsRectangle'>

                <div className="separatorButtonsDIV">
                    <button className='btnGoBack' onClick={(e) => { navigate('/projects') }}><BsIcons.BsFillArrowLeftSquareFill size={'25px'} /></button>
                    <button id='setInfoProject-true' className="projectDetailsButton activatedSeparator" onClick={(e) => (changeSeparators(e))}> Show Project Details </button>
                    <button className='projectDetailsButton' id='setInfoProject-false' onClick={(e) => (changeSeparators(e))}>  Related Ideas or Necessities </button>
                </div>
                <div>

                    {projectInfo !== null && projectInfo !== undefined && userInfo.id !== undefined ?
                        <div className='projectContent'>
                            <div className="projectTitleDivAndFavorite">
                                <div className="projectTitleDiv">
                                    {favorite ?
                                        <button className='btn-favorite-project' onClick={removeProjectFavorite}> <FaIcons.FaHeart size='30px' color="C01111" onClick={removeProjectFavorite} /></button> :
                                        <button className='btn-favorite-project' onClick={addProjectFavorite}> <FaIcons.FaRegHeart size='30px' color="C01111" onClick={addProjectFavorite} /></button>}

                                    <Title title={projectInfo.title} className='projectDetails-title'></Title>
                                    
                    

                                </div>
                                {userInfo.userType === 'ADMIN'  &&  projectInfo.projectStatus !== "CONCLUDED" ?
                                    <div className='btns_admin_ProjectDetails' >
                                        <button className='buttonMyProjectsEdit' onClick={(e) => { navigate('/editProject/' + id); }}>  <FaIcons.FaEdit onClick={(e) => { navigate('/editProject/' + id); }} /> </button>
                                        <button className='buttonMyProjectsEdit-ManageUsers' onClick={(e) => { navigate('/manageUsersProject/' + id); }} >  <FaIcons.FaUserEdit onClick={(e) => { navigate('/manageUsersProject/' + id); }} /> </button>


                                    </div> : ''}

                            </div>
                            <div className="body-projectToSee">
                                <div className="projectContentIMGANDTEXTDIV_1">
                                {projectInfo.projectStatus !== "INPROGRESS" ? <span>Project concluded</span>:''}
                                    <div className="projectContentIMGANDTEXT">
                   
                                        {/*<Text className={"text-ideaNecessityToSee"} text={props.description} />*/}
                                        <div dangerouslySetInnerHTML={{ __html: projectInfo.projectContent }}></div>
                                        <img className='picture-projectDetails' src={projectInfo.imageProject === null ? NoPictureProject : projectInfo.imageProject} />

                                    </div>

                                    <div className='projectButton'>


                                        <div className='divButtonPartOfProject'>
                                            {projectInfo.projectStatus !== "INPROGRESS" ?
                                                <button className='btn-toBeAMember' disabled> This project is concluded</button> :
                                                userAlreadyMember ?
                                                    <button className='btn-toBeAMember'disabled > You're already a member</button> :
                                                    noVacancies ?
                                                        <button className='btn-toBeAMember' disabled > Sorry. No vacancies on this project</button> :
                                                        userAlreadyInvited ?
                                                            <button className='btn-toBeAMember' disabled> You have a pending invitation to this project</button> :
                                                            userInfo.numberOfActiveProjects > 0 ?
                                                                <button className='btn-toBeAMember' disabled> You're already participating on a project</button> :
                                                                userAlreadyRequested ?
                                                                    <button className='btn-toBeAMember' disabled> Your request is being evaluated</button> :
                                                                    <button className='btn-toBeAMember' onClick={askToBeAMember}> Ask to be a part of this project</button>

                                            }
                                        </div>
                                    </div>
                                </div>
                                <div className='showProjectDetails' style={{ display: showInfoProject ? "block" : "none" }}>

                                    <div className="projectResourcesAndPlan">
                                        <div className='projectPlan-DIV'>
                                            <Title title='Project Plan' className='projectDetails-title'></Title>
                                            <div dangerouslySetInnerHTML={{ __html: projectInfo.projectPlan }}></div>
                                        </div>
                                        <div className='projectResources-DIV'>
                                            <Title title='Project Resources' className='projectDetails-title'></Title>
                                            <div dangerouslySetInnerHTML={{ __html: projectInfo.projectResources }}>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="interestsAndSkills_ProjectDetails">
                                        <div className="interestsAndSkills_ProjectDetailsTITLE">
                                            <Title title='Project Interests and Skills: ' className='projectDetails-title'></Title>
                                        </div>
                                        <div className="interestsAndSkills_divProjectDetails">
                                            <InterestAndSkill allSkill={skillList} allInterest={interestList} ></InterestAndSkill>
                                        </div>

                                    </div>
                                    <div className="projectMembers">
                                        <Title title='Project Members: ' className='projectDetails-title'></Title>
                                        <ul className="projectMembers-list">
                                            {membersList !== null && membersList.length > 0 ?
                                                membersList.map((e) =>
                                                    <div>

                                                        <li className='eachMemberLi' key={e.id} >
                                                            <div>
                                                                <img className='pictureUser' src={e.pictureUser!==null? e.pictureUser: images[Math.floor(Math.random() * images.length)]} alt="" />
                                                            </div>
                                                            {console.log(e)}
                                                            <Link to={'/userProfile/' + e.idMember} className='usernameMember' > {e.usernameMember}</Link> </li >


                                                    </div>
                                                ) : ''

                                            }

                                        </ul>

                                    </div>

                                </div>

                                <div className='showProjectDetails' style={{ display: showInfoProject ? "none" : "block" }}>




                                    {ideaNecessityList.length > 0 ? ideaNecessityList.map(ideaNecessity => {
                                        return (
                                            <LineIdeaNecessity
                                                page={'allIdeaNecessity'}
                                                type={ideaNecessity.ideaOrNecessity}
                                                id={ideaNecessity.id}
                                                title={ideaNecessity.title}
                                                description={ideaNecessity.description}
                                                author={ideaNecessity.nameAuthor}
                                                creationTime={ideaNecessity.creationTime}
                                                vote={ideaNecessity.vote}
                                                updateTime={ideaNecessity.updateTime}
                                                className={(ideaNecessity.deletedIdeaNecessity === false) ? "ideaNecessity" : "ideaNecessityDelete"}
                                                deletedIdeaNecessity={ideaNecessity.deletedIdeaNecessity}
                                                view={'show'} />

                                        )
                                    }) : ''}
                                </div>

                            </div>

                        </div>
                        : ''}
                </div>
            </div>
        </div >
    )
}