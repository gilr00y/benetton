package com.expr.pentaho.benetton

import org.eclipse.swt.SWT
import org.eclipse.swt.events._
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets.{Listener, _}
import org.pentaho.di.core.Const
import org.pentaho.di.core.exception.KettleException
import org.pentaho.di.trans._
import org.pentaho.di.trans.step._
import org.pentaho.di.ui.core.dialog.ErrorDialog
import org.pentaho.di.ui.core.widget.{ColumnInfo, TableView}
import org.pentaho.di.ui.trans.step._

import scala.collection.mutable.ListBuffer


class BenettonStepDialog(parent: Shell, m: Object, transMeta: TransMeta, stepName: String)
    extends BaseStepDialog(parent, m.asInstanceOf[BaseStepMeta], transMeta, stepName)
    with StepDialogInterface {

  this.shell = parent

  private[this] val stepMeta = m.asInstanceOf[StepMetaInterface]
  private[this] val ourMeta = m.asInstanceOf[BenettonStepMeta]

  val middle = props.getMiddlePct
  val margin = Const.MARGIN

  import org.pentaho.di.core.Const

  def open: String = {
    val parent = getParent
    val display = parent.getDisplay
    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN)
    props.setLook(shell)
    setShellImage(shell, ourMeta)
    val lsMod = new ModifyListener() {
      override def modifyText(e: ModifyEvent): Unit = {
        ourMeta.setChanged
      }
    }
    backupChanged = ourMeta.hasChanged
    val formLayout = new FormLayout
    formLayout.marginWidth = Const.FORM_MARGIN
    formLayout.marginHeight = Const.FORM_MARGIN
    shell.setLayout(formLayout)
    shell.setText("Benetton")
    val middle = props.getMiddlePct
    val margin = Const.MARGIN
    // Stepname line
    val stepnameLabel = new Label(shell, SWT.RIGHT)
    stepnameLabel.setText("Step Name")
    props.setLook(stepnameLabel)
    val stepnameLabelLayout = new FormData
    stepnameLabelLayout.left = new FormAttachment(0, 0)
    stepnameLabelLayout.right = new FormAttachment(middle, -margin)
    stepnameLabelLayout.top = new FormAttachment(0, margin)
    stepnameLabel.setLayoutData(stepnameLabelLayout)
    val stepnameField = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER)
    stepnameField.setText(stepname)
    props.setLook(stepnameField)
    stepnameField.addModifyListener(lsMod)
    val stepnameFieldLayout = new FormData
    stepnameFieldLayout.left = new FormAttachment(middle, 0)
    stepnameFieldLayout.top = new FormAttachment(0, margin)
    stepnameFieldLayout.right = new FormAttachment(100, 0)
    stepnameField.setLayoutData(stepnameFieldLayout)

    val groupLabel = new Label(shell, SWT.NONE)
    groupLabel.setText("JOIN Conditions")
    props.setLook(groupLabel)
    val groupLabelLayout = new FormData
    groupLabelLayout.left = new FormAttachment(0, 0)
    groupLabelLayout.top = new FormAttachment(stepnameField, margin)
    groupLabel.setLayoutData(groupLabelLayout)
    val numGroupCols = 4 // Stream A, negation, operator, Stream B
    val numGroupRows = Option(ourMeta.getGroupFields.length).getOrElse(1)
    val colArr = new Array[ColumnInfo](numGroupCols)
    colArr(0) = new ColumnInfo("Stream A Col", ColumnInfo.COLUMN_TYPE_CCOMBO, Array[String](""), false)
    colArr(1) = new ColumnInfo("Negation", ColumnInfo.COLUMN_TYPE_CCOMBO, Array[String](""), false)
    colArr(2) = new ColumnInfo("Operator", ColumnInfo.COLUMN_TYPE_CCOMBO, Array[String](""), false)
    colArr(3) = new ColumnInfo("Stream B Col", ColumnInfo.COLUMN_TYPE_CCOMBO, Array[String](""), false)
    val groupTbl = new TableView(transMeta, shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, colArr, numGroupRows, lsMod, props)
    val getFieldBtnA = new Button(shell, SWT.PUSH)
    val getFieldBtnB = new Button(shell, SWT.PUSH)
    getFieldBtnA.setText("Get Stream A fields.")
    getFieldBtnB.setText("Get Stream B fields.")
    val getFieldBtnALayout = new FormData
    val getFieldBtnBLayout = new FormData
    getFieldBtnALayout.top = new FormAttachment(groupTbl, margin)
    getFieldBtnBLayout.top = new FormAttachment(getFieldBtnA, margin)
    getFieldBtnALayout.right = new FormAttachment(100, 0)
    getFieldBtnBLayout.right = new FormAttachment(100, 0)
    getFieldBtnA.setLayoutData(getFieldBtnALayout)
    getFieldBtnB.setLayoutData(getFieldBtnBLayout)
    val groupTblLayout = new FormData
    groupTblLayout.left = new FormAttachment(0, 0)
    groupTblLayout.top = new FormAttachment(groupLabel, margin)
    groupTblLayout.right = new FormAttachment(getFieldBtnA, -margin)
    groupTblLayout.right = new FormAttachment(getFieldBtnB, -margin)
    groupTblLayout.bottom = new FormAttachment(45, 0)
    groupTbl.setLayoutData(groupTblLayout)

    // Search the fields in the background
    val runnable = new Runnable() {
      override def run(): Unit = {
        val stepMeta = transMeta.findStep(stepname)
        if (stepMeta != null) try {
          val row = transMeta.getPrevStepFields(stepMeta)
          // Remember these fields...
          var i = 0
          while ( {
            i < row.size
          }) {
//            ourMetaFields.put(row.getValueMeta(i).getName, Integer.valueOf(i))

            {
              i += 1; i - 1
            }
          }
//          setComboBoxes
        } catch {
          case e: KettleException =>
            logError(e.toString)
        }
      }
    }
    new Thread(runnable).start()
    // THE BUTTONS
    wOK = new Button(shell, SWT.PUSH)
    wOK.setText("OK")
    wCancel = new Button(shell, SWT.PUSH)
    wCancel.setText("Cancel")
    setButtonPositions(Array(wOK, wCancel), margin, null)

    def ok(): Unit = {
      val groupsize: Int = groupTbl.nrNonEmpty
      var tmpGroupFields = new ListBuffer[scala.List[String]]
      for(i <- 0 until groupsize) {
        val item: Array[Object] = groupTbl.getRow(i).getData;

        tmpGroupFields.+=(scala.List(
          item(ourMeta.rowNumIdx).toString(),
          item(ourMeta.streamAIdx).toString,
          item(ourMeta.negationIdx).toString,
          item(ourMeta.operatorIdx).toString,
          item(ourMeta.streamBIdx).toString
        ))
      }
      // Stamp ListBuffer to List, assign to BenettonStepMeta
      ourMeta.groupFields = tmpGroupFields.toList

      stepname = stepnameField.getText
      dispose()
    }
    // Add listeners
    val lsOK = new Listener() {
      def handleEvent(e: Event): Unit = {
        ok()
      }
    }

    def popInputFields(streamIdx: Int): Unit = {
      try {
        val prevSteps = transMeta.getPrevSteps(ourMeta.getParentStepMeta)
        val prev0 = prevSteps(0).toString
        val prev1 = prevSteps(1).toString
        val stepFields0 = transMeta.getStepFields(prev0)
        // TODO: Make stepfields1 accessible through dropdown in UI.
        val stepFields1 = transMeta.getStepFields(prev1)

        if (stepFields0 != null && !stepFields0.isEmpty) {
          BaseStepDialog.getFieldsFromPrevious(
            stepFields0, groupTbl, 1, Array[Int](1),
            new Array[Int](1), -1, -1, null
          )
        }
      } catch {
        case ke: KettleException => new ErrorDialog(shell, "Kettle Error", ke.toString, ke)
        case e: Exception => new ErrorDialog(shell, "Generic Error", e.toString, e)
      }
    }

    val lsGetA = new Listener() {
      def handleEvent(e: Event): Unit = {
        println("LSGET EVENT")
//        val rowSets: JList[RowSet] = transMeta.getPrevStepNames(ourMeta)
//        val streamMeta = ourMeta.
        popInputFields(0)
      }
    }

    val lsGetB = new Listener() {
      def handleEvent(e: Event): Unit = {
        println("LSGET EVENT")
        popInputFields(streamIdx=1)
      }
    }

    val lsCancel = new Listener() {
      def handleEvent(e: Event): Unit = {
        println("LSCANCeL EVENT")
//        cancel
      }
    }
    wOK.addListener(SWT.Selection, lsOK)
    getFieldBtnA.addListener(SWT.Selection, lsGetA)
    getFieldBtnB.addListener(SWT.Selection, lsGetB)
    wCancel.addListener(SWT.Selection, lsCancel)
    lsDef = new SelectionAdapter() {
      override def widgetDefaultSelected(e: SelectionEvent): Unit = {
//        ok
      }
    }




    stepnameField.addSelectionListener(lsDef)
    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener(new ShellAdapter() {
      override def shellClosed(e: ShellEvent): Unit = {
        println("SHELL CLOSED")
//        cancel
      }
    })
    // Set the shell size, based upon previous time...
    setSize()
    // Sets table entries with XML-parsed values
    def popData(): Unit = {
      val groupFields = ourMeta.getGroupFields
      for(rowIdx <- 0 until groupFields.length) {
        val item: TableItem = groupTbl.table.getItem(rowIdx)
        for(colIdx <- 0 until groupFields(rowIdx).length) {
          item.setText(colIdx, groupFields(rowIdx)(colIdx))
        }
      }
    }
    popData()
    ourMeta.setChanged(backupChanged)
    shell.open()
    while ( {
      !shell.isDisposed
    }) if (!display.readAndDispatch) display.sleep
    stepname
  }

  def makeRow(shell: Shell, labelText: String, initialValue: String, relativeTo: Option[Control]): Text = {
    val topForm = relativeTo match {
      case Some(control) => new FormAttachment(control, margin)
      case None => new FormAttachment(0, margin)
    }

    val label = new Label(shell, SWT.RIGHT)
    label.setText(labelText)
    props.setLook(label)
    label.setLayoutData {
      val formData = new FormData
      formData.left = new FormAttachment(0,margin)
      formData.top = topForm
      formData.right = new FormAttachment(middle, -margin)
      formData
    }

    val field = new Text(shell, SWT.LEFT | SWT.BORDER)
    field.setText(initialValue)
    props.setLook(field)
    field.setLayoutData {
      val formData = new FormData
      formData.left = new FormAttachment(middle,0)
      formData.top = topForm
      formData.right = new FormAttachment(100, 0)
      formData
    }

    field
  }
}
