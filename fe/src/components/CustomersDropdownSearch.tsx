import {Button, Col, Dropdown, Form, Image, InputGroup} from "react-bootstrap";
import Customer from "../model/Customer.ts";
import {useState} from "react";
import AddCustomerModal from "/src/components/modals/AddCustomerModal.tsx";
import Messages from "../Messages.ts";

interface CustomersDropdownSearchProps {
    onCustomerSelected: (c: Customer) => void;
}
async function getCustomers(regex: string): Promise<Customer[]>{
    const response = await fetch(`${Messages.BACKEND}/api/v1.0.0/customer/getbyphonenumberstartingwith/?regex=` + regex,
        {
            method: 'GET',
            credentials: 'include'
        });
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
    const [isCustomerChosen, setIsCustomerChosen] = useState<boolean>(false);
    const [showAddCustomerModal, setShowAddCustomerModal] = useState(false);
    const [addCustomerExitCode, setAddCustomerExitCode] = useState<number | null>(null);
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
            <Col>
                <InputGroup className="mb-3">
                    <Form.Control
                        type="tel"
                        placeholder="Also include country code (es. +39)"
                        value={completionValue}
                        autoFocus
                        onChange={(e) => {
                            setIsCustomerChosen(false);
                            searchAndFilter(e.target.value).then();
                        }}
                    />

                    {!isCustomerChosen && (
                        <Button
                            variant="outline-secondary"
                            onClick={() => setShowAddCustomerModal(true)}>
                            <Image className="btn-icon" src="src/assets/btn_icon/add_cust.png"/>
                        </Button>

                    )}

                </InputGroup>
                {(addCustomerExitCode == 1 || addCustomerExitCode == -1) && (
                    <Form.Label>
                        {addCustomerExitCode == 1 ? "Customer created successfully" : "Customer creation failed!"}
                    </Form.Label>
                )}
                {showDropdown && (
                    <Dropdown.Menu show style={{position: "absolute", width: "100%"}}>
                        {customersFound.map((item: Customer) => (
                            <Dropdown.Item
                                onClick={() => {
                                    setCompletionValue(item.phoneNumber);
                                    props.onCustomerSelected(item);
                                    setShowDropdown(false);
                                    setIsCustomerChosen(true);
                                }}>
                                {item.toString()}
                            </Dropdown.Item>
                        ))}
                    </Dropdown.Menu>
                )}
            </Col>
            <AddCustomerModal
                shown={showAddCustomerModal}
                handleClose={(exit_code: number) => {
                    setShowAddCustomerModal(false)
                    setAddCustomerExitCode(exit_code);
                }}
            />
        </>
    )
}

export default CustomersDropdownSearch;