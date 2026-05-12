#!/usr/bin/env bash
set -e

mkdir -p out
find src/main/java -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out br.edu.mca.grafos.App
