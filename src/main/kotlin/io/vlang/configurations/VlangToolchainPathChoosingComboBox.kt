package io.vlang.configurations

import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.ComboBoxWithWidePopup
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.ui.AnimatedIcon
import com.intellij.ui.ComboboxSpeedSearch
import com.intellij.ui.components.fields.ExtendableTextComponent
import com.intellij.ui.components.fields.ExtendableTextField
import io.vlang.utils.addTextChangeListener
import io.vlang.utils.pathAsPath
import kotlinx.coroutines.*
import java.nio.file.Path
import javax.swing.plaf.basic.BasicComboBoxEditor
import kotlin.io.path.pathString

class VlangToolchainPathChoosingComboBox(onTextChanged: () -> Unit = {}) :
    ComponentWithBrowseButton<ComboBoxWithWidePopup<Path>>(ComboBoxWithWidePopup(), null) {

    private val editor: BasicComboBoxEditor = object : BasicComboBoxEditor() {
        override fun createEditorComponent(): ExtendableTextField = ExtendableTextField()
    }

    private val pathTextField: ExtendableTextField
        get() = childComponent.editor.editorComponent as ExtendableTextField

    private val busyIconExtension: ExtendableTextComponent.Extension =
        ExtendableTextComponent.Extension { AnimatedIcon.Default.INSTANCE }

    var selectedPath: String?
        get() = pathTextField.text
        set(value) {
            pathTextField.text = value.orEmpty()
        }

    init {
        val comboboxSpeedSearch = ComboboxSpeedSearch.installOn(childComponent)
        comboboxSpeedSearch.setupListeners()
        childComponent.editor = editor
        childComponent.isEditable = true

        addActionListener {
            val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            FileChooser.chooseFile(descriptor, null, null) { file ->
                childComponent.selectedItem = file.pathAsPath
            }
        }

        pathTextField.addTextChangeListener { onTextChanged() }
    }

    private fun setBusy(busy: Boolean) {
        if (busy) {
            pathTextField.addExtension(busyIconExtension)
        } else {
            pathTextField.removeExtension(busyIconExtension)
        }
        repaint()
    }

    /**
     * Obtains a list of toolchains on a default pool using [toolchainObtainer], then fills the combobox on the EDT.
     */
    fun addToolchainsAsync(scope: CoroutineScope, toolchainObtainer: () -> List<Path>) = scope.launch {
        withContext(Dispatchers.EDT) {
            setBusy(true)

            val toolchains = withContext(Dispatchers.Default) { toolchainObtainer() }

            setBusy(false)
            childComponent.removeAllItems()
            toolchains.forEach(childComponent::addItem)
            selectedPath = selectedPath?.ifEmpty { null } ?: (toolchains.firstOrNull()?.pathString ?: "")
        }
    }
}
