package dominio.integracion;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.Prestamo;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {

	private static final String CRONICA_DE_UNA_MUERTA_ANUNCIADA = "Cronica de una muerta anunciada";
	
	private static final String STIVEN_GIRALDO = "Stiven Giraldo";
	private SistemaDePersistencia sistemaPersistencia;

	private RepositorioLibro repositorioLibros;
	private RepositorioPrestamo repositorioPrestamo;

	@Before
	public void setUp() {

		sistemaPersistencia = new SistemaDePersistencia();

		repositorioLibros = sistemaPersistencia.obtenerRepositorioLibros();
		repositorioPrestamo = sistemaPersistencia.obtenerRepositorioPrestamos();

		sistemaPersistencia.iniciar();
	}

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void prestarLibroTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		blibliotecario.prestar(libro.getIsbn(), STIVEN_GIRALDO);

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Assert.assertNotNull(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn()));

	}

	@Test
	public void prestarLibroNoDisponibleTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		blibliotecario.prestar(libro.getIsbn(), STIVEN_GIRALDO);
		try {

			blibliotecario.prestar(libro.getIsbn(), STIVEN_GIRALDO);
			fail();

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE, e.getMessage());
		}
	}

	@Test
	public void prestarLibroPalindromoTest() {
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder().conIsbn("1A0A1");
		Libro libro = libroTestDataBuilder.build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act

		try {

			blibliotecario.prestar(libro.getIsbn(), STIVEN_GIRALDO);
			fail();

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_SOLO_SE_USA_EN_LA_BIBLIOTECA, e.getMessage());
		}

	}

	@Test
	public void prestarLibroConFechaDeEntregaTest() {
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder().conIsbn("8A2C75C9").conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA);
		Libro libro = libroTestDataBuilder.build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		blibliotecario.prestar(libro.getIsbn(), STIVEN_GIRALDO);
		Prestamo prestamo = repositorioPrestamo.obtener(libro.getIsbn());
		
		// assert
		Assert.assertNotNull(prestamo.getFechaEntregaMaxima());
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
        Assert.assertEquals(CRONICA_DE_UNA_MUERTA_ANUNCIADA, prestamo.getLibro().getTitulo());
        Assert.assertEquals(STIVEN_GIRALDO, prestamo.getNombreUsuario());
        Assert.assertNotNull(prestamo.getFechaSolicitud());

	}
}
