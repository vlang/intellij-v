package org.vlang.ide.colors

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.psi.codeStyle.DisplayPriority
import com.intellij.psi.codeStyle.DisplayPrioritySortable
import org.vlang.ide.ui.VIcons
import org.vlang.lang.VlangSyntaxHighlighter

class VlangColorsSettingsPage : ColorSettingsPage, DisplayPrioritySortable {
    companion object {
        private val ANNOTATOR_TAGS: Map<String, TextAttributesKey> = VlangColor.values().associateBy({ it.name }, { it.textAttributesKey })
        private val ATTRS: Array<AttributesDescriptor> = VlangColor.values().map { it.attributesDescriptor }.toTypedArray()
    }

    override fun getHighlighter() = VlangSyntaxHighlighter()
    override fun getIcon() = VIcons.V
    override fun getAttributeDescriptors() = ATTRS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getAdditionalHighlightingTagToDescriptorMap() = ANNOTATOR_TAGS
    override fun getDisplayName() = "V"

    override fun getDemoText() = """// Simple example module main
module <MODULE>main</MODULE>

import <MODULE>sqlite</MODULE>

${'$'}if windows {
	<COMPILE_DIRECTIVE>#flag -lgdi32</COMPILE_DIRECTIVE>
	<COMPILE_DIRECTIVE>#include "windows.h"</COMPILE_DIRECTIVE>
}

type <TYPE_ALIAS>Callback</TYPE_ALIAS> = fn ()
pub type <PUBLIC_TYPE_ALIAS>PublicCallback</PUBLIC_TYPE_ALIAS> = fn ()

const <CONSTANT>ide_name</CONSTANT> = 'IntelliJ IDEA'
pub const <PUBLIC_CONSTANT>ide_version</PUBLIC_CONSTANT> = 2022

pub enum <PUBLIC_ENUM>PublicTypes</PUBLIC_ENUM> {
	<ENUM_FIELD>simple</ENUM_FIELD>
	<ENUM_FIELD>complex</ENUM_FIELD>
}

enum <ENUM>PrivateTypes</ENUM> {
	<ENUM_FIELD>simple</ENUM_FIELD>
	<ENUM_FIELD>complex</ENUM_FIELD>
}

// <COMMENT_REFERENCE>IPublic</COMMENT_REFERENCE> interface documentation
pub interface <PUBLIC_INTERFACE>IPublic</PUBLIC_INTERFACE> {
	<INTERFACE_METHOD>method</INTERFACE_METHOD>() <PUBLIC_TYPE_ALIAS>PublicCallback</PUBLIC_TYPE_ALIAS>
}

interface <INTERFACE>IPrivate</INTERFACE> {
	<INTERFACE_METHOD>method</INTERFACE_METHOD>() <TYPE_ALIAS>Callback</TYPE_ALIAS>
}

struct <STRUCT>PrivateHashOwner</STRUCT> {
	<FIELD>hash</FIELD> <PUBLIC_STRUCT>string</PUBLIC_STRUCT>
}

<ATTRIBUTE>[table</ATTRIBUTE>: 'code_storage'<ATTRIBUTE>]</ATTRIBUTE>
pub struct <PUBLIC_STRUCT>PublicCodeStorage</PUBLIC_STRUCT> {
	<FIELD>id</FIELD>   <BUILTIN_TYPE>int</BUILTIN_TYPE>    <ATTRIBUTE>[primary; sql: serial]</ATTRIBUTE>
pub:
	<PUBLIC_FIELD>code</PUBLIC_FIELD> <PUBLIC_STRUCT>string</PUBLIC_STRUCT> <ATTRIBUTE>[nonull]</ATTRIBUTE>
	<PUBLIC_FIELD>hash</PUBLIC_FIELD> <PUBLIC_STRUCT>string</PUBLIC_STRUCT> <ATTRIBUTE>[nonull]</ATTRIBUTE>
mut:
	<MUTABLE_FIELD>count_use</MUTABLE_FIELD> <BUILTIN_TYPE>int</BUILTIN_TYPE>
}

// <DOC_HEADING># Header</DOC_HEADING>
// You can use <DOC_STRONG>**Bold**</DOC_STRONG> or <DOC_EMPHASIS>*Italic*</DOC_EMPHASIS> text.
// Also you can use <DOC_CODE>`code`</DOC_CODE> text.
// <DOC_CODE>```</DOC_CODE>
// <DOC_CODE>assert s.private_method() == 0</DOC_CODE>
// <DOC_CODE>```</DOC_CODE>
// You can use <DOC_LINK>[link](https://vlang.io)</DOC_LINK>.
// And other markdown features like lists.
fn (mut <MUTABLE_RECEIVER>cs</MUTABLE_RECEIVER> <PUBLIC_STRUCT>PublicCodeStorage</PUBLIC_STRUCT>) <FUNCTION>private_method</FUNCTION>() {
	<MUTABLE_RECEIVER>cs</MUTABLE_RECEIVER>.<PUBLIC_FUNCTION>public_method</FUNCTION>()
}

pub fn (mut <MUTABLE_RECEIVER>cs</MUTABLE_RECEIVER> <PUBLIC_STRUCT>PublicCodeStorage</PUBLIC_STRUCT>) <PUBLIC_FUNCTION>public_method</PUBLIC_FUNCTION>() {
	<MUTABLE_RECEIVER>cs</MUTABLE_RECEIVER>.<FUNCTION>private_method</FUNCTION>()
}

<ATTRIBUTE>[deprecated</ATTRIBUTE>: 'use public_method() instead'<ATTRIBUTE>]</ATTRIBUTE>
pub fn (<RECEIVER>cs</RECEIVER> <PUBLIC_STRUCT>PublicCodeStorage</PUBLIC_STRUCT>) <PUBLIC_FUNCTION>immutable_method</PUBLIC_FUNCTION>(<PARAMETER>a</PARAMETER> <BUILTIN_TYPE>int</BUILTIN_TYPE>, mut <MUTABLE_PARAMETER>b</MUTABLE_PARAMETER> <BUILTIN_TYPE>u8</BUILTIN_TYPE>) {
	<PUBLIC_FUNCTION>println</PUBLIC_FUNCTION>(<RECEIVER>cs</RECEIVER>)
}

pub struct <PUBLIC_STRUCT>Array</PUBLIC_STRUCT><<GENERIC_PARAMETER>T</GENERIC_PARAMETER>> {
	<FIELD>data</FIELD> []<GENERIC_PARAMETER>T</GENERIC_PARAMETER>
}

pub fn <PUBLIC_FUNCTION>array_map</PUBLIC_FUNCTION><<GENERIC_PARAMETER>T</GENERIC_PARAMETER>, <GENERIC_PARAMETER>U</GENERIC_PARAMETER>>(<PARAMETER>arr</PARAMETER> []<GENERIC_PARAMETER>T</GENERIC_PARAMETER>, <PARAMETER>cb</PARAMETER> fn (<PARAMETER>el</PARAMETER> <GENERIC_PARAMETER>T</GENERIC_PARAMETER>) <GENERIC_PARAMETER>U</GENERIC_PARAMETER>) []<GENERIC_PARAMETER>U</GENERIC_PARAMETER> {}

__global <GLOBAL_VARIABLE>global_age</GLOBAL_VARIABLE> = 100

/**
 * You can also use block comments.
 */
fn <FUNCTION>main</FUNCTION>() {
	mut <MUTABLE_VARIABLE>code_storage</MUTABLE_VARIABLE> := <PUBLIC_STRUCT>PublicCodeStorage</PUBLIC_STRUCT>{
		<PUBLIC_FIELD>code</PUBLIC_FIELD>: "Hello, World!"
	}
	<PUBLIC_FUNCTION>println</PUBLIC_FUNCTION>(<MUTABLE_VARIABLE>code_storage</MUTABLE_VARIABLE>.<PUBLIC_FIELD>code</PUBLIC_FIELD>)

	<VARIABLE>count_use</VARIABLE> := <MUTABLE_VARIABLE>code_storage</MUTABLE_VARIABLE>.<MUTABLE_FIELD>count_use</MUTABLE_FIELD>

	<VARIABLE>cb</VARIABLE> := fn [<CAPTURED_VARIABLE>count_use</CAPTURED_VARIABLE>, <MUTABLE_CAPTURED_VARIABLE>code_storage</MUTABLE_CAPTURED_VARIABLE>] () {
		<PUBLIC_FUNCTION>print</PUBLIC_FUNCTION>(<CAPTURED_VARIABLE>count_use</CAPTURED_VARIABLE>)
		<PUBLIC_FUNCTION>print</PUBLIC_FUNCTION>(<MUTABLE_CAPTURED_VARIABLE>code_storage</MUTABLE_CAPTURED_VARIABLE>)
	}
	<VARIABLE>cb</VARIABLE>()

	<PUBLIC_FUNCTION>println</PUBLIC_FUNCTION>(<CT_CONSTANT>@FILE</CT_CONSTANT>)
	<PUBLIC_FUNCTION>println</PUBLIC_FUNCTION>(<CT_CONSTANT>@FILE_LINE</CT_CONSTANT>)

	<VARIABLE>pi</VARIABLE> := 3.1415
	<VARIABLE>character</VARIABLE> := `\0`
	<VARIABLE>next_line</VARIABLE> := ${'$'}if windows { "\r\n" } ${'$'}else { "\n" }

	<PUBLIC_FUNCTION>print</PUBLIC_FUNCTION>(<VARIABLE>character</VARIABLE>)
	<PUBLIC_FUNCTION>print</PUBLIC_FUNCTION>(<VARIABLE>next_line</VARIABLE>)
	<PUBLIC_FUNCTION>print</PUBLIC_FUNCTION>(<GLOBAL_VARIABLE>global_age</GLOBAL_VARIABLE>)

	unsafe {
		goto <LABEL>label</LABEL>
	}

<USED_LABEL>label</USED_LABEL>:
	<VARIABLE>database_name</VARIABLE> := 'database'
	<VARIABLE>db</VARIABLE> := <MODULE>sqlite</MODULE>.<PUBLIC_FUNCTION>connect</PUBLIC_FUNCTION>('<STRING_INTERPOLATION>${"\${"}</STRING_INTERPOLATION>database_name<STRING_INTERPOLATION>}</STRING_INTERPOLATION>.sql') or { <PUBLIC_FUNCTION>panic</PUBLIC_FUNCTION>(err) }
	<KEYWORD>sql</KEYWORD> <VARIABLE>db</VARIABLE> {
		<SQL_KEYWORD>create</SQL_KEYWORD> <SQL_KEYWORD>table</SQL_KEYWORD> <PUBLIC_STRUCT>PublicCodeStorage</PUBLIC_STRUCT>
	}

	<VARIABLE>snippets</VARIABLE> := <KEYWORD>sql</KEYWORD> <VARIABLE>db</VARIABLE> {
		<SQL_KEYWORD>select</SQL_KEYWORD> <SQL_KEYWORD>from</SQL_KEYWORD> <PUBLIC_STRUCT>PublicCodeStorage</PUBLIC_STRUCT> <SQL_KEYWORD>where</SQL_KEYWORD> <PUBLIC_FIELD>code</PUBLIC_FIELD> == "Hello, World!"
	}

	if <VARIABLE>snippets</VARIABLE>.<PUBLIC_FIELD>len</PUBLIC_FIELD> == 0 {
		<PUBLIC_FUNCTION>panic</PUBLIC_FUNCTION>('Snippet not found!')
	}
    
    <PUBLIC_FUNCTION>print</PUBLIC_FUNCTION>(<RAW_STRING>r"Exit {0}"</RAW_STRING>)
    <PUBLIC_FUNCTION>print</PUBLIC_FUNCTION>(<C_STRING>c"Exit C{0}"</C_STRING>)
	<PUBLIC_FUNCTION>exit</PUBLIC_FUNCTION>(0)

	<CT_METHOD_CALL>${"$"}compile_error</CT_METHOD_CALL>("error")
}
    """.trimIndent()

    override fun getPriority() = DisplayPriority.KEY_LANGUAGE_SETTINGS
}
