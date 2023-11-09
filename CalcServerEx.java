package Calculator;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class CalcServerEx {
    public static String calc(String exp) {
        StringTokenizer st = new StringTokenizer(exp, " ");
        if (st.countTokens() != 3)
            return "error";
        String res1 = "";
        String res2 = "";
        String opcode = st.nextToken();
        int op1 = Integer.parseInt(st.nextToken());
        int op2 = Integer.parseInt(st.nextToken());
        String reop1 = Integer.toString(op1);
        String reop2 = Integer.toString(op2);
        switch (opcode) 
        {
            case "ADD":
                res1 = Integer.toString(op1 + op2);
                res2 = " "+ reop1+" + "+ reop2 + " = " + res1;
                break;
            case "MIN":
                res1 = Integer.toString(op1 - op2);
                res2 = " "+ reop1+" - "+ reop2 + " = " + res1;
                break;
            case "MUL":
                res1 = Integer.toString(op1 * op2);
                res2 = " "+ reop1+" * "+ reop2 + " = " + res1;
                break;
            case "DIV":
                if(op1 == 0 || op2 == 0){
                    res2 = "Incorrect : divided by zero";
                    break;
                }
                else{
                    res1 = Integer.toString(op1 / op2);
                    res2 = " "+ reop1+" / "+ reop2 + " = " + res1;
                    break;
                }
            default:
                res2 = "You entered an incorrect value. Check the values you entered and re-enter them ";
        }
        return res2;
    }

    public static void main(String[] args) throws Exception {
        ServerSocket listener = null;
        Socket sock = null;
        
        listener = new ServerSocket(9999); // Create server socket
        System.out.println("Waiting for connection.....");
        ExecutorService pool = Executors.newFixedThreadPool(20);
         while(true){
            sock= listener.accept(); // Waiting for connection request from client
            pool.execute(new Capitalizer(sock));
        }    
    }

    private static class Capitalizer implements Runnable{
        private Socket socket;

        Capitalizer(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run(){
            BufferedReader in = null;
            BufferedWriter out = null;

            System.out.println("Connected: " + socket);
            try{
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));
                while (true) {
                    String inputMessage = in.readLine();
                    if (inputMessage.equalsIgnoreCase("bye")) {
                        System.out.println("The connection was closed by the client.");
                        break; // Close the connection when you receive "bye"
                    }
                    System.out.println(inputMessage); // Print received messages on the screen
                    String res = calc(inputMessage); // calculate. The calculation result is res
                    out.write(res + "\n"); // Transmit calculation result string
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    socket.close(); // Close communication socket
                } catch (IOException e) {
                    System.out.println("An error occurred while chatting with the client.");
                }
                System.out.println("Closed: " + socket);
            }
        }
    }
    
}
