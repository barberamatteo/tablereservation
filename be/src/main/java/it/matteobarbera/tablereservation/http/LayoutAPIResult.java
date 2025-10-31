package it.matteobarbera.tablereservation.http;

public abstract class LayoutAPIResult {
    protected final Enum<?> status;

    public LayoutAPIResult(Enum<?> status) {
        this.status = status;
    }

    public Enum<?> getStatus() {
        return status;
    }

    public static class Success extends LayoutAPIResult {
        private final Object result;
        public Success(LayoutAPIInfo info){
            super(info);
            this.result = null;
        }

        public Success(Object result, LayoutAPIInfo info){
            super(info);
            this.result = result;
        }

        public Object getResult() {
            return result;
        }

    }

    public static class Failure extends LayoutAPIResult {
        public Failure(LayoutAPIError error) {
            super(error);
        }
        public LayoutAPIError getError() {
            return (LayoutAPIError) getStatus();
        }
    }
}
