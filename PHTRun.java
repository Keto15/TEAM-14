package base;


import lejos.robotics.SampleProvider;
import ev3dev.sensors.BaseSensor;
import lejos.hardware.port.Port;
import lejos.robotics.navigation.LineFollowingMoveController;
import lejos.utility.Delay;


//TODO: Do we need those imports?

/**
 *  Title: PHTRun thread
 *
 *  This is the thread where all the truck logic for task execution should be implemented.
 *  Use function method to do that (it can be extended with other functions).
 */

class PHTRun extends Thread {
    private Thread t;
    private String threadName;

    private static int HALF_SECOND = 500;
    private static int ONE_SECOND = 1000;
    private static int TWO_SECOND = 2000;
    private static int THREE_SECOND = 3000;

    PHTRun ( String threadName) {
        this.threadName = threadName;
        System.out.println("Creating " +  this.threadName );
    }

    private boolean runMotors() {

        try {
            while (PackageHandlingTruck.isRunning && !PackageHandlingTruck.runThreadIsExecuted) {


                //TODO: YOUR CODE HERE
                //TODO: CHECK THIS DOCUMENTATION TO UNDERSTAND HOW TO RUN THIS TRUCK
                //TODO: AND HOW TO WRITE CODE:
                //https://github.com/CONNEX-AB-Delivery-System/DS-DeliveryTruck/blob/master/README.md
                



             /* SampleProvider sp = PackageHandlingTruck.lineReader.getRGBMode() ;
                int sampleSize = sp.sampleSize();
                float[] sample = new float[sampleSize];
                for  (int i = 0; i < 10; i++) {
                    sp.fetchSample(sample, 0);
                    System.out.println("R=" + i + " Sample={}" + (int) sample[0]);
                    System.out.println("G=" + i + " Sample={}" + (int) sample[1]);
                    System.out.println("B=" + i + " Sample={}" + (int) sample[2]);

                    Delay.msDelay(HALF_SECOND);
                }
            */



                Thread.sleep(1000);
                followlinered();
                uturn();
                followlinegreen();
                drop();
                uturn();






                PackageHandlingTruck.runThreadIsExecuted = true;
                PackageHandlingTruck.outputCommandSCS = "FINISHED";
                System.out.println("Task Executed");

            }

        } catch (InterruptedException e) {
            System.out.println("Thread " +  this.threadName + " interrupted.");
        }

        PackageHandlingTruck.leftMotor.stop(true);
        PackageHandlingTruck.rightMotor.stop(true);

        return true;
    }

    public  static void followlinered()
    {

        PackageHandlingTruck.rightMotor.setSpeed(150);
        PackageHandlingTruck.rightMotor.backward();
        PackageHandlingTruck.leftMotor.setSpeed(150);
        PackageHandlingTruck.leftMotor.backward();
        // follow the line!!!!

        SampleProvider sp = PackageHandlingTruck.lineReader.getRGBMode() ;
        int sampleSize = sp.sampleSize();
        float[] sample = new float[sampleSize];

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean stopflag4 = false;
        while(!stopflag4){

            sp.fetchSample(sample,0);

            int [] speed = (linefollower(sample[0],sample[1],sample[2]));



            PackageHandlingTruck.leftMotor.setSpeed(speed[0]);
            PackageHandlingTruck.rightMotor.setSpeed(speed[1]);
            PackageHandlingTruck.rightMotor.backward();

            PackageHandlingTruck.leftMotor.backward();
            System.out.println(speed[0]);
            System.out.println(speed[1]);
            System.out.println("R=" + (int) sample[0]);
            System.out.println("G=" + (int) sample[1]);
            System.out.println("B="  + (int) sample[2]);





            if(sample[0]>=140 &&  sample[1]<=55 && sample[2]>= 10 && sample[2]<= 65 )
            {
                PackageHandlingTruck.rightMotor.stop(true);
                PackageHandlingTruck.leftMotor.stop(true);
                stopflag4 = true;

            }


            Delay.msDelay(200);

        }

    }


