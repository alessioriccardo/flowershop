# Flowershop

## Installation
Open with an IDE and run As Spring Boot Application from main Class FlowerShopApplication

## Use
You can run the application and send a REST call like:

POST http://localhost:8083/flowershop/sendInputFile
Add a Multipart file as Body of the call linking the inputFile you want to process.

## Test 
A Test class is included in the src/test/java folder.
An input file is included in src/test/resource folder and has this syntax:
10 R12
15 L09
13 T58

## License

[MIT](https://choosealicense.com/licenses/mit/)