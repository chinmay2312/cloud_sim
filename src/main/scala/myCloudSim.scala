import java.util
import java.util.{Calendar, LinkedList}

import scala.collection.{JavaConverters, mutable}
import com.typesafe.scalalogging.Logger
import com.typesafe.config
import org.cloudbus.cloudsim.Cloudlet
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared
import org.cloudbus.cloudsim.Datacenter
import org.cloudbus.cloudsim.DatacenterBroker
import org.cloudbus.cloudsim.DatacenterCharacteristics
import org.cloudbus.cloudsim.Host
import org.cloudbus.cloudsim.Log
import org.cloudbus.cloudsim.Pe
import org.cloudbus.cloudsim.Storage
import org.cloudbus.cloudsim.UtilizationModel
import org.cloudbus.cloudsim.UtilizationModelFull
import org.cloudbus.cloudsim.Vm
import org.cloudbus.cloudsim.VmAllocationPolicySimple
import org.cloudbus.cloudsim.VmSchedulerTimeShared
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple

import scala.collection.JavaConverters._
import scala.collection.convert.WrapAsJava

object myCloudSim{

    def main(args: Array[String]): Unit = {
        val numUsers = 1

        val cal = Calendar.getInstance()

        CloudSim.init(numUsers, cal, false)

        val datacenter = createDatacenter("Datacenter1")
        val datacenterbroker = new DatacenterBroker("Broker")
        val brokerID = datacenterbroker.getId()

        val vmid = 0
        val mips = 100
        val size = 10000
        val ram = 512
        val bw = 1000
        val pesNumber = 1
        val vmm = "Xen"
        val vmList = java.util.Arrays.asList(new Vm(vmid, brokerID, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared))

        datacenterbroker.submitVmList(vmList)

        val id = 0
        val length = 400000
        val fileSize = 300
        val outputSize = 300
        val utilizationModel = new UtilizationModelFull()

        val cloudlet = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel)
        cloudlet.setUserId(brokerID)
        cloudlet.setVmId(vmid)

        val cloudletList = java.util.Arrays.asList(cloudlet)

        datacenterbroker.submitCloudletList(cloudletList)

        CloudSim.startSimulation()

        CloudSim.stopSimulation()

        val indent = "    "
        Log.printLine()
        Log.printLine("========== OUTPUT ==========")
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time")
        val messages = datacenterbroker.getCloudletReceivedList()
        messages.forEach { (e : Cloudlet) => if(e.getCloudletStatus() == Cloudlet.SUCCESS)
            {
                Log.printLine(indent + e.getCloudletId + indent + indent + "SUCCESS" + indent + indent + e.getResourceId() + indent + indent + indent + e.getVmId() + indent + indent + e.getActualCPUTime() + indent + indent + e.getExecStartTime() + indent + indent + e.getFinishTime())
            }
          else
            {
                Log.printLine(indent + e.getCloudletId + indent + indent + "FAILURE" + indent + indent + e.getResourceId() + indent + indent + indent + e.getVmId() + indent + indent + e.getActualCPUTime() + indent + indent + e.getExecStartTime() + indent + indent + e.getFinishTime())
            }
        }

        Log.printLine("Finished")


    }
    def createDatacenter(datacenterName : String) : Datacenter = {
        //Creating the machine and all its properties
        val mips = 2000
        val peList = java.util.Arrays.asList(new Pe(0, new PeProvisionerSimple(mips)))

        val hostId = 0
        val ram = 2048
        val storage = 1000000
        val bw = 10000

        val hostList = java.util.Arrays.asList(
            new Host(
                hostId,
                new RamProvisionerSimple(ram),
                new BwProvisionerSimple(bw),
                storage,
                peList,
                new VmSchedulerTimeShared(peList)
            )
        )
        //Finished Machine

        //Creating the Datacenter characteristics object that stores the datacenter's properties
        val arch = "x86"
        val os = "Linus"
        val vmm = "Xen"
        val time_zone = 10.0
        val cost = 3.0
        val costPerMem = 0.05
        val costPerStorage = 0.001
        val costPerBw = 0.1

        val storageList = new util.LinkedList[Storage]
        // we are not adding SAN
        val datacenterChar = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw)
        //Finished datacenter characteristics

        //Creating the datacenter
        val datacenter = new Datacenter(datacenterName, datacenterChar, new VmAllocationPolicySimple(hostList), storageList, 0)

        datacenter
    }
}


