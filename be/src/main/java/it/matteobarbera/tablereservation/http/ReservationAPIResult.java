package it.matteobarbera.tablereservation.http;

public abstract class ReservationAPIResult {
    protected final Enum<?> status;

    public ReservationAPIResult(Enum<?> status) {
        this.status = status;
    }

    public Enum<?> getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return this instanceof Success;
    }

    public Success getSuccess() {
        if (isSuccess()) {
            return (Success) this;
        } else {
            throw new ClassCastException("The reservation result is not a success");
        }
    }

    public Failure getFailure() {
        if (!isSuccess()) {
            return (Failure) this;
        } else {
            throw new ClassCastException("The reservation result is not a failure");
        }
    }

    public static class Success extends ReservationAPIResult {
        private final Object result;

        public Success(ReservationAPIInfo info){
            super(info);
            this.result = null;
        }
        public Success(Object result, ReservationAPIInfo info) {
            super(info);
            this.result = result;
        }
        public Object getResult() {
            return result;
        }
        public ReservationAPIInfo getInfo() {
            return (ReservationAPIInfo) status;
        }
    }

    public static class Failure extends ReservationAPIResult {
        public Failure(ReservationAPIError error) {
            super(error);
        }
        public ReservationAPIError getError() {
            return (ReservationAPIError) status;
        }
    }
}
