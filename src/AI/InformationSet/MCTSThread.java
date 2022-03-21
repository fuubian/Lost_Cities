package AI.InformationSet;

public class MCTSThread extends Thread {

    private MCTSAgent agent;
    private boolean running;

    public MCTSThread (MCTSAgent agent) {
        this.agent = agent;
    }

    public void run() {
        this.running = true;
        while (this.running) {
            this.agent.runThrough();
        }
    }

    public void stopRun() {
        this.running = false;
    }
}
