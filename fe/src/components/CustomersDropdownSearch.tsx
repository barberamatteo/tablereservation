import {Dropdown, DropdownMenu, DropdownToggle, Form} from "react-bootstrap";
import Customer from "../model/Customer.ts";
import {useState} from "react";

interface CustomersDropdownSearchProps {
    customers: Customer[],
    onSelect: (value: string) => void
}
async function getCustomers(regex: string){
    return await fetch('http://localhost:8080/api/v1/customer/getbyphonenumberstartingwith/?regex=' + regex).then(value => {return value.json()});
}
function CustomersDropdownSearch(){
    const [completionValue, setCompletionValue] = useState<string>("");
    const [showDropdown, setShowDropdown] = useState(false);
    const [customersFound, setCustomersFound] = useState<Customer[]>([]);
    const handleSelect = (value: string) => {
        setCompletionValue(value);
        setShowDropdown(false);
        //props.onSelect(value);
    }

    const searchAndFilter = async (value: string) => {
        await getCustomers(value).then((values) => {
            console.log(values);
            setCustomersFound(values);
            setShowDropdown(true);
        })
    }
    return (
        <>
            <Form.Control
                type="tel"
                placeholder="Also include country code (es. +39)"
                value={completionValue}
                autoFocus
                onChange={(e) => searchAndFilter(e.target.value)}
            />
            {showDropdown && (
                <Dropdown.Menu show style={{position: "absolute", width: "100%"}}>
                    {customersFound.map((item) => (
                        <Dropdown.Item>
                            {item.toString()}
                        </Dropdown.Item>
                    ))}
                </Dropdown.Menu>
            )}
        </>
        /*
        <Dropdown>
                <DropdownToggle>
                    Prova
                </DropdownToggle>
                <Dropdown.Menu>
                    <Dropdown.Item href="#/action-1">Action</Dropdown.Item>
                    <Dropdown.Item href="#/action-2">Another action</Dropdown.Item>
                    <Dropdown.Item href="#/action-3">Something else</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
         */
    )
}

export default CustomersDropdownSearch;