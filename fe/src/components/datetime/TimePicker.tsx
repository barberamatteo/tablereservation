import Flatpickr from "react-flatpickr";
import "flatpickr/dist/flatpickr.min.css";
import "bootstrap/dist/css/bootstrap.min.css";

interface TimePickerProps {
    time: string | null;
    callback: (value: string) => void;
}


function TimePicker(props: TimePickerProps){
    function FlatPickrOnChangeHook(selectedDates: Date[], dateStr: string, instance: Flatpickr){
        props.callback(
            (selectedDates[0].getHours() < 10 ? "0" + selectedDates[0].getHours() : selectedDates[0].getHours())
            + ":"
            + (selectedDates[0].getMinutes() < 10 ? "0" + selectedDates[0].getMinutes() : selectedDates[0].getMinutes())
            + ":00"
        )
    }
    return (
        <div className="form-group">
            <Flatpickr
                id="timePicker"
                className="form-control"
                value={props.time}
                options={{
                    onChange: FlatPickrOnChangeHook,
                    enableTime: true,
                    noCalendar: true,
                    dateFormat: "H:i",
                    time_24hr: true,
                }}
                placeholder="HH:MM"
            />
        </div>
    );
}


export default TimePicker;
