package dominio;

import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String EL_LIBRO_SOLO_SE_USA_EN_LA_BIBLIOTECA = "Los libros palindromos solo se pueden utilizar en la biblioteca";
	public static final String EL_LIBRO_YA_FUE_PRESTADO = "Este libro ya fue prestado";
	private static final int SUMA_ISBN_MAYOR = 30;

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn, String nombreUsuario) {

		if (esPalindromo(isbn)) {
			throw new PrestamoException(EL_LIBRO_SOLO_SE_USA_EN_LA_BIBLIOTECA);
		}

		if (this.esPrestado(isbn)) {
			throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);

		}

		Libro libro = this.repositorioLibro.obtenerPorIsbn(isbn);
		Date fechaPrestamo = new Date();
		Date fechaEntrega = null;

		if (this.esMayorATreinta(isbn)) {
			fechaEntrega = this.calcularFechaEntrega(fechaPrestamo);
		}

		Prestamo prestamo = new Prestamo(fechaPrestamo, libro, fechaEntrega, nombreUsuario);
		this.repositorioPrestamo.agregar(prestamo);

	}

	public boolean esPrestado(String isbn) {
		return this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn) != null;
	}

	public boolean esPalindromo(String isbn) {

		StringBuilder isbnReversado = new StringBuilder(isbn).reverse();
		return isbnReversado.toString().equals(isbn);

	}

	public boolean esMayorATreinta(String isbn) {
		char[] cadena = isbn.toCharArray();
		int suma = 0;
		for (char caracter : cadena) {
			if (Character.isDigit(caracter)) {
				suma = suma + Character.getNumericValue(caracter);
			}
		}
		return suma > SUMA_ISBN_MAYOR;
	}

	public Date calcularFechaEntrega(Date fecha) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fecha);

		int dia = 1, dias = 15;

		for (int i = 1; i < dias; i++) {
			calendario.add(Calendar.DAY_OF_WEEK, dia);
			if (calendario.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				dias++;
			}
		}
		return calendario.getTime();

	}

}
