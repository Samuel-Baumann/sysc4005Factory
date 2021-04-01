import java.util.*;

public class factorySim {

    //Global Variables
    public static double Clock, simRuntime, lastI1Block, lastI2Block, totalI1Block, totalI2Block;
    public static Queue<simEvent> FEL;

    //Inputs - using data provided we can choose how to simulate 'randomness'

    private static double lambda1, lambda2, lambda3, lambda4, lambda5, lambda6;
    private static ITTGeneration C1input, C2_2input, C2_3input, W1input, W2input, W3input;
    private static int inputArraySize;

    private static location I1, I2, W1, W2, W3, C1B1_1, C1B1_2, C1B2_1, C1B2_2, C1B3_1, C1B3_2, C2B_1, C2B_2, C3B_1, C3B_2, OUT;

    private static int c1num, c2num, c3num, p1num, p2num, p3num;
    private static boolean isI1waiting, isI2waiting;

    private static void Init(){
        Clock = 0;
        simRuntime = 2000.0;
        FEL = new PriorityQueue<simEvent>();
        c1num = 0;
        c2num = 0;
        c3num = 0;
        p1num = 0;
        p2num = 0;
        p3num = 0;
        lastI1Block = 0;
        lastI2Block = 0;
        totalI2Block = 0;
        totalI2Block = 0;
        isI1waiting = false;
        isI2waiting = false;

        //Create our locations to hold components
        I1 = new location(location.position.I1, false);
        I2 = new location(location.position.I2, false);
        W1 = new location(location.position.W1, false);
        W2 = new location(location.position.W2, false);
        W3 = new location(location.position.W3, false);
        C1B1_1 = new location(location.position.C1B1_1, false);
        C1B1_2 = new location(location.position.C1B1_2, false);
        C1B2_1 = new location(location.position.C1B2_1, false);
        C1B2_2 = new location(location.position.C1B2_2, false);
        C1B3_1 = new location(location.position.C1B3_1, false);
        C1B3_2 = new location(location.position.C1B3_2, false);
        C2B_1 = new location(location.position.C2B_1, false);
        C2B_2 = new location(location.position.C2B_2, false);
        C3B_1 = new location(location.position.C3B_1, false);
        C3B_2 = new location(location.position.C3B_2, false);
        OUT = new location(location.position.OUT, false);


        //ITT generation variables to be customized - note the higher values in 4 and 6 for shorter overall times!
        lambda1 = 0.09654;
        lambda2 = 0.06436;
        lambda3 = 0.04847;
        lambda4 = 0.46044;
        lambda5 = 0.09015;
        lambda6 = 0.11369;
        inputArraySize = 900; //this value should always be as large as possible to ensure the sim always still has minute values to pull from!

        C1input = new ITTGeneration(0, lambda1, inputArraySize);
        C2_2input = new ITTGeneration(0, lambda2, inputArraySize);
        C2_3input = new ITTGeneration(0, lambda3, inputArraySize);
        W1input = new ITTGeneration(0, lambda4, inputArraySize);
        W2input = new ITTGeneration(0, lambda5, inputArraySize);
        W3input = new ITTGeneration(0, lambda6, inputArraySize);
    }

