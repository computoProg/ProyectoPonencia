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

            <title>Tipo</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
             <script  language="javascript">
    function confirmDelete()
    {
        return confirm('Esta seguro que desea borrar el rol?');
    }
    </script>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
        <h1>Tipo</h1>
        <h:form>
            <h:panelGrid columns="2">
                <h:outputText value="Tipo:"/>
                <h:outputText value="#{TipoPonencia.tipo.tipo}" title="Tipo" />

                <h:outputText value="Ponencias asociadas:" />
                <h:panelGroup>
                    <h:outputText value="Cantidad de Ponencias: #{TipoPonencia.num_ponencias}"
                                  title="Tipo" />
                    <h:outputText rendered="#{empty TipoPonencia.ponenciasTipo}"
                                  value="(No existe Ponencias)"/>
                    <h:dataTable value="#{TipoPonencia.ponenciasTipo}"
                                 var="item"
                                 border="0"
                                 cellpadding="2"
                                 cellspacing="0"
                                 rowClasses="jsfcrud_odd_row,jsfcrud_even_row"
                                 rules="all" style="border:solid 1px"
                                 rendered="#{not empty TipoPonencia.ponenciasTipo}">

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Nombre"/>
                            </f:facet>
                            <h:outputText value="#{item.nombre}"/>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Descripción"/>
                            </f:facet>
                            <h:outputText value="#{item.descripcion}"/>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="FechaInicial"/>
                            </f:facet>
                            <h:outputText value="#{item.fechaInicial}">
                                <f:convertDateTime pattern="d/M/yyyy" />
                            </h:outputText>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Fecha Final"/>
                            </f:facet>
                            <h:outputText value="#{item.fechaFinal}">
                                <f:convertDateTime pattern="d/M/yyyy" />
                            </h:outputText>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Evento"/>
                            </f:facet>
                            <h:outputText value="#{item.eventoidEvento.nombre}"/>
                        </h:column>

                        <h:column>
                            <f:facet name="header">
                                <h:outputText escape="false" value="&nbsp;"/>
                            </f:facet>

                            <h:outputText value=" "/>
                            <h:commandButton value="Editar" action="#{ponencia.editSetup}" image="../images/editar.png" title="Editar">
                                <f:param name="jsfcrud.currentTipoPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][TipoPonencia.tipo][TipoPonencia.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][ponencia.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="tipo" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.TipoPonenciaController" />
                            </h:commandButton>
                            <h:outputText value=" "/>
                            <h:commandButton value="Borrar" action="#{ponencia.remove(participantes.participantes.cedula, participantes.p.rol.idrol)}" image="../images/delete.png" title="Borrar" onclick="return confirmDelete();">
                                <f:param name="jsfcrud.currentTipoPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][TipoPonencia.tipo][TipoPonencia.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.currentPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][ponencia.converter].jsfcrud_invoke}"/>
                                <f:param name="jsfcrud.relatedController" value="tipo" />
                                <f:param name="jsfcrud.relatedControllerType" value="beans.TipoPonenciaController" />
                            </h:commandButton>
                        </h:column>
                    </h:dataTable>
                </h:panelGroup>

            </h:panelGrid>
            <br />

            <br />
            <br />
            <h:commandButton action="tipoponencia_list" value="Volver">
                <f:param name="jsfcrud.currentTipoPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][TipoPonencia.tipo][TipoPonencia.converter].jsfcrud_invoke}" />
            </h:commandButton>
            <br />


            <br />
            <br />
            <h:commandLink value="Inicio" action="welcome_admin" immediate="true" >
                <f:param name="jsfcrud.currentTipoPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][TipoPonencia.tipo][TipoPonencia.converter].jsfcrud_invoke}" />
            </h:commandLink>
        </h:form>
        </body>
    </html>
</f:view>
