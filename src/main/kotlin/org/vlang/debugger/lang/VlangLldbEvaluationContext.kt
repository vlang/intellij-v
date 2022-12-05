package org.vlang.debugger.lang

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Expirable
import com.intellij.openapi.util.UserDataHolderEx
import com.jetbrains.cidr.execution.debugger.backend.*
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBEvaluationContext
import org.vlang.debugger.mapItems
import org.vlang.debugger.renderers.*
import org.vlang.debugger.withContext
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

class VlangLldbEvaluationContext(
    val project: Project,
    driver: DebuggerDriver,
    expirable: Expirable?,
    thread: LLThread,
    frame: LLFrame,
    cacheHolder: UserDataHolderEx,
) : LLDBEvaluationContext(driver, expirable, thread, frame, cacheHolder) {

    private fun findRenderer(value: LLValue): VlangValueRenderer? =
        VlangValueRenderer.EP_NAME.extensionList.find { it.isApplicable(project, value) }

    override fun getData(value: LLValue): LLValueData {
        val renderer = findRenderer(value)
            ?: return getRawData(value)
        return renderer.getData(value.withContext(VlangRendererEvaluationContext(this, value)))

//        val valueType = value.type
//        if (valueType == "string") {
//            return VlangStringRenderer.getData(value.withContext(VlangRendererEvaluationContext(this, value)))
//        }
//
//        if (valueType.startsWith("Array_")) {
//            val type = valueType.substringAfter("Array_")
//            return VlangArrayRenderer(type).getData(value.withContext(VlangRendererEvaluationContext(this, value)))
//        }
//
//        if (valueType.startsWith("Map")) {
//            return VlangMapRenderer.getData(value.withContext(VlangRendererEvaluationContext(this, value)))
//        }
//
//        val fqn = VlangCTypeParser.convertCNameToVName(valueType)
//        val klass = runReadAction {
//            VlangClassLikeIndex.find(fqn, project, null, null).firstOrNull()
//        }
//        if (klass is VlangEnumDeclaration) {
//            return VlangEnumRenderer.getData(value.withContext(VlangRendererEvaluationContext(this, value)))
//        }
    }

    // name -> имя переменной или выражение?
    // type -> тип выражения
    // displayType -> тип переменной который видимо будет выведен в дебагере
    // address -> адрес переменной. Null если это выражение
    // referenceExpression -> некоторое выражение для переменной

    // LLValueData
    // value -> значение переменной, если значение это адрес, но он будет показан и выделен другим цветом
    // description -> описание выводящееся в дебагере справа от типа
    // isSynthetic -> ???
    // mayHaveChildren -> есть ли дочерние элементы, если значение false то элемент нельзя развернуть и он будет считаться как примитив
    // hasLongerDescription -> когда true показыват ссылку `... View` при клике на который показывается поле, но оно почему-то пустое
    // вероятно, оно не пустое когда значение слишком большое и его нельзя отобразить в дебагере
    //
    // Сложные типы имеют пустой value и даже строки имеют пустой value

    // Если надо вывести некоторое значение справа от типа (в виде превью значения, например длину массива), то
    // нужно вернуть LLValueData с не пустым description.
    // Интересно, но если вернуть не пустой value то он также отображается слева
    // Если description != null, то выводится он всегда, не смотря на value

    /**
     * @param size размер массива, по умолчанию 50, смотри cidr.debugger.value.maxChildren key
     */
    override fun getVariableChildren(value: LLValue, offset: Int, size: Int): DebuggerDriver.ResultList<LLValue> {
        val renderer = findRenderer(value)
            ?: return getRawVariableChildren(value, offset, size)
        return renderer.getVariableChildren(value.withContext(VlangRendererEvaluationContext(this, value)), offset, size)
            .mapItems { it.llValue }
    }

    fun getRawData(llValue: LLValue): LLValueData = (llValue as? VlangFakeLLValue)?.data
        ?: super.getData(llValue)

    fun getRawVariableChildren(llValue: LLValue, offset: Int, size: Int) =
        (llValue as? VlangFakeLLValue)?.children
            ?.let { DebuggerDriver.ResultList.create(it.subList(offset, Integer.min(offset + size, it.size)), offset + size < it.size) }
            ?: super.getVariableChildren(llValue, offset, size)


    companion object {
        fun getVariableChildren(
            context: VlangLldbEvaluationContext,
            value: LLValue, offset: Int, size: Int
        ): DebuggerDriver.ResultList<LLValue>? {
            val valueType = value.type

//            if (valueType.startsWith("Array_")) {
//                val type = valueType.substringAfter("Array_")
//                return VlangArrayRenderer(type).getVariableChildren(value.withContext(VlangRendererEvaluationContext(context, value)), offset, size)
//                    .mapItems { it.llValue }
//            }
//
//            if (valueType.startsWith("Map")) {
//                return VlangMapRenderer.getVariableChildren(value.withContext(VlangRendererEvaluationContext(context, value)), offset, size)
//                    .mapItems { it.llValue }
//            }

            val fqn = VlangCTypeParser.convertCNameToVName(valueType)
            val klass = runReadAction {
                VlangClassLikeIndex.find(fqn, context.project, null, null).firstOrNull()
            }
            if (klass is VlangEnumDeclaration) {
                return VlangEnumRenderer.getVariableChildren(value.withContext(VlangRendererEvaluationContext(context, value)), offset, size)
                    .mapItems { it.llValue }
            }

            return null
        }
    }
}