package dominio.unitaria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {

	@Test
	public void esPrestadoTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();
		
		Libro libro = libroTestDataBuilder.build(); 
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(libro);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act 
		boolean esPrestado =  bibliotecario.esPrestado(libro.getIsbn());
		
		//assert
		assertTrue(esPrestado);
	}
	
	@Test
	public void libroNoPrestadoTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();
		
		Libro libro = libroTestDataBuilder.build(); 
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(null);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act 
		boolean esPrestado =  bibliotecario.esPrestado(libro.getIsbn());
		
		//assert
		assertFalse(esPrestado);
	}
	
	@Test
	public void esPalindromoTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder().conIsbn("1A0A1");
		Libro libro = libroTestDataBuilder.build();
		
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act
		boolean esPalindrome = bibliotecario.esPalindromo(libro.getIsbn());

		//assert
		assertTrue(esPalindrome);
	}
	
	@Test
	public void noEsPalindromoTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder().conIsbn("1A0A1b");
		Libro libro = libroTestDataBuilder.build();
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act
		boolean esPalindrome = bibliotecario.esPalindromo(libro.getIsbn());
		
		//assert
		assertFalse(esPalindrome);
	}
	
	@Test
	public void esMayorATreintaTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder().conIsbn("8A2C75C9");
		Libro libro = libroTestDataBuilder.build();
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act
		boolean esMayor = bibliotecario.esMayorATreinta(libro.getIsbn());
		
		//assert
		assertTrue(esMayor);
	}
	
	@Test
	public void noEsMayorATreintaTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder().conIsbn("8A2C9");
		Libro libro = libroTestDataBuilder.build();
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act
		boolean noEsMayor = bibliotecario.esMayorATreinta(libro.getIsbn());
		
		//assert
		assertFalse(noEsMayor);
	}
	
	@Test
	public void calcularFechaEntregaTest() throws ParseException {
		
		// arrange
		
		DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
		Date fechaInicial = formatoFecha.parse("08-07-2019");
		Date fechaEsperada = formatoFecha.parse("24-07-2019");
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act
		Date fechaEntrega = bibliotecario.calcularFechaEntrega(fechaInicial);
		
		//assert
		assertEquals(fechaEsperada, fechaEntrega);
	}
	
	@Test
	public void calcularFechaEntregaTresDomingosTest() throws ParseException {
		
		// arrange
		
		DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
		Date fechaInicial = formatoFecha.parse("06-07-2019");
		Date fechaEsperada = formatoFecha.parse("23-07-2019");
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act
		Date fechaEntrega = bibliotecario.calcularFechaEntrega(fechaInicial);
		
		//assert
		assertEquals(fechaEsperada, fechaEntrega);
	}
	
	
}

