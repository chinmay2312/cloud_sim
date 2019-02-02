
import java.util.Calendar
//import java.util.List
import java.text.DecimalFormat

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import collection.JavaConverters._
import org.cloudbus.cloudsim._
import org.cloudbus.cloudsim.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.provisioners.{BwProvisionerSimple, PeProvisionerSimple, RamProvisionerSimple}

object myCloudSim {

    //private val cloudletList=List[Cloudlet]()
    //private val vmList=List[Vm]()

    def main(args: Array[String]) {
        println("Hello, world!")
        val num_user =1
        val calendar = Calendar.getInstance()
        val trace_flag = false

        CloudSim.init(num_user,calendar,trace_flag)

        val datacenter0:Datacenter = createDatacenter("Datacenter_0")

        val broker:DatacenterBroker=createBroker()
        val brokerId:Int = broker.getId()

        val vmid =0
        val mips = 1000
        val size:Long = 10000
        //val ram = 512
        val ram = ConfigFactory.load().getConfig("vm").getInt("ram")
        Log.printLine("ram size = "+ram)
        val bw = 1000
        val pesNumber = 1
        val vmm:String = "Xen"

        val vm:Vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared())

        val vmList:List[Vm]=List(vm)

        broker.submitVmList(vmList.asJava)

        val id =0
        val length = 400000
        val fileSize = 300
        val outputSize = 300
        val utilizationModel: UtilizationModel = new UtilizationModelFull()

        val cloudlet:Cloudlet =new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel)
        cloudlet.setUserId(brokerId)
        cloudlet.setVmId(vmid)

        val cloudletList:List[Cloudlet] = List(cloudlet)

        broker.submitCloudletList(cloudletList.asJava)

        CloudSim.startSimulation()
        CloudSim.stopSimulation()

        val newList:java.util.List[Cloudlet] = broker.getCloudletReceivedList()
        printCloudletList(newList)
    }

    def createDatacenter(name: String): Datacenter ={

      //val hostList =List[Host]()

      //val peList:List[Pe] =List[Pe]()

        val mips:Int = 1000

        val peList:List[Pe] = List(new Pe(0, new PeProvisionerSimple(mips)))
        Log.printLine("peList size="+peList.length)
        //for(Pe pe:peList)

        val hostId:Int = 0
        val ram:Int = ConfigFactory.load().getConfig("host").getInt("ram")
        val storage:Long = 1000000
        val bw:Int = 10000

        val hostList:List[Host]= List(new Host(hostId,
            new RamProvisionerSimple(ram),
            new BwProvisionerSimple(bw),
            storage, peList.asJava,
            new VmSchedulerTimeShared(peList.asJava)))

        val arch:String = "x86"
        val os:String = "Linux"
        val vmm:String = "Xen"

        val time_zone:Double = 10.0
        val cost:Double = 3.0
        val costPerMem:Double = 0.05
        val costPerStorage:Double = 0.001
        val costPerBw:Double = 0.0
        val storageList=List[Storage]()
        val characteristics:DatacenterCharacteristics =
            new DatacenterCharacteristics(arch, os, vmm, hostList.asJava, time_zone, cost, costPerMem, costPerStorage, costPerBw)

        val datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList.asJava), storageList.asJava, 0)
        /*try   {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList.asJava), storageList.asJava, 0)
        }
        catch {
            case e:Exception => e.printStackTrace()
        }*/
        //datacenter = new Datacenter(name, );
        //datacenter = 2
        datacenter
  }

    def createBroker():DatacenterBroker ={
        new DatacenterBroker("Broker")
    }

    def printCloudletList(list: java.util.List[Cloudlet]): Unit = {
        val size = list.size()
        Log.printLine()
        //Log.printLine("size = "+size)
        Log.printLine("===== OUTPUT =====")
        Log.printLine("Cloudlet ID \t STATUS \t Data center ID \t VM ID \t\t Time \t Start Time \t Finish Time")

        val dft= new DecimalFormat("000.00")

        list.asScala.foreach{cloudlet => {
            Log.print("\t"+cloudlet.getCloudletId+"\t\t")
            if(cloudlet.getCloudletStatus()==Cloudlet.SUCCESS)  {
                Log.print("SUCCESS")

                Log.printLine("\t\t\t" + cloudlet.getResourceId + "\t\t\t" + cloudlet.getVmId + "\t\t" + dft.format(cloudlet.getActualCPUTime) + "\t\t" + dft.format(cloudlet.getExecStartTime) + "\t\t" + dft.format(cloudlet.getFinishTime))
            }
        }}

    }
}