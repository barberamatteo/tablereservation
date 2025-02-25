class Customer {
    id: number;
    email: string;
    name: string;
    phoneNumber: string;

    public constructor(id: number, email: string, name: string, phoneNumber: string) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}

export default Customer;