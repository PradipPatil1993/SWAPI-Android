# SWAPI API Sample Android Application

This is Sample android application for SWAPI api implementation 

## Project UI Flow
1. Sample project has 2 screens 
2. On First screen user can search the star war character/people or selected from dropdown option
3. On Second screen user can see more details for selected character.

## How it's work internally ?
1. https://swapi.dev/ - Free api used for sample example, find more details on link
2. To Fetch the Characters, Character details and the Films for the selected charactes are get fetched using api's 
3. Before loading first screen all the available characters get fetched and then dispayed to used for Search and to choose for favourite character to see more details 
4. To see the films details by the favourite character again api's get called then can be shown on details screen

## Technology stack 
1. **Language** - Kotlin
2. **Architecture** - MVVM
3. **Dependency Injection** - Dagger's Hilt
4. **Android Navigation** - Navigation graph
5. **Layout Dresigning** - ConstraintLayout
6. **Async Operation/Threading** - Coroutine
7. **Testing** - Unit Test

## Third party libraries used
1. UI Design - CardView, NavigationGraph, MaterialDesign
1. Networking - Retrofit 
2. Testing - JUnit, Mockito, Mockk


## Video Demo
 <img src="https://user-images.githubusercontent.com/11188728/180207098-ceafc56c-b4de-4aa7-b5f7-7829ee5c594f.gif" width="300" height="640">

