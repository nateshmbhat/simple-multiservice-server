import java.io.*;
import java.net.*;
import java.util.*;

class Server {
    private static String getDataFromClient(Socket clientSocket) {
        char[] buffer = new char[20000];
        try {
            (new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))).read(buffer);
        } catch (Exception e) {
            System.out.println(e);
            System.exit(2);
        }
        System.out.println("\nClient sent data : \n " + (new String(buffer)) + "\n");
        return new String(buffer);
    }

    public static ArrayList<Integer> getNumbersFromClient(Socket clientSocket) {
        String[] array = getDataFromClient(clientSocket).split(" ");
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < array.length; i++){
            System.out.print(i);
            try {
                System.out.println(array[i]);
                numbers.add(Integer.parseInt(array[i].trim()));
                
            } catch (Exception E) {
                System.out.println(i);
            }
        }
        return numbers;
    }

    public static void sendDataToClient(Socket clientSocket, String data) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            out.write(data);
            out.close();
        } catch (Exception E) {
            System.out.println(E);
            System.exit(3);
        }
    }
}

class SortingService extends Server {
    static void startService(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        System.out.println("Started Sorting Server ...");
        Socket client;
        while (true) {
            client = socket.accept();
            ArrayList<Integer> numbers = getNumbersFromClient(client);
            Collections.sort(numbers);
            System.out.println(numbers);
            sendDataToClient(client, numbers.toString());
            client.close();
        }
    }
}

class MedianService extends Server {
    static void startService(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        System.out.println("Started Median Server ...");
        Socket client;
        while (true) {
            client = socket.accept();
            ArrayList<Integer> numbers = getNumbersFromClient(client);
            Collections.sort(numbers);
            System.out.println(numbers);
            int midindex = numbers.size() / 2;

            if(midindex-1 <0){
                sendDataToClient(client, "Median is : " + numbers.get(0));
                client.close();
                return ; 
            }

            double result = (numbers.size() % 2 == 1) ? Double.parseDouble(""+numbers.get(midindex)) : (numbers.get(midindex) + numbers.get(midindex - 1)) / 2.0 ; 

            System.out.println("Result = " + numbers.size());
            sendDataToClient(client, "Median is : " + result );
            client.close();
        }
    }
}

class MinMaxService extends Server {
    static void startService(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        System.out.println("Started MinMax Server ...");
        Socket client;
        while (true) {
            client = socket.accept();
            ArrayList<Integer> numbers = getNumbersFromClient(client);
            System.out.println(numbers);

            sendDataToClient(client, "Minimum = " + Collections.min(numbers) + " : Maximum = "  + Collections.max(numbers)) ; 
            client.close();
        }
    }
}

class FrequencyService extends Server {
    static void startService(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        System.out.println("Started Frequency Server ...");
        Socket client;
        while (true) {
            client = socket.accept();
            ArrayList<Integer> numbers = getNumbersFromClient(client);
            HashMap<Integer,Integer> map = new HashMap<>(); 
            for(Integer n : numbers){
                Integer f = map.get(n) ; 
                if(f==null) map.put(n , 1) ; 
                else map.put(n , f+1) ; 
            }

            System.out.println(numbers);
            sendDataToClient(client, "Frequency Mapping is : \n" + map );
            client.close();
        }
    }
}

class Main {
    final static int sort = 1900, median = 1901, minmax = 1902, freq = 1903;

    public static void main(String[] args) {
        try {
            Thread sortThread = new Thread(new Runnable() {
                public void run() {
                    try{
                        SortingService.startService(sort);
                    }
                    catch(Exception E){System.out.println(E);}
                }
            });

            Thread medianThread = new Thread(new Runnable() {
                public void run() {
                    try{
                        MedianService.startService(median);
                    }
                    catch(Exception E){System.out.println(E);}
                }
            });

            Thread minmaxThread = new Thread(new Runnable() {
                public void run() {
                    try{
                        MinMaxService.startService(minmax);
                    }
                    catch(Exception E){System.out.println(E);}
                }
            });

            Thread frequencyThread = new Thread(new Runnable() {
                public void run() {
                    try{
                        FrequencyService.startService(freq);
                    }
                    catch(Exception E){System.out.println(E);}
                }
            });


            sortThread.start() ; 
            medianThread.start() ; 
            minmaxThread.start() ; 
            frequencyThread.start() ; 
        }

        catch (Exception E) {
            System.out.println("Exception Occured : " + E);
        }
    }
}