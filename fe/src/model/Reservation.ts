import Table from "./Table.ts";
import Customer from "./Customer.ts";
class Reservation {
    id: number;
    jointTables: Table[];
    interval: {
        startDateTime: string;
        endDateTime: string;
    };
    customer: Customer;
    numberOfPeople: number;


    public constructor(id: number, jointTables: Table[], interval: {
        startDateTime: string;
        endDateTime: string
    }, customer: Customer, numberOfPeople: number) {
        this.id = id;
        this.jointTables = jointTables;
        this.interval = interval;
        this.customer = customer;
        this.numberOfPeople = numberOfPeople;
    }
}

export default Reservation;
