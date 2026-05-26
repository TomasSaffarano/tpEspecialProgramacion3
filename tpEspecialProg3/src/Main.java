import servicios.Servicios;

public class Main {

    public static void main(String[] args) {
        //"/csv/camiones.csv"

        Servicios servicios = new Servicios("/csv/camiones.csv", "/csv/paquetes.csv");

        System.out.println(servicios.servicio1("noexiste"));
        System.out.println(servicios.servicio1("P001"));

        System.out.println(servicios.servicio2(true));
        System.out.println("----------------------------------------");
        System.out.println(servicios.servicio2(false));



    }
}
