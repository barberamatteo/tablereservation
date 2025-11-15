package it.matteobarbera.tablereservation.http;

public class CustomerAPIResult {
    protected final Enum<?> status;

    public CustomerAPIResult(Enum<?> status) {
        this.status = status;
    }
    public Enum<?> getStatus() {
        return status;
    }

    public boolean isSuccess(){
        return this instanceof Success;
    }

    public Success getSuccess(){
        if (isSuccess()){
            return (Success) this;
        } else {
            throw new ClassCastException("The customer result is not a success");
        }
    }


    public static class Success extends CustomerAPIResult {
        private final Object result;
        public Success(CustomerAPIInfo info) {
            super(info);
            this.result = null;
        }

        public Success(Object result, CustomerAPIInfo info) {
            super(info);
            this.result = result;
        }

        public Object getResult() {
            return result;
        }

    }

    public static class Failure extends CustomerAPIResult {
        public Failure(CustomerAPIError error) {
            super(error);
        }
        public CustomerAPIError getError(){
            return (CustomerAPIError) status;
        }
    }

}
