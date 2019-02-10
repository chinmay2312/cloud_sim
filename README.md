## CS441-Cloud: Homework 1

## Steps to run
Since this project uses SBT, the command for running simulation(s) in this project starts with "sbt" if running through regular command line

The command is 
    sbt clean compile "runMain myCloudSim6 default.conf"

Explanation: 

- "runMain" is used to specify a specific main class to be run, when mu;tiple main clases exist
- Here, "myCloudSim6" is the desired class to be run
- The parameter "default.conf" is the input configuration file chosen by the user. To choose the 2nd option for config file i.e. "experiment.conf", simply replace the parameter in the following manner:
	sbt clean compile "runMain myCloudSim6 experiment.conf"

## Configuration files
###default.conf
* 100 cloudlets
###experiment.conf
* 500 cloudlets

## Author
* Chinmay Gangal