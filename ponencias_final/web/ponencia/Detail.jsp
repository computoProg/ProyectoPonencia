<%@page contentType="text/html"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>

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
            <title>Detalle Ponencia</title>

            <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
        <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
            
            <h:outputText value="HOME > Consultar ponencia - Detalle Ponencia"/>
            
        <h1>Detalle Ponencia: <h:outputText value="#{ponencia.ponencia.nombre}"/></h1>
        <h:form>
            
            <h:panelGrid columns="2">
                
                <h:outputText value="Cédula:" style="font-weight:bold" rendered="#{participantes.p.rol.idrol != 2}" />
                <h:outputText value="#{ponencia.ponencia.participantesCedula.cedula}" rendered="#{participantes.p.rol.idrol != 2}" />
                
                <h:outputText value="Fecha de la ponencia: " style="font-weight:bold"/>
                <h:outputText value="#{ponencia.ponencia.fechaInicial}">
                    <f:convertDateTime pattern="d/MM/yyyy"/>
                </h:outputText>
                
                <%--<h:outputText value="Pais:" style="font-weight:bold"/>
                <h:outputText value="#{ponencia.pais}"/>
                
                <h:outputText value="Departamento:" style="font-weight:bold"/>
                <h:outputText value="#{ponencia.departamento}"/>
                --%>
                
                <h:outputText value="Ciudad:" style="font-weight:bold"/>
                <h:outputText value="#{ponencia.ponencia.ciudadidCiudad.nombre}"/>
                
                <h:outputText value="Tipo de Ponencia:" style="font-weight:bold"/>
                <h:outputText value="#{ponencia.ponencia.tipoIdtipo.tipo}"/>
                
                <h:outputText value="Apoyo:" style="font-weight:bold"/>
                <h:outputText value="#{ponencia.ponencia.apoyoIdapoyo.apoyo}"/>
                
                <h:outputText value="Evento:" style="font-weight:bold"/>
                <h:outputText value="#{ponencia.ponencia.evento}"/>
                
                <h:outputText value="Proyecto:" style="font-weight:bold"/>
                <h:outputText value="#{ponencia.ponencia.proyectoIdproyecto.nombre}"/>
                
                <h:outputText value="Descripción:" style="font-weight:bold"/>
                <h:outputText value="#{ponencia.ponencia.descripcion}"/>

                
            </h:panelGrid>
            
            
            <br />
            <h:commandButton action="#{ponencia.editSetup}" value="Editar Ponencia">
                <f:param name="jsfcrud.currentPonencia" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][ponencia.ponencia][ponencia.converter].jsfcrud_invoke}"/>
            </h:commandButton>
            
            <br /><br />
            <h:commandLink value="Volver a listar las Ponencias" action="#{ponencia.listSetup(participantes.p.rol.idrol, participantes.p.cedula)}" immediate="true" />
            
            <br /><br /><br /><br /><br /><br />
            
            <h:commandButton action="#{ponencia.remove(participantes.participantes.cedula, participantes.p.rol.idrol)}" 
                             value="Eliminar Ponencia">
                <%--onclick="return confirmDelete();">--%>
                <%--value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][ponencia.ponencia][ponencia.converter].jsfcrud_invoke}"/>--%>
                <f:param name="jsfcrud.currentPonencia"                          
                         value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][ponencia.ponencia][ponencia.converter].jsfcrud_invoke}"/>
            </h:commandButton>
        </h:form>
            
        </body>
    </html>
</f:view>
