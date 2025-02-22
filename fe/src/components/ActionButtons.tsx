import 'bootstrap/dist/js/bootstrap.bundle.min.js';
function ActionButtons() {
    return (
        <>
            <div className="btn-group action-btns mx-auto" role="group" aria-label="Action Buttons">
                <button type="button" className="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#addReservationModal">
                    <img className="btn-icon" src="src/assets/btn_icon/add_rsv.png" alt="Add reservation"/>
                </button>

                <button type="button" className="btn btn-outline-secondary">
                    <img className="btn-icon" src="src/assets/btn_icon/add_tb.png" alt="Add table"/>
                </button>
                <button type="button" className="btn btn-outline-secondary">
                    <img className="btn-icon" src="src/assets/btn_icon/add_def.png" alt="Add table definition"/>
                </button>
            </div>

            <div className="modal fade" id="addReservationModal" tabIndex={-1} aria-labelledby="addReservationModalTitle"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="addReservationModalTitle">Add Reservation</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal"
                                    aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <form>
                                <label htmlFor="customer-phone" className="col-form-label">Customer phone
                                    number:</label>
                                <input type="text" className="form-control" id="customer-phone"
                                       aria-label="Prefix choice dropdown"/>

                                <div className="mb-3">
                                    <label htmlFor="message-text" className="col-form-label">Message:</label>
                                    <textarea className="form-control" id="message-text"></textarea>
                                </div>
                                <div className="form-group">
                                    <label className="active" htmlFor="dateStandard">Select date of the reservation</label>
                                    <input className="form-control" type="date" id="dateStandard" name="dateStandard"/>
                                </div>
                                <div className="form-group">
                                    <label className="active" htmlFor="timeStandard">Select time of the reservation</label>
                                    <input className="form-control" id="timeStandard" type="time"/>
                                </div>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" className="btn btn-primary">Send message</button>
                        </div>
                    </div>
                </div>
            </div>

        </>
    )
}

export default ActionButtons;