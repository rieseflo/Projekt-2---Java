# Gemstone Classifier

This is a Java-based application that classifies gemstones based on various properties using a convolutional neural network.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java 17
- Maven 3.8.4
- Docker

### Building

To build the Docker image, run:

docker build -t rieseflo/gemstone-classifier .

### Running 

To run the Docker container, use:

docker run -p 8080:8080 rieseflo/gemstone-classifier

The application will be accessible at http://localhost:8080.

### Built With

- Java - The programming language used
- Maven - Dependency Management
- Docker - Used for containerization

### Used Data

The used dataset is accessible on Kaggle at https://www.kaggle.com/datasets/fransell/gemstones-images-expanded.


