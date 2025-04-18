import {Dropdown, Form} from "react-bootstrap";
import Customer from "../model/Customer.ts";
import {useState} from "react";

interface CustomersDropdownSearchProps {
    onCustomerSelected: (c: Customer) => void;
}
async function getCustomers(regex: string): Promise<Customer[]>{
    const response = await fetch('http://0.0.0.0:8080/api/v1.0.0/customer/getbyphonenumberstartingwith/?regex=' + regex);
    if (response.status === 200) {
        const data = await response.json();
        return data.map((c: { id: number; email: string; name: string; phoneNumber: string; }) => new Customer(c.id, c.email, c.name, c.phoneNumber));
    }
    return [] as Customer[];

}
function CustomersDropdownSearch(props: CustomersDropdownSearchProps){
    const [completionValue, setCompletionValue] = useState<string>("");
    const [showDropdown, setShowDropdown] = useState(false);
    const [customersFound, setCustomersFound] = useState<Customer[]>([]);
    /*const handleSelect = (value: string) => {
        setCompletionValue(value);
        setShowDropdown(false);
        //props.onSelect(value);
    }       */

    const searchAndFilter = async (value: string) => {
        setCompletionValue(value);
        await getCustomers(value).then((values) => {
            if (values.length !== 0) {
                setCustomersFound(values as Customer[]);
                setShowDropdown(true);
            } else {
                setCustomersFound([]);
                setShowDropdown(false);
            }
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
                    {customersFound.map((item: Customer) => (
                        <Dropdown.Item
                            onClick={() => {
                                setCompletionValue(item.phoneNumber)
                                props.onCustomerSelected(item)
                                setShowDropdown(false)}}>
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