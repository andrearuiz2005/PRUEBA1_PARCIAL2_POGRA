/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba1_parcialii;
import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author ar466
 */
public class ControlEmpleado {

    public static void main(String[] args) {
        EmpleadoManager manager = new EmpleadoManager();
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            int opcion = obtenerOpcion(scanner);

            try {
                if (opcion == 1) {
                    agregarEmpleado(manager, scanner);
                } else if (opcion == 2) {
                    listarEmpleadosNoDespedidos(manager);
                } else if (opcion == 3) {
                    despedirEmpleado(manager, scanner);
                } else if (opcion == 4) {
                    buscarEmpleadoActivo(manager, scanner);
                } else if (opcion == 5) {
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                } else {
                    System.out.println("Opcion no valida. Por favor, seleccione una opcion del 1 al 5.");
                }
            } catch (IOException e) {
                System.out.println("Ocurrio un error durante la operacion: " + e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("**** MENU ****");
        System.out.println("1- Agregar Empleado");
        System.out.println("2- Listar Empleados No Despedidos");
        System.out.println("3- Despedir Empleado");
        System.out.println("4- Buscar Empleado Activo");
        System.out.println("5- Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private static int obtenerOpcion(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingrese un numero valido.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void agregarEmpleado(EmpleadoManager manager, Scanner scanner) throws IOException {
        scanner.nextLine();
        System.out.print("Ingrese el nombre del empleado: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el salario del empleado: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Por favor, ingrese un numero valido para el salario.");
            scanner.next();
        }
        double salario = scanner.nextDouble();
        manager.addEmployee(nombre, salario);
        System.out.println("Empleado agregado exitosamente.");
    }

    private static void listarEmpleadosNoDespedidos(EmpleadoManager manager) throws IOException {
        manager.printActiveEmployees();
    }

    private static void despedirEmpleado(EmpleadoManager manager, Scanner scanner) throws IOException {
        System.out.print("Ingrese el codigo del empleado a despedir: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingrese un codigo valido.");
            scanner.next();
        }
        int codigoDespedir = scanner.nextInt();

        if (manager.isEmployeeActive(codigoDespedir)) {
            if (manager.dismissEmployee(codigoDespedir)) {
                System.out.println("Empleado despedido exitosamente.");
            } else {
                System.out.println("Hubo un problema al despedir al empleado.");
            }
        } else {
            System.out.println("Empleado no encontrado o ya fue despedido.");
        }
    }

    private static void buscarEmpleadoActivo(EmpleadoManager manager, Scanner scanner) throws IOException {
        System.out.print("Ingrese el codigo del empleado a buscar: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingrese un codigo valido.");
            scanner.next();
        }
        int codigoBuscar = scanner.nextInt();
        if (manager.isEmployeeActive(codigoBuscar)) {
            String detallesEmpleado = manager.getEmployeeDetails(codigoBuscar);
            System.out.println("Informacion del empleado:\n" + detallesEmpleado);
        } else {
            System.out.println("El empleado no esta activo o no existe.");
        }
    }
}
