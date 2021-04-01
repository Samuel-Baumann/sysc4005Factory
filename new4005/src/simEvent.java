import java.util.*;

public class simEvent implements Comparable<simEvent>{

    //our sim events are: arrival at inspector, leaving inspector, arriving at buffer, leaving buffer, arriving at workstation and leaving workstation + end simulation

    public static enum eventType {LI1, LI2_2, LI2_3, LW1, LW2, LW3, ES};
    private eventType eType;                                                // Type of the event
    private Double eTime;                                                   // Event Time
    private component eComp;

    public simEvent(eventType eType, double eTime, component eComp) {
        this.eType = eType;
        this.eTime = eTime;
        this.eComp = eComp;
    }

    @Override
    public int compareTo(simEvent ev) {
        return this.geteTime().compareTo(ev.geteTime());
    }

    public eventType geteType() {
        return eType;
    }

    public void seteType(eventType eType) {
        this.eType = eType;
    }

    public Double geteTime() {
        return eTime;
    }

    public void seteTime(Double eTime) {
        this.eTime = eTime;
    }

    public component geteComp(){
        return eComp;
    }

}
