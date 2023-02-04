package org.vlang.lang.completion.sort

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionSorter
import com.intellij.codeInsight.completion.impl.CompletionSorterImpl
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementWeigher
import org.vlang.lang.completion.VlangLookupElement
import org.vlang.lang.completion.VlangLookupElementProperties

val VLANG_COMPLETION_WEIGHERS: List<Any> = listOf(
    /**
     * Based on the value passed via [com.intellij.codeInsight.completion.PrioritizedLookupElement.withPriority].
     * Unused in our case.
     * @see com.intellij.codeInsight.completion.PriorityWeigher
     */
    "priority",

    preferTrue(VlangLookupElementProperties::isNotDeprecated, id = "vlang-prefer-not-deprecated"),
    preferTrue(VlangLookupElementProperties::isContextElement, id = "vlang-prefer-context-element"),
    preferTrue(VlangLookupElementProperties::isContextMember, id = "vlang-prefer-context-member"),
    preferTrue(VlangLookupElementProperties::isTypeCompatible, id = "vlang-prefer-compatible-type"),
    preferTrue(VlangLookupElementProperties::isLocal, id = "vlang-prefer-locals"),
    preferTrue(VlangLookupElementProperties::isSameModule, id = "vlang-prefer-same-module"),
    preferTrue(VlangLookupElementProperties::isReceiverTypeCompatible, id = "vlang-prefer-compatible-self-type"),
    preferUpperVariant(VlangLookupElementProperties::elementKind, id = "vlang-prefer-by-kind"),

    /**
     * Checks prefix matching.
     * @see com.intellij.codeInsight.completion.impl.RealPrefixMatchingWeigher
     */
    "prefix",

    /**
     * Bubbles up the most frequently used items.
     * @see com.intellij.codeInsight.completion.StatisticsWeigher.LookupStatisticsWeigher
     */
    "stats",

    /**
     * Puts closer elements above more distant ones (relative to the location where completion is invoked).
     * For example, elements from workspace packages considered closer than elements from from external packages.
     * Specific rules are defined by [com.intellij.psi.util.proximity.ProximityWeigher] implementations
     * registered using `<com.intellij.weigher key="proximity">` extension point.
     * @see com.intellij.codeInsight.completion.LookupElementProximityWeigher
     */
    "proximity",
)

val VLANG_COMPLETION_WEIGHERS_GROUPED: List<AnchoredWeigherGroup> = splitIntoGroups(VLANG_COMPLETION_WEIGHERS)

fun withVlangSorter(parameters: CompletionParameters, result: CompletionResultSet): CompletionResultSet {
    var sorter = (CompletionSorter.defaultSorter(parameters, result.prefixMatcher) as CompletionSorterImpl)
        .withoutClassifiers { it.id == "liftShorter" }
    for (weigherGroups in VLANG_COMPLETION_WEIGHERS_GROUPED) {
        sorter = sorter.weighAfter(weigherGroups.anchor, *weigherGroups.weighers)
    }
    return result.withRelevanceSorter(sorter)
}

private fun splitIntoGroups(weighersWithAnchors: List<Any>): List<AnchoredWeigherGroup> {
    val firstEntry = weighersWithAnchors.firstOrNull() ?: return emptyList()
    check(firstEntry is String) {
        "The first element in the weigher list must be a string placeholder like \"priority\"; " +
                "actually it is `${firstEntry}`"
    }
    val result = mutableListOf<AnchoredWeigherGroup>()
    val weigherIds = hashSetOf<String>()
    var currentAnchor: String = firstEntry
    var currentWeighers = mutableListOf<LookupElementWeigher>()
    // Add "dummy weigher" in order to execute `is String ->` arm in the last iteration
    for (weigherOrAnchor in weighersWithAnchors.asSequence().drop(1).plus(sequenceOf("dummy weigher"))) {
        when (weigherOrAnchor) {
            is String -> {
                if (currentWeighers.isNotEmpty()) {
                    result += AnchoredWeigherGroup(currentAnchor, currentWeighers.toTypedArray())
                    currentWeighers = mutableListOf()
                }
                currentAnchor = weigherOrAnchor
            }
            is VlangCompletionWeigher -> {
                if (!weigherIds.add(weigherOrAnchor.id)) {
                    error("Found a ${VlangCompletionWeigher::class.simpleName}.id duplicate: " +
                            "`${weigherOrAnchor.id}`")
                }
                currentWeighers += VlangCompletionWeigherAsLookupElementWeigher(weigherOrAnchor)
            }
            else -> error("The weigher list must consists of String placeholders and instances of " +
                    "${VlangCompletionWeigher::class.simpleName}, got ${weigherOrAnchor.javaClass}")
        }
    }
    return result
}

class AnchoredWeigherGroup(val anchor: String, val weighers: Array<LookupElementWeigher>)

private class VlangCompletionWeigherAsLookupElementWeigher(
    private val weigher: VlangCompletionWeigher
) : LookupElementWeigher(weigher.id, /* negated = */ false, /* dependsOnPrefix = */ false) {
    override fun weigh(element: LookupElement): Comparable<*> {
        val lookupElement = element.`as`(VlangLookupElement::class.java)
        return weigher.weigh(lookupElement ?: element)
    }
}

private fun preferTrue(
    property: (VlangLookupElementProperties) -> Boolean,
    id: String
): VlangCompletionWeigher = object : VlangCompletionWeigher {
    override fun weigh(element: LookupElement): Boolean =
        if (element is VlangLookupElement) !property(element.props) else true

    override val id: String get() = id
}

private fun preferUpperVariant(
    property: (VlangLookupElementProperties) -> Enum<*>,
    id: String
): VlangCompletionWeigher = object : VlangCompletionWeigher {
    override fun weigh(element: LookupElement): Int =
        if (element is VlangLookupElement) property(element.props).ordinal else Int.MAX_VALUE

    override val id: String get() = id
}
