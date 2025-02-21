/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pasantias;

import java.io.FileOutputStream;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.util.*;
import java.util.List;

public class ComprobantePagoPDF {

    public static void main(String[] args) {
        String filePath = "comprobante_pago_prueba.pdf";

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font textFont = new Font(Font.HELVETICA, 10);
            Font textHeaderFont = new Font(Font.HELVETICA, 10, Font.BOLD);

            Paragraph header = new Paragraph("Sistema de nomina \n Rol ventas", textHeaderFont);
            header.setAlignment(Element.ALIGN_LEFT);
            document.add(header);
            document.add(new Paragraph("\n"));

            Paragraph title = new Paragraph("COMPROBANTE DE PAGO", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph dateRange = new Paragraph("01/02/2025 - 15/02/2025", textFont);
            dateRange.setAlignment(Element.ALIGN_CENTER);
            document.add(dateRange);
            document.add(new Paragraph("\n"));

            PdfPTable tableInfo = new PdfPTable(4);
            tableInfo.setWidthPercentage(100);

            //float[] columnWidths = {2f, 2f, 4f, 3f}; // El primer número es el ancho de la primera columna, el segundo el de la segunda, etc.
            // Asigna los anchos a la tabla
            //tableInfo.setWidths(columnWidths);
            tableInfo.addCell(getInfoHeaderCell("Periodo: ENERO", textFont, Element.ALIGN_LEFT));
            tableInfo.addCell(getInfoHeaderCell("Año: 2025", textFont, Element.ALIGN_LEFT));
            tableInfo.addCell(getInfoHeaderCell("Empleado: 0924934995 -- Juanito alimania", textFont, Element.ALIGN_LEFT));
            tableInfo.addCell(getInfoHeaderCell("Función: Asesor Comercial", textFont, Element.ALIGN_LEFT));

            document.add(tableInfo);
            document.add(new Paragraph("\n"));

            PdfPTable tableDetail = new PdfPTable(3);
            tableDetail.setWidthPercentage(100);

            tableDetail.addCell(getHeaderCell("Rubros", headerFont));
            tableDetail.addCell(getHeaderCell("Ingresos", headerFont));
            tableDetail.addCell(getHeaderCell("Egresos", headerFont));

            PdfPCell ingresosCell = new PdfPCell();
            ingresosCell.addElement(createIngresosTable(textFont));
            ingresosCell.setBorder(Rectangle.NO_BORDER);

            PdfPCell egresosCell = new PdfPCell();
            egresosCell.addElement(createEgresosTable(textFont));
            egresosCell.setBorder(Rectangle.NO_BORDER);

            PdfPTable mainTable = new PdfPTable(6);
            mainTable.setWidthPercentage(100);

            // Fuente para texto
            // Crear subtablas con datos
            PdfPTable ingresosTable = createIngresosTable(textFont);
            PdfPTable egresosTable = createEgresosTable(textFont);

            // Extraer los datos de las subtablas
            List<List<String>> ingresosData = extractTableData(ingresosTable);
            List<List<String>> egresosData = extractTableData(egresosTable);

            // Obtener la cantidad máxima de filas entre ambas tablas
            int maxRows = Math.max(ingresosData.size(), egresosData.size());

            // Llenar la tabla principal con datos de ambas tablas
            for (int i = 0; i < maxRows; i++) {

                mainTable.addCell(new PdfPCell(new Phrase("", textFont))); // Columna 1 vacía
                mainTable.addCell(new PdfPCell(new Phrase("", textFont))); // Columna 2 vacía

                // Si hay datos en la tabla de ingresos, se agregan; si no, se pone vacío
                if (i < ingresosData.size()) {
                    mainTable.addCell(getCell(ingresosData.get(i).get(0), textFont, Element.ALIGN_RIGHT)); // Columna 4
                    mainTable.addCell(getCell(ingresosData.get(i).get(1), textFont, Element.ALIGN_RIGHT)); // Columna 4

                } else {
                    mainTable.addCell(new PdfPCell(new Phrase("", textFont))); // Columna 3 vacía
                    mainTable.addCell(new PdfPCell(new Phrase("", textFont))); // Columna 4 vacía
                }

                // Si hay datos en la tabla de egresos, se agregan; si no, se pone vacío
                if (i < egresosData.size()) {
                    mainTable.addCell(new PdfPCell(new Phrase(egresosData.get(i).get(0), textFont))); // Columna 5
                    mainTable.addCell(new PdfPCell(new Phrase(egresosData.get(i).get(1), textFont))); // Columna 6
                } else {
                    mainTable.addCell(new PdfPCell(new Phrase("", textFont))); // Columna 5 vacía
                    mainTable.addCell(new PdfPCell(new Phrase("", textFont))); // Columna 6 vacía
                }
            }
            document.add(tableDetail);

            document.add(mainTable);
            PdfPTable tableDetailes = new PdfPTable(3);
            tableDetailes.setWidthPercentage(100);

            tableDetailes.addCell(getCell(" ", textFont, Element.ALIGN_LEFT));
            tableDetailes.addCell(ingresosCell);
            tableDetailes.addCell(egresosCell);

            //ableDetail.addCell(getCell(" ", textFont, Element.ALIGN_LEFT));
            //tableDetail.addCell(ingresosCell);
            //tableDetail.addCell(egresosCell);
            //document.add(tableDetailes);
            document.add(new Paragraph("\n"));

// Calcular totales
            double totalIngresos = 2024.30;
            double totalEgresos = 1514.43;
            double netoAPagar = totalIngresos - totalEgresos;

            PdfPTable tableDetailTotal = new PdfPTable(4);
            tableDetail.setWidthPercentage(100);

            tableDetailTotal.addCell(getCell("TOTAL INGRESOS:", textHeaderFont, Element.ALIGN_LEFT));
            tableDetailTotal.addCell(getCell(String.format("%.2f", totalIngresos), textFont, Element.ALIGN_CENTER));

            tableDetailTotal.addCell(getCell("TOTAL EGRESOS:", textHeaderFont, Element.ALIGN_LEFT));
            tableDetailTotal.addCell(getCell(String.format("%.2f", totalEgresos), textFont, Element.ALIGN_CENTER));

            tableDetailTotal.addCell(getCell("", textFont, Element.ALIGN_LEFT));
            tableDetailTotal.addCell(getCell("", textFont, Element.ALIGN_CENTER));

            tableDetailTotal.addCell(getCell("NETO A PAGAR:", textHeaderFont, Element.ALIGN_LEFT));
            tableDetailTotal.addCell(getCell(String.format("%.2f", netoAPagar), textHeaderFont, Element.ALIGN_CENTER));

            document.add(tableDetailTotal);
            document.add(new Paragraph("\n"));

            PdfPTable firmas = new PdfPTable(3);
            firmas.setWidthPercentage(100);

            firmas.addCell(getCell("_____________________________ \n \n Elaborado por: USUARIO", textFont, Element.ALIGN_CENTER));
            firmas.addCell(getCell("_____________________________ \n \n Supervisado por:", textFont, Element.ALIGN_CENTER));
            firmas.addCell(getCell("_____________________________ \n \n Recibí Conforme: Carmen Torres", textFont, Element.ALIGN_CENTER));

            document.add(firmas);

            document.close();
            System.out.println("PDF generado exitosamente en " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PdfPTable createIngresosTable(Font font) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(150);
        table.addCell(getCell("Total H.Ex. 100%", font, Element.ALIGN_LEFT));
        table.addCell(getCell("123.02", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Sueldo", font, Element.ALIGN_LEFT));
        table.addCell(getCell("797.97", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Ingresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("1,012.15", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Bonos O Ingreso", font, Element.ALIGN_LEFT));
        table.addCell(getCell("13.30", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Fond Res x Per", font, Element.ALIGN_LEFT));
        table.addCell(getCell("77.86", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total H.Ex. 100%", font, Element.ALIGN_LEFT));
        table.addCell(getCell("123.02", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Sueldo", font, Element.ALIGN_LEFT));
        table.addCell(getCell("797.97", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Ingresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("1,012.15", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Bonos O Ingreso", font, Element.ALIGN_LEFT));
        table.addCell(getCell("13.30", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Fond Res x Per", font, Element.ALIGN_LEFT));
        table.addCell(getCell("77.86", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total H.Ex. 100%", font, Element.ALIGN_LEFT));
        table.addCell(getCell("123.02", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Sueldo", font, Element.ALIGN_LEFT));
        table.addCell(getCell("797.97", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Ingresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("1,012.15", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Bonos O Ingreso", font, Element.ALIGN_LEFT));
        table.addCell(getCell("13.30", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Fond Res x Per", font, Element.ALIGN_LEFT));
        table.addCell(getCell("77.86", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total H.Ex. 100%", font, Element.ALIGN_LEFT));
        table.addCell(getCell("123.02", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Sueldo", font, Element.ALIGN_LEFT));
        table.addCell(getCell("797.97", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Ingresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("1,012.15", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Bonos O Ingreso", font, Element.ALIGN_LEFT));
        table.addCell(getCell("13.30", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Fond Res x Per", font, Element.ALIGN_LEFT));
        table.addCell(getCell("77.86", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total H.Ex. 100%", font, Element.ALIGN_LEFT));
        table.addCell(getCell("123.02", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Sueldo", font, Element.ALIGN_LEFT));
        table.addCell(getCell("797.97", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Ingresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("1,012.15", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Bonos O Ingreso", font, Element.ALIGN_LEFT));
        table.addCell(getCell("13.30", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Fond Res x Per", font, Element.ALIGN_LEFT));
        table.addCell(getCell("77.86", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total H.Ex. 100%", font, Element.ALIGN_LEFT));
        table.addCell(getCell("123.02", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Sueldo", font, Element.ALIGN_LEFT));
        table.addCell(getCell("797.97", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Ingresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("1,012.15", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Bonos O Ingreso", font, Element.ALIGN_LEFT));
        table.addCell(getCell("13.30", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Fond Res x Per", font, Element.ALIGN_LEFT));
        table.addCell(getCell("77.86", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total H.Ex. 100%", font, Element.ALIGN_LEFT));
        table.addCell(getCell("123.02", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Sueldo", font, Element.ALIGN_LEFT));
        table.addCell(getCell("797.97", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Ingresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("1,012.15", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Bonos O Ingreso", font, Element.ALIGN_LEFT));
        table.addCell(getCell("13.30", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Fond Res x Per", font, Element.ALIGN_LEFT));
        table.addCell(getCell("77.86", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total H.Ex. 100%", font, Element.ALIGN_LEFT));
        table.addCell(getCell("123.02", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Sueldo", font, Element.ALIGN_LEFT));
        table.addCell(getCell("797.97", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Ingresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("1,012.15", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Bonos O Ingreso", font, Element.ALIGN_LEFT));
        table.addCell(getCell("13.30", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Fond Res x Per", font, Element.ALIGN_LEFT));
        table.addCell(getCell("77.86", font, Element.ALIGN_RIGHT));
        return table;
    }

    private static PdfPTable createEgresosTable(Font font) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(150);
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Aporte Indiv.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("88.29", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Total Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("548.95", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Neto a pagar", font, Element.ALIGN_LEFT));
        table.addCell(getCell("473.20", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Anticipo Quinc.", font, Element.ALIGN_LEFT));
        table.addCell(getCell("398.99", font, Element.ALIGN_RIGHT));
        table.addCell(getCell("Otros Egresos", font, Element.ALIGN_LEFT));
        table.addCell(getCell("15.00", font, Element.ALIGN_RIGHT));

        return table;
    }

    private static PdfPCell getCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell getInfoHeaderCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM); // Mantiene solo los bordes superior e inferior
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
        return data;
    }
}
