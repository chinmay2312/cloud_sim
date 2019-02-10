import java.util.Calendar
import collection.JavaConverters._

import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim._
import org.scalatest.FlatSpec

class myCloudSimTest extends FlatSpec {
  //test("myCloudSim.")

  val ms = new MySim()

  CloudSim.init(1, Calendar.getInstance(), false)

  behavior of "createDatacenter() method"
  it should "return a new object of type Datacenter" in {
    val cdc:Datacenter = ms.createDatacenter("dc0", 2, "experiment1.conf")
    assert(cdc.getClass.getSimpleName == "Datacenter")
  }

  val dcbName:String = ConfigFactory.load("test1.conf").getConfig("datacenterbroker").getString("name")
  behavior of "createDatacenterBroker() method"
  it should "return a new object of type DatacenterBroker of name:"+dcbName in {
    val dcb = ms.createBroker(dcbName)
    assert(dcb.getClass.getSimpleName=="DatacenterBroker")
    assert(dcb.getName==dcbName)
  }

  val vmCount:Int = ConfigFactory.load("test1.conf").getConfig("vm").getInt("count")
  behavior of "createVM() method"
  it should "return a new List of length "+vmCount+" of element-type Vm" in {
    val vmList:List[Vm] = ms.createVM(0, vmCount, "experiment1.conf")
    val rndm = new scala.util.Random
    assert(vmList.length == vmCount)
    assert(vmList(rndm.nextInt(vmList.length)).getClass.getSimpleName == "Vm")
  }

  val cloudletCount:Int = ConfigFactory.load("test1.conf").getConfig("cloudlet").getInt("count")
  behavior of "createCloudlet() method"
  it should "return a new List of length "+cloudletCount in {
    val cloudletList = ms.createCloudlet(0, cloudletCount, "experiment1.conf")
    assert(cloudletList.length == cloudletCount)
  }
  it should "return a new List of element-type Cloudlet" in {
    val cloudletList = ms.createCloudlet(0, cloudletCount, "experiment1.conf")
    val rndm = new scala.util.Random
    assert(cloudletList(rndm.nextInt(cloudletList.length)).getClass.getSimpleName == "Cloudlet")
  }

  behavior of "MySim class"
  it should "has more total cost for low-performance simulation" in {
    val configFilename1 = "experiment1.conf"
    val configFilename2 = "experiment2.conf"

    val num_user = ConfigFactory.load(configFilename1).getConfig("init").getInt("num_user")
    val calendar = Calendar.getInstance()
    val trace_flag = false
    CloudSim.init(num_user, calendar, trace_flag)

    val ms1 = new MySim()
    ms1.createDatacenter("Datacenter_0", 1, configFilename1)
    ms1.createDatacenter("Datacenter_1",1, configFilename1)
    val broker1:DatacenterBroker = ms1.createBroker("Broker")
    val brokerId:Int = broker1.getId
    val vmList: List[Vm] = ms1.createVM(brokerId, ConfigFactory.load(configFilename1).getConfig("vm").getInt("count"), configFilename1)
    val cloudletList = ms1.createCloudlet(brokerId, ConfigFactory.load(configFilename1).getConfig("cloudlet").getInt("count"), configFilename1)
    broker1.submitVmList(vmList.asJava)
    broker1.submitCloudletList(cloudletList.asJava)
    CloudSim.startSimulation()
    val recList1: java.util.List[Cloudlet] = broker1.getCloudletReceivedList()
    val cost1 = ms1.getTotalCost(recList1)
    CloudSim.stopSimulation()

    val ms2 = new MySim()
    CloudSim.init(num_user, calendar, trace_flag)
    ms2.createDatacenter("Datacenter_0", 1, configFilename2)
    ms2.createDatacenter("Datacenter_1",1, configFilename2)
    val broker2:DatacenterBroker = ms2.createBroker("Broker")
    val brokerId2:Int = broker2.getId
    val vmList2: List[Vm] = ms2.createVM(brokerId2, ConfigFactory.load(configFilename2).getConfig("vm").getInt("count"), configFilename2)
    val cloudletList2 = ms2.createCloudlet(brokerId2, ConfigFactory.load(configFilename2).getConfig("cloudlet").getInt("count"), configFilename2)
    broker2.submitVmList(vmList2.asJava)
    broker2.submitCloudletList(cloudletList2.asJava)
    CloudSim.startSimulation()
    val recList2: java.util.List[Cloudlet] = broker2.getCloudletReceivedList()
    val cost2 = ms2.getTotalCost(recList2)
    CloudSim.stopSimulation()

    assert(cost1<=cost2)
  }

}
