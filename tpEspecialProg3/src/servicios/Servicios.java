package servicios;

import modelos.Camion;
import modelos.Paquete;

import java.util.ArrayList;
import java.util.List;

public class Servicios {

    private  ArrayList<Camion> camiones;
    private  ArrayList<Paquete> paquetes;

    /*Completar con las estructuras y métodos privados que se
   * requieran
    *
     * Expresar la complejidad temporal del constructor.
     */
    public Servicios(String pathCamiones, String pathPaquetes){
        camiones = new ArrayList<>();
        camiones = CSVReader.leerCamiones(pathCamiones);
        paquetes = new ArrayList<>();
        paquetes =  CSVReader.leerPaquetes(pathPaquetes);
    }

    /*
     * Expresar la complejidad temporal del servicio 1: O(n) n: cantidad de paquetes.
     * La complejidad se calcula por tener una llamada a un metodo
     * cuya complejidad es O(n)
     */
    public Paquete servicio1(String codigoPaquete) {

        /*Servicio 1: Dado un código de paquete (String), retornar toda la información
        del paquete asociado. En caso de no existir, retornar null.*/
        if(codigoPaquete==null){
            return null;
        }
        if(paquetes!=null){
            Paquete paquete = this.getPaquetePorCodigo(codigoPaquete);
            if(paquete != null){
                return paquete;
            }
        }
        return null;
    }
    /*
     * Expresar la complejidad temporal del servicio 2.
     */
    public List<Paquete> servicio2(boolean contieneAlimentos) {
        //TO DO
        return null;
    }
    /*
     * Expresar la complejidad temporal del servicio 3.
     */
    public List<Paquete> servicio3(int urgenciaMinima, int
            urgenciaMaxima) {
        //TO DO
        return null;
    }



/*
*
*
*   METODOS INTERNOS AUXILIARES
*
*
*
* */


    //COMPLEJIDAD O(n) n: cantidad paquetes
    public Paquete getPaquetePorCodigo(String codigoPaquete) {
        for (Paquete paquete : paquetes) {
            if (paquete.getCodigo().equals(codigoPaquete)) {
                return paquete;
            }
        }
        return null;
    }
}
