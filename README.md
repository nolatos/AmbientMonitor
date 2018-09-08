AmbientMonitor
## Running the application
To run the application, please first make sure its extension is "jar". If not, please rename it such. After that, just like any jar, either double click it, or navigate on the command line into the directory containing the jar, then type java -jar AmbientMonitor.jar

Once this is done, please type the name of the data file into the text field, and press load to load the data.
The application can now make a prediction, though cross-validating it should make it more accurate. To cross validate, simply select a number in the drop-down menu, then click validate.

When you want to, you can press predict, and predictions will be shown for the next ten minute interval.

## Algorithm
After some experimentation, I decided that the most accurate model is simply to project the previous n values' trend into the next one, where n is an integer defaulted at 5, but can change with validation. The difference is simply calculated with an exponential weighted moving average, of the form estimate = (1 - a) * estimate + a * sample, where a is a double who's value is defaulted at 0.9, but who's value will change with cross-validation. 
