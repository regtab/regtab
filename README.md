# RegTab API

**RegTab API** is a tool designed for the development of applied software for extracting relational data from arbitrary tables presented in machine-readable documents such as Excel spreadsheets or CSVs.

**When it can be useful:**

There are a big volume of tables that are presented in human-centric documents: reports, articles, plans, etc. Such tables conceal valuable relational data that typically cannot be read directly. To be applied in various real-world scenarios (e.g. business analytics, decision support systems, data journalism) these data should be extracted from source tables and stored into databases.

**How it works:**

RegTab API provides two main components:
 -  _Interpretable Table Model_ (ITM): Extending the capabilities of common representation of tables by adding semantics that allow automatic inference of recordsets from them. 
 -  _Regular Table Language_ (RTL): A domain-specific language of patterns specifying regular structures of tables. RTL-patterns can be matched with tables represented as ITM-instances for validating them and enriching them by semantics. As a result of the successful matching an RTL-pattern with a table, the corresponding ITM-instance is enriched with semantics, making it possible to automatically extract the recordset.

Automatic data extraction from a table requires the following steps:
- Representing the table as an ITM-instance.
- Providing an RTL-pattern specifying the table.
- Matching the RTL-pattern with the ITM-instance.
- Inference the recordset from the ITM-instance.

This tool is implemented as a library for the Java software development platform, allowing RTL-patterns to be embedded in source code compiled into Java bytecode.

## Prerequisites

- JDK 21+
- Maven 3.9+

## Usage

The usage of this tool includes the following steps.

**Step 1: Add the dependency**

