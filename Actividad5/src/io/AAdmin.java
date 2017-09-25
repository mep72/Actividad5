package io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class AAdmin {
	 // Obtiene logger de la clase definido en log4j2.xml
	// 1. Escribe tu codigo aqui {
	static Logger logger = LogManager.getLogger("io.AAdmin");
	
	// static Logger logger = java.util.logging.Logger.getLogger("io.Admin");
	
	//}
	// define la version de la aplicación
	private static final String APP_VERSION = "v1.0";

	public static void main(String[] args) {
		int seleccion;
		try (Scanner scanner = new Scanner(System.in)){
			while(true){
				System.out.format("Administrador de Archivos %s\n\n", APP_VERSION);
				// Inicio Menu
				System.out.println("Opciones: ");
				System.out.println("(1) Listar Contenido de Directorio");
				System.out.println("(2) Salir");
				System.out.println("\nPor favor ingrese una opción: ");
				// Fin de Menu
				// Try Anidado
				try{
					// Asigna token Integer parseado
					seleccion = scanner.nextInt();
					switch(seleccion){
					case 1:
						logger.info("Listar contenido seleccionado.");
						System.out.println("Ingrese el directorio a listar:");
						String directorio = scanner.next();
						
						System.out.println("Guardar resultados en archivo? ");
						System.out.println("(1) Si");
						System.out.println("(2) No");
						int guardar = scanner.nextInt();
						
						// variable para guardar resultado de contenido
						List<Path> paths = new ArrayList<>();
						Path dir = Paths.get(directorio);
						logger.info("Listando contenidos de {}", dir.getFileName());

						// Valida si existe el directorio y si es Directorio
						if (Files.exists(dir) && Files.isDirectory(dir)){
							// 2. Obtiene el contenido por medio del método WalkDir
							// Escribe tu codigo aqui {
							paths = NIO2RecursiveDir.walkDir(dir, paths);
							//}
							// Registra un evento INFO en el log con los elementos que se encontraron
							logger.info("Se encontraron {} elementos", paths.size());
							// 3. Imprime los elemetos resultado de walkdir
							// Escribe tu codigo aqui {
							for(Path path : paths) {
								System.out.println(path.toString());
							}
							//}
							System.out.println("");
						} else {
							logger.error("Path {} no existe o no es directorio", dir.normalize());
							System.out.format("Verifica que %s exista y sea un directorio e intenta de nuevo.\n\n", directorio);
						}
						
						if (guardar == 1){
							List<String> lineas = new ArrayList<>();
							// 4. Convierte Lista de Paths a lista de String
							// Escribe tu codigo aqui {
							for(Path path : paths) {
								lineas.add(path.toString());
							}
							//}
							
							/* 
							 * Resultado lo tiene que guardar dentro de un archivo
							 * con el formato directorio.txt 
							 * */ 
							Path guardarDir = Paths.get(dir.getFileName() + ".txt");
							/* 5. Escribe el archivo en guardarDir con el contenido de
							 * la variable lineas, Character set UTF_8 y opcion CREATE 
							 * */ 
							// Escribe tu codigo aqui {
							try {
								FileWriter fileWriter = new FileWriter(guardarDir.toString());
								for(String linea : lineas) {
									fileWriter.write(linea);
								}
								fileWriter.flush();
								fileWriter.close();
							} catch(IOException ex) {
								logger.error("{}:{}", ex.getClass(), ex.getMessage());
								System.err.format("Ocurrio un error al escribir en archivo de salida %s", ex.getMessage());
							}
							//}

							// Informa la ubicación del archivo
							System.out.format("Resultados guardados en %s\n", guardarDir.getFileName());
							logger.info("Resultados guardados en {}", guardarDir.getFileName());
						} 
						break;
					case 2:
						System.out.println("Saliendo..");
						logger.info("Saliendo...");
						return;
					default:
						System.err.println("Opción inválida.");
						logger.error("Opción inválida: {}", seleccion);
						break;
					}
				} catch (InputMismatchException ex){
					logger.error("{}: {}", ex.getClass(), ex.getMessage());
					System.err.format("Ocurrió un error. Para más información consulta el log de la aplicación.");
					scanner.next();
				}

			}
		} catch (Exception ex){
			logger.error("{}: {}", ex.getClass(), ex.getMessage());
			System.err.format("Ocurrió un error. Para más información consulta el log de la aplicación.");
		}
	}
	
}
