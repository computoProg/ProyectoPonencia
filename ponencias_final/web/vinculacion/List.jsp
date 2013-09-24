<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
             <h:form>
                <h:commandLink title="volver al inicio" action="#{participantes.validaWelcome}">
                    <h:graphicImage value="../images/Home.png"/><h:outputText value="inicio"/>
                </h:commandLink> | <h:commandLink  title="cambiar contraseÃ±a" action="#{participantes.go_change}">
                <h:graphicImage value="../images/clave.png"/>           <Strong><h:outputText value="Cambiar contraseña"/>
                </Strong> </h:commandLink> | <h:commandLink  title="Usuario" action="#{participantes.editSetup}">
                <h:graphicImage value="../images/user.png"/>           <Strong><h:outputText value="#{user.nombre}"/>
                </Strong> </h:commandLink> |<h:outputText value=" #{participantes.fecha}"/> <div align="right">
                    <h:commandLink action="#{participantes.logout}" value="(salir)">
                        <h:graphicImage value="../images/exit.png"/>
                    </h:commandLink></div></h:form>
            <title>Lista de vinculaciones</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
                                              <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea borrar la vinculación?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Lista de vinculaciones</h1>
        <h:form styleClass="jsfcrud_list_form">
            <h:outputText escape="false" value="(no existen vinculaciones)<br />" rendered="#{vinculacion.pagingInfo.itemCount == 0}" />
            <h:panelGroup rendered="#{vinculacion.pagingInfo.itemCount > 0}">
                <h:commandButton action="#{vinculacion.prev}" value="Anterior #{vinculacion.pagingInfo.batchSize}" rendered="#{vinculacion.pagingInfo.firstItem >= vinculacion.pagingInfo.batchSize}" image="../images/ant.png"/>&nbsp;
                <h:outputText value="vinculacion #{vinculacion.pagingInfo.firstItem + 1}-#{vinculacion.pagingInfo.lastItem} de #{vinculacion.pagingInfo.itemCount}"/>&nbsp;
                <h:commandButton action="#{vinculacion.next}" value="Sig #{vinculacion.pagingInfo.batchSize}" rendered="#{vinculacion.pagingInfo.lastItem + vinculacion.pagingInfo.batchSize <= vinculacion.pagingInfo.itemCount}" image="../images/sig.png"/>&nbsp;
                <h:commandButton action="#{vinculacion.next}" value="Siguientes #{vinculacion.pagingInfo.itemCount - vinculacion.pagingInfo.lastItem} " image="../images/sig.png"
                               rendered="#{vinculacion.pagingInfo.lastItem < vinculacion.pagingInfo.itemCount && vinculacion.pagingInfo.lastItem + vinculacion.pagingInfo.batchSize > vinculacion.pagingInfo.itemCount}"/>
                <h:dataTable value="#{vinculacion.vinculacionItems}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">
                    <%--<h:column>
                        <f:facet name="header">
                            <h:outputText value="Idvinculacion"/>
                        </f:facet>
                        <h:outputText value="#{item.idvinculacion}"/>
                    </h:column>--%>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Vinculacion"/>
                        </f:facet>
                        <h:outputText value="#{item.vinculacion}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText escape="false" value="&nbsp;"/>
                        </f:facet>
                        <h:commandButton value="Participantes " title="Ver participantes" action="#{vinculacion.detailSetup}">
                                <f:param name="jsfcrud.currentVinculacion" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][vinculacion.converter].jsfcrud_invoke}"/>
                            </h:commandButton>
                        <h:outputText value=" "/>
                        <h:commandButton value="Editar" action="#{vinculacion.editSetup}" image="../images/editar.png" title="Editar">
                            <f:param name="jsfcrud.currentVinculacion" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][vinculacion.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        <h:outputText value=" "/>
                        <h:commandButton value="Borrar" action="#{vinculacion.remove}" image="../images/delete.png" title="Borrar" onclick="return confirmDelete();">
                            <f:param name="jsfcrud.currentVinculacion" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][vinculacion.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                    </h:column>

                </h:dataTable>
            </h:panelGroup>
            <br />
            <h:commandButton action="#{vinculacion.createSetup}" value="Nueva Vinculacion"/>
            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" />


        </h:form>
        </body>
    </html>
</f:view>
