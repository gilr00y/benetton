package com.expr.pentaho.benetton

import org.eclipse.swt.SWT
import org.eclipse.swt.events._
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets.{Listener, _}
import org.pentaho.di.core.Const
import org.pentaho.di.core.exception.KettleException
import org.pentaho.di.trans._
import org.pentaho.di.trans.step._
import org.pentaho.di.trans.steps.groupby.GroupByMeta
import org.pentaho.di.ui.core.widget.{ColumnInfo, TableView}
import org.pentaho.di.ui.trans.step._


class BenettonStepDialog(parent: Shell, m: Object, transMeta: TransMeta, stepName: String)
    extends BaseStepDialog(parent, m.asInstanceOf[BaseStepMeta], transMeta, stepName)
    with StepDialogInterface {

  this.shell = parent

  private[this] val stepMeta = m.asInstanceOf[StepMetaInterface]
  private[this] val ourMeta = m.asInstanceOf[BenettonStepMeta]

  val middle = props.getMiddlePct
  val margin = Const.MARGIN



//  def get(): Unit = {
//    try {
//      val r = transMeta.getPrevStepFields(stepname)
//      if (r != null && !r.isEmpty) {
//        BaseStepDialog.getFieldsFromPrevious(
//          r, wGroup, 1, Array[Int](1),
//          new Array[Int](1), -1, -1, null
//        )
//      }
//    } catch {
//      case ke: KettleException => new ErrorDialog(shell, "Kettle Error", ke.toString, ke)
//      case e: Exception => new ErrorDialog(shell, "Generic Error", e.toString, e)
//    }
//  }

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
    groupLabel.setText("GroupBy Label")
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
    val getFieldBtn = new Button(shell, SWT.PUSH)
    getFieldBtn.setText("Get Fields")
    val getFieldBtnLayout = new FormData
    getFieldBtnLayout.top = new FormAttachment(groupTbl, margin)
    getFieldBtnLayout.right = new FormAttachment(100, 0)
    getFieldBtn.setLayoutData(getFieldBtnLayout)
    val groupTblLayout = new FormData
    groupTblLayout.left = new FormAttachment(0, 0)
    groupTblLayout.top = new FormAttachment(groupLabel, margin)
    groupTblLayout.right = new FormAttachment(getFieldBtn, -margin)
    groupTblLayout.bottom = new FormAttachment(45, 0)
    groupTbl.setLayoutData(groupTblLayout)
    // THE Aggregate fields
    val wlAgg = new Label(shell, SWT.NONE)
    wlAgg.setText("Aggregates:")
    props.setLook(wlAgg)
    val fdlAgg = new FormData
    fdlAgg.left = new FormAttachment(0, 0)
    fdlAgg.top = new FormAttachment(groupTbl, margin)
    wlAgg.setLayoutData(fdlAgg)
    val UpInsCols = 4
    val UpInsRows = 1
    val ciReturn = new Array[ColumnInfo](UpInsCols)
    ciReturn(0) = new ColumnInfo("Name", ColumnInfo.COLUMN_TYPE_TEXT, false)
    ciReturn(1) = new ColumnInfo("Subject", ColumnInfo.COLUMN_TYPE_CCOMBO, Array[String](""), false)
    ciReturn(2) = new ColumnInfo("Type", ColumnInfo.COLUMN_TYPE_CCOMBO, GroupByMeta.typeGroupLongDesc)
    ciReturn(3) = new ColumnInfo("Value", ColumnInfo.COLUMN_TYPE_TEXT, false)
    ciReturn(3).setToolTip("Tooltip Here")
    ciReturn(3).setUsingVariables(true)
    val wAgg = new TableView(transMeta, shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, ciReturn, UpInsRows, lsMod, props)
    wAgg.addModifyListener((modifyEvent: ModifyEvent) => {
      def foo(modifyEvent: ModifyEvent):Unit = {
//        setFlags
        ourMeta.setChanged
      }

      foo(modifyEvent)
    })
    val wGetAgg = new Button(shell, SWT.PUSH)
    wGetAgg.setText("Get Lookup Fields")
    val fdGetAgg = new FormData
    fdGetAgg.top = new FormAttachment(wlAgg, margin)
    fdGetAgg.right = new FormAttachment(100, 0)
    wGetAgg.setLayoutData(fdGetAgg)
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
    val fdAgg = new FormData
    fdAgg.left = new FormAttachment(0, 0)
    fdAgg.top = new FormAttachment(wlAgg, margin)
    fdAgg.right = new FormAttachment(wGetAgg, -margin)
    fdAgg.bottom = new FormAttachment(wOK, -margin)
    wAgg.setLayoutData(fdAgg)
    // Add listeners
    val lsOK: Listener = new Listener() {
      def handleEvent(e: Event): Unit = {
        println("LSOK EVENT")
      }
    }
    val lsGet: Listener = new Listener() {
      def handleEvent(e: Event): Unit = {
        println("LSGET EVENT")
      }
    }
    val lsGetAgg: Listener = new Listener() {
      def handleEvent(e: Event): Unit = {
        println("LSGETAGG EVENT")
//        getAgg
      }
    }
    val lsCancel: Listener = new Listener() {
      def handleEvent(e: Event): Unit = {
        println("LSCANCeL EVENT")
//        cancel
      }
    }
    wOK.addListener(SWT.Selection, lsOK)
    getFieldBtn.addListener(SWT.Selection, lsGet)
    wGetAgg.addListener(SWT.Selection, lsGetAgg)
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
//    getData
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
