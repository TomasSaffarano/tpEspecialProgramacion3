package servicios;

import modelos.Camion;
import modelos.Paquete;

import java.util.*;

public class Servicios {

    private ArrayList<Camion> camiones;
    private ArrayList<Paquete> paquetes;

    private int estadosGenerados;
    private double mejorPeso;
    private HashMap<Camion, ArrayList<Paquete>> mejorAsignacion;
    //variables para optimizacion de paso de informacion en metodo backtracking

    private int urgenciaMaxima = 100;
    private int urgenciaMinima = 80;
    //variables de clase permite que sea editable solo en codigo (por desarrolladores o podria ser un admin si se
    // habilita un getter y setter) y que sea customizable por cliente...


    /*
     * Expresar la complejidad temporal del constructor: O(c+p)
     * ya que recorre todos los camiones y todos los paquetes, ademas de
     * reocorrer la estructura de archivos para encontrar los csv y parsearlos
     */
    public Servicios(String pathCamiones, String pathPaquetes) {
        camiones = new ArrayList<>();
        camiones = CSVReader.leerCamiones(pathCamiones);
        paquetes = new ArrayList<>();
        paquetes = CSVReader.leerPaquetes(pathPaquetes);
    }

    public Servicios(String pathCamiones, String pathPaquetes, int urgenciaMinima, int urgenciaMaxima) {
        camiones = new ArrayList<>();
        camiones = CSVReader.leerCamiones(pathCamiones);
        paquetes = new ArrayList<>();
        paquetes = CSVReader.leerPaquetes(pathPaquetes);
        this.urgenciaMinima = urgenciaMinima;
        this.urgenciaMaxima = urgenciaMaxima;
    }

