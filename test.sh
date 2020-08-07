#!/bin/bash

for i in $(seq 50) ;
do
  curl http://localhost:8080/
done
