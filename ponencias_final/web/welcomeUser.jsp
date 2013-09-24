<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri = "http://richfaces.org/rich" %>
<%@ taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%--
    This file is an entry point for JavaServer Faces application.
--%>
<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <h:form>
                <h:graphicImage value="/Home.png"/> <h:outputText value="Inicio"/>                
                <h:commandLink  title="Cambiar Contraseña" action="#{participantes.go_change}">
                    <h:graphicImage value="../clave.png" rendered="true"/>
                    <Strong>
                        <h:outputText value="Cambiar contraseña"/>
                    </Strong>
                </h:commandLink> |      
                <h:commandLink  title="Usuario" action="#{participantes.editSetup}">
                    <h:graphicImage value="../user.png"/>           
                    <Strong>
                        <h:outputText value="#{user.nombre}"/>
                    </Strong> 
                </h:commandLink> |
                <h:outputText value=" #{participantes.fecha}"/>
                <div align="right">
                    <h:commandLink action="#{participantes.logout}" value="(salir)">
                        <h:graphicImage value="../exit.png"/>
                    </h:commandLink>
                </div>
            </h:form>
        <link rel="stylesheet" type="text/css" href="/ponencias/faces/jsfcrud.css" />
        </head>
        <body>
            <h:panelGroup id="messagePanel" layout="block">
            <h:messages errorStyle="color: red" infoStyle="color: green" layout="table"/>
        </h:panelGroup>
            <h1><h:outputText value="HOME USUARIO"/></h1>
               
                <h:form>
                    <h:commandButton action="#{ponencia.listSetup(2,participantes.p.cedula)}"  value="Mis Ponencias">                        
                        <f:param name="jsfcrud.currentParticipantes" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][participantes.converter].jsfcrud_invoke}"/>
                    </h:commandButton>
                    <h:commandButton action="#{ponencia.createSetup}" value="Ingresar Ponencia">
                        <f:param name="jsfcrud.currentParticipantes" value="#{jsfcrud_class['beans.util.JsfUtil'].jsfcrud_method['getAsConvertedString'][item][participantes.converter].jsfcrud_invoke}"/>
                    </h:commandButton>
                </h:form>

                <h:form>
                    <h:commandButton action="#{participantes.detailSetup}" value="Mis datos de cuenta"/>
                </h:form>
            <br/>
              <h:form>
                  <h:commandButton action="#{participantes.logout}" value="Salir"/>
              </h:form>
               
        </body>
    </html>
</f:view>
