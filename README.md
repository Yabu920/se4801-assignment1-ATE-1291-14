# se4801-assignment1-ATE-1291-14



Name: YEABSRA ABERA YIMAM 
ID: ATE/1291/14  



#How to build the project

Run the following command in terminal:

./mvnw.cmd clean install  // this will compile the project and download all dependency

# How to run the applicatioin
./mvnw.cmd spring-boot:run    // the application will start on https://localhost:8080

#How to run test 
./mvnw.cmd test

#API endpoints 

GET /api/products?page=0&size=10   //Get all Product
GET /api/products/{id}             // Get product by ID
POST /api/products                  // Create Products
GET /api/products/search?keyword=lap&maxPrice=1500   //Search Products
PATCH /api/products/{id}/stock      // Update Products
