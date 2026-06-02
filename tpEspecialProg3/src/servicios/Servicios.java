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
     *   SEGUNDA PARTE
     *
     *
     *
     *
Se desea establecer una asignación de todos los paquetes a los camiones
disponibles con el objetivo de minimizar el peso total de los paquetes que no
pudieron ser asignados a ningún camión.
Se sabe que existen ciertas restricciones para asignar un paquete a un camión:
• Primero, ningún camión podrá superar su capacidad máxima de carga. La
capacidad de cada camión está definida en el archivo de entrada.
• Segundo, los paquetes que contienen alimentos sólo podrán ser asignados
a camiones refrigerados.
     * */

    //robo del ejercicio 5 de backtraking
/*
dejo el esqueleto para que lo debatamos por discord
    private int mejorTiempo;
    private int[] mejorAsignacion;

    public int[] asignarTareas(
            int[] tareas,
            int cantidadProcesadores) {

        int[] cargas = new int[cantidadProcesadores];

        int[] asignacionActual = new int[tareas.length];

        mejorTiempo = Integer.MAX_VALUE;

        mejorAsignacion =
                new int[tareas.length];

        asignarProcesos(
                tareas,
                0,
                cargas,
                asignacionActual);

        return mejorAsignacion;
    }


    private void asignarProcesos(int[] tareas, int index, int[] cargas,
                                 int[] asignacionActual) {
        // caso base
        if (index == tareas.length) {

            int tiempoTotal = obtenerMaximo(cargas);

            if (tiempoTotal < mejorTiempo) {

                mejorTiempo = tiempoTotal;

                mejorAsignacion = Arrays.copyOf(asignacionActual,
                                asignacionActual.length);
            }

            return;
        }

        // probar cada procesador
        for (int p = 0; p < cargas.length; p++) {

            // asignar tarea al procesador p
            cargas[p] += tareas[index];

            asignacionActual[index] = p;

            asignarProcesos(
                    tareas,
                    index + 1,
                    cargas,
                    asignacionActual);

            // BACKTRACKING
            cargas[p] -= tareas[index];
        }
    }

    private int obtenerMaximo(int[] cargas) {

        int max = cargas[0];

        for (int c : cargas) {

            if (c > max) {
                max = c;
            }
        }

        return max;
    }*/


/* robo mochila fraccionaria con greedy


    public double[] getCantidadPorObjeto(ObjetoMochila[] objetos, double pesoDisponible){
        if(pesoDisponible<=0){
            return null;
        }

        double[] cantidadObjetos = new double[objetos.length];
        //aca deberia ser un map donde indique nombre objeto y su fraccion

        //primero ordenar los objetos por valorPorKg...

        Arrays.sort(objetos,
                (o1, o2) -> Double.compare(
                        o2.getValor()/o2.getPesoKG(), //valorPorKilogramo
                        o1.getValor()/o1.getPesoKG()
                )); //restriccion para pesos negativos, o evitar en la creacion (constructor objeto mochila)
        //luego...

        for(int i=0;i<cantidadObjetos.length;i++){

            ObjetoMochila objeto = objetos[i];

            if(objeto.getPesoKG()>0){

                if(objeto.getPesoKG() <= pesoDisponible){
                    cantidadObjetos[i]=1.0;
                    pesoDisponible-=objeto.getPesoKG();
                }else {
                    cantidadObjetos[i] =
                            pesoDisponible / objeto.getPesoKG();
                    break;
                }


            }

        }

        return cantidadObjetos;
    }
 */




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
