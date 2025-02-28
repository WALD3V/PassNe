/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pasantias;

/**
 * Clase para generar un archivo PDF con los detalles del rol de un empleado.
 * @author PC
 */
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class GeneratePDF {

    /**
     * Método para generar un archivo PDF con los detalles del rol de un empleado.
     * 
     * @param rol Objeto RolGeneral con los detalles del rol del empleado.
     * @param fileName Nombre del archivo PDF a generar.
     */
    public static void generate(RolGeneral rol, String fileName) throws Exception {

        // Reiniciar las variables globales para cada empleado
        totalIngresos = 0;
        totalEgresos = 0;
        totalEgmenosDesc = 0;
        descuentos = 0;
        netoAPagar = 0;

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');

        // Definir el patrón de formato de números
        String patron = "#,###.00";

        // Crear un objeto DecimalFormat con el patrón y los símbolos personalizados
        DecimalFormat formato = new DecimalFormat(patron, simbolos);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font textFont1 = new Font(Font.HELVETICA, 10, Font.BOLD);
        Font textFont2 = new Font(Font.HELVETICA, 9, Font.BOLD);
        Font textFont3 = new Font(Font.HELVETICA, 9);
        Font textFont = new Font(Font.HELVETICA, 10);
        Font textHeaderFont = new Font(Font.HELVETICA, 10, Font.BOLD);

        Image logo = Image.getInstance("src/pasantias/Sample.png");
        logo.scaleToFit(100, 50); // Ajustar el tamaño de la imagen
        logo.setAlignment(Element.ALIGN_RIGHT);

        Paragraph header = new Paragraph("Sistema de nomina \nRol ventas", textHeaderFont);
        header.setAlignment(Element.ALIGN_LEFT);

        // Crear una tabla con dos celdas para el encabezado
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        //headerTable.setWidths(new int[]{4, 1}); // Ajustar el ancho de las columnas

        // Agregar el texto a la primera celda
        PdfPCell textCell = new PdfPCell(header);
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        headerTable.addCell(textCell);

        // Agregar la imagen a la segunda celda
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerTable.addCell(logoCell);

        // Agregar la tabla al documento
        document.add(headerTable);
        document.add(new Paragraph("\n"));


        // Crear el título y la fecha del comprobante

        Paragraph title = new Paragraph("COMPROBANTE DE PAGO", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph dateRange = new Paragraph(
                "01/" + rol.getPeriodoMes() + "/" + rol.getPeriodoAnio() + " - " + rol.getFechaCorte(), textFont);
        dateRange.setAlignment(Element.ALIGN_CENTER);
        document.add(dateRange);
        document.add(new Paragraph("\n"));

        PdfPTable tableInfo = new PdfPTable(1);
        tableInfo.setWidthPercentage(100);

        Paragraph infoParagraph = new Paragraph();
        infoParagraph.setAlignment(Element.ALIGN_CENTER);
        infoParagraph.add(new Chunk("Periodo: ", textFont1));
        infoParagraph.add(new Chunk(rol.getNombreMes() + "      ", textFont));

        infoParagraph.add(new Chunk("Año: ", textFont1));
        infoParagraph.add(new Chunk(rol.getPeriodoAnio() + "        ", textFont));

        infoParagraph.add(new Chunk("Empleado: ", textFont1));
        infoParagraph.add(new Chunk(
                rol.getEmpCodigo() + " - " + rol.getEmpNombres() + " " + rol.getEmpApellidos() + "      ",
                textFont));

        infoParagraph.add(new Chunk("Función: ", textFont1));
        infoParagraph.add(new Chunk(rol.getEmpFuncion(), textFont));

        // Agregar todo el detalle en una sola celda
        tableInfo.addCell(getInfoHeaderCell(infoParagraph, Element.ALIGN_CENTER));

        document.add(tableInfo);
        document.add(new Paragraph("\n"));


        // Crear la tabla de detalle
        PdfPTable tableDetail = new PdfPTable(3);
        tableDetail.setWidthPercentage(100);

        tableDetail.addCell(getHeaderCell("Rubros", headerFont));
        tableDetail.addCell(getHeaderCell("Ingresos", headerFont));
        tableDetail.addCell(getHeaderCell("Egresos", headerFont));

        document.add(tableDetail);

        // organizar los datos en la tabla principal
        PdfPTable mainTable = new PdfPTable(6);
        mainTable.setWidthPercentage(100);

        PdfPTable ingresosEgresosTable = createIngresosEgresosTable(rol.getIngresos(), rol.getEgresos(), textFont);

        // Agregar la tabla de ingresos y egresos al documento
        document.add(ingresosEgresosTable);

        document.add(new Paragraph("\n"));

        //agregamos la tabla de desglose de haberes y descuentos
        // Crear y agregar la tabla de descuentos
        PdfPTable descuentosTable = createDescuentosTable(rol.getDescuentos(), textFont1, textFont2, textFont3);
        document.add(descuentosTable);
        document.add(new Paragraph("\n\n"));

        //actualizamos las variables que muestran los totales
        // Calcular totales
        PdfPTable tableDetailTotal = new PdfPTable(4);
        tableDetail.setWidthPercentage(100);

        tableDetailTotal.addCell(getCell("TOTAL INGRESOS:", textHeaderFont, Element.ALIGN_LEFT));
        tableDetailTotal.addCell(getCell(formato.format(totalIngresos), textFont, Element.ALIGN_CENTER));

        tableDetailTotal.addCell(getCell("TOTAL EGRESOS:", textHeaderFont, Element.ALIGN_LEFT));
        tableDetailTotal.addCell(getCell(formato.format(totalEgmenosDesc), textFont, Element.ALIGN_CENTER));

        tableDetailTotal.addCell(getCell("", textFont, Element.ALIGN_LEFT));
        tableDetailTotal.addCell(getCell("", textFont, Element.ALIGN_CENTER));

        tableDetailTotal.addCell(getCell("NETO A PAGAR:", textHeaderFont, Element.ALIGN_LEFT));
        tableDetailTotal.addCell(getCell(formato.format(netoAPagar), textHeaderFont, Element.ALIGN_CENTER));

        document.add(tableDetailTotal);
        document.add(new Paragraph("\n\n"));

        PdfPTable firmas = new PdfPTable(3);
        firmas.setWidthPercentage(100);

        firmas.addCell(getCell("_____________________________ \n \n Elaborado por: \n USUARIO", textFont,
                Element.ALIGN_CENTER));
        firmas.addCell(
                getCell("_____________________________ \n \n Supervisado por: \n ", textFont, Element.ALIGN_CENTER));
        firmas.addCell(getCell("_____________________________ \n \n Recibí Conforme: \n " + rol.getEmpNombres()
                + rol.getEmpApellidos(), textFont, Element.ALIGN_CENTER));

        document.add(firmas);

        document.close();

    }

    private static double totalIngresos = 0;
    private static double totalEgresos = 0;
    private static double totalEgmenosDesc = 0;
    private static double descuentos = 0;
    private static double netoAPagar = 0;

    private static PdfPCell getCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell getCeVar(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Rectangle.LEFT);
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

    private static PdfPCell getDessCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        // cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell getDessCell2(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell getDessCell3(String text, Font font) {

        double num = Double.parseDouble(text);

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');

        // Definir el patrón de formato
        String patron = "#,###.00";

        // Crear un objeto DecimalFormat con el patrón y los símbolos personalizados
        DecimalFormat formato = new DecimalFormat(patron, simbolos);

        // Formatear el número
        String numeroFormateado = formato.format(num);

        PdfPCell cell = new PdfPCell(new Phrase(numeroFormateado, font));
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell getDessCell4(String text, Font font) {
        double num = Double.parseDouble(text);

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');

        // Definir el patrón de formato
        String patron = "#,###.00";

        // Crear un objeto DecimalFormat con el patrón y los símbolos personalizados
        DecimalFormat formato = new DecimalFormat(patron, simbolos);

        // Formatear el número
        String numeroFormateado = formato.format(num);
        PdfPCell cell = new PdfPCell(new Phrase(numeroFormateado, font));
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.TOP);
        return cell;
    }

    private static PdfPTable createIngresosEgresosTable(List<DetalleRol> ingresos, List<DetalleRol> egresos, Font font) {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        int maxRows = Math.max(ingresos.size(), egresos.size());

        for (int i = 0; i < maxRows; i++) {
            table.addCell(getCell("", font, Element.ALIGN_LEFT)); // Columna 1 vacía
            table.addCell(getCell("", font, Element.ALIGN_LEFT)); // Columna 2 vacía

            if (i < ingresos.size()) {
                DetalleRol ingreso = ingresos.get(i);
                table.addCell(getCell(ingreso.getNombre(), font, Element.ALIGN_LEFT)); // Columna 3
                table.addCell(getCellNum(String.valueOf(ingreso.getValor()), Element.ALIGN_RIGHT)); // Columna 4
                totalIngresos += ingreso.getValor(); // Acumulando el valor en cada iteración
            } else {
                table.addCell(getCell("", font, Element.ALIGN_LEFT)); // Columna 3 vacía
                table.addCell(getCell("", font, Element.ALIGN_RIGHT)); // Columna 4 vacía
            }

            if (i < egresos.size()) {
                DetalleRol egreso = egresos.get(i);
                table.addCell(getCeVar(egreso.getNombre(), font, Element.ALIGN_LEFT)); // Columna 5
                table.addCell(getCellNum(String.valueOf(egreso.getValor()), Element.ALIGN_RIGHT)); // Columna 6
                totalEgresos += egreso.getValor(); // Acumulando el valor en cada iteración
            } else {
                table.addCell(getCell("", font, Element.ALIGN_LEFT)); // Columna 5 vacía
                table.addCell(getCell("", font, Element.ALIGN_RIGHT)); // Columna 6 vacía
            }
        }

        return table;
    }

    private static PdfPTable createDescuentosTable(List<DetalleRol> descuentosList, Font textFont1, Font textFont2, Font textFont3) {
        double totalDescuentos = 0;
        PdfPTable tableDess = new PdfPTable(4);
        tableDess.setWidthPercentage(60);
        tableDess.setHorizontalAlignment(Element.ALIGN_LEFT); // Centrado

        PdfPCell header1 = getDessCell("DESGLOSE DE HABERES Y DESCUENTOS", textFont1);
        header1.setColspan(4); // Ocupará 4 columnas
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableDess.addCell(header1);

        tableDess.addCell(getDessCell("Cuota", textFont2));
        tableDess.addCell(getDessCell("Descripción", textFont2));
        tableDess.addCell(getDessCell("Valor", textFont2));
        tableDess.addCell(getDessCell("Saldo", textFont2));

        // Agregar los descuentos a la tabla
        for (DetalleRol descuento : descuentosList) {
            tableDess.addCell(getDessCell2(descuento.getReferencia(), textFont3)); // Cuota (Referencia)
            tableDess.addCell(getDessCell2(descuento.getNombre(), textFont3)); // Descripción (Nombre)
            tableDess.addCell(getDessCell3(String.valueOf(descuento.getValor()), textFont3)); // Valor
            tableDess.addCell(getDessCell3(String.valueOf(descuento.getSaldo()), textFont3)); // Saldo
            totalDescuentos += descuento.getValor(); // Acumular el valor del descuento
        }

        totalEgmenosDesc = totalEgresos + descuentos;
        netoAPagar = totalIngresos - totalEgmenosDesc;
        // Actualizar la variable global descuentos
        descuentos += totalDescuentos;

        tableDess.addCell(getDessCell("", textFont2));
        tableDess.addCell(getDessCell("", textFont2));
        tableDess.addCell(getDessCell4(String.valueOf(totalDescuentos), textFont2));
        tableDess.addCell(getDessCell("", textFont2));

        return tableDess;
    }

}
