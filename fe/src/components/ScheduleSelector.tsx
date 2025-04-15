import DatePicker from "./datetime/DatePicker.tsx";
import {useState} from "react";

interface ScheduleSelectorProps{
    onChange: (newDate: string) => void;
    init: string;
}

function ScheduleSelector(props: ScheduleSelectorProps) {
    console.log("Setted date to ", props.init);
    function handleChange(newDate: string){
        props.onChange(newDate);
    }
    return (
        <div style={{marginTop: "100px", width: "14rem"}}>
            <p>Schedule of:</p>
            <DatePicker
                date={props.init}
                callback={handleChange}
                showDefault={false}
            />
        </div>
    )
}

export default ScheduleSelector;