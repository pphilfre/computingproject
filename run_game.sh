#!/bin/bash

# Create saves directory
mkdir -p saves

# Build the project with Maven
echo "Building project..."
mvn clean package

if [ $? -eq 0 ]; then
    echo "Build successful! Running game..."
    echo
    # Run the game
    java -jar target/text-adventure-engine-1.0-SNAPSHOT-jar-with-dependencies.jar
else
    echo "Build failed. Please check the errors above."
fi
