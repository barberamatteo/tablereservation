import Flatpickr from "react-flatpickr";
import "flatpickr/dist/flatpickr.min.css";
import "bootstrap/dist/css/bootstrap.min.css";

interface DatePickerProps {
    date: string;
    callback: (newDate: string) => void;
    showDefault: boolean;
}


function DatePicker(props: DatePickerProps) {
    function FlatPickrOnChangeHook(selectedDates: Date[], dateStr: string, instance: Flatpickr){
        const date = selectedDates[0]
        const yyyy = date.getFullYear();
        const mm = date.getMonth() + 1;
        const dd = date.getDate();
        props.callback(
            yyyy + "-" + (mm < 10 ? "0" + mm : mm)+ "-" + (dd < 10 ? "0" + dd : dd)
        );

    }
    console.log("Date is equal to ", props.date)

    return(
        <>
            <div className="form-group">
                <Flatpickr
                    date={props.date}
                    id="timePicker"
                    className="form-control"
                    options={{
                        onChange: FlatPickrOnChangeHook,
                        minDate: "today",
                        enableTime: false,
                        defaultDate: props.date,
                        dateFormat: "d-m-Y"
                    }}
                />
            </div>
        </>
    )
}
export default DatePicker;