package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

/**
 * Clase para almacenar la informaci�n de el precio de las acciones
 * 
 */
public class StockPrice implements Serializable {

	/**
	 * Id generado aleatoriamente
	 */
	private static final long serialVersionUID = 6920472853042767550L;

	/**
	 * Variable para almacenar el nombre de la acci�n
	 */
	private String symbol;

	/**
	 * Variable para almacenar el valor de la acci�n
	 */
	private double price;

	/**
	 * Variable para almacenar el cambio que ha sufrido la acci�n desde el valor
	 * anterior al actual
	 */
	private double change;

	/**
	 * Constructor de la clase sin par�metros para permitir la serializaci�n de
	 * la misma
	 */
	public StockPrice() {
		super();
	}

	/**
	 * Constructor de la clase
	 * 
	 * @param symbol
	 *            El nombre de la acci�n
	 * @param price
	 *            El precio de la acci�n
	 * @param change
	 *            El cambio que ha sufrido la acci�n en su valor
	 */
	public StockPrice(String symbol, double price, double change) {
		this.symbol = symbol;
		this.price = price;
		this.change = change;
	}

	/**
	 * Funci�n que nos permite recuperar el nombre de una acci�n
	 * 
	 * @return El nombre de la acci�n
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Funci�n que nos permite asignar el nombre a una acci�n
	 * 
	 * @param symbol
	 *            El nombre que queremos asignar a la acci�n
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Funci�n que nos permite recuperar el precio de la acci�n
	 * 
	 * @return El precio de la acci�n
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Funci�n que nos permite asignar un precio a una acci�n
	 * 
	 * @param price
	 *            El precio que queremos asignar a una acci�n
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Funci�n que nos permite recuperar el cambio de precio sufrido por una
	 * acci�n
	 * 
	 * @return El cambio de precio sufrido por una acci�n
	 */
	public double getChange() {
		return change;
	}

	/**
	 * Funci�n que nos permite asignar el cambio de precio sufrido por una
	 * acci�n
	 * 
	 * @param change
	 *            El cambio de precio sufrido por una acci�n
	 */
	public void setChange(double change) {
		this.change = change;
	}

	/**
	 * Funci�n que nos permite recuperar el cambio de precio que ha sufrido una
	 * acci�n en porcentaje
	 * 
	 * @return El cambio de precio que ha sufrido una acci�n en porcentaje
	 */
	public double getChangePercent() {
		return 10.0 * this.change / this.price;
	}

}
