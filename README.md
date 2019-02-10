## CS441-Cloud: Homework 1

##Prerequisites
* Scala 2.12
* SBT version 1.2.8

## Steps to run
Since this project uses SBT, the command for running simulation(s) in this project starts with "sbt" if running through regular command line

The command is 

    sbt clean compile "runMain myCloudSim6 experiment1.conf"

Explanation: 

- "runMain" is used to specify a specific main class to be run, when mu;tiple main clases exist
- Here, "myCloudSim6" is the desired class to be run
- The parameter "experiment1.conf" is the input configuration file chosen by the user. To choose the 2nd option for config file i.e. "experiment2.conf", simply replace the parameter in the following manner:

	`sbt clean compile "runMain myCloudSim6 experiment2.conf"`

## Steps to test
We have 5 unit tests & 1 integration test included in this project, in the file "myCloudSimTest.scala"

The unit tests verify the following:

- createCloudlet method method returns list of given length
- createDatacenter method returns new Datacenter object
- createDatacenterBroker method returns new DatacenterBroker object of given name
- createVm method returns a list of VMs, and the list is of given length
- createCloudlet method method returns a list of Cloudlets

The integration test verifies the following:

- For the same cloudlets (task size), the simulation with less hardware invokes more cost than the other simulation with hardware better performance
 
This file uses the configuration file "test1.conf" as a resource
to run the tests included in "myCloudSimTest.scala", simply run the following in command line:

`sbt clean compile test`


## Configuration files

Similarities:

- Number of users = 1
- Cloudlet
	- Number of cloudlets = 100
	- Length of each cloudlet = 10000
	- Input file size in each cloudlet = 300
	- Output file size of each cloudlet = 300

Summary of Differences:

- experiment1.conf has much more host storage,bandwidth, RAM and speed (i.e. MIPS) as compared to experiment2.conf
- experiment1.conf has slightly better VM characteristics (speed in MIPS, size, RAM) as compared to experiment2.conf

## Author
* Chinmay Gangal