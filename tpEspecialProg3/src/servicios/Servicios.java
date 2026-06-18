package servicios;

import modelos.Camion;
import modelos.Paquete;

import java.util.*;

public class Servicios {

    private ArrayList<Camion> camiones;
    private ArrayList<Paquete> paquetes;



    private HashMap<String,Paquete> paquetePorCodigo;
    //optimizacion servicio 1


    private int estadosGenerados;
    private double mejorPeso;
    private HashMap<Camion, ArrayList<Paquete>> mejorAsignacion;
    private HashMap<Camion,Double> pesoActual;
    //variables para optimizacion de paso de informacion en metodo backtracking

    private int urgenciaMaxima = 100;
    private int urgenciaMinima = 80;
    private HashMap<Camion, ArrayList<Paquete>> asignacionGreedy;
    //variables de clase permite que sea editable solo en codigo (por desarrolladores o podria ser un admin si se
    // habilita un getter y setter) y que sea customizable por cliente...

    /*
     * O(C + P)
     * Leer camiones: O(C)
     * Leer paquetes: O(P)
     * Inicializar HashMap por código: O(P)
     *
     * O(C + P + P) => O(C + P)
     */
    public Servicios(String pathCamiones, String pathPaquetes) {
        camiones = new ArrayList<>();
        camiones = CSVReader.leerCamiones(pathCamiones);
        paquetes = new ArrayList<>();
        paquetes = CSVReader.leerPaquetes(pathPaquetes);
        this.iniciarPaquetePorCodigo();
    }


    /*
     * O(C + P)
     * Leer camiones: O(C)
     * Leer paquetes: O(P)
     * Inicializar HashMap por código: O(P)
     *
     * O(C + P + P) => O(C + P)
     */
    public Servicios(String pathCamiones, String pathPaquetes, int urgenciaMinima, int urgenciaMaxima) {
        camiones = new ArrayList<>();
        camiones = CSVReader.leerCamiones(pathCamiones);
        paquetes = new ArrayList<>();
        paquetes = CSVReader.leerPaquetes(pathPaquetes);
        this.urgenciaMinima = urgenciaMinima;
        this.urgenciaMaxima = urgenciaMaxima;
        this.iniciarPaquetePorCodigo();
    }

    private void iniciarPaquetePorCodigo(){
        paquetePorCodigo = new HashMap<>();
        if(this.paquetes!=null){
            for(Paquete paquete:paquetes){
                paquetePorCodigo.put(paquete.getCodigo(),paquete);
            }
        }
    }

    /*
     * Expresar la complejidad temporal del servicio 1:
     * O(1)
     * La complejidad se optimiza por guardar el map paquetes por codigo.
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
        return paquetePorCodigo.get(codigoPaquete);
    }

    /*
     * complejidad temporal O(p) p: cantidad paquetes
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
     *
     */

    //O((C+1)^P)
    // porque es O(P + private backtracking), gana el del recursivo
    public double backtracking() {
        this.estadosGenerados = 0;
        mejorPeso = Double.MAX_VALUE;
        mejorAsignacion = new HashMap<>();

        HashMap<Camion, ArrayList<Paquete>> asignacionActual = new HashMap<>();

        for (Camion c : camiones) {
            asignacionActual.put(c, new ArrayList<>());
        }

        pesoActual = new HashMap<>();

        for (Camion c : camiones) {

            asignacionActual.put(c, new ArrayList<>());

            pesoActual.put(c, 0.0);
        }

        backtracking(
                0,
                asignacionActual,
                0.0);

        this.mostrarMejorAsignacion();
        System.out.println("Estados generados: " + estadosGenerados);

        return mejorPeso;
    }


    /*Para cada paquete se busca asignar en cada camión o no asignar...
    Cantidad de ramas: (C + 1) & Profundidad: P
    --------> c.c. Árbol (C+1)^P ---> Complejidad sin contar verificaciones: O((C+1)^P)
    verificacion actual O(1)
    -> almacenar el peso acual optimiza la c.c a */

    // TOTAL: O((C+1)^P) p paquetes, c camiones
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

                double pesoCamionPrevio = pesoActual.get(camion);

