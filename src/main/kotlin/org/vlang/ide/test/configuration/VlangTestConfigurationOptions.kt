package org.vlang.ide.test.configuration

import com.intellij.execution.configurations.LocatableRunConfigurationOptions

class VlangTestConfigurationOptions : LocatableRunConfigurationOptions() {
    private val myScope = string("").provideDelegate(this, "vlangTestScope")
    private val myFilename = string("").provideDelegate(this, "vlangTestFilename")
    private val myDirectory = string("").provideDelegate(this, "vlangTestDirectory")
    private val myPattern = string("").provideDelegate(this, "vlangTestPattern")
    private val myAdditionalParameters = string("").provideDelegate(this, "vlangTestAdditionalParameters")

    var scope: VlangTestScope
        get() = VlangTestScope.from(myScope.getValue(this) ?: "")
        set(value) {
            myScope.setValue(this, value.name)
        }

    var directory: String
        get() = myDirectory.getValue(this) ?: ""
        set(value) {
            myDirectory.setValue(this, value)
        }

    var filename: String
        get() = myFilename.getValue(this) ?: ""
        set(value) {
            myFilename.setValue(this, value)
        }

    var pattern: String
        get() = myPattern.getValue(this) ?: ""
        set(value) {
            myPattern.setValue(this, value)
        }

    var additionalParameters: String
        get() = myAdditionalParameters.getValue(this) ?: ""
        set(value) {
            myAdditionalParameters.setValue(this, value)
        }
}
