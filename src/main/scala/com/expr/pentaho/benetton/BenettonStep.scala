package com.expr.pentaho.benetton

import java.util.{List => JList, Map => JMap}

import org.pentaho.di.core.database.DatabaseMeta
import org.pentaho.di.core.row._
import org.pentaho.di.core.{Counter, RowSet}
import org.pentaho.di.trans._
import org.pentaho.di.trans.step._

import scala.collection.mutable.ListBuffer
import scala.xml.XML

class BenettonStep(smi: StepMeta, sdi: StepDataInterface, copyNr: Int, transMeta: TransMeta, trans: Trans) extends BaseStep(smi, sdi, copyNr, transMeta, trans) {
  override def safeModeChecking(row: RowMetaInterface): Unit = {
    logError("CHECKING SAFE MODE NOW... Kind of...")
  }
  val rowSets: JList[RowSet] = getInputRowSets

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
  val rowNumIdx = 0
  val streamAIdx = 1
  val negationIdx = 2
  val operatorIdx = 3
  val streamBIdx = 4
//  def getIncomingFields: Array[]
//  val rowSets =
//  val rmA: RowSet = rowSets(0)
//  val rmB: RowSet = rowSets(1)
//  rmA.getRowMeta
  def getGroupFields: List[List[String]] = {
    groupFields
  }
  def getStep(smi: StepMeta, sdi: StepDataInterface, copyNr: Int, transMeta: TransMeta, trans: Trans) =
    new BenettonStep(smi, sdi, copyNr, transMeta, trans)

  def getStepData = new BenettonStepData

  def setDefault(): Unit = {  }

  private def getGroupFieldXML: String = {
    println("WRITING XML:")
    var out: String = "<groupFields>"
    groupFields.foreach(gfList => out += (s"<groupField>" +
      s"<streamACol>${gfList(streamAIdx)}</streamACol>" +
      s"<negation>${gfList(negationIdx)}</negation>" +
      s"<operator>${gfList(operatorIdx)}</operator>" +
      s"<streamBCol>${gfList(streamBIdx)}</streamBCol></groupField>"))
    out += "</groupFields>"
    println(out)
    out
  }
   override def getXML:String = {
     println("WRITING BENETTON TO XML")
     s"<settings>$getGroupFieldXML</settings>"
   }

    import java.io.StringWriter
    import javax.xml.transform.{OutputKeys, TransformerFactory}
    import javax.xml.transform.dom.DOMSource
    import javax.xml.transform.stream.StreamResult

    private def nodeToString(node: org.w3c.dom.Node) = {
      val tf = TransformerFactory.newInstance
      val transformer = tf.newTransformer
      transformer.setOutputProperty(OutputKeys.INDENT, "yes")
      val writer = new StringWriter
      transformer.transform(new DOMSource(node), new StreamResult(writer))
      writer.toString
    }
   override def loadXML(node: org.w3c.dom.Node, databases: JList[DatabaseMeta], counters: JMap[String, Counter]): Unit = {
     val n = XML.loadString(nodeToString(node))
     val gfNodes = n \\ "groupField"

     println(s"LOAD BENETTON XML: $gfNodes")
     var tmpGroupFields = new ListBuffer[List[String]]
     var i = 1
     for(gfNode <- gfNodes) {
        tmpGroupFields += scala.List(
          i.toString,
          (gfNode \ "streamACol").head.text,
          (gfNode \ "negation").head.text,
          (gfNode \ "operator").head.text,
          (gfNode \ "streamBCol").head.text
        )
       i += 1
     }
     groupFields = tmpGroupFields.toList
   }

  // override def readRep(rep: Repository, stepId: ObjectId, databases: JList[DatabaseMeta], counters: JMap[String, Counter]): Unit = {
  //   throw new UnsupportedOperationException("readRep")
  // }

  // override def saveRep(rep: Repository, transformId: ObjectId, stepId: ObjectId): Unit = {
  //   throw new UnsupportedOperationException("readRep")
  // }
}


class BenettonStepData extends BaseStepData with StepDataInterface
