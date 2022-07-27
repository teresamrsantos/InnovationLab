import React from "react";
import LineIdeaNecessity from "./LineIdeaNecessity";

export default (props) => {
    const listIdeaNecessity = props.testinformation.filter(ideaNecessity => (ideaNecessity.titleId2.toLowerCase().includes(props.wordSearch.toLowerCase())
        || ideaNecessity.titleId1.toLowerCase().includes(props.wordSearch.toLowerCase()))).map((ideaNecessity) => {
            if(ideaNecessity.deletedIdeaNecessity1!=true && ideaNecessity.deletedIdeaNecessity2!=true){
            if (ideaNecessity.ideaId1 == props.id) {
                return <LineIdeaNecessity
                    title={ideaNecessity.titleId2}
                    description={ideaNecessity.description}
                    author={ideaNecessity.authorIdeaId2}
                    authorJustification={ideaNecessity.author}
                    type={ideaNecessity.ideaOrNecessityId2}
                    idAss={ideaNecessity.ideaId2}
                    page={props.page}
                    className={props.className}
                    id={props.id}
                    view={'show'}
                />
            } else {
                return <LineIdeaNecessity
                    title={ideaNecessity.titleId1}
                    description={ideaNecessity.description}
                    author={ideaNecessity.authorIdeaId1}
                    authorJustification={ideaNecessity.author}
                    type={ideaNecessity.ideaOrNecessityId1}
                    idAss={ideaNecessity.ideaId1}
                    page={props.page}
                    className={props.className}
                    id={props.id}
                    view={'show'}
                />
            }}
        });

    return (
        <ul>{listIdeaNecessity}</ul>
    )
}