package server;

import java.security.InvalidParameterException;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.Runnable;

class InventoryItem
{
    public String name;
    public int qty;

    public InventoryItem(String nameParam, int qtyParam)
    {
        if(nameParam == null || nameParam.length() <= 0) throw new InvalidParameterException();

        name = nameParam;
        qty = qtyParam;
    }

    public void updateQty(int qtyChange)
    {
        qty -= qtyChange;
    }

    public static Comparator<InventoryItem> ItemComparator = new Comparator<InventoryItem>()
    {
        public int compare(InventoryItem i1, InventoryItem i2)
        {
            String item1Name = i1.name.toUpperCase();
            String item2Name = i2.name.toUpperCase();
            return item1Name.compareTo(item2Name);
        }
    };

    public boolean repOk(){
        return this.name != null;
    }
}

class Order
{
    public int id;
    public String username;
    public String productName;
    public int qty;

    public Order(int idParam, String usernameParam, String productNameParam, int qtyParam)
    {
        if(usernameParam == null || usernameParam.length() <= 0) throw new InvalidParameterException();
        if(productNameParam == null || productNameParam.length() <= 0) throw new InvalidParameterException();

        id = idParam;
        username = usernameParam;
        productName = productNameParam;
        qty = qtyParam;
    }
}


class Listener implements Runnable
{
    public String protocol;
    public int tcpPort;
    public volatile boolean running = true;

    public Listener(String protocolParam, int portParam)
    {
        protocol = protocolParam;
        tcpPort = portParam;
    }

    public void stop(){
        running = false;
    }

    public void run()
    {
        try
        {
            ServerSocket tcpServerSocket = new ServerSocket(tcpPort);
            Socket tcpClientSocket;
            while (running)
            {
                tcpClientSocket = tcpServerSocket.accept();
                Runnable tcpServerThread = new ServerThread(tcpClientSocket, tcpPort);
                new Thread(tcpServerThread).start();
            }
            tcpServerSocket.close();

        } catch (SocketException se)
        {
            System.err.println(se);
        } catch (IOException ioe)
        {
            System.err.println(ioe);
        }
    }

}


class ServerThread implements Runnable
{
    public static ArrayList<InventoryItem> invList = new ArrayList<InventoryItem>();
    public static ArrayList<Order> orderList = new ArrayList<Order>();
    public static int maxOrderID = 100;  //Initialize the orderIDs

    public String protocol;  //T = TCP
    public int port;

    public Socket tcpClientSocket;

    public ServerThread(Socket tcpClientSocketParam, int tcpPort)
    {
        //Constructor for incoming TCP request
        protocol = "T";
        port = tcpPort;
        tcpClientSocket = tcpClientSocketParam;
    }



    public static String processMessage(String[] msgArray)
    {
        if(msgArray == null || msgArray.length <= 0) throw new InvalidParameterException();

        switch (msgArray[0])
        {
            case "purchase":
                return purchase(msgArray);

            case "cancel":
                return cancel(msgArray);

            case "search":
                return search(msgArray);

            case "list":
                return list();
        }
        return "Invalid request";
    }

    public static String purchase(String[] msgArray)
    {
        if(msgArray == null || msgArray.length <= 0) throw new InvalidParameterException();
        int foundProd = 0;
        String replyMessage = "";
        if (msgArray.length == 4)
        {
            for (InventoryItem myItem : invList)
            {
                if (myItem.name != null && myItem.name.equals(msgArray[2]))
                {
                    foundProd = 1;
                    if (myItem.qty >= Integer.parseInt(msgArray[3]))
                    {
                        maxOrderID += 1;
                        Order myOrder = new Order(maxOrderID, msgArray[1], msgArray[2], Integer.parseInt(msgArray[3]));
                        orderList.add(myOrder);
                        myItem.updateQty(Integer.parseInt(msgArray[3]));
                        return "Your order has been placed, " + maxOrderID + ", " + msgArray[2] + ", " + msgArray[3];
                    } else return "Not Available - Not enough items";
                }
            }
            if (foundProd == 0) return "Not Available - We do not sell this product";
        } else return "Incorrect number of parameters for PURCHASE";
        return replyMessage;
    }

