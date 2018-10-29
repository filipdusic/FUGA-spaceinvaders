# InvaderDetector Application

This is Spring Boot Console application for detecting space invaders in radar image

**Table of Content**

- [Introduction](#introduction)
- [How it works](#how-it-works)
- [Improvement points](#improvement-points)
- [Building application](#build-the-application)
- [Run the application](#run-the-application)

## Introduction

The program can detect matches of all 'o' characters on the invader image with the 'o' characters on the radar image segments. The process of detection divides the radar image into a sub-images of invader image size to perform a check. 'o' represents a character that defines a particular shape on invader image, so it is considered as a matching character. The matching does not have to be full, it is possible to adjust the accuracy (precision). The following terms introduced by detection process are noise and junk. When there is an 'o' character at a particular position of the radar image segment, and if it is not located at the appropriate position of the invader image, that 'o' is treated as a noise or a junk. If one of the neighboring positions of the invader image (bottom, top, left, right, bottom-left, bottom-right, top-left, top-right) is 'o', it is considered that the current 'o' (located on radar image segment) is noise. If there is not a single 'o' in the neighboring positions, the current 'o' is a junk. 


## How it works

The program as an input argument receives a minimum matching percentage. This argument indicates the minimum required matching of the invader image with the radar image segment. If an input argument is not specified, the detection process uses a predefined value of 80%, which can be configured in the application.properties file. The program loads files (invaders and radar image) located in resources directory. Paths to those files are specified in application.properties. After that, program transforms each image to Matrix object and performs detection process using given (or predefined) matching percentage for accuracy. For each sub-matrix of the radar matrix that fulfills the matching condition, the program will record a detection result. Each detection result contains the following informations:
        - Segment of radar matrix that matches the invader (with coordinates)
        - Description
        - Matching percentage
        - Noise
        - Junk
        - Colored picture of segment on radar image displayed in console output

## Buld the application

Build the application using Maven: `mvn clean package`

## Run the application

Run the application: `mvn spring-boot:run` (Default percentage (>= 80%) will be used for matching accuracy)
Run the application with input arguments: `mvn spring-boot:run -Dspring-boot.run.arguments=63.0` (Matching percetange will be set to >= 63%)