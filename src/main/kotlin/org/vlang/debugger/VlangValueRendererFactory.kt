package org.vlang.debugger

import com.intellij.execution.ExecutionException
import com.intellij.openapi.util.Pair
import com.intellij.xdebugger.XDebuggerBundle
import com.intellij.xdebugger.frame.XFullValueEvaluator
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess.DebuggerUIUpdateCommand
import com.jetbrains.cidr.execution.debugger.CidrDebuggerUtil
import com.jetbrains.cidr.execution.debugger.backend.*
import com.jetbrains.cidr.execution.debugger.evaluation.ValueRendererFactory
import com.jetbrains.cidr.execution.debugger.evaluation.ValueRendererFactory.FactoryContext
import com.jetbrains.cidr.execution.debugger.evaluation.renderers.ValueRenderer
import com.jetbrains.cidr.execution.debugger.evaluation.renderers.ValueRendererUtils
import org.vlang.debugger.renderers.VlangValueRenderer.Companion.removeRendererMarks

class VlangValueRendererFactory : ValueRendererFactory {
    override fun createRenderer(factoryContext: FactoryContext): ValueRenderer {
        val renderer = object : ValueRenderer(factoryContext.physicalValue) {
            override fun doComputeValueAndEvaluator(
                context: EvaluationContext,
                llValue: LLValue,
                data: LLValueData,
            ): Pair<String, XFullValueEvaluator> {
                val descriptionOrig = data.description ?: return super.doComputeValueAndEvaluator(context, llValue, data)
                val hasLongerDescription = data.hasLongerDescription()
                if (hasLongerDescription) {
                    val linkText = XDebuggerBundle.message("node.test.show.full.value")
                    val evaluator = this.createFullDescriptionEvaluator(linkText, descriptionOrig)
                    val valueAndEvaluator = super.doComputeValueAndEvaluator(context, llValue, data)
                    return Pair.create(valueAndEvaluator.first, evaluator)
                }

                return super.doComputeValueAndEvaluator(context, llValue, data)
            }

            private fun createFullDescriptionEvaluator(linkText: String, descriptionRaw: String): XFullValueEvaluator {
                return object : XFullValueEvaluator(linkText) {
                    override fun startEvaluation(callback: XFullValueEvaluationCallback) {
                        value.process.postCommand(DebuggerUIUpdateCommand { _: DebuggerDriver ->
                            if (callback.isObsolete) return@DebuggerUIUpdateCommand
                            try {
                                var description = descriptionRaw
                                if (myValue.process.isRichValueDescriptionEnabled) {
                                    description = description.removeRendererMarks()
                                }
                                callback.evaluated(ValueRendererUtils.getRawString(description))
                            } catch (e: DebuggerCommandException) {
                                callback.errorOccurred(e.message!!)
                            } catch (e: ExecutionException) {
                                callback.errorOccurred(CidrDebuggerUtil.getExceptionMessage(e))
                                throw e
                            }
                        })
                    }
                }
            }
        }
        return renderer
    }
}
