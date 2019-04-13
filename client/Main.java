import java.io.PrintWriter;
import java.net.* ; 
import java.io.* ; 
import java.net.Socket;
import java.util.Scanner;

public class Main{

    static Socket socket ; 
    static String address = "127.0.0.1"; 
    final static int sortPort = 1900, medianPort = 1901, minmaxPort = 1902, freqPort = 1903;

    public static void setupSocket(int port){
        try{
            socket = new Socket(address , port) ; 
        }
        catch(Exception E){

            System.out.println("Exception in setupSocket() : " + E);
            System.exit(2);
        }
    }

    public static void sendDataToServer(String data){
        try{
            PrintWriter out = new PrintWriter(socket.getOutputStream()) ; 
            out.write(data) ; 
            out.flush();
        }
        catch(Exception E){
            System.out.println(E);
            System.exit(3);
        }
    }

    public static String getDataFromServer() {
        System.out.println("Waiting for data from server ... " );
        char[] buffer = new char[20000];
        try {
            (new BufferedReader(new InputStreamReader(socket.getInputStream()))).read(buffer);
        } catch (Exception E) {
            System.out.println(E);
            System.exit(1) ; 
        }
        return new String(buffer);
    }

    public static void main(String[] args) {
        System.out.println("\n1.Sorting\n2.Find min and max\n3.Find Median\n4.Find frequency ");
        System.out.print("\nWhat Service do you want ? : ");
        Scanner scan = new Scanner(System.in) ; 
        int choice = scan.nextInt() ; 
        if(choice<1 || choice>4) System.exit(0);

        switch(choice){
            case 1 : 
                setupSocket(sortPort);
                break ; 
            case 2 : 
                setupSocket(minmaxPort);
                break ; 
            case 3 : 
                setupSocket(medianPort);
                break ; 
            case 4 :
                setupSocket(freqPort);
                break ; 
        }
        
        System.out.println("Enter numbers : ");
        scan.nextLine() ;
        sendDataToServer(scan.nextLine())  ;

        String data = getDataFromServer() ; 
        System.out.println("Server Sent Result : ");
        System.out.println("====================\n") ;
        System.out.println(data) ; 
        System.out.println("\n====================") ;
        try{
            socket.close() ; 
        }
        catch(Exception E){
            System.out.println(E);
        }
    }
}