    public  static void followlinegreen()
    {

        PackageHandlingTruck.rightMotor.setSpeed(175);
        PackageHandlingTruck.rightMotor.backward();
        PackageHandlingTruck.leftMotor.setSpeed(175);
        PackageHandlingTruck.leftMotor.backward();
        // follow the line!!!!

        SampleProvider sp = PackageHandlingTruck.lineReader.getRGBMode() ;
        int sampleSize = sp.sampleSize();
        float[] sample = new float[sampleSize];

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean stopflag4 = false;
        while(!stopflag4){

            sp.fetchSample(sample,0);

            int [] speed = (linefollower(sample[0],sample[1],sample[2]));



            PackageHandlingTruck.leftMotor.setSpeed(speed[0]);
            PackageHandlingTruck.rightMotor.setSpeed(speed[1]);
            PackageHandlingTruck.rightMotor.backward();

            PackageHandlingTruck.leftMotor.backward();
            System.out.println(speed[0]);
            System.out.println(speed[1]);
            System.out.println("R=" + (int) sample[0]);
            System.out.println("G=" + (int) sample[1]);
            System.out.println("B="  + (int) sample[2]);




            //jgh +20
            if(sample[0]<= 65 &&  sample[1]>=80 && sample[2]<= 60   )
            {
                PackageHandlingTruck.rightMotor.stop(true);
                PackageHandlingTruck.leftMotor.stop(true);
                stopflag4 = true;

            }


            Delay.msDelay(200);

        }

    }


    public static void uturn()
    {
        SampleProvider sp = PackageHandlingTruck.lineReader.getRGBMode() ;
        int sampleSize = sp.sampleSize();
        float[] sample = new float[sampleSize];

        PackageHandlingTruck.rightMotor.setSpeed(125);
        PackageHandlingTruck.rightMotor.forward();
        PackageHandlingTruck.leftMotor.setSpeed(125);
        PackageHandlingTruck.leftMotor.backward();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean stopflag3 = false ;



        while(!stopflag3)
        {

            sp.fetchSample(sample,0);
            int average = (int)(sample[0] + sample[1] + sample[2])/3;
            System.out.println("average=" + average);
            if (average<100)
            {
                stopflag3= true;
                PackageHandlingTruck.rightMotor.stop();
                PackageHandlingTruck.leftMotor.stop();
            }


            Delay.msDelay(300);

        }
        


    }

    public static void drop()
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        PackageHandlingTruck.liftMotor.setSpeed(100);
        PackageHandlingTruck.liftMotor.rotateTo(-80, true);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PackageHandlingTruck.liftMotor.stop(true);

        SampleProvider sp2 = PackageHandlingTruck.palletDetector.getRGBMode() ;
        int sampleSize2 = sp2.sampleSize();
        float[] sample2 = new float[sampleSize2];
        System.out.println("R2=" + (int) sample2[0]);
        System.out.println("G2=" + (int) sample2[1]);
        System.out.println("B2="+ (int) sample2[2]);
        Delay.msDelay(HALF_SECOND);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PackageHandlingTruck.leftMotor.setSpeed(100);
        PackageHandlingTruck.rightMotor.setSpeed(100);
        PackageHandlingTruck.leftMotor.forward();
        PackageHandlingTruck.rightMotor.forward();


        Boolean stopflag2 = false;
        while (!stopflag2)
        {
            sp2.fetchSample(sample2, 0);
            if(sample2[0]<=5  && sample2[1]<=10  && sample2[2]<= 15)

            {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                PackageHandlingTruck.rightMotor.stop();
                PackageHandlingTruck.leftMotor.stop();
                stopflag2 = true;

            }
            System.out.println("R2=" + (int) sample2[0]);
            System.out.println("G2=" + (int) sample2[1]);
            System.out.println("B2="+ (int) sample2[2]);
            Delay.msDelay(HALF_SECOND);

        }


    }

    public static int [] linefollower (float R, float G, float B){
            float var3 = (float)((R + G + B) / 3);
            float var4 = var3 * 100.0F / 255.0F;
            int[] speed = new int[2];
            if (var4 <= 18.0F) {
                speed[0] = 175;
                speed[1] = 0;
            } else if (var4 > 18.0F && var4 <= 25.0F) {
                speed[0] = 175;
                speed[1] = 90;
            } else if (var4 > 25.0F && var4 <= 29.0F) {
                speed[0] = 175;
                speed[1] = 135;
            } else if (var4 > 29 && var4 < 31 ){
                speed[0] = 175;
                speed[1] = 175;
            } else if (var4 >= 31.0F && var4 < 35.0F) {
                speed[0] = 135;
                speed[1] = 175;
            } else if (var4 >= 35.0F && var4 < 42.0F) {
                speed[0] = 90;
                speed[1] = 175;
            } else if (var4 >= 42.0F && var4 < 52.0F) {
                speed[0] = 0;
                speed[1] = 175;
            }

            return speed;
        }



    public void run() {
        System.out.println("Running " +  this.threadName );

        this.runMotors();

        System.out.println("Thread " +  this.threadName + " exiting.");
    }

    public void start () {
        System.out.println("Starting " +  this.threadName );
        if (t == null) {
            t = new Thread (this, this.threadName);
            t.start ();
        }
    }
}