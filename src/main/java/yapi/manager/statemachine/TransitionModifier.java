package yapi.manager.statemachine;

public class TransitionModifier {

    private boolean excludeInCapture = false;
    private boolean startCapture = false;
    private boolean stopCapture = false;
    private boolean invert = false;

    public TransitionModifier() {}

    public TransitionModifier(boolean excludeInCapture, boolean startCapture, boolean stopCapture, boolean invert) {
        setExcludeInCapture(excludeInCapture);
        setStartCapture(startCapture);
        setStopCapture(stopCapture);
        setInvert(invert);
    }

    public boolean isExcludeInCapture() {
        return excludeInCapture;
    }

    public TransitionModifier setExcludeInCapture(boolean excludeInCapture) {
        if (startCapture || stopCapture) {
            return this;
        }
        this.excludeInCapture = excludeInCapture;
        return this;
    }

    public boolean isStartCapture() {
        return startCapture;
    }

    public TransitionModifier setStartCapture(boolean startCapture) {
        if (stopCapture || excludeInCapture) {
            return this;
        }
        this.startCapture = startCapture;
        return this;
    }

    public boolean isStopCapture() {
        return stopCapture;
    }

    public TransitionModifier setStopCapture(boolean stopCapture) {
        if (startCapture || excludeInCapture) {
            return this;
        }
        this.stopCapture = stopCapture;
        return this;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    @Override
    public String toString() {
        return "TransitionModifier{" +
                "excludeInCapture=" + excludeInCapture +
                ", startCapture=" + startCapture +
                ", stopCapture=" + stopCapture +
                ", invert=" + invert +
                '}';
    }
}
