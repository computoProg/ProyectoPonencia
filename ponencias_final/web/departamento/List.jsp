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
                    <h:graphicImage value="../Home.png"/><h:outputText value="inicio"/>
                </h:commandLink> | <h:commandLink  title="cambiar contraseÃ±a" action="#{participantes.go_change}">
                <h:graphicImage value="../clave.png"/>           <Strong><h:outputText value="Cambiar contraseña"/>
                </Strong> </h:commandLink> | <h:commandLink  title="Usuario" action="#{participantes.editSetup}">
                <h:graphicImage value="../user.png"/>           <Strong><h:outputText value="#{user.nombre}"/>
                </Strong> </h:commandLink> |<h:outputText value=" #{participantes.fecha}"/> <div align="right">
                    <h:commandLink action="#{participantes.logout}" value="(salir)">
                        <h:graphicImage value="../exit.png"/>
                    </h:commandLink></div></h:form>
            <title>Lista de Departamentos</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
                                              <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea borrar la Ciudad?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Lista de Departamentos</h1>
        <h:form styleClass="jsfcrud_list_form">
            <h:outputText escape="false" value="(no existen Departamentos)<br />" rendered="#{Departamento.pagingInfo.itemCount == 0}" />
            <h:panelGroup rendered="#{Departamento.pagingInfo.itemCount > 0}">
               <h:commandButton action="#{Departamento.prev}" value="Anterior #{Departamento.pagingInfo.batchSize}" rendered="#{Departamento.pagingInfo.firstItem >= Departamento.pagingInfo.batchSize}" image="../ant.png"/>&nbsp;
                <h:outputText value="Departamento #{Departamento.pagingInfo.firstItem + 1}-#{Departamento.pagingInfo.lastItem} de #{Departamento.pagingInfo.itemCount}"/>&nbsp;
                <h:commandButton action="#{Departamento.next}" value="Sig #{Departamento.pagingInfo.batchSize}" rendered="#{Departamento.pagingInfo.lastItem + Departamento.pagingInfo.batchSize <= Departamento.pagingInfo.itemCount}" image="../sig.png"/>&nbsp;
                <h:commandButton action="#{Departamento.next}" value="Siguientes #{Departamento.pagingInfo.itemCount - Departamento.pagingInfo.lastItem} " image="../sig.png"
                               rendered="#{Departamento.pagingInfo.lastItem < Departamento.pagingInfo.itemCount && Departamento.pagingInfo.lastItem + Departamento.pagingInfo.batchSize > Departamento.pagingInfo.itemCount}"/>
                <h:dataTable value="#{Departamento.departamentoItems}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Departamento"/>
                        </f:facet>
                        <h:outputText value="#{item.nombre}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Pais"/>
                        </f:facet>
                        <h:outputText value="#{item.paisidPais.nombre}"/>
                    </h:column>

                    <h:column>
                        <f:facet name="header">
                            <h:outputText escape="false" value="&nbsp;"/>
                        </f:facet>
                        <h:commandButton value="Ciudades"  title="Ver Ciudades" action="#{Departamento.detailSetup}">
                                <f:param name="jsfcrud.currentDepartamento" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][Departamento.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        <h:outputText value=" "/>
                        <h:commandButton value="Editar" action="#{Departamento.editSetup}" image="../editar.png" title="Editar">
                            <f:param name="jsfcrud.currentDepartamento" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][Departamento.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        <h:outputText value=" "/>
                        <h:commandButton value="Borrar" action="#{Departamento.remove}"image="../delete.png" title="Borrar" onclick="return confirmDelete();">
                            <f:param name="jsfcrud.currentDepartamento" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][Departamento.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                    </h:column>

                </h:dataTable>
            </h:panelGroup>
            <br />
            <h:commandButton action="#{Departamento.createSetup}" value="Ingresar Departamento"/>
            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" />
        </h:form>
        </body>
    </html>
</f:view>
