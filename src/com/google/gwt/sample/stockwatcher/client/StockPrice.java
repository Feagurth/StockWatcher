package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

/**
 * Clase para almacenar la información de el precio de las acciones
 * 
 */
public class StockPrice implements Serializable {

	/**
	 * Id generado aleatoriamente
	 */
	private static final long serialVersionUID = 6920472853042767550L;

	/**
	 * Variable para almacenar el nombre de la acción
	 */
	private String symbol;

	/**
	 * Variable para almacenar el valor de la acción
	 */
	private double price;

	/**
	 * Variable para almacenar el cambio que ha sufrido la acción desde el valor
	 * anterior al actual
	 */
	private double change;

	/**
	 * Constructor de la clase sin parámetros para permitir la serialización de
	 * la misma
	 */
	public StockPrice() {
		super();
	}

	/**
	 * Constructor de la clase
	 * 
	 * @param symbol
	 *            El nombre de la acción
	 * @param price
	 *            El precio de la acción
	 * @param change
	 *            El cambio que ha sufrido la acción en su valor
	 */
	public StockPrice(String symbol, double price, double change) {
		this.symbol = symbol;
		this.price = price;
		this.change = change;
	}

	/**
	 * Función que nos permite recuperar el nombre de una acción
	 * 
	 * @return El nombre de la acción
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Función que nos permite asignar el nombre a una acción
	 * 
	 * @param symbol
	 *            El nombre que queremos asignar a la acción
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Función que nos permite recuperar el precio de la acción
	 * 
	 * @return El precio de la acción
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Función que nos permite asignar un precio a una acción
	 * 
	 * @param price
	 *            El precio que queremos asignar a una acción
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Función que nos permite recuperar el cambio de precio sufrido por una
	 * acción
	 * 
	 * @return El cambio de precio sufrido por una acción
	 */
	public double getChange() {
		return change;
	}

	/**
	 * Función que nos permite asignar el cambio de precio sufrido por una
	 * acción
	 * 
	 * @param change
	 *            El cambio de precio sufrido por una acción
	 */
	public void setChange(double change) {
		this.change = change;
	}

	/**
	 * Función que nos permite recuperar el cambio de precio que ha sufrido una
	 * acción en porcentaje
	 * 
	 * @return El cambio de precio que ha sufrido una acción en porcentaje
	 */
	public double getChangePercent() {
		return 10.0 * this.change / this.price;
	}

}
