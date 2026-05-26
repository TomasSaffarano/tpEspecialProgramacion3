package servicios;

import modelos.Camion;
import modelos.Paquete;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSVReader {

    public static ArrayList<Camion> leerCamiones(String path) {
        ArrayList<Camion> camiones = new ArrayList<>();

        try {
            InputStream is = CSVReader.class.getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int total = Integer.parseInt(br.readLine());

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");

                int id = Integer.parseInt(partes[0]);
                String patente = partes[1];
                boolean refrigerado = partes[2].equals("1");
                double capacidad = Double.parseDouble(partes[3]);

                camiones.add(new Camion(id, patente, refrigerado, capacidad));
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return camiones;
    }

    public static ArrayList<Paquete> leerPaquetes(String path) {
        ArrayList<Paquete> paquetes = new ArrayList<>();

        try {
            InputStream is = CSVReader.class.getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int total = Integer.parseInt(br.readLine());

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");

                int id = Integer.parseInt(partes[0]);
                String codigo = partes[1];
                double peso = Double.parseDouble(partes[2]);
                boolean alimentos = partes[3].equals("1");
                int urgencia = Integer.parseInt(partes[4]);

                paquetes.add(new Paquete(id, codigo, peso, alimentos, urgencia));
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return paquetes;
    }
}
