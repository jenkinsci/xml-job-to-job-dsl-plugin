<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xls="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="text" omit-xml-declaration="yes" indent="yes"/>

<xsl:template match="project">job("test") {
	<xsl:apply-templates/>
}</xsl:template>

<xsl:template match="properties|jenkins.model.BuildDiscarderProperty|parameterDefinition|choices"><xsl:apply-templates/></xsl:template>

<xsl:template match="hudson.plugins.buildblocker.BuildBlockerProperty">blockOn("""<xsl:value-of select="./blockingJobs" />""", {
		<xsl:apply-templates/>
})</xsl:template>

<xsl:template match="fromFile|fromMacro|macroFirst">
	<xsl:value-of select="name(.)" />(<xsl:value-of select="." />)
</xsl:template>

<xsl:template match="blockLevel|scanQueueFor|description|macroTemplate">
	<xsl:value-of select="name(.)" />("<xsl:value-of select="." />")
</xsl:template>

<xsl:template match="strategy[@class='hudson.tasks.LogRotator']">
logRotator(<xsl:value-of select="./daysToKeep" />)
</xsl:template>

<xsl:template match="hudson.model.ParametersDefinitionProperty">parameters {
	<xsl:apply-templates/>
}</xsl:template>

<xsl:template match="hudson.model.StringParameterDefinition">stringParam("<xsl:value-of select="./name" />", "<xsl:value-of select="./defaultValue" />", "<xsl:value-of select="./description" />")</xsl:template>

<xsl:template match="hudson.model.BooleanParameterDefinition">booleanParam("<xsl:value-of select="./name" />", "<xsl:value-of select="./defaultValue" />", "<xsl:value-of select="./description" />")</xsl:template>

<xsl:template match="hudson.model.ChoiceParameterDefinition">choiceParam(<xsl:apply-templates select="./choices/a"/>, "<xsl:value-of select="./name" />", "<xsl:value-of select="./description" />")</xsl:template>

<xsl:template match="a[@class='string-array']">[<xsl:for-each select="./string">"<xsl:value-of select="." />"<xsl:if test="position() != last()"><xsl:text>, </xsl:text></xsl:if></xsl:for-each>]</xsl:template>

<xsl:template match="builders">
	steps {
		<xsl:apply-templates />
	}</xsl:template>

<xsl:template match="org.jenkinsci.plugins.buildnameupdater.BuildNameUpdater">buildNameUpdater {
			<xls:apply-templates />
		}</xsl:template>

<xls:template match="hudson.tasks.Shell">
		shell("""<xls:value-of select="." />""")
</xls:template>

<xsl:template match="*">
</xsl:template>

<xsl:strip-space elements="*"/>
</xsl:stylesheet>