The dependency can be gotten in an appropriate format at the [Maven Central Repository](https://mvnrepository.com/artifact/com.regtab.core/regtab-core).

For example, if the Maven format is used then the following dependency should be added into the POM-file:
```xml
<dependency>
    <groupId>com.regtab.core</groupId>
    <artifactId>regtab-core</artifactId>
    <version>0.1.3</version>
</dependency>
```

**Step 2: Create the instance of ITM**

Let's there is a source table: 
```java
final String[][] sourceTable = {  
	{"", "2000", "2005", "2010"},  
	{"Australia", "19028802", "20176844", "22031750"},  
	{"Austria", "8002186", "8201359", "8351643"},  
	{"Belgium", "10239085", "10445852", "10839905"}  
};  
```

The instance of ITM corresponding to the source table can be created as follows: 
```java  
// The number of rows in the source table  
final int numOfRows = sourceTable.length;  
// The number of columns in the source table  
final int numOfCols = sourceTable[0].length;  
  
// Create the instance of ITM corresponding to the source table  
final ITable itm = new ITable(numOfRows, numOfCols);  

// Populate the instance of ITM by cells from the source table  
int rowIndex = 0;  
for (String[] row : sourceTable) {  
	int colIndex = 0;  
	for (String cellValue : row) {  
		itm.createCell(rowIndex, colIndex, null, cellValue);  
		colIndex++;  
	}  
	rowIndex++;  
}
```

In the case when a source table is represented in a CSV file, for example, as follows:
```java 
"","2000","2005","2010"
"Australia","19028802","20176844","22031750"
"Austria","8002186","8201359","8351643"
"Belgium","10239085","10445852","10839905"
```
> Note that this CSV file should be corresponded to the format where all values are wrapped in double quotes, comma is used as the value delimiter, and newline as the row delimiter.

The instance of ITM can be created from the CSV file as follows: 
```java
// Create the reader to input the source table from CSV
CSVReader csvReader = new CSVReader("table.csv");
// Create the instance of ITM by reading data from the source table
ITable itm = csvReader.read();
```

In the case when a source table is represented in a Excel file, for example, as follows:
```java
=""	="2000"	="2005"	="2010"
="Australia"	="19028802"	="20176844"	="22031750"
="Austria"	="8002186"	="8201359"	="8351643"
="Belgium"	="10239085"	="10445852"	="10839905"
```
> In order to view these data in Excel, please, create a new Excel workbook, copy these data, and paste it to the workbook.

The instance of ITM can be created from the CSV file as follows:
```java
// Create the reader to input the source table from Excel
XlReader xlReader = new XlReader("table.xlsx");
// It is expected that the source table is placed on the first sheet
final int sheetIndex = 0;
// It is expected that cells of the source table are "A1:D4"
final String rangeOfCells = "A1:D4";
// Create the instance of ITM by reading data from the source table
ITable itm = xlReader.read(sheetIndex, rangeOfCells);
```

> Note that the range of cells can be `null` to read all not blank cells from the sheet.

<!--
Todo: Write about splitting merged cells
Todo: Write about using formatted/unformatted values
Todo: Write about splitting multiline cells
-->

**Step 3: Create the RTL-pattern specifying the instance of ITM**

Present the RTL-pattern as a string, for example, as follows:
```java
String rtl = "[[SKIP][VAL: SCHEMA='YEAR']+]"  
	+ "[[VAL: SCHEMA='COUNTRY'][VAL: RECORD=(ROW{1};COL{1}); SCHEMA='DATA']+]+";
```

Compile the RTL-pattern:
```java
RTLPattern pattern = RTLPattern.compile(rtl);
```

**Step 4: Apply the RTL-pattern to enrich the instance of ITM by semantics**

Create the matcher for the RTL-pattern:
```java
RTLMatcher matcher = pattern.matcher();  
```

Match the RTL-pattern with the instance of ITM:
```java
TableMatch match = matcher.match(itm); 
```

Apply the result of matching the RTL-pattern with the instance of ITM:
```java
match.apply();  
```
As a result, the instance of ITM is enriched by semantics.

Alternatively, it is possible to use the shorter way:
```java  
RTLPattern.apply(rtl, itm);
```

**Step 5: Inference the recordset from the instance of ITM enriched by semantics**

It is possible to use the shorter way:
```java
Recordset recordset = itm.extract();  
```

The recordset can accessed as follows:
```java
// Print the header of the recordset  
final String[] header = recordset.header();  
System.out.println(Arrays.toString(header));  
  
// Print the header of the recordset  
final String[][] data = recordset.data();  
for (String[] record: data) {  
	System.out.println(Arrays.toString(record));  
}
```

**Step 6: Store the extracted recordset**

Store the extracted recordset to the CSV file
```java
// Create the writer to output the recordset to CSV
CSVWriter csvWriter = new CSVWriter("recordset.csv");
// Write the recordset to CSV
csvWriter.write(recordset);
```

As a result, the stored recordset should look like this one:
```java
"DATA","COUNTRY","YEAR"  
"19028802","Australia","2000"  
"20176844","Australia","2005"  
"22031750","Australia","2010"  
"8002186","Austria","2000"  
"8201359","Austria","2005"  
"8351643","Austria","2010"  
"10239085","Belgium","2000"  
"10445852","Belgium","2005"  
"10839905","Belgium","2010"
```

Or store the extracted recordset to the Excel file
```java
// Create the writer to output the recordset to Excel
XlWriter xlWriter = new XlWriter("recordset.xlsx");
// Write the recordset to Excel
xlWriter.write(recordset);
```

As a result, the stored recordset should look like this one:
```java
="DATA"	="COUNTRY"	="YEAR"
="19028802"	="Australia"	="2000"
="20176844"	="Australia"	="2005"
="22031750"	="Australia"	="2010"
="8002186"	="Austria"	="2000"
="8201359"	="Austria"	="2005"
="8351643"	="Austria"	="2010"
="10239085"	="Belgium"	="2000"
="10445852"	="Belgium"	="2005"
="10839905"	="Belgium"	="2010"
```
> In order to view these data in Excel, please, create a new Excel workbook, copy these data, and paste it to the workbook.

## Illustrative examples

### Illustrative example 1

#### Task

There is a source table that lists entities (electrical transformers) as follows:

| Equipment       | Reference data | Reference data | Passport details | Passport details |
|-----------------|----------------|----------------|------------------|------------------|
| Transformer T-1 | Type           | DEP-1600       | Full weight      | 6680             |
| Transformer T-1 | Factory num.   | 406034         | Last M.R. date   | 2005             |
| Transformer T-1 | Defect data    | none           |                  |                  |
| Transformer T-2 | Type           | DEP-2400       | Full weight      | 8480             |
| Transformer T-2 | Factory num.   | 555879         | Last M.R. date   | 2005             |
| Transformer T-2 | Defect data    | none           |                  |                  |

It is needed to extract all listed entities and store them into the recordset as follows:

| Equipment       | Type     | Full weight | Factory num. | Last M.R. date | Defect data |
|-----------------|----------|-------------|--------------|----------------|-------------|
| Transformer T-1 | DEP-1600 | 6680        | 406034       | 2005           | none        |
| Transformer T-2 | DEP-2400 | 8480        | 555879       | 2005           | none        |

#### Solution

The source table can be matched against the RTL-pattern presented below:
```
[[ATTR][SKIP]{4}]
[[VAL: SCHEMA=COL; RECORD=ROW; JOIN=(UP:@TEXT==THIS@TEXT)]
{SCHEMA=LEFT [ATTR][@BLANK ? SKIP | VAL]}{2}]+
```

When the source table is stored in the CSV file (`table.csv`) as follows:
```java
"Equipment","Reference data","Reference data","Passport details","Passport details"
"Transformer T-1","Type","DEP-1600","Full weight","6680"
"Transformer T-1","Factory num.","406034","Last M.R. date","2005"
"Transformer T-1","Defect data","none","",""
"Transformer T-2","Type","DEP-2400","Full weight","8480"
"Transformer T-2","Factory num.","555879","Last M.R. date","2005"
"Transformer T-2","Defect data","none","",""
```
> Note that this CSV file should be corresponded to the format where all values are wrapped in double quotes, comma is used as the value delimiter, and newline as the row delimiter.

Then the RTL-pattern can be applied in a Java program as follows:
```java
import com.regtab.core.model.ITable;
import com.regtab.core.model.Recordset;
import com.regtab.core.readers.CSVReader;
import com.regtab.core.rtl.interpreter.RTLPattern;
import com.regtab.core.writers.CSVWriter;
import java.io.IOException;

public class Example1 {  
	public static void main(String[] args) throws IOException {  
		// Create the reader to input the source table from CSV  
		CSVReader csvReader = new CSVReader("table.csv");  
		// Create the instance of ITM by reading data from the source table  
		ITable itm = csvReader.read();  
		  
		// Present the RTL-pattern as a string  
		String rtl = "[[ATTR][SKIP]{4}]" +  
			"[[VAL: SCHEMA=COL; RECORD=ROW; JOIN=(UP:@TEXT==THIS@TEXT)]" +  
			"{[ATTR][@BLANK ? SKIP | VAL: SCHEMA=LEFT]}{2}]+";  
		  
		// Apply the RTL-pattern to enrich the instance of ITM by semantics  
		RTLPattern.apply(rtl, itm);  
		  
		// Inference the recordset from the instance of ITM  
		Recordset recordset = itm.extract();  
		  
		// Store the extracted recordset to the CSV file  
		CSVWriter csvWriter = new CSVWriter("recordset.csv");  
		csvWriter.write(recordset);  
	}
}
```

As a result of running this program the extracted recordset (`recordset.csv`) should look like this one:
```java
"Equipment","Type","Full weight","Factory num.","Last M.R. date","Defect data"
"Transformer T-1","DEP-1600","6680","406034","2005","none"
"Transformer T-2","DEP-2400","8480","555879","2005","none"
```

The project of this example can be downloaded from [GitHub](https://github.com/regtab/examples).

### Illustrative example 2

#### Task

There is a source table that lists measurements (minimal, maximal, and average values) in crossing of three dimensions (substance, unit of measurement, and month) as follows:
|                       | Mar               | Aug               |
|-----------------------|-------------------|-------------------|
| Organic nitrogen, %   | 0.05-1.26/0.27    | 0.04-0.32/0.18    |
| Organic carbon, %     | 0.18-2.69/1.39    | 0.40-3.40/1.73    |
| Sulfide sulfur, mg/kg | 0.86-513.10/66.76 | 1.03-313.21/22.08 |

The context of this table indicates that these measurements were obtained from bottom sediment samples collected in 2022 at the location named Solzan.

It is needed to extract all listed measurements and store them into the recordset as follows:
| MIN  | MAX    | AVE   | INDICATOR        | UOM   | YEAR | MONTH | LOC    | SMPL     |
|------|--------|-------|------------------|-------|------|-------|--------|----------|
| 0.05 | 1.26   | 0.27  | Organic nitrogen | %     | 2022 | Mar   | Solzan | Sediment |
| 0.04 | 0.32   | 0.18  | Organic nitrogen | %     | 2022 | Aug   | Solzan | Sediment |
| 0.18 | 2.69   | 1.39  | Organic carbon   | %     | 2022 | Mar   | Solzan | Sediment |
| 0.14 | 3.40   | 1.73  | Organic carbon   | %     | 2022 | Aug   | Solzan | Sediment |
| 0.86 | 513.10 | 66.76 | Sulfide sulfur   | mg/kg | 2022 | Mar   | Solzan | Sediment |
| 1.03 | 313.21 | 22.08 | Sulfide sulfur   | mg/kg | 2022 | Aug   | Solzan | Sediment |

#### Solution

The source table can be matched against the RTL-pattern presented below:
```
[[SKIP][VAL: SCHEMA='MONTH']+][[(VAL: SCHEMA='INDICATOR' ',' VAL: SCHEMA='UOM')]
[(VAL: SCHEMA='MIN' '-' VAL: SCHEMA='MAX' '/' VAL: SCHEMA='AVE';
RECORD=(CELL;(ROW:C0);(COL:R0);'YEAR:2022';'LOC:Solzan';'SMPL:Sediment'))]+]+
```

When the source table is stored in the CSV file (`table.csv`) as follows:
```java
"","Mar","Aug"
"Organic nitrogen, %","0.05-1.26/0.27","0.04-0.32/0.18"
"Organic carbon, %","0.18-2.69/1.39","0.40-3.40/1.73"
"Sulfide sulfur, mg/kg","0.86-513.10/66.76","1.03-313.21/22.08"
```
> Note that this CSV file should be corresponded to the format where all values are wrapped in double quotes, comma is used as the value delimiter, and newline as the row delimiter.

Then the RTL-pattern can be applied in a Java program as follows:
```java
import com.regtab.core.model.ITable;
import com.regtab.core.model.Recordset;
import com.regtab.core.readers.CSVReader;
import com.regtab.core.rtl.interpreter.RTLMatcher;
import com.regtab.core.rtl.interpreter.RTLPattern;
import com.regtab.core.rtl.interpreter.TableMap;
import com.regtab.core.writers.CSVWriter;
import java.io.IOException;

public class Example2 {  
	public static void main(String[] args) throws IOException {  
		// Create the reader to input the source table from CSV  
		CSVReader csvReader = new CSVReader("table.csv");  
		// Create the instance of ITM by reading data from the source table  
		ITable itm = csvReader.read();  
		  
		// Present the RTL-pattern as a string  
		String rtl = "[[SKIP][VAL: SCHEMA='MONTH']+]" + 
			"[[(VAL: SCHEMA='INDICATOR' ',' VAL: SCHEMA='UOM')]" +  
			"[(VAL: SCHEMA='MIN' '-' VAL: SCHEMA='MAX' '/' VAL: SCHEMA='AVE';" +  
			"RECORD=(CELL;(ROW:C0);(COL:R0);'YEAR:2022';'LOC:Solzan';'SMPL:Sediment'))]+]+";  
		  
		// Compile the RTL-pattern  
		RTLPattern pattern = RTLPattern.compile(rtl);  
		assert pattern != null;  
		  
		// Create the matcher of the RTL-pattern  
		RTLMatcher matcher = pattern.matcher();  
		  
		// Match the RTL-pattern with the instance of ITM  
		TableMatch match = matcher.match(itm);  
		  
		// Apply the RTL-pattern to enrich the instance of ITM by semantics  
		match.apply();  
		  
		// Inference the recordset from the instance of ITM  
		Recordset recordset = itm.extract();  
		  
		// Store the extracted recordset to the CSV file  
		CSVWriter csvWriter = new CSVWriter("recordset.csv");  
		csvWriter.write(recordset);  
	}  
}
```

As a result of running this program the extracted recordset (`recordset.csv`) should look like this one:
```java
"AVE","MIN","MAX","INDICATOR","UOM","MONTH","YEAR","LOC","SMPL"  
"0.27","0.05","1.26","Organic nitrogen","%","Mar","2022","Solzan","Sediment"  
"0.18","0.04","0.32","Organic nitrogen","%","Aug","2022","Solzan","Sediment"  
"1.39","0.18","2.69","Organic carbon","%","Mar","2022","Solzan","Sediment"  
"1.73","0.40","3.40","Organic carbon","%","Aug","2022","Solzan","Sediment"  
"66.76","0.86","513.10","Sulfide sulfur","mg/kg","Mar","2022","Solzan","Sediment"  
"22.08","1.03","313.21","Sulfide sulfur","mg/kg","Aug","2022","Solzan","Sediment"
```

The project of this example can be downloaded from [GitHub](https://github.com/regtab/examples).

## Author

Alexey O. Shigarov

## License
Apache License, Version 2.0