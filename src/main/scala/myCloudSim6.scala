import java.text.DecimalFormat
import java.util.Calendar

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

import collection.JavaConverters._
import org.cloudbus.cloudsim._
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.provisioners.{BwProvisionerSimple, PeProvisionerSimple, RamProvisionerSimple}

class MySim {

  def createBroker(brokerName:String):DatacenterBroker ={
    new DatacenterBroker(brokerName)
  }

  def createVM(userId: Int, vms: Int, configFilename: String): List[Vm] = {

    //Creates a container to store VMs. This list is passed to the broker later

    //VM Parameters
    val size = ConfigFactory.load(configFilename).getConfig("vm").getInt("size")    //image size (MB)
    val ram = ConfigFactory.load(configFilename).getConfig("vm").getInt("ram")    //vm memory (MB)
    val mips = ConfigFactory.load(configFilename).getConfig("vm").getInt("mips")
    val bw = ConfigFactory.load(configFilename).getConfig("vm").getInt("bw")
    val pesNumber = ConfigFactory.load(configFilename).getConfig("vm").getInt("pesNum")   //number of cpus
    val vmm = "Chinmay's VMs"    //VMM name

    //create VMs
    //val list:List[Vm] = for(i<-0 to vms) yield new Vm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared)
    val vmRange = 0 until vms toList
    val list = vmRange.map( i => new Vm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared))

    //val vm:Vm = new Vm(0, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared)

    list
  }

  def createCloudlet(userId: Int, cloudlets: Int, configFilename:String): List[Cloudlet] ={

    //cloudlet parameters
    val length = ConfigFactory.load(configFilename).getConfig("cloudlet").getInt("length")
    val fileSize = ConfigFactory.load(configFilename).getConfig("cloudlet").getInt("fileSize")
    val outputSize = ConfigFactory.load(configFilename).getConfig("cloudlet").getInt("outputSize")
    val pesNumber = 1
    val utilizationModel = new UtilizationModelFull()

    val taskRange = 0 until cloudlets toList
    val list:List[Cloudlet] = taskRange.map(i => new Cloudlet(i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel))
    //val list:List[Cloudlet] = for(i<-0 to cloudlets) yield new Cloudlet(i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel)
    list.foreach(_.setUserId(userId))
    list
  }

  def createDatacenter(name: String, hostCount: Int, configFilename: String): Datacenter ={

    //val hostList =List[Host]()

    //val peList:List[Pe] =List[Pe]()

    val mips = ConfigFactory.load(configFilename).getConfig("host").getInt("mips")

    val peList = List(new Pe(0, new PeProvisionerSimple(mips)))

    val ram = ConfigFactory.load(configFilename).getConfig("host").getInt("ram")
    val storage = ConfigFactory.load(configFilename).getConfig("host").getInt("storage")
    val bw = ConfigFactory.load(configFilename).getConfig("host").getInt("bw")

    val hostRange = 0 until hostCount toList
    val hostList:List[Host] = hostRange.map(i=> new Host(i,
      new RamProvisionerSimple(ram),
      new BwProvisionerSimple(bw),
      storage, peList.asJava,
      new VmSchedulerTimeShared(peList.asJava)))

    /*val hostList = List(new Host(hostId,
      new RamProvisionerSimple(ram),
      new BwProvisionerSimple(bw),
      storage, peList.asJava,
      new VmSchedulerTimeShared(peList.asJava)))
*/
    val arch = "x86"
    val os = "Linux"
    val vmm = "Xen"

    val time_zone = 10.0
    val cost = 3.0
    val costPerMem = 0.05
    val costPerStorage = 0.001
    val costPerBw = 0.0
    val storageList=List[Storage]()
    val characteristics =
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

}

object myCloudSim6 {//extends App{

  def main(args: Array[String]):Unit ={

    val ms = new MySim()

    val logger = Logger("myCloudSim6")
    logger.info("Config file chosen: " + args(0))
    val configFilename = args(0)
    val num_user = ConfigFactory.load(configFilename).getConfig("init").getInt("num_user")
    val calendar = Calendar.getInstance()
    val trace_flag = false
    CloudSim.init(num_user, calendar, trace_flag)

    //val datacenter0: Datacenter =
      ms.createDatacenter("Datacenter_0", 1, configFilename)
    //val datacenter1: Datacenter =
      ms.createDatacenter("Datacenter_1",1, configFilename)

    val broker:DatacenterBroker = ms.createBroker("Broker")
    val brokerId:Int = broker.getId

    val vmList: List[Vm] = ms.createVM(brokerId, ConfigFactory.load(configFilename).getConfig("vm").getInt("count"), configFilename)
    val cloudletList = ms.createCloudlet(brokerId, ConfigFactory.load(configFilename).getConfig("cloudlet").getInt("count"), configFilename)

    broker.submitVmList(vmList.asJava)
    broker.submitCloudletList(cloudletList.asJava)

    CloudSim.startSimulation()
    val newList: java.util.List[Cloudlet] = broker.getCloudletReceivedList()
    CloudSim.stopSimulation()
    printCloudletList(newList)

  }

  def printCloudletList(list: java.util.List[Cloudlet]): Unit = {
    Log.printLine("===== OUTPUT =====")
    Log.printLine("Cloudlet ID \t STATUS \t Data center ID \t VM ID \t\t Time \t Start Time \t Finish Time")

    val dft= new DecimalFormat("000.00")

    list.asScala.foreach{cloudlet => {
      Log.print("\t"+cloudlet.getCloudletId+"\t\t")
      if(cloudlet.getCloudletStatus==Cloudlet.SUCCESS)  {
        Log.print("SUCCESS")
        Log.printLine("\t\t\t" + cloudlet.getResourceId + "\t\t\t" + cloudlet.getVmId + "\t\t" + dft.format(cloudlet.getActualCPUTime) + "\t\t" + dft.format(cloudlet.getExecStartTime) + "\t\t" + dft.format(cloudlet.getFinishTime))
      }
    }}
  }

}