    /*
     * Expresar la complejidad temporal del servicio 1: O(p) p: cantidad de paquetes.
     * La complejidad se calcula por tener que recorrer todos los paquetes.
     */
    public Paquete servicio1(String codigoPaquete) {

        /*Servicio 1: Dado un código de paquete (String), retornar toda la información
        del paquete asociado. En caso de no existir, retornar null.*/

        if (paquetes == null) {
            return null;
        }

        if (codigoPaquete == null) {
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
     * Expresar la complejidad temporal del servicio 2: O(p) p: cantidad paquetes
     * recorro una vez el array de paquetes
     */
    public List<Paquete> servicio2(boolean contieneAlimentos) {
        /* Dado un booleano que indica si se buscan paquetes que
         * contienen alimentos (true) o que no contienen alimentos (false), retornar el
         * listado de paquetes correspondiente. */
        if (paquetes == null) {
            return new ArrayList<>();
        }


        List<Paquete> paquetesQueCumplen = new ArrayList<>();
        for (Paquete paquete : paquetes) {
            if (paquete.isContieneAlimentos() == contieneAlimentos) {
                paquetesQueCumplen.add(paquete);
            }
        }
        return paquetesQueCumplen;
    }

    /*
     * Expresar la complejidad temporal del servicio 3: O(p) p: cantidad paquetes
     * recorro una vez el array de paquetes
     */
    public List<Paquete> servicio3(int urgenciaMinima, int
            urgenciaMaxima) {
        /* Dados dos valores enteros que representan un nivel de urgencia
         * mínimo y máximo, retornar todos los paquetes cuyo nivel de urgencia se
         * encuentre dentro de ese rango (inclusive).*/

        if (paquetes == null) {
            return new ArrayList<>();
        }

        if (urgenciaMinima < 0 || urgenciaMaxima < 0) {
            return new ArrayList<>();
        }

        if (urgenciaMinima > urgenciaMaxima) {
            int aux = urgenciaMaxima;
            urgenciaMaxima = urgenciaMinima;
            urgenciaMinima = aux;
            System.out.println("Se han dislexeado los numeros, los di vuelta...");
        }

        List<Paquete> paquetesQueCumplen = new ArrayList<>();
        for (Paquete paquete : paquetes) {
            int urgencia = paquete.getNivelUrgencia();
            if (urgenciaMinima <= urgencia &&
                    urgencia <= urgenciaMaxima) {
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
     * Estrategia Backtracking

El algoritmo explora todas las combinaciones posibles de asignación de paquetes a camiones.
Para cada paquete:
Se prueba asignarlo a cada camión válido
O decidís no asignarlo
Esto genera un árbol de decisiones donde:
Cada nivel = un paquete
Cada rama = decisión (a qué camión o no asignar)
El objetivo es minimizar el peso no asignado, por eso:
Vas acumulando pesoNoAsignadoActual
Te quedas con la mejor solución global (mejorPeso)
     *

*/


    // O(C) C camiones
    public double backtracking() {
        this.estadosGenerados = 0;
        mejorPeso = Double.MAX_VALUE;
        mejorAsignacion = new HashMap<>();

        HashMap<Camion, ArrayList<Paquete>> asignacionActual = new HashMap<>();

        for (Camion c : camiones) {
            asignacionActual.put(c, new ArrayList<>());
        }

        backtracking(
                0,
                asignacionActual,
                0.0);

        System.out.println("Estados generados: " + estadosGenerados);

        return mejorPeso;
    }

    /*Para cada paquete se busca asignar en cada camión o no asignar...
    Cantidad de ramas: (C + 1) & Profundidad: P
    --------> c.c. Árbol (C+1)^P ---> Complejidad sin contar verificaciones: O((C+1)^P),
    se le agrega * P para considerar cada verificación (el bucle se multiplica)
    -> almacenar el peso acual optimiza la c.c a */

    // TOTAL: O(P * (C+1)^P) p paquetes, c camiones
    private void backtracking(
            int indicePaquete,
            HashMap<Camion, ArrayList<Paquete>> asignacionActual,
            double pesoNoAsignadoActual) {

        // CASO BASE
        this.estadosGenerados++;

        if (indicePaquete == paquetes.size()) {

            if (pesoNoAsignadoActual < mejorPeso) {

                mejorPeso = pesoNoAsignadoActual;

                mejorAsignacion = copiarAsignacion(
                        asignacionActual);
            }

            return;
        }

        Paquete paquete =
                paquetes.get(indicePaquete);

        // OPCION 1:
        // Intentar asignarlo a cada camión válido

        for (Camion camion : camiones) {

            if (puedeAsignarse(
                    paquete,
                    camion,
                    asignacionActual)) {

                asignacionActual
                        .get(camion)
                        .add(paquete);

                backtracking(
                        indicePaquete + 1,
                        asignacionActual,
                        pesoNoAsignadoActual);

                // BACKTRACK

                asignacionActual
                        .get(camion)
                        .remove(paquete);
            }
        }

        // OPCION 2:
        // dejar el paquete afuera

        backtracking(
                indicePaquete + 1,
                asignacionActual,
                pesoNoAsignadoActual
                        + paquete.getPesoKg());
    }


    // O(M) M = paquetes cargados en ese camión
    private boolean puedeAsignarse(
            Paquete paquete,
            Camion camion,
            HashMap<Camion, ArrayList<Paquete>> asignacion) {

        if (paquete.isContieneAlimentos()
                && !camion.isRefrigerado()) {

            return false;
        }

        double pesoActual = 0;

        for (Paquete p :
                asignacion.get(camion)) {

            pesoActual += p.getPesoKg();
        }

        return pesoActual +
                paquete.getPesoKg()
                <= camion.getCapacidadKg();
    }



    //O(C + P) C camiones y P paquetes
    private HashMap<Camion, ArrayList<Paquete>>
    copiarAsignacion(
            HashMap<Camion, ArrayList<Paquete>> original) {

        HashMap<Camion, ArrayList<Paquete>> copia =
                new HashMap<>();

        for (Camion c : original.keySet()) {

            copia.put(
                    c,
                    new ArrayList<>(
                            original.get(c)));
        }

        return copia;
    }



/*
*Complejidad computacional del backtracking
* Sea:
n = cantidad de paquetes
m = cantidad de camiones
En el peor caso, cada paquete tiene:
*
m opciones (camiones)
+1 opción (no asignarlo)
 Entonces:
Complejidad del backtracking = O((m+1)^n


*
*
*Estrategia greedy

Nuestro enfoque es:

Ordenar camiones por mayor capacidad
Ordenar paquetes:
Primero los más urgentes
Luego los menos urgentes
Dentro de cada grupo: mayor peso primero
Para cada camión:
Se intentara meter paquetes mientras haya espacio
* */

    //devuelve kg restantes
    public double greedy(){

        ArrayList<Camion> camionesGreedy = new ArrayList<>();
        ArrayList<Paquete> paquetesGreedyUrgentes = new ArrayList<>();
        ArrayList<Paquete> paquetesGreedyMenosUrgentes = new ArrayList<>();

        if(paquetes==null) {
            System.out.println("Andamos sin paquetes.");
            return 00.00;
        }
        else{
            if(urgenciaMinima>urgenciaMaxima){
                int aux = urgenciaMaxima;
                urgenciaMaxima = urgenciaMinima;
                urgenciaMinima = aux;
            }
            paquetesGreedyUrgentes = new ArrayList<Paquete>(this.servicio3(urgenciaMinima,urgenciaMaxima));
            paquetesGreedyMenosUrgentes = new ArrayList<Paquete>(this.servicio3(0,urgenciaMinima));
        }

        if(camiones==null) {
            System.out.println("Andamos sin camiones.");
            double pesoTotal = 0;
            for (Paquete p: paquetesGreedyUrgentes) {
                pesoTotal += p.getPesoKg();
            }
            for (Paquete p: paquetesGreedyMenosUrgentes) {
                pesoTotal += p.getPesoKg();
            }
            return pesoTotal;
        }
        else camionesGreedy = new ArrayList<>(this.camiones);

        //vamos a ordenar camiones por peso
        camionesGreedy.sort(
                Comparator.comparingDouble(
                        Camion::getCapacidadKg
                ).reversed()
        );

        //paquetes por franja de prioridad, son dos grupos, se ordena cada uno por peso y luego urgencia
        paquetesGreedyUrgentes.sort(
                Comparator.comparingDouble(
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
                    //al eliminarlo sobre el iterador lo elimino de ambas
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

  Complejidad computacional del metodo greedy
    Ordenar camiones:
    O(mlogm)
    Ordenar paquetes:
    O(nlogn)
    Asignación:
    Por cada camión → recorrés paquetes
    En peor caso:  O(m*n)

     Total:   O(nlogn+mlogm+m⋅n)
     Si ponemos la complejidad predominante
     O(m*n)

*/

}
