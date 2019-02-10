import java.util.Calendar

import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.{Datacenter, DatacenterBroker, Log}
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

}
