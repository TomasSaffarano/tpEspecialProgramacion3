import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<camion> camiones = csvReader.leerCamiones("/csv/camiones.csv");
        ArrayList<paquete> paquetes = csvReader.leerPaquetes("/csv/paquetes.csv");

        System.out.println("CAMIONES:");
        for (camion c : camiones) {
            System.out.println(c);
        }

        System.out.println("\nPAQUETES:");
        for (paquete p : paquetes) {
            System.out.println(p);
        }
    }
}
