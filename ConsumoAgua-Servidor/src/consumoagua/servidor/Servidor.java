package consumoagua.servidor;

import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Server;
import consumoagua.rmi.lib.IRemotaCalculoConsumoAgua;

public class Servidor {

    public static void main(String[] args) {
        try {
            CallHandler callHandler = new CallHandler();

            callHandler.registerGlobal(
                    IRemotaCalculoConsumoAgua.class,
                    new CalculoRmiConsumoAguaImplem()
            );

            Server server = new Server();
            server.bind(9007, callHandler);

            System.out.println("Servidor RMI iniciado en el puerto 9007...");
            System.out.println("Esperando conexiones...");

        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor:");
            e.printStackTrace();
        }
    }
}
