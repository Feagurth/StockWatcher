package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Clase para traducir la información JSON convirtiendo la información
 * recuperada en un objeto con el que pdoamos tratar. Esta clase hace uso de
 * JSNI (JavaScript Native Interface) y los tipos Overlay de GWT que nos va a
 * permitir hacer uso de funciones en javascript nativo en primer caso y nos
 * dará flexibilidad para interactuar con los objetos javascript
 */
class StockData extends JavaScriptObject {

	/**
	 * Constructor de la clase
	 * 
	 * Los tipos Overlay siempre tienen un constructor sin argumentos y
	 * protected.
	 */
	protected StockData() {
	}

	// Los siguientes tres métodos son métodos JSNI (JavaScript Native
	// Interface) definidos para recuperar la información recuperada del
	// servidor

	/**
	 * Función que nos permite recuperar el símbolo de una acción
	 * 
	 * @return String El símbolo de una acción
	 */
	public final native String getSymbol() /*-{
		return this.symbol;
	}-*/;

	/**
	 * Función que nos permite recuperar el precio de una acción
	 * 
	 * @return double El precio de una acción
	 */
	public final native double getPrice() /*-{
		return this.price;
	}-*/;

	/**
	 * Función que nos permite recuperar el cambio de precio de una acción
	 * 
	 * @return double El cambio de precio de una acción
	 */
	public final native double getChange() /*-{
		return this.change;
	}-*/;

	// El siguiente método no es un método JSNI

	/**
	 * Función que nos permite recuperar el porcentaje de cambio de precio de
	 * una acción
	 * 
	 * @return double El porcentaje de cambio de precio de una acción
	 */
	public final double getChangePercent() {
		return 100.0 * getChange() / getPrice();
	}
}