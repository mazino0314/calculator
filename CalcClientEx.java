package Calculator;

import java.io.*;
import java.net.*;
import java.util.*;

public class CalcClientEx {
    
    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                System.out.print("Calculation formula (enter blank, e.g. ADD 24 42)>>"); // prompt
                String outputMessage = scanner.nextLine(); // Read formulas from your keyboard
                if (outputMessage.equalsIgnoreCase("bye")) {
                    out.write(outputMessage + "\n"); // Send "bye" string
                    out.flush();
                    break; // If the user enters “bye”, it is sent to the server and the connection is terminated.
                }
                out.write(outputMessage + "\n"); // Send formula string read from keyboard
                out.flush();
                String inputMessage = in.readLine(); // Receive calculation results from server
                System.out.println( inputMessage);
            }
        } catch (IOException e) {
            System.out. println(e.getMessage());
        } finally {
            try {
                scanner.close();
                if (socket != null)
                    socket.close(); //close client socket
            } catch (IOException e) {
                System.out.println("An error occurred while chatting with the server.");
            }
        }
    }
}