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
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class GeneratePDF {

    public static void generate(RolGeneral rol, String fileName) throws Exception {

        String mesComoString = rol.getPeriodoMes(); // obtener el numero del mes 
        int numeroMes = Integer.parseInt(mesComoString); // Convertir a entero

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');

        // Definir el patrón de formato
        String patron = "#,###.00";

        // Crear un objeto DecimalFormat con el patrón y los símbolos personalizados
        DecimalFormat formato = new DecimalFormat(patron, simbolos);

        String nombreMes = Month.of(numeroMes).getDisplayName(TextStyle.FULL, new Locale("es")).toUpperCase();;

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font textFont1 = new Font(Font.HELVETICA, 9, Font.BOLD);
        Font textFont = new Font(Font.HELVETICA, 8);
        Font textHeaderFont = new Font(Font.HELVETICA, 10, Font.BOLD);

        Paragraph header = new Paragraph("Sistema de nomina \n Rol ventas", textHeaderFont);
        header.setAlignment(Element.ALIGN_LEFT);
        document.add(header);
        document.add(new Paragraph("\n"));

        Paragraph title = new Paragraph("COMPROBANTE DE PAGO", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph dateRange = new Paragraph("01/" + rol.getPeriodoMes() + "/" + rol.getPeriodoAnio() + " - " + rol.getFechaCorte(), textFont);
        dateRange.setAlignment(Element.ALIGN_CENTER);
        document.add(dateRange);
        document.add(new Paragraph("\n"));

        PdfPTable tableInfo = new PdfPTable(1);
        tableInfo.setWidthPercentage(100);

        Paragraph infoParagraph = new Paragraph();
        infoParagraph.setAlignment(Element.ALIGN_CENTER);
        infoParagraph.add(new Chunk("Periodo: ", textFont1));
        infoParagraph.add(new Chunk(nombreMes + "                   ", textFont));

        infoParagraph.add(new Chunk("Año: ", textFont1));
        infoParagraph.add(new Chunk(rol.getPeriodoAnio() + "                    ", textFont));

        infoParagraph.add(new Chunk("Empleado: ", textFont1));
        infoParagraph.add(new Chunk(rol.getEmpCodigo() + " - " + rol.getEmpNombres() + " " + rol.getEmpApellidos() + "                 ", textFont));

        infoParagraph.add(new Chunk("Función: ", textFont1));
        infoParagraph.add(new Chunk(rol.getEmpFuncion(), textFont));

        // Agregar todo en una sola celda
        tableInfo.addCell(getInfoHeaderCell(infoParagraph, Element.ALIGN_CENTER));

        document.add(tableInfo);
        document.add(new Paragraph("\n"));

        PdfPTable tableDetail = new PdfPTable(3);
        tableDetail.setWidthPercentage(100);

        tableDetail.addCell(getHeaderCell("Rubros", headerFont));
        tableDetail.addCell(getHeaderCell("Ingresos", headerFont));
        tableDetail.addCell(getHeaderCell("Egresos", headerFont));

        //organizar los datos en la tabla principal
        PdfPTable mainTable = new PdfPTable(6);
        mainTable.setWidthPercentage(100);

        // Fuente para texto
        // Crear subtablas con datos
        PdfPTable ingresosTable = createIngresosTable(rol.getIngresos(), textFont);

        PdfPTable egresosTable = createEgresosTable(rol.getEgresos(), textFont);

        // Extraer los datos de las subtablas
        List<List<String>> ingresosData = extractTableData(ingresosTable);
        List<List<String>> egresosData = extractTableData(egresosTable);

        // Obtener la cantidad máxima de filas entre ambas tablas
        int maxRows = Math.max(ingresosData.size(), egresosData.size());

        // Llenar la tabla principal con datos de ambas tablas
        for (int i = 0; i < maxRows; i++) {

            mainTable.addCell(getCell("", textFont, Element.ALIGN_LEFT)); // Columna 5
            mainTable.addCell(getCell("", textFont, Element.ALIGN_CENTER)); // columna 6

            // Si hay datos en la tabla de ingresos, se agregan; si no, se pone vacío
            if (i < ingresosData.size()) {
                mainTable.addCell(getCell(ingresosData.get(i).get(0), textFont, Element.ALIGN_LEFT)); // Columna 4
                mainTable.addCell(getCellNum(ingresosData.get(i).get(1), Element.ALIGN_CENTER)); //Columna 4

            } else {
                mainTable.addCell(getCell("", textFont, Element.ALIGN_LEFT)); // Columna 4 vacia
                mainTable.addCell(getCell("", textFont, Element.ALIGN_CENTER)); // Columna 5 vacía
            }

            // Si hay datos en la tabla de egresos, se agregan; si no, se pone vacío
            if (i < egresosData.size()) {
                mainTable.addCell(getCell(ingresosData.get(i).get(0), textFont, Element.ALIGN_LEFT)); // Columna 5
                mainTable.addCell(getCellNum(ingresosData.get(i).get(1), Element.ALIGN_CENTER)); //columna 6
            } else {
                mainTable.addCell(getCell("", textFont, Element.ALIGN_LEFT)); // Columna 5
                mainTable.addCell(getCell("", textFont, Element.ALIGN_CENTER)); // columna 6
            }
        }

        document.add(tableDetail);
        document.add(mainTable);

        document.add(new Paragraph("\n"));

// Calcular totales
        PdfPTable tableDetailTotal = new PdfPTable(4);
        tableDetail.setWidthPercentage(100);

        tableDetailTotal.addCell(getCell("TOTAL INGRESOS:", textHeaderFont, Element.ALIGN_LEFT));
        tableDetailTotal.addCell(getCell(formato.format(totalIngresos), textFont, Element.ALIGN_CENTER));

        tableDetailTotal.addCell(getCell("TOTAL EGRESOS:", textHeaderFont, Element.ALIGN_LEFT));
        tableDetailTotal.addCell(getCell(formato.format(totalEgresos), textFont, Element.ALIGN_CENTER));

        tableDetailTotal.addCell(getCell("", textFont, Element.ALIGN_LEFT));
        tableDetailTotal.addCell(getCell("", textFont, Element.ALIGN_CENTER));

        tableDetailTotal.addCell(getCell("NETO A PAGAR:", textHeaderFont, Element.ALIGN_LEFT));
        tableDetailTotal.addCell(getCell(formato.format(netoAPagar), textHeaderFont, Element.ALIGN_CENTER));

        document.add(tableDetailTotal);
        document.add(new Paragraph("\n"));

        PdfPTable firmas = new PdfPTable(3);
        firmas.setWidthPercentage(100);

        firmas.addCell(getCell("_____________________________ \n \n Elaborado por: USUARIO", textFont, Element.ALIGN_CENTER));
        firmas.addCell(getCell("_____________________________ \n \n Supervisado por:", textFont, Element.ALIGN_CENTER));
        firmas.addCell(getCell("_____________________________ \n \n Recibí Conforme: " + rol.getEmpNombres() + rol.getEmpApellidos(), textFont, Element.ALIGN_CENTER));

        document.add(firmas);

        document.close();

    }

    private static double totalIngresos = 0; // Ahora es estática
    private static double totalEgresos = 0;
    private static double netoAPagar = 0;

    private static PdfPTable createIngresosTable(List<DetalleRol> ingresos, Font font) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        for (DetalleRol ingreso : ingresos) {
            table.addCell(getCell(ingreso.getNombre(), font, Element.ALIGN_LEFT));
            table.addCell(getCell(String.valueOf(ingreso.getValor()), font, Element.ALIGN_RIGHT)); // Conversión segura
            totalIngresos += ingreso.getValor(); // Acumulando el valor en cada iteración

        }
        return table;
    }

    private static PdfPTable createEgresosTable(List<DetalleRol> egresos, Font font) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        for (DetalleRol egreso : egresos) {
            table.addCell(getCell(egreso.getNombre(), font, Element.ALIGN_LEFT));
            table.addCell(getCell(String.valueOf(egreso.getValor()), font, Element.ALIGN_RIGHT)); // Conversión segura
            totalEgresos += egreso.getValor(); // Acumulando el valor en cada iteración

        }
        return table;
    }

    private static PdfPCell getCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell getCellNum(String numero, int alignment) {
        // Definir los símbolos para el formato numérico
        Font textFont = new Font(Font.HELVETICA, 8);
        double num = Double.parseDouble(numero);

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');

        // Definir el patrón de formato
        String patron = "#,###.00";

        // Crear un objeto DecimalFormat con el patrón y los símbolos personalizados
        DecimalFormat formato = new DecimalFormat(patron, simbolos);

        // Formatear el número
        String numeroFormateado = formato.format(num);

        // Crear la celda con el número formateado y la fuente especificada
        PdfPCell cell = new PdfPCell(new Phrase(numeroFormateado, textFont));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);

        return cell;
    }

    private static PdfPCell getInfoHeaderCell(Paragraph paragraph, int alignment) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(paragraph);
        cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM); // Mantiene solo los bordes superior e inferior
        cell.setPaddingBottom(10);

        return cell;
    }

    private static PdfPCell getHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static List<List<String>> extractTableData(PdfPTable table) {
        List<List<String>> data = new ArrayList<>();

        // Obtener todas las filas de la tabla
        List<?> rows = table.getRows(); // getRows() devuelve List<?> (lista de objetos)

        for (Object rowObj : rows) {
            if (rowObj instanceof PdfPRow) { // Asegurar que el objeto es un PdfPRow
                PdfPRow row = (PdfPRow) rowObj; // Convertir Object a PdfPRow
                List<String> rowData = new ArrayList<>();
                PdfPCell[] cells = row.getCells(); // Obtener las celdas de la fila

                for (PdfPCell cell : cells) {
                    if (cell != null && cell.getPhrase() != null) {
                        rowData.add(cell.getPhrase().getContent()); // Obtener el texto
                    } else {
                        rowData.add(""); // Si la celda está vacía, agregar un string vacío
                    }
                }

                data.add(rowData);
            }
        }
        netoAPagar = totalIngresos - totalEgresos;
        return data;
    }
}
