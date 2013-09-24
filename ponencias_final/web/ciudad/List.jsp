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
            <title>Lista de Ciudades</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
    <script  language="javascript">
    function confirmDelete() {
        return confirm('Esta seguro que desea borrar la Ciudad?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Lista de Ciudades</h1>
        <h:form styleClass="jsfcrud_list_form">
            <h:outputText escape="false" value="(no existen Ciudades)<br />" rendered="#{Ciudad.pagingInfo.itemCount == 0}" />
            <h:panelGroup rendered="#{Ciudad.pagingInfo.itemCount > 0}">
               <h:commandButton action="#{Ciudad.prev}" value="Anterior #{Ciudad.pagingInfo.batchSize}" rendered="#{Ciudad.pagingInfo.firstItem >= Ciudad.pagingInfo.batchSize}" image="../ant.png"/>&nbsp;
                <h:outputText value="Ciudad #{Ciudad.pagingInfo.firstItem + 1}-#{Ciudad.pagingInfo.lastItem} de #{Ciudad.pagingInfo.itemCount}"/>&nbsp;
                <h:commandButton action="#{Ciudad.next}" value="Sig #{Ciudad.pagingInfo.batchSize}" rendered="#{Ciudad.pagingInfo.lastItem + Ciudad.pagingInfo.batchSize <= Ciudad.pagingInfo.itemCount}" image="../sig.png"/>&nbsp;
                <h:commandButton action="#{Ciudad.next}" value="Siguientes #{Ciudad.pagingInfo.itemCount - Ciudad.pagingInfo.lastItem} " image="../sig.png"
                               rendered="#{Ciudad.pagingInfo.lastItem < Ciudad.pagingInfo.itemCount && Ciudad.pagingInfo.lastItem + Ciudad.pagingInfo.batchSize > Ciudad.pagingInfo.itemCount}"/>
            <h:dataTable value="#{Ciudad.ciudadItems}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Ciudad"/>
                        </f:facet>
                        <h:outputText value="#{item.nombre}"/>
                    </h:column>
                <%--<h:column>
                        <f:facet name="header">
                            <h:outputText value="Departamento"/>
                        </f:facet>
                        <h:outputText value="#{item.departamentoidDepartamento.nombre}"/>
                    </h:column>
                --%>
                <%--<h:column>
                        <f:facet name="header">
                            <h:outputText value="País"/>
                        </f:facet>
                        <h:outputText value="#{item.departamentoidDepartamento.paisidPais.nombre}"/>
                    </h:column>
                --%>    

                    <h:column>
                        <f:facet name="header">
                            <h:outputText escape="false" value="&nbsp;"/>
                        </f:facet>
                        
                        <%--<h:commandButton value="Eventos"  title="Ver Eventos" action="#{Ciudad.detailSetup}">
                                <f:param name="jsfcrud.currentCiudad" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][Ciudad.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        --%>
                        <h:outputText value=" "/>
                        <h:commandButton value="Editar" action="#{Ciudad.editSetup}" image="../editar.png" title="Editar">
                            <f:param name="jsfcrud.currentCiudad" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][Ciudad.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        <%--<h:outputText value=" "/>
                        <h:commandButton value="Borrar" action="#{Ciudad.remove}"image="../delete.png" title="Borrar" onclick="return confirmDelete();">
                            <f:param name="jsfcrud.currentCiudad" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][Ciudad.converter].jsfcrud_invoke}"/>
</h:commandButton>--%>
                    </h:column>

                </h:dataTable>
            </h:panelGroup>
            <br />
            <h:commandButton action="#{Ciudad.createSetup}" value="Ingresar Ciudad"/>
            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" />
        </h:form>
        </body>
    </html>
</f:view>
