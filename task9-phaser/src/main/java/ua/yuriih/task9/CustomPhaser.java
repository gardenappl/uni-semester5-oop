package ua.yuriih.task9;

public class CustomPhaser {
    private int arrivedParties = 0;
    private int registeredParties = 0;
    private int waitingParties = 0;
    private int phase = 0;

    private final Object lock = new Object();

    public int bulkRegister(int parties) {
        if (parties < 0)
            throw new IllegalArgumentException("Must register at least 0 parties");
        this.registeredParties += parties;
        return phase;
    }

    public int register() {
        this.registeredParties++;
        return phase;
    }

    public int arrive() {
        int currentPhase = this.phase;

        int currentPartyNum = this.arrivedParties;
        this.arrivedParties++;

        if (this.arrivedParties == this.registeredParties)
            onAdvance(currentPartyNum, this.registeredParties);

        return currentPhase;
    }

    public int arriveAndDeregister() {
        if (this.registeredParties == 0)
            throw new IllegalStateException("Tried to unregistered when there are no registered parties");
        int currentPhase = this.phase;

        int currentPartyNum = this.arrivedParties;
        int currentRegisteredParties = this.registeredParties;
        this.registeredParties--;

        if (this.arrivedParties == this.registeredParties)
            onAdvance(currentPartyNum, currentRegisteredParties);

        return currentPhase;
    }

    public int awaitAdvance(int phase) {
        if (this.phase != phase)
            return this.phase + 1;

        int currentPartyNum = this.arrivedParties;
        int currentRegisteredParties = this.registeredParties;
        this.arrivedParties++;

        this.waitingParties++;
        while (this.arrivedParties < this.registeredParties) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException ignored) {}
            }
        }
        this.waitingParties--;

        onAdvance(currentPartyNum, currentRegisteredParties);

        return phase + 1;
    }

    private void onAdvance(int currentPartyNum, int registeredParties) {
        //Last to arrive, wake everyone up
        if (currentPartyNum == registeredParties - 1) {
            if (this.phase == Integer.MAX_VALUE)
                this.phase = 0;
            else
                this.phase++;

            synchronized (lock) {
                lock.notifyAll();
            }
        }
        //Last to wake up
        if (this.waitingParties == 0) {
            this.arrivedParties = 0;
        }
    }

    public int getArrivedParties() {
        return arrivedParties;
    }

    public int getRegisteredParties() {
        return registeredParties;
    }

    public int getUnarrivedParties() {
        return registeredParties - arrivedParties;
    }

    public int getPhase() {
        return phase;
    }
}
