package main;

import server.*;

public class Main {

    public static void main(String[] args)
    {
        Server.webServer = new WebServer(8081);
        Server.webServer.run();
    }
}