    ;

    public static String cancel(String[] msgArray)
    {
        if(msgArray == null || msgArray.length <= 0) throw new InvalidParameterException();
        int foundOrder = 0;
        String replyMessage = "";
        if (msgArray[1].matches("^\\d+$"))
        {
            for (Order myOrder : orderList)
            {
                if (myOrder.id == Integer.parseInt(msgArray[1]))
                {
                    foundOrder = 1;
                    for (InventoryItem myItem : invList)
                    {
                        if (myItem.name.equals(myOrder.productName))
                        {
                            myItem.updateQty(-1 * myOrder.qty);
                        }
                    }
                    orderList.remove(orderList.indexOf(myOrder));
                    return replyMessage = "Order " + msgArray[1] + " is cancelled";
                }
            }
            if (foundOrder == 0) return replyMessage = msgArray[1] + " not found, no such order";
        } else return replyMessage = "Order ID is not a number";
        return replyMessage;
    }



    public static String search(String[] msgArray)
    {
        if(msgArray == null || msgArray.length <= 0) throw new InvalidParameterException();
        int foundOrder = 0;
        String replyMessage = "";
        for (Order myOrder : orderList)
        {
            if (myOrder.username.equals(msgArray[1]))
            {
                foundOrder = 1;
                replyMessage += myOrder.id + ", " + myOrder.productName + ", " + myOrder.qty + "\n";
            }
        }
        if (foundOrder == 0) return replyMessage = "No order found for " + msgArray[1];
        return replyMessage;
    }


    public static String list()
    {
        String replyMessage = "";
        for (InventoryItem myItem : invList)
            replyMessage += myItem.name + " " + myItem.qty + "; \n";

        return replyMessage;
    }


    public void run()
    {
        try
        {
            //We have received a TCP socket from the client.  Receive message and reply.
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(tcpClientSocket.getInputStream()));
            PrintWriter outputWriter = new PrintWriter(tcpClientSocket.getOutputStream(), true);
            String inputLine = inputReader.readLine();
            if (inputLine.length() > 0)
            {
                String[] msgArray = inputLine.trim().split("\\s+");
                String replyMessage = processMessage(msgArray);
                String[] replyArray = replyMessage.split(System.getProperty("line.separator"));
                for (int x = 0; x < replyArray.length; x++) outputWriter.write(replyArray[x]);
                outputWriter.flush();
                outputWriter.close();
            }
            tcpClientSocket.close();

        } catch (SocketException se)
        {
            System.err.println(se);
        } catch (IOException ioe)
        {
            System.err.println(ioe);
        }
    }
}


public class Server
{

    public static void processInventory(String fileName){
        if(fileName == null || fileName.length() <= 0) throw new InvalidParameterException();
        try {
            FileReader invFile = new FileReader(fileName);
            BufferedReader invBuff = new BufferedReader(invFile);
            String invLine = null;
            while ((invLine = invBuff.readLine()) != null) {
                if (invLine.length() > 0) {
                    String[] itemArr = invLine.split("\\s+");
                    if (itemArr.length == 2) {
                        InventoryItem newItem = new InventoryItem(itemArr[0], Integer.parseInt(itemArr[1]));
                        ServerThread.invList.add(newItem);
                    }
                }
            }
            Collections.sort(ServerThread.invList, InventoryItem.ItemComparator);  //Sort the inventory items by name
        }catch(IOException e){System.out.println("IOException reading inventory file; " + e);}
    }

    public static void main(String[] args)
    {
        int tcpPort;
        if (args.length != 2)
        {
            System.out.println("ERROR: Provide 2 arguments");
            System.out.println("\t(1) <tcpPort>: the port number for TCP connection");
            System.out.println("\t(3) <file>: the file of inventory");

            System.exit(-1);
        }

        tcpPort = Integer.parseInt(args[0]);
        String fileName = args[1];

        processInventory(fileName);

        //spawn tcpListener;
        Runnable tr = new Listener("T", tcpPort);
        new Thread(tr).start();
    }
}
