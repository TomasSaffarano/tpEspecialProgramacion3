import servicios.Servicios;

public class Main {

    public static void main(String[] args) {
        //"/csv/camiones.csv"

        Servicios servicios = new Servicios("/csv/camiones.csv", "/csv/paquetes.csv");

/*        System.out.println("CAMIONES:");
        for (Camion c : camiones) {
            System.out.println(c);
        }

        System.out.println("\nPAQUETES:");
        for (Paquete p : paquetes) {
            System.out.println(p);
        }
*/

        
        System.out.println(servicios.servicio1("noexiste"));
        System.out.println(servicios.servicio1("P001"));



    }
}