    public static void main(String[] args){

        Init();

        System.out.println("~~~~~~~~~~~~~~~BEGIN SIM~~~~~~~~~~~~~~~");
        System.out.println("Clock: "+ Clock);

        //C1input.printArray();
        //C2_2input.printArray();
        //C2_3input.printArray();
        //W1input.printArray();
        //W2input.printArray();
        //W3input.printArray();

        component temp;
        simEvent nextEvent;

        while(Clock < simRuntime){

            if(!I1.isOccupied()){
                temp = new component(c1num, component.componentType.C1, I1);
                System.out.println("Created new component of type " + temp.getcType() + "-" + temp.getIDnum());
                I1.setOccupied(true);
                ScheduleEvent(simEvent.eventType.LI1, temp, temp.getIDnum());
                c1num++;
            }

            if(!I2.isOccupied()){
                //choose c2 or c3 at random
                Random coinFlip = new Random();
                int decider = coinFlip.nextInt(2);

                if(decider == 0){

                    temp = new component(c2num, component.componentType.C2, I2);
                    System.out.println("Created new component of type " + temp.getcType() + "-" + temp.getIDnum());
                    I2.setOccupied(true);
                    ScheduleEvent(simEvent.eventType.LI2_2, temp, temp.getIDnum());
                    c2num++;

                }else{

                    temp = new component(c3num, component.componentType.C3, I2);
                    System.out.println("Created new component of type " + temp.getcType() + "-" + temp.getIDnum());
                    I2.setOccupied(true);
                    ScheduleEvent(simEvent.eventType.LI2_3, temp, temp.getIDnum());
                    c3num++;

                }

            }

            if(!W1.isOccupied() && C1B1_1.isOccupied()){
                temp = C1B1_1.getComp();
                W1.setComp(temp);
                W1.setOccupied(true);
                C1B1_1.setOccupied(false);
                System.out.println("Component " + temp.getcType() + "-" + temp.getIDnum() + " at C1Buffer1 moved to W1");
                C1B1_1.setComp(null);
                if(C1B1_2.isOccupied()){
                    C1B1_1.setComp(C1B1_2.getComp());
                    C1B1_1.setOccupied(true);
                    C1B1_2.setOccupied(false);
                    System.out.println("Component " + temp.getcType() + "-" + temp.getIDnum() + " at C1Buffer1 promoted");
                    C1B1_2.setComp(null);
                }
                ScheduleEvent(simEvent.eventType.LW1, temp, temp.getIDnum());
            }
            if(!W2.isOccupied() && C1B2_1.isOccupied() && C2B_1.isOccupied()){
                temp = C2B_1.getComp();
                W2.setComp(temp);
                W2.setOccupied(true);
                System.out.println("Component " + temp.getcType() + "-" + temp.getIDnum() + " at C2Buffer1 moved to W2");
                C2B_1.setComp(null);
                C1B2_1.setComp(null);
                C2B_1.setOccupied(false);
                C1B2_1.setOccupied(false);
                if(C1B2_2.isOccupied() && !C1B2_1.isOccupied()){
                    C1B2_1.setComp(C1B1_2.getComp());
                    C1B2_2.setOccupied(false);
                    C1B2_1.setOccupied(true);
                    System.out.println("Component " + temp.getcType() + "-" + temp.getIDnum() + " at C1Buffer2 promoted");
                    C1B2_2.setComp(null);
                }
                if(C2B_2.isOccupied() && !C2B_1.isOccupied()){
                    C2B_1.setComp(C2B_2.getComp());
                    C2B_2.setOccupied(false);
                    C2B_1.setOccupied(true);
                    System.out.println("Component " + temp.getcType() + "-" + temp.getIDnum() + " at C2Buffer promoted");
                    C2B_2.setComp(null);
                }
                ScheduleEvent(simEvent.eventType.LW2, temp, temp.getIDnum());
            }
            if(!W3.isOccupied() && C1B3_1.isOccupied() && C3B_1.isOccupied()){
                temp = C3B_1.getComp();
                W3.setComp(temp);
                W3.setOccupied(true);
                System.out.println("Component " + temp.getcType() + "-" + temp.getIDnum() + " at C3Buffer1 moved to W3");
                C3B_1.setComp(null);
                C1B3_1.setComp(null);
                C3B_1.setOccupied(false);
                C1B3_1.setOccupied(false);
                if(C1B3_2.isOccupied() && !C1B3_1.isOccupied()){
                    C1B3_1.setComp(C1B1_2.getComp());
                    C1B3_2.setOccupied(false);
                    C1B3_1.setOccupied(true);
                    System.out.println("Component " + temp.getcType() + "-" + temp.getIDnum() + " at C1Buffer3 promoted");
                    C1B3_2.setComp(null);
                }
                if(C3B_2.isOccupied() && !C3B_1.isOccupied()){
                    C3B_1.setComp(C3B_2.getComp());
                    C3B_2.setOccupied(false);
                    C3B_1.setOccupied(true);
                    System.out.println("Component " + temp.getcType() + "-" + temp.getIDnum() + " at C3Buffer promoted");
                    C3B_2.setComp(null);
                }
                ScheduleEvent(simEvent.eventType.LW3, temp, temp.getIDnum());
            }

            if(FEL.size() > 0) {
                System.out.println("FEL Size: " + FEL.size() + ", Polling!");
            }
            nextEvent = FEL.poll();

            if(nextEvent != null){
                Clock = nextEvent.geteTime();
                System.out.println("CLOCK: " + Clock);
                ProcessEvent(nextEvent, nextEvent.geteComp());
            }
        }

        totalI2Block = Clock - lastI2Block;
        GenerateReport();


    }

