import java.util.*;

public class factorySimFlawed {

    //Global double variables for time-related fields

    public static double Clock, simRuntime;
    public static double lastI1Block, totalI1Block, lastI2Block, totalI2Block;

    //Queues and Buffers
    public static Queue<simEvent> FEL;
    public static ArrayList<component> C1buffer1, C1buffer2, C1buffer3, C2buffer, C3buffer;
    public static component I1hold, I2hold;

    //Inputs - using data provided we can choose how to simulate 'randomness'

    private static double lambda1, lambda2, lambda3, lambda4, lambda5, lambda6;
    private static ITTGeneration C1input, C2_2input, C2_3input, W1input, W2input, W3input;
    private static int inputArraySize;

    //Metrics for measuring quantities of interest

    private static int c1num, c2num, c3num, p1num, p2num, p3num;
    private static boolean isI1Blocked, isI2Blocked, isI1Busy, isI2Busy, isW1Busy, isW2Busy, isW3Busy;

    private static void Init(){

        //Zero / initialize all variables
        Clock = 0;
        simRuntime = 10000.0;
        FEL = new PriorityQueue<simEvent>();
        C1buffer1 = new ArrayList<component>();
        C1buffer2 = new ArrayList<component>();
        C1buffer3 = new ArrayList<component>();
        C2buffer = new ArrayList<component>();
        C3buffer = new ArrayList<component>();
        lastI1Block = 0;
        lastI2Block = 0;
        totalI1Block = 0;
        totalI2Block = 0;
        c1num = 0;
        c2num = 0;
        c3num = 0;
        p1num = 0;
        p2num = 0;
        p3num = 0;
        isI1Blocked = false;
        isI2Blocked = false;
        isI1Busy = false;
        isI2Busy = false;
        isW1Busy = false;
        isW2Busy = false;
        isW3Busy = false;
        I1hold = null;
        I2hold = null;

        //ITT generation variables to be customized - note the higher values in 4 and 6 for shorter overall times!
        lambda1 = 0.09654;
        lambda2 = 0.06436;
        lambda3 = 0.04847;
        lambda4 = 0.21718;
        lambda5 = 0.09015;
        lambda6 = 0.11369;
        inputArraySize = 1200; //this value should always be as large as possible to ensure the sim always still has minute values to pull from!

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

        component temp;
        simEvent nextEvent;

        while (Clock < simRuntime){

            //If I1 is free, create a new C1, set I1 to busy and schedule a new event for our C1 to leave!
            //Don't forget to increment the number of C1s so that our decArrays know which time value to look up

            if(!isI1Busy){
                temp = new component(c1num, component.componentType.C1);
                System.out.println("Created new component of type "+ temp.getcType() + "-" + temp.getIDnum());
                isI1Busy = true;
                I1hold = temp;
                ScheduleEvent(simEvent.eventType.LI1, temp, temp.getIDnum());
                c1num++;
            }

            //Same as I1, but with a small twist:

            if(!isI2Busy){

                //choose c2 or c3 at random
                Random coinFlip = new Random();
                int decider = coinFlip.nextInt(2);

                if(decider == 0){

                    temp = new component(c2num, component.componentType.C2);
                    System.out.println("Created new component of type " + temp.getcType() + "-" + temp.getIDnum());
                    isI2Busy = true;
                    I2hold = temp;
                    ScheduleEvent(simEvent.eventType.LI2_2, temp, temp.getIDnum());
                    c2num++;

                }else{

                    temp = new component(c3num, component.componentType.C3);
                    System.out.println("Created new component of type " + temp.getcType() + "-" + temp.getIDnum());
                    isI2Busy = true;
                    I2hold = temp;
                    ScheduleEvent(simEvent.eventType.LI2_3, temp, temp.getIDnum());
                    c3num++;

                }

            }

            if(!isW1Busy && !(C1buffer1.isEmpty())){
                isW1Busy = true;
                temp = C1buffer1.get(0);
                C1buffer1.remove(0);
                if(isI1Blocked){
                    C1buffer1.add(I1hold);
                    I1hold = new component(0, component.componentType.Empty);
                    isI1Blocked = false;
                    isI1Busy = false;
                }
                System.out.println(" Component " + temp.getcType() + "-" + temp.getIDnum() + " moved to W1");
                ScheduleEvent(simEvent.eventType.LW1, temp, p1num);
            }

            if(!isW2Busy && !(C1buffer2.isEmpty()) && !(C2buffer.isEmpty())){
                isW2Busy = true;
                temp = C2buffer.get(0);
                C1buffer2.remove(0);
                C2buffer.remove(0);
                if(isI1Blocked){
                    C1buffer2.add(I1hold);
                    I1hold = new component(0, component.componentType.Empty);
                    isI1Blocked = false;
                    isI1Busy = false;
                }
                if(isI2Blocked){
                    C2buffer.add(I2hold);
                    I2hold = new component(0, component.componentType.Empty);
                    isI2Blocked = false;
                    isI2Busy = false;
                }
                System.out.println(" Component " + temp.getcType() + "-" + temp.getIDnum() + " moved to W2");
                ScheduleEvent(simEvent.eventType.LW2, temp, p2num);
            }

            if(!isW3Busy && !(C1buffer3.isEmpty()) && !(C3buffer.isEmpty())){
                isW3Busy = true;
                temp = C3buffer.get(0);
                C1buffer3.remove(0);
                C3buffer.remove(0);
                if(isI1Blocked){
                    C1buffer3.add(I1hold);
                    I1hold = new component(0, component.componentType.Empty);
                    isI1Blocked = false;
                    isI1Busy = false;
                }
                if(isI2Blocked){
                    C1buffer3.add(I2hold);
                    I2hold = new component(0, component.componentType.Empty);
                    isI2Blocked = false;
                    isI2Busy = false;
                }
                System.out.println(" Component " + temp.getcType() + "-" + temp.getIDnum() + " moved to W3");
                ScheduleEvent(simEvent.eventType.LW3, temp, p3num);
            }

            if(FEL.size() > 0){
                System.out.println("FEL size: " + FEL.size() + ", Polling!");
            }
            nextEvent = FEL.poll();


            //displaySystemState();

            if(nextEvent != null){
                Clock = nextEvent.geteTime();
                System.out.println("Clock advanced to " + Clock);
                System.out.println("- - - - - - - - - - - - -");
                ProcessEvent(nextEvent, nextEvent.geteComp());
            }

            if(lastI1Block != 0){
                totalI1Block += Clock - lastI1Block;
                lastI1Block = Clock;
            }
            if(lastI2Block != 0){
                totalI2Block += Clock - lastI2Block;
                lastI2Block = Clock;
            }


        }

        GenerateReport();

    }

