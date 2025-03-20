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

    public static class Success<T> extends ReservationAPIResult {
        private final T result;
        public Success(T result, ReservationAPIInfo info) {
            super(info);
            this.result = result;
        }
        public T getResult() {
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
