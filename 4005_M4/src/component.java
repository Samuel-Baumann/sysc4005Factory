public class component {

    public static enum componentType {C1, C2, C3, Empty};
    private componentType cType;
    private int IDnum;

    public component(int id, componentType type){

        IDnum = id;
        cType = type;
    }

    public int getIDnum(){
        return IDnum;
    }

    public componentType getcType() {
        return cType;
    }

}
