# Student Planner Application

This is Team AAAT's capstone project. Follow the instructions below to download and run the app.

## Prerequisites

To run this app, you'll need to have the following software installed on your computer:

### If building from source
- [JDK](https://openjdk.org/projects/jdk/20/) (For building from source, version 20 or higher)
- [Node.js](https://nodejs.org/en/) (version 12 or higher)
- [npm](https://www.npmjs.com/) (version 6 or higher)
- [Git](https://git-scm.com/)
- [Maven](https://maven.apache.org/) (version 3.8.1 or higher)

### If using the pre-built binaries
- [Java SE](https://www.oracle.com/cis/java/technologies/downloads/#java20) (version 20 or higher)

## Building from source

To download the code, follow these steps:

1. Go to the [GitHub repository](https://github.com/Team-AAAT/CanvasPlanner) for this app.
2. Click the "Code" button and select "Download ZIP" to download the code as a ZIP file.
3. Extract the ZIP file to a location on your computer.

Alternatively, you can clone the repository using Git:

```git clone git@github.com:Team-AAAT/CanvasPlanner.git```

## Installing dependencies

Before you can run the app, you'll need to install its dependencies. Open a terminal or command prompt and navigate to the directory where you extracted the code.

```cd /path/to/student-planner```

Cd into the `resources` directory:

```cd src/main/resources```

Then, run the following command:

```npm install```

This will install all the necessary dependencies for the app.

### Adding Java backend

Download the jar file from [here](https://pennstateoffice365-my.sharepoint.com/:u:/g/personal/acw5549_psu_edu/EQcQCknTzQNGtNmRxdEMOZoBlOg5gZ3lZolARt3UtJ_7hQ?e=LHTNdP) and add it to the `resources` directory.

if you wanted to run the backend directly from the java classes supplied, use Maven to build the project. 

```mvn clean install```

## Running the app

To run the app, run the following command from the `frontend` directory:

```npm start```


This will start the Electron app. You should see a window appear with the demo GUI.

## Further help

If you run into any issues or have any questions, feel free to [open an issue](https://github.com/Team-AAAT/CanvasPlanner/issues)