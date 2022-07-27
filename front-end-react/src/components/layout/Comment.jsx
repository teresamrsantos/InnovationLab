import React, { useState } from "react";
import Text from "./Text";
import Button from "./Button";
import Answer from "./Answer";
import "./Comment.css";

export default (props) => {
    const [seeMore, setSeeMore] = useState(2);
    var name = props.firstName + " " + props.lastName;
    var timestamp = props.creationTime;
    const creationTime = new Date(timestamp * 1);

    const listItems = props.answerList.map((answer,index) =>{
    if (index > (props.answerList.length-seeMore-1)) {
        return <Answer
            description={answer.description}
            firstName={answer.userJoinPost.firstName}
            lastName={answer.userJoinPost.lastName}
            username={answer.userJoinPost.username}
            creationTime={answer.creationTime}
            answerList={answer.answerList}
            idPost={props.idPost}
            replyUsername={props.replyUsername}
            replyAnswer={props.replyAnswer}
            key={index}
        />
    }
});
    
    return (
        <div className={"Comment"}>
            <Text className={"name-comment"} text={name} />
            <Text className={"date-comment"} text={creationTime.getDate() + "/" + (creationTime.getMonth() + 1) + "/" + creationTime.getFullYear() +
                " " + creationTime.getHours() + ":" + creationTime.getMinutes()} />
            <Text className={"description-comment"} text={props.description} />
            <Button className="reply-comment" id={props.idPost} text={"Reply"} onclick={(event) => {props.replyAnswer(props.username,event) }}/>
            {(seeMore >= props.answerList.length) ? '' :
            <Button className={"seemore-answer"} text={"View previous reply..."} onclick={(event) => { setSeeMore(seeMore+2) }} />}
            {listItems}
        </div>
    );
};