import org.cloudbus.cloudsim.DatacenterBroker
import org.scalatest.FlatSpec

class myCloudSimTest extends FlatSpec {
  //test("myCloudSim.")

  behavior of "createBroker() method"

  it should "return a new object of type DatacenterBroker" in {
    def f[T](v: T) = v

    assert(f(myCloudSim.createBroker()) == DatacenterBroker)
  }

  /*"createBroker() method"

  should

  "return a new object of type DatacenterBroker" in {
    val mcs = new myCloudSim()
    val broker = mcs.createBroker()

    def f[T](v: T) = v

    assert(f(broker) == DatacenterBroker)
  }*/
}
