#!/bin/bash
/*bin/false
/usr/bin/env kotlin "$0" -- "$@"
exit #*/
// This is very hacky way workaround for
// not being able to pass arguments to Kotlin script because of kotlinc intercepting them

@file:Repository("https://jcenter.bintray.com")
@file:DependsOn("org.jsoup:jsoup:1.13.1")
@file:DependsOn("com.github.ajalt:clikt:2.6.0")

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File

Wiapack().main(args)

inner class Wiapack : CliktCommand()
{
	private val rootFile: File by argument("FILE", help = "Path to base HTML file to start with",
	                                       completionCandidates = CompletionCandidates.Path).file(mustExist = true,
	                                                                                              canBeDir = false,
	                                                                                              mustBeReadable = true)
	private val outputFile: File? by option("-o", "--output",
	                                        help = "Path to file to write the output to").file(canBeDir = false)

	override fun run()
	{
		val packer = Packer(rootFile)
		val newDocument = packer.pack()

		outputFile?.writeText(newDocument) ?: println(newDocument)
	}
}

inner class Packer(private val rootFile: File)
{
	private val directory: File = rootFile.parentFile

	fun pack(): String
	{
		val document = parseDocument(rootFile)
		packLinks(document)
		packScripts(document)
		return document.toString().unescapeChars()
	}

	private fun parseDocument(file: File) = Jsoup.parse(file, "UTF-8")

	private fun packLinks(element: Element) = packElements(element.getElementsByTag("link")) { transformLink(it) }

	private fun packScripts(element: Element) = packElements(element.getElementsByTag("script")) { transformScript(it) }

	private fun packElements(elements: List<Element>, transform: (Element) -> Element?) = elements.forEach {
		val transformed = transform(it) ?: return@forEach
		val parent = it.parent()
		it.remove()
		parent.appendChild(transformed)
	}

	private fun transformLink(element: Element) = when(element.attr("rel"))
	{
		"stylesheet" -> transformLinkStylesheet(element)
		else -> null
	}

	private fun transformLinkStylesheet(element: Element): Element?
	{
		val href = element.attr("href").takeIf { it.isNotBlank() } ?: return null
		val content = readFile(href)
		return Element("style").appendText("\\n$content")
	}

	private fun transformScript(element: Element): Element?
	{
		val src = element.attr("src").takeIf { it.isNotBlank() } ?: return null
		val content = readFile(src)
		return Element("script").appendText("\\n$content")
	}

	private fun readFile(file: String) = File(directory, file).readText().escapeChars()

	private fun String.escapeChars() = replace("\n", "\\n").replace("\t", "\\t").replace(" ", """\x20""")

	private fun String.unescapeChars() = replace("\\n", "\n").replace("\\t", "\t").replace("""\x20""", " ")
}
