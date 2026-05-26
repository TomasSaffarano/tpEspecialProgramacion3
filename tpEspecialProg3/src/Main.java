import modelos.Camion;
import modelos.Paquete;
import servicios.CSVReader;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Camion> camiones = CSVReader.leerCamiones("/csv/camiones.csv");
        ArrayList<Paquete> paquetes = CSVReader.leerPaquetes("/csv/paquetes.csv");

        System.out.println("CAMIONES:");
        for (Camion c : camiones) {
            System.out.println(c);
        }

        System.out.println("\nPAQUETES:");
        for (Paquete p : paquetes) {
            System.out.println(p);
        }
    }
}
