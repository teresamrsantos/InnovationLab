import React from "react";
import ProjectSquare from "./ProjectSquare";
import './AllProjects.css'

export default (props) => {

    function verificationIncluded(list, array) {
        for (let j = 0; j < array.length; j++) {
            if (!list.includes(array[j])) {
                return false;
            }
        }
        return true;
    }

    let projectList = ''
    if (props.page === 'allFavoriteProjects') {
            projectList = props.testinformation
            .filter(element => element.projectStatus.includes(props.concludedInProgress) && verificationIncluded(element.interestAssociatedList, props.interest) &&
                verificationIncluded(element.skillAssociatedList, props.skill) &&
                verificationIncluded(element.ideaNecessityAssociatedList, props.ideaNecessity))
            .map((projectInfo) =>

                <ProjectSquare
                    key={projectInfo.idProject}
                    page={props.page}
                    pictureProject={projectInfo.imageProject}
                    idUser={props.idUser}
                    id={projectInfo.idProject}
                    title={projectInfo.title}
                    idAuthor={projectInfo.userDTOJoinProject.idUser}
                    author={projectInfo.userDTOJoinProject.username}
                    creationDate={projectInfo.creationTime}
                    numberMaxMembers={projectInfo.numberMaxMembers}
                    numberOfMembers={projectInfo.numberOfMembers}
                    membersList={projectInfo.membersList}
                    className={projectInfo.projectStatus === 'CONCLUDED' ? 'concluded' : 'inprogress'}
                    favorite={(projectInfo.userListFavorites.includes(props.idUser)) ? true : false}
                    removeActiveProject={props.removeActiveProject}
                    resetSize={props.resetSize}
                ></ProjectSquare>
            )
    } else if (props.page === 'myProjects') {

        projectList = props.testinformation
            .filter(element => element.projectStatus.includes(props.concludedInProgress) && verificationIncluded(element.interestAssociatedList, props.interest) &&
                verificationIncluded(element.skillAssociatedList, props.skill) &&
                verificationIncluded(element.ideaNecessityAssociatedList, props.ideaNecessity))
            .map((projectInfo) =>
                <ProjectSquare
                    key={projectInfo.idProject}
                    page={props.page}
                    pictureProject={projectInfo.imageProject}
                    idUser={props.idUser}
                    id={projectInfo.idProject}
                    title={projectInfo.title}
                    idAuthor={projectInfo.userDTOJoinProject.idUser}
                    author={projectInfo.userDTOJoinProject.username}
                    creationDate={projectInfo.creationTime}
                    numberMaxMembers={projectInfo.numberMaxMembers}
                    numberOfMembers={projectInfo.numberOfMembers}
                    membersList={projectInfo.membersList}
                    className={projectInfo.projectStatus === 'CONCLUDED' ? 'concluded' : 'inprogress'}
                    favorite={(projectInfo.userListFavorites.includes(props.idUser)) ? true : false}
                    removeActiveProject={props.removeActiveProject}
                ></ProjectSquare>
            )
    } else {
     
        projectList = props.testinformation
            .filter(element => verificationIncluded(element.interestAssociatedList, props.interest) &&
                verificationIncluded(element.skillAssociatedList, props.skill) &&
                verificationIncluded(element.ideaNecessityAssociatedList, props.ideaNecessity))
            .map((projectInfo) =>
                <ProjectSquare
                    key={projectInfo.idProject}
                    page={props.page}
                    pictureProject={projectInfo.imageProject}
                    idUser={props.idUser}
                    id={projectInfo.idProject}
                    title={projectInfo.title}
                    idAuthor={projectInfo.userDTOJoinProject.idUser}
                    author={projectInfo.userDTOJoinProject.username}
                    creationDate={projectInfo.creationTime}
                    numberMaxMembers={projectInfo.numberMaxMembers}
                    numberOfMembers={projectInfo.numberOfMembers}
                    membersList={projectInfo.membersList}
                    userType={props.userType}
                    className={projectInfo.projectStatus === 'CONCLUDED' ? 'concluded' : 'inprogress'}
                    favorite={(projectInfo.userListFavorites.includes(props.idUser)) ? true : false}
                    removeActiveProject={props.removeActiveProject}
                ></ProjectSquare>
            )
    }



    return (
        <ul className="projectsListUL">{projectList}</ul>
    )
}