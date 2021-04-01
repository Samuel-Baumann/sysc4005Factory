public class component {

    public static enum componentType {C1, C2, C3};
    private componentType cType;
    private int IDnum;
    private location current;

    public component(int id, componentType type, location where){

        IDnum = id;
        cType = type;
        current = where;
    }

    public int getIDnum(){
        return IDnum;
    }

    public componentType getcType() {
        return cType;
    }

    public location getLocation() {
        return current;
    }

    public void setLocation(location newLoc){
        current = newLoc;
    }
}
