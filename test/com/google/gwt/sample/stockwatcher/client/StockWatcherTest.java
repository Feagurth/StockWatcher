package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Clase para hacer pruebas JUnit sobre la aplicación
 * 
 * La clase debe extender GWTTestCase para poder realizarse las pruebas de forma
 * correcta
 * 
 */
public class StockWatcherTest extends GWTTestCase {

	/**
	 * Must refer to a valid module that sources this class.
	 */

	/**
	 * Clase que nos permite recuperar el nombre del módulo GWT
	 * 
	 * Debe hacer referencia a el módulo que vamos a probar
	 */
	public String getModuleName() {
		return "com.google.gwt.sample.stockwatcher.StockWatcher";
	}

	/**
	 * Un test de prueba sencillo que siempre dará verdero
	 */
	public void testSimple() {
		assertTrue(true);
	}

	/**
	 * Método que nos permite comprobar si los campos instanciados de la clase
	 * StockPrice se están asignando de forma correcta
	 */
	public void testStockPriceCtor() {

		// Guardamos en variables los valores que conformarán el objeto
		// StockPrice que usaremos de a modo de prueba para validar las
		// asignaciones
		String symbol = "XYZ";
		double price = 70.0;
		double change = 2.0;
		double changePercent = 100.0 * change / price;

		// Creamos un objeto StockPrice con los valores almacenados
		StockPrice sp = new StockPrice(symbol, price, change);

		// Verificamos que el objeto creado no sea nulo
		assertNotNull(sp);

		// Verificamos que el valor del símbolo es el mismo, tanto en el objeto
		// creado como en la variable con la cual ha sido creado
		assertEquals(symbol, sp.getSymbol());

		// Verificamos que el valor del precio es el mismo, tanto en el objeto
		// creado como en la variable con la cual ha sido creado, teniendo como
		// umbral de error lo especificado como tercer parámetro
		assertEquals(price, sp.getPrice(), 0.001);

		// Verificamos que el valor del cambio es el mismo, tanto en el objeto
		// creado como en la variable con la cual ha sido creado, teniendo como
		// umbral de error lo especificado como tercer parámetro
		assertEquals(change, sp.getChange(), 0.001);

		// Verificamos que el valor del cambio percentual es el mismo, tanto en
		// el objeto creado como en la variable con la cual ha sido creado,
		// teniendo como umbral de error lo especificado como tercer parámetro
		assertEquals(changePercent, sp.getChangePercent(), 0.001);
	}

}