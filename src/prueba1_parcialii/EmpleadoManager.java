/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba1_parcialii;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ar466
 */

public class EmpleadoManager {
    private RandomAccessFile remps;
    private int currentCode;

    public EmpleadoManager() {
        try {
            File mf = new File("company");
            mf.mkdir();
            remps = new RandomAccessFile("company/empleado.emp", "rw");
            currentCode = 1;
        } catch (IOException e) {
            System.out.println("Error al inicializar el archivo de empleados.");
        }
    }

    public void addEmployee(String name, double salary) throws IOException {
        remps.seek(remps.length());
        int code = currentCode++;
        remps.writeInt(code);
        remps.writeUTF(name);
        remps.writeDouble(salary);
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        remps.writeLong(0);
        createEmployeeFolders(code);
    }

    private String employeeFolder(int code) {
        return "company/empleado" + code;
    }

    private void createEmployeeFolders(int code) throws IOException {
        File edir = new File(employeeFolder(code));
        edir.mkdir();
        createYearSalesFile(code);
    }

    private RandomAccessFile salesFileFor(int code) throws IOException {
        String dirPadre = employeeFolder(code);
        int yearActual = Calendar.getInstance().get(Calendar.YEAR);
        String path = dirPadre + "/ventas" + yearActual + ".emp";
        return new RandomAccessFile(path, "rw");
    }

    private void createYearSalesFile(int code) throws IOException {
        RandomAccessFile raf = salesFileFor(code);
        if (raf.length() == 0) {
            for (int mes = 0; mes < 12; mes++) {
                raf.writeDouble(0);
                raf.writeBoolean(false);
            }
        }
    }

    public void printActiveEmployees() throws IOException {
        remps.seek(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("**** LISTA DE EMPLEADOS ****");
        int count = 1;

        while (remps.getFilePointer() < remps.length()) {
            int code = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (fechaDespido == 0) {
                String fechaContratacionStr = dateFormat.format(new Date(fechaContratacion));
                System.out.println(count + ". " + code + " - " + name + " - " + salary + " Lps - " + fechaContratacionStr);
                count++;
            }
        }

        if (count == 1) {
            System.out.println("No hay empleados no despedidos en la lista.");
        }
    }

    public boolean isEmployeeActive(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int currentCode = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();
            if (currentCode == code && fechaDespido == 0) {
                remps.seek(remps.getFilePointer() - 28);
                return true;
            }
        }
        return false;
    }

    public String getEmployeeDetails(int code) throws IOException {
        remps.seek(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        while (remps.getFilePointer() < remps.length()) {
            int currentCode = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (currentCode == code) {
                String fechaContratacionStr = dateFormat.format(new Date(fechaContratacion));
                String fechaDespidoStr = (fechaDespido == 0) ? "N/A" : dateFormat.format(new Date(fechaDespido));
                return "Codigo: " + currentCode + "\nNombre: " + name + "\nSalario: " + salary + " Lps" +
                        "\nFecha de Contratacion: " + fechaContratacionStr + "\nFecha de Despido: " + fechaDespidoStr;
            }
        }
        return "Empleado no encontrado.";
    }

    public boolean dismissEmployee(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int currentCode = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();

            if (currentCode == code && fechaDespido == 0) {
                remps.seek(remps.getFilePointer() - 8);
                remps.writeLong(Calendar.getInstance().getTimeInMillis());
                System.out.println("Empleado despedido: " + name);
                return true;
            }
        }
        return false;
    }
}