                pesoActual.put(camion, pesoCamionPrevio+ paquete.getPesoKg());

                backtracking(
                        indicePaquete + 1,
                        asignacionActual,
                        pesoNoAsignadoActual);

                // BACKTRACK

                asignacionActual
                        .get(camion)
                        .remove(paquete);

                pesoActual.put(camion, pesoCamionPrevio);
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


    // O(1) optimizacion por map peso actual
    private boolean puedeAsignarse(
            Paquete paquete,
            Camion camion,
            HashMap<Camion, ArrayList<Paquete>> asignacion) {

        if (paquete.isContieneAlimentos()
                && !camion.isRefrigerado()) {

            return false;
        }

        return pesoActual.get(camion) +
                    paquete.getPesoKg()
                    <= camion.getCapacidadKg();

    }



    //O(C + P) C camiones y P paquetes
    private HashMap<Camion, ArrayList<Paquete>> copiarAsignacion(
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
p = cantidad de paquetes
c = cantidad de camiones
En el peor caso, cada paquete tiene:
*
* Servicio 3. llamado dos veces O(P) p paquetes
* sort camiones: O(C log C) c camiones
* sort urgentes: O(U log U) + sort menos urgentes: O(M log M): O(P log P) p paquetes
* for y while de greedy: O(C * P)
* calcular resto: O(P)
* total: O(P log P + C log C + C*P)
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

    public double greedy(){

        ArrayList<Camion> camionesGreedy = new ArrayList<>();
        ArrayList<Paquete> paquetesGreedyUrgentes = new ArrayList<>();
        ArrayList<Paquete> paquetesGreedyMenosUrgentes = new ArrayList<>();
        estadosGenerados=0;

        asignacionGreedy = new HashMap<>();

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


        for(Camion camion : camionesGreedy){
            asignacionGreedy.put(camion, new ArrayList<>());
        }

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
            Iterator<Paquete> it = paquetesGreedyTotal.iterator();

            while(it.hasNext()){

                estadosGenerados++;

                Paquete paq = it.next();

                //si no es refrigerado el camion pero paquete es de alimentos, se saltea...
                if(paq.isContieneAlimentos()
                        && !camion.isRefrigerado()){
                    continue;
                }

                if(paq.getPesoKg() <= pesoDisponible){

                    asignacionGreedy.get(camion).add(paq);

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
        this.mostrarAsignacionGreedy();
        System.out.println(
                "Cantidad de candidatos considerados: "
                        + estadosGenerados
        );
        return resto;
    }


/*

  Complejidad computacional del metodo greedy
    Ordenar camiones:
    O(c log c)
    Ordenar paquetes:
    O(p log p)
    Asignación:
    Por cada camión → recorrés paquetes
    En peor caso:  O(c*p)

     Total:   O(c log c+ plog p+ c*p)

*/


    public void mostrarAsignacionGreedy() {

        System.out.println("===== SOLUCION OBTENIDA GREEDY =====");

        for (Camion camion : asignacionGreedy.keySet()) {

            System.out.println("Camion " + camion.getPatente());

            for (Paquete paquete : asignacionGreedy.get(camion)) {

                System.out.println(
                        "   - " + paquete.getCodigo()
                );
            }

            System.out.println();
        }
    }


    public void mostrarMejorAsignacion() {

        System.out.println("===== SOLUCION OBTENIDA BACKTRACKING =====");

        for (Camion camion : mejorAsignacion.keySet()) {

            System.out.println(
                    "Camion " + camion.getPatente()
                            + " (" + camion.getCapacidadKg() + " kg)"
            );

            double pesoCamion = 0;

            for (Paquete paquete : mejorAsignacion.get(camion)) {

                System.out.println(
                        "   - " + paquete.getCodigo()
                                + " (" + paquete.getPesoKg() + " kg)"
                );

                pesoCamion += paquete.getPesoKg();
            }

            System.out.println(
                    "   Peso cargado: "
                            + pesoCamion
                            + " kg"
            );

            System.out.println();
        }

        System.out.println(
                "Peso no asignado: "
                        + mejorPeso
                        + " kg"
        );
    }
}
