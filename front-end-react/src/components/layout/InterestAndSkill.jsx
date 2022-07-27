import React from "react";
import Button from "../../components/layout/Button";
import Text from "../../components/layout/Text";
import "./InterestAndSkill.css";

export default (props) => {
    let Array =[];
    const type = props.allSkill.map((skill) => {
        if(!Array.includes(skill.skillType)){
            Array.push(skill.skillType)
        }
    })

   const interestList = props.allInterest.map((interest) => {
        return <Button
            className={"btn-interestAndSkill"}
            text={interest.description}
        />
    });

   const hardwareList = props.allSkill.map((skill) => {
        if (skill.skillType == "HARDWARE") {
            return <Button className={"btn-interestAndSkill"} text={skill.description}/>
        }
    });

    const softwareList = props.allSkill.map((skill) => {
        if (skill.skillType == "SOFTWARE") {
            return <Button className={"btn-interestAndSkill"} text={skill.description}/>
        }
    });

    const knowledgeList = props.allSkill.map((skill) => {
        if (skill.skillType == "KNOWLEDGE") { 
            return<Button className={"btn-interestAndSkill"} text={skill.description}/>
        }
    });


    const toolsList = props.allSkill.map((skill) => {
        if (skill.skillType == "TOOLS") {
            return <Button className={"btn-interestAndSkill"} text={skill.description}/>
        }
    });


    return (
        <div className="interestAndSkill">
            <>
            {props.allInterest.length===0?'':
            <div>
                <Text className={"text-interestAndSkill1"} text={'Interest'} />
                <div>
                    {interestList}
                </div>
            </div>}
            </>
            <>
            {props.allSkill.length===0?'':
            <div>
                <Text className={"text-interestAndSkill2"} text={'Skill'} />
                <div className="div-skill">
                {!Array.includes('HARDWARE')?'':
                    <div>
                        <Text className={"text-typeSkill"} text={'Hardware'} />
                        <div>
                            {hardwareList}
                        </div>
                    </div>}
                    {!Array.includes('SOFTWARE')?'':
                    <div>
                        <Text className={"text-typeSkill"} text={'Software'} />
                        <div>
                            {softwareList}
                        </div>
                    </div>}
                    {!Array.includes('TOOLS')?'':
                    <div>
                        <Text className={"text-typeSkill"} text={'Tools'} />
                        <div>
                            {toolsList}
                        </div>
                    </div>}
                    {!Array.includes('KNOWLEDGE')?'':
                    <div>
                        <Text className={"text-typeSkill"} text={'Knowledge'} />
                        <div>
                            {knowledgeList}
                        </div>
                    </div>}
                </div>
            </div>}
            </>
        </div>
    );
};