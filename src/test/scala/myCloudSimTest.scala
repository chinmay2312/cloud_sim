import org.cloudbus.cloudsim.{DatacenterBroker, Log}
import org.scalatest.FlatSpec

class myCloudSimTest extends FlatSpec {
  //test("myCloudSim.")

  behavior of "createBroker() method"

  it should "return a new object of type DatacenterBroker" in {

    val mcs = myCloudSim
    val cb:Boolean = mcs.main(Array())

    //assert(mcs.createBroker().getClass.getSimpleName == "DatacenterBroker")
    assert(cb)
  }
}
