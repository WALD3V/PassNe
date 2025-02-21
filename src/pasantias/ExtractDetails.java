/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pasantias;

/**
 *
 * @author PC
 */
import java.sql.*;
import java.util.*;

public class ExtractDetails {

    public static RolGeneral getRolByEmpleado(Connection connection, String empCodigo) throws SQLException {
        List<DetalleRol> ingresos = new ArrayList<>();
        List<DetalleRol> egresos = new ArrayList<>();
        RolGeneral rol = null;

        String queryEmpleado = "SELECT * FROM rolgeneral_cab WHERE EMP_CODIGO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(queryEmpleado)) {
            stmt.setString(1, empCodigo);
            ResultSet rsEmpleado = stmt.executeQuery();

            if (rsEmpleado.next()) {
                String periodoAnio = rsEmpleado.getString("Periodo_anio");
                String periodoMes = rsEmpleado.getString("Periodo_Mes");
                String fechaCorte = rsEmpleado.getString("Fecha_corte");
                String codigo = rsEmpleado.getString("EMP_CODIGO");
                String nombres = rsEmpleado.getString("EMP_NOMBRES");
                String apellidos = rsEmpleado.getString("EMP_APELLIDOS");
                String funcion = rsEmpleado.getString("EMP_FUNCION");

                // Consulta para obtener detalles de ingresos y egresos
                String queryDetalles = "SELECT * FROM rolgeneral_det WHERE EMP_CODIGO = ?";
                try (PreparedStatement stmtDetalles = connection.prepareStatement(queryDetalles)) {
                    stmtDetalles.setString(1, codigo);
                    ResultSet rsDetalles = stmtDetalles.executeQuery();

                    while (rsDetalles.next()) {
                        String tipo = rsDetalles.getString("TIPO");
                        String nombre = rsDetalles.getString("NOMBRE");
                        double valor = rsDetalles.getDouble("VALOR");

                        DetalleRol detalle = new DetalleRol(codigo, nombre, tipo, "Referencia", valor, 0.0);

                        if ("I".equals(tipo)) {
                            ingresos.add(detalle);
                        } else if ("E".equals(tipo)) {
                            egresos.add(detalle);
                        }
                    }
                }

                // Crear el objeto RolGeneral con todos los detalles
                rol = new RolGeneral(periodoAnio, periodoMes, fechaCorte, codigo, nombres, apellidos, funcion, ingresos, egresos);
            }
        }
        return rol; // Se devuelve el rol procesado o null si no se encontró el empleado
    }

}
