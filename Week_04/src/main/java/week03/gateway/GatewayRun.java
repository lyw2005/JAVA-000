package week03.gateway;

import week03.gateway.inbound.GatewayServer;
import week03.gateway.outbound.netty.NettyHttpClient;

public class GatewayRun {

    public static void main(String[] args) {
        GatewayServer gatewayServer = new GatewayServer(8888, 1, 16);
        gatewayServer.startGateway();

    }

}
