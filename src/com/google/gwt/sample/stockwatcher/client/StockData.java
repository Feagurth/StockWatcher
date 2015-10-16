package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Clase para traducir la informaci�n JSON convirtiendo la informaci�n
 * recuperada en un objeto con el que pdoamos tratar. Esta clase hace uso de
 * JSNI (JavaScript Native Interface) y los tipos Overlay de GWT que nos va a
 * permitir hacer uso de funciones en javascript nativo en primer caso y nos
 * dar� flexibilidad para interactuar con los objetos javascript
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

	// Los siguientes tres m�todos son m�todos JSNI (JavaScript Native
	// Interface) definidos para recuperar la informaci�n recuperada del
	// servidor

	/**
	 * Funci�n que nos permite recuperar el s�mbolo de una acci�n
	 * 
	 * @return String El s�mbolo de una acci�n
	 */
	public final native String getSymbol() /*-{
		return this.symbol;
	}-*/;

	/**
	 * Funci�n que nos permite recuperar el precio de una acci�n
	 * 
	 * @return double El precio de una acci�n
	 */
	public final native double getPrice() /*-{
		return this.price;
	}-*/;

	/**
	 * Funci�n que nos permite recuperar el cambio de precio de una acci�n
	 * 
	 * @return double El cambio de precio de una acci�n
	 */
	public final native double getChange() /*-{
		return this.change;
	}-*/;

	// El siguiente m�todo no es un m�todo JSNI

	/**
	 * Funci�n que nos permite recuperar el porcentaje de cambio de precio de
	 * una acci�n
	 * 
	 * @return double El porcentaje de cambio de precio de una acci�n
	 */
	public final double getChangePercent() {
		return 100.0 * getChange() / getPrice();
	}
}