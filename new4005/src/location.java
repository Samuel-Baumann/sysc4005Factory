public class location {
    public static enum position{I1, I2, W1, W2, W3, C1B1_1, C1B1_2, C1B2_1, C1B2_2, C1B3_1, C1B3_2, C2B_1, C2B_2, C3B_1, C3B_2, OUT}
    private position name;
    private boolean isOccupied;
    private component comp;

    public location(position name, boolean initState){
        this.name = name;
        isOccupied = initState;
    }

    public location(position name, boolean initState, component comp){
        this.name = name;
        isOccupied = initState;
        this.comp = comp;
    }

    public position getName() {
        return name;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void setComp(component newComp){
        comp = newComp;
    }

    public component getComp(){
        if(!(comp == null)){
            return comp;
        }
        return null;
    }
}