    private static void ProcessEvent(simEvent evt, component comp) {
        System.out.println("Processing Event " + evt.geteType());

        switch(evt.geteType()){
            case LI1:
                if(C1buffer1.isEmpty()){
                    isI1Busy = false;
                    System.out.println("Component " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer1");
                    C1buffer1.add(comp);
                    I1hold = new component(0, component.componentType.Empty);
                    displaySystemState();
                }else if(C1buffer2.isEmpty()){
                    isI1Busy = false;
                    System.out.println("Component " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer2");
                    C1buffer2.add(comp);
                    I1hold = new component(0, component.componentType.Empty);
                    displaySystemState();
                }else if(C1buffer3.isEmpty()){
                    isI1Busy = false;
                    System.out.println("Component " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer3");
                    C1buffer3.add(comp);
                    I1hold = new component(0, component.componentType.Empty);
                    displaySystemState();
                }else if(C1buffer1.size() < 2){
                    isI1Busy = false;
                    System.out.println("Component " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer1, Back");
                    C1buffer1.add(comp);
                    I1hold = new component(0, component.componentType.Empty);
                    displaySystemState();
                }else if(C1buffer2.size() < 2){
                    isI1Busy = false;
                    System.out.println("Component " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer2, Back");
                    C1buffer2.add(comp);
                    I1hold = new component(0, component.componentType.Empty);
                    displaySystemState();
                }else if(C1buffer3.size() < 2){
                    isI1Busy = false;
                    System.out.println("Component " + comp.getcType() + "-" + comp.getIDnum() + " moved to C1Buffer3, Back");
                    C1buffer3.add(comp);
                    I1hold = new component(0, component.componentType.Empty);
                    displaySystemState();
                }else{
                    System.out.println("Inspector 1 Blocked: Started at " + Clock);
                    displaySystemState();
                    isI1Blocked = true;
                    lastI1Block = Clock;
                }
                break;
            case LI2_2:
                if(C2buffer.size() < 2){
                    System.out.println("Component " + comp.getcType() + "-" + comp.getIDnum() +" moved to buffer2");
                    C2buffer.add(comp);
                    I2hold = new component(0, component.componentType.Empty);
                    displaySystemState();
                    isI2Busy = false;
                }else{
                    System.out.println("Inspector 2 Blocked: Started at " + Clock);
                    displaySystemState();
                    isI2Blocked = true;
                    lastI2Block = Clock;
                }
                break;
            case LI2_3:
                if(C3buffer.size() < 2){
                    System.out.println("Component " + comp.getcType() + "-" + comp.getIDnum() +" moved to buffer3");
                    C3buffer.add(comp);
                    I2hold = new component(0, component.componentType.Empty);
                    displaySystemState();
                    isI2Busy = false;
                }else{
                    System.out.println("Inspector 2 Blocked: Started at "+ Clock);
                    displaySystemState();
                    isI2Blocked = true;
                    lastI2Block = Clock;
                }
                break;
            case LW1:
                p1num++;
                isW1Busy = false;
                System.out.println("Created Product P1. Total: " + p1num);
                displaySystemState();
                break;
            case LW2:
                p2num++;
                isW2Busy = false;
                System.out.println("Created Product P2. Total: " + p2num);
                if(isI2Blocked){
                    C2buffer.add(I2hold);
                    isI2Blocked = false;
                    isI2Busy = false;
                    I2hold = new component(0, component.componentType.Empty);
                }
                displaySystemState();
                break;
            case LW3:
                p3num++;
                isW3Busy = false;
                System.out.println("Created Product P3. Total: " + p3num);
                if(isI2Blocked){
                    C3buffer.add(I2hold);
                    isI2Blocked = false;
                    isI2Busy = false;
                    I2hold = new component(0, component.componentType.Empty);
                }
                displaySystemState();
                break;
        }

        System.out.println("Event " + evt.geteType() + " Processed.");
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

    private static void GenerateReport() {

        System.out.println("----------------SIM FINISHED----------------");
        System.out.println("Sim instructed to stop once an event passed " + simRuntime + " minutes. Sim ran for " + Clock + " minutes.");
        System.out.println("Lambda inputs used: " + lambda1 + ", " + lambda2 + ", " + lambda3 + ", " + lambda4 + ", " + lambda5 + ", " + lambda6 + ".");
        System.out.println("Products Created: P1 - " + p1num + ". P2 - " + p2num + ". P3 - " + p3num);
        System.out.println("Inspector 1 was blocked for " + totalI1Block + " minutes, for a total of " + ((totalI1Block / simRuntime) * 100) + "% blocked");
        System.out.println("Inspector 2 was blocked for " + totalI2Block + " minutes, for a total of " + ((totalI2Block / simRuntime) * 100) + "% blocked");

    }

    public static void displaySystemState(){
        System.out.println("########################");
        System.out.println("SYSTEM STATE:");
        System.out.println("I1: " + I1hold.getcType() + "-" + I1hold.getIDnum());
        System.out.println("I2: " + I2hold.getcType() + "-" + I2hold.getIDnum());

        System.out.println(C1buffer1.size() + " " + C1buffer2.size() + " " + C1buffer3.size() + " " + C2buffer.size() + " " + C3buffer.size());

        if(C1buffer1.size() > 0){
            System.out.println("C1Buffer1 " + C1buffer1.get(0).getcType() + "-" + C1buffer1.get(0).getIDnum());
            if(C1buffer1.size() > 1){
                System.out.println(C1buffer1.get(1).getcType() + "-" + C1buffer1.get(1).getIDnum());
            }
        }
        if(C1buffer2.size() > 0){
            System.out.println("C1Buffer2 " + C1buffer2.get(0).getcType() + "-" + C1buffer2.get(0).getIDnum());
            if(C1buffer2.size() > 1){
                System.out.println(C1buffer2.get(1).getcType() + "-" + C1buffer2.get(1).getIDnum());
            }
        }
        if(C1buffer3.size() > 0){
            System.out.println("C1Buffer3 " + C1buffer3.get(0).getcType() + "-" + C1buffer3.get(0).getIDnum());
            if(C1buffer3.size() > 1){
                System.out.println(C1buffer3.get(1).getcType() + "-" + C1buffer3.get(1).getIDnum());
            }
        }
        if(C2buffer.size() > 0){
            System.out.println("C2Buffer " + C2buffer.get(0).getcType() + "-" + C2buffer.get(0).getIDnum());
            if(C2buffer.size() > 1){
                System.out.println(C2buffer.get(1).getcType() + "-" + C2buffer.get(1).getIDnum());
            }
        }
        if(C3buffer.size() > 0){
            System.out.println("C3Buffer " + C3buffer.get(0).getcType() + "-" + C3buffer.get(0).getIDnum());
            if(C3buffer.size() > 1){
                System.out.println(C3buffer.get(1).getcType() + "-" + C3buffer.get(1).getIDnum());
            }
        }
        System.out.println("########################");
    }
}
