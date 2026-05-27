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
     * Expresar la complejidad temporal del constructor: O(c+p)
     * ya que recorre todos los camiones y todos los paquetes, ademas de 
     * reocorrer la estructura de archivos para encontrar los csv y parsearlos
     */
    public Servicios(String pathCamiones, String pathPaquetes){
        camiones = new ArrayList<>();
        camiones = CSVReader.leerCamiones(pathCamiones);
        paquetes = new ArrayList<>();
        paquetes =  CSVReader.leerPaquetes(pathPaquetes);
    }

    /*
     * Expresar la complejidad temporal del servicio 1: O(n) n: cantidad de paquetes.
     * La complejidad se calcula por tener que recorrer todos los paquetes. 
     */
    public Paquete servicio1(String codigoPaquete) {

        /*Servicio 1: Dado un código de paquete (String), retornar toda la información
        del paquete asociado. En caso de no existir, retornar null.*/

        if(paquetes==null){
            return null;
        }

        if(codigoPaquete==null){
            return null;
        }

        for (Paquete paquete : paquetes) {
            if (paquete.getCodigo().equals(codigoPaquete)) {
                //retorna la primera vez que coincide, se eliminan segundas coincidencias
                return paquete;
            }
        }

        return null;
    }
    /*
     * Expresar la complejidad temporal del servicio 2: O(n) n: cantidad paquetes
     * recorro una vez el array de paquetes
     */
    public List<Paquete> servicio2(boolean contieneAlimentos) {
        /* Dado un booleano que indica si se buscan paquetes que
        * contienen alimentos (true) o que no contienen alimentos (false), retornar el
        * listado de paquetes correspondiente. */
        if(paquetes==null){
            return new ArrayList<>();
        }


        List<Paquete> paquetesQueCumplen = new ArrayList<>();
        for(Paquete paquete : paquetes){
            if(paquete.isContieneAlimentos()==contieneAlimentos){
                paquetesQueCumplen.add(paquete);
            }
        }
        return paquetesQueCumplen;
    }
    /*
     * Expresar la complejidad temporal del servicio 3: O(n) n: cantidad paquetes
     * recorro una vez el array de paquetes
     */
    public List<Paquete> servicio3(int urgenciaMinima, int
            urgenciaMaxima) {
        /* Dados dos valores enteros que representan un nivel de urgencia
        * mínimo y máximo, retornar todos los paquetes cuyo nivel de urgencia se
        * encuentre dentro de ese rango (inclusive).*/

        if(paquetes==null){
            return new ArrayList<>();
        }

        if(urgenciaMinima<0 || urgenciaMaxima<0){
            return new ArrayList<>();
        }

        if(urgenciaMinima>urgenciaMaxima){
            int aux = urgenciaMaxima;
            urgenciaMaxima = urgenciaMinima;
            urgenciaMinima = aux;
            System.out.println("Se han dislexeado los numeros, los di vuelta...");
        }

        List<Paquete> paquetesQueCumplen = new ArrayList<>();
        for(Paquete paquete : paquetes){
            int urgencia = paquete.getNivelUrgencia();
            if(urgenciaMinima <= urgencia &&
                    urgencia <= urgenciaMaxima){
                paquetesQueCumplen.add(paquete);
            }
        }
        return paquetesQueCumplen;
    }



/*
*
*
*   METODOS INTERNOS AUXILIARES
* 
*
*. queda comentado por no volver a ser utilizada la logica por ahora. veremos segunda parte
* */
/*

    //COMPLEJIDAD O(n) n: cantidad paquetes
    private Paquete getPaquetePorCodigo(String codigoPaquete) {
        for (Paquete paquete : paquetes) {
            if (paquete.getCodigo().equals(codigoPaquete)) {
                return paquete;
            }
        }
        return null;
    }

*/

}
