
import Text from "./Text";
import Button from "./Button";
import "./Answer.css";

export default (props) => {
var name = props.firstName+" "+props.lastName;
var timestamp = props.creationTime;
const creationTime = new Date(timestamp*1);

return (
        <div className={"Answer"}>
            <Text className={"name-answer"} text={name} />
            <Text className={"date-answer"} text={creationTime.getDate()+"/"+(creationTime.getMonth()+1)+"/"+creationTime.getFullYear()+
            " "+creationTime.getHours()+":"+creationTime.getMinutes()} />
            <Text className={"description-answer"} text={props.description} />
            <Button className="reply-answer" text={"Reply"} id={props.idPost} onclick={(event) => {props.replyAnswer(props.username,event) }}/>
        </div>
    );
};