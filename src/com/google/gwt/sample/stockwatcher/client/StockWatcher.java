package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Clase StockWatcher
 */
public class StockWatcher implements EntryPoint {

	/**
	 * Constante para definir el tiempo de refresco de las actualizaciones
	 */
	private static final int REFRESH_INTERVAL = 5000;

	// Objetos usados para la interfaz gráfica
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stocksFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addStockButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();

	// ArrayList que contiene las acciones que el usuario ha introducido
	private ArrayList<String> stocks = new ArrayList<String>();

	// Creamos una instancia de la interfaz de constantes internacionalizadas
	private StockWatcherConstants constants = GWT
			.create(StockWatcherConstants.class);

	// Creamos una instancia de la interfaz de mensajes internacionalizados
	private StockWatcherMessages messages = GWT
			.create(StockWatcherMessages.class);

	/**
	 * Método de entrada. Es el primer método que se carga al iniciar la clase
	 * En este método irá toda la inicizalición de la interfaz gráfica, así como
	 * la asignación de eventos
	 */
	public void onModuleLoad() {

		// Definimos el título de la ventana sacando el valor de las constantes
		// internacionalizadas
		Window.setTitle(constants.stockWatcher());

		// Recuperamos el objeto con id 'appTitle' de la página html estática y
		// le añadimos el valor de la constantes internacionalizadas del nombre
		// de la aplicación
		RootPanel.get("appTitle").add(new Label(constants.stockWatcher()));

		// Creamos el botón de añadir acciones, pasándole como texto la cadena
		// sacada de la interfaz de constantes internacionalizadas
		addStockButton = new Button(constants.add());
		
		// Definimos los valores de los encabezados de las columnas, recuperando
		// el texto de las mismas de la interfaz de internacionalización
		stocksFlexTable.setText(0, 0, constants.symbol());
		stocksFlexTable.setText(0, 1, constants.price());
		stocksFlexTable.setText(0, 2, constants.change());
		stocksFlexTable.setText(0, 3, constants.remove());

		// Asignamos un relleno a las celdas de la tabla
		stocksFlexTable.setCellPadding(4);

		// Añadimos los estilos a la tabla
		stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		stocksFlexTable.addStyleName("watchList");
		stocksFlexTable.getCellFormatter().addStyleName(0, 0,
				"watchListNonNumericColumnHeader");
		stocksFlexTable.getCellFormatter().addStyleName(0, 1,
				"watchListNumericColumnHeader");
		stocksFlexTable.getCellFormatter().addStyleName(0, 2,
				"watchListNumericColumnHeader");
		stocksFlexTable.getCellFormatter().addStyleName(0, 3,
				"watchListRemoveColumn");

		// Añadimos los controles necesarios al panel
		addPanel.add(newSymbolTextBox);
		addPanel.add(addStockButton);
		addPanel.addStyleName("addPanel");

		// Definimos un evento para la pulsación del botón en el cuadro de texto
		newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {

			/**
			 * Evento de pulsación de tecla
			 */
			public void onKeyDown(KeyDownEvent event) {
				// Comprobamos si la tecla pulsada es la tecla enter
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					// Llamamos a la función para añadir una acción
					addStock();
				}
			}
		});

		// Definimos un evento para el botón de añadir acción
		addStockButton.addClickHandler(new ClickHandler() {
			/**
			 * Evento click
			 */
			public void onClick(ClickEvent event) {
				// Llamamos a la función para añadir una acción
				addStock();
			}
		});

		// Añadimos el botón al panel
		addPanel.add(addStockButton);

		// Añadimos al panel principal el resto de controles
		mainPanel.add(stocksFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);

		// Asociamos el panel principal con la página HTML anfritriona
		RootPanel.get("stockList").add(mainPanel);

		// Movemos el cursor al cuadro de texto
		newSymbolTextBox.setFocus(true);

		// Creamos un objeto timer
		Timer refreshTimer = new Timer() {
			/**
			 * Asignarmos al la función de refrescar el listado de acciónes para
			 * que se realice a cada ejecución del timer
			 */
			@Override
			public void run() {
				refreshWatchList();
			}
		};

		// Definimos el tiempo de refresco del timer
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

	}

	/**
	 * Función que nos permite añadir una acción a la tabla
	 */
	private void addStock() {

		// Recuperamos el valor introducido en la caja de texto, recortamos los
		// espacios en blanco y lo pasamos a mayúsculas, tras lo cual lo
		// almacenamos en una variable
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();

		// Pasamos el foco al cuadro de texto
		newSymbolTextBox.setFocus(true);

		// Usamos expresiones regulares para validar el valor introducido.
		// El sistema solo permitirá numeros, letras y puntos con una longitud
		// entre 1 y 10
		if (!symbol.matches("^[0-9A-Z&#92;&#92;.]{1,10}$")) {
			// Si el valor introducido no es válido, mostramos un mensaje al
			// usuario cuyo contenido viene definino en el interfaz de
			// internacionalización correspondiente
			Window.alert(messages.invalidSymbol(symbol));

			// Seleccionamos todo el texto que contiene el TextBox
			newSymbolTextBox.selectAll();

			// Salimos de la función
			return;
		}

		// Si todo es correcto, borramos el valor introducido
		newSymbolTextBox.setText("");

		// Comprobamos si el ArrayList de acciones contiene el valor introducido
		if (stocks.contains(symbol)) {
			// Si el valor ya existe, no hace falta volver a introducirlo, por
			// tanto salimos de la función
			return;
		}

		// Almacenamos el número de filas que tiene la tabla
		int row = stocksFlexTable.getRowCount();

		// Añadimos la acción al ArrayList de almacenaje de acciones
		stocks.add(symbol);

		// Añadimos la acción a la última fila de la tabla
		stocksFlexTable.setText(row, 0, symbol);

		// Añadimos una etiqueta a cada celda de la columna 2
		stocksFlexTable.setWidget(row, 2, new Label());

		// Añadimos los estilos de las columnas a la nueva fila
		stocksFlexTable.getCellFormatter().addStyleName(row, 1,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(row, 2,
				"watchListNumericColumn");
		stocksFlexTable.getCellFormatter().addStyleName(row, 3,
				"watchListRemoveColumn");

		// Creamos un botón destinado a eliminar la fila de la lista
		Button removeStockButton = new Button("x");

		// Definimos el estilo para el botón
		removeStockButton.addStyleDependentName("remove");

		// Añadimos un evento click al botón recién creado
		removeStockButton.addClickHandler(new ClickHandler() {
			/**
			 * Función para el evento click
			 */
			public void onClick(ClickEvent event) {

				// Recuperamos y almacenamos la posición que le corresponde en
				// el ArrayList a la acción que vamos a eliminar
				int removedIndex = stocks.indexOf(symbol);

				// Eliminamos la acción del ArrayList
				stocks.remove(removedIndex);

				// Eliminamos de la tabla la fila correspondiente
				stocksFlexTable.removeRow(removedIndex + 1);
			}
		});

		// Usamos setWidget para incrustar el botón en la última columna de la
		// fila
		stocksFlexTable.setWidget(row, 3, removeStockButton);

		// Refrescamos la lista de acciones
		refreshWatchList();

	}

	/**
	 * Función que calcula y refresca los preciones de las acciones
	 */
	private void refreshWatchList() {

		/**
		 * Constante para definir el precio máximo que tomará una acción
		 */
		final double MAX_PRICE = 100.0;

		/**
		 * Constante para definir el cambio de precio máximo que va a sufrir una
		 * acción
		 */
		final double MAX_PRICE_CHANGE = 0.02;

		// Creamos un array de objetos StockPrice del mismo tamaño que la lista
		// de acciones para almacenar los valores de estas
		StockPrice[] prices = new StockPrice[stocks.size()];

		// Iteramos por todos los valores contenido en el ArrayList de acciones
		for (int i = 0; i < stocks.size(); i++) {

			// Generamos el precio de la acción multiplicando el precio máximo
			// por un número decimal aleatorio entre 0 y 1
			double price = Random.nextDouble() * MAX_PRICE;

			// Generamos el cambio de valor multiplicando el precio por el
			// cambio de precio máximo multiplicado por un valor aleatorio entre
			// -1 y +1
			double change = price * MAX_PRICE_CHANGE
					* (Random.nextDouble() * 2.0 - 1.0);

			// Añadimos el nuevo objeto StockPrice al array de precios de las
			// acciones
			prices[i] = new StockPrice(stocks.get(i), price, change);
		}

		// Actualizamos la tabla con los precios de las acciones
		updateTable(prices);

	}

	/**
	 * Función que nos permite actualizar los valores de la tabla de acciones
	 * 
	 * @param prices
	 *            Array con los valores de las acciones
	 */
	private void updateTable(StockPrice[] prices) {

		// Comprobamos si tenemos precios con los que trabajar en el array
		if (prices.length > 0) {

			// Iteramos por el array de valores de las acciones
			for (int i = 0; i < prices.length; i++) {

				// Actualizamos en la tabla el precio introducido
				updateTable(prices[i]);
			}

			// Actualizamos el valor de la etiqueta con la fecha y hora actual
			// del sistema, recuperando el mensaje de la interfaz de
			// internacionalización correspondiente
			lastUpdatedLabel.setText(messages.lastUpdate(new Date()));
		}

	}

	/**
	 * Actualiza una sola linea de la tabla de acciones
	 * 
	 * @param price
	 *            Información de los valores de la acción
	 */
	private void updateTable(StockPrice price) {

		// Verificamos que la acción aún forma parte de los valores introducidos
		// en la tabla
		if (!stocks.contains(price.getSymbol())) {
			// Si no es así ,salimos de la función
			return;
		}

		// Almacenamos la posición en la tabla de la acción que hemos
		// introducido, cuya posición será la posición en el ArrayList de
		// acciones +1
		int row = stocks.indexOf(price.getSymbol()) + 1;

		// Format the data in the Price and Change fields.
		String priceText = NumberFormat.getFormat("#,##0.00").format(
				price.getPrice());

		// Definimos un formato específico para mostrar los valores de la acción
		NumberFormat changeFormat = NumberFormat
				.getFormat("+#,##0.00;-#,##0.00");

		// Usamos el formato creado para tratar el cambio de precio de la acción
		// almacenando el resultado en una variable
		String changeText = changeFormat.format(price.getChange());

		// Usamos el formato creado para tratar el cambio de precio en
		// percentiles y lo almacenamos en una variable
		String changePercentText = changeFormat
				.format(price.getChangePercent());

		// Modificamos los valores de la fila de la tabla con los nuevos valores
		stocksFlexTable.setText(row, 1, priceText);

		// Recuperamos el objeto Label que hay en la celda correspondiente a la
		// columna 2
		Label changeWidget = (Label) stocksFlexTable.getWidget(row, 2);

		// Cambiamos el valor del texto de la etiqueta por el nuevo valor
		changeWidget.setText(changeText + " (" + changePercentText + "%)");

		// Definimos una variable con el nombre de un estilo si no hay cambios
		String changeStyleName = "noChange";

		// Comprobamos si el cambio de precio en percentiles es menor de -0.1
		if (price.getChangePercent() < -0.1f) {
			// Si es así cambiamos el valor de la variable por el nombre del
			// estilo para cambios negativos
			changeStyleName = "negativeChange";
		}
		// Si el cambio de precio en percentiles no es menor de -0.1,
		// comprobamos si es mayor de 0.1
		else if (price.getChangePercent() > 0.1f) {
			// Si es así cambiamos el valor de la variable por el nombre del
			// estilo para cambios positivos
			changeStyleName = "positiveChange";
		}

		// Cambiamos el estilo de la etiqueta con el estilo que hemos obtenido
		changeWidget.setStyleName(changeStyleName);

	}

}