import java.util.Calendar

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

  val dcbName = ConfigFactory.load("test1.conf").getConfig("datacenterbroker").getString("name")
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
    val rndm = new scala.util.Random
    assert(cloudletList.length == cloudletCount)
  }
  it should "return a new List of element-type Cloudlet" in {
    val cloudletList = ms.createCloudlet(0, cloudletCount, "experiment1.conf")
    val rndm = new scala.util.Random
    assert(cloudletList(rndm.nextInt(cloudletList.length)).getClass.getSimpleName == "Cloudlet")
  }

}
