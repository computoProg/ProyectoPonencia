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
            <title>Lista de tipos</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
                                              <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea borrar el tipo?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Lista de tipos</h1>
        <h:form styleClass="jsfcrud_list_form">
            <h:outputText escape="false" value="(no existen tipos)<br />" rendered="#{TipoPonencia.pagingInfo.itemCount == 0}" />
            <h:panelGroup rendered="#{TipoPonencia.pagingInfo.itemCount > 0}">
                <h:commandButton action="#{TipoPonencia.prev}" value="anterior #{TipoPonencia.pagingInfo.batchSize}" rendered="#{TipoPonencia.pagingInfo.firstItem >= TipoPonencia.pagingInfo.batchSize}" image="../images/ant.png"/>&nbsp;

                <h:outputText value="tipo #{TipoPonencia.pagingInfo.firstItem + 1}-#{TipoPonencia.pagingInfo.lastItem} de #{TipoPonencia.pagingInfo.itemCount}"/>&nbsp;
                <h:commandButton action="#{TipoPonencia.next}" value="Sig #{TipoPonencia.pagingInfo.batchSize}" rendered="#{TipoPonencia.pagingInfo.lastItem + TipoPonencia.pagingInfo.batchSize <= TipoPonencia.pagingInfo.itemCount}" image="../images/sig.png"/>&nbsp;
                <h:commandButton action="#{TipoPonencia.next}" value="Proximos #{TipoPonencia.pagingInfo.itemCount - TipoPonencia.pagingInfo.lastItem}"
                                 rendered="#{TipoPonencia.pagingInfo.lastItem <TipoPonencia.pagingInfo.itemCount && TipoPonencia.pagingInfo.lastItem + TipoPonencia.pagingInfo.batchSize > TipoPonencia.pagingInfo.itemCount}" image="../images/sig.png"/>
                <h:dataTable value="#{TipoPonencia.tipoItems}" var="item" border="0" cellpadding="2" cellspacing="0" rowClasses="jsfcrud_odd_row,jsfcrud_even_row" rules="all" style="border:solid 1px">

                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Tipo"/>
                        </f:facet>
                        <h:outputText value="#{item.tipo}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText escape="false" value="&nbsp;"/>
                        </f:facet>
                        <h:commandButton value="Ponencias" title="Ver Ponencias" action="#{TipoPonencia.detailSetup}">
                                <f:param name="jsfcrud.currentTipoPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][TipoPonencia.converter].jsfcrud_invoke}"/>
                            </h:commandButton>
                        <h:outputText value=" "/>
                        <h:commandButton value="Editar" action="#{TipoPonencia.editSetup}" image="../images/editar.png" title="Editar">
                            <f:param name="jsfcrud.currentTipoPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][TipoPonencia.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                        <h:outputText value=" "/>
                        <h:commandButton value="Borrar" action="#{TipoPonencia.remove}" image="../images/delete.png" title="Borrar" onclick="return confirmDelete();">
                            <f:param name="jsfcrud.currentTipoPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][TipoPonencia.converter].jsfcrud_invoke}"/>
                        </h:commandButton>
                    </h:column>

                </h:dataTable>
            </h:panelGroup>
            <br />
            <h:commandButton action="#{TipoPonencia.createSetup}" value="Nuevo Tipo"/>
            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" />
        </h:form>
        </body>
    </html>
</f:view>
