package com.expr.pentaho.benetton

import java.util.{List => JList, Map => JMap}

import javax.xml.xpath.XPathFactory
import org.pentaho.di.core.database.DatabaseMeta
import org.pentaho.di.core.row._
import org.pentaho.di.core.{Counter, RowSet}
import org.pentaho.di.trans._
import org.pentaho.di.trans.step._


class BenettonStep(smi: StepMeta, sdi: StepDataInterface, copyNr: Int, transMeta: TransMeta, trans: Trans) extends BaseStep(smi, sdi, copyNr, transMeta, trans) {
  override def safeModeChecking(row: RowMetaInterface): Unit = {
    logError("CHECKING SAFE MODE NOW... Kind of...")
  }
  val rowSets: JList[RowSet] = getInputRowSets
  logError(rowSets.toString())

  def valOrNull(s: String) = if (s.isEmpty) null else s

  override def init(smi: StepMetaInterface, sdi: StepDataInterface) = {
    super.init(smi, sdi)
  }

  override def processRow(smi: StepMetaInterface, sdi: StepDataInterface): Boolean = {
    val meta = smi.asInstanceOf[BenettonStepMeta]
    val data = sdi.asInstanceOf[BenettonStepData]
    var grp: List[Any] = List()
    logError(this.rowSets.toString)
    var lastRow: Array[Object] = getRow // First row in the group.. need to initialize with something to retrieve `rowMeta`
//    val rowMeta = Option(getInputRowMeta).getOrElse(new RowMeta)
//    val metaList = rowMeta.getValueMetaList
    import scala.collection.JavaConverters._
    for(rowset <- rowSets.asScala) {
      val rowMeta = getInputRowMeta
      logError("Processing Step: " + rowset)
      val row: Array[Object] = getRowFrom(rowset)
      row.foreach(x => logError(Option(x).getOrElse("Null").toString))
    }
    val groupBys = Array("_id")

    false
  }
  
  override def dispose(smi: StepMetaInterface, sdi: StepDataInterface): Unit = {
    super.dispose(smi, sdi)
  }
}

class BenettonStepMeta extends BaseStepMeta with StepMetaInterface {
  override def excludeFromRowLayoutVerification: Boolean = { true }//super.excludeFromRowLayoutVerification()
  var groupFields: List[List[String]] = List(List("Hello", ">", "There"))

  def getGroupFields: List[List[String]] = {
    groupFields
  }
  def getStep(smi: StepMeta, sdi: StepDataInterface, copyNr: Int, transMeta: TransMeta, trans: Trans) =
    new BenettonStep(smi, sdi, copyNr, transMeta, trans)

  def getStepData = new BenettonStepData

  def setDefault(): Unit = {  }

  // override def check(remarks: JList[CheckResultInterface], meta: TransMeta, stepMeta: StepMeta, prev: RowMetaInterface, input: Array[String], output: Array[String], info: RowMetaInterface) = {
  //   // TODO: Implement a check
  // }

  // override def getFields(inputRowMeta: RowMetaInterface, name: String, info: Array[RowMetaInterface], nextStep: StepMeta, space: VariableSpace): Unit = {
  //   val v = valueMeta()
  //   v.setOrigin(name)
  //   inputRowMeta.addValueMeta(v)
  //   logBasic("outgoing valueMeta:" + inputRowMeta.toString())
  // }
  private def getGroupFieldXML: String = {
    var out: String = "<groupFields>"
    groupFields.foreach(gfList => out += (s"<groupField>" +
      s"<streamACol>${gfList.head}</streamACol>" +
      s"<operator>${gfList(1)}</operator>" +
      s"<streamBCol>${gfList(2)}</streamBCol></groupField>"))
    out += "</groupFields>"
    out
  }
   override def getXML:String = {
     println("WRITING BENETTON TO XML")
     s"<settings>$getGroupFieldXML</settings>"
   }
   override def loadXML(node: org.w3c.dom.Node, databases: JList[DatabaseMeta], counters: JMap[String, Counter]): Unit = {
     val xpath = XPathFactory.newInstance.newXPath
     val gf = xpath.evaluate("//settings/groupFields", node)
     println(s"Loading XML from ${gf.toString}")
//
//     token = xpath.evaluate("//settings/groupFields", node)
//     projectId = xpath.evaluate("//settings/projectId", node)
//     queue = xpath.evaluate("//settings/queue", node)
//     outputField = xpath.evaluate("//settings/outputField", node)

//     println(s"Loaded params: token = $token, projectId = $projectId, queue = $queue, outputField = $outputField")
   }

  // override def readRep(rep: Repository, stepId: ObjectId, databases: JList[DatabaseMeta], counters: JMap[String, Counter]): Unit = {
  //   throw new UnsupportedOperationException("readRep")
  // }

  // override def saveRep(rep: Repository, transformId: ObjectId, stepId: ObjectId): Unit = {
  //   throw new UnsupportedOperationException("readRep")
  // }
}


class BenettonStepData extends BaseStepData with StepDataInterface