    private static void ProcessEvent(simEvent nextEvent, component comp) {
        System.out.println("Processing Event " + nextEvent.geteType());
        switch (nextEvent.geteType()){
            case LI1:
                if(!C1B1_1.isOccupied()){
                    comp.setLocation(C1B1_1);
                    System.out.println("Component at I1 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer1_1");
                    C1B1_1.setComp(comp);
                    I1.setComp(null);
                    I1.setOccupied(false);
                    C1B1_1.setOccupied(true);
                }else if(!C1B2_1.isOccupied()) {
                    comp.setLocation(C1B2_1);
                    System.out.println("Component at I1 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer2_1");
                    C1B2_1.setComp(comp);
                    I1.setComp(null);
                    I1.setOccupied(false);
                    C1B2_1.setOccupied(true);
                }else if(!C1B3_1.isOccupied()) {
                    comp.setLocation(C1B3_1);
                    System.out.println("Component at I1 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer3_1");
                    C1B3_1.setComp(comp);
                    I1.setComp(null);
                    I1.setOccupied(false);
                    C1B3_1.setOccupied(true);
                }else if(!C1B1_2.isOccupied()) {
                    comp.setLocation(C1B1_2);
                    System.out.println("Component at I1 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer1_2");
                    C1B1_2.setComp(comp);
                    I1.setComp(null);
                    I1.setOccupied(false);
                    C1B1_2.setOccupied(true);
                }else if(!C1B2_2.isOccupied()) {
                    comp.setLocation(C1B2_2);
                    System.out.println("Component at I1 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer2_2");
                    C1B2_2.setComp(comp);
                    I1.setComp(null);
                    I1.setOccupied(false);
                    C1B2_2.setOccupied(true);
                }else if(!C1B3_2.isOccupied()) {
                    comp.setLocation(C1B3_2);
                    System.out.println("Component at I1 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer3_2");
                    C1B3_2.setComp(comp);
                    I1.setComp(null);
                    I1.setOccupied(false);
                    C1B3_2.setOccupied(true);
                }else{
                    isI1waiting = true;
                    lastI1Block = Clock;
                    System.out.println("I1 is blocked! Current component " + comp.getcType() + "-" + comp.getIDnum() + ". Started at Clock value " + Clock);
                }
                break;
            case LI2_2:
                if(!C2B_1.isOccupied()){
                    comp.setLocation(C2B_1);
                    System.out.println("Component at I2 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C2Buffer1");
                    C2B_1.setComp(comp);
                    I2.setComp(null);
                    I2.setOccupied(false);
                    C2B_1.setOccupied(true);
                }else if(!C2B_2.isOccupied()){
                    comp.setLocation(C2B_2);
                    System.out.println("Component at I2 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C2Buffer2");
                    C2B_2.setComp(comp);
                    I2.setComp(null);
                    I2.setOccupied(false);
                    C2B_2.setOccupied(true);
                }else{
                    isI2waiting = true;
                    lastI2Block = Clock;
                    System.out.println("I2 is blocked! Current component " + comp.getcType() + "-" + comp.getIDnum() + ". Started at Clock value " + Clock);
                }
                break;
            case LI2_3:
                if(!C3B_1.isOccupied()){
                    comp.setLocation(C3B_1);
                    System.out.println("Component at I2 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C3Buffer1");
                    C3B_1.setComp(comp);
                    I2.setComp(null);
                    I2.setOccupied(false);
                    C3B_1.setOccupied(true);
                }else if(!C3B_2.isOccupied()){
                    comp.setLocation(C3B_2);
                    System.out.println("Component at I2 " + comp.getcType() + "-" + comp.getIDnum() + " moved to C3Buffer2");
                    C3B_2.setComp(comp);
                    I2.setComp(null);
                    I2.setOccupied(false);
                    C3B_2.setOccupied(true);
                }else{
                    isI2waiting = true;
                    lastI2Block = Clock;
                    System.out.println("I2 is blocked! Current component " + comp.getcType() + "-" + comp.getIDnum() + ". Started at Clock value " + Clock);
                }
            case LW1:
                W1.setOccupied(false);
                W1.setComp(null);
                comp.setLocation(OUT);
                p1num++;
                System.out.println("Product 1 created. Total: " + p1num);
                break;
            case LW2:
                W3.setOccupied(false);
                W3.setComp(null);
                comp.setLocation(OUT);
                p2num++;
                System.out.println("Product 2 created. Total: " + p2num);
                break;
            case LW3:
                W3.setOccupied(false);
                W3.setComp(null);
                comp.setLocation(OUT);
                p3num++;
                System.out.println("Product 3 created. Total: " +p3num);
                break;
        }

        System.out.println("Event Processed");
    }

    private static void GenerateReport() {
        System.out.println("----------------SIM FINISHED----------------");
        System.out.println("Sim instructed to stop once an event passed " + simRuntime + " minutes. Sim ran for " + Clock + " minutes.");
        System.out.println("Lambda inputs used: " + lambda1 + ", " + lambda2 + ", " + lambda3 + ", " + lambda4 + ", " + lambda5 + ", " + lambda6 + ".");
        System.out.println("Decimal arrays were of size " + inputArraySize);
        System.out.println("Products Created: P1 - " + p1num + ". P2 - " + p2num + ". P3 - " + p3num);
        System.out.println("Inspector 1 was blocked for " + totalI1Block + " minutes, for a total of " + ((totalI1Block / simRuntime) * 100) + "% blocked");
        System.out.println("Inspector 2 was blocked for " + totalI2Block + " minutes, for a total of " + ((totalI2Block / simRuntime) * 100) + "% blocked");
    }

    private static void ScheduleEvent(simEvent.eventType eType, component comp, int index) {
        double eTime = switch (eType) {
            case LI1 -> C1input.getDecArray()[index];
            case LI2_2 -> C2_2input.getDecArray()[index];
            case LI2_3 -> C2_3input.getDecArray()[index];
            case LW1 -> W1input.getDecArray()[index];
            case LW2 -> W2input.getDecArray()[index];
            case LW3 -> W3input.getDecArray()[index];
            default -> -1;
        };

        simEvent newEvent = new simEvent(eType, Clock + eTime, comp);
        System.out.println("==> New Event! Type: " + eType + ". Finished at: " + (Clock + eTime));
        FEL.offer(newEvent);
    }
}
