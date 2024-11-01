#!/bin/bash

DB_HOST="database"
DB_PORT=9092  # Use the port exposed by H2 on the 'db' VM

# Wait for H2 database to be ready
while ! nc -z $DB_HOST $DB_PORT; do
  echo "Waiting for H2 database to be ready..."
  sleep 5
done

echo "H2 database is ready!"
