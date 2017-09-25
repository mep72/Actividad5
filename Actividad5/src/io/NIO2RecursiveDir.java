package io;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NIO2RecursiveDir {
	public static void main(String[] args) {
		/* Crear lista para guardar resultados*/
		List<Path> paths = new ArrayList<>();
		
		/* Obtener  acceso al recurso */
		Path folder = Paths.get("io");
		System.out.format("Contenidos del directorio \"%s\": \n", folder.getFileName());
		
		/* Iterar recursivamente el contenido del directorio */
		paths = walkDir(folder, paths);
		
		/* Imprimir el contenido de la lista */
		paths.forEach(System.out::println);
		
	}
	public static List<Path> walkDir(Path path, List<Path> paths){		
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(path)){
			for(Path p: stream){
				if (Files.isDirectory(p)){
					walkDir(p, paths);
				}
				paths.add(p);
			}
		} catch (IOException | DirectoryIteratorException ex){
			System.err.format("Error %s: %s", ex.getClass(), ex.getMessage());
		}
		return paths;
	}
}
