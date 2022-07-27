import { FaHandsHelping, FaRegLightbulb } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import Text from "../../components/layout/Text";
import Title from "../../components/layout/Title";
import "./LineIdeaNecessity_Project.css";

export default (props) => {
    const navigate = useNavigate();
    var classname = props.className || 'ideaNecessity';

    var type = props.type === "IDEA";
    var vote = props.vote + ' votes';
    var author = 'Author:' + props.author;
    var token = sessionStorage.getItem("token")
    var userType = props.userType;
    var listOfAssociatedIdeas = props.listOfAssociatedIdeas;

    var creationTimeAux = props.creationTime;
    const creationTime = new Date(creationTimeAux);
    var creationTimeEnd = "Created in: " + creationTime.getDate() + "/" + (creationTime.getMonth() + 1) + "/" + creationTime.getFullYear() +
        " " + creationTime.getHours() + ":" + creationTime.getMinutes();

    var updateTimeAux = props.updateTime;
    const updateTime = new Date(updateTimeAux);
    var updateTimeEnd = "Created in: " + updateTime.getDate() + "/" + (updateTime.getMonth() + 1) + "/" + updateTime.getFullYear() +
        " " + updateTime.getHours() + ":" + updateTime.getMinutes();



    function addIdeaNecessityEditProject(event) {

        var id = props.id + '/editProject';

        if (document.getElementById(id).classList.contains('added')) {

            if (classname === 'ideaNecessity_editProject' && listOfAssociatedIdeas.length > 1 || classname !== 'ideaNecessity_editProject') {

                document.getElementById(id).classList.remove('added');
                event.target.innerText = ' + '

                props.removeIdeaNecessityList(id)
            } else {
                props.removeIdeaNecessityList(id)
            }
        } else {

            document.getElementById(id).classList.add('added');
            event.target.innerText = ' - '
            props.fillIdeaNecessityList(id)

        }

    }



    function addIdeaNecessity(event) {

        var id = event.target.id;
        if (document.getElementById(id).classList.contains('added')) {
                document.getElementById(id).classList.remove('added');
            event.target.innerText = ' + '
     
            props.removeIdeaNecessityList(id)
        } else {
            document.getElementById(id).classList.add('added');
            event.target.innerText = ' - '
            props.fillIdeaNecessityList(id)

        }

    }

 

    function showMore(event) {
        var id = event.target.id;
        navigate('/commentIdeaNecessity/' + id)
    }



    return (
        <div>

            {classname === 'ideaNecessity_editProject' ?
                <div>

                    <li className={listOfAssociatedIdeas.some(e => e.id === props.id) ? classname + ' added' : classname} value={props.id} id={props.id + '/editProject'}>
                        <div className="type-ideaNecessity">
                            {type ? <FaRegLightbulb size={"20%"} className="simbol-idea_AddProject" color="#090446" /> :
                                <FaHandsHelping size={"40%"} className="simbol-necessity_AddProject" color="#FEB95F" />}
                        </div>

                        <div className="content-ideaNecessity_AddProject">
                            <Title className="title-ideaNecessity_AddProject" title={props.title} />
                            <div className="description-ideaNecessity_AddProject" dangerouslySetInnerHTML={{ __html: props.description}}> 
                            
                            </div>
                            <a className="btnShowMore" id={props.id} onClick={(event) => { showMore(event) }}>Show more...</a>.
                            <div className="info-ideaNecessity_AddProject">
                                <Text className="author-ideaNecessity_AddProject" text={author} />
                                <Text className="date2-ideaNecessity_AddProject" text={creationTimeEnd} />
                            </div>
                        </div>

                        <div className="action-ideaNecessity_AddProject">
                            <div className="action">

                                <button className='addIdeaNecessityToProject_AddProject' id={props.id} onClick={(event) => { addIdeaNecessityEditProject(event) }} >{listOfAssociatedIdeas.some(e => e.id === props.id) ? ' -' : '+'}  </button>
                            </div>
                        </div>
                    </li>

                </div>

                :

                <li className={classname} value={props.id} id={props.id}>
                    <div className="type-ideaNecessity">
                        {type ? <FaRegLightbulb size={"20%"} className="simbol-idea_AddProject" color="#090446" /> :
                            <FaHandsHelping size={"40%"} className="simbol-necessity_AddProject" color="#FEB95F" />}
                    </div>

                    <div className="content-ideaNecessity_AddProject">
                        <Title className="title-ideaNecessity_AddProject" title={props.title} />
                        <div className="description-ideaNecessity_AddProject" dangerouslySetInnerHTML={{ __html: props.description}}> 
                            
                            </div>
                        <a className="btnShowMore" id={props.id} onClick={(event) => { showMore(event) }}>Show more...</a>.
                        <div className="info-ideaNecessity_AddProject">
                            <Text className="author-ideaNecessity_AddProject" text={author} />
                            <Text className="date2-ideaNecessity_AddProject" text={creationTimeEnd} />
                        </div>
                    </div>

                    <div className="action-ideaNecessity_AddProject">
                        <div className="action">

                            <button className='addIdeaNecessityToProject_AddProject' id={props.id} onClick={(event) => { addIdeaNecessity(event) }} >+</button>
                        </div>
                    </div>
                </li>

            }


        </div>);

};
