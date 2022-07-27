import React from "react";
import LineIdeaNecessity from "./LineIdeaNecessity";
import LineIdeaNecessity_AddProject from "../../views/Projects/LineIdeaNecessity_Project"

export default (props) => {

    function verificationIncluded(list, array) {
        for (let j = 0; j < array.length; j++) {
            if (!list.includes(array[j])) {
                return false;
            }
        }
        return true;
    }

    const listIdeaNecessity = props.testinformation
        .filter(person => person.ideaOrNecessity.includes(props.filterIdeaNecessity)
            && verificationIncluded(person.interestAssociatedList, props.interest) &&
            verificationIncluded(person.skillAssociatedList, props.skill))
        .map((ideaNecessity) =>

            <LineIdeaNecessity
                key={ideaNecessity.id}
                resetSize={props.resetSize}
                resetListIdeaNecessity={props.resetListIdeaNecessity}
                type={ideaNecessity.ideaOrNecessity}
                id={ideaNecessity.id}
                title={ideaNecessity.title}
                description={ideaNecessity.description}
                author={ideaNecessity.nameAuthor}
                creationTime={ideaNecessity.creationTime}
                vote={ideaNecessity.vote}
                updateTime={ideaNecessity.updateTime}
                className={(ideaNecessity.deletedIdeaNecessity === false) ? "ideaNecessity" : "ideaNecessityDelete"}
                favorite={ideaNecessity.favoriteList.includes(props.idUser)}
                userType={props.userType}
                page={props.page}
                deletedIdeaNecessity={ideaNecessity.deletedIdeaNecessity}
                idAuthor={ideaNecessity.idAuthor}
                view={'show'}
                setAlertChange={props.setAlertChange} />

        );

    const listIdeaNecessity_Project = props.testinformation
        .filter(person => person.ideaOrNecessity.includes(props.filterIdeaNecessity)
            && verificationIncluded(person.interestAssociatedList, props.interest) &&
            verificationIncluded(person.skillAssociatedList, props.skill))
        .map((ideaNecessity) =>
            <LineIdeaNecessity_AddProject
                key={ideaNecessity.id}
                fillIdeaNecessityList={props.fillIdeaNecessityList}
                removeIdeaNecessityList={props.removeIdeaNecessityList}
                resetListIdeaNecessity={props.resetListIdeaNecessity}
                type={ideaNecessity.ideaOrNecessity}
                id={ideaNecessity.id}
                title={ideaNecessity.title}
                description={ideaNecessity.description.substring(0, 250)}
                author={ideaNecessity.nameAuthor}
                creationTime={ideaNecessity.creationTime}
                updateTime={ideaNecessity.updateTime}
                className={"ideaNecessity_" + props.page}
                listOfAssociatedIdeas={props.listOfAssociatedIdeas}
                userType={props.userType} />
        );


    if (props.page == 'allIdeaNecessity' || props.page === 'allMyIdeaNecessity' || props.page === 'allFavoriteIdeaNecessity') {
        return (
            <ul>{listIdeaNecessity}</ul>
        );
    } else if (props.page == 'addProject' || props.page == 'editProject') {
        return (
            <ul>{listIdeaNecessity_Project}</ul>
        );
    }
}