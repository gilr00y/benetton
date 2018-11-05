package com.expr.pentaho.benetton

import org.eclipse.swt.SWT
import org.eclipse.swt.events.{ModifyEvent, ModifyListener}
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._
import org.pentaho.di.core.Const
import org.pentaho.di.core.exception.KettleException
import org.pentaho.di.trans._
import org.pentaho.di.trans.step._
import org.pentaho.di.ui.core.dialog.ErrorDialog
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

  // On open dialog.
//  def open(): String = {
//    val parent = getParent
//    val display = parent.getDisplay
//
//    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN )
//    shell.setText("Benetton: Comparison Join")
//    props.setLook(shell)
//    setShellImage(shell, ourMeta)
//
//    val layout = new FormLayout
//    layout.marginWidth = Const.FORM_MARGIN
//    layout.marginHeight = Const.FORM_MARGIN
//    shell.setLayout(layout)
//    val stepName = makeRow(shell, "Step Name:", stepname, None)
//
//    val gbGroup = new Label( shell, SWT.NONE)
//    gbGroup.setText("JOIN ON:")
//    props.setLook( gbGroup )
//
//    val fdlGroup = new FormData
//    fdlGroup.left = new FormAttachment( 0,0 )
//    fdlGroup.top = new FormAttachment(stepName, margin)
//    gbGroup.setLayoutData( fdlGroup )
//
//    val nrKeyCols: Int = 1
//    val nrKeyRows: Int = ourMeta.getGroupFields.length
//    val lsMod = new ModifyListener {
//
//      def modifyText(e: ModifyEvent): Unit = {
//        println("MODIFY TEXT CALLED")
////        ourMeta.setChanged()
//      }
//    }
//    val ciKey: Array[ColumnInfo] = Array(
//      new ColumnInfo( "Stream A Join Col", ColumnInfo.COLUMN_TYPE_CCOMBO)
//    )
//
//    val wGroup = new TableView(
//      transMeta, shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, ciKey,
//      nrKeyRows, lsMod, props
//    )
//
//    def get(): Unit = {
//      try {
//        val r = transMeta.getPrevStepFields(stepname)
//        if (r != null && !r.isEmpty) {
//          BaseStepDialog.getFieldsFromPrevious(
//            r, wGroup, 1, Array[Int](1),
//            new Array[Int](1), -1, -1, null
//          )
//        }
//      } catch {
//        case ke: KettleException => new ErrorDialog(shell, "Kettle Error", ke.toString, ke)
//        case e: Exception => new ErrorDialog(shell, "Generic Error", e.toString, e)
//      }
//    }
//    val lsGet = new Listener() {
//      def handleEvent(e: Event): Unit = {
//        get()
//      }
//    }
//
//    val wGet = new Button(shell, SWT.PUSH)
//    wGet.setText( "Get Fields" )
//    fdGet = new FormData
//    fdGet.top = new FormAttachment(gbGroup, margin)
//    fdGet.right = new FormAttachment(100, 0)
//    wGet.setLayoutData(fdGet)
//    wGet.addListener(SWT.Selection, lsGet)
//
//    val fdGroup = new FormData
//    fdGroup.left = new FormAttachment(0, 0)
//    fdGroup.top = new FormAttachment(gbGroup, margin)
//    fdGroup.right = new FormAttachment(wGet, -margin)
//    fdGroup.bottom = new FormAttachment(45, 0)
//    wGroup.setLayoutData(fdGroup)
//
//    // val projectId = makeRow(shell, "IronMQ Project Id (blank to locate in .json config):", ourMeta.projectId, Some(token))
//    //   val queue = makeRow(shell, "IronMQ Queue Name (required):", ourMeta.queue, Some(projectId))
//    //   val outputField = makeRow(shell, "Output field name:", ourMeta.outputField, Some(queue))
//
//
//
//    val okButton = new Button(shell, SWT.PUSH)
//    okButton.setText("OK")
//    okButton.addListener(SWT.Selection, new Listener() {
//      def handleEvent(e: Event): Unit = {
//        // if (stepName.getText.nonEmpty && queue.getText.nonEmpty) {
//        stepname = stepName.getText
////        ourMeta.groupFields =
//        // ourMeta.projectId = projectId.getText
//        // ourMeta.queue = queue.getText
//        // ourMeta.outputField = outputField.getText
//        ourMeta.setChanged(true)
//
//        shell.dispose()
//      }
//    })
//
//    val cancelButton = new Button(shell, SWT.PUSH)
//    cancelButton.setText("Cancel")
//    cancelButton.addListener(SWT.Selection, new Listener {
//      def handleEvent(e: Event): Unit = {
//        stepname = null
//        ourMeta.setChanged(false)
//        shell.dispose()
//      }
//    })
//
//    setButtonPositions(Array(okButton, cancelButton), margin, wGroup)
//    //   setSize()
//
//    shell.pack()
//    shell.open()
//    while ( !shell.isDisposed ) {
//      if ( !display.readAndDispatch() ) {
//        display.sleep()
//      }
//    }
//    stepname
//  }

  import org.eclipse.swt.SWT
  import org.eclipse.swt.events.ModifyEvent
  import org.eclipse.swt.events.ModifyListener
  import org.eclipse.swt.events.SelectionAdapter
  import org.eclipse.swt.events.SelectionEvent
  import org.eclipse.swt.events.ShellAdapter
  import org.eclipse.swt.events.ShellEvent
  import org.eclipse.swt.layout.FormAttachment
  import org.eclipse.swt.layout.FormData
  import org.eclipse.swt.layout.FormLayout
  import org.eclipse.swt.widgets.DirectoryDialog
  import org.eclipse.swt.widgets.Display
  import org.pentaho.di.core.Const
  import org.pentaho.di.core.exception.KettleException
  import org.pentaho.di.core.row.RowMetaInterface
  import org.pentaho.di.i18n.BaseMessages
  import org.pentaho.di.trans.step.StepMeta
  import org.pentaho.di.trans.steps.groupby.GroupByMeta
  import org.pentaho.di.ui.core.widget.ColumnInfo
  import org.pentaho.di.ui.core.widget.TextVar

  def open: String = {
    val parent = getParent
    val display = parent.getDisplay
    shell = new Label(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN)
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
    wlStepname = new Label(shell, SWT.RIGHT)
    wlStepname.setText("Step Name")
    props.setLook(wlStepname)
    fdlStepname = new FormData
    fdlStepname.left = new FormAttachment(0, 0)
    fdlStepname.right = new FormAttachment(middle, -margin)
    fdlStepname.top = new FormAttachment(0, margin)
    wlStepname.setLayoutData(fdlStepname)
    wStepname = new Label(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER)
    wStepname.setText(stepname)
    props.setLook(wStepname)
    wStepname.addModifyListener(lsMod)
    fdStepname = new FormData
    fdStepname.left = new FormAttachment(middle, 0)
    fdStepname.top = new FormAttachment(0, margin)
    fdStepname.right = new FormAttachment(100, 0)
    wStepname.setLayoutData(fdStepname)
    // Include all rows?
    val wlAllRows = new Label(shell, SWT.RIGHT)
    wlAllRows.setText(BaseMessages.getString(PKG, "GroupByDialog.AllRows.Label"))
    props.setLook(wlAllRows)
    val fdlAllRows = new FormData
    fdlAllRows.left = new FormAttachment(0, 0)
    fdlAllRows.top = new FormAttachment(wStepname, margin)
    fdlAllRows.right = new FormAttachment(middle, -margin)
    wlAllRows.setLayoutData(fdlAllRows)
    val wAllRows = new Button(shell, SWT.CHECK)
    props.setLook(wAllRows)
    val fdAllRows = new FormData
    fdAllRows.left = new FormAttachment(middle, 0)
    fdAllRows.top = new FormAttachment(wStepname, margin)
    fdAllRows.right = new FormAttachment(100, 0)
    wAllRows.setLayoutData(fdAllRows)
    wAllRows.addSelectionListener(new SelectionAdapter() {
      override def widgetSelected(e: SelectionEvent): Unit = {
        ourMeta.setChanged
        setFlags
      }
    })
    val wlSortDir = new Label(shell, SWT.RIGHT)
    wlSortDir.setText(BaseMessages.getString(PKG, "GroupByDialog.TempDir.Label"))
    props.setLook(wlSortDir)
    val fdlSortDir = new FormData
    fdlSortDir.left = new FormAttachment(0, 0)
    fdlSortDir.right = new FormAttachment(middle, -margin)
    fdlSortDir.top = new FormAttachment(wAllRows, margin)
    wlSortDir.setLayoutData(fdlSortDir)
    val wbSortDir = new Button(shell, SWT.PUSH | SWT.CENTER)
    props.setLook(wbSortDir)
    wbSortDir.setText(BaseMessages.getString(PKG, "GroupByDialog.Browse.Button"))
    val fdbSortDir = new FormData
    fdbSortDir.right = new FormAttachment(100, 0)
    fdbSortDir.top = new FormAttachment(wAllRows, margin)
    wbSortDir.setLayoutData(fdbSortDir)
    val wSortDir = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER)
    props.setLook(wSortDir)
    wSortDir.addModifyListener(lsMod)
    val fdSortDir = new FormData
    fdSortDir.left = new FormAttachment(middle, 0)
    fdSortDir.top = new FormAttachment(wAllRows, margin)
    fdSortDir.right = new FormAttachment(wbSortDir, -margin)
    wSortDir.setLayoutData(fdSortDir)
    wbSortDir.addSelectionListener(new SelectionAdapter() {
      override def widgetSelected(arg0: SelectionEvent): Unit = {
        val dd = new DirectoryDialog(shell, SWT.NONE)
        dd.setFilterPath(wSortDir.getText)
        val dir = dd.open
        if (dir != null) wSortDir.setText(dir)
      }
    })
    // Whenever something changes, set the tooltip to the expanded version:
    wSortDir.addModifyListener(new ModifyListener() {
      override def modifyText(e: ModifyEvent): Unit = {
        wSortDir.setToolTipText(transMeta.environmentSubstitute(wSortDir.getText))
      }
    })
    // Prefix line...
    val wlPrefix = new Label(shell, SWT.RIGHT)
    wlPrefix.setText(BaseMessages.getString(PKG, "GroupByDialog.FilePrefix.Label"))
    props.setLook(wlPrefix)
    val fdlPrefix = new FormData
    fdlPrefix.left = new FormAttachment(0, 0)
    fdlPrefix.right = new FormAttachment(middle, -margin)
    fdlPrefix.top = new FormAttachment(wbSortDir, margin * 2)
    wlPrefix.setLayoutData(fdlPrefix)
    val wPrefix = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER)
    props.setLook(wPrefix)
    wPrefix.addModifyListener(lsMod)
    val fdPrefix = new FormData
    fdPrefix.left = new FormAttachment(middle, 0)
    fdPrefix.top = new FormAttachment(wbSortDir, margin * 2)
    fdPrefix.right = new FormAttachment(100, 0)
    wPrefix.setLayoutData(fdPrefix)
    val wlAddLineNr = new Label(shell, SWT.RIGHT)
    wlAddLineNr.setText(BaseMessages.getString(PKG, "GroupByDialog.AddLineNr.Label"))
    props.setLook(wlAddLineNr)
    val fdlAddLineNr = new FormData
    fdlAddLineNr.left = new FormAttachment(0, 0)
    fdlAddLineNr.top = new FormAttachment(wPrefix, margin)
    fdlAddLineNr.right = new FormAttachment(middle, -margin)
    wlAddLineNr.setLayoutData(fdlAddLineNr)
    val wAddLineNr = new Button(shell, SWT.CHECK)
    props.setLook(wAddLineNr)
    val fdAddLineNr = new FormData
    fdAddLineNr.left = new FormAttachment(middle, 0)
    fdAddLineNr.top = new FormAttachment(wPrefix, margin)
    fdAddLineNr.right = new FormAttachment(100, 0)
    wAddLineNr.setLayoutData(fdAddLineNr)
    wAddLineNr.addSelectionListener(new SelectionAdapter() {
      override def widgetSelected(e: SelectionEvent): Unit = {
//        ourMeta.setAddingLineNrInGroup(!ourMeta.isAddingLineNrInGroup)
        ourMeta.setChanged
        setFlags
      }
    })
    // LineNrField line...
    val wlLineNrField = new Label(shell, SWT.RIGHT)
    wlLineNrField.setText(BaseMessages.getString(PKG, "GroupByDialog.LineNrField.Label"))
    props.setLook(wlLineNrField)
    val fdlLineNrField = new FormData
    fdlLineNrField.left = new FormAttachment(0, 0)
    fdlLineNrField.right = new FormAttachment(middle, -margin)
    fdlLineNrField.top = new FormAttachment(wAddLineNr, margin)
    wlLineNrField.setLayoutData(fdlLineNrField)
    val wLineNrField = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER)
    props.setLook(wLineNrField)
    wLineNrField.addModifyListener(lsMod)
    val fdLineNrField = new FormData
    fdLineNrField.left = new FormAttachment(middle, 0)
    fdLineNrField.top = new FormAttachment(wAddLineNr, margin)
    fdLineNrField.right = new FormAttachment(100, 0)
    wLineNrField.setLayoutData(fdLineNrField)
    // Always pass a result rows as output
    //
    val wlAlwaysAddResult = new Label(shell, SWT.RIGHT)
    wlAlwaysAddResult.setText(BaseMessages.getString(PKG, "GroupByDialog.AlwaysAddResult.Label"))
    wlAlwaysAddResult.setToolTipText(BaseMessages.getString(PKG, "GroupByDialog.AlwaysAddResult.ToolTip"))
    props.setLook(wlAlwaysAddResult)
    val fdlAlwaysAddResult = new FormData
    fdlAlwaysAddResult.left = new FormAttachment(0, 0)
    fdlAlwaysAddResult.top = new FormAttachment(wLineNrField, margin)
    fdlAlwaysAddResult.right = new FormAttachment(middle, -margin)
    wlAlwaysAddResult.setLayoutData(fdlAlwaysAddResult)
    val wAlwaysAddResult = new Button(shell, SWT.CHECK)
    wAlwaysAddResult.setToolTipText(BaseMessages.getString(PKG, "GroupByDialog.AlwaysAddResult.ToolTip"))
    props.setLook(wAlwaysAddResult)
    val fdAlwaysAddResult = new FormData
    fdAlwaysAddResult.left = new FormAttachment(middle, 0)
    fdAlwaysAddResult.top = new FormAttachment(wLineNrField, margin)
    fdAlwaysAddResult.right = new FormAttachment(100, 0)
    wAlwaysAddResult.setLayoutData(fdAlwaysAddResult)
    val wlGroup = new Label(shell, SWT.NONE)
    wlGroup.setText(BaseMessages.getString(PKG, "GroupByDialog.Group.Label"))
    props.setLook(wlGroup)
    val fdlGroup = new FormData
    fdlGroup.left = new FormAttachment(0, 0)
    fdlGroup.top = new FormAttachment(wAlwaysAddResult, margin)
    wlGroup.setLayoutData(fdlGroup)
    val nrKeyCols = 1
    val nrKeyRows = if (ourMeta.getGroupField != null) ourMeta.getGroupField.length
    else 1
    val ciKey = new Array[ColumnInfo](nrKeyCols)
    ciKey(0) = new ColumnInfo(BaseMessages.getString(PKG, "GroupByDialog.ColumnInfo.GroupField"), ColumnInfo.COLUMN_TYPE_CCOMBO, Array[String](""), false)
    val wGroup = new TableView(transMeta, shell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL, ciKey, nrKeyRows, lsMod, props)
    wGet = new Button(shell, SWT.PUSH)
    wGet.setText(BaseMessages.getString(PKG, "GroupByDialog.GetFields.Button"))
    fdGet = new FormData
    fdGet.top = new FormAttachment(wlGroup, margin)
    fdGet.right = new FormAttachment(100, 0)
    wGet.setLayoutData(fdGet)
    val fdGroup = new FormData
    fdGroup.left = new FormAttachment(0, 0)
    fdGroup.top = new FormAttachment(wlGroup, margin)
    fdGroup.right = new FormAttachment(wGet, -margin)
    fdGroup.bottom = new FormAttachment(45, 0)
    wGroup.setLayoutData(fdGroup)
    // THE Aggregate fields
    val wlAgg = new Label(shell, SWT.NONE)
    wlAgg.setText(BaseMessages.getString(PKG, "GroupByDialog.Aggregates.Label"))
    props.setLook(wlAgg)
    val fdlAgg = new FormData
    fdlAgg.left = new FormAttachment(0, 0)
    fdlAgg.top = new FormAttachment(wGroup, margin)
    wlAgg.setLayoutData(fdlAgg)
    val UpInsCols = 4
    val UpInsRows = if (ourMeta.getAggregateField != null) ourMeta.getAggregateField.length
    else 1
    val ciReturn = new Array[ColumnInfo](UpInsCols)
    ciReturn(0) = new ColumnInfo(BaseMessages.getString(PKG, "GroupByDialog.ColumnInfo.Name"), ColumnInfo.COLUMN_TYPE_TEXT, false)
    ciReturn(1) = new ColumnInfo(BaseMessages.getString(PKG, "GroupByDialog.ColumnInfo.Subject"), ColumnInfo.COLUMN_TYPE_CCOMBO, Array[String](""), false)
    ciReturn(2) = new ColumnInfo(BaseMessages.getString(PKG, "GroupByDialog.ColumnInfo.Type"), ColumnInfo.COLUMN_TYPE_CCOMBO, GroupByMeta.typeGroupLongDesc)
    ciReturn(3) = new ColumnInfo(BaseMessages.getString(PKG, "GroupByDialog.ColumnInfo.Value"), ColumnInfo.COLUMN_TYPE_TEXT, false)
    ciReturn(3).setToolTip(BaseMessages.getString(PKG, "GroupByDialog.ColumnInfo.Value.Tooltip"))
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
    wGetAgg.setText(BaseMessages.getString(PKG, "GroupByDialog.GetLookupFields.Button"))
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
            ourMetaFields.put(row.getValueMeta(i).getName, Integer.valueOf(i))

            {
              i += 1; i - 1
            }
          }
          setComboBoxes
        } catch {
          case e: KettleException =>
            logError(BaseMessages.getString(PKG, "System.Dialog.GetFieldsFailed.Message"))
        }
      }
    }
    new Thread(runnable).start()
    // THE BUTTONS
    wOK = new Button(shell, SWT.PUSH)
    wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"))
    wCancel = new Button(shell, SWT.PUSH)
    wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"))
    setButtonPositions(Array[Label](wOK, wCancel), margin, null)
    val fdAgg = new FormData
    fdAgg.left = new FormAttachment(0, 0)
    fdAgg.top = new FormAttachment(wlAgg, margin)
    fdAgg.right = new FormAttachment(wGetAgg, -margin)
    fdAgg.bottom = new FormAttachment(wOK, -margin)
    wAgg.setLayoutData(fdAgg)
    // Add listeners
    lsOK = new Label() {
      def handleEvent(e: Label): Unit = {
        ok
      }
    }
    lsGet = new Label() {
      def handleEvent(e: Label): Unit = {
        get
      }
    }
    lsGetAgg = new Label() {
      def handleEvent(e: Label): Unit = {
        getAgg
      }
    }
    lsCancel = new Label() {
      def handleEvent(e: Label): Unit = {
        cancel
      }
    }
    wOK.addListener(SWT.Selection, lsOK)
    wGet.addListener(SWT.Selection, lsGet)
    wGetAgg.addListener(SWT.Selection, lsGetAgg)
    wCancel.addListener(SWT.Selection, lsCancel)
    lsDef = new SelectionAdapter() {
      override def widgetDefaultSelected(e: SelectionEvent): Unit = {
        ok
      }
    }
    wStepname.addSelectionListener(lsDef)
    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener(new ShellAdapter() {
      override def shellClosed(e: ShellEvent): Unit = {
        cancel
      }
    })
    // Set the shell size, based upon previous time...
    setSize
    getData
    ourMeta.setChanged(backupChanged)
    shell.open
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
