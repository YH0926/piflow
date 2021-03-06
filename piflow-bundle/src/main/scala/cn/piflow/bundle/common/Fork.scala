package cn.piflow.bundle.common

import cn.piflow.conf._
import cn.piflow.conf.bean.PropertyDescriptor
import cn.piflow.conf.util.{ImageUtil, MapUtil}
import cn.piflow.{JobContext, JobInputStream, JobOutputStream, ProcessContext}

import scala.beans.BeanProperty

class Fork extends ConfigurableStop{

  val authorEmail: String = "xjzhu@cnic.cn"
  val description: String = "Fork data into different stops"
  val inportList: List[String] = List(PortEnum.DefaultPort.toString)
  val outportList: List[String] = List(PortEnum.AnyPort.toString)

  var outports : List[String] = _

  override def setProperties(map: Map[String, Any]): Unit = {
    val outportStr = MapUtil.get(map,"outports").asInstanceOf[String]
    outports = outportStr.split(",").toList
  }

  override def initialize(ctx: ProcessContext): Unit = {

  }

  override def perform(in: JobInputStream, out: JobOutputStream, pec: JobContext): Unit = {
    val df = in.read().cache()
    outports.foreach(out.write(_, df));
  }

  override def getPropertyDescriptor(): List[PropertyDescriptor] = {
    var descriptor : List[PropertyDescriptor] = List()
    val outports = new PropertyDescriptor().name("outports").displayName("outports").description("outports string, seperated by ,.").defaultValue("").required(true)
    descriptor = outports :: descriptor
    descriptor
  }

  override def getIcon(): Array[Byte] = {
    ImageUtil.getImage("icon/common/Fork.png")
  }

  override def getGroup(): List[String] = {
    List(StopGroup.CommonGroup.toString)
  }
}
