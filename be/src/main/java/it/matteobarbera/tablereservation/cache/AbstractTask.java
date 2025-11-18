package it.matteobarbera.tablereservation.cache;

public abstract class AbstractTask {
    final Runnable task;

    public AbstractTask(Runnable task) {
        this.task = task;
    }

    public void runTask(){
        task.run();
    }
}
