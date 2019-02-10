## CS441-Cloud: Homework 1

The purpose of this project is to create multiple cloud simulations using CloudSim and evaluate the executions of applications in cloud datacenters with different characteristics and deployment models.

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
- experiment1.conf uses TimeSharing Policy for Cloudlet scheduling, while experiment2.conf uses SpaceSharing Policy for Cloudlet scheduling 

##Simulation Assembly

- class MySim
	- has relevant methods for creating list of VMs, creating list of cloudlets, creating datacenter with given characteristics, creating the broker entity that assigns VMs to hosts, as well as as calculating the cost for the chosen simulation as determined by a specific formula mentioned below
	- createBroker()
	- createVM()
	- createCloudlet()
	- createDatacenter()
	- getTotalCost()

- .conf files
	- The configurations for both the simulations are present in the files under /src/main/resources folder as experiment1.conf and experiment2.conf

- object myCloudSim6
	- this has the main() method which is the primary thread of any chosen simulation. It maintains sequence of steps for building, starting and then stopping the simulation.

##Evaluation of Simulations
In this project, we use single main class myCloudSim6.scala, and provide a choice of 2 confiuration files as 2 simulations.

The files "experiment1.conf" and "experiment2.conf" have different characteristics for the hosts and the machines.

The metric for evaluation of each simulation has been formulated as the sum of cost per second of each cloudlet added to the actual CPU time occupied by each cloudlet scaled by a factor of 0.3

The existing functionality in CloudSim "getProcessingCost()" considers only bandwidth, as can be verified from the source code in the open-source CloudSim project. Hence, we have implemented our own costing formula, as explained above

We observe that experiment1 yields a cost of 7800, while experiment2 yields a massive cost of 150300. As can be seen, this is a huge difference for the provider as well as for the consumer.

Also, experiment1 finishes execution in 250 time units, while experiment2 consumes 5000 time units

##Conclusion
From the above experiments, it is observed that time-sharing policy combined with better storage and processors on the hosts as well as VMs leads to faster as well as cheaper execution

## Author
* Chinmay Gangal