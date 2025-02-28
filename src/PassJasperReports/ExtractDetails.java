/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PassJasperReports;

/**
 *
 * @author ventas 1
 */


import java.sql.*;
import java.util.*;

public class ExtractDetails {

    public static Map<String, Object> getRolByEmpleado(Connection connection, String empCodigo) throws SQLException {
        // Map para almacenar los resultados
        Map<String, Object> result = new HashMap<>();
        
        // Arreglos para almacenar los datos de ingresos y egresos
        List<String[]> ingresos = new ArrayList<>();
        List<String[]> egresos = new ArrayList<>();

        // Consulta para obtener los datos de la cabecera (rolgeneral_cab)
        String queryEmpleado = "SELECT * FROM rolgeneral_cab WHERE EMP_CODIGO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(queryEmpleado)) {
            stmt.setString(1, empCodigo);
            ResultSet rsEmpleado = stmt.executeQuery();

            if (rsEmpleado.next()) {
                // Obtener los datos de la cabecera
                String periodoAnio = rsEmpleado.getString("Periodo_anio");
                int periodoMes = rsEmpleado.getInt("Periodo_Mes");
                String fechaCorte = rsEmpleado.getString("Fecha_corte");
                String codigo = rsEmpleado.getString("EMP_CODIGO");
                String nombres = rsEmpleado.getString("EMP_NOMBRES");
                String apellidos = rsEmpleado.getString("EMP_APELLIDOS");
                String funcion = rsEmpleado.getString("EMP_FUNCION");

                // Guardar los datos de la cabecera en el map
                result.put("periodoAnio", periodoAnio);
                result.put("periodoMes", periodoMes);
                result.put("fechaCorte", fechaCorte);
                result.put("codigo", codigo);
                result.put("nombres", nombres);
                result.put("apellidos", apellidos);
                result.put("funcion", funcion);

                // Consulta para obtener los detalles (rolgeneral_det)
                String queryDetalles = "SELECT * FROM rolgeneral_det WHERE EMP_CODIGO = ?";

                try (PreparedStatement stmtDetalles = connection.prepareStatement(queryDetalles)) {
                    stmtDetalles.setString(1, codigo);
                    ResultSet rsDetalles = stmtDetalles.executeQuery();

                    // Procesar cada detalle de ingreso y egreso
                    while (rsDetalles.next()) {
                        String tipo = rsDetalles.getString("TIPO");
                        String nombre = rsDetalles.getString("NOMBRE");
                        double valor = rsDetalles.getDouble("VALOR");

                        // Guardar los detalles en los arreglos correspondientes
                        String[] detalle = {nombre, tipo, String.valueOf(valor)};
                        if ("I".equals(tipo)) {
                            ingresos.add(detalle); // Ingreso
                        } else if ("E".equals(tipo)) {
                            egresos.add(detalle); // Egreso
                        }
                    }
                }
            }
        }

        // Guardar los arreglos de ingresos y egresos en el map
        result.put("ingresos", ingresos);
        result.put("egresos", egresos);

        return result; // Devolver el map con todos los datos
    }
}
