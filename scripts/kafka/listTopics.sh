#!/bin/bash
docker exec kafka bash /bin/kafka-topics --list --bootstrap-server kafka:9092
