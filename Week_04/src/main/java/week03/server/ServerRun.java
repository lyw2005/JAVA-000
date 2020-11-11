package week03.server;

public class ServerRun {

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8808);
        httpServer.startServer();
    }
}
