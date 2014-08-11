## How to create a kpi from a csv file

In your groovy file, define the structure of the csv file with a class :

	class CsvRow {

        int time;
        Date date;
        Person head;
        Person manager;
        Person worker;
        String site;
	
	}

<i>If you define the properties of the class as private, public or protected, you will have to create the associated getters/setters to make it work.</i>

Define the kpi as usual. For example : 

	def kpibuilder = new ObjectGraphBuilder()
	kpibuilder.classNameResolver = "org.komea.product.database.model"

	define "kpi", kpibuilder.kpi (
        nameAndDescription: "Total time spent by an employee in a month.",
        entityType: EntityType.PERSON,
        groupFormula: GroupFormula.AVG_VALUE,
        kpiKey: "person_month_time_spent",
        providerType: ProviderType.TIMEMANAGEMENT,
        valueDirection: ValueDirection.NONE,
        valueMax: 31*24d,
        valueMin: 0d,
        valueType: ValueType.INT
    )

Create a new CSVQuery instance and set the backup delay in its constructor.

	query = new CSVQuery(BackupDelay.HOUR)
    
Use the setResource() method to define the location of the csv file :

	query.setResource("http://www.your-domain/your-file.csv")
    
By default, this is the configuration of the csv parser :
* semicolon as cell separator
* single quote used as quote char
* the first line is skipped

But you can override this configuration by calling some methods of the CSVQuery object.

	query.setCharSeparator(',')
    query.setFirstLine(0)
    query.setCharQuote('\"')
    
Specify which class define the structure of the csv file and bind each column of the csv file to a property of this class.

	query.setRowClass(CsvRow.class)
    query.setColumnMapping(
    	"time", "date", "head", "manager", "worker", "site"
    )
    
If you have some cells in the csv file that need to be converted into Date objects, you can indicate the converter to use for building the Java object.
For example, if the date strings in the cells have the format "dd/MM/yyyy", you can define your converter as follow : 

	query.defineConverter(Date.class, new SimpleDateConverter("dd/MM/yyyy"))

You can also define custom converters to build specific Java objects from values of csv cells.

	public class CustomConverter extends StringConverter<CustomObject> {
    	@Override
		public CustomObject convertValue(final String _value) {
        	return new CustomObject(_value);
        }
    }
    
    query.defineConverter(Date.class, new CustomConverter())
    
Now, you can create the JoSQL query that will be executed on the csv file.
The result of the query must have 2 fields : the first one must be the entity and the second one the value.

	query.setQuery(
    	"SELECT worker, @total_time FROM "+CsvRow.class.getName()+"
			WHERE date BETWEEN toDate('01-04-2014', 'dd-MM-yyyy') 
            	AND toDate('30-04-2014', 'dd-MM-yyyy')
			GROUP BY worker
			EXECUTE ON GROUP_BY_RESULTS sum(time) AS total_time"
    )

<i>This is an example of a JoSQL query that returns the total time spent for all the workers between the 01/04/2014 and the 30/04/2014.</i>