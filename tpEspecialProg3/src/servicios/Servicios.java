package servicios;

import modelos.Camion;
import modelos.Paquete;

import java.util.*;

public class Servicios {

    private  ArrayList<Camion> camiones;
    private  ArrayList<Paquete> paquetes;

    private static int urgenciaMaxima=100;
    private static int urgenciaMinima=50;
    //variables de clase permite que sea editable solo en codigo (por desarrolladores o podria ser un admin si se
    // habilita un getter y setter) y que sea customizable por cliente...


    public static int getUrgenciaMaxima() {
        return urgenciaMaxima;
    }

    public static void setUrgenciaMaxima(int urgenciaMaxima) {
        Servicios.urgenciaMaxima = urgenciaMaxima;
    }

    public static int getUrgenciaMinima() {
        return urgenciaMinima;
    }

    public static void setUrgenciaMinima(int urgenciaMinima) {
        Servicios.urgenciaMinima = urgenciaMinima;
    }


    /*
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

    public Servicios(String pathCamiones, String pathPaquetes, int urgenciaMinima, int urgenciaMaxima){
        camiones = new ArrayList<>();
        camiones = CSVReader.leerCamiones(pathCamiones);
        paquetes = new ArrayList<>();
        paquetes =  CSVReader.leerPaquetes(pathPaquetes);
        this.urgenciaMinima = urgenciaMinima;
        this.urgenciaMaxima = urgenciaMaxima;
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
     *
     *
     *   //robo del ejercicio 5 de backtraking

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
    *
    *  private void asignarProcesos(int[] tareas, int index, int[] cargas,
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
     }
         * */


/*
*
* Complejidad O()
*
* */

    //devuelve kg restantes
    public double greedy(){

        ArrayList<Camion> camionesGreedy = new ArrayList<>();
        ArrayList<Paquete> paquetesGreedyUrgentes = new ArrayList<>();
        ArrayList<Paquete> paquetesGreedyMenosUrgentes = new ArrayList<>();

        if(paquetes==null) return 00.00;
        else{
            if(urgenciaMinima>urgenciaMaxima){
                int aux = urgenciaMaxima;
                urgenciaMaxima = urgenciaMinima;
                urgenciaMinima = aux;
            }
            paquetesGreedyUrgentes = (ArrayList<Paquete>) this.servicio3(urgenciaMinima,urgenciaMaxima);
            paquetesGreedyMenosUrgentes = (ArrayList<Paquete>) this.servicio3(0,urgenciaMinima);
        }

        if(camiones==null) return 0.0;
        else camionesGreedy = camiones;

        //vamos a ordenar camiones por peso
        camionesGreedy.sort(
                Comparator.comparingDouble(
                        Camion::getCapacidadKg
                ).reversed()
        );

        //paquetes por franja de prioridad, son dos grupos, se ordena cada uno por peso y luego urgencia
        paquetesGreedyUrgentes.sort(
                Comparator
                        .comparingDouble(
                                Paquete::getPesoKg
                        ).reversed()
                        .thenComparing(
                                Comparator.comparingInt(Paquete::getNivelUrgencia)
                                        .reversed()
                        ));

        paquetesGreedyMenosUrgentes.sort(
                Comparator
                        .comparingDouble(
                                Paquete::getPesoKg
                        ).reversed()
                        .thenComparing(
                                Comparator.comparingInt(Paquete::getNivelUrgencia)
                                        .reversed()
                        ));


        ArrayList<Paquete> paquetesGreedyTotal= new ArrayList<>();
        paquetesGreedyTotal.addAll(paquetesGreedyUrgentes);
        paquetesGreedyTotal.addAll(paquetesGreedyMenosUrgentes);
        //el add All mantiene el orden que queremos de urgentes por peso, prioridad - menos urgentes por peso, prioridad

        //primero por cada camion para llenarlos todos...


        for(Camion camion : camionesGreedy){

            double pesoDisponible = camion.getCapacidadKg();
//            List<Paquete> paquetesAlCamion = new ArrayList<>();
            Iterator<Paquete> it = paquetesGreedyTotal.iterator();

            while(it.hasNext()){

                Paquete paq = it.next();

                //si no es refrigerado el camion pero paquete es de alimentos, se saltea...
                if(paq.isContieneAlimentos()
                        && !camion.isRefrigerado()){
                    continue;
                }

                if(paq.getPesoKg() <= pesoDisponible){

                    //paquetesAlCamion.add(paq); no retorno una lista de paquetes por camion, sino el kg sobrante

                    pesoDisponible -= paq.getPesoKg();

                    it.remove();
                    paquetesGreedyTotal.remove(paq);
                    //lo elimino de ambas 
                }

                if(pesoDisponible == 0){
                    break; //paso al proximo camion
                }
            }
        }

        double resto = 0.0;
        if(!paquetesGreedyTotal.isEmpty()){
            for(Paquete paquete : paquetesGreedyTotal){
                resto +=paquete.getPesoKg();
            }
        }
        return resto;
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
