import servicios.Servicios;

public class Main {

    public static void main(String[] args) {
        //"/csv/camiones.csv"

        Servicios servicios = new Servicios("/csv/camiones.csv", "/csv/paquetes.csv");


        //pruebas primera parte
/*
        // servicio 1
        System.out.println(servicios.servicio1("noexiste"));
        System.out.println(servicios.servicio1("P001"));

        // servicio 2
        System.out.println(servicios.servicio2(true));
        System.out.println("----------------------------------------");
        System.out.println(servicios.servicio2(false));
*/

        // servicio 3
        System.out.println(servicios.servicio3(-1,6));
        System.out.println("----------------------------------------");
        System.out.println(servicios.servicio3(50,1));
        System.out.println("----------------------------------------");
        System.out.println(servicios.servicio3(19,100));
        System.out.println("----------------------------------------");
        System.out.println(servicios.servicio3(19,10));
        System.out.println("----------------------------------------");
        System.out.println(servicios.servicio3(50,90));


        System.out.println("---------------------------------------- todos:");
        System.out.println(servicios.servicio3(2,100));





    }
}
