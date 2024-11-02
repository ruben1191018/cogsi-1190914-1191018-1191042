#!/bin/bash

# Path to the Spring Boot application's properties file
APP_PROPERTIES_PATH="cogsi-1190914-1191018-1191042/ca2/part2/app/src/main/resources/application.properties"

# Create the directory if it doesn't exist
mkdir -p "$(dirname "$APP_PROPERTIES_PATH")"

# Create or overwrite the application.properties file with H2 server mode settings
cat <<EOL > "$APP_PROPERTIES_PATH"
# H2 Database settings for server mode
spring.datasource.url=jdbc:h2:tcp://localhost:1011/./test
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
EOL

echo "Configured application.properties for Spring Boot application to connect to H2 server on db VM